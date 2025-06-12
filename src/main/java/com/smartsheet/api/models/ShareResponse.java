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

import com.smartsheet.api.models.enums.AccessLevel;

/**
 * Represents a ShareResponse object in the Smartsheet API.
 */
public class ShareResponse {
    /**
     * The id of the share.
     */
    private String id;

    /**
     * User's primary email address for user shares.
     */
    private String email;

    /**
     * User Id if the share is a user share.
     */
    private Long userId;

    /**
     * Group Id if the share is a group share.
     */
    private Long groupId;

    /**
     * If present, the name of the user or group to which the asset is shared.
     */
    private String name;

    /**
     * The type of this share. One of the following values: GROUP or USER.
     */
    private String type;

    /**
     * The access level of the share.
     */
    private AccessLevel accessLevel;

    /**
     * The scope of this share. One of the following values:
     * ITEM: an item-level share (that is, the specific object to which the share applies is shared with the user or group).
     * WORKSPACE: a workspace-level share (that is, the workspace that contains the asset to which the share applies
     * is shared with the user or group).
     */
    private String scope;

    /**
     * Gets the id of the share.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the share.
     *
     * @param id the id
     */
    public ShareResponse setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email
     */
    public ShareResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the user id
     */
    public ShareResponse setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Gets the group id.
     *
     * @return the group id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Sets the group id.
     *
     * @param groupId the group id
     */
    public ShareResponse setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * Gets the name of the user or group.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user or group.
     *
     * @param name the name
     */
    public ShareResponse setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the type of the share.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the share.
     *
     * @param type the type
     */
    public ShareResponse setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the access level of the share.
     *
     * @return the access level
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level of the share.
     *
     * @param accessLevel the access level
     */
    public ShareResponse setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }

    /**
     * Gets the scope of the share.
     *
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the scope of the share.
     *
     * @param scope the scope
     */
    public ShareResponse setScope(String scope) {
        this.scope = scope;
        return this;
    }
}
