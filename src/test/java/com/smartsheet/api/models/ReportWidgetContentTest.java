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

import static org.assertj.core.api.Assertions.assertThat;

class ReportWidgetContentTest {
    @Nested
    class BuilderTests {
        @Test
        void reportWidgetContentBuilder() {
            // Arrange
            WidgetHyperlink hyperlink = new WidgetHyperlink();

            // Act
            ReportWidgetContent contentNoArg = ReportWidgetContent.builder().build();
            contentNoArg.setReportId(123L);
            contentNoArg.setHtmlContent("<div>Report Content</div>");
            contentNoArg.setHyperlink(hyperlink);

            ReportWidgetContent contentAllArg = ReportWidgetContent.builder()
                    .reportId(123L)
                    .htmlContent("<div>Report Content</div>")
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
            ReportWidgetContent content = ReportWidgetContent.builder().build();

            // Act
            WidgetType widgetType = content.getWidgetType();

            // Assert
            assertThat(widgetType).isEqualTo(WidgetType.GRIDGANTT);
        }
    }

    @Nested
    class InterfaceImplementationTests {
        @Test
        void testImplementsWidgetContent() {
            // Arrange
            ReportWidgetContent content = ReportWidgetContent.builder().build();

            // Act & Assert
            assertThat(content).isInstanceOf(WidgetContent.class);
        }
    }
}
