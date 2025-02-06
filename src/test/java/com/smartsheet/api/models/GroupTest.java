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

class GroupTest {
    @Nested
    class BuilderTests {
        @Test
        void groupBuilder() {
            // Act
            Date createdAtDate = new Date();
            Date modifiedAtDate = new Date();
            List<GroupMember> members = List.of(new GroupMember());

            Group groupNoArg = Group.builder().build();
            groupNoArg.setId(1L);
            groupNoArg.setName("Group Name");
            groupNoArg.setDescription("Description");
            groupNoArg.setOwner("owner@example.com");
            groupNoArg.setOwnerId(2L);
            groupNoArg.setCreatedAt(createdAtDate);
            groupNoArg.setModifiedAt(modifiedAtDate);
            groupNoArg.setMembers(members);

            Group groupAllArg = Group.builder()
                    .id(1L)
                    .name("Group Name")
                    .description("Description")
                    .owner("owner@example.com")
                    .ownerId(2L)
                    .createdAt(createdAtDate)
                    .modifiedAt(modifiedAtDate)
                    .members(members)
                    .build();

            // Assert
            assertThat(groupNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(groupAllArg);
        }
    }

    @Nested
    class EqualsTests {
        @Test
        void testEquals_differentIds() {
            // Arrange
            Group group1 = Group.builder().id(1L).build();
            Group group2 = Group.builder().id(2L).build();

            // Act
            boolean result = group1.equals(group2);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void testEquals_sameIds() {
            // Arrange
            Group group1 = Group.builder().id(1L).build();
            Group group2 = Group.builder().id(1L).build();

            // Act
            boolean result = group1.equals(group2);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void testEquals_sameIdsDifferentOtherFields() {
            // Arrange
            Group group1 = Group.builder().id(1L).name("Name1").build();
            Group group2 = Group.builder().id(1L).name("Name2").build();

            // Act
            boolean result = group1.equals(group2);

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Nested
    class HashCodeTests {
        @Test
        void testHashCode_differentIds() {
            // Arrange
            Group group1 = Group.builder().id(1L).build();
            Group group2 = Group.builder().id(2L).build();

            // Act
            int hashCode1 = group1.hashCode();
            int hashCode2 = group2.hashCode();

            // Assert
            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIds() {
            // Arrange
            Group group1 = Group.builder().id(1L).build();
            Group group2 = Group.builder().id(1L).build();

            // Act
            int hashCode1 = group1.hashCode();
            int hashCode2 = group2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        void testHashCode_sameIdsDifferentOtherFields() {
            // Arrange
            Group group1 = Group.builder().id(1L).name("Name1").build();
            Group group2 = Group.builder().id(1L).name("Name2").build();

            // Act
            int hashCode1 = group1.hashCode();
            int hashCode2 = group2.hashCode();

            // Assert
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
