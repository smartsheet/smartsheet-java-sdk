/*
* Copyright (C) 2024 Smartsheet
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PassthroughResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testPassthroughMethods() throws SmartsheetException, IOException {
        Long id = 0L;
        String payload =
                "{\"name\": \"my new sheet\"," +
                        "\"columns\": [" +
                        "{\"title\": \"Favorite\", \"type\": \"CHECKBOX\", \"symbol\": \"STAR\"}," +
                        "{\"title\": \"Primary Column\", \"primary\": true, \"type\": \"TEXT_NUMBER\"}" +
                        "]" +
                        "}";
        String jsonResponse = smartsheet.passthroughResources().postRequest("sheets", payload, null);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            assertThat(root.get("message").asText()).isEqualTo("SUCCESS");
            JsonNode result = root.get("result");
            assertThat(result.get("id").asLong()).isNotNull();
            id = result.get("id").asLong();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("include", "objectValue");
        jsonResponse = smartsheet.passthroughResources().getRequest("sheets/" + id, parameters);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            assertThat(root.get("name").asText()).isEqualTo("my new sheet");
        } catch (IOException e) {
            e.printStackTrace();
        }

        payload = "{\"name\": \"my new new sheet\"}";
        jsonResponse = smartsheet.passthroughResources().putRequest("sheets/" + id, payload, null);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            assertThat(root.get("message").asText()).isEqualTo("SUCCESS");
            JsonNode result = root.get("result");
            assertThat(result.get("name").asText()).isEqualTo("my new new sheet");
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonResponse = smartsheet.passthroughResources().deleteRequest("sheets/" + id);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            assertThat(root.get("message").asText()).isEqualTo("SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
