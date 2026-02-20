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

import com.smartsheet.api.models.enums.ReportAggregationType;

/**
 * Represents an aggregation criterion for a report.
 */
public class ReportAggregationCriterion {

    /**
     * The column to aggregate.
     */
    private ReportColumnIdentifier column;

    /**
     * Type of aggregation.
     */
    private ReportAggregationType aggregationType;

    /**
     * Indicates whether the group is expanded in the UI (default: true).
     */
    private Boolean isExpanded;

    /**
     * Gets the column to aggregate.
     *
     * @return the column
     */
    public ReportColumnIdentifier getColumn() {
        return column;
    }

    /**
     * Sets the column to aggregate.
     *
     * @param column the column
     */
    public ReportAggregationCriterion setColumn(ReportColumnIdentifier column) {
        this.column = column;
        return this;
    }

    /**
     * Gets the type of aggregation.
     *
     * @return the aggregation type
     */
    public ReportAggregationType getAggregationType() {
        return aggregationType;
    }

    /**
     * Sets the type of aggregation.
     *
     * @param aggregationType the aggregation type
     */
    public ReportAggregationCriterion setAggregationType(ReportAggregationType aggregationType) {
        this.aggregationType = aggregationType;
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
    public ReportAggregationCriterion setIsExpanded(Boolean isExpanded) {
        this.isExpanded = isExpanded;
        return this;
    }
}
