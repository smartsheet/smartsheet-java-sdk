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

class FolderTest {
    @Nested
    class BuilderTests {
        @Test
        void folderBuilder() {
            List<Sheet> sheets = List.of(new Sheet());
            List<Folder> folders = List.of(Folder.builder().build());
            List<Report> reports = List.of(new Report());
            List<Template> templates = List.of(Template.builder().build());
            List<Sight> sights = List.of(Sight.builder().build());

            // Act
            Folder folderNoArg = Folder.builder().build();
            folderNoArg.setId(1L);
            folderNoArg.setName("Folder Name");
            folderNoArg.setSheets(sheets);
            folderNoArg.setFolders(folders);
            folderNoArg.setReports(reports);
            folderNoArg.setTemplates(templates);
            folderNoArg.setSights(sights);
            folderNoArg.setFavorite(true);
            folderNoArg.setPermalink("http://example.com");

            Folder folderAllArg = Folder.builder()
                    .id(1L)
                    .name("Folder Name")
                    .sheets(sheets)
                    .folders(folders)
                    .reports(reports)
                    .templates(templates)
                    .sights(sights)
                    .favorite(true)
                    .permalink("http://example.com")
                    .build();

            // Assert
            assertThat(folderNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(folderAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Folder folder1 = Folder.builder().id(1L).build();
            Folder folder2 = Folder.builder().id(2L).build();

            // Act
            boolean result = folder1.equals(folder2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Folder folder1 = Folder.builder().id(1L).build();
            Folder folder2 = Folder.builder().id(1L).build();

            // Act
            boolean result = folder1.equals(folder2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Folder folder1 = Folder.builder().id(1L).name("Name1").build();
            Folder folder2 = Folder.builder().id(1L).name("Name2").build();

            // Act
            boolean result = folder1.equals(folder2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Folder folder1 = Folder.builder().id(1L).build();
            Folder folder2 = Folder.builder().id(2L).build();

            // Act
            int hashCode1 = folder1.hashCode();
            int hashCode2 = folder2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Folder folder1 = Folder.builder().id(1L).build();
            Folder folder2 = Folder.builder().id(1L).build();

            // Act
            int hashCode1 = folder1.hashCode();
            int hashCode2 = folder2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Folder folder1 = Folder.builder().id(1L).name("Name1").build();
            Folder folder2 = Folder.builder().id(1L).name("Name2").build();

            // Act
            int hashCode1 = folder1.hashCode();
            int hashCode2 = folder2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
