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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestListReportColumns {

    private static final String TEST_LAST_KEY = "someLastKeyToken";
    private static final long TEST_MAX_ITEMS = 50L;

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

        assertThat(response).isNotNull();
        assertThat(response.getLastKey()).isNull();
        assertThat(response.getData()).hasSize(4);

        // col1: Task Name - primary, with autoNumberFormat
        ReportColumn col1 = response.getData().get(0);
        assertThat(col1.getVirtualId()).isEqualTo(7001L);
        assertThat(col1.getIndex()).isEqualTo(0);
        assertThat(col1.getTitle()).isEqualTo("Task Name");
        assertThat(col1.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(col1.getPrimary()).isTrue();
        assertThat(col1.getWidth()).isEqualTo(150);
        assertThat(col1.getHidden()).isFalse();
        assertThat(col1.getValidation()).isTrue();
        assertThat(col1.getVersion()).isEqualTo(0);
        AutoNumberFormat autoNumberFormat = col1.getAutoNumberFormat();
        assertThat(autoNumberFormat).isNotNull();
        assertThat(autoNumberFormat.getFill()).isEqualTo("0001");
        assertThat(autoNumberFormat.getPrefix()).isEqualTo("TASK-");
        assertThat(autoNumberFormat.getStartingNumber()).isEqualTo(1L);
        assertThat(autoNumberFormat.getSuffix()).isEqualTo("");

        // col2: Status - PICKLIST
        ReportColumn col2 = response.getData().get(1);
        assertThat(col2.getVirtualId()).isEqualTo(7002L);
        assertThat(col2.getIndex()).isEqualTo(1);
        assertThat(col2.getTitle()).isEqualTo("Status");
        assertThat(col2.getType()).isEqualTo(ColumnType.PICKLIST);
        assertThat(col2.getWidth()).isEqualTo(120);
        assertThat(col2.getHidden()).isFalse();
        assertThat(col2.getValidation()).isFalse();
        assertThat(col2.getVersion()).isEqualTo(0);

        // col3: Created By - system column CREATED_BY
        ReportColumn col3 = response.getData().get(2);
        assertThat(col3.getVirtualId()).isEqualTo(7003L);
        assertThat(col3.getIndex()).isEqualTo(2);
        assertThat(col3.getTitle()).isEqualTo("Created By");
        assertThat(col3.getType()).isEqualTo(ColumnType.CONTACT_LIST);
        assertThat(col3.getSystemColumnType()).isEqualTo(SystemColumnType.CREATED_BY);
        assertThat(col3.getWidth()).isEqualTo(150);
        assertThat(col3.getHidden()).isFalse();
        assertThat(col3.getValidation()).isFalse();
        assertThat(col3.getVersion()).isEqualTo(1);

        // col4: Sheet Name - sheetNameColumn
        ReportColumn col4 = response.getData().get(3);
        assertThat(col4.getVirtualId()).isEqualTo(7004L);
        assertThat(col4.getIndex()).isEqualTo(3);
        assertThat(col4.getTitle()).isEqualTo("Sheet Name");
        assertThat(col4.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(col4.getSheetNameColumn()).isTrue();
        assertThat(col4.getWidth()).isEqualTo(200);
        assertThat(col4.getHidden()).isFalse();
        assertThat(col4.getValidation()).isFalse();
        assertThat(col4.getVersion()).isEqualTo(0);
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

        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(2);

        ReportColumn col1 = response.getData().get(0);
        assertThat(col1.getIndex()).isEqualTo(0);
        assertThat(col1.getTitle()).isEqualTo("Task Name");
        assertThat(col1.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(col1.getPrimary()).isTrue();

        ReportColumn col2 = response.getData().get(1);
        assertThat(col2.getIndex()).isEqualTo(1);
        assertThat(col2.getType()).isEqualTo(ColumnType.DATETIME);
        assertThat(col2.getSystemColumnType()).isEqualTo(SystemColumnType.CREATED_DATE);
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
    void testListReportColumnsOmitsQueryParamsWhenNotProvided() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/list-report-columns/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().listReportColumns(TEST_REPORT_ID, null, null);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);

        assertThat(wiremockRequest.getQueryParams().containsKey("lastKey")).isFalse();
        assertThat(wiremockRequest.getQueryParams().containsKey("maxItems")).isFalse();
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
