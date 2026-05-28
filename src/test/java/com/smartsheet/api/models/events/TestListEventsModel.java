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

package com.smartsheet.api.models.events;

import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import com.smartsheet.api.models.Event;
import com.smartsheet.api.models.enums.EventObjectType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class TestListEventsModel {

    private static Event deserialize(String json) throws Exception {
        JacksonJsonSerializer serializer = new JacksonJsonSerializer();
        return serializer.deserialize(Event.class, new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
    }

    @Nested
    class ObjectIdStrDeserializationTests {

        @Test
        void deserialize_withObjectIdStr_populatesField() throws Exception {
            String json = "{\"eventId\":\"test-event\",\"objectId\":123,\"objectIdStr\":\"abc-def-123\",\"objectType\":\"SHEET\"}";

            Event event = deserialize(json);

            assertThat(event.getObjectIdStr()).isEqualTo("abc-def-123");
        }

        @Test
        void deserialize_withoutObjectIdStr_fieldIsNull() throws Exception {
            String json = "{\"eventId\":\"test-event\",\"objectId\":456,\"objectType\":\"SHEET\"}";

            Event event = deserialize(json);

            assertThat(event.getObjectIdStr()).isNull();
        }

        @Test
        void deserialize_withNullObjectIdStr_fieldIsNull() throws Exception {
            String json = "{\"eventId\":\"test-event\",\"objectId\":789,\"objectIdStr\":null,\"objectType\":\"SHEET\"}";

            Event event = deserialize(json);

            assertThat(event.getObjectIdStr()).isNull();
        }

        @Test
        void deserialize_doesNotAffectOtherFields() throws Exception {
            String json = "{\"eventId\":\"evt-42\",\"objectId\":999,\"objectIdStr\":\"xyz-789\",\"objectType\":\"SHEET\"}";

            Event event = deserialize(json);

            // Fields present in the JSON must be correctly deserialized
            assertThat(event.getEventId()).isEqualTo("evt-42");
            assertThat(event.getObjectId()).isEqualTo(999);
            assertThat(event.getObjectIdStr()).isEqualTo("xyz-789");
            assertThat(event.getObjectType()).isEqualTo(EventObjectType.SHEET);

            // Fields absent from the JSON must remain null
            assertThat(event.getAction()).isNull();
            assertThat(event.getSource()).isNull();
            assertThat(event.getUserId()).isNull();
            assertThat(event.getRequestUserId()).isNull();
            assertThat(event.getAccessTokenName()).isNull();
            assertThat(event.getAdditionalDetails()).isNull();
            assertThat(event.getEventTimestamp()).isNull();
        }
    }

    @Nested
    class ObjectIdStrBuilderPatternTests {

        @Test
        void setObjectIdStr_returnsThisForChaining() {
            Event event = new Event();

            Event returned = event.setObjectIdStr("chained-value");

            assertThat(returned).isSameAs(event);
        }

        @Test
        void setObjectIdStr_storesValue() {
            Event event = new Event().setObjectIdStr("test-789");

            assertThat(event.getObjectIdStr()).isEqualTo("test-789");
        }

        @Test
        void setObjectIdStr_withNull_storesNull() {
            Event event = new Event().setObjectIdStr("initial").setObjectIdStr(null);

            assertThat(event.getObjectIdStr()).isNull();
        }

        @Test
        void builderChain_setsMultipleFields() {
            Event event = new Event()
                    .setEventId("evt-1")
                    .setObjectIdStr("abc-123");

            assertThat(event.getEventId()).isEqualTo("evt-1");
            assertThat(event.getObjectIdStr()).isEqualTo("abc-123");
        }
    }
}
