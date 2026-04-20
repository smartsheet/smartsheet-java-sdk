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

import java.util.List;

/**
 * The report definition contains filters, grouping and sorting properties of the report.
 * <p>
 * Note: When groupingCriteria is defined the primary column of the report will move to the
 * index 0 when it is first rendered by the app.
 */
public class ReportDefinition {

    /**
     * Represents the filter expression for the report.
     */
    private ReportFilterExpression filters;

    /**
     * Represents the list of report grouping criteria.
     */
    private List<ReportGroupingCriterion> groupingCriteria;

    /**
     * Represents the list of report summarizing criteria.
     */
    private List<ReportSummarizingCriterion> summarizingCriteria;

    /**
     * Represents the list of report sorting criteria.
     */
    private List<ReportSortingCriterion> sortingCriteria;

    /**
     * Gets the filter expression.
     *
     * @return the filters
     */
    public ReportFilterExpression getFilters() {
        return filters;
    }

    /**
     * Sets the filter expression.
     *
     * @param filters the filter expression
     */
    public ReportDefinition setFilters(ReportFilterExpression filters) {
        this.filters = filters;
        return this;
    }

    /**
     * Gets the list of report grouping criteria.
     *
     * @return the grouping criteria
     */
    public List<ReportGroupingCriterion> getGroupingCriteria() {
        return groupingCriteria;
    }

    /**
     * Sets the list of report grouping criteria.
     *
     * @param groupingCriteria the grouping criteria
     */
    public ReportDefinition setGroupingCriteria(List<ReportGroupingCriterion> groupingCriteria) {
        this.groupingCriteria = groupingCriteria;
        return this;
    }

    /**
     * Gets the list of report summarizing criteria.
     *
     * @return the summarizing criteria
     */
    public List<ReportSummarizingCriterion> getSummarizingCriteria() {
        return summarizingCriteria;
    }

    /**
     * Sets the list of report summarizing criteria.
     *
     * @param summarizingCriteria the summarizing criteria
     */
    public ReportDefinition setSummarizingCriteria(List<ReportSummarizingCriterion> summarizingCriteria) {
        this.summarizingCriteria = summarizingCriteria;
        return this;
    }

    /**
     * Gets the list of report sorting criteria.
     *
     * @return the sorting criteria
     */
    public List<ReportSortingCriterion> getSortingCriteria() {
        return sortingCriteria;
    }

    /**
     * Sets the list of report sorting criteria.
     *
     * @param sortingCriteria the sorting criteria
     */
    public ReportDefinition setSortingCriteria(List<ReportSortingCriterion> sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
        return this;
    }
}
