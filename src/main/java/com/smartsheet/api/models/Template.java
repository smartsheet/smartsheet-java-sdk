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
import com.smartsheet.api.models.enums.ChildResourceType;
import com.smartsheet.api.models.enums.GlobalTemplate;

import java.util.Date;
import java.util.List;

/**
 * A template object that is a default layout for future sheets.
 *
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/522123-using-templates">Using Templates Help</a>
 */
public class Template extends NamedModel<Long> {
    /**
     * Represents the description for the template.
     */
    private String description;

    /**
     * Represents the access level for the template.
     */
    private AccessLevel accessLevel;

    /**
     * URL to the small preview image for this template
     */
    private String image;

    /**
     * URL to the large preview image for this template
     */
    private String largeImage;

    /**
     * Locale of the template
     */
    private String locale;

    /**
     * Type of the template. One of "sheet" or "report"
     */
    private String type;

    /**
     * List of search tags for this template
     */
    private List<String> tags;

    /**
     * List of categories this template belongs to
     */
    private List<String> categories;

    /**
     * Flag indicating whether the template is blank
     */
    private Boolean blank;

    /**
     * Type of global template. One of "BLANK_SHEET", "TASK_LIST", or "PROJECT_SHEET"
     */
    private GlobalTemplate globalTemplate;

    /**
     * The resource permalink
     */
    private String permalink;

    /**
     * The resource creation date
     */
    private Date createdAt;

    /**
     * The resource last modified date
     */
    private Date modifiedAt;

    /**
     * The resource type
     */
    private ChildResourceType resourceType;

    /**
     * Gets the description of the template.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the template.
     *
     * @param description the new description
     */
    public Template setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the access level of the template.
     *
     * @return the access level
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level of the template.
     *
     * @param accessLevel the new access level
     */
    public Template setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }

    /**
     * Gets the URL to the small preview image for this template.
     *
     * @return the URL
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the URL to the small preview image for this template.
     */
    public Template setImage(String image) {
        this.image = image;
        return this;
    }

    /**
     * Gets the URL to the large preview image for this template.
     *
     * @return the URL
     */
    public String getLargeImage() {
        return largeImage;
    }

    /**
     * Sets the URL to the large preview image for this template.
     */
    public Template setLargeImage(String largeImage) {
        this.largeImage = largeImage;
        return this;
    }

    /**
     * Gets the locate of the template.
     *
     * @return locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the locate of the template.
     */
    public Template setLocale(String locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Gets the type of the template. One of "sheet" or "report"
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the template. One of "sheet" or "report"
     */
    public Template setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the list of search tags for this template.
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Sets the array of search tags for this template.
     */
    public Template setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Gets the list of categories this template belongs to
     *
     * @return array of categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Sets the array of categories this template belongs to
     */
    public Template setCategories(List<String> categories) {
        this.categories = categories;
        return this;
    }

    /**
     * Returns true if this template is blank
     *
     * @return flag indicating whether template is blank
     */
    public Boolean isBlank() {
        return blank;
    }

    /**
     * Sets the flag indicating whether this template is blank.
     */
    public Template setBlank(Boolean blank) {
        this.blank = blank;
        return this;
    }

    /**
     * Gets the type of global template
     *
     * @return template type
     */
    public GlobalTemplate getGlobalTemplate() {
        return globalTemplate;
    }

    /**
     * Sets the type of global template
     */
    public Template setGlobalTemplate(GlobalTemplate globalTemplate) {
        this.globalTemplate = globalTemplate;
        return this;
    }

    /**
     * Gets the permalink.
     *
     * @return the permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * Sets the permalink.
     *
     * @param permalink the permalink
     */
    public Template setPermalink(String permalink) {
        this.permalink = permalink;
        return this;
    }

    /**
     * Gets the creation date.
     *
     * @return the created at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date.
     *
     * @param createdAt the creation date
     */
    public Template setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Gets the last modified date.
     *
     * @return the modified at
     */
    public Date getModifiedAt() {
        return modifiedAt;
    }

    /**
     * Sets the last modified date.
     *
     * @param modifiedAt the last modified date
     */
    public Template setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    /**
     * Gets the resource type.
     *
     * @return the resource type
     */
    public ChildResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Sets the resource type.
     *
     * @param resourceType the resource type
     */
    public Template setResourceType(ChildResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }
}
