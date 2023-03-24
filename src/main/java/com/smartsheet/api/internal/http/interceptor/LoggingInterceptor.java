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

import com.smartsheet.api.internal.http.RequestAndResponseData;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class LoggingInterceptor implements Interceptor {
    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Long requestDuration = response.receivedResponseAtMillis() - response.sentRequestAtMillis();

        logger.info(
                "{} {}, Response Code:{}, Request completed in {} ms",
                request.method(),
                request.url(),
                response.code(),
                requestDuration
        );

        if (response.code() != 200) {
            logger.warn("{}", RequestAndResponseData.of(request, response, Set.of()));
        } else {
            logger.debug("{}", RequestAndResponseData.of(request, response, Set.of()));
        }

        return response;
    }
}
