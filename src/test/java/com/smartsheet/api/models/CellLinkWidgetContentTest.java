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

import com.smartsheet.api.models.enums.WidgetType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CellLinkWidgetContentTest {
    @Nested
    class BuilderTests {
        @Test
        void cellLinkWidgetContentBuilder() {
            // Arrange
            Long sheetId = 123L;
            List<CellDataItem> cellData = List.of(CellDataItem.builder().build());
            List<Column> columns = List.of(new Column());
            WidgetHyperlink hyperlink = new WidgetHyperlink();

            // Act
            CellLinkWidgetContent contentNoArg = CellLinkWidgetContent.builder().build();
            contentNoArg.setSheetId(sheetId);
            contentNoArg.setCellData(cellData);
            contentNoArg.setColumns(columns);
            contentNoArg.setHyperlink(hyperlink);

            CellLinkWidgetContent contentAllArg = CellLinkWidgetContent.builder()
                    .sheetId(sheetId)
                    .cellData(cellData)
                    .columns(columns)
                    .hyperlink(hyperlink)
                    .build();

            // Assert
            assertThat(contentNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(contentAllArg);
        }
    }

    @Nested
    class WidgetTypeTests {
        @Test
        void testGetWidgetType() {
            // Arrange
            CellLinkWidgetContent content = CellLinkWidgetContent.builder().build();

            // Act
            WidgetType widgetType = content.getWidgetType();

            // Assert
            assertThat(widgetType).isEqualTo(WidgetType.METRIC);
        }
    }

    @Nested
    class InterfaceImplementationTests {
        @Test
        void testImplementsWidgetContent() {
            // Arrange
            CellLinkWidgetContent content = CellLinkWidgetContent.builder().build();

            // Act & Assert
            assertThat(content).isInstanceOf(WidgetContent.class);
        }
    }
}
