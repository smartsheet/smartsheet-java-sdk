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

import com.smartsheet.api.models.enums.ParentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiscussionTest {
    @Nested
    class BuilderTests {
        @Test
        void discussionBuilder() {
            // Act
            List<Comment> comments = List.of(new Comment());
            Comment comment = new Comment();
            List<Attachment> commentAttachments = List.of(new Attachment());
            Date lastCommentedAt = new Date();
            User lastCommentedUser = new User();
            User createdBy = new User();

            Discussion discussionNoArg = Discussion.builder().build();
            discussionNoArg.setId(1L);
            discussionNoArg.setTitle("Title");
            discussionNoArg.setComments(comments);
            discussionNoArg.setComment(comment);
            discussionNoArg.setCommentAttachments(commentAttachments);
            discussionNoArg.setCommentCount(5);
            discussionNoArg.setLastCommentedAt(lastCommentedAt);
            discussionNoArg.setLastCommentedUser(lastCommentedUser);
            discussionNoArg.setAccessLevel("Admin");
            discussionNoArg.setParentId(2L);
            discussionNoArg.setParentType(ParentType.SHEET);
            discussionNoArg.setCreatedBy(createdBy);
            discussionNoArg.setReadOnly(true);

            Discussion discussionAllArg = Discussion.builder()
                    .id(1L)
                    .title("Title")
                    .comments(comments)
                    .comment(new Comment())
                    .commentAttachments(commentAttachments)
                    .commentCount(5)
                    .lastCommentedAt(lastCommentedAt)
                    .lastCommentedUser(lastCommentedUser)
                    .accessLevel("Admin")
                    .parentId(2L)
                    .parentType(ParentType.SHEET)
                    .createdBy(createdBy)
                    .readOnly(true)
                    .build();

            // Assert
            assertThat(discussionNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(discussionAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Discussion discussion1 = Discussion.builder().id(1L).build();
            Discussion discussion2 = Discussion.builder().id(2L).build();

            // Act
            boolean result = discussion1.equals(discussion2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Discussion discussion1 = Discussion.builder().id(1L).build();
            Discussion discussion2 = Discussion.builder().id(1L).build();

            // Act
            boolean result = discussion1.equals(discussion2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Discussion discussion1 = Discussion.builder().id(1L).title("Title1").build();
            Discussion discussion2 = Discussion.builder().id(1L).title("Title2").build();

            // Act
            boolean result = discussion1.equals(discussion2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Discussion discussion1 = Discussion.builder().id(1L).build();
            Discussion discussion2 = Discussion.builder().id(2L).build();

            // Act
            int hashCode1 = discussion1.hashCode();
            int hashCode2 = discussion2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Discussion discussion1 = Discussion.builder().id(1L).build();
            Discussion discussion2 = Discussion.builder().id(1L).build();

            // Act
            int hashCode1 = discussion1.hashCode();
            int hashCode2 = discussion2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Discussion discussion1 = Discussion.builder().id(1L).title("Title1").build();
            Discussion discussion2 = Discussion.builder().id(1L).title("Title2").build();

            // Act
            int hashCode1 = discussion1.hashCode();
            int hashCode2 = discussion2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
