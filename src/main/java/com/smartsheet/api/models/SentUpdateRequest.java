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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartsheet.api.models.enums.UpdateRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * The SentUpdateRequest model.
 * <p>
 * This class has the "equals" and "hashCode" methods overridden and will base equality based on if the "id" field is equal.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Builder
public class SentUpdateRequest {
    /**
     * Represents the ID.
     * <p>
     * This excludes "id" field from being serialized to JSON. This is needed because when updating a resource,
     * the resource ID should be present and deserialized in the response, but it shouldn't be serialized and sent to Smartsheet REST API.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    /**
     * Id of the originating update request.
     */
    private Long updateRequestId;

    /**
     * The date and time for when the sent update request was sent to the recipient
     */
    private Date sentAt;

    /**
     * User object containing name and email of the sender
     */
    private User sentBy;

    /**
     * The status of the sent update request
     */
    private UpdateRequestStatus status;

    /**
     * Ids of rows for which update is requested.
     */
    private List<Long> rowIds;

    /**
     * Ids of columns included in the request.
     */
    private List<Long> columnIds;

    /**
     * A flag to indicate whether or not the attachments were include in the email.
     */
    private Boolean includeAttachments;

    /**
     * A flag to indicate whether or not the discussions were include in the email.
     */
    private Boolean includeDiscussions;

    /**
     * Recipient object
     */
    private Recipient sentTo;

    /**
     * The subject of the email
     */
    private String subject;

    /**
     * The message of the email
     */
    private String message;
}

