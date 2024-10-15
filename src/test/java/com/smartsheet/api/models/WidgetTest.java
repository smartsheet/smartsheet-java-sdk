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

import com.smartsheet.api.models.enums.WidgetType;
import com.smartsheet.api.models.format.Format;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WidgetTest {
    @Nested
    class BuilderTests {
        @Test
        void widgetBuilder() {
            // Arrange
            // Common Objects
            Format titleFormat = new Format();
            ImageWidgetContent contents = new ImageWidgetContent();
            Error error = Error.builder().message("error").build();

            // Act
            Widget widgetNoArg = Widget.builder().build();
            widgetNoArg.setId(1L);
            widgetNoArg.setType(WidgetType.CHART);
            widgetNoArg.setTitle("title");
            widgetNoArg.setShowTitle(true);
            widgetNoArg.setShowTitleIcon(false);
            widgetNoArg.setTitleFormat(titleFormat);
            widgetNoArg.setXPosition(2);
            widgetNoArg.setYPosition(3);
            widgetNoArg.setHeight(4);
            widgetNoArg.setWidth(5);
            widgetNoArg.setVersion(6);
            widgetNoArg.setContents(contents);
            widgetNoArg.setError(error);

            Widget widgetAllArg = Widget.builder()
                    .id(1L)
                    .type(WidgetType.CHART)
                    .title("title")
                    .showTitle(true)
                    .showTitleIcon(false)
                    .titleFormat(titleFormat)
                    .xPosition(2)
                    .yPosition(3)
                    .height(4)
                    .width(5)
                    .version(6)
                    .contents(contents)
                    .error(error)
                    .build();

            // Assert
            assertThat(widgetNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(widgetAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Widget widget1 = Widget.builder().id(1L).build();
            Widget widget2 = Widget.builder().id(2L).build();

            // Act
            boolean result = widget1.equals(widget2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Widget widget1 = Widget.builder().id(1L).build();
            Widget widget2 = Widget.builder().id(1L).build();

            // Act
            boolean result = widget1.equals(widget2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Widget widget1 = Widget.builder().id(1L).title("different").build();
            Widget widget2 = Widget.builder().id(1L).title("titles").build();

            // Act
            boolean result = widget1.equals(widget2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Widget widget1 = Widget.builder().id(1L).build();
            Widget widget2 = Widget.builder().id(2L).build();

            // Act
            int hashCode1 = widget1.hashCode();
            int hashCode2 = widget2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Widget widget1 = Widget.builder().id(1L).build();
            Widget widget2 = Widget.builder().id(1L).build();

            // Act
            int hashCode1 = widget1.hashCode();
            int hashCode2 = widget2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Widget widget1 = Widget.builder().id(1L).title("different").build();
            Widget widget2 = Widget.builder().id(1L).title("titles").build();

            // Act
            int hashCode1 = widget1.hashCode();
            int hashCode2 = widget2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
