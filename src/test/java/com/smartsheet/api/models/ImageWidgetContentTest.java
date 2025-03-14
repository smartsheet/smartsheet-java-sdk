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
import com.smartsheet.api.models.format.Format;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageWidgetContentTest {
    @Nested
    class BuilderTests {
        @Test
        void imageWidgetContentBuilder() {
            // Arrange
            Format format = new Format();
            WidgetHyperlink hyperlink = new WidgetHyperlink();

            // Act
            ImageWidgetContent contentNoArg = ImageWidgetContent.builder().build();
            contentNoArg.setPrivateId("abc123");
            contentNoArg.setFileName("image.jpg");
            contentNoArg.setFormat(format);
            contentNoArg.setHeight(300);
            contentNoArg.setHyperlink(hyperlink);
            contentNoArg.setWidth(400);

            ImageWidgetContent contentAllArg = ImageWidgetContent.builder()
                    .privateId("abc123")
                    .fileName("image.jpg")
                    .format(format)
                    .height(300)
                    .hyperlink(hyperlink)
                    .width(400)
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
            ImageWidgetContent content = ImageWidgetContent.builder().build();

            // Act
            WidgetType widgetType = content.getWidgetType();

            // Assert
            assertThat(widgetType).isEqualTo(WidgetType.IMAGE);
        }
    }

    @Nested
    class InterfaceImplementationTests {
        @Test
        void testImplementsWidgetContent() {
            // Arrange
            ImageWidgetContent content = ImageWidgetContent.builder().build();

            // Act & Assert
            assertThat(content).isInstanceOf(WidgetContent.class);
        }
    }
}
