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

import java.util.List;

/**
 * Represents one specific result of a search.
 */
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

    /**
     * Gets the text for this specific search result.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text for this specific search result.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the object id for this specific search result.
     *
     * @return the object id
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * Sets the object id for this specific search result.
     *
     * @param objectId the new object id
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    /**
     * Gets the object type for this specific search result.
     *
     * @return the object type
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the object type for this specific search result.
     *
     * @param objectType the new object type
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * Gets the parent object id for this specific search result.
     *
     * @return the parent object id
     */
    public Long getParentObjectId() {
        return parentObjectId;
    }

    /**
     * Sets the parent object id for this specific search result.
     *
     * @param parentObjectId the new parent object id
     */
    public void setParentObjectId(Long parentObjectId) {
        this.parentObjectId = parentObjectId;
    }

    /**
     * Gets the parent object type for this specific search result.
     *
     * @return the parent object type
     */
    public String getParentObjectType() {
        return parentObjectType;
    }

    /**
     * Sets the parent object type for this specific search result.
     *
     * @param parentObjectType the new parent object type
     */
    public void setParentObjectType(String parentObjectType) {
        this.parentObjectType = parentObjectType;
    }

    /**
     * Gets the parent object name for this specific search result.
     *
     * @return the parent object name
     */
    public String getParentObjectName() {
        return parentObjectName;
    }

    /**
     * Sets the parent object name for this specific search result.
     *
     * @param parentObjectName the new parent object name
     */
    public void setParentObjectName(String parentObjectName) {
        this.parentObjectName = parentObjectName;
    }

    /**
     * Gets the context data for this specific search result.
     *
     * @return the context data
     */
    public List<String> getContextData() {
        return contextData;
    }

    /**
     * Sets the context data for this specific search result.
     *
     * @param contextData the new context data
     */
    public void setContextData(List<String> contextData) {
        this.contextData = contextData;
    }

    /**
     * Get the attachment type if the search result item is an attachment
     *
     * @return the attachment type
     */
    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    /**
     * Set the attachment type if the search result item is an attachment
     *
     * @param attachmentType the attachment type
     */
    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    /**
     * Gets the mime type.
     *
     * @return the mime type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the mime type.
     *
     * @param mimeType the new mime type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Get a flag indicating if the search result item is a favorite
     *
     * @return the favorite flag
     */
    public Boolean getFavorite() {
        return favorite;
    }

    /**
     * Set a flag indicating if the search result item is a favorite
     *
     * @param favorite the favorite flag
     */
    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Get a flag indicating if the search result item parent is a favorite
     *
     * @return the favorite flag
     */
    public Boolean getParentObjectFavorite() {
        return parentObjectFavorite;
    }

    /**
     * Set a flag indicating if the search result item parent is a favorite
     *
     * @param parentObjectFavorite the favorite flag
     */
    public void setParentObjectFavorite(Boolean parentObjectFavorite) {
        this.parentObjectFavorite = parentObjectFavorite;
    }
}
