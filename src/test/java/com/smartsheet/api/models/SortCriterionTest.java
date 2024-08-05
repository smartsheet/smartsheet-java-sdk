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

import com.smartsheet.api.models.enums.SortDirection;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortCriterionTest {
    @Nested
    class BuilderTests {
        @Test
        void sortCriterionBuilder() {
            // Arrange

            // Act
            SortCriterion sortCriterionNoArg = SortCriterion.builder().build();
            sortCriterionNoArg.setId(1L);
            sortCriterionNoArg.setName("name");
            sortCriterionNoArg.setColumnId(2L);
            sortCriterionNoArg.setDirection(SortDirection.ASCENDING);

            SortCriterion sortCriterionAllArg = SortCriterion.builder()
                    .id(1L)
                    .name("name")
                    .columnId(2L)
                    .direction(SortDirection.ASCENDING)
                    .build();

            // Assert
            assertThat(sortCriterionNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(sortCriterionAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            SortCriterion sortCriterion1 = SortCriterion.builder().id(1L).build();
            SortCriterion sortCriterion2 = SortCriterion.builder().id(2L).build();

            // Act
            boolean result = sortCriterion1.equals(sortCriterion2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            SortCriterion sortCriterion1 = SortCriterion.builder().id(1L).build();
            SortCriterion sortCriterion2 = SortCriterion.builder().id(1L).build();

            // Act
            boolean result = sortCriterion1.equals(sortCriterion2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            SortCriterion sortCriterion1 = SortCriterion.builder().id(1L).name("different").build();
            SortCriterion sortCriterion2 = SortCriterion.builder().id(1L).name("names").build();

            // Act
            boolean result = sortCriterion1.equals(sortCriterion2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            SortCriterion sortCriterion1 = SortCriterion.builder().id(1L).build();
            SortCriterion sortCriterion2 = SortCriterion.builder().id(2L).build();

            // Act
            int hashCode1 = sortCriterion1.hashCode();
            int hashCode2 = sortCriterion2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            SortCriterion sortCriterion1 = SortCriterion.builder().id(1L).build();
            SortCriterion sortCriterion2 = SortCriterion.builder().id(1L).build();

            // Act
            int hashCode1 = sortCriterion1.hashCode();
            int hashCode2 = sortCriterion2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            SortCriterion sortCriterion1 = SortCriterion.builder().id(1L).name("different").build();
            SortCriterion sortCriterion2 = SortCriterion.builder().id(1L).name("names").build();

            // Act
            int hashCode1 = sortCriterion1.hashCode();
            int hashCode2 = sortCriterion2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
