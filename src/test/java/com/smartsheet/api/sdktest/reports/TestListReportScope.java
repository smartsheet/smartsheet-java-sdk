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

package com.smartsheet.api.sdktest.reports;

import com.github.tomakehurst.wiremock.http.QueryParameter;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.ReportScopeInclusion;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.enums.ReportAssetType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestListReportScope {
    private static final String TEST_LAST_KEY = "someLastKeyToken";
    private static final long TEST_MAX_ITEMS = 50L;

    @Test
    void testListReportScopeGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-scope/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().listReportScope(TEST_REPORT_ID, TEST_LAST_KEY, TEST_MAX_ITEMS);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/reports/2233445566/scope");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(receivedQueryParams.get("lastKey").getValues()).isEqualTo(List.of(TEST_LAST_KEY));
        assertThat(receivedQueryParams.get("maxItems").getValues()).isEqualTo(List.of(Long.toString(TEST_MAX_ITEMS)));
    }

    @Test
    void testListReportScopeAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-scope/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        TokenPaginatedResult<ReportScopeInclusion> response = smartsheet.reportResources()
                .listReportScope(TEST_REPORT_ID, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getLastKey()).isNull();
        assertThat(response.getData()).hasSize(3);

        assertThat(response.getData().get(0).getAssetType()).isEqualTo(ReportAssetType.SHEET);
        assertThat(response.getData().get(0).getAssetId()).isEqualTo(2331373580117892L);

        assertThat(response.getData().get(1).getAssetType()).isEqualTo(ReportAssetType.WORKSPACE);
        assertThat(response.getData().get(1).getAssetId()).isEqualTo(7879278542455688L);

        assertThat(response.getData().get(2).getAssetType()).isEqualTo(ReportAssetType.SHEET);
        assertThat(response.getData().get(2).getAssetId()).isEqualTo(1234567890123456L);
    }

    @Test
    void testListReportScopeRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-scope/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        TokenPaginatedResult<ReportScopeInclusion> response = smartsheet.reportResources()
                .listReportScope(TEST_REPORT_ID, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getData().get(0).getAssetType()).isEqualTo(ReportAssetType.SHEET);
        assertThat(response.getData().get(0).getAssetId()).isEqualTo(2331373580117892L);
    }

    @Test
    void testListReportScopeError500Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().listReportScope(TEST_REPORT_ID, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testListReportScopeOmitsQueryParamsWhenNotProvided() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-scope/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().listReportScope(TEST_REPORT_ID, null, null);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);

        assertThat(wiremockRequest.getQueryParams().containsKey("lastKey")).isFalse();
        assertThat(wiremockRequest.getQueryParams().containsKey("maxItems")).isFalse();
    }

    @Test
    void testListReportScopeError400Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().listReportScope(TEST_REPORT_ID, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
