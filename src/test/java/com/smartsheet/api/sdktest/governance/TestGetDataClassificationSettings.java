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

package com.smartsheet.api.sdktest.governance;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.ApproverEntry;
import com.smartsheet.api.models.ClassificationLabel;
import com.smartsheet.api.models.DataClassificationSettings;
import com.smartsheet.api.models.DowngradeApprovalSettings;
import com.smartsheet.api.models.LabelApproverEntry;
import com.smartsheet.api.models.enums.ApproverType;
import com.smartsheet.api.models.enums.DowngradeApprovalMode;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.smartsheet.api.sdktest.governance.CommonTestConstants.TEST_ASSET_ID;
import static com.smartsheet.api.sdktest.governance.CommonTestConstants.TEST_ORG_ID;
import static com.smartsheet.api.sdktest.governance.CommonTestConstants.TEST_PLAN_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestGetDataClassificationSettings {

    private static final String EXPECTED_PATH = "/2.0/governance/data-classification/settings";
    private static final String GUIDELINES_URL = "https://wiki.example.com/classification-guide";
    private static final String CONFIDENTIAL_LABEL_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
    private static final String INTERNAL_LABEL_ID = "4aa85f64-5717-4562-b3fc-2c963f66afa7";
    private static final long TEST_GROUP_ID_1 = 5129226945881988L;
    private static final long TEST_GROUP_ID_2 = 2877427132196740L;
    private static final long TEST_USER_ID = 5448085317937028L;

    private static final DataClassificationSettings EXPECTED_ALL_PROPERTIES;
    private static final DataClassificationSettings EXPECTED_REQUIRED_PROPERTIES;
    private static final DataClassificationSettings EXPECTED_DISABLED_PLAN;
    private static final DataClassificationSettings EXPECTED_APPROVAL_NEEDED;

    static {
        ClassificationLabel confidentialAllProperties = label(CONFIDENTIAL_LABEL_ID, "Confidential",
                "Highly sensitive information", "#ffe0e3", 1, false);
        ClassificationLabel internalAllProperties = label(INTERNAL_LABEL_ID, "Internal",
                "For internal use only", "#b9f4c3", 2, true);
        ClassificationLabel confidentialRequired = label(CONFIDENTIAL_LABEL_ID, "Confidential",
                null, "#ffe0e3", 1, false);

        LabelApproverEntry labelApprover = new LabelApproverEntry();
        labelApprover.setLabelId(INTERNAL_LABEL_ID);
        labelApprover.setApprovers(List.of(
                approver(ApproverType.USERS, List.of(TEST_USER_ID)),
                approver(ApproverType.WORKSPACE_ADMINS, List.of())
        ));

        EXPECTED_ALL_PROPERTIES = settings(false, List.of(confidentialAllProperties, internalAllProperties),
                downgrade(DowngradeApprovalMode.CUSTOM, null, List.of(labelApprover)));
        EXPECTED_ALL_PROPERTIES.setGuidelinesUrl(GUIDELINES_URL);
        EXPECTED_ALL_PROPERTIES.setAllowManualChange(true);

        EXPECTED_REQUIRED_PROPERTIES = settings(false, List.of(confidentialRequired),
                downgrade(DowngradeApprovalMode.NONE, null, null));

        EXPECTED_DISABLED_PLAN = settings(true, List.of(),
                downgrade(DowngradeApprovalMode.NONE, null, null));

        EXPECTED_APPROVAL_NEEDED = settings(false, List.of(confidentialRequired),
                downgrade(DowngradeApprovalMode.APPROVAL_NEEDED, List.of(
                        approver(ApproverType.GROUPS, List.of(TEST_GROUP_ID_1, TEST_GROUP_ID_2)),
                        approver(ApproverType.USERS, List.of(TEST_USER_ID))
                ), null));
        EXPECTED_APPROVAL_NEEDED.setAllowManualChange(true);
    }

    @Test
    void testGetDataClassificationSettingsGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/governance/get-data-classification-settings/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.governanceResources().getDataClassificationSettings(TEST_PLAN_ID);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo(EXPECTED_PATH);
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(queryParams(wiremockRequest)).isEqualTo(Map.of(
                "planId", List.of(String.valueOf(TEST_PLAN_ID))
        ));
    }

    @Test
    void testGetDataClassificationSettingsAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/governance/get-data-classification-settings/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        DataClassificationSettings response = smartsheet.governanceResources()
                .getDataClassificationSettings(TEST_PLAN_ID);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        assertThat(wiremockRequest.getBodyAsString()).isEmpty();
        assertThat(response).usingRecursiveComparison().isEqualTo(EXPECTED_ALL_PROPERTIES);
    }

    @Test
    void testGetDataClassificationSettingsRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/governance/get-data-classification-settings/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        DataClassificationSettings response = smartsheet.governanceResources()
                .getDataClassificationSettings(TEST_PLAN_ID);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        assertThat(wiremockRequest.getBodyAsString()).isEmpty();
        assertThat(response).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_PROPERTIES);
    }

    @Test
    void testGetDataClassificationSettingsDisabledPlan() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/governance/get-data-classification-settings/disabled-plan",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        DataClassificationSettings response = smartsheet.governanceResources()
                .getDataClassificationSettings(TEST_PLAN_ID);

        assertThat(response).usingRecursiveComparison().isEqualTo(EXPECTED_DISABLED_PLAN);
    }

    @Test
    void testGetDataClassificationSettingsApprovalNeededMode() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/governance/get-data-classification-settings/downgrade-approval-mode-approval-needed",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        DataClassificationSettings response = smartsheet.governanceResources()
                .getDataClassificationSettings(TEST_PLAN_ID);

        assertThat(response).usingRecursiveComparison().isEqualTo(EXPECTED_APPROVAL_NEEDED);
    }

    @Test
    void testGetDataClassificationSettingsBySheetGeneratedUrlIsCorrect() throws SmartsheetException {
        assertAssetUrl("sheet");
    }

    @Test
    void testGetDataClassificationSettingsByReportGeneratedUrlIsCorrect() throws SmartsheetException {
        assertAssetUrl("report");
    }

    @Test
    void testGetDataClassificationSettingsBySightGeneratedUrlIsCorrect() throws SmartsheetException {
        assertAssetUrl("sight");
    }

    @Test
    void testGetDataClassificationSettingsError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.governanceResources().getDataClassificationSettings(TEST_PLAN_ID);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testGetDataClassificationSettingsError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.governanceResources().getDataClassificationSettings(TEST_PLAN_ID);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }

    private static void assertAssetUrl(String assetType) throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/governance/get-data-classification-settings/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.governanceResources().getDataClassificationSettings(assetType, TEST_ASSET_ID);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo(EXPECTED_PATH);
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(queryParams(wiremockRequest)).isEqualTo(Map.of(
                "assetType", List.of(assetType),
                "assetId", List.of(String.valueOf(TEST_ASSET_ID))
        ));
    }

    private static Map<String, List<String>> queryParams(LoggedRequest request) {
        return request.getQueryParams().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValues()));
    }

    private static DataClassificationSettings settings(boolean isDisabled, List<ClassificationLabel> labels,
                                                       DowngradeApprovalSettings downgradeApprovalSettings) {
        DataClassificationSettings settings = new DataClassificationSettings();
        settings.setOrgId(TEST_ORG_ID);
        settings.setPlanId(TEST_PLAN_ID);
        settings.setIsDisabled(isDisabled);
        settings.setLabels(labels);
        settings.setDowngradeApprovalSettings(downgradeApprovalSettings);
        return settings;
    }

    private static ClassificationLabel label(String id, String name, String description, String color,
                                             int sensitivityOrder, boolean isDefault) {
        ClassificationLabel label = new ClassificationLabel();
        label.setId(id);
        label.setName(name);
        label.setDescription(description);
        label.setColor(color);
        label.setSensitivityOrder(sensitivityOrder);
        label.setIsDefault(isDefault);
        return label;
    }

    private static DowngradeApprovalSettings downgrade(DowngradeApprovalMode mode, List<ApproverEntry> approvers,
                                                       List<LabelApproverEntry> labelApprovers) {
        DowngradeApprovalSettings downgrade = new DowngradeApprovalSettings();
        downgrade.setMode(mode);
        downgrade.setApprovers(approvers);
        downgrade.setLabelApprovers(labelApprovers);
        return downgrade;
    }

    private static ApproverEntry approver(ApproverType type, List<Long> ids) {
        ApproverEntry approver = new ApproverEntry();
        approver.setType(type);
        approver.setIds(ids);
        return approver;
    }
}
