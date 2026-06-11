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
 * Node in a report path response. Contains recursive folders leading to the target report.
 */
public class ReportPathNode extends PathNode {

    private List<ReportPathNode> folders;

    private List<PathLeaf> reports;

    public List<ReportPathNode> getFolders() {
        return folders;
    }

    /**
     * @param folders the list of nested folder nodes; may be null
     */
    public ReportPathNode setFolders(List<ReportPathNode> folders) {
        this.folders = folders;
        return this;
    }

    public List<PathLeaf> getReports() {
        return reports;
    }

    /**
     * @param reports the list of report leaf nodes within this folder; may be null
     */
    public ReportPathNode setReports(List<PathLeaf> reports) {
        this.reports = reports;
        return this;
    }

    /**
     * Walks down through folders until a node contains reports, then returns the first report.
     */
    public PathLeaf getLeafReport() {
        if (reports != null && !reports.isEmpty()) {
            return reports.getFirst();
        }

        if (folders != null && !folders.isEmpty()) {
            return folders.getFirst().getLeafReport();
        }

        return null;
    }

    /**
     * Returns a UNIX style {@code /}-joined path of folder names plus the target report name.
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>Workspace → Folder → Report returns {@code "/Workspace/Folder/Report"}</li>
     *   <li>Workspace → Report (no intermediate folders) returns {@code "/Workspace/Report"}</li>
     * </ul>
     */
    public String getLeafReportPath() {
        if (reports != null && !reports.isEmpty()) {
            return String.format("/%s/%s", getName(), reports.getFirst().getName());
        }

        if (folders != null && !folders.isEmpty()) {
            return String.format("/%s%s", getName(), folders.get(0).getLeafReportPath());
        }

        return null;
    }
}
