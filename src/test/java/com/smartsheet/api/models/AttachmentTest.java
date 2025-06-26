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

import com.smartsheet.api.models.enums.AttachmentParentType;
import com.smartsheet.api.models.enums.AttachmentSubType;
import com.smartsheet.api.models.enums.AttachmentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class AttachmentTest {

    @Nested
    class BuilderTests {
        @Test
        void attachmentBuilder() {
            // Arrange
            Date createdAt = new Date();
            User createdBy = new User();
            createdBy.setId(123L);
            createdBy.setName("Test User");

            // Act
            Attachment attachmentNoArg = Attachment.builder().build();
            attachmentNoArg.setId(1L);
            attachmentNoArg.setName("Test Attachment");
            attachmentNoArg.setUrl("https://example.com");
            attachmentNoArg.setUrlExpiresInMillis(3600000L);
            attachmentNoArg.setAttachmentType(AttachmentType.FILE);
            attachmentNoArg.setAttachmentSubType(AttachmentSubType.PDF);
            attachmentNoArg.setCreatedAt(createdAt);
            attachmentNoArg.setMimeType("application/pdf");
            attachmentNoArg.setParentType(AttachmentParentType.SHEET);
            attachmentNoArg.setParentId(100L);
            attachmentNoArg.setSizeInKb(1024L);
            attachmentNoArg.setCreatedBy(createdBy);
            attachmentNoArg.setDescription("Test Description");

            Attachment attachmentAllArg = Attachment.builder()
                    .id(1L)
                    .name("Test Attachment")
                    .url("https://example.com")
                    .urlExpiresInMillis(3600000L)
                    .attachmentType(AttachmentType.FILE)
                    .attachmentSubType(AttachmentSubType.PDF)
                    .createdAt(createdAt)
                    .mimeType("application/pdf")
                    .parentType(AttachmentParentType.SHEET)
                    .parentId(100L)
                    .sizeInKb(1024L)
                    .createdBy(createdBy)
                    .description("Test Description")
                    .build();

            // Assert
            assertThat(attachmentNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(attachmentAllArg);
        }
    }

    @Nested
    class EqualsAndHashCodeTests {
        @Test
        void equalsAndHashCode() {
            // Arrange
            Attachment attachment1 = Attachment.builder().id(1L).name("Test Attachment").build();
            Attachment attachment2 = Attachment.builder().id(1L).name("Different Name").build();
            Attachment attachment3 = Attachment.builder().id(2L).name("Test Attachment").build();

            // Assert
            assertThat(attachment1).isEqualTo(attachment1);
            assertThat(attachment1).isEqualTo(attachment2);
            assertThat(attachment1).isNotEqualTo(attachment3);
            assertThat(attachment1.hashCode()).isEqualTo(attachment2.hashCode());
            assertThat(attachment1.hashCode()).isNotEqualTo(attachment3.hashCode());
        }
    }

    @Nested
    class ToStringTests {
        @Test
        void toString_containsAllFields() {
            // Arrange
            Attachment attachment = Attachment.builder()
                    .id(1L)
                    .name("Test Attachment")
                    .url("https://example.com")
                    .build();

            // Act
            String toString = attachment.toString();

            // Assert
            assertThat(toString).contains("id=1");
            assertThat(toString).contains("name=Test Attachment");
            assertThat(toString).contains("url=https://example.com");
        }
    }
}
