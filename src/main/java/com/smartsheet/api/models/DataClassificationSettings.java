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

import java.util.List;

/**
 * Represents the data classification settings for a plan.
 */
public class DataClassificationSettings {

    private Long orgId;
    private Long planId;
    private Boolean isDisabled;
    private String guidelinesUrl;
    private Boolean allowManualChange;
    private List<ClassificationLabel> labels;
    private DowngradeApprovalSettings downgradeApprovalSettings;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getGuidelinesUrl() {
        return guidelinesUrl;
    }

    public void setGuidelinesUrl(String guidelinesUrl) {
        this.guidelinesUrl = guidelinesUrl;
    }

    public Boolean getAllowManualChange() {
        return allowManualChange;
    }

    public void setAllowManualChange(Boolean allowManualChange) {
        this.allowManualChange = allowManualChange;
    }

    public List<ClassificationLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ClassificationLabel> labels) {
        this.labels = labels;
    }

    public DowngradeApprovalSettings getDowngradeApprovalSettings() {
        return downgradeApprovalSettings;
    }

    public void setDowngradeApprovalSettings(DowngradeApprovalSettings downgradeApprovalSettings) {
        this.downgradeApprovalSettings = downgradeApprovalSettings;
    }
}
