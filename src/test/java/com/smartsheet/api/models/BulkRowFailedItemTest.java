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

class BulkRowFailedItemTest {
    @Nested
    class BuilderTests {
        @Test
        void bulkRowFailedItemBuilder() {
            // Arrange
            Error error = Error.builder().message("error message").build();

            // Act
            BulkRowFailedItem bulkRowFailedItemNoArg = BulkRowFailedItem.builder().build();
            bulkRowFailedItemNoArg.setIndex(5);
            bulkRowFailedItemNoArg.setError(error);
            bulkRowFailedItemNoArg.setRowId(6L);

            BulkRowFailedItem bulkRowFailedItemAllArg = BulkRowFailedItem.builder()
                    .index(5)
                    .error(error)
                    .rowId(6L)
                    .build();

            // Assert
            assertThat(bulkRowFailedItemNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(bulkRowFailedItemAllArg);
        }
    }
}
