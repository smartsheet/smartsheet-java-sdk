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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A report object that is a filtered view of the data from one or more Sheets.
 *
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/522214-creating-reports">Creating Reports Help</a>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Report extends AbstractSheet<ReportRow, ReportColumn, ReportCell> {

    /**
     * A report's scope can be defined as the sheet ids and workspace ids that make up the report.
     */
    private Scope scope;

    /**
     * Represents the sheets that rows in the report originated from.
     */
    private List<Sheet> sourceSheets;
}
