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
 * Represents an summarizing criterion for a report.
 */
public class ReportSummarizingCriterion {

    /**
     * The column to summarize.
     */
    private ReportColumnIdentifier column;

    /**
     * Type of aggregation.
     */
    private ReportAggregationType aggregationType;

    /**
     * Gets the column to summarize.
     *
     * @return the column
     */
    public ReportColumnIdentifier getColumn() {
        return column;
    }

    /**
     * Sets the column to summarize.
     *
     * @param column the column
     */
    public ReportSummarizingCriterion setColumn(ReportColumnIdentifier column) {
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
    public ReportSummarizingCriterion setAggregationType(ReportAggregationType aggregationType) {
        this.aggregationType = aggregationType;
        return this;
    }
}
