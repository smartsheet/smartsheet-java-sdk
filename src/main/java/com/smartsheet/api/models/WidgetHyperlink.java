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

public class WidgetHyperlink extends Hyperlink {

    /**
     * Specifies what happens when a user clicks the widget.
     */
    private String interactionType;

    /**
     * If interactionType = SMARTSHEET_ITEM this is the folder to open
     */
    private Long folderId;

    /**
     * If interactionType = SMARTSHEET_ITEM this is the workspace to open
     */
    private Long workspaceId;

    /**
     * Gets the interaction type
     *
     * @return the interaction type
     */
    public String getInteractionType() {
        return interactionType;
    }

    /**
     * Sets the interaction type
     */
    public WidgetHyperlink setInteractionType(String interactionType) {
        this.interactionType = interactionType;
        return this;
    }

    /**
     * Gets the folder ID - valid when interactionType == SMARTSHEET_ITEM
     *
     * @return the folder ID
     */
    public Long getFolderId() {
        return folderId;
    }

    /**
     * Sets the folder ID - valid when interactionType == SMARTSHEET_ITEM
     */
    public WidgetHyperlink setFolderId(Long folderId) {
        this.folderId = folderId;
        return this;
    }

    /**
     * Gets the workspace ID - valid when interactionType == SMARTSHEET_ITEM
     *
     * @return the workspace ID
     */
    public Long getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Sets the workspace ID - valid when interactionType == SMARTSHEET_ITEM
     */
    public WidgetHyperlink setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }
}
