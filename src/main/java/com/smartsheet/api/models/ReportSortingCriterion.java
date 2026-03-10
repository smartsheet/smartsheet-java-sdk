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
 * Represents a sorting criterion for a report.
 */
public class ReportSortingCriterion {

    /**
     * The column to sort by.
     */
    private ReportColumnIdentifier column;

    /**
     * Sorting direction.
     */
    private SortDirection sortingDirection;

    /**
     * Gets the column to sort by.
     *
     * @return the column
     */
    public ReportColumnIdentifier getColumn() {
        return column;
    }

    /**
     * Sets the column to sort by.
     *
     * @param column the column
     */
    public ReportSortingCriterion setColumn(ReportColumnIdentifier column) {
        this.column = column;
        return this;
    }

    /**
     * Gets the sorting direction.
     *
     * @return the sorting direction
     */
    public SortDirection getSortingDirection() {
        return sortingDirection;
    }

    /**
     * Sets the sorting direction.
     *
     * @param sortingDirection the sorting direction
     */
    public ReportSortingCriterion setSortingDirection(SortDirection sortingDirection) {
        this.sortingDirection = sortingDirection;
        return this;
    }
}
