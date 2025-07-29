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

import com.smartsheet.api.models.CreateShareRequest;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.ShareResponse;
import com.smartsheet.api.models.UpdateShareRequest;

import java.util.List;

/**
 * <p>This interface provides methods to access Asset Share resources.</p>
 *
 * <p>Thread Safety: Implementation of this interface must be thread safe.</p>
 */
public interface AssetShareResources {

    /**
     * <p>List shares of a given asset.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method:</p>
     * <p>    GET /shares?assetId={assetId}&amp;assetType={assetType}</p>
     *
     * @param assetId the asset id
     * @param assetType the asset type (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @param parameters the pagination parameters
     * @param includeWorkspaceShares include workspace shares in enumeration
     * @return the list of ShareResponse objects (note that an empty list will be returned if there is none).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    PagedResult<ShareResponse> listShares(
            String assetId,
            String assetType,
            PaginationParameters parameters,
            Boolean includeWorkspaceShares
    ) throws SmartsheetException;

    /**
     * <p>Get a Share.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method:</p>
     * <p>    GET /shares/{shareId}?assetId={assetId}&amp;assetType={assetType}</p>
     *
     * @param shareId the ID of the share
     * @param assetId the ID of the asset
     * @param assetType the type of the asset (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @return the share response (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    ShareResponse getShare(String shareId, String assetId, String assetType) throws SmartsheetException;

    /**
     * <p>Shares the asset with the specified Users and Groups.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method:</p>
     * <p>    POST /shares?assetId={assetId}&amp;assetType={assetType}</p>
     *
     * @param assetId the ID of the asset to share
     * @param assetType the type of the asset (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @param shareRequests list of create share request objects
     * @param sendEmail whether to send email
     * @return the created shares
     * @throws SmartsheetException the smartsheet exception
     */
    List<ShareResponse> shareTo(
            String assetId,
            String assetType,
            List<CreateShareRequest> shareRequests,
            Boolean sendEmail
    ) throws SmartsheetException;

    /**
     * <p>Update a share.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method:</p>
     * <p>    PATCH /shares/{shareId}?assetId={assetId}&amp;assetType={assetType}</p>
     *
     * @param shareId the ID of the share
     * @param assetId the ID of the asset
     * @param assetType the type of the asset (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @param shareRequest the update share request
     * @return the updated share (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    ShareResponse updateShare(String shareId, String assetId, String assetType, UpdateShareRequest shareRequest) throws SmartsheetException;

    /**
     * <p>Delete a share.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method:</p>
     * <p>    DELETE /shares/{shareId}?assetId={assetId}&amp;assetType={assetType}</p>
     *
     * @param shareId the ID of the share to delete
     * @param assetId the ID of the asset
     * @param assetType the type of the asset (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @throws SmartsheetException the smartsheet exception
     */
    void deleteShare(String shareId, String assetId, String assetType) throws SmartsheetException;
}
