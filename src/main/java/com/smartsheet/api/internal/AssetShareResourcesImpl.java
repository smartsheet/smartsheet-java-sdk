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

import com.smartsheet.api.AssetShareResources;
import com.smartsheet.api.AuthorizationException;
import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.ServiceUnavailableException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.CreateShareRequest;
import com.smartsheet.api.models.ShareResponse;
import com.smartsheet.api.models.UpdateShareRequest;
import com.smartsheet.api.models.ListAssetSharesResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation of the AssetShareResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class AssetShareResourcesImpl extends AbstractResources implements AssetShareResources {
    private static final String SHARES_PATH = "shares";
    private static final String ASSET_ID_PARAM = "assetId";
    private static final String ASSET_TYPE_PARAM = "assetType";

    /**
     * Constructor.
     *
     * @param smartsheet the smartsheet
     */
    public AssetShareResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * List shares of a given asset.
     * <p>
     * It mirrors to the following Smartsheet REST API method:
     * GET /shares?assetId={assetId}&amp;assetType={assetType}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param assetId the asset id
     * @param assetType the asset type (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @param lastKey lastKey from previous response to get next page of results
     * @param maxItems The maximum amount of items to return in the response. The default and minimum are 100.
     * @param sharingInclude defines the scope of the share. Possible values are ITEM or WORKSPACE.
     * @return the shares (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public ListAssetSharesResponse<ShareResponse> listShares(
            String assetId,
            String assetType,
            String lastKey,
            Long maxItems,
            String sharingInclude
    ) throws SmartsheetException {
        String path = SHARES_PATH;

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(ASSET_ID_PARAM, assetId);
        queryParameters.put(ASSET_TYPE_PARAM, assetType);

        if (lastKey != null) {
            queryParameters.put("lastKey", lastKey);
        }

        if (maxItems != null) {
            queryParameters.put("maxItems", maxItems);
        }

        if (sharingInclude != null) {
            queryParameters.put("sharingInclude", sharingInclude);
        }

        path += QueryUtil.generateUrl(null, queryParameters);

        return this.listAssetSharesWithTokenPagination(path, ShareResponse.class);
    }

    /**
     * Get a Share.
     * <p>
     * It mirrors to the following Smartsheet REST API method:
     * GET /shares/{shareId}?assetId={assetId}&amp;assetType={assetType}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param shareId the ID of the share
     * @param assetId the ID of the asset
     * @param assetType the type of the asset (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @return the share response (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public ShareResponse getShare(String shareId, String assetId, String assetType) throws SmartsheetException {
        String path = SHARES_PATH + "/" + shareId;
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(ASSET_ID_PARAM, assetId);
        queryParameters.put(ASSET_TYPE_PARAM, assetType);
        path += QueryUtil.generateUrl(null, queryParameters);
        return this.getResource(path, ShareResponse.class);
    }

    /**
     * Shares the asset with the specified Users and Groups.
     * <p>
     * It mirrors to the following Smartsheet REST API method:
     * POST /shares?assetId={assetId}&amp;assetType={assetType}
     * <p>
     * Exceptions:
     * IllegalArgumentException : if shareRequests is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param assetId the ID of the asset to share
     * @param assetType the type of the asset (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @param shareRequests list of create share request objects
     * @param sendEmail whether to send email
     * @return the created shares
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public List<ShareResponse> shareTo(
            String assetId,
            String assetType,
            List<CreateShareRequest> shareRequests,
            Boolean sendEmail
    ) throws SmartsheetException {
        Util.throwIfNull(shareRequests);
        String path = SHARES_PATH;
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(ASSET_ID_PARAM, assetId);
        queryParameters.put(ASSET_TYPE_PARAM, assetType);
        if (sendEmail != null) {
            queryParameters.put("sendEmail", sendEmail);
        }
        path += QueryUtil.generateUrl(null, queryParameters);
        return this.postAndReceiveList(path, shareRequests, ShareResponse.class);
    }

    /**
     * Update a share.
     * <p>
     * It mirrors to the following Smartsheet REST API method:
     * PATCH /shares/{shareId}?assetId={assetId}&amp;assetType={assetType}
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
    @Override
    public ShareResponse updateShare(
            String shareId,
            String assetId,
            String assetType,
            UpdateShareRequest shareRequest
    ) throws SmartsheetException {
        Util.throwIfNull(shareRequest);
        String path = SHARES_PATH + "/" + shareId;
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(ASSET_ID_PARAM, assetId);
        queryParameters.put(ASSET_TYPE_PARAM, assetType);
        path += QueryUtil.generateUrl(null, queryParameters);
        return this.patchResource(path, ShareResponse.class, shareRequest);
    }

    /**
     * Delete a share.
     * <p>
     * It mirrors to the following Smartsheet REST API method:
     * DELETE /shares/{shareId}?assetId={assetId}&amp;assetType={assetType}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param shareId the ID of the share to delete
     * @param assetId the ID of the asset
     * @param assetType the type of the asset (e.g. "sheet", "workspace", "report", "sight", "file", "collection")
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public void deleteShare(String shareId, String assetId, String assetType) throws SmartsheetException {
        String path = SHARES_PATH + "/" + shareId;
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(ASSET_ID_PARAM, assetId);
        queryParameters.put(ASSET_TYPE_PARAM, assetType);
        path += QueryUtil.generateUrl(null, queryParameters);
        this.deleteResource(path, ShareResponse.class);
    }
}
