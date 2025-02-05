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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsheet.api.models.BooleanObjectValue;
import com.smartsheet.api.models.ContactObjectValue;
import com.smartsheet.api.models.DateObjectValue;
import com.smartsheet.api.models.Duration;
import com.smartsheet.api.models.MultiContactObjectValue;
import com.smartsheet.api.models.MultiPicklistObjectValue;
import com.smartsheet.api.models.NumberObjectValue;
import com.smartsheet.api.models.ObjectValue;
import com.smartsheet.api.models.Predecessor;
import com.smartsheet.api.models.PredecessorList;
import com.smartsheet.api.models.StringObjectValue;
import com.smartsheet.api.models.enums.ObjectValueType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectValueDeserializer extends JsonDeserializer<ObjectValue> {

    @Override
    public ObjectValue deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        final ObjectValue objectValue;
        ContactObjectValue contactObjectValue = null;

        if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ObjectValueAttributeSuperset superset = mapper.readValue(jp, ObjectValueAttributeSuperset.class);

            ObjectValueType parsedObjectType;
            try {
                parsedObjectType = ObjectValueType.valueOf(superset.objectType);
            } catch (IllegalArgumentException e) {
                // If a new object type is introduced to the Smartsheet API that this version of the SDK doesn't support,
                // return null instead of throwing an exception.
                return null;
            }

            switch (parsedObjectType) {
                case DURATION:
                    objectValue = new Duration(
                            superset.negative,
                            superset.elapsed,
                            superset.weeks,
                            superset.days,
                            superset.hours,
                            superset.minutes,
                            superset.seconds,
                            superset.milliseconds);
                    break;

                case PREDECESSOR_LIST:
                    objectValue = new PredecessorList(superset.predecessors);
                    break;

                case CONTACT:
                    contactObjectValue = new ContactObjectValue();
                    contactObjectValue.setName(superset.name);
                    contactObjectValue.setEmail(superset.email);
                    contactObjectValue.setId(superset.id);
                    objectValue = contactObjectValue;
                    break;

                case DATE:
                    // Intentional fallthrough
                case DATETIME:
                    // Intentional fallthrough
                case ABSTRACT_DATETIME:
                    objectValue = new DateObjectValue(parsedObjectType, superset.value);
                    break;

                case MULTI_CONTACT:
                    List<ContactObjectValue> contactObjectValues = new ArrayList<>();
                    for (Object contact : superset.values) {
                        contactObjectValue = mapper.convertValue(contact, ContactObjectValue.class);
                        contactObjectValues.add(contactObjectValue);
                    }
                    objectValue = new MultiContactObjectValue(contactObjectValues);
                    break;

                case MULTI_PICKLIST:
                    objectValue = new MultiPicklistObjectValue((List<String>) superset.values);
                    break;

                default:
                    objectValue = null;
            }
        } else {
            JsonToken token = jp.getCurrentToken();
            if (token.isBoolean()) {
                objectValue = new BooleanObjectValue(jp.getBooleanValue());
            } else if (token.isNumeric()) {
                objectValue = new NumberObjectValue(jp.getNumberValue());
            } else {
                objectValue = new StringObjectValue(jp.getText());
            }
        }
        return objectValue;
    }

    private static class ObjectValueAttributeSuperset {
        // This needs to be represented as a string so that any new object types added won't completely break the API
        public String objectType;

        // PREDECESSOR_LIST specific attributes
        public List<Predecessor> predecessors;

        // DURATION specific attributes
        public Boolean negative;
        public Boolean elapsed;
        public Double weeks;
        public Double days;
        public Double hours;
        public Double minutes;
        public Double seconds;
        public Double milliseconds;

        // CONTACT specific attributes
        public String id;
        public String name;
        public String email;

        // MULTI_CONTACT
        public List<?> values;

        // Various other types
        public String value;
    }
}
