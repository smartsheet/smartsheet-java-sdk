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

class SentUpdateRequestTest {
    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            SentUpdateRequest sentUpdateRequest1 = SentUpdateRequest.builder().id(1L).build();
            SentUpdateRequest sentUpdateRequest2 = SentUpdateRequest.builder().id(2L).build();

            // Act
            boolean result = sentUpdateRequest1.equals(sentUpdateRequest2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            SentUpdateRequest sentUpdateRequest1 = SentUpdateRequest.builder().id(1L).build();
            SentUpdateRequest sentUpdateRequest2 = SentUpdateRequest.builder().id(1L).build();

            // Act
            boolean result = sentUpdateRequest1.equals(sentUpdateRequest2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            SentUpdateRequest sentUpdateRequest1 = SentUpdateRequest.builder().id(1L).message("different").build();
            SentUpdateRequest sentUpdateRequest2 = SentUpdateRequest.builder().id(1L).message("messages").build();

            // Act
            boolean result = sentUpdateRequest1.equals(sentUpdateRequest2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            SentUpdateRequest sentUpdateRequest1 = SentUpdateRequest.builder().id(1L).build();
            SentUpdateRequest sentUpdateRequest2 = SentUpdateRequest.builder().id(2L).build();

            // Act
            int hashCode1 = sentUpdateRequest1.hashCode();
            int hashCode2 = sentUpdateRequest2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            SentUpdateRequest sentUpdateRequest1 = SentUpdateRequest.builder().id(1L).build();
            SentUpdateRequest sentUpdateRequest2 = SentUpdateRequest.builder().id(1L).build();

            // Act
            int hashCode1 = sentUpdateRequest1.hashCode();
            int hashCode2 = sentUpdateRequest2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            SentUpdateRequest sentUpdateRequest1 = SentUpdateRequest.builder().id(1L).message("different").build();
            SentUpdateRequest sentUpdateRequest2 = SentUpdateRequest.builder().id(1L).message("messages").build();

            // Act
            int hashCode1 = sentUpdateRequest1.hashCode();
            int hashCode2 = sentUpdateRequest2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
