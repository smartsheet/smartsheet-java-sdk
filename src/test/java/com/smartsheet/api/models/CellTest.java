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

import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.format.Format;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CellTest {
    @Nested
    class BuilderTests {
        @Test
        void cellBuilder() {
            // Arrange
            Object value = "Test Value";
            ObjectValue objectValue = new StringObjectValue();
            Hyperlink hyperlink = Hyperlink.builder().build();
            CellLink linkInFromCell = CellLink.builder().build();
            List<CellLink> linksOutToCells = List.of(CellLink.builder().build());
            Image image = Image.builder().build();
            Format format = new Format();

            // Act
            Cell cellNoArg = Cell.builder().build();
            cellNoArg.setColumnType(ColumnType.TEXT_NUMBER);
            cellNoArg.setType(ColumnType.TEXT_NUMBER);
            cellNoArg.setValue(value);
            cellNoArg.setObjectValue(objectValue);
            cellNoArg.setDisplayValue("Test Display Value");
            cellNoArg.setColumnId(123L);
            cellNoArg.setRowId(456L);
            cellNoArg.setHyperlink(hyperlink);
            cellNoArg.setLinkInFromCell(linkInFromCell);
            cellNoArg.setLinksOutToCells(linksOutToCells);
            cellNoArg.setConditionalFormat("conditionalFormat");
            cellNoArg.setImage(image);
            cellNoArg.setFormula("=SUM(A1:A10)");
            cellNoArg.setStrict(true);
            cellNoArg.setFormat(format);
            cellNoArg.setOverrideValidation(false);

            Cell cellAllArg = Cell.builder()
                    .columnType(ColumnType.TEXT_NUMBER)
                    .type(ColumnType.TEXT_NUMBER)
                    .value(value)
                    .objectValue(objectValue)
                    .displayValue("Test Display Value")
                    .columnId(123L)
                    .rowId(456L)
                    .hyperlink(hyperlink)
                    .linkInFromCell(linkInFromCell)
                    .linksOutToCells(linksOutToCells)
                    .conditionalFormat("conditionalFormat")
                    .image(image)
                    .formula("=SUM(A1:A10)")
                    .strict(true)
                    .format(format)
                    .overrideValidation(false)
                    .build();

            // Assert
            assertThat(cellNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(cellAllArg);
        }
    }
}
