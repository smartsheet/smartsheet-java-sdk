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
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlternateEmailTest {

    @Nested
    class BuilderTests {
        @Test
        void alternateEmailBuilder() {
            // Arrange
            Long id = 123L;
            String email = "test@example.com";
            Boolean confirmed = true;

            // Act
            AlternateEmail emailNoArg = new AlternateEmail();
            emailNoArg.setId(id);
            emailNoArg.setEmail(email);
            emailNoArg.setConfirmed(confirmed);

            AlternateEmail emailAllArg = new AlternateEmail(id, email, confirmed);

            AlternateEmail emailBuilder = AlternateEmail.builder()
                    .id(id)
                    .email(email)
                    .confirmed(confirmed)
                    .build();

            // Assert
            assertThat(emailNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(emailAllArg);

            assertThat(emailBuilder)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(emailAllArg);
        }

        @Test
        void builderRequiresEmail() {
            // Act & Assert
            assertThrows(InstantiationError.class, () -> AlternateEmail.builder().build());
        }

        @Test
        void createAlternateEmail_convenience() {
            // Arrange
            String email = "test@example.com";

            // Act
            AlternateEmail alternateEmail = AlternateEmail.createAlternateEmail(email);

            // Assert
            assertThat(alternateEmail.getEmail()).isEqualTo(email);
            assertThat(alternateEmail.getId()).isNull();
            assertThat(alternateEmail.getConfirmed()).isNull();
        }
    }

    @Nested
    class EqualsAndHashCodeTests {
        @Test
        void equalsAndHashCode() {
            // Arrange
            AlternateEmail email1 = AlternateEmail.builder()
                    .id(123L)
                    .email("test@example.com")
                    .confirmed(true)
                    .build();

            AlternateEmail email2 = AlternateEmail.builder()
                    .id(123L)
                    .email("test@example.com")
                    .confirmed(true)
                    .build();

            AlternateEmail email3 = AlternateEmail.builder()
                    .id(456L)
                    .email("other@example.com")
                    .confirmed(false)
                    .build();

            // Assert
            assertThat(email1).isEqualTo(email2);
            assertThat(email1.hashCode()).isEqualTo(email2.hashCode());

            assertThat(email1).isNotEqualTo(email3);
            assertThat(email1.hashCode()).isNotEqualTo(email3.hashCode());
        }
    }

    @Nested
    class ToStringTests {
        @Test
        void toString_containsAllFields() {
            // Arrange
            AlternateEmail email = AlternateEmail.builder()
                    .id(123L)
                    .email("test@example.com")
                    .confirmed(true)
                    .build();

            // Act
            String toString = email.toString();

            // Assert
            assertThat(toString).contains("id=123");
            assertThat(toString).contains("email=test@example.com");
            assertThat(toString).contains("confirmed=true");
        }
    }
}
