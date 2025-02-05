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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartsheet.api.models.enums.ParentType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

/**
 * Represents the Discussion object.
 * <p>
 * This class has the "equals" and "hashCode" methods overridden and will base equality based on if the "id" field is equal.
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/504767-using-discussions">Help Using Discussions</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Jacksonized
@Builder
public class Discussion {
    /**
     * Represents the ID.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    /**
     * Represents the title for the discussion.
     */
    private String title;

    /**
     * Represents the comments for the discussion.
     */
    private List<Comment> comments;

    /**
     * Represents the comment for the discussion (outbound only - singular "comment")
     */
    private Comment comment;

    /**
     * Represents the comment attachments.
     */
    private List<Attachment> commentAttachments;

    /**
     * The number of comments in the discussion.
     */
    private Integer commentCount;

    /**
     * Represents the date a comment was last added to a discussion.
     */
    private Date lastCommentedAt;

    /**
     * Represents the last user that left a comment in the discussion.
     */
    private User lastCommentedUser;

    /**
     * Users permission on the Discussion
     */
    private String accessLevel;

    /**
     * Represents ID of the directly associated row or sheet.
     */
    private Long parentId;

    /**
     * Represents the “SHEET” or “ROW”: present only when the direct association is not clear.
     */
    private ParentType parentType;

    /**
     * Represents the User object containing name and email of the creator of the Discussion.
     */
    private User createdBy;

    /**
     * Represents the status of the Discussion.
     */
    private Boolean readOnly;
}
