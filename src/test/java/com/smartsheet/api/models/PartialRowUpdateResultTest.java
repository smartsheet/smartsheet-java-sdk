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

class PartialRowUpdateResultTest {
    @Nested
    class BuilderTests {
        @Test
        void partialRowUpdateResultBuilder() {
            // Arrange
            List<Row> result = List.of(new Row());
            List<BulkRowFailedItem> failedItems = List.of(BulkRowFailedItem.builder().build());

            // Act
            PartialRowUpdateResult updateResultNoArg = PartialRowUpdateResult.builder().build();
            updateResultNoArg.setResult(result);
            updateResultNoArg.setFailedItems(failedItems);
            updateResultNoArg.setResultCode(200);
            updateResultNoArg.setMessage("Success");
            updateResultNoArg.setVersion(1);

            PartialRowUpdateResult updateResultAllArg = PartialRowUpdateResult.builder()
                    .result(result)
                    .failedItems(failedItems)
                    .resultCode(200)
                    .message("Success")
                    .version(1)
                    .build();

            // Assert
            assertThat(updateResultNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(updateResultAllArg);
        }
    }

    @Nested
    class InheritanceTests {
        @Test
        void testInheritanceFromResult() {
            // Arrange
            List<Row> result = List.of(new Row());

            // Act
            Result<List<Row>> updateResult = PartialRowUpdateResult.builder()
                    .result(result)
                    .resultCode(200)
                    .message("Success")
                    .version(1)
                    .build();

            // Assert
            assertThat(updateResult.getResult()).isEqualTo(result);
            assertThat(updateResult.getResultCode()).isEqualTo((Integer) 200);
            assertThat(updateResult.getMessage()).isEqualTo("Success");
            assertThat(updateResult.getVersion()).isEqualTo((Integer) 1);
        }
    }
}
