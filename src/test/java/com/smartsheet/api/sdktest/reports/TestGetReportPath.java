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

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.PathLeaf;
import com.smartsheet.api.models.ReportPathNode;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestGetReportPath {

    private static final long TEST_REPORT_ID = 1234567890123456L;
    private static final String GET_NESTED_REPORT_PATH_STUB = "/reports/get-nested-report-path/all-response-body-properties";
    private static final long WORKSPACE_ID = 4509918431602564L;
    private static final String WORKSPACE_NAME = "Sample Workspace";
    private static final String WORKSPACE_PERMALINK = "https://app.smartsheet.com/workspaces/mock_workspace_id";
    private static final long ROOT_REPORT_ID = 5678901234567890L;

    private static final ReportPathNode EXPECTED_NESTED_RESULT;
    private static final ReportPathNode EXPECTED_ROOT_RESULT;

    static {
        PathLeaf nestedReport = new PathLeaf();
        nestedReport.setId(3456789012345678L);
        nestedReport.setName("Project Report");
        nestedReport.setPermalink("https://app.smartsheet.com/reports/3456789012345678");
        nestedReport.setAccessLevel(AccessLevel.ADMIN);
        nestedReport.setCreatedAt(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        nestedReport.setModifiedAt(ZonedDateTime.parse("2024-06-01T00:00:00Z"));

        ReportPathNode subfolder = new ReportPathNode();
        subfolder.setId(2345678901234567L);
        subfolder.setName("Project Plans Subfolder");
        subfolder.setPermalink("https://app.smartsheet.com/folders/2345678901234567");
        subfolder.setReports(List.of(nestedReport));

        ReportPathNode folder = new ReportPathNode();
        folder.setId(1234567890123456L);
        folder.setName("Project Plans");
        folder.setPermalink("https://app.smartsheet.com/folders/1234567890123456");
        folder.setFolders(List.of(subfolder));

        EXPECTED_NESTED_RESULT = new ReportPathNode();
        EXPECTED_NESTED_RESULT.setId(WORKSPACE_ID);
        EXPECTED_NESTED_RESULT.setName(WORKSPACE_NAME);
        EXPECTED_NESTED_RESULT.setPermalink(WORKSPACE_PERMALINK);
        EXPECTED_NESTED_RESULT.setAccessLevel(AccessLevel.OWNER);
        EXPECTED_NESTED_RESULT.setFolders(List.of(folder));

        PathLeaf rootReport = new PathLeaf();
        rootReport.setId(ROOT_REPORT_ID);
        rootReport.setName("Root Level Report");
        rootReport.setPermalink("https://app.smartsheet.com/reports/rootlevel");
        rootReport.setAccessLevel(AccessLevel.ADMIN);
        rootReport.setCreatedAt(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        rootReport.setModifiedAt(ZonedDateTime.parse("2024-06-01T00:00:00Z"));

        EXPECTED_ROOT_RESULT = new ReportPathNode();
        EXPECTED_ROOT_RESULT.setId(WORKSPACE_ID);
        EXPECTED_ROOT_RESULT.setName(WORKSPACE_NAME);
        EXPECTED_ROOT_RESULT.setPermalink(WORKSPACE_PERMALINK);
        EXPECTED_ROOT_RESULT.setAccessLevel(AccessLevel.OWNER);
        EXPECTED_ROOT_RESULT.setReports(List.of(rootReport));
    }

    @Test
    void testGetReportPathGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                GET_NESTED_REPORT_PATH_STUB,
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().getReportPath(TEST_REPORT_ID);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/path");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testGetReportPathAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                GET_NESTED_REPORT_PATH_STUB,
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        ReportPathNode result = smartsheet.reportResources().getReportPath(TEST_REPORT_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_NESTED_RESULT);
        assertThat(result.getReportPath()).isEqualTo("Sample Workspace/Project Plans/Project Plans Subfolder/Project Report");
    }

    @Test
    void testGetReportPathRootLevelAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-root-report-path/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        ReportPathNode result = smartsheet.reportResources().getReportPath(TEST_REPORT_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_ROOT_RESULT);
        assertThat(result.getReportPath()).isEqualTo("Sample Workspace/Root Level Report");
    }

    @Test
    void testGetReportPathError404Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/404-response",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.reportResources().getReportPath(TEST_REPORT_ID)
        );
        assertThat(exception.getMessage()).isEqualTo("Not Found");
    }

    @Test
    void testGetReportPathError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/500-response",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.reportResources().getReportPath(TEST_REPORT_ID)
        );
        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }
}
