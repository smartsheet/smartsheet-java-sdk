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
import com.smartsheet.api.models.AutoNumberFormat;
import com.smartsheet.api.models.ReportColumn;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.SystemColumnType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestListReportColumns {

    private static final String TEST_LAST_KEY = "someLastKeyToken";
    private static final long TEST_MAX_ITEMS = 50L;

    private static final TokenPaginatedResult<ReportColumn> EXPECTED_ALL_PROPERTIES;
    private static final TokenPaginatedResult<ReportColumn> EXPECTED_REQUIRED_PROPERTIES;

    static {
        ReportColumn col1 = new ReportColumn();
        col1.setVirtualId(7001L);
        col1.setIndex(0);
        col1.setTitle("Task Name");
        col1.setType(ColumnType.TEXT_NUMBER);
        col1.setPrimary(true);
        col1.setWidth(150);
        col1.setHidden(false);
        col1.setValidation(true);
        col1.setVersion(0);
        col1.setAutoNumberFormat(new AutoNumberFormat()
                .setFill("0001")
                .setPrefix("TASK-")
                .setStartingNumber(1L)
                .setSuffix(""));

        ReportColumn col2 = new ReportColumn();
        col2.setVirtualId(7002L);
        col2.setIndex(1);
        col2.setTitle("Status");
        col2.setType(ColumnType.PICKLIST);
        col2.setWidth(120);
        col2.setHidden(false);
        col2.setValidation(false);
        col2.setVersion(0);

        ReportColumn col3 = new ReportColumn();
        col3.setVirtualId(7003L);
        col3.setIndex(2);
        col3.setTitle("Created By");
        col3.setType(ColumnType.CONTACT_LIST);
        col3.setSystemColumnType(SystemColumnType.CREATED_BY);
        col3.setWidth(150);
        col3.setHidden(false);
        col3.setValidation(false);
        col3.setVersion(1);

        ReportColumn col4 = new ReportColumn();
        col4.setVirtualId(7004L);
        col4.setIndex(3);
        col4.setTitle("Sheet Name");
        col4.setType(ColumnType.TEXT_NUMBER);
        col4.setSheetNameColumn(true);
        col4.setWidth(200);
        col4.setHidden(false);
        col4.setValidation(false);
        col4.setVersion(0);

        EXPECTED_ALL_PROPERTIES = new TokenPaginatedResult<ReportColumn>()
                .setData(new ArrayList<>(List.of(col1, col2, col3, col4)));

        ReportColumn req1 = new ReportColumn();
        req1.setIndex(0);
        req1.setTitle("Task Name");
        req1.setType(ColumnType.TEXT_NUMBER);
        req1.setPrimary(true);

        ReportColumn req2 = new ReportColumn();
        req2.setIndex(1);
        req2.setType(ColumnType.DATETIME);
        req2.setSystemColumnType(SystemColumnType.CREATED_DATE);

        EXPECTED_REQUIRED_PROPERTIES = new TokenPaginatedResult<ReportColumn>()
                .setData(new ArrayList<>(List.of(req1, req2)));
    }

    @Test
    void testListReportColumnsGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-columns/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().listReportColumns(TEST_REPORT_ID, TEST_LAST_KEY, TEST_MAX_ITEMS);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/columns");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(receivedQueryParams.get("lastKey").getValues()).isEqualTo(List.of(TEST_LAST_KEY));
        assertThat(receivedQueryParams.get("maxItems").getValues()).isEqualTo(List.of(Long.toString(TEST_MAX_ITEMS)));
    }

    @Test
    void testListReportColumnsAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-columns/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        TokenPaginatedResult<ReportColumn> response = smartsheet.reportResources()
                .listReportColumns(TEST_REPORT_ID, null, null);

        assertThat(response).usingRecursiveComparison().isEqualTo(EXPECTED_ALL_PROPERTIES);
    }

    @Test
    void testListReportColumnsRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-columns/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        TokenPaginatedResult<ReportColumn> response = smartsheet.reportResources()
                .listReportColumns(TEST_REPORT_ID, null, null);

        assertThat(response).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_PROPERTIES);
    }

    @Test
    void testListReportColumnsError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().listReportColumns(TEST_REPORT_ID, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testListReportColumnsError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().listReportColumns(TEST_REPORT_ID, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
