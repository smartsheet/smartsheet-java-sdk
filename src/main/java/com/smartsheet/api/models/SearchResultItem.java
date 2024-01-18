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

import com.smartsheet.api.models.enums.AttachmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Represents one specific result of a search.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class SearchResultItem {
    /**
     * Represents the text for this specific search result.
     */
    private String text;

    /**
     * Represents the object ID for this specific search result.
     */
    private Long objectId;

    /**
     * Represents the object type (row, discussion, attach) for this specific search result.
     */
    private String objectType;

    /**
     * Represents the parent object ID for this specific search result.
     */
    private Long parentObjectId;

    /**
     * Represents the parent object type for this specific search result.
     */
    private String parentObjectType;

    /**
     * Represents the parent object name for this specific search result.
     */
    private String parentObjectName;

    /**
     * Represents the context data for this specific search result.
     */
    private List<String> contextData;

    /**
     * Represents the attachment type if the search result item is an attachment.
     */
    private AttachmentType attachmentType;

    /**
     * Represents the MIME type.
     */
    private String mimeType;

    /**
     * If the search result item is a favorite
     */
    private Boolean favorite;

    /**
     * If the parent object of the search item is a favorite
     */
    private Boolean parentObjectFavorite;
}
