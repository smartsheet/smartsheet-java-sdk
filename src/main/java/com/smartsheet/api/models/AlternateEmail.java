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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * The AlternateEmail object, retruned by endpoints like
 * the <a href="https://smartsheet.redoc.ly/tag/alternateEmailAddress#operation/get-alternate-email">Get Alternate Email endpoint</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "AlternateEmailBuilder")
@Jacksonized
public class AlternateEmail {

    /**
     * The alternate email id
     */
    private Long id;

    /**
     * The user's alternate email address (user@example.com)
     */
    private String email;

    /**
     * Flag indicating whether the alternate email address has been confirmed
     */
    private Boolean confirmed;

    /**
     * A convenience method for creating an AlternateEmail with just an email address
     *
     * @param email the email address
     * @return a new AlternateEmail
     */
    public static AlternateEmail createAlternateEmail(String email) {
        if (email == null) {
            throw new InstantiationError("An email address must be set.");
        }
        return builder().email(email).build();
    }
    
    /**
     * Custom builder implementation to validate email
     */
    public static class AlternateEmailBuilder {
        public AlternateEmail build() {
            if (this.email == null) {
                throw new InstantiationError("An email address must be set.");
            }
            return new AlternateEmail(this.id, this.email, this.confirmed);
        }
    }
}
