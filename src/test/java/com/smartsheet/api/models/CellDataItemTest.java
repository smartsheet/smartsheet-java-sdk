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

import com.smartsheet.api.models.format.Format;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CellDataItemTest {
    @Nested
    class BuilderTests {
        @Test
        void cellDataItemBuilder() {
            // Arrange
            Object objectValue = "Test Value";
            Cell cell = new Cell();
            Format labelFormat = new Format();
            Format valueFormat = new Format();
            SummaryField profileField = new SummaryField();

            // Act
            CellDataItem cellDataItemNoArg = CellDataItem.builder().build();
            cellDataItemNoArg.setColumnId(123L);
            cellDataItemNoArg.setRowId(456L);
            cellDataItemNoArg.setSheetId(789L);
            cellDataItemNoArg.setObjectValue(objectValue);
            cellDataItemNoArg.setCell(cell);
            cellDataItemNoArg.setDataSource("CELL");
            cellDataItemNoArg.setLabel("Test Label");
            cellDataItemNoArg.setLabelFormat(labelFormat);
            cellDataItemNoArg.setOrder(1);
            cellDataItemNoArg.setValueFormat(valueFormat);
            cellDataItemNoArg.setProfileField(profileField);

            CellDataItem cellDataItemAllArg = CellDataItem.builder()
                    .columnId(123L)
                    .rowId(456L)
                    .sheetId(789L)
                    .objectValue(objectValue)
                    .cell(cell)
                    .dataSource("CELL")
                    .label("Test Label")
                    .labelFormat(labelFormat)
                    .order(1)
                    .valueFormat(valueFormat)
                    .profileField(profileField)
                    .build();

            // Assert
            assertThat(cellDataItemNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(cellDataItemAllArg);
        }
    }
}
