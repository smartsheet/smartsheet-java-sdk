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
 * Represents a CreateShareRequest object in the Smartsheet API.
 */
public class CreateShareRequest {
    /**
     * The primary email address of a user to share to. Must be provided if groupId is not provided.
     */
    private String email;

    /**
     * The id of the group to share to. Must be provided if email is not provided.
     */
    private Long groupId;

    /**
     * The access level to grant to the user or group.
     */
    private AccessLevel accessLevel;

    /**
     * The subject of the email that is optionally sent to notify the recipient.
     * Must set the sendEmail query parameter to true for this parameter to take effect.
     */
    private String subject;

    /**
     * The message included in the body of the email that is optionally sent to the recipient.
     * Must set the sendEmail query parameter to true for this parameter to take effect.
     */
    private String message;

    /**
     * Indicates whether to send a copy of the email to the sharer of the sheet.
     * Must set the sendEmail query parameter to true for this parameter to take effect.
     */
    private Boolean ccMe;

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
     * @return this CreateShareRequest
     */
    public CreateShareRequest setEmail(String email) {
        this.email = email;
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
     * @return this CreateShareRequest
     */
    public CreateShareRequest setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * Gets the access level.
     *
     * @return the access level
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level.
     *
     * @param accessLevel the access level
     * @return this CreateShareRequest
     */
    public CreateShareRequest setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }

    /**
     * Gets the subject of the email.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the email.
     *
     * @param subject the subject
     * @return this CreateShareRequest
     */
    public CreateShareRequest setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Gets the message of the email.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the email.
     *
     * @param message the message
     * @return this CreateShareRequest
     */
    public CreateShareRequest setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Gets the ccMe flag.
     *
     * @return the ccMe flag
     */
    public Boolean getCcMe() {
        return ccMe;
    }

    /**
     * Sets the ccMe flag.
     *
     * @param ccMe the ccMe flag
     * @return this CreateShareRequest
     */
    public CreateShareRequest setCcMe(Boolean ccMe) {
        this.ccMe = ccMe;
        return this;
    }

    /**
     * A convenience class for creating a {@link CreateShareRequest} with the appropriate fields.
     */
    public static class CreateShareRequestBuilder {
        private String email;
        private Long groupId;
        private AccessLevel accessLevel;
        private String subject;
        private String message;
        private Boolean ccMe;

        /**
         * Sets the email of the user.
         *
         * @param email the email
         * @return this CreateShareRequestBuilder
         */
        public CreateShareRequestBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the group id.
         *
         * @param groupId the group id
         * @return this CreateShareRequestBuilder
         */
        public CreateShareRequestBuilder setGroupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        /**
         * Sets the access level.
         *
         * @param accessLevel the access level
         * @return this CreateShareRequestBuilder
         */
        public CreateShareRequestBuilder setAccessLevel(AccessLevel accessLevel) {
            this.accessLevel = accessLevel;
            return this;
        }

        /**
         * Sets the subject of the email.
         *
         * @param subject the subject
         * @return this CreateShareRequestBuilder
         */
        public CreateShareRequestBuilder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Sets the message of the email.
         *
         * @param message the message
         * @return this CreateShareRequestBuilder
         */
        public CreateShareRequestBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the ccMe flag.
         *
         * @param ccMe the ccMe flag
         * @return this CreateShareRequestBuilder
         */
        public CreateShareRequestBuilder setCcMe(Boolean ccMe) {
            this.ccMe = ccMe;
            return this;
        }

        /**
         * Builds the CreateShareRequest.
         *
         * @return the CreateShareRequest
         */
        public CreateShareRequest build() {
            CreateShareRequest request = new CreateShareRequest();
            request.setEmail(email);
            request.setGroupId(groupId);
            request.setAccessLevel(accessLevel);
            request.setSubject(subject);
            request.setMessage(message);
            request.setCcMe(ccMe);
            return request;
        }
    }
}
