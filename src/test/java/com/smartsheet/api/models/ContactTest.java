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

class ContactTest {
    @Nested
    class BuilderTests {
        @Test
        void contactBuilder() {
            // Arrange
            // Common Objects

            // Act
            Contact contactNoArg = Contact.builder().build();
            contactNoArg.setId("id");
            contactNoArg.setName("name");
            contactNoArg.setEmail("email");

            Contact contactAllArg = Contact.builder()
                    .id("id")
                    .name("name")
                    .email("email")
                    .build();

            // Assert
            assertThat(contactNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(contactAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Contact contact1 = Contact.builder().id("id").build();
            Contact contact2 = Contact.builder().id("id2").build();

            // Act
            boolean result = contact1.equals(contact2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Contact contact1 = Contact.builder().id("id").build();
            Contact contact2 = Contact.builder().id("id").build();

            // Act
            boolean result = contact1.equals(contact2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Contact contact1 = Contact.builder().id("id").name("different").build();
            Contact contact2 = Contact.builder().id("id").name("names").build();

            // Act
            boolean result = contact1.equals(contact2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Contact contact1 = Contact.builder().id("id").build();
            Contact contact2 = Contact.builder().id("id2").build();

            // Act
            int hashCode1 = contact1.hashCode();
            int hashCode2 = contact2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Contact contact1 = Contact.builder().id("id").build();
            Contact contact2 = Contact.builder().id("id").build();

            // Act
            int hashCode1 = contact1.hashCode();
            int hashCode2 = contact2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Contact contact1 = Contact.builder().id("id").name("different").build();
            Contact contact2 = Contact.builder().id("id").name("names").build();

            // Act
            int hashCode1 = contact1.hashCode();
            int hashCode2 = contact2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
