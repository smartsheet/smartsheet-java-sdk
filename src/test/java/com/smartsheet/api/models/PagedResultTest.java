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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PagedResultTest {
    @Nested
    class BuilderTests {
        @Test
        void pagedResultBuilder() {
            // Arrange

            // Act
            PagedResult<String> pagedResultNoArg = PagedResult.<String>builder().build();
            pagedResultNoArg.setPageNumber(1);
            pagedResultNoArg.setPageSize(100);
            pagedResultNoArg.setTotalCount(250);
            pagedResultNoArg.setTotalPages(3);
            pagedResultNoArg.setData(List.of("Item1", "Item2", "Item3"));

            PagedResult<String> pagedResultAllArg = PagedResult.<String>builder()
                    .pageNumber(1)
                    .pageSize(100)
                    .totalCount(250)
                    .totalPages(3)
                    .data(List.of("Item1", "Item2", "Item3"))
                    .build();

            // Assert
            assertThat(pagedResultNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(pagedResultAllArg);
        }
    }

    @Nested
    class GenericTypeTests {
        @Test
        void testDifferentGenericTypes() {
            // Arrange & Act
            PagedResult<String> stringResult = PagedResult.<String>builder()
                    .data(List.of("Item1", "Item2"))
                    .build();

            PagedResult<Integer> integerResult = PagedResult.<Integer>builder()
                    .data(List.of(1, 2))
                    .build();

            // Assert
            assertThat(stringResult.getData()).containsExactly("Item1", "Item2");
            assertThat(integerResult.getData()).containsExactly(1, 2);
        }
    }
}
