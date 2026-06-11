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
 * Node in a sight path response. Contains recursive folders leading to the target sight.
 */
public class SightPathNode extends PathNode {

    private List<SightPathNode> folders;

    private List<PathLeaf> sights;

    public List<SightPathNode> getFolders() {
        return folders;
    }

    /**
     * @param folders the list of nested folder nodes; may be null
     */
    public SightPathNode setFolders(List<SightPathNode> folders) {
        this.folders = folders;
        return this;
    }

    public List<PathLeaf> getSights() {
        return sights;
    }

    /**
     * @param sights the list of sight leaf nodes within this folder; may be null
     */
    public SightPathNode setSights(List<PathLeaf> sights) {
        this.sights = sights;
        return this;
    }

    /**
     * Walks down through folders until a node contains sights, then returns the first sight.
     */
    public PathLeaf getLeafSight() {
        if (sights != null && !sights.isEmpty()) {
            return sights.getFirst();
        }

        if (folders != null && !folders.isEmpty()) {
            return folders.getFirst().getLeafSight();
        }

        return null;
    }

    /**
     * Returns a UNIX style {@code /}-joined path of folder names plus the target sight name.
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>Workspace → Folder → Sight returns {@code "/Workspace/Folder/Sight"}</li>
     *   <li>Workspace → Sight (no intermediate folders) returns {@code "/Workspace/Sight"}</li>
     * </ul>
     */
    public String getLeafSightPath() {
        if (sights != null && !sights.isEmpty()) {
            return String.format("/%s/%s", getName(), sights.getFirst().getName());
        }

        if (folders != null && !folders.isEmpty()) {
            return String.format("/%s%s", getName(), folders.get(0).getLeafSightPath());
        }

        return null;
    }
}
