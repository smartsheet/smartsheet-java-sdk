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

class WebhookSharedSecretTest {

    @Nested
    class BuilderTests {
        @Test
        void webhookSharedSecretBuilder() {
            // Act
            WebhookSharedSecret secretNoArg = WebhookSharedSecret.builder().build();
            secretNoArg.setSharedSecret("test-secret");

            WebhookSharedSecret secretAllArg = WebhookSharedSecret.builder()
                    .sharedSecret("test-secret")
                    .build();

            // Assert
            assertThat(secretNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(secretAllArg);
        }
    }

    @Nested
    class ToStringTests {
        @Test
        void toString_containsAllFields() {
            // Arrange
            WebhookSharedSecret secret = WebhookSharedSecret.builder()
                    .sharedSecret("test-secret")
                    .build();

            // Act
            String toString = secret.toString();

            // Assert
            assertThat(toString).contains("sharedSecret=test-secret");
        }
    }
}