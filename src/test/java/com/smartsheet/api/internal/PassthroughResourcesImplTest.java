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

package com.smartsheet.api.internal;

import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PassthroughResourcesImplTest extends ResourcesImplBase {

    private PassthroughResourcesImpl passthroughResources;

    @BeforeEach
    public void setUp() throws Exception {
        passthroughResources = new PassthroughResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetRequest_happyPath() throws SmartsheetException {
        server.setResponseBody("{\"resultCode\":0,\"message\":\"SUCCESS\"}");
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        String response = passthroughResources.getRequest("/sheets/rows", params);
        assertThat(response).isNotBlank();
    }

    @Test
    void testGetRequest_exception() {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        server.setStatus(400);
        server.setResponseBody("{\"errorCode\":1032,\"message\":\"Something went wrong\"}");
        assertThatThrownBy(() -> {
            passthroughResources.getRequest("/sheets/rows", params);
        })
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Something went wrong");
    }

    @Test
    void testPostRequest_happyPath() throws SmartsheetException {
        server.setResponseBody("{\"resultCode\":0,\"message\":\"SUCCESS\"}");
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        String payload = "some body here";
        String response = passthroughResources.postRequest("/sheets/rows", payload, params);
        assertThat(response).isNotBlank();
    }

    @Test
    void testPostRequest_exception() {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        String payload = "some body here";
        server.setStatus(400);
        server.setResponseBody("{\"errorCode\":1032,\"message\":\"Something went wrong\"}");
        assertThatThrownBy(() -> {
            passthroughResources.postRequest("/sheets/rows", payload, params);
        })
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Something went wrong");
    }

    @Test
    void testPutRequest_happyPath() throws SmartsheetException {
        server.setResponseBody("{\"resultCode\":0,\"message\":\"SUCCESS\"}");
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        String payload = "some body here";
        String response = passthroughResources.putRequest("/sheets/rows", payload, params);
        assertThat(response).isNotBlank();
    }

    @Test
    void testPutRequest_exception() {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        String payload = "some body here";
        server.setStatus(400);
        server.setResponseBody("{\"errorCode\":1032,\"message\":\"Something went wrong\"}");
        assertThatThrownBy(() -> {
            passthroughResources.putRequest("/sheets/rows", payload, params);
        })
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Something went wrong");
    }

    @Test
    void testPostRequest_nullPayload_throwsException() {
        server.setResponseBody("{\"resultCode\":0,\"message\":\"SUCCESS\"}");
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        String payload = null;
        assertThatThrownBy(() -> {
            passthroughResources.postRequest("/sheets/rows", payload, params);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testPutRequest_nullPayload_throwsException() {
        server.setResponseBody("{\"resultCode\":0,\"message\":\"SUCCESS\"}");
        Map<String, Object> params = new HashMap<>();
        params.put("sheetId", 1234L);
        params.put("rowId", 4567L);
        String payload = null;
        assertThatThrownBy(() -> {
            passthroughResources.putRequest("/sheets/rows", payload, params);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDeleteRequest_happyPath() throws SmartsheetException {
        server.setResponseBody("{\"resultCode\":0,\"message\":\"SUCCESS\"}");
        String response = passthroughResources.deleteRequest("/sheets/1234/rows/4567");
        assertThat(response).isNotBlank();
    }

    @Test
    void testDeleteRequest_exception() {
        server.setStatus(400);
        server.setResponseBody("{\"errorCode\":1032,\"message\":\"Something went wrong\"}");
        assertThatThrownBy(() -> {
            passthroughResources.deleteRequest("/sheets/1234/rows/4567");
        })
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Something went wrong");
    }

}
