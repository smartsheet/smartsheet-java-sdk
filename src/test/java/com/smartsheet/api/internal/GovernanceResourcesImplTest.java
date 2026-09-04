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

import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.DataClassificationSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GovernanceResourcesImplTest extends ResourcesImplBase {

    private GovernanceResourcesImpl governanceResources;

    @BeforeEach
    public void setUp() throws Exception {
        governanceResources = new GovernanceResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    // ── URL + query param ─────────────────────────────────────────────────────

    @Test
    void testGetDataClassificationSettings_urlAndQueryParam() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/getDataClassificationSettings.json"));

        governanceResources.getDataClassificationSettings(41878788L);

        String requestUrl = server.getLastRequestUrl();
        assertThat(requestUrl).contains("/governance/data-classification/settings");
        assertThat(requestUrl).contains("planId=41878788");
    }

    // ── APPROVAL_NEEDED mode (happy path) ─────────────────────────────────────

    @Test
    void testGetDataClassificationSettings_approvalNeededMode() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/getDataClassificationSettings.json"));

        DataClassificationSettings settings = governanceResources.getDataClassificationSettings(41878788L);

        assertThat(settings).isNotNull();
        assertThat(settings.getOrgId()).isEqualTo(1244212L);
        assertThat(settings.getPlanId()).isEqualTo(41878788L);
        assertThat(settings.getIsDisabled()).isFalse();
        assertThat(settings.getGuidelinesUrl()).isEqualTo("https://wiki.example.com/classification-guide");
        assertThat(settings.getAllowManualChange()).isTrue();

        assertThat(settings.getLabels()).hasSize(1);
        assertThat(settings.getLabels().get(0).getId()).isEqualTo("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        assertThat(settings.getLabels().get(0).getName()).isEqualTo("Confidential");
        assertThat(settings.getLabels().get(0).getDescription()).isEqualTo("Highly sensitive information");
        assertThat(settings.getLabels().get(0).getColor()).isEqualTo("#FFE0E3");
        assertThat(settings.getLabels().get(0).getSensitivityOrder()).isEqualTo(1);
        assertThat(settings.getLabels().get(0).getIsDefault()).isFalse();

        assertThat(settings.getDowngradeApprovalSettings()).isNotNull();
        assertThat(settings.getDowngradeApprovalSettings().getMode()).isEqualTo("APPROVAL_NEEDED");
        assertThat(settings.getDowngradeApprovalSettings().getApprovers()).hasSize(1);
        assertThat(settings.getDowngradeApprovalSettings().getApprovers().get(0).getType()).isEqualTo("GROUPS");
        assertThat(settings.getDowngradeApprovalSettings().getApprovers().get(0).getIds()).containsExactly(5001L, 5002L);
        assertThat(settings.getDowngradeApprovalSettings().getLabelApprovers()).isNull();
    }

    // ── CUSTOM mode ───────────────────────────────────────────────────────────

    @Test
    void testGetDataClassificationSettings_customMode() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/getDataClassificationSettingsCustomMode.json"));

        DataClassificationSettings settings = governanceResources.getDataClassificationSettings(41878788L);

        assertThat(settings.getDowngradeApprovalSettings().getMode()).isEqualTo("CUSTOM");
        // CUSTOM: no top-level approvers, only per-label approvers
        assertThat(settings.getDowngradeApprovalSettings().getApprovers()).isNull();
        assertThat(settings.getDowngradeApprovalSettings().getLabelApprovers()).hasSize(1);
        assertThat(settings.getDowngradeApprovalSettings().getLabelApprovers().get(0).getLabelId())
                .isEqualTo("4aa85f64-5717-4562-b3fc-2c963f66afa7");

        var labelApprovers = settings.getDowngradeApprovalSettings().getLabelApprovers().get(0).getApprovers();
        assertThat(labelApprovers).hasSize(2);
        assertThat(labelApprovers.get(0).getType()).isEqualTo("USERS");
        assertThat(labelApprovers.get(0).getIds()).containsExactly(7001L);
        assertThat(labelApprovers.get(1).getType()).isEqualTo("WORKSPACE_ADMINS");
        assertThat(labelApprovers.get(1).getIds()).isEmpty();
    }

    // ── NONE mode (required-only fields) ──────────────────────────────────────

    @Test
    void testGetDataClassificationSettings_noneMode() throws IOException, SmartsheetException {
        server.setResponseBody("{\"orgId\":1244212,\"planId\":41878788,\"isDisabled\":false," +
                "\"labels\":[{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"name\":\"Confidential\"," +
                "\"color\":\"#FFE0E3\",\"sensitivityOrder\":1,\"isDefault\":false}]," +
                "\"downgradeApprovalSettings\":{\"mode\":\"NONE\"}}");

        DataClassificationSettings settings = governanceResources.getDataClassificationSettings(41878788L);

        assertThat(settings.getDowngradeApprovalSettings().getMode()).isEqualTo("NONE");
        assertThat(settings.getDowngradeApprovalSettings().getApprovers()).isNull();
        assertThat(settings.getDowngradeApprovalSettings().getLabelApprovers()).isNull();
        assertThat(settings.getGuidelinesUrl()).isNull();
        assertThat(settings.getAllowManualChange()).isNull();
        assertThat(settings.getLabels().get(0).getDescription()).isNull();
    }

    // ── Disabled plan ─────────────────────────────────────────────────────────

    @Test
    void testGetDataClassificationSettings_disabledPlan() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/getDataClassificationSettingsDisabledPlan.json"));

        DataClassificationSettings settings = governanceResources.getDataClassificationSettings(41878788L);

        assertThat(settings.getIsDisabled()).isTrue();
        assertThat(settings.getLabels()).isEmpty();
        assertThat(settings.getGuidelinesUrl()).isNull();
        assertThat(settings.getAllowManualChange()).isNull();
        assertThat(settings.getDowngradeApprovalSettings().getMode()).isEqualTo("NONE");
        assertThat(settings.getDowngradeApprovalSettings().getApprovers()).isNull();
    }

    // ── assetType + assetId ───────────────────────────────────────────────────

    @Test
    void testGetDataClassificationSettings_byAsset_urlContainsAssetParams() throws SmartsheetException, IOException {
        server.setStatus(200);
        server.setResponseBody(new File("src/test/resources/mock-api-responses/governance/get_data_classification_settings/all-response-body-properties.json"));

        governanceResources.getDataClassificationSettings("sheet", 112398785741L);

        assertThat(server.getLastRequest().getUri())
                .contains("assetType=sheet")
                .contains("assetId=112398785741")
                .doesNotContain("planId");
    }

    @Test
    void testGetDataClassificationSettings_byAsset_returnsSettings() throws SmartsheetException, IOException {
        server.setStatus(200);
        server.setResponseBody(new File("src/test/resources/mock-api-responses/governance/get_data_classification_settings/all-response-body-properties.json"));

        DataClassificationSettings settings = governanceResources.getDataClassificationSettings("sheet", 112398785741L);

        assertThat(settings).isNotNull();
    }

// ── Error responses ───────────────────────────────────────────────────────

    @Test
    void testGetDataClassificationSettings_error400() {
        server.setStatus(400);
        server.setResponseBody("{\"errorCode\":1032,\"message\":\"Bad Request\"}");

        assertThatThrownBy(() -> governanceResources.getDataClassificationSettings(41878788L))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void testGetDataClassificationSettings_error403() {
        server.setStatus(403);
        server.setResponseBody("{\"errorCode\":1004,\"message\":\"You are not authorized to perform this action.\"}");

        assertThatThrownBy(() -> governanceResources.getDataClassificationSettings(41878788L))
                .isInstanceOf(SmartsheetException.class);
    }

    @Test
    void testGetDataClassificationSettings_error404() {
        server.setStatus(404);
        server.setResponseBody("{\"errorCode\":1006,\"message\":\"Not Found\"}");

        assertThatThrownBy(() -> governanceResources.getDataClassificationSettings(41878788L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetDataClassificationSettings_error500() {
        server.setStatus(500);
        server.setResponseBody("{\"errorCode\":1500,\"message\":\"Internal Server Error\"}");

        assertThatThrownBy(() -> governanceResources.getDataClassificationSettings(41878788L))
                .isInstanceOf(SmartsheetException.class);
    }
}
