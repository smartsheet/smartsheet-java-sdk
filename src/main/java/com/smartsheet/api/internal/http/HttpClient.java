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

package com.smartsheet.api.internal.http;

import java.io.Closeable;

/**
 * This interface defines methods to make an HTTP request.
 * <p>
 * Thread Safety: Implementation of this interface must be thread safe.
 */
public interface HttpClient extends Closeable {
    /**
     * Make an HTTP request and return the response.
     * <p>
     * Parameters: - request : the HTTP request
     * <p>
     * Returns: the HTTP response
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - HttpClientException : if there is any other
     * error occurred during the operation
     *
     * @param request the request
     * @return the http response
     * @throws HttpClientException the http client exception
     */
    HttpResponse request(HttpRequest request) throws HttpClientException;

    /**
     * Release connection.
     */
    void releaseConnection();
}
