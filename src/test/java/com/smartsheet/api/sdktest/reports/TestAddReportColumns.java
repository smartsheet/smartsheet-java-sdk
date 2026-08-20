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
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.SystemColumnType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestAddReportColumns {

    private List<ReportColumn> testColumns;

    private static final List<ReportColumn> EXPECTED_ALL_COLUMNS;
    private static final List<ReportColumn> EXPECTED_REQUIRED_COLUMNS;

    static {
        ReportColumn col1All = new ReportColumn();
        col1All.setVirtualId(12345L);
        col1All.setIndex(4);
        col1All.setTitle("Item selected");
        col1All.setType(ColumnType.CHECKBOX);
        col1All.setHidden(false);
        col1All.setVersion(0);
        col1All.setWidth(150);
        col1All.setValidation(false);

        ReportColumn col2All = new ReportColumn();
        col2All.setVirtualId(12346L);
        col2All.setIndex(5);
        col2All.setTitle("Sheet name");
        col2All.setType(ColumnType.TEXT_NUMBER);
        col2All.setSheetNameColumn(true);
        col2All.setHidden(false);
        col2All.setVersion(0);
        col2All.setWidth(150);
        col2All.setValidation(false);

        ReportColumn col3All = new ReportColumn();
        col3All.setVirtualId(12347L);
        col3All.setIndex(6);
        col3All.setTitle("Created By");
        col3All.setType(ColumnType.CONTACT_LIST);
        col3All.setSystemColumnType(SystemColumnType.CREATED_BY);
        col3All.setHidden(false);
        col3All.setVersion(0);
        col3All.setWidth(150);
        col3All.setValidation(false);

        ReportColumn col4All = new ReportColumn();
        col4All.setVirtualId(12348L);
        col4All.setIndex(7);
        col4All.setTitle("Primary");
        col4All.setType(ColumnType.TEXT_NUMBER);
        col4All.setPrimary(true);
        col4All.setHidden(false);
        col4All.setVersion(0);
        col4All.setWidth(200);
        col4All.setValidation(false);

        AutoNumberFormat autoNumberFormat = new AutoNumberFormat();
        autoNumberFormat.setFill("000");
        autoNumberFormat.setPrefix("TASK-");
        autoNumberFormat.setStartingNumber(1L);
        autoNumberFormat.setSuffix("");

        ReportColumn col5All = new ReportColumn();
        col5All.setVirtualId(12349L);
        col5All.setIndex(8);
        col5All.setTitle("Row Number");
        col5All.setType(ColumnType.TEXT_NUMBER);
        col5All.setSystemColumnType(SystemColumnType.AUTO_NUMBER);
        col5All.setHidden(false);
        col5All.setVersion(0);
        col5All.setWidth(100);
        col5All.setValidation(false);
        col5All.setAutoNumberFormat(autoNumberFormat);

        EXPECTED_ALL_COLUMNS = List.of(col1All, col2All, col3All, col4All, col5All);

        ReportColumn col1Req = new ReportColumn();
        col1Req.setVirtualId(12345L);
        col1Req.setIndex(4);
        col1Req.setTitle("Item selected");
        col1Req.setType(ColumnType.CHECKBOX);
        col1Req.setVersion(0);

        ReportColumn col2Req = new ReportColumn();
        col2Req.setVirtualId(12346L);
        col2Req.setIndex(5);
        col2Req.setTitle("Sheet name");
        col2Req.setType(ColumnType.TEXT_NUMBER);
        col2Req.setSheetNameColumn(true);
        col2Req.setVersion(0);

        ReportColumn col3Req = new ReportColumn();
        col3Req.setVirtualId(12347L);
        col3Req.setIndex(6);
        col3Req.setTitle("Created By");
        col3Req.setType(ColumnType.CONTACT_LIST);
        col3Req.setSystemColumnType(SystemColumnType.CREATED_BY);
        col3Req.setVersion(0);

        ReportColumn col4Req = new ReportColumn();
        col4Req.setVirtualId(12348L);
        col4Req.setIndex(7);
        col4Req.setTitle("Primary");
        col4Req.setType(ColumnType.TEXT_NUMBER);
        col4Req.setPrimary(true);
        col4Req.setVersion(0);

        AutoNumberFormat autoNumberFormatReq = new AutoNumberFormat();
        autoNumberFormatReq.setFill("000");
        autoNumberFormatReq.setPrefix("TASK-");
        autoNumberFormatReq.setStartingNumber(1L);
        autoNumberFormatReq.setSuffix("");

        ReportColumn col5Req = new ReportColumn();
        col5Req.setVirtualId(12349L);
        col5Req.setIndex(8);
        col5Req.setTitle("Row Number");
        col5Req.setType(ColumnType.TEXT_NUMBER);
        col5Req.setSystemColumnType(SystemColumnType.AUTO_NUMBER);
        col5Req.setVersion(0);
        col5Req.setAutoNumberFormat(autoNumberFormatReq);

        EXPECTED_REQUIRED_COLUMNS = List.of(col1Req, col2Req, col3Req, col4Req, col5Req);
    }

    private static final Map<String, Object> EXPECTED_COLUMN1_REQUEST = Map.of(
            "title", "Item selected",
            "type", "CHECKBOX",
            "index", 4,
            "sheetNameColumn", false
    );

    private static final Map<String, Object> EXPECTED_COLUMN2_REQUEST = Map.of(
            "title", "Sheet name",
            "type", "TEXT_NUMBER",
            "index", 5,
            "sheetNameColumn", true
    );

    @BeforeEach
    void setUp() {
        ReportColumn column1 = new ReportColumn.AddReportColumnBuilder()
                .setTitle("Item selected")
                .setType(ColumnType.CHECKBOX)
                .setIndex(4)
                .setSheetNameColumn(false)
                .build();

        ReportColumn column2 = new ReportColumn.AddReportColumnBuilder()
                .setTitle("Sheet name")
                .setType(ColumnType.TEXT_NUMBER)
                .setIndex(5)
                .setSheetNameColumn(true)
                .build();

        testColumns = new ArrayList<>();
        testColumns.add(column1);
        testColumns.add(column2);
    }

    @Test
    void testAddReportColumnsGeneratedUrlIsCorrect() throws SmartsheetException {
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
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
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

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> expectedRequestBody = List.of(EXPECTED_COLUMN1_REQUEST, EXPECTED_COLUMN2_REQUEST);
        String expectedJson = objectMapper.writeValueAsString(expectedRequestBody);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        assertThat(addedColumns).usingRecursiveComparison().isEqualTo(EXPECTED_ALL_COLUMNS);
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

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> expectedRequestBody = List.of(EXPECTED_COLUMN1_REQUEST, EXPECTED_COLUMN2_REQUEST);
        String expectedJson = objectMapper.writeValueAsString(expectedRequestBody);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        assertThat(addedColumns).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_COLUMNS);
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
        assertThat(exception.getMessage()).isEqualTo("reportColumns should not be empty.");
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
