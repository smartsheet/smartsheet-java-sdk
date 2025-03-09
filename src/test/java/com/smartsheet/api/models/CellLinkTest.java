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

package com.smartsheet.api.models;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CellLinkTest {
    @Nested
    class BuilderTests {
        @Test
        void cellLinkBuilder() {
            // Act
            CellLink cellLinkNoArg = CellLink.builder().build();
            cellLinkNoArg.setStatus("OK");
            cellLinkNoArg.setSheetId(123L);
            cellLinkNoArg.setRowId(456L);
            cellLinkNoArg.setColumnId(789L);
            cellLinkNoArg.setSheetName("Test Sheet");

            CellLink cellLinkAllArg = CellLink.builder()
                    .status("OK")
                    .sheetId(123L)
                    .rowId(456L)
                    .columnId(789L)
                    .sheetName("Test Sheet")
                    .build();

            // Assert
            assertThat(cellLinkNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(cellLinkAllArg);
        }
    }

    @Nested
    class IsNullTests {
        @Test
        void testIsNull_allNull() {
            // Arrange
            CellLink cellLink = CellLink.builder().build();

            // Act
            boolean isNull = cellLink.isNull();

            // Assert
            assertThat(isNull).isTrue();
        }

        @Test
        void testIsNull_sheetIdNotNull() {
            // Arrange
            CellLink cellLink = CellLink.builder()
                    .sheetId(123L)
                    .build();

            // Act
            boolean isNull = cellLink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }

        @Test
        void testIsNull_rowIdNotNull() {
            // Arrange
            CellLink cellLink = CellLink.builder()
                    .rowId(456L)
                    .build();

            // Act
            boolean isNull = cellLink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }

        @Test
        void testIsNull_columnIdNotNull() {
            // Arrange
            CellLink cellLink = CellLink.builder()
                    .columnId(789L)
                    .build();

            // Act
            boolean isNull = cellLink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }

        @Test
        void testIsNull_allNotNull() {
            // Arrange
            CellLink cellLink = CellLink.builder()
                    .sheetId(123L)
                    .rowId(456L)
                    .columnId(789L)
                    .build();

            // Act
            boolean isNull = cellLink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }
    }
}
