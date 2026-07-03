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
import com.smartsheet.api.models.AutoNumberFormat;
import com.smartsheet.api.models.ReportColumn;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestGetReportColumn {

    private static final long TEST_COLUMN_VIRTUAL_ID = 7001L;

    private static final ReportColumn EXPECTED_ALL_PROPERTIES;
    private static final ReportColumn EXPECTED_REQUIRED_PROPERTIES;

    static {
        EXPECTED_ALL_PROPERTIES = new ReportColumn();
        EXPECTED_ALL_PROPERTIES.setVirtualId(7001L);
        EXPECTED_ALL_PROPERTIES.setIndex(0);
        EXPECTED_ALL_PROPERTIES.setTitle("Task Name");
        EXPECTED_ALL_PROPERTIES.setType(ColumnType.TEXT_NUMBER);
        EXPECTED_ALL_PROPERTIES.setPrimary(true);
        EXPECTED_ALL_PROPERTIES.setWidth(150);
        EXPECTED_ALL_PROPERTIES.setHidden(false);
        EXPECTED_ALL_PROPERTIES.setValidation(true);
        EXPECTED_ALL_PROPERTIES.setVersion(0);
        EXPECTED_ALL_PROPERTIES.setAutoNumberFormat(new AutoNumberFormat()
                .setFill("0001")
                .setPrefix("TASK-")
                .setStartingNumber(1L)
                .setSuffix(""));

        EXPECTED_REQUIRED_PROPERTIES = new ReportColumn();
        EXPECTED_REQUIRED_PROPERTIES.setIndex(0);
        EXPECTED_REQUIRED_PROPERTIES.setTitle("Task Name");
        EXPECTED_REQUIRED_PROPERTIES.setType(ColumnType.TEXT_NUMBER);
        EXPECTED_REQUIRED_PROPERTIES.setPrimary(true);
    }

    @Test
    void testGetReportColumnGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-column/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().getReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/columns/" + TEST_COLUMN_VIRTUAL_ID);
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testGetReportColumnWithLevelGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-column/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().getReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID, 3);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/columns/" + TEST_COLUMN_VIRTUAL_ID + "?level=3");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testGetReportColumnAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-column/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        ReportColumn column = smartsheet.reportResources().getReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        assertThat(wiremockRequest.getBodyAsString()).isEmpty();
        assertThat(column).usingRecursiveComparison().isEqualTo(EXPECTED_ALL_PROPERTIES);
    }

    @Test
    void testGetReportColumnRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-column/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        ReportColumn column = smartsheet.reportResources().getReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID);

        assertThat(column).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_PROPERTIES);
    }

    @Test
    void testGetReportColumnError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().getReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testGetReportColumnError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().getReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
