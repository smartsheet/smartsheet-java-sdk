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

import com.smartsheet.api.models.DataClassificationSettings;

/**
 * This interface provides methods for Governance operations.
 */
public interface GovernanceResources {

    /**
     * <p>Gets the data classification settings for a plan.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /governance/data-classification/settings</p>
     *
     * @param planId the ID of the plan whose classification settings to return
     * @return the DataClassificationSettings
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    DataClassificationSettings getDataClassificationSettings(long planId) throws SmartsheetException;

    /**
     * <p>Gets the data classification settings for the plan of a sheet, report, or dashboard.
     * Use this overload when your request relates to a specific sheet, report, or dashboard.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method:
     * GET /governance/data-classification/settings?assetType={assetType}&amp;assetId={assetId}</p>
     *
     * @param assetType the type of the asset: "sheet", "report", or "sight" (dashboard)
     * @param assetId   the ID of the sheet, report, or dashboard
     * @return the DataClassificationSettings
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    DataClassificationSettings getDataClassificationSettings(String assetType, long assetId) throws SmartsheetException;
}
