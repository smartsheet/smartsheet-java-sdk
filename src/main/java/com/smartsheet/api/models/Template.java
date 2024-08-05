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
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.GlobalTemplate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * A template object that is a default layout for future sheets.
 * <p>
 * This class has the "equals" and "hashCode" methods overridden and will base equality based on if the "id" field is equal.
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/522123-using-templates">Using Templates Help</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Jacksonized
@Builder
public class Template {
    /**
     * Represents the ID.
     * <p>
     * This excludes "id" field from being serialized to JSON. This is needed because when updating a resource,
     * the resource ID should be present and deserialized in the response, but it shouldn't be serialized and sent to Smartsheet REST API.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    /**
     * Represents the name.
     */
    private String name;

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
}
