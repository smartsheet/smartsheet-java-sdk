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
    public PathLeaf getSight() {
        SightPathNode node = this;
        while (node != null) {
            if (node.getSights() != null && !node.getSights().isEmpty()) {
                return node.getSights().get(0);
            }
            if (node.getFolders() != null && !node.getFolders().isEmpty()) {
                node = node.getFolders().get(0);
            } else {
                break;
            }
        }
        return null;
    }

    /**
     * Returns a {@code /}-joined path of folder names plus the target sight name.
     */
    public String getSightPath() {
        List<String> names = new ArrayList<>();
        SightPathNode node = this;
        PathLeaf leaf = null;
        while (node != null) {
            names.add(node.getName());
            if (node.getSights() != null && !node.getSights().isEmpty()) {
                leaf = node.getSights().get(0);
                break;
            }
            if (node.getFolders() != null && !node.getFolders().isEmpty()) {
                node = node.getFolders().get(0);
            } else {
                break;
            }
        }
        if (leaf == null) {
            return null;
        }
        names.add(leaf.getName());
        return String.join("/", names);
    }
}
