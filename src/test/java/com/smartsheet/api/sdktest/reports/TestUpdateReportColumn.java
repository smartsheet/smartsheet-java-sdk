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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.AutoNumberFormat;
import com.smartsheet.api.models.ReportColumn;
import com.smartsheet.api.models.UpdateReportColumnRequest;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUpdateReportColumn {

    private static final long TEST_COLUMN_VIRTUAL_ID = 7001L;

    private UpdateReportColumnRequest testRequest;

    private static final Map<String, Object> EXPECTED_REQUEST_BODY = Map.of(
            "title", "Updated Task Name",
            "index", 2,
            "width", 200,
            "hidden", false
    );

    @BeforeEach
    void setUp() {
        testRequest = new UpdateReportColumnRequest();
        testRequest.setTitle("Updated Task Name");
        testRequest.setIndex(2);
        testRequest.setWidth(200);
        testRequest.setHidden(false);
    }

    @Test
    void testUpdateReportColumnGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-column/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().updateReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID, testRequest);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/columns/" + TEST_COLUMN_VIRTUAL_ID);
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.PUT);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testUpdateReportColumnAllResponseBodyProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-column/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        ReportColumn result = smartsheet.reportResources().updateReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID, testRequest);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(EXPECTED_REQUEST_BODY);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        assertThat(result).isInstanceOf(ReportColumn.class);
        assertThat(result.getVirtualId()).isEqualTo(7001L);
        assertThat(result.getIndex()).isEqualTo(2);
        assertThat(result.getTitle()).isEqualTo("Updated Task Name");
        assertThat(result.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getPrimary()).isTrue();
        assertThat(result.getWidth()).isEqualTo(200);
        assertThat(result.getHidden()).isFalse();
        assertThat(result.getValidation()).isTrue();
        assertThat(result.getVersion()).isEqualTo(0);

        AutoNumberFormat autoNumberFormat = result.getAutoNumberFormat();
        assertThat(autoNumberFormat).isNotNull();
        assertThat(autoNumberFormat.getFill()).isEqualTo("0001");
        assertThat(autoNumberFormat.getPrefix()).isEqualTo("TASK-");
        assertThat(autoNumberFormat.getStartingNumber()).isEqualTo(1L);
        assertThat(autoNumberFormat.getSuffix()).isEqualTo("");
    }

    @Test
    void testUpdateReportColumnRequiredResponseBodyProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-column/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        ReportColumn result = smartsheet.reportResources().updateReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID, testRequest);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(EXPECTED_REQUEST_BODY);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        assertThat(result).isInstanceOf(ReportColumn.class);
        assertThat(result.getIndex()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("Updated Column");
        assertThat(result.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getPrimary()).isTrue();
        assertThat(result.getVirtualId()).isNull();
    }

    @Test
    void testUpdateReportColumnInvalidArgument() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-column/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.reportResources().updateReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID, null);
        });
    }

    @Test
    void testUpdateReportColumnError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().updateReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID, testRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testUpdateReportColumnError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().updateReportColumn(TEST_REPORT_ID, TEST_COLUMN_VIRTUAL_ID, testRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
