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

    /**
     * Expected request body structure - per OpenAPI spec.
     */
    private static final Map<String, Object> EXPECTED_REQUEST_BODY = Map.of(
            "name", "Q2 Earnings Report",
            "destination", Map.of(
                    "destinationId", 12345,
                    "destinationType", "folder"  // lowercase per API spec
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
                            "assetType", "sheet",  // lowercase per API spec
                            "assetId", 67890
                    )
            ),
            "isSummaryReport", false
    );

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
    void testCreateReportGeneratedUrlIsCorrect() throws SmartsheetException, JsonProcessingException {
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

        // Validate the request body matches the expected structure as a whole
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(EXPECTED_REQUEST_BODY);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));
    }

    @Test
    void testCreateReportResponseBodyAllProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/create-report/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        CreateReportResult result = smartsheet.reportResources().createReport(testRequest);

        // Validate request body matches expected structure as a whole (per OpenAPI spec)
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(EXPECTED_REQUEST_BODY);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        // Verify response parsing - all properties
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(987654321L);
        assertThat(result.getName()).isEqualTo("Q2 Earnings Report");
        assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(result.getPermalink()).isEqualTo("https://app.smartsheet.com/reports/c8gJxw87cXpRCvCC5PPw6jFhFRrf5r8PxCrxvW21");
        assertThat(result.getIsSummaryReport()).isFalse();

        // Verify columns are returned
        assertThat(result.getColumns()).isNotNull();
        assertThat(result.getColumns()).hasSize(4);

        // Verify first column (primary column)
        ReportColumn col1 = result.getColumns().get(0);
        assertThat(col1.getVirtualId()).isEqualTo(1234567890123456L);
        assertThat(col1.getIndex()).isEqualTo(0);
        assertThat(col1.getTitle()).isEqualTo("Primary column");
        assertThat(col1.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(col1.getPrimary()).isTrue();

        // Verify second column (sheet name column)
        ReportColumn col2 = result.getColumns().get(1);
        assertThat(col2.getVirtualId()).isEqualTo(2345678901234567L);
        assertThat(col2.getIndex()).isEqualTo(1);
        assertThat(col2.getTitle()).isEqualTo("Sheet name");
        assertThat(col2.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(col2.getSheetNameColumn()).isTrue();

        // Verify third column (system column)
        ReportColumn col3 = result.getColumns().get(2);
        assertThat(col3.getVirtualId()).isEqualTo(3456789012345678L);
        assertThat(col3.getIndex()).isEqualTo(2);
        assertThat(col3.getTitle()).isEqualTo("Created at");
        assertThat(col3.getType()).isEqualTo(ColumnType.DATETIME);
        assertThat(col3.getSystemColumnType()).isEqualTo(SystemColumnType.CREATED_DATE);

        // Verify fourth column (picklist column)
        ReportColumn col4 = result.getColumns().get(3);
        assertThat(col4.getVirtualId()).isEqualTo(4567890123456789L);
        assertThat(col4.getIndex()).isEqualTo(3);
        assertThat(col4.getTitle()).isEqualTo("Selected item");
        assertThat(col4.getType()).isEqualTo(ColumnType.PICKLIST);
    }

    @Test
    void testCreateReportResponseBodyRequiredProperties() throws SmartsheetException, JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/create-report/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        CreateReportResult result = smartsheet.reportResources().createReport(testRequest);

        // Validate request body matches expected structure as a whole (per OpenAPI spec)
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(EXPECTED_REQUEST_BODY);
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(expectedJson));

        // Verify response parsing - required properties only
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(987654321L);
        assertThat(result.getName()).isEqualTo("Q2 Earnings Report");
        assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(result.getPermalink()).isEqualTo("https://app.smartsheet.com/reports/c8gJxw87cXpRCvCC5PPw6jFhFRrf5r8PxCrxvW21");
        // isSummaryReport is optional, not returned in required response
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
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().createReport(testRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }

    @Test
    void testCreateReportError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().createReport(testRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }
}
