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
import com.smartsheet.api.models.enums.ObjectValueType;

/**
 * Represents a CURRENT_USER filter value for report filters.
 * <p>
 * This is used to match against the current authenticated user in report filter criteria.
 * The objectType is "CURRENT_USER" which is not part of the standard ObjectValueType enum
 * as it's specific to report filters.
 */
public class CurrentUserObjectValue implements ReportFilterObjectValue {

    @JsonProperty("objectType")
    private final String objectType = "CURRENT_USER";

    @JsonProperty("value")
    private String value;

    /**
     * Default constructor.
     * Sets value to empty string which is typical for CURRENT_USER filters.
     */
    public CurrentUserObjectValue() {
        this.value = "";
    }

    /**
     * Constructor with custom value.
     *
     * @param value the value (typically empty for CURRENT_USER)
     */
    public CurrentUserObjectValue(String value) {
        this.value = value;
    }

    /**
     * Gets the object type string.
     * This returns "CURRENT_USER" for JSON serialization.
     *
     * @return "CURRENT_USER"
     */
    @JsonProperty("objectType")
    public String getObjectTypeString() {
        return objectType;
    }

    /**
     * Gets the value.
     *
     * @return the value (typically empty string)
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the value
     * @return this CurrentUserObjectValue for method chaining
     */
    public CurrentUserObjectValue setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Returns null since CURRENT_USER is not part of the standard ObjectValueType enum.
     * The objectType field is handled separately via {@link #getObjectTypeString()}.
     *
     * @return null
     */
    public ObjectValueType getObjectType() {
        return null;
    }
}
