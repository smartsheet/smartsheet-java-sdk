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

import com.smartsheet.api.models.enums.UserStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * Represents a user model with basic user information.
 */
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class UserModel extends IdentifiableModel<Long> {
    /**
     * Represents the email address.
     */
    private String email;

    /**
     * Represents the first name.
     */
    private String firstName;

    /**
     * Represents the last name.
     */
    private String lastName;

    /**
     * Represents the admin flag which allows managing users and accounts.
     */
    private Boolean admin;

    /**
     * Represents the licensed sheet creator flag which allows creating and owning sheets.
     */
    private Boolean licensedSheetCreator;

    /**
     * Represents the resource manager flag which allows the user access to the Resource Manager functionality.
     */
    private Boolean resourceViewer;

    /**
     * Represents the group admin flag which allows users to create and modify groups.
     */
    private Boolean groupAdmin;

    /**
     * Represents the user status (active, pending, declined).
     */
    private UserStatus status;

    /**
     * An array of AlternateEmail Objects representing the alternate email addresses associated with the User account
     */
    private List<AlternateEmail> alternateEmails;

    /**
     * The number of sheets owned by the current user within the organization
     */
    private Integer sheetCount;

    /**
     * Last login time of the current user
     */
    private Date lastLogin;

    /**
     * Timestamp of viewing an Enterprise Custom Welcome Screen by the current user
     */
    private Date customWelcomeScreenViewed;

    /**
     * Company name from the user's profile
     */
    private String company;

    /**
     * Department name from the user's profile
     */
    private String department;

    /**
     * User's mobile phone number from the profile
     */
    private String mobilePhone;

    /**
     * Link to the user's profile image
     */
    private ProfileImage profileImage;

    /**
     * User's role
     */
    private String role;

    /**
     * User's title
     */
    private String title;

    /**
     * Work phone number from the user's profile
     */
    private String workPhone;
}
