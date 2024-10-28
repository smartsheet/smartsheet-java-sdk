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

import com.smartsheet.api.models.enums.DestinationType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorDetailTest {
    @Nested
    class BuilderTests {
        @Test
        void errorDetailBuilder() {
            // Act
            ErrorDetail errorDetailNoArg = ErrorDetail.builder().build();
            errorDetailNoArg.setAlternateEmailAddress("alternate@example.com");
            errorDetailNoArg.setIndex(1);
            errorDetailNoArg.setMaxSheetCount(100);
            errorDetailNoArg.setPrimaryEmailAddress("primary@example.com");
            errorDetailNoArg.setRowId(12345L);
            errorDetailNoArg.setTopContainerId(67890L);
            errorDetailNoArg.setTopContainerType(DestinationType.FOLDER);

            ErrorDetail errorDetailAllArg = ErrorDetail.builder()
                    .alternateEmailAddress("alternate@example.com")
                    .index(1)
                    .maxSheetCount(100)
                    .primaryEmailAddress("primary@example.com")
                    .rowId(12345L)
                    .topContainerId(67890L)
                    .topContainerType(DestinationType.FOLDER)
                    .build();

            // Assert
            assertThat(errorDetailNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(errorDetailAllArg);
        }
    }
}
