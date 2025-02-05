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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Represents a folder.
 * <p>
 * This class has the "equals" and "hashCode" methods overridden and will base equality based on if the "id" field is equal.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Builder
// We need to have a constructor with no arguments for the subclasses of this class to work
@NoArgsConstructor
// We need to have a constructor with all arguments for Lombok Builder to work
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Folder {
    /**
     * Represents the ID.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    /**
     * Represents the name.
     */
    private String name;

    /**
     * Represents the sheets contained in the folder.
     */
    private List<Sheet> sheets;

    /**
     * Represents the child folders contained in the folder.
     */
    private List<Folder> folders;

    /**
     * Represents the reports.
     */
    private List<Report> reports;

    /**
     * Represents the templates contained in the folder.
     */
    private List<Template> templates;

    /**
     * Represents the Sights contained in the folder.
     */
    private List<Sight> sights;

    /**
     * Returns if the user has marked the Folder as a Favorite in their Home tab.
     *
     * @deprecated As of API 3.2.2. Please use the isFavorite method in FavoritesResources instead.
     */
    @Deprecated(since = "3.2.2", forRemoval = true)
    private Boolean favorite;

    /**
     * Represents the Direct URL to Folder.
     */
    private String permalink;
}
