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

import com.smartsheet.api.models.enums.CrossSheetReferenceStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CrossSheetReferenceTest {
    @Nested
    class BuilderTests {
        @Test
        void crossSheetReferenceBuilder() {
            // Arrange
            // Common Objects

            // Act
            CrossSheetReference crossSheetReferenceNoArg = CrossSheetReference.builder().build();
            crossSheetReferenceNoArg.setId(1L);
            crossSheetReferenceNoArg.setName("name");
            crossSheetReferenceNoArg.setEndColumnId(2L);
            crossSheetReferenceNoArg.setEndRowId(3L);
            crossSheetReferenceNoArg.setSourceSheetId(4L);
            crossSheetReferenceNoArg.setStartColumnId(5L);
            crossSheetReferenceNoArg.setStartRowId(6L);
            crossSheetReferenceNoArg.setStatus(CrossSheetReferenceStatus.OK);

            CrossSheetReference crossSheetReferenceAllArg = CrossSheetReference.builder()
                    .id(1L)
                    .name("name")
                    .endColumnId(2L)
                    .endRowId(3L)
                    .sourceSheetId(4L)
                    .startColumnId(5L)
                    .startRowId(6L)
                    .status(CrossSheetReferenceStatus.OK)
                    .build();


            // Assert
            assertThat(crossSheetReferenceNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(crossSheetReferenceAllArg);
        }
    }


    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            CrossSheetReference crossSheetReference1 = CrossSheetReference.builder().id(1L).build();
            CrossSheetReference crossSheetReference2 = CrossSheetReference.builder().id(2L).build();

            // Act
            boolean result = crossSheetReference1.equals(crossSheetReference2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            CrossSheetReference crossSheetReference1 = CrossSheetReference.builder().id(1L).build();
            CrossSheetReference crossSheetReference2 = CrossSheetReference.builder().id(1L).build();

            // Act
            boolean result = crossSheetReference1.equals(crossSheetReference2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            CrossSheetReference crossSheetReference1 = CrossSheetReference.builder().id(1L).name("different").build();
            CrossSheetReference crossSheetReference2 = CrossSheetReference.builder().id(1L).name("names").build();

            // Act
            boolean result = crossSheetReference1.equals(crossSheetReference2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            CrossSheetReference crossSheetReference1 = CrossSheetReference.builder().id(1L).build();
            CrossSheetReference crossSheetReference2 = CrossSheetReference.builder().id(2L).build();

            // Act
            int hashCode1 = crossSheetReference1.hashCode();
            int hashCode2 = crossSheetReference2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            CrossSheetReference crossSheetReference1 = CrossSheetReference.builder().id(1L).build();
            CrossSheetReference crossSheetReference2 = CrossSheetReference.builder().id(1L).build();

            // Act
            int hashCode1 = crossSheetReference1.hashCode();
            int hashCode2 = crossSheetReference2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            CrossSheetReference crossSheetReference1 = CrossSheetReference.builder().id(1L).name("different").build();
            CrossSheetReference crossSheetReference2 = CrossSheetReference.builder().id(1L).name("names").build();

            // Act
            int hashCode1 = crossSheetReference1.hashCode();
            int hashCode2 = crossSheetReference2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
