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

package com.smartsheet.api.internal;

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WorkspaceFolderResources;
import com.smartsheet.api.models.Folder;

/**
 * This is the implementation of the WorkspaceFolderResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class WorkspaceFolderResourcesImpl extends AbstractResources implements WorkspaceFolderResources {

    /**
     * Constructor.
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is
     *
     * @param smartsheet the smartsheet
     */
    public WorkspaceFolderResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * Create a folder in the workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspace/{id}/folders
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if folder is null
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param workspaceId the workspace id
     * @param folder      the folder to create
     * @return the created folder
     * @throws SmartsheetException the smartsheet exception
     */
    public Folder createFolder(long workspaceId, Folder folder) throws SmartsheetException {
        return this.createResource("workspaces/" + workspaceId + "/folders", Folder.class, folder);
    }
}
