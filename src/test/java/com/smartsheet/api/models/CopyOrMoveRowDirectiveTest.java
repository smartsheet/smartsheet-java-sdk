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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CopyOrMoveRowDirectiveTest {
    @Nested
    class BuilderTests {
        @Test
        void copyOrMoveRowDirectiveBuilder() {
            // Arrange
            CopyOrMoveRowDestination to = CopyOrMoveRowDestination.builder().sheetId(5L).build();

            // Act
            CopyOrMoveRowDirective copyOrMoveRowDirectiveNoArg = CopyOrMoveRowDirective.builder().build();
            copyOrMoveRowDirectiveNoArg.setRowIds(List.of(5L));
            copyOrMoveRowDirectiveNoArg.setTo(to);

            CopyOrMoveRowDirective copyOrMoveRowDirectiveAllArg = CopyOrMoveRowDirective.builder()
                    .rowIds(List.of(5L))
                    .to(to)
                    .build();

            // Assert
            assertThat(copyOrMoveRowDirectiveNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(copyOrMoveRowDirectiveAllArg);
        }
    }
}
