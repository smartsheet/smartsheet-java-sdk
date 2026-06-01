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

package com.smartsheet.api;

import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.CopyExclusion;
import com.smartsheet.api.models.enums.WorkspaceCopyInclusion;
import com.smartsheet.api.models.enums.WorkspaceRemapExclusion;
import com.smartsheet.api.models.enums.GetWorkspaceMetadataInclusion;
import com.smartsheet.api.models.enums.GetWorkspaceChildrenInclusion;
import com.smartsheet.api.models.enums.ChildrenResourceType;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.TokenPaginationParameters;

import java.util.EnumSet;

/**
 * <p>This interface provides methods to access Workspace resources.</p>
 *
 * <p>Thread Safety: Implementation of this interface must be thread safe.</p>
 */
public interface WorkspaceResources {

    /**
     * <p>List all workspaces.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /workspaces</p>
     *
     * @param paging the object containing the token-based pagination parameters
     * @return TokenPaginatedResult of workspaces (empty list if there are none)
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    TokenPaginatedResult<Workspace> listWorkspaces(TokenPaginationParameters paging) throws SmartsheetException;

    /**
     * <p>Create a workspace.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /workspaces</p>
     *
     * @param workspace the workspace to create
     * @return the created workspace
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    Workspace createWorkspace(Workspace workspace) throws SmartsheetException;

    /**
     * <p>Update a workspace.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: PUT /workspace/{id}</p>
     *
     * @param workspace the workspace to update
     * @return the updated workspace (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null)
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    Workspace updateWorkspace(Workspace workspace) throws SmartsheetException;

    /**
     * <p>Delete a workspace.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: DELETE /workspace{id}</p>
     *
     * @param id the id of the workspace
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    void deleteWorkspace(long id) throws SmartsheetException;

    /**
     * <p>Creates a copy of the specified workspace.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /workspaces/{workspaceId}/copy</p>
     * <p>
     * Exceptions:
     * IllegalArgumentException : if folder is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param workspaceId          the folder id
     * @param containerDestination describes the destination container
     * @param includes             optional parameters to include
     * @param skipRemap            optional parameters to exclude
     * @return the folder
     * @throws SmartsheetException the smartsheet exception
     */
    Workspace copyWorkspace(
            long workspaceId,
            ContainerDestination containerDestination,
            EnumSet<WorkspaceCopyInclusion> includes,
            EnumSet<WorkspaceRemapExclusion> skipRemap
    ) throws SmartsheetException;

    /**
     * <p>Creates a copy of the specified workspace.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /workspaces/{workspaceId}/copy</p>
     * <p>
     * Exceptions:
     * IllegalArgumentException : if folder is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param workspaceId          the folder id
     * @param containerDestination describes the destination container
     * @param includes             optional parameters to include
     * @param skipRemap            optional parameters to NOT re-map in the new folder
     * @param excludes             optional parameters to exclude
     * @return the folder
     * @throws SmartsheetException the smartsheet exception
     * @deprecated As of release 2.0. `excludes` param is deprecated. Please use the `copyWorkspace` method with `includes` instead.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    Workspace copyWorkspace(long workspaceId, ContainerDestination containerDestination, EnumSet<WorkspaceCopyInclusion> includes,
                            EnumSet<WorkspaceRemapExclusion> skipRemap, EnumSet<CopyExclusion> excludes) throws SmartsheetException;

    /**
     * <p>Return the WorkspaceFolderResources object that provides access to Folder resources associated with Workspace
     * resources.</p>
     *
     * @return the workspace folder resources
     */
    WorkspaceFolderResources folderResources();

    /**
     * <p>Return the ShareResources object that provides access to Share resources associated with Workspace
     * resources.</p>
     *
     * @return the share resources object
     */
    ShareResources shareResources();

    /**
     * <p>Get metadata of a workspace.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /workspaces/{workspaceId}/metadata</p>
     *
     * @param workspaceId the workspace id
     * @param includes    the include parameters
     * @return the workspace metadata (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null)
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    Workspace getWorkspaceMetadata(long workspaceId, EnumSet<GetWorkspaceMetadataInclusion> includes) throws SmartsheetException;

    /**
     * <p>Get children of a workspace.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /workspaces/{workspaceId}/children</p>
     *
     * @param workspaceId           the workspace id
     * @param childrenResourceTypes the resource types to filter by (optional)
     * @param includes              the include parameters (optional)
     * @param lastKey               the last key for pagination (optional)
     * @param maxItems              the maximum number of items to return (optional)
     * @return the paginated children response
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    TokenPaginatedResult<Object> getWorkspaceChildren(long workspaceId, EnumSet<ChildrenResourceType> childrenResourceTypes,
                                                   EnumSet<GetWorkspaceChildrenInclusion> includes,
                                                   String lastKey, Integer maxItems) throws SmartsheetException;
}
