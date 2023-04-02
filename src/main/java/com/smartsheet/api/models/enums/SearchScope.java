package com.smartsheet.api.models.enums;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

public enum SearchScope {
    ATTACHMENTS("attachments"),
    CELL_DATA("cellData"),
    COMMENTS("comments"),
    FOLDER_NAMES("folderNames"),
    REPORT_NAMES("reportNames"),
    SHEET_NAMES("sheetNames"),
    SIGHT_NAMES("sightNames"),
    SUMMARY_FIELDS("summaryFields"),
    TEMPLATE_NAMES("templateNames"),
    WORKSPACE_NAMES("workspaceNames"),
    ;

    String scope;

    SearchScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return scope;
    }
}
