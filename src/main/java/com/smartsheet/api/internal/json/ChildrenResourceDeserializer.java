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

package com.smartsheet.api.internal.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.Sight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for children resources that deserializes each item based
 * on its resourceType property.
 * Items can be deserialized as Sheet, Folder, Report, or Sight objects.
 */
public class ChildrenResourceDeserializer extends JsonDeserializer<List<Object>> {

    @Override
    public List<Object> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        List<Object> children = new ArrayList<>();

        if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
            // Use the same ObjectMapper from the deserialization context to maintain
            // configuration
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();

            while (jp.nextToken() != JsonToken.END_ARRAY) {
                if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
                    JsonNode node = jp.readValueAsTree();
                    JsonNode resourceTypeNode = node.get("resourceType");

                    if (resourceTypeNode != null && resourceTypeNode.isTextual()) {
                        String resourceType = resourceTypeNode.asText().toLowerCase();
                        Object child = null;

                        try {
                            switch (resourceType) {
                                case "sheet":
                                    child = mapper.treeToValue(node, Sheet.class);
                                    break;
                                case "folder":
                                    child = mapper.treeToValue(node, Folder.class);
                                    break;
                                case "report":
                                    child = mapper.treeToValue(node, Report.class);
                                    break;
                                case "sight":
                                    child = mapper.treeToValue(node, Sight.class);
                                    break;
                                default:
                                    // If a new resource type is introduced that this version of the SDK doesn't
                                    // support,
                                    // fall back to a generic object representation
                                    child = mapper.treeToValue(node, Object.class);
                                    break;
                            }
                        } catch (Exception e) {
                            // If deserialization fails, fall back to generic object
                            child = mapper.treeToValue(node, Object.class);
                        }

                        if (child != null) {
                            children.add(child);
                        }
                    } else {
                        // No resourceType property found, add as generic object
                        Object child = mapper.treeToValue(node, Object.class);
                        children.add(child);
                    }
                }
            }
        }

        return children;
    }
}
