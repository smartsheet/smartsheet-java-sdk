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
     * Get the data classification settings for a plan.
     * Use this overload to get the settings for a plan.
     *
     * @param planId the ID of the plan whose classification settings to return
     * @return the DataClassificationSettings
     * @throws SmartsheetException if there is any other error during the operation
     */
    DataClassificationSettings getDataClassificationSettings(long planId) throws SmartsheetException;

    /**
     * Get the data classification settings for a plan.
     * Use this overload when your request relates to a specific sheet, report, or dashboard.
     * Accepted assetType values: "sheet", "report", "sight" (dashboard).
     *
     * @param assetType the type of the asset: sheet, report, or sight (dashboard)
     * @param assetId   the ID of the sheet, report, or dashboard
     * @return the DataClassificationSettings
     * @throws SmartsheetException if there is any other error during the operation
     */
    DataClassificationSettings getDataClassificationSettings(String assetType, long assetId) throws SmartsheetException;
}
