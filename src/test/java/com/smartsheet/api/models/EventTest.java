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

package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.EventAction;
import com.smartsheet.api.models.enums.EventObjectType;
import com.smartsheet.api.models.enums.EventSource;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {
    @Nested
    class BuilderTests {
        @Test
        void eventBuilder() {
            // Act
            Event eventNoArg = Event.builder().build();
            eventNoArg.setAccessTokenName("accessTokenName");
            eventNoArg.setAction(EventAction.CREATE);
            eventNoArg.setAdditionalDetails(Map.of("key", "value"));
            eventNoArg.setEventId("eventId");
            eventNoArg.setEventTimestamp("2024-01-01T00:00:00Z");
            eventNoArg.setObjectId("objectId");
            eventNoArg.setObjectType(EventObjectType.SHEET);
            eventNoArg.setRequestUserId(123L);
            eventNoArg.setSource(EventSource.API_INTEGRATED_APP);
            eventNoArg.setUserId(456L);

            Event eventAllArg = Event.builder()
                    .accessTokenName("accessTokenName")
                    .action(EventAction.CREATE)
                    .additionalDetails(Map.of("key", "value"))
                    .eventId("eventId")
                    .eventTimestamp("2024-01-01T00:00:00Z")
                    .objectId("objectId")
                    .objectType(EventObjectType.SHEET)
                    .requestUserId(123L)
                    .source(EventSource.API_INTEGRATED_APP)
                    .userId(456L)
                    .build();

            // Assert
            assertThat(eventNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(eventAllArg);
        }
    }
}
