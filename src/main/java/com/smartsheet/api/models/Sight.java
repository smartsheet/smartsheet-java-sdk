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

import com.smartsheet.api.models.enums.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Sight {
    /**
     * Represents the ID.
     */
    private Long id;

    /**
     * Represents the name.
     */
    private String name;

    /**
     * Number of columns that the Sight contains
     */
    private Integer columnCount;

    /**
     * Array of Widget objects
     */
    private List<Widget> widgets;

    /**
     * Indicates whether the User has marked the Sight as a favorite
     */
    private Boolean favorite;

    /**
     * User's permissions on the Sight.
     */
    private AccessLevel accessLevel;

    /**
     * URL that represents a direct link to the Sight
     */
    private String permalink;

    /**
     * Time of creation
     */
    private Date createdAt;

    /**
     * Time last modified
     */
    private Date modifiedAt;

    /**
     * Represents the source of the sheet.
     */
    private Source source;

    /**
     * A workspace object, limited to only id and Name
     */
    private Workspace workspace;

    /**
     * The background color of the Sight
     */
    private String backgroundColor;
}

