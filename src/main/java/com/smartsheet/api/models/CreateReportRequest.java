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
 * Represents the request object for creating a new report.
 */
public class CreateReportRequest {
    /**
     * Report name (1-50 characters).
     */
    private String name;

    /**
     * List of columns to include in the report (1-400 items).
     */
    private List<ReportColumn> columns;

    /**
     * List of sheets and/or workspaces that define the report scope (1-100 items).
     */
    private List<ReportScopeInclusion> scope;

    /**
     * Report definition including filters, grouping, summarizing, and sorting.
     */
    private ReportDefinition reportDefinition;

    /**
     * True if the report is a sheet summary; false if it is a row report.
     */
    private Boolean isSummaryReport;

    /**
     * Destination container for the report (folder or workspace).
     */
    private ReportDestination destination;

    /**
     * Gets the report name.
     *
     * @return the report name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the report name.
     *
     * @param name the report name (1-50 characters)
     * @return this CreateReportRequest object for method chaining
     */
    public CreateReportRequest setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the columns for the report.
     *
     * @return the list of report columns
     */
    public List<ReportColumn> getColumns() {
        return columns;
    }

    /**
     * Sets the columns for the report.
     *
     * @param columns the list of report columns (1-400 items)
     * @return this CreateReportRequest object for method chaining
     */
    public CreateReportRequest setColumns(List<ReportColumn> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * Gets the scope of the report.
     *
     * @return the list of report scope inclusions
     */
    public List<ReportScopeInclusion> getScope() {
        return scope;
    }

    /**
     * Sets the scope of the report.
     *
     * @param scope the list of sheets and/or workspaces (1-100 items)
     * @return this CreateReportRequest object for method chaining
     */
    public CreateReportRequest setScope(List<ReportScopeInclusion> scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Gets the report definition.
     *
     * @return the report definition
     */
    public ReportDefinition getReportDefinition() {
        return reportDefinition;
    }

    /**
     * Sets the report definition.
     *
     * @param reportDefinition the report definition including filters, grouping, summarizing, and sorting
     * @return this CreateReportRequest object for method chaining
     */
    public CreateReportRequest setReportDefinition(ReportDefinition reportDefinition) {
        this.reportDefinition = reportDefinition;
        return this;
    }

    /**
     * Gets whether this is a summary report.
     *
     * @return true if this is a sheet summary report, false if it is a row report
     */
    public Boolean getIsSummaryReport() {
        return isSummaryReport;
    }

    /**
     * Sets whether this is a summary report.
     *
     * @param isSummaryReport true for sheet summary report, false for row report
     * @return this CreateReportRequest object for method chaining
     */
    public CreateReportRequest setIsSummaryReport(Boolean isSummaryReport) {
        this.isSummaryReport = isSummaryReport;
        return this;
    }

    /**
     * Gets the destination container for the report.
     *
     * @return the destination container
     */
    public ReportDestination getDestination() {
        return destination;
    }

    /**
     * Sets the destination container for the report.
     *
     * @param destination the destination folder or workspace
     * @return this CreateReportRequest object for method chaining
     */
    public CreateReportRequest setDestination(ReportDestination destination) {
        this.destination = destination;
        return this;
    }
}