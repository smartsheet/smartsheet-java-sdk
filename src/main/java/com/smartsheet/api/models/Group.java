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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

/**
 * Represents a Group Object.
 * <p>
 * This class has the "equals" and "hashCode" methods overridden and will base equality based on if the "id" field is equal.
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/1554165-managing-groups-team-enterprise-only-">Managing groups</a>
 */
@Getter
@Setter
// Only include explicitly included fields in the toString method so clients don't log sensitive data (such as name/email)
@ToString(onlyExplicitlyIncluded = true)
@Jacksonized
@EqualsAndHashCode(of = "id")
@Builder
public class Group {
    /**
     * Represents the ID.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Include
    private Long id;

    /**
     * Represents the name.
     */
    private String name;

    /**
     * The description of the group.
     */
    private String description;

    /**
     * The email address of the owner of the group.
     */
    private String owner;

    /**
     * The id of the owner of the group.
     */
    @ToString.Include
    private Long ownerId;

    /**
     * The date when the group was created.
     */
    @ToString.Include
    private Date createdAt;

    /**
     * The date when the group was last modified.
     */
    @ToString.Include
    private Date modifiedAt;

    /**
     * The list of members in the group.
     */
    private List<GroupMember> members;
}
