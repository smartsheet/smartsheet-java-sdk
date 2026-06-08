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
import com.smartsheet.api.WorkspaceResources;
import com.smartsheet.api.internal.util.QueryUtil;
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
import com.smartsheet.api.internal.json.ChildrenResourceDeserializer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the implementation of the WorkspaceResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class WorkspaceResourcesImpl extends AbstractResources implements WorkspaceResources {
    private static final String WORKSPACES = "workspaces";
    private static final String INCLUDE_PARAM = "include";

    /**
     * Represents the WorkspaceFolderResources.
     * <p>
     * It will be initialized in constructor and will not change afterwards.
     */
    private WorkspaceFolderResources folders;

    /**
     * Constructor.
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is
     *
     * @param smartsheet the smartsheet
     */
    public WorkspaceResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
        this.folders = new WorkspaceFolderResourcesImpl(smartsheet);
    }

    /**
     * List all workspaces.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /workspaces
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param paging the object containing the token-based pagination parameters
     * @return TokenPaginatedResult of workspaces (empty list if there are none)
     * @throws SmartsheetException the smartsheet exception
     */
    public TokenPaginatedResult<Workspace> listWorkspaces(TokenPaginationParameters paging) throws SmartsheetException {
        String path = WORKSPACES;
        if (paging != null) {
            path += paging.toQueryString();
        }
        return this.listResourcesWithTokenPagination(path, Workspace.class);
    }

    /**
     * Create a workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspaces
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param workspace the workspace to create, limited to the following required attributes: * name (string)
     * @return the created workspace
     * @throws SmartsheetException the smartsheet exception
     */
    public Workspace createWorkspace(Workspace workspace) throws SmartsheetException {
        return this.createResource(WORKSPACES, Workspace.class, workspace);
    }

    /**
     * Update a workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /workspace/{id}
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param workspace the workspace to update limited to the following attribute: * name (string)
     * @return the updated workspace (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public Workspace updateWorkspace(Workspace workspace) throws SmartsheetException {
        return this.updateResource(WORKSPACES + "/" + workspace.getId(), Workspace.class, workspace);
    }

    /**
     * Delete a workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /workspace{id}
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param id the ID of the workspace
     * @throws SmartsheetException the smartsheet exception
     */
    public void deleteWorkspace(long id) throws SmartsheetException {
        this.deleteResource(WORKSPACES + "/" + id, Workspace.class);
    }

    /**
     * Creates a copy of the specified workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspaces/{workspaceId}/copy
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
    public Workspace copyWorkspace(
            long workspaceId,
            ContainerDestination containerDestination,
            EnumSet<WorkspaceCopyInclusion> includes,
            EnumSet<WorkspaceRemapExclusion> skipRemap
    ) throws SmartsheetException {
        return copyWorkspace(workspaceId, containerDestination, includes, skipRemap, null);
    }

    /**
     * Creates a copy of the specified workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspaces/{workspaceId}/copy
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
     * @param excludes             optional parameters to exclude     *
     * @return the folder
     * @throws SmartsheetException the smartsheet exception
     * @deprecated As of release 2.0. `excludes` param is deprecated. Please use the `copyWorkspace` method with `includes` instead.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public Workspace copyWorkspace(long workspaceId, ContainerDestination containerDestination, EnumSet<WorkspaceCopyInclusion> includes,
                                   EnumSet<WorkspaceRemapExclusion> skipRemap, EnumSet<CopyExclusion> excludes) throws SmartsheetException {

        String path = WORKSPACES + "/" + workspaceId + "/copy";
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(INCLUDE_PARAM, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put("skipRemap", QueryUtil.generateCommaSeparatedList(skipRemap));
        parameters.put("exclude", QueryUtil.generateCommaSeparatedList(excludes));

        path += QueryUtil.generateUrl(null, parameters);

        return this.createResource(path, Workspace.class, containerDestination);
    }

    /**
     * Return the WorkspaceFolderResources object that provides access to Folder resources associated with Workspace
     * resources.
     *
     * @return the workspace folder resources
     */
    public WorkspaceFolderResources folderResources() {
        return this.folders;
    }

    /**
     * Get metadata of a workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /workspaces/{workspaceId}/metadata
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param workspaceId the workspace id
     * @param includes    used to specify the optional objects to include
     * @return the workspace metadata (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public Workspace getWorkspaceMetadata(long workspaceId,
                                          EnumSet<GetWorkspaceMetadataInclusion> includes) throws SmartsheetException {
        String path = WORKSPACES + "/" + workspaceId + "/metadata";

        // Add the parameters to a map and build the query string at the end
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE_PARAM, QueryUtil.generateCommaSeparatedList(includes));
        path += QueryUtil.generateUrl(null, parameters);

        return this.getResource(path, Workspace.class);
    }

    /**
     * Get children of a workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /workspaces/{workspaceId}/children
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param workspaceId           the workspace id
     * @param childrenResourceTypes the resource types to filter by (optional)
     * @param includes              used to specify the optional objects to include
     * @param lastKey               the last key for pagination (optional)
     * @param maxItems              the maximum number of items to return (optional)
     * @return the paginated children response
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public TokenPaginatedResult<Object> getWorkspaceChildren(long workspaceId, EnumSet<ChildrenResourceType> childrenResourceTypes,
                                                          EnumSet<GetWorkspaceChildrenInclusion> includes,
                                                          String lastKey, Integer maxItems) throws SmartsheetException {
        String path = WORKSPACES + "/" + workspaceId + "/children";

        // Add the parameters to a map and build the query string at the end
        Map<String, Object> parameters = new HashMap<>();
        if (childrenResourceTypes != null && !childrenResourceTypes.isEmpty()) {
            parameters.put("childrenResourceTypes", QueryUtil.generateCommaSeparatedList(childrenResourceTypes));
        }
        parameters.put(INCLUDE_PARAM, QueryUtil.generateCommaSeparatedList(includes));
        if (lastKey != null) {
            parameters.put("lastKey", lastKey);
        }
        if (maxItems != null) {
            parameters.put("maxItems", maxItems);
        }
        path += QueryUtil.generateUrl(null, parameters);

        return this.listResourcesWithTokenPagination(path, new ChildrenResourceDeserializer());
    }
}
