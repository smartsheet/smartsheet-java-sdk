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

import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.GlobalTemplate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateTest {
    @Nested
    class BuilderTests {
        @Test
        void templateBuilder() {
            // Arrange

            // Act
            Template templateNoArg = Template.builder().build();
            templateNoArg.setId(1L);
            templateNoArg.setName("name");
            templateNoArg.setDescription("description");
            templateNoArg.setAccessLevel(AccessLevel.ADMIN);
            templateNoArg.setImage("image");
            templateNoArg.setLargeImage("largeImage");
            templateNoArg.setLocale("locale");
            templateNoArg.setType("type");
            templateNoArg.setTags(List.of("tag1", "tag2"));
            templateNoArg.setCategories(List.of("category1", "category2"));
            templateNoArg.setBlank(true);
            templateNoArg.setGlobalTemplate(GlobalTemplate.TASK_LIST);

            Template templateAllArg = Template.builder()
                    .id(1L)
                    .name("name")
                    .description("description")
                    .accessLevel(AccessLevel.ADMIN)
                    .image("image")
                    .largeImage("largeImage")
                    .locale("locale")
                    .type("type")
                    .tags(List.of("tag1", "tag2"))
                    .categories(List.of("category1", "category2"))
                    .blank(true)
                    .globalTemplate(GlobalTemplate.TASK_LIST)
                    .build();

            // Assert
            assertThat(templateNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(templateAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Template template1 = Template.builder().id(1L).build();
            Template template2 = Template.builder().id(2L).build();

            // Act
            boolean result = template1.equals(template2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Template template1 = Template.builder().id(1L).build();
            Template template2 = Template.builder().id(1L).build();

            // Act
            boolean result = template1.equals(template2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Template template1 = Template.builder().id(1L).name("different").build();
            Template template2 = Template.builder().id(1L).name("names").build();

            // Act
            boolean result = template1.equals(template2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Template template1 = Template.builder().id(1L).build();
            Template template2 = Template.builder().id(2L).build();

            // Act
            int hashCode1 = template1.hashCode();
            int hashCode2 = template2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Template template1 = Template.builder().id(1L).build();
            Template template2 = Template.builder().id(1L).build();

            // Act
            int hashCode1 = template1.hashCode();
            int hashCode2 = template2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Template template1 = Template.builder().id(1L).name("different").build();
            Template template2 = Template.builder().id(1L).name("names").build();

            // Act
            int hashCode1 = template1.hashCode();
            int hashCode2 = template2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
