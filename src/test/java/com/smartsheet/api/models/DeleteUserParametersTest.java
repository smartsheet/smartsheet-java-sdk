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

class DeleteUserParametersTest {
    @Nested
    class BuilderTests {
        @Test
        void deleteUserParametersBuilder() {
            // Arrange
            Long transferToId = 123L;
            Boolean transferSheets = true;
            Boolean removeFromSharing = false;

            // Act
            DeleteUserParameters paramsNoArg = DeleteUserParameters.builder().build();
            paramsNoArg.setTransferToId(transferToId);
            paramsNoArg.setTransferSheets(transferSheets);
            paramsNoArg.setRemoveFromSharing(removeFromSharing);

            DeleteUserParameters paramsAllArg = DeleteUserParameters.builder()
                    .transferToId(transferToId)
                    .transferSheets(transferSheets)
                    .removeFromSharing(removeFromSharing)
                    .build();

            // Assert
            assertThat(paramsNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(paramsAllArg);
        }
    }

    @Nested
    class ToQueryStringTests {
        @Test
        void testToQueryString_allParametersSet() {
            // Arrange
            DeleteUserParameters params = DeleteUserParameters.builder()
                    .transferToId(123L)
                    .transferSheets(true)
                    .removeFromSharing(false)
                    .build();

            // Act
            String queryString = params.toQueryString();

            // Assert
            assertThat(queryString).isEqualTo(
                    "?transferSheets=true" +
                            "&removeFromSharing=false" +
                            "&transferTo=123"
            );
        }

        @Test
        void testToQueryString_someParametersNull() {
            // Arrange
            DeleteUserParameters params = DeleteUserParameters.builder()
                    .transferToId(123L)
                    .build();

            // Act
            String queryString = params.toQueryString();

            // Assert
            assertThat(queryString).isEqualTo("?transferTo=123");
        }

        @Test
        void testToQueryString_allParametersNull() {
            // Arrange
            DeleteUserParameters params = DeleteUserParameters.builder().build();

            // Act
            String queryString = params.toQueryString();

            // Assert
            assertThat(queryString).isEmpty();
        }
    }
}
