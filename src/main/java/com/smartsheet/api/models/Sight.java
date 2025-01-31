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

import java.util.Date;
import java.util.List;

public class Sight extends NamedModel<Long> {

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
     *
     * @deprecated As of API 3.2.2. Please use the isFavorite method in FavoritesResources instead.
     */
    @Deprecated(since = "3.2.2", forRemoval = true)
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

    /**
     * Provide an 'override' of setName (returns Sight not NamedModel)
     *
     * @param name the new name
     */
    public Sight setName(String name) {
        super.setName(name);
        return this;
    }

    /**
     * Get the number of columns that the Sight contains
     *
     * @return columnCount
     */
    public Integer getColumnCount() {
        return columnCount;
    }

    /**
     * Set the number of columns that the Sight contains
     */
    public Sight setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    /**
     * Get the array of Sight widgets
     *
     * @return array of widgets
     */
    public List<Widget> getWidgets() {
        return widgets;
    }

    /**
     * Set the array of Sight widgets
     */
    public Sight setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
        return this;
    }

    /**
     * Get flag indicating whether the user has marked the Sight as a favorite
     *
     * @return favorite flag
     *
     * @deprecated As of API 3.2.2. Please use the isFavorite method in FavoritesResources instead.
     */
    @Deprecated(since = "3.2.2", forRemoval = true)
    public Boolean getFavorite() {
        return favorite;
    }

    /**
     * Set flag indicating whether the user has marked the Sight as a favorite
     *
     * @deprecated As of API 3.2.2. Please use the isFavorite method in FavoritesResources instead.
     */
    @Deprecated(since = "3.2.2", forRemoval = true)
    public Sight setFavorite(Boolean favorite) {
        this.favorite = favorite;
        return this;
    }

    /**
     * User's permissions on the Sight (OWNDER, ADMIN, VIEWER)
     *
     * @return accessLevel
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Set User's permissions on the Sight
     */
    public Sight setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }

    /**
     * URL that represents a direct link to the Sight
     *
     * @return permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * Set URL pointing to a direct link to the Sight
     */
    public Sight setPermalink(String permalink) {
        this.permalink = permalink;
        return this;
    }

    /**
     * Get time of Sight creation
     *
     * @return createdAt (Date)
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Set time of Sight creation
     */
    public Sight setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Get time of last modification
     *
     * @return modifiedAt (Date)
     */
    public Date getModifiedAt() {
        return modifiedAt;
    }

    /**
     * Set time of last modification
     */
    public Sight setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    /**
     * Gets the source.
     *
     * @return the source
     */
    public Source getSource() {
        return source;
    }

    /**
     * Sets the source.
     *
     * @param source the source
     */
    public Sight setSource(Source source) {
        this.source = source;
        return this;
    }

    /**
     * A workspace object limited to only id and name
     *
     * @return workspace
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Set workspace object for this Sight (limited to only id and name)
     */
    public Sight setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        return this;
    }

    /**
     * Get the background color of the Sight
     *
     * @return the background color
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Set the background color of the Sight
     *
     * @param backgroundColor the background color
     */
    public Sight setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
}

