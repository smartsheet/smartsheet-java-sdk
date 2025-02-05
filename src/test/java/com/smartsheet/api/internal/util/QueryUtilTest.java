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

package com.smartsheet.api.internal.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class QueryUtilTest {
    @Nested
    class GenerateCommaSeparatedList {
        @Test
        void generateCommaSeparatedList_emptyCollection() {
            // Act
            String result = QueryUtil.generateCommaSeparatedList(Collections.emptySet());

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        void generateCommaSeparatedList_nullCollection() {
            // Act
            String result = QueryUtil.generateCommaSeparatedList(null);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        void generateCommaSeparatedList_stringCollection() {
            // Arrange
            List<String> stringCollection = List.of("first", "second", "third");

            // Act
            String result = QueryUtil.generateCommaSeparatedList(stringCollection);

            // Assert
            assertThat(result).isEqualTo("first,second,third");
        }

        @Test
        void generateCommaSeparatedList_longCollection() {
            // Arrange
            List<Long> longCollection = List.of(1L, 2L, 3L);

            // Act
            String result = QueryUtil.generateCommaSeparatedList(longCollection);

            // Assert
            assertThat(result).isEqualTo("1,2,3");
        }
    }

    @Nested
    class GenerateUrl {
        @Test
        void generateUrl_bothNull() {
            // Act
            String result = QueryUtil.generateUrl(null, null);

            // Assert
            assertThat(result).isEmpty();

        }

        @Test
        void generateUrl_nullBaseUrl_withParams() {
            // Arrange
            Map<String, String> params = Map.of("paramOne", "valueOne", "paramTwo", "valueTwo");

            // Act
            String result = QueryUtil.generateUrl(null, params);

            // Assert
            // note: must test both orders because the map order is non-deterministic
            assertThat(result).isIn(
                    "?paramOne=valueOne&paramTwo=valueTwo",
                    "?paramTwo=valueTwo&paramOne=valueOne"
            );

        }

        @Test
        void generateUrl_nullBaseUrl_emptyParams() {
            // Act
            String result = QueryUtil.generateUrl(null, Collections.emptyMap());

            // Assert
            assertThat(result).isEmpty();

        }

        @Test
        void generateUrl_withBaseUrl_nullParams() {
            // Act
            String result = QueryUtil.generateUrl("baseUrl.com", null);

            // Assert
            assertThat(result).isEqualTo("baseUrl.com");

        }

        @Test
        void generateUrl_withBaseUrl_withNullValueParam() {
            // Arrange
            Map<String, String> params = new HashMap<>();
            params.put("paramOne", null);
            params.put("paramTwo", "valueTwo");

            // Act
            String result = QueryUtil.generateUrl("baseUrl.com", params);

            // Assert
            assertThat(result).isEqualTo("baseUrl.com?paramTwo=valueTwo");
        }

        @Test
        void generateUrl_withBaseUrl_withEmptyParam() {
            // Arrange
            Map<String, String> params = Map.of("paramOne", "", "paramTwo", "valueTwo");

            // Act
            String result = QueryUtil.generateUrl("baseUrl.com", params);

            // Assert
            assertThat(result).isEqualTo("baseUrl.com?paramTwo=valueTwo");
        }

        @Test
        void generateUrl_withBaseUrl_withNullKeyParam() {
            // Arrange
            Map<String, String> params = new HashMap<>();
            params.put(null, "valueOne");
            params.put("paramTwo", "valueTwo");

            // Act
            String result = QueryUtil.generateUrl("baseUrl.com", params);

            // Assert
            assertThat(result).isEqualTo("baseUrl.com?paramTwo=valueTwo");
        }
    }
}
