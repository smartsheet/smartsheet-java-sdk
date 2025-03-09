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

class RowWrapperTest {
    @Nested
    class BuilderTests {
        @Test
        void rowWrapperBuilder() {
            // Arrange
            List<Row> rows = List.of(new Row());

            // Act
            RowWrapper wrapperNoArg = RowWrapper.builder().build();
            wrapperNoArg.setToTop(true);
            wrapperNoArg.setToBottom(false);
            wrapperNoArg.setParentId(123L);
            wrapperNoArg.setSiblingId(456L);
            wrapperNoArg.setRows(rows);

            RowWrapper wrapperAllArg = RowWrapper.builder()
                    .toTop(true)
                    .toBottom(false)
                    .parentId(123L)
                    .siblingId(456L)
                    .rows(rows)
                    .build();

            // Assert
            assertThat(wrapperNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(wrapperAllArg);
        }
    }
}
