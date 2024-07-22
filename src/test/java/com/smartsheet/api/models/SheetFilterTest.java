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

import com.smartsheet.api.models.enums.SheetFilterType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SheetFilterTest {
    @Nested
    class BuilderTests {
        @Test
        void sheetFilterBuilder() {
            // Arrange
            // Common Objects
            SheetFilterDetails sheetFilterDetails = new SheetFilterDetails();

            // Act
            SheetFilter sheetFilterNoArg = SheetFilter.builder().build();
            sheetFilterNoArg.setId(1L);
            sheetFilterNoArg.setName("name");
            sheetFilterNoArg.setFilterType(SheetFilterType.PERSONAL);
            sheetFilterNoArg.setQuery(sheetFilterDetails);
            sheetFilterNoArg.setVersion(2);

            SheetFilter sheetFilterAllArg = SheetFilter.builder()
                    .id(1L)
                    .name("name")
                    .filterType(SheetFilterType.PERSONAL)
                    .query(sheetFilterDetails)
                    .version(2)
                    .build();


            // Assert
            assertThat(sheetFilterNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(sheetFilterAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            SheetFilter sheetFilter1 = SheetFilter.builder().id(1L).build();
            SheetFilter sheetFilter2 = SheetFilter.builder().id(2L).build();

            // Act
            boolean result = sheetFilter1.equals(sheetFilter2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            SheetFilter sheetFilter1 = SheetFilter.builder().id(1L).build();
            SheetFilter sheetFilter2 = SheetFilter.builder().id(1L).build();

            // Act
            boolean result = sheetFilter1.equals(sheetFilter2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            SheetFilter sheetFilter1 = SheetFilter.builder().id(1L).name("different").build();
            SheetFilter sheetFilter2 = SheetFilter.builder().id(1L).name("names").build();

            // Act
            boolean result = sheetFilter1.equals(sheetFilter2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            SheetFilter sheetFilter1 = SheetFilter.builder().id(1L).build();
            SheetFilter sheetFilter2 = SheetFilter.builder().id(2L).build();

            // Act
            int hashCode1 = sheetFilter1.hashCode();
            int hashCode2 = sheetFilter2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            SheetFilter sheetFilter1 = SheetFilter.builder().id(1L).build();
            SheetFilter sheetFilter2 = SheetFilter.builder().id(1L).build();

            // Act
            int hashCode1 = sheetFilter1.hashCode();
            int hashCode2 = sheetFilter2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            SheetFilter sheetFilter1 = SheetFilter.builder().id(1L).name("different").build();
            SheetFilter sheetFilter2 = SheetFilter.builder().id(1L).name("names").build();

            // Act
            int hashCode1 = sheetFilter1.hashCode();
            int hashCode2 = sheetFilter2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
