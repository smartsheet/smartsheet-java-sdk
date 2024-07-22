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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SightTest {
    @Nested
    class BuilderTests {
        @Test
        void sightBuilder() {
            // Arrange
            // Common Objects
            Date createdAt = new Date();
            Date modifiedAt = new Date();
            Workspace workspace = new Workspace();

            // Act
            Sight sightNoArg = Sight.builder().build();
            sightNoArg.setId(1L);
            sightNoArg.setName("name");
            sightNoArg.setColumnCount(2);
            sightNoArg.setWidgets(List.of(Widget.builder().id(1L).build(), Widget.builder().id(2L).build()));
            sightNoArg.setFavorite(true);
            sightNoArg.setAccessLevel(AccessLevel.EDITOR);
            sightNoArg.setPermalink("permalink");
            sightNoArg.setCreatedAt(createdAt);
            sightNoArg.setModifiedAt(modifiedAt);
            sightNoArg.setSource(Source.builder().id(99L).build());
            sightNoArg.setWorkspace(workspace);
            sightNoArg.setBackgroundColor("backgroundColor");

            Sight sightAllArg = Sight.builder()
                    .id(1L)
                    .name("name")
                    .columnCount(2)
                    .widgets(List.of(Widget.builder().id(1L).build(), Widget.builder().id(2L).build()))
                    .favorite(true)
                    .accessLevel(AccessLevel.EDITOR)
                    .permalink("permalink")
                    .createdAt(createdAt)
                    .modifiedAt(modifiedAt)
                    .source(Source.builder().id(99L).build())
                    .workspace(workspace)
                    .backgroundColor("backgroundColor")
                    .build();


            // Assert
            assertThat(sightNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(sightAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Sight sight1 = Sight.builder().id(1L).build();
            Sight sight2 = Sight.builder().id(2L).build();

            // Act
            boolean result = sight1.equals(sight2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Sight sight1 = Sight.builder().id(1L).build();
            Sight sight2 = Sight.builder().id(1L).build();

            // Act
            boolean result = sight1.equals(sight2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Sight sight1 = Sight.builder().id(1L).name("different").build();
            Sight sight2 = Sight.builder().id(1L).name("names").build();

            // Act
            boolean result = sight1.equals(sight2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Sight sight1 = Sight.builder().id(1L).build();
            Sight sight2 = Sight.builder().id(2L).build();

            // Act
            int hashCode1 = sight1.hashCode();
            int hashCode2 = sight2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Sight sight1 = Sight.builder().id(1L).build();
            Sight sight2 = Sight.builder().id(1L).build();

            // Act
            int hashCode1 = sight1.hashCode();
            int hashCode2 = sight2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Sight sight1 = Sight.builder().id(1L).name("different").build();
            Sight sight2 = Sight.builder().id(1L).name("names").build();

            // Act
            int hashCode1 = sight1.hashCode();
            int hashCode2 = sight2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
