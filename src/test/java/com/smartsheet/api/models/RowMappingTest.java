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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RowMappingTest {
    @Nested
    class BuilderTests {
        @Test
        void rowMappingBuilder() {
            // Act
            RowMapping rowMappingNoArg = RowMapping.builder().build();
            rowMappingNoArg.setFrom(1L);
            rowMappingNoArg.setTo(2L);

            RowMapping rowMappingAllArg = RowMapping.builder()
                    .from(1L)
                    .to(2L)
                    .build();

            // Assert
            assertThat(rowMappingNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(rowMappingAllArg);
        }
    }
}
