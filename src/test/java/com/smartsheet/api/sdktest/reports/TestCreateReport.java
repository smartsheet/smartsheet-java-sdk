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
import com.smartsheet.api.models.CreateReportRequest;
import com.smartsheet.api.models.CreateReportResult;
import com.smartsheet.api.models.ReportColumn;
import com.smartsheet.api.models.ReportDestination;
import com.smartsheet.api.models.ReportScopeInclusion;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.ReportAssetType;
import com.smartsheet.api.models.enums.ReportDestinationType;
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

import static org.assertj.core.api.Assertions.assertThat;

public class TestCreateReport {

    private CreateReportRequest testRequest;

    private static final Map<String, Object> EXPECTED_REQUEST_BODY = Map.of(
            "name", "Q2 Earnings Report",
            "destination", Map.of(
                    "destinationId", 12345L,
                    "destinationType", "folder"
            ),
            "columns", List.of(
                    Map.of(
                            "title", "Task Name",
                            "type", "TEXT_NUMBER",
                            "primary", true,
                            "index", 0,
                            "sheetNameColumn", false
                    )
            ),
            "scope", List.of(
                    Map.of(
                            "assetType", "sheet",
                            "assetId", 67890L
                    )
            ),
            "isSummaryReport", false
    );

    private static final CreateReportResult EXPECTED_ALL_PROPERTIES_RESULT;

    static {
        ReportColumn col1 = new ReportColumn();
        col1.setVirtualId(1234567890123456L);
        col1.setIndex(0);
        col1.setTitle("Primary column");
        col1.setType(ColumnType.TEXT_NUMBER);
        col1.setPrimary(true);
        col1.setHidden(false);
        col1.setVersion(0);
        col1.setWidth(200);
        col1.setValidation(false);

        ReportColumn col2 = new ReportColumn();
        col2.setVirtualId(2345678901234567L);
        col2.setIndex(1);
        col2.setTitle("Sheet name");
        col2.setType(ColumnType.TEXT_NUMBER);
        col2.setSheetNameColumn(true);
        col2.setHidden(false);
        col2.setVersion(0);
        col2.setWidth(150);
        col2.setValidation(false);

        ReportColumn col3 = new ReportColumn();
        col3.setVirtualId(3456789012345678L);
        col3.setIndex(2);
        col3.setTitle("Created at");
        col3.setType(ColumnType.DATETIME);
        col3.setSystemColumnType(SystemColumnType.CREATED_DATE);
        col3.setHidden(false);
        col3.setVersion(0);
        col3.setWidth(150);
        col3.setValidation(false);

        ReportColumn col4 = new ReportColumn();
        col4.setVirtualId(4567890123456789L);
        col4.setIndex(3);
        col4.setTitle("Selected item");
        col4.setType(ColumnType.PICKLIST);
        col4.setHidden(false);
        col4.setVersion(0);
        col4.setWidth(150);
        col4.setValidation(false);

        EXPECTED_ALL_PROPERTIES_RESULT = new CreateReportResult()
                .setId(987654321L)
                .setName("Q2 Earnings Report")
                .setAccessLevel(AccessLevel.OWNER)
                .setPermalink("https://app.smartsheet.com/reports/c8gJxw87cXpRCvCC5PPw6jFhFRrf5r8PxCrxvW21")
                .setIsSummaryReport(false)
                .setColumns(List.of(col1, col2, col3, col4));
    }

    private static final CreateReportResult EXPECTED_REQUIRED_PROPERTIES_RESULT = new CreateReportResult()
            .setId(987654321L)
            .setName("Q2 Earnings Report")
            .setAccessLevel(AccessLevel.OWNER)
            .setPermalink("https://app.smartsheet.com/reports/c8gJxw87cXpRCvCC5PPw6jFhFRrf5r8PxCrxvW21");

    @BeforeEach
    void setUp() {
        ReportDestination destination = new ReportDestination();
        destination.setDestinationId(12345L);
        destination.setDestinationType(ReportDestinationType.FOLDER);

        List<ReportColumn> columns = new ArrayList<>();
        ReportColumn column = new ReportColumn();
        column.setTitle("Task Name");
        column.setType(ColumnType.TEXT_NUMBER);
        column.setPrimary(true);
        column.setIndex(0);
        column.setSheetNameColumn(false);
        columns.add(column);

        List<ReportScopeInclusion> scope = new ArrayList<>();
        ReportScopeInclusion scopeItem = new ReportScopeInclusion();
        scopeItem.setAssetType(ReportAssetType.SHEET);
        scopeItem.setAssetId(67890L);
        scope.add(scopeItem);

        testRequest = new CreateReportRequest();
        testRequest.setName("Q2 Earnings Report");
        testRequest.setDestination(destination);
        testRequest.setColumns(columns);
        testRequest.setScope(scope);
        testRequest.setIsSummaryReport(false);
    }

    @Test
    void testCreateReportGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/create-report/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().createReport(testRequest);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.POST);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testCreateReportAllResponseBodyProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/create-report/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        CreateReportResult result = smartsheet.reportResources().createReport(testRequest);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(EXPECTED_REQUEST_BODY);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_ALL_PROPERTIES_RESULT);
    }

    @Test
    void testCreateReportRequiredResponseBodyProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/create-report/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        CreateReportResult result = smartsheet.reportResources().createReport(testRequest);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(EXPECTED_REQUEST_BODY);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_PROPERTIES_RESULT);
    }

    @Test
    void testCreateReportInvalidArgument() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/create-report/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.reportResources().createReport(null);
        });
    }

    @Test
    void testCreateReportError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/400-response",
                requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().createReport(testRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }

    @Test
    void testCreateReportError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/500-response",
                requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().createReport(testRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }
}
