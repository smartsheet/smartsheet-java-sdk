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

class HyperlinkTest {
    @Nested
    class BuilderTests {
        @Test
        void hyperlinkBuilder() {
            // Act
            Hyperlink hyperlinkNoArg = Hyperlink.builder().build();
            hyperlinkNoArg.setUrl("https://www.smartsheet.com");
            hyperlinkNoArg.setSheetId(123L);
            hyperlinkNoArg.setReportId(456L);
            hyperlinkNoArg.setSightId(789L);

            Hyperlink hyperlinkAllArg = Hyperlink.builder()
                    .url("https://www.smartsheet.com")
                    .sheetId(123L)
                    .reportId(456L)
                    .sightId(789L)
                    .build();

            // Assert
            assertThat(hyperlinkNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(hyperlinkAllArg);
        }
    }

    @Nested
    class IsNullTests {
        @Test
        void testIsNull_allNull() {
            // Arrange
            Hyperlink hyperlink = Hyperlink.builder().build();

            // Act
            boolean isNull = hyperlink.isNull();

            // Assert
            assertThat(isNull).isTrue();
        }

        @Test
        void testIsNull_urlNotNull() {
            // Arrange
            Hyperlink hyperlink = Hyperlink.builder()
                    .url("https://www.smartsheet.com")
                    .build();

            // Act
            boolean isNull = hyperlink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }

        @Test
        void testIsNull_sheetIdNotNull() {
            // Arrange
            Hyperlink hyperlink = Hyperlink.builder()
                    .sheetId(123L)
                    .build();

            // Act
            boolean isNull = hyperlink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }

        @Test
        void testIsNull_reportIdNotNull() {
            // Arrange
            Hyperlink hyperlink = Hyperlink.builder()
                    .reportId(456L)
                    .build();

            // Act
            boolean isNull = hyperlink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }

        @Test
        void testIsNull_sightIdNotNull() {
            // Arrange
            Hyperlink hyperlink = Hyperlink.builder()
                    .sightId(789L)
                    .build();

            // Act
            boolean isNull = hyperlink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }

        @Test
        void testIsNull_allNotNull() {
            // Arrange
            Hyperlink hyperlink = Hyperlink.builder()
                    .url("https://www.smartsheet.com")
                    .sheetId(123L)
                    .reportId(456L)
                    .sightId(789L)
                    .build();

            // Act
            boolean isNull = hyperlink.isNull();

            // Assert
            assertThat(isNull).isFalse();
        }
    }
}
