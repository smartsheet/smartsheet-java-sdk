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

/**
 * Represents the request body for updating a report column.
 * Used with PUT /reports/{reportId}/columns/{columnVirtualId}.
 */
public class UpdateReportColumnRequest {

    /** The title of the column (optional - only for primary, sheet name, and system type columns). */
    private String title;

    /** The index of the column (optional - moves column, shifts others). */
    private Integer index;

    /** Whether the column is hidden (optional). */
    private Boolean hidden;

    /** The width of the column in pixels (optional, minimum 1). */
    private Integer width;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title
     * @return this UpdateReportColumnRequest for chaining
     */
    public UpdateReportColumnRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Gets the index.
     *
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Sets the index.
     *
     * @param index the index
     * @return this UpdateReportColumnRequest for chaining
     */
    public UpdateReportColumnRequest setIndex(Integer index) {
        this.index = index;
        return this;
    }

    /**
     * Gets whether the column is hidden.
     *
     * @return hidden flag
     */
    public Boolean getHidden() {
        return hidden;
    }

    /**
     * Sets whether the column is hidden.
     *
     * @param hidden the hidden flag
     * @return this UpdateReportColumnRequest for chaining
     */
    public UpdateReportColumnRequest setHidden(Boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the width.
     *
     * @param width the width (minimum 1)
     * @return this UpdateReportColumnRequest for chaining
     */
    public UpdateReportColumnRequest setWidth(Integer width) {
        this.width = width;
        return this;
    }
}
