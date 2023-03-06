package com.smartsheet.api.internal.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);

        int tryCount = 0;
        while (!response.isSuccessful() && tryCount < 3) {
            tryCount++;

            response.close();
            response = chain.proceed(request);
        }

        return response;
    }
}
