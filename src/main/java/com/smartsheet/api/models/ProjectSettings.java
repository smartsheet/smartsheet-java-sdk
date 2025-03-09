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

import com.smartsheet.api.models.enums.DayOfWeek;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class ProjectSettings {
    /**
     * Working days of a week for a project sheet.
     */
    private List<DayOfWeek> workingDays;

    /**
     * Non-working days for a project sheet. Must be an array of strings that are valid ISO-8601 dates ('YYYY-MM-DD’)
     */
    private List<String> nonWorkingDays;

    /**
     * Length of a workday in hours for a project sheet. Valid value must be above or equal to 1 hour, and less than or equal to 24 hours.
     */
    private Float lengthOfDay;
}
