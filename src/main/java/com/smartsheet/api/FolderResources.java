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
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.enums.CopyExclusion;
import com.smartsheet.api.models.enums.FolderCopyInclusion;
import com.smartsheet.api.models.enums.FolderRemapExclusion;
import com.smartsheet.api.models.enums.GetFolderMetadataInclusion;
import com.smartsheet.api.models.enums.GetFolderChildrenInclusion;
import com.smartsheet.api.models.enums.ChildrenResourceType;

import java.util.EnumSet;

/**
 * <p>This interface provides methods to access Folder resources.</p>
 *
 * <p>Thread Safety: Implementation of this interface must be thread safe.</p>
 */
public interface FolderResources {

    /**
     * <p>Update a folder.</p>
     *
     * @param folder the folder to update
     * @return the updated folder (note that if there is no such folder, this method will throw Resource Not Found
     * Exception rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    Folder updateFolder(Folder folder) throws SmartsheetException;

    /**
     * <p>Delete a folder.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: DELETE /folder{id}</p>
     *
     * @param folderId the folder id
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    void deleteFolder(long folderId) throws SmartsheetException;

    /**
     * <p>Create a folder.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /folder/{id}/folders</p>
     *
     * @param parentFolderId the parent folder id
     * @param folder         the folder to create
     * @return the created folder
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    Folder createFolder(long parentFolderId, Folder folder) throws SmartsheetException;

    /**
     * <p>Creates a copy of the specified Folder.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/copy</p>
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
    Folder copyFolder(
            long folderId,
            ContainerDestination containerDestination,
            EnumSet<FolderCopyInclusion> includes,
            EnumSet<FolderRemapExclusion> skipRemap
    ) throws SmartsheetException;

    /**
     * <p>Creates a copy of the specified Folder.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/copy</p>
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
    Folder copyFolder(
            long folderId,
            ContainerDestination containerDestination,
            EnumSet<FolderCopyInclusion> includes,
            EnumSet<FolderRemapExclusion> skipRemap,
            EnumSet<CopyExclusion> excludes
    ) throws SmartsheetException;

    /**
     * <p>Moves the specified Folder to another location.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/move</p>
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
    Folder moveFolder(long folderId, ContainerDestination containerDestination) throws SmartsheetException;

    /**
     * <p>Get metadata of a folder.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /folders/{folderId}/metadata</p>
     *
     * @param folderId the folder id
     * @param includes the include parameters (optional)
     * @return the folder metadata (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null)
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    Folder getFolderMetadata(long folderId, EnumSet<GetFolderMetadataInclusion> includes) throws SmartsheetException;

    /**
     * <p>Get children of a folder.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /folders/{folderId}/children</p>
     *
     * @param folderId              the folder id
     * @param childrenResourceTypes the resource type to filter by (optional)
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
    TokenPaginatedResult<Object> getFolderChildren(long folderId, EnumSet<ChildrenResourceType> childrenResourceTypes,
                                                EnumSet<GetFolderChildrenInclusion> includes,
                                                String lastKey, Integer maxItems) throws SmartsheetException;
}
