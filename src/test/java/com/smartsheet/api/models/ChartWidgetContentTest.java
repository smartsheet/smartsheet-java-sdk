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

class ChartWidgetContentTest {
    @Nested
    class BuilderTests {
        @Test
        void chartWidgetContentBuilder() {
            // Arrange
            List<Object> axes = List.of("X", "Y");
            WidgetHyperlink hyperlink = new WidgetHyperlink();
            List<Long> includedColumnIds = List.of(789L, 101112L);
            Object legend = "RIGHT";
            List<SelectionRange> selectionRanges = List.of(SelectionRange.builder().build());
            List<Object> series = List.of("Series1", "Series2");

            // Act
            ChartWidgetContent contentNoArg = ChartWidgetContent.builder().build();
            contentNoArg.setReportId(123L);
            contentNoArg.setSheetId(456L);
            contentNoArg.setAxes(axes);
            contentNoArg.setHyperlink(hyperlink);
            contentNoArg.setIncludedColumnIds(includedColumnIds);
            contentNoArg.setLegend(legend);
            contentNoArg.setSelectionRanges(selectionRanges);
            contentNoArg.setSeries(series);

            ChartWidgetContent contentAllArg = ChartWidgetContent.builder()
                    .reportId(123L)
                    .sheetId(456L)
                    .axes(axes)
                    .hyperlink(hyperlink)
                    .includedColumnIds(includedColumnIds)
                    .legend(legend)
                    .selectionRanges(selectionRanges)
                    .series(series)
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
            ChartWidgetContent content = ChartWidgetContent.builder().build();

            // Act
            WidgetType widgetType = content.getWidgetType();

            // Assert
            assertThat(widgetType).isEqualTo(WidgetType.CHART);
        }
    }

    @Nested
    class InterfaceImplementationTests {
        @Test
        void testImplementsWidgetContent() {
            // Arrange
            ChartWidgetContent content = ChartWidgetContent.builder().build();

            // Act & Assert
            assertThat(content).isInstanceOf(WidgetContent.class);
        }
    }
}
