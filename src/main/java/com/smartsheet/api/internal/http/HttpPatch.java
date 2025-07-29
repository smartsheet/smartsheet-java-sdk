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

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * HTTP PATCH method.
 * <p>
 * The HTTP PATCH method is defined in <a href="https://tools.ietf.org/html/rfc5789">RFC 5789</a>:
 * The PATCH method requests that a set of changes described in the
 * request entity be applied to the resource identified by the Request-URI.
 * </p>
 */
public class HttpPatch extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "PATCH";

    /**
     * Constructor with URI.
     *
     * @param uri the URI
     */
    public HttpPatch(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * Constructor with String URI.
     *
     * @param uri the URI
     */
    public HttpPatch(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
