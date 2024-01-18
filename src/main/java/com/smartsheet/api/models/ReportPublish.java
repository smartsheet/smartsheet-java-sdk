/*
* Copyright (C) 2024 Smartsheet
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The ReportPublish object. Retruned by endpoints such as the
 * @see <a href="https://smartsheet.redoc.ly/tag/sheets#operation/get-sheetPublish">get sheet publish status</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ReportPublish {

    /**
     * Represents the current publish status of the report.
     */
    private boolean readOnlyFullEnabled;

    /**
     * Indicates who can see the published report
     *    If "ALL", report is available to anyone who has the link.
     *    If "ORG", report is available only to members of the report owner's organization.
     */
    private String readOnlyFullAccessibleBy;

    /**
     * String containing the URL of the published report.
     */
    private String readOnlyFullUrl;

    /**
     * Flag to show or hide the left nav toolbar for the read only report.
     */
    private Boolean readOnlyFullShowToolbar;

    /**
     *  Default view for published report. (GRID, CARDS, CALENDAR)
     */
    private String readOnlyFullDefaultView;
}
