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

import com.smartsheet.api.models.enums.ReportAssetType;

/**
 * Represents the ReportScopeInclusion object used when adding and removing report scopes.
 */
public class ReportScopeInclusion {
    /**
     * The type of asset that is included in the report.
     */
    private ReportAssetType assetType;

    /**
     * The id of the asset that is included in the report.
     */
    private Long assetId;

    /**
     * Get the type of asset that is included in the report.
     * @return the type of asset that is included in the report
     */
    public ReportAssetType getAssetType() {
        return assetType;
    }

    /**
     * Set the type of asset that is included in the report.
     * @param assetType the type of asset that is included in the report
     */
    public void setAssetType(ReportAssetType assetType) {
        this.assetType = assetType;
    }

    /**
     * Get the id of the asset that is included in the report.
     * @return the id of the asset that is included in the report
     */
    public Long getAssetId() {
        return assetId;
    }

    /**
     * Set the id of the asset that is included in the report.
     * @param assetId the id of the asset that is included in the report
     */
    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }
}
