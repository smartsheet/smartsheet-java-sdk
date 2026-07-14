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

package com.smartsheet.api.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the resourceType field value returned in API responses for child resources.
 */
public enum ChildResourceType {
    /**
     * A sheet resource.
     */
    @JsonProperty("sheet")
    SHEET,

    /**
     * A report resource.
     */
    @JsonProperty("report")
    REPORT,

    /**
     * A sight (dashboard) resource.
     */
    @JsonProperty("sight")
    SIGHT,

    /**
     * A folder resource.
     */
    @JsonProperty("folder")
    FOLDER,

    /**
     * A template resource.
     */
    @JsonProperty("template")
    TEMPLATE
}
