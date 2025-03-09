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

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

/**
 * Represents the Comment object.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Jacksonized
@Builder
public class Comment {
    /**
     * Represents the ID.
     */
    private Long id;

    /**
     * Represents the text for the comment.
     */
    private String text;

    /**
     * Represents the user that created the comment.
     */
    private User createdBy;

    /**
     * Represents the date the comment was modified.
     */
    private Date modifiedDate;

    /**
     * Represents the attachments for the comment.
     */
    private List<Attachment> attachments;

    /**
     * Represents the discussion ID.
     */
    private Long discussionId;

    /**
     * The date the comment was created.
     */
    private Date createdAt;

    /**
     * The date the comment was last modified.
     */
    private Date modifiedAt;
}
