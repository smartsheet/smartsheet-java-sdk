package com.smartsheet.api.internal.http.interceptor;

/*
 * #[license]
 * Smartsheet Java SDK
 * %%
 * Copyright (C) 2014 - 2023 Smartsheet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * %[license]
 */

import com.smartsheet.api.Trace;
import com.smartsheet.api.internal.http.HttpEntitySnapshot;
import com.smartsheet.api.internal.http.HttpResponse;
import com.smartsheet.api.internal.http.RequestAndResponseData;
import com.smartsheet.api.internal.json.JsonSerializer;
import com.smartsheet.api.models.Error;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.methods.HttpRequestBase;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class RetryInterceptor implements Interceptor {

    private final JsonSerializer jsonSerializer;
    private long maxRetryTimeMillis = 15000;
    private static final Logger logger = LoggerFactory.getLogger(RetryInterceptor.class);

    /** to avoid creating new sets for each call (we use Sets for practical and perf reasons) */
    private static final Set<Trace> REQUEST_RESPONSE_SUMMARY = Collections.unmodifiableSet(new HashSet<Trace>(
            Arrays.asList(Trace.RequestHeaders, Trace.RequestBodySummary, Trace.ResponseHeaders, Trace.ResponseBodySummary)));

    private static final Set<Trace> REQUEST_RESPONSE = Collections.unmodifiableSet(new HashSet<Trace>(
            Arrays.asList(Trace.RequestHeaders, Trace.RequestBody, Trace.ResponseHeaders, Trace.ResponseBody)));

    public RetryInterceptor(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public void setMaxRetryTimeMillis(long maxRetryTimeMillis) {
        this.maxRetryTimeMillis = maxRetryTimeMillis;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        int tryCount = 0;
        while (!response.isSuccessful() && shouldRetry(response, tryCount, totalTime)) {
            tryCount++;

            response.close();
            response = chain.proceed(request);
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
        }

        return response;
    }

    private boolean shouldRetry(Response response, int previousAttempts, long totalElapsedTimeMillis) {
        if (response.body() == null) {
            return true;
        }

        Error error;
        try {
            error = jsonSerializer.deserialize(Error.class, response.body().byteStream());
        } catch (IOException exception) {
            return false;
        }

        switch(error.getErrorCode()) {
            case 4001: /** Smartsheet.com is currently offline for system maintenance. Please check back again shortly. */
            case 4002: /** Server timeout exceeded. Request has failed */
            case 4003: /** Rate limit exceeded. */
            case 4004: /** An unexpected error has occurred. Please retry your request.
             * If you encounter this error repeatedly, please contact api@smartsheet.com for assistance. */
                break;
            default:
                return false;
        }

        long backoffMillis = calcBackoff(previousAttempts, totalElapsedTimeMillis, error);
        if(backoffMillis < 0)
            return false;

        logger.info("HttpError StatusCode=" + response.code() + ": Retrying in " + backoffMillis + " milliseconds");
        try {
            Thread.sleep(backoffMillis);
        }
        catch (InterruptedException e) {
            logger.warn("sleep interrupted", e);
            return false;
        }
        return true;
    }

    /**
     * The backoff calculation routine. Uses exponential backoff. If the maximum elapsed time
     * has expired, this calculation returns -1 causing the caller to fall out of the retry loop.
     *
     * @param previousAttempts
     * @param totalElapsedTimeMillis
     * @param error
     * @return -1 to fall out of retry loop, positive number indicates backoff time
     */
    public long calcBackoff(int previousAttempts, long totalElapsedTimeMillis, Error error) {

        long backoffMillis = (long)(Math.pow(2, previousAttempts) * 1000) + new Random().nextInt(1000);

        if(totalElapsedTimeMillis + backoffMillis > maxRetryTimeMillis) {
            logger.info("Elapsed time " + totalElapsedTimeMillis + " + backoff time " + backoffMillis +
                    " exceeds max retry time " + maxRetryTimeMillis + ", exiting retry loop");
            return -1;
        }
        return backoffMillis;
    }
}
