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
 * Node in a sheet path response. Contains recursive folders leading to the target sheet.
 */
public class SheetPathNode extends PathNode {

    private List<SheetPathNode> folders;

    private List<PathLeaf> sheets;

    public List<SheetPathNode> getFolders() {
        return folders;
    }

    /**
     * @param folders the list of nested folder nodes; may be null
     */
    public SheetPathNode setFolders(List<SheetPathNode> folders) {
        this.folders = folders;
        return this;
    }

    public List<PathLeaf> getSheets() {
        return sheets;
    }

    /**
     * @param sheets the list of sheet leaf nodes within this folder; may be null
     */
    public SheetPathNode setSheets(List<PathLeaf> sheets) {
        this.sheets = sheets;
        return this;
    }

    /**
     * Walks down through folders until a node contains sheets, then returns the first sheet.
     */
    public PathLeaf getLeafSheet() {
        if (sheets != null && !sheets.isEmpty()) {
            return sheets.getFirst();
        }

        if (folders != null && !folders.isEmpty()) {
            return folders.getFirst().getLeafSheet();
        }

        return null;
    }

    /**
     * Returns a UNIX style {@code /}-joined path of folder names plus the target sheet name.
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>Workspace → Folder → Sheet returns {@code "/Workspace/Folder/Sheet"}</li>
     *   <li>Workspace → Sheet (no intermediate folders) returns {@code "/Workspace/Sheet"}</li>
     * </ul>
     */
    public String getLeafSheetPath() {
        if (sheets != null && !sheets.isEmpty()) {
            return String.format("/%s/%s", getName(), sheets.getFirst().getName());
        }

        if (folders != null && !folders.isEmpty()) {
            return String.format("/%s%s", getName(), folders.get(0).getLeafSheetPath());
        }

        return null;
    }
}
