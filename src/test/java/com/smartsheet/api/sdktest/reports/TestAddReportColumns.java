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
import com.smartsheet.api.models.ReportColumn;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestAddReportColumns {

    private List<ReportColumn> testColumns;

    /**
     * Expected request body for column 1 (checkbox) - per OpenAPI spec.
     */
    private static final Map<String, Object> EXPECTED_COLUMN1_REQUEST = Map.of(
            "title", "Item selected",
            "type", "CHECKBOX",
            "index", 4,
            "sheetNameColumn", false
    );

    /**
     * Expected request body for column 2 (sheet name) - per OpenAPI spec.
     */
    private static final Map<String, Object> EXPECTED_COLUMN2_REQUEST = Map.of(
            "title", "Sheet name",
            "type", "TEXT_NUMBER",
            "index", 5,
            "sheetNameColumn", true
    );

    @BeforeEach
    void setUp() {
        ReportColumn column1 = new ReportColumn();
        column1.setTitle("Item selected");
        column1.setType(ColumnType.CHECKBOX);
        column1.setIndex(4);

        ReportColumn column2 = new ReportColumn();
        column2.setTitle("Sheet name");
        column2.setType(ColumnType.TEXT_NUMBER);
        column2.setIndex(5);
        column2.setSheetNameColumn(true);

        testColumns = new ArrayList<>();
        testColumns.add(column1);
        testColumns.add(column2);
    }

    @Test
    void testAddReportColumnsGeneratedUrlIsCorrect() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/add-report-columns/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().addReportColumns(TEST_REPORT_ID, testColumns);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/columns");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.POST);

        // Validate the request body matches the expected structure as a whole
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> expectedRequestBody = List.of(EXPECTED_COLUMN1_REQUEST, EXPECTED_COLUMN2_REQUEST);
        String expectedJson = objectMapper.writeValueAsString(expectedRequestBody);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));
    }

    @Test
    void testAddReportColumnsAllResponseBodyProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/add-report-columns/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        List<ReportColumn> addedColumns = smartsheet.reportResources().addReportColumns(TEST_REPORT_ID, testColumns);

        // Validate request body matches expected structure as a whole
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> expectedRequestBody = List.of(EXPECTED_COLUMN1_REQUEST, EXPECTED_COLUMN2_REQUEST);
        String expectedJson = objectMapper.writeValueAsString(expectedRequestBody);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        // Verify response: all properties including virtualId
        assertThat(addedColumns).isNotNull();
        assertThat(addedColumns).hasSize(2);

        // Verify first column - checkbox (response includes all properties)
        assertThat(addedColumns.get(0).getVirtualId()).isEqualTo(12345L);
        assertThat(addedColumns.get(0).getTitle()).isEqualTo("Item selected");
        assertThat(addedColumns.get(0).getType()).isEqualTo(ColumnType.CHECKBOX);
        assertThat(addedColumns.get(0).getIndex()).isEqualTo(4);
        assertThat(addedColumns.get(0).getHidden()).isFalse();
        assertThat(addedColumns.get(0).getVersion()).isEqualTo(0);
        assertThat(addedColumns.get(0).getWidth()).isEqualTo(150);

        // Verify second column - sheet name (response includes all properties)
        assertThat(addedColumns.get(1).getVirtualId()).isEqualTo(12346L);
        assertThat(addedColumns.get(1).getTitle()).isEqualTo("Sheet name");
        assertThat(addedColumns.get(1).getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(addedColumns.get(1).getIndex()).isEqualTo(5);
        assertThat(addedColumns.get(1).getSheetNameColumn()).isTrue();
        assertThat(addedColumns.get(1).getHidden()).isFalse();
        assertThat(addedColumns.get(1).getVersion()).isEqualTo(0);
        assertThat(addedColumns.get(1).getWidth()).isEqualTo(150);
    }

    @Test
    void testAddReportColumnsRequiredResponseBodyProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/add-report-columns/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        List<ReportColumn> addedColumns = smartsheet.reportResources().addReportColumns(TEST_REPORT_ID, testColumns);

        // Validate request body matches expected structure as a whole (per OpenAPI spec)
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> expectedRequestBody = List.of(EXPECTED_COLUMN1_REQUEST, EXPECTED_COLUMN2_REQUEST);
        String expectedJson = objectMapper.writeValueAsString(expectedRequestBody);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        // Verify response parsing
        assertThat(addedColumns).isNotNull();
        assertThat(addedColumns).hasSize(2);

        // Verify first column - required properties only
        assertThat(addedColumns.get(0).getVirtualId()).isEqualTo(12345L);
        assertThat(addedColumns.get(0).getTitle()).isEqualTo("Item selected");
        assertThat(addedColumns.get(0).getType()).isEqualTo(ColumnType.CHECKBOX);
        assertThat(addedColumns.get(0).getIndex()).isEqualTo(4);

        // Verify second column - required properties only
        assertThat(addedColumns.get(1).getVirtualId()).isEqualTo(12346L);
        assertThat(addedColumns.get(1).getTitle()).isEqualTo("Sheet name");
        assertThat(addedColumns.get(1).getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(addedColumns.get(1).getIndex()).isEqualTo(5);
    }

    @Test
    void testAddReportColumnsInvalidArgument() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/add-report-columns/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.reportResources().addReportColumns(TEST_REPORT_ID, null);
        });

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.reportResources().addReportColumns(TEST_REPORT_ID, new ArrayList<>());
        });
        assertThat(exception.getMessage()).isEqualTo("columns should not be empty.");
    }

    @Test
    void testAddReportColumnsError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().addReportColumns(TEST_REPORT_ID, testColumns);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testAddReportColumnsError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().addReportColumns(TEST_REPORT_ID, testColumns);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
