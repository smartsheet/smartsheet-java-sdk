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

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {
    @Nested
    class BuilderTests {
        @Test
        void commentBuilder() {
            // Act
            User createdBy = new User();
            Date modifiedDate = new Date();
            List<Attachment> attachments = List.of(new Attachment());
            Date createdAt = new Date();
            Date modifiedAt = new Date();

            Comment commentNoArg = Comment.builder().build();
            commentNoArg.setId(1L);
            commentNoArg.setText("Sample text");
            commentNoArg.setCreatedBy(createdBy);
            commentNoArg.setModifiedDate(modifiedDate);
            commentNoArg.setAttachments(attachments);
            commentNoArg.setDiscussionId(2L);
            commentNoArg.setCreatedAt(createdAt);
            commentNoArg.setModifiedAt(modifiedAt);

            Comment commentAllArg = Comment.builder()
                    .id(1L)
                    .text("Sample text")
                    .createdBy(createdBy)
                    .modifiedDate(modifiedDate)
                    .attachments(attachments)
                    .discussionId(2L)
                    .createdAt(createdAt)
                    .modifiedAt(modifiedAt)
                    .build();

            // Assert
            assertThat(commentNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(commentAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Comment comment1 = Comment.builder().id(1L).build();
            Comment comment2 = Comment.builder().id(2L).build();

            // Act
            boolean result = comment1.equals(comment2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Comment comment1 = Comment.builder().id(1L).build();
            Comment comment2 = Comment.builder().id(1L).build();

            // Act
            boolean result = comment1.equals(comment2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Comment comment1 = Comment.builder().id(1L).text("Text1").build();
            Comment comment2 = Comment.builder().id(1L).text("Text2").build();

            // Act
            boolean result = comment1.equals(comment2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Comment comment1 = Comment.builder().id(1L).build();
            Comment comment2 = Comment.builder().id(2L).build();

            // Act
            int hashCode1 = comment1.hashCode();
            int hashCode2 = comment2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Comment comment1 = Comment.builder().id(1L).build();
            Comment comment2 = Comment.builder().id(1L).build();

            // Act
            int hashCode1 = comment1.hashCode();
            int hashCode2 = comment2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Comment comment1 = Comment.builder().id(1L).text("Text1").build();
            Comment comment2 = Comment.builder().id(1L).text("Text2").build();

            // Act
            int hashCode1 = comment1.hashCode();
            int hashCode2 = comment2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
