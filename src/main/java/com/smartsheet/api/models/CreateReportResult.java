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

import com.smartsheet.api.models.enums.AccessLevel;

/**
 * Represents the result returned when creating a new report.
 */
public class CreateReportResult {
    /**
     * The report's unique identifier.
     */
    private Long id;

    /**
     * The report's name.
     */
    private String name;

    /**
     * The user's access level to the report.
     */
    private AccessLevel accessLevel;

    /**
     * URL to the report in Smartsheet.
     */
    private String permalink;

    /**
     * True if the report is a sheet summary; false if it is a row report.
     */
    private Boolean isSummaryReport;

    /**
     * Gets the report ID.
     *
     * @return the report ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the report ID.
     *
     * @param id the report ID
     */
    public CreateReportResult setId(Long id) {
        this.id = id;
        return this;
    }

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
     * @param name the report name
     */
    public CreateReportResult setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the access level.
     *
     * @return the access level
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level.
     *
     * @param accessLevel the access level
     */
    public CreateReportResult setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }

    /**
     * Gets the permalink to the report.
     *
     * @return the permalink URL
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * Sets the permalink to the report.
     *
     * @param permalink the permalink URL
     */
    public CreateReportResult setPermalink(String permalink) {
        this.permalink = permalink;
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
     */
    public CreateReportResult setIsSummaryReport(Boolean isSummaryReport) {
        this.isSummaryReport = isSummaryReport;
        return this;
    }
}
