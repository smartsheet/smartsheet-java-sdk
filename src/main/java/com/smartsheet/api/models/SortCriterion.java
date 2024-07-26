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
import com.smartsheet.api.models.enums.SortDirection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The SortCriterion model.
 * <p>
 * This class has the "equals" and "hashCode" methods overridden and will base equality based on if the "id" field is equal.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SortCriterion {
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
     * The column ID to sort on
     */
    private Long columnId;

    /**
     * The direction of the sort
     */
    private SortDirection direction;
}
