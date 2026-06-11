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

import java.util.ArrayList;
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
     * Walks the nested {@code folders} list recursively and returns the deepest (target) folder node.
     *
     * <p>If this node has no nested folders (i.e. the folder is at the root of the workspace or the API
     * returned only a single-level path), {@code this} is returned.</p>
     *
     * @return the deepest {@link FolderPathNode} reachable from this node; never {@code null}
     */
    public FolderPathNode getFolder() {
        FolderPathNode node = this;
        while (node.getFolders() != null && !node.getFolders().isEmpty()) {
            node = node.getFolders().get(0);
        }
        return node;
    }

    /**
     * Returns a {@code /}-joined string of names from the root node down to the target (deepest) folder.
     *
     * <p>Each path segment is the {@link #getName()} of the corresponding node in the hierarchy. When
     * this node has no nested folders (e.g. the folder lives directly under the workspace), only the
     * name of this node is returned with no separator.</p>
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>Workspace → FolderA → FolderB → Target returns {@code "Workspace/FolderA/FolderB/Target"}</li>
     *   <li>Workspace → Target (no intermediate folders) returns {@code "Workspace/Target"}</li>
     *   <li>Target at root (no workspace or folders in response) returns {@code "Target"}</li>
     * </ul>
     *
     * @return a non-null path string; contains only the node name when there are no nested folders
     */
    public String getFolderPath() {
        List<String> names = new ArrayList<>();
        FolderPathNode node = this;
        while (node != null) {
            names.add(node.getName());
            if (node.getFolders() != null && !node.getFolders().isEmpty()) {
                node = node.getFolders().get(0);
            } else {
                break;
            }
        }
        return String.join("/", names);
    }
}
