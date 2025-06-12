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
 * Represents an UpdateShareRequest object in the Smartsheet API.
 */
public class UpdateShareRequest {
    /**
     * The access level to grant to the user or group.
     */
    private AccessLevel accessLevel;

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
     * @return this UpdateShareRequest
     */
    public UpdateShareRequest setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }

    /**
     * A convenience class for creating an {@link UpdateShareRequest} with the appropriate fields.
     */
    public static class UpdateShareRequestBuilder {
        private AccessLevel accessLevel;

        /**
         * Sets the access level.
         *
         * @param accessLevel the access level
         * @return this UpdateShareRequestBuilder
         */
        public UpdateShareRequestBuilder setAccessLevel(AccessLevel accessLevel) {
            this.accessLevel = accessLevel;
            return this;
        }

        /**
         * Builds the UpdateShareRequest.
         *
         * @return the UpdateShareRequest
         */
        public UpdateShareRequest build() {
            UpdateShareRequest request = new UpdateShareRequest();
            request.setAccessLevel(accessLevel);
            return request;
        }
    }
}
