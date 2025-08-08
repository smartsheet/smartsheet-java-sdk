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

import com.smartsheet.api.FolderResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.CopyExclusion;
import com.smartsheet.api.models.enums.FolderCopyInclusion;
import com.smartsheet.api.models.enums.FolderRemapExclusion;
import com.smartsheet.api.models.enums.SourceInclusion;
import com.smartsheet.api.models.enums.GetFolderMetadataInclusion;
import com.smartsheet.api.models.enums.GetFolderChildrenInclusion;
import com.smartsheet.api.models.enums.ChildrenResourceType;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.internal.json.ChildrenResourceDeserializer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the implementation of the FolderResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class FolderResourcesImpl extends AbstractResources implements FolderResources {
    private static final String FOLDERS_PATH = "folders/";
    private static final String INCLUDE_PARAM = "include";

    /**
     * Constructor.
     * <p>
     * Parameters: - smartsheet : the SmartsheetImpl
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public FolderResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * Get a folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /folder/{id}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folderId the folder id
     * @param includes the include parameters
     * @return the folder (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null)
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    @Deprecated(since = "3.4.0", forRemoval = true)
    public Folder getFolder(long folderId, EnumSet<SourceInclusion> includes) throws SmartsheetException {
        String path = FOLDERS_PATH + folderId;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE_PARAM, QueryUtil.generateCommaSeparatedList(includes));
        path += QueryUtil.generateUrl(null, parameters);

        return this.getResource(path, Folder.class);
    }

    /**
     * Update a folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /folder/{id}
     * <p>
     * Exceptions:
     * IllegalArgumentException : if folder is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folder the folder to update
     * @return the updated folder (note that if there is no such folder, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public Folder updateFolder(Folder folder) throws SmartsheetException {

        return this.updateResource(FOLDERS_PATH + folder.getId(), Folder.class, folder);
    }

    /**
     * Delete a folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /folder{id}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folderId the folder id
     * @throws SmartsheetException the smartsheet exception
     */
    public void deleteFolder(long folderId) throws SmartsheetException {

        this.deleteResource(FOLDERS_PATH + folderId, Folder.class);
    }

    /**
     * List child folders of a given folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /folder/{id}/folders
     * <p>
     * Parameters: - parentFolderId : the parent folder ID
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param parentFolderId the parent folder id
     * @param parameters     the parameters for pagination
     * @return the child folders (note that empty list will be returned if no child folder found)
     * @throws SmartsheetException the smartsheet exception
     */
    @Deprecated(since = "3.4.0", forRemoval = true)
    public PagedResult<Folder> listFolders(long parentFolderId, PaginationParameters parameters) throws SmartsheetException {
        String path = FOLDERS_PATH + parentFolderId + "/folders";

        if (parameters != null) {
            path += parameters.toQueryString();
        }

        return this.listResourcesWithWrapper(path, Folder.class);
    }

    /**
     * Create a folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folder/{id}/folders
     * <p>
     * Exceptions:
     * IllegalArgumentException : if folder is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param parentFolderId the parent folder id
     * @param folder         the folder to create
     * @return the folder
     * @throws SmartsheetException the smartsheet exception
     */
    public Folder createFolder(long parentFolderId, Folder folder) throws SmartsheetException {

        return this.createResource(FOLDERS_PATH + parentFolderId + "/folders", Folder.class, folder);
    }

    /**
     * Creates a copy of the specified Folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/copy
     * <p>
     * Exceptions:
     * IllegalArgumentException : if folder is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folderId             the folder id
     * @param containerDestination describes the destination container
     * @param includes             optional parameters to include
     * @param skipRemap            optional parameters to exclude
     * @return the folder
     * @throws SmartsheetException the smartsheet exception
     */
    public Folder copyFolder(
            long folderId,
            ContainerDestination containerDestination,
            EnumSet<FolderCopyInclusion> includes,
            EnumSet<FolderRemapExclusion> skipRemap
    ) throws SmartsheetException {
        return copyFolder(folderId, containerDestination, includes, skipRemap, null);
    }

    /**
     * Creates a copy of the specified Folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/copy
     * <p>
     * Exceptions:
     * IllegalArgumentException : if folder is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folderId             the folder id
     * @param containerDestination describes the destination container
     * @param includes             optional parameters to include
     * @param skipRemap            optional parameters to NOT re-map in the new folder
     * @param excludes             optional parameters to exclude
     * @return the folder
     * @throws SmartsheetException the smartsheet exception
     */
    public Folder copyFolder(long folderId, ContainerDestination containerDestination, EnumSet<FolderCopyInclusion> includes,
                             EnumSet<FolderRemapExclusion> skipRemap, EnumSet<CopyExclusion> excludes) throws SmartsheetException {

        String path = FOLDERS_PATH + folderId + "/copy";
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(INCLUDE_PARAM, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put("skipRemap", QueryUtil.generateCommaSeparatedList(skipRemap));
        parameters.put("exclude", QueryUtil.generateCommaSeparatedList(excludes));

        path += QueryUtil.generateUrl(null, parameters);

        return this.createResource(path, Folder.class, containerDestination);
    }

    /**
     * Moves the specified Folder to another location.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/move
     * <p>
     * Exceptions:
     * IllegalArgumentException : if folder is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folderId             the folder id
     * @param containerDestination describes the destination container
     * @return the folder
     * @throws SmartsheetException the smartsheet exception
     */
    public Folder moveFolder(long folderId, ContainerDestination containerDestination) throws SmartsheetException {

        String path = FOLDERS_PATH + folderId + "/move";
        return this.createResource(path, Folder.class, containerDestination);
    }

    /**
     * Get metadata of a folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /folders/{folderId}/metadata
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folderId the folder id
     * @param includes used to specify the optional objects to include
     * @return the folder metadata (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public Folder getFolderMetadata(long folderId, EnumSet<GetFolderMetadataInclusion> includes) throws SmartsheetException {
        String path = FOLDERS_PATH + folderId + "/metadata";

        // Add the parameters to a map and build the query string at the end
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE_PARAM, QueryUtil.generateCommaSeparatedList(includes));
        path += QueryUtil.generateUrl(null, parameters);

        return this.getResource(path, Folder.class);
    }

    /**
     * Get children of a folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /folders/{folderId}/children
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param folderId              the folder id
     * @param childrenResourceTypes the resource types to filter by (optional)
     * @param includes              used to specify the optional objects to include
     * @param lastKey               the last key for pagination (optional)
     * @param maxItems              the maximum number of items to return (optional)
     * @return the paginated children response
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public TokenPaginatedResult<Object> getFolderChildren(long folderId, EnumSet<ChildrenResourceType> childrenResourceTypes,
                                                       EnumSet<GetFolderChildrenInclusion> includes,
                                                       String lastKey, Integer maxItems) throws SmartsheetException {
        String path = FOLDERS_PATH + folderId + "/children";

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
