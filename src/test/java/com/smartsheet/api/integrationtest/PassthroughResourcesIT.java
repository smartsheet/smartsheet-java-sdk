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

package com.smartsheet.api.integrationtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.resilience4j.RetryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PassthroughResourcesIT extends ITResourcesImpl {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    Smartsheet smartsheet;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testPassthroughMethods() throws SmartsheetException, IOException {
        String payload =
                "{\"name\": \"my new sheet\"," +
                        "\"columns\": [" +
                        "{\"title\": \"Favorite\", \"type\": \"CHECKBOX\", \"symbol\": \"STAR\"}," +
                        "{\"title\": \"Primary Column\", \"primary\": true, \"type\": \"TEXT_NUMBER\"}" +
                        "]" +
                        "}";
        String jsonResponse = smartsheet.passthroughResources().postRequest("sheets", payload, null);
        JsonNode root = MAPPER.readTree(jsonResponse);
        assertThat(root.get("message").asText()).isEqualTo("SUCCESS");
        JsonNode result = root.get("result");
        assertThat(result.get("id").asLong()).isNotNull();
        long id = result.get("id").asLong();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("include", "objectValue");
        jsonResponse = RetryUtil.callWithRetry(
                () -> smartsheet.passthroughResources().getRequest("sheets/" + id, parameters));
        root = MAPPER.readTree(jsonResponse);
        assertThat(root.get("name").asText()).isEqualTo("my new sheet");

        payload = "{\"name\": \"my new new sheet\"}";
        jsonResponse = smartsheet.passthroughResources().putRequest("sheets/" + id, payload, null);
        root = MAPPER.readTree(jsonResponse);
        assertThat(root.get("message").asText()).isEqualTo("SUCCESS");
        result = root.get("result");
        assertThat(result.get("name").asText()).isEqualTo("my new new sheet");

        jsonResponse = smartsheet.passthroughResources().deleteRequest("sheets/" + id);
        root = MAPPER.readTree(jsonResponse);
        assertThat(root.get("message").asText()).isEqualTo("SUCCESS");
    }
}
