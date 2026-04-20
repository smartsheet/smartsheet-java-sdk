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

import com.smartsheet.api.models.enums.SortDirection;

/**
 * Represents a grouping criterion for a report.
 */
public class ReportGroupingCriterion {

    /**
     * The column to group by.
     */
    private ReportColumnIdentifier column;

    /**
     * Sorting direction within the group.
     */
    private SortDirection sortingDirection;

    /**
     * Indicates whether the group is expanded in the UI (default: true).
     */
    private Boolean isExpanded;

    /**
     * Gets the column to group by.
     *
     * @return the column
     */
    public ReportColumnIdentifier getColumn() {
        return column;
    }

    /**
     * Sets the column to group by.
     *
     * @param column the column
     */
    public ReportGroupingCriterion setColumn(ReportColumnIdentifier column) {
        this.column = column;
        return this;
    }

    /**
     * Gets the sorting direction within the group.
     *
     * @return the sorting direction
     */
    public SortDirection getSortingDirection() {
        return sortingDirection;
    }

    /**
     * Sets the sorting direction within the group.
     *
     * @param sortingDirection the sorting direction
     */
    public ReportGroupingCriterion setSortingDirection(SortDirection sortingDirection) {
        this.sortingDirection = sortingDirection;
        return this;
    }

    /**
     * Gets whether the group is expanded in the UI.
     *
     * @return true if expanded, false otherwise
     */
    public Boolean getIsExpanded() {
        return isExpanded;
    }

    /**
     * Sets whether the group is expanded in the UI.
     *
     * @param isExpanded true if expanded, false otherwise
     */
    public ReportGroupingCriterion setIsExpanded(Boolean isExpanded) {
        this.isExpanded = isExpanded;
        return this;
    }
}
