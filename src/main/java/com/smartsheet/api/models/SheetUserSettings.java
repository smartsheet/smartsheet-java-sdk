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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SheetUserSettings {

    /**
     * The ID of the filter currently applied to the sheet
     */
    private Long appliedSheetFilterId;

    /**
     * Identifies if the user has critical path enabled.
     */
    private Boolean criticalPathEnabled;

    /**
     * Identifies if the user has display summary tasks enabled.
     */
    private Boolean displaySummaryTasks;

    /**
     * True if the user has critical path enabled.
     *
     * @return criticalPathEnabled
     */
    public Boolean isCriticalPathEnabled() {
        return criticalPathEnabled;
    }

    /**
     * True if the user has display summary tasks enabled
     *
     * @return displaySummaryTasks
     */
    public Boolean isDisplaySummaryTasksEnabled() {
        return displaySummaryTasks;
    }
}
