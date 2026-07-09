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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.smartsheet.api.models.CurrentUserObjectValue;
import com.smartsheet.api.models.ObjectValue;
import com.smartsheet.api.models.ReportFilterObjectValue;

import java.io.IOException;

/**
 * Deserializes filter values typed as {@link ReportFilterObjectValue} in
 * {@link com.smartsheet.api.models.ReportFilterCriterion}.
 * <p>
 * Filter values arrive either as raw JSON primitives ({@code "Test"}, {@code 42}) or as
 * discriminated objects ({@code {"objectType":"DATE",...}}, {@code {"objectType":"CURRENT_USER",...}}).
 * The report-filter-specific CURRENT_USER case is handled here; all other shapes are delegated
 * to {@link ObjectValueDeserializer}, whose results ({@link com.smartsheet.api.models.StringObjectValue},
 * {@link com.smartsheet.api.models.NumberObjectValue}, {@link com.smartsheet.api.models.DateObjectValue})
 * also implement {@link ReportFilterObjectValue}.
 */
public class ReportFilterObjectValueDeserializer extends JsonDeserializer<ReportFilterObjectValue> {

    private final ObjectValueDeserializer objectValueDeserializer = new ObjectValueDeserializer();

    @Override
    public ReportFilterObjectValue deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();

        if (node.isObject() && "CURRENT_USER".equals(node.path("objectType").asText())) {
            CurrentUserObjectValue currentUser = new CurrentUserObjectValue();
            JsonNode value = node.get("value");
            if (value != null && !value.isNull()) {
                currentUser.setValue(value.asText());
            }
            return currentUser;
        }

        JsonParser delegateParser = node.traverse(jp.getCodec());
        delegateParser.nextToken();
        ObjectValue objectValue = objectValueDeserializer.deserialize(delegateParser, ctxt);

        if (objectValue instanceof ReportFilterObjectValue) {
            return (ReportFilterObjectValue) objectValue;
        }
        return null;
    }
}
