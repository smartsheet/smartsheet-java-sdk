/*
 * Copyright (C) 2025 Smartsheet
 *
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
 */

package com.smartsheet.api.resilience4j;

import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.SmartsheetException;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.CheckedFunction0;

import java.time.Duration;

/**
 * thin wrapper around Resilience4J
 */
public class RetryUtil {
    // 1s between attempts, only retry on ResourceNotFoundException, fail after 3 attempts
    private static final RetryRegistry RETRY_REGISTRY = RetryRegistry.of(RetryConfig.custom()
            .maxAttempts(3)
            .waitDuration(Duration.ofMillis(1000))
            .retryExceptions(ResourceNotFoundException.class)
            .failAfterMaxAttempts(true)
            .build());

    /**
     * a version of Res4J's CheckedFunction0 that throws SmartsheetException (i.e., basically all the API methods)
     */
    public interface SmartsheetCheckedFunction<R> extends CheckedFunction0<R> {
        @Override
        R apply() throws SmartsheetException;
    }

    /**
     * retry the provided method until is succeeds or 3 attempts are exceeded.
     *
     * @param callback is the API code to be invoked
     * @param <R>      the type of return value
     * @return the value of the first successful API call
     * @throws SmartsheetException if we fail to successfully call the {@code callback}
     * @throws RuntimeException    if any non-SmartsheetException is thrown (i.e., something really really bad)
     */
    public static <R> R callWithRetry(SmartsheetCheckedFunction<R> callback) throws SmartsheetException {
        try {
            return RETRY_REGISTRY.retry("").executeCheckedSupplier(callback);
        } catch (Throwable t) {
            if (t instanceof SmartsheetException) {
                throw (SmartsheetException) t;
            }
            // no point in wrapping a RuntimeEx in another RuntimeEx, is there?
            throw (t instanceof RuntimeException) ? (RuntimeException) t : new RuntimeException(t);
        }
    }
}
