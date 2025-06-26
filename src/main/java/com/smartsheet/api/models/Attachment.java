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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

/**
 * Represents the Attachment object.
 *
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/518408-uploading-attachments">Help Uploading
 * Attachments</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Attachment {

    /**
     * Represents the ID.
     */
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Represents the name.
     */
    private String name;

    /**
     * Represents the URL.
     */
    private String url;

    /**
     * Represents the URL expiration time.
     */
    private Long urlExpiresInMillis;

    /**
     * Represents the attachment type.
     */
    private AttachmentType attachmentType;

    /**
     * Represents the attachment sub type.
     */
    private AttachmentSubType attachmentSubType;

    /**
     * Represents the creation timestamp.
     */
    private Date createdAt;

    /**
     * Represents the MIME type.
     */
    private String mimeType;

    /**
     * Represents the parent type.
     */
    private AttachmentParentType parentType;

    /**
     * Represents the parent ID.
     */
    private Long parentId;

    /**
     * Represents the attachment size.
     */
    private Long sizeInKb;

    /**
     * The user who created the attachment.
     */
    private User createdBy;

    /**
     * Represents the attachment description
     */
    private String description;
}
