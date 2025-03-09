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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HomeTest {
    @Nested
    class BuilderTests {
        @Test
        void homeBuilder() {
            // Arrange
            List<Sheet> sheets = List.of(new Sheet());
            List<Folder> folders = List.of(new Folder());
            List<Report> reports = List.of(new Report());
            List<Template> templates = List.of(Template.builder().build());
            List<Workspace> workspaces = List.of(new Workspace());
            List<Sight> sights = List.of(Sight.builder().build());

            // Act
            Home homeNoArg = Home.builder().build();
            homeNoArg.setSheets(sheets);
            homeNoArg.setFolders(folders);
            homeNoArg.setReports(reports);
            homeNoArg.setTemplates(templates);
            homeNoArg.setWorkspaces(workspaces);
            homeNoArg.setSights(sights);

            Home homeAllArg = Home.builder()
                    .sheets(sheets)
                    .folders(folders)
                    .reports(reports)
                    .templates(templates)
                    .workspaces(workspaces)
                    .sights(sights)
                    .build();

            // Assert
            assertThat(homeNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(homeAllArg);
        }
    }
}
