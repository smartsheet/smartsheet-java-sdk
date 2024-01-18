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

import java.util.List;

/**
 * Represents the Home object.
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/522237-the-home-tab">Home Tab Help</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Home {
    /**
     * Represents the sheets in the home location.
     */
    private List<Sheet> sheets;

    /**
     * Represents the folders in the home location.
     */
    private List<Folder> folders;

    /**
     * Represents the reports in the home location.
     */
    private List<Report> reports;

    /**
     * Represents the templates in the home location.
     */
    private List<Template> templates;

    /**
     * Represents the workspaces in the home location.
     */
    private List<Workspace> workspaces;

    /**
     * Represents the sights in the home location.
     */
    private List<Sight> sights;
}
