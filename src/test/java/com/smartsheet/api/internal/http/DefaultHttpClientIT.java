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

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Note this is an IT test because at least one of the tests requires an internet connection
class DefaultHttpClientIT {
    private final HttpClient client = new DefaultHttpClient();

    @Test
    void testRequest() throws HttpClientException, URISyntaxException {
        // Null Argument
        assertThatThrownBy(() -> client.request(null))
                .isInstanceOf(IllegalArgumentException.class);

        HttpRequest request = new HttpRequest();

        // No URL in request
        assertThatThrownBy(() -> client.request(request))
                .isInstanceOf(IllegalArgumentException.class);

        // Test each http method
        // Note this requires an internet connection
        request.setUri(new URI("http://google.com"));
        request.setMethod(HttpMethod.GET);
        client.request(request);
        client.releaseConnection();

        request.setMethod(HttpMethod.POST);
        client.request(request);
        client.releaseConnection();

        request.setMethod(HttpMethod.PUT);
        client.request(request);
        client.releaseConnection();

        request.setMethod(HttpMethod.DELETE);
        client.request(request);
        client.releaseConnection();

        // Test request with set headers and http entity but a null content
        Map<String, String> headers = new HashMap<>();
        headers.put("name", "value");
        HttpEntity entity = new HttpEntity();
        entity.setContentType("text/html; charset=ISO-8859-4");
        request.setEntity(entity);
        request.setHeaders(headers);
        request.setMethod(HttpMethod.POST);
        client.request(request);
        client.releaseConnection();

        // Test request with set headers and http entity and some content
        entity.setContent(new ByteArrayInputStream("Hello World!".getBytes()));
        request.setEntity(entity);
        client.request(request);
        client.releaseConnection();

        // Test Client Protocol Exception by passing a second content-length
        headers.put("Content-Length", "10");
        request.setHeaders(headers);
        assertThatThrownBy(() -> client.request(request))
                .isInstanceOf(HttpClientException.class);
        client.releaseConnection();
        headers.remove("Content-Length");
        request.setHeaders(headers);

        // Test IOException
        request.setUri(new URI("http://bad.domain"));
        assertThatThrownBy(() -> client.request(request))
                .isInstanceOf(HttpClientException.class);
        client.releaseConnection();
    }
}
