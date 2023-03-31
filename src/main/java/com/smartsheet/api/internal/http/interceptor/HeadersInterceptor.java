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

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HeadersInterceptor implements Interceptor {

    private String accessToken;
    private String assumedUser;
    private String changeAgent;
    private String userAgent;

    public HeadersInterceptor(String authToken, String assumedUser, String changeAgent, String userAgent) {
        this.accessToken = authToken;
        this.assumedUser = assumedUser;
        this.changeAgent = changeAgent;
        this.userAgent = userAgent;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAssumedUser(String assumedUser) {
        this.assumedUser = assumedUser;
    }

    public void setChangeAgent(String changeAgent) {
        this.changeAgent = changeAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json");

        if (assumedUser != null) {
            builder.addHeader("Assume-User", URLEncoder.encode(assumedUser, StandardCharsets.UTF_8));
        }

        if (changeAgent != null) {
            builder.addHeader("Smartsheet-Change-Agent", URLEncoder.encode(changeAgent, StandardCharsets.UTF_8));
        }

        if (userAgent != null) {
            builder.addHeader("User-Agent", userAgent);
        }

        return chain.proceed(builder.build());
    }
}
