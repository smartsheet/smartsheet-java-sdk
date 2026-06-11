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
 * Node in a folder path response. Contains recursive folders leading to the target folder.
 */
public class FolderPathNode extends PathNode {

    private List<FolderPathNode> folders;

    public List<FolderPathNode> getFolders() {
        return folders;
    }

    /**
     * @param folders the list of nested folder nodes; may be null
     */
    public FolderPathNode setFolders(List<FolderPathNode> folders) {
        this.folders = folders;
        return this;
    }

    /**
     * Walks down through folders until reaching the deepest (target) folder node, then returns it.
     */
    public FolderPathNode getLeafFolder() {
        if (folders == null || folders.isEmpty()) {
            return this;
        }

        return folders.getFirst().getLeafFolder();
    }

    /**
     * Returns a UNIX style {@code /}-joined path of folder names down to the target folder.
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>Workspace → FolderA → FolderB → Target returns {@code "/Workspace/FolderA/FolderB/Target"}</li>
     *   <li>Workspace → Target (no intermediate folders) returns {@code "/Workspace/Target"}</li>
     * </ul>
     */
    public String getLeafFolderPath() {
        if (folders == null || folders.isEmpty()) {
            return String.format("/%s", getName());
        }

        return String.format("/%s%s", getName(), folders.getFirst().getLeafFolderPath());
    }
}
