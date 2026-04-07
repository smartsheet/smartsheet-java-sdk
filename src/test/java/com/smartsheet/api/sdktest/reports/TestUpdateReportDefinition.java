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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.ReportColumnIdentifier;
import com.smartsheet.api.models.ReportDefinition;
import com.smartsheet.api.models.ReportFilterCriterion;
import com.smartsheet.api.models.ReportFilterExpression;
import com.smartsheet.api.models.ReportGroupingCriterion;
import com.smartsheet.api.models.ReportSortingCriterion;
import com.smartsheet.api.models.ReportSummarizingCriterion;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.ReportAggregationType;
import com.smartsheet.api.models.enums.ReportFilterExpressionOperator;
import com.smartsheet.api.models.enums.ReportFilterOperator;
import com.smartsheet.api.models.enums.SortDirection;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

public class TestUpdateReportDefinition {

    private ReportDefinition testReportDefinition;
    private String testReportDefinitionJson;

    @BeforeEach
    void setUp() {
        testReportDefinition = new ReportDefinition();

        // Set filters
        testReportDefinition.setFilters(
            new ReportFilterExpression()
                .setOperator(ReportFilterExpressionOperator.AND)
                .setCriteria(new ArrayList<>() {{
                        add(
                            new ReportFilterCriterion()
                                    .setOperator(ReportFilterOperator.EQUAL)
                                    .setColumn(
                                            new ReportColumnIdentifier()
                                                    .setPrimary(true)
                                                    .setTitle("Primary")
                                                    .setType(ColumnType.TEXT_NUMBER)
                                    )
                        );
                    }})
        );

        // Set grouping criteria
        testReportDefinition.setGroupingCriteria(new ArrayList<>() {{
            add(
                new ReportGroupingCriterion()
                        .setColumn(
                                new ReportColumnIdentifier()
                                        .setTitle("Status")
                                        .setType(ColumnType.PICKLIST)
                        )
                        .setSortingDirection(SortDirection.ASCENDING)
                        .setIsExpanded(true)
            );
        }});

        // Set summarizing criteria
        testReportDefinition.setSummarizingCriteria(new ArrayList<>() {{
            add(
                new ReportSummarizingCriterion()
                        .setColumn(
                                new ReportColumnIdentifier()
                                        .setTitle("Amount")
                                        .setType(ColumnType.TEXT_NUMBER)
                        )
                        .setAggregationType(ReportAggregationType.SUM)
            );
        }});

        // Set sorting criteria
        testReportDefinition.setSortingCriteria(new ArrayList<>() {{
            add(
                new ReportSortingCriterion()
                        .setColumn(
                                new ReportColumnIdentifier()
                                        .setTitle("Date")
                                        .setType(ColumnType.DATE)
                        )
                        .setSortingDirection(SortDirection.DESCENDING)
            );
        }});

        testReportDefinitionJson = "{\"filters\":" +
                "{\"operator\":\"AND\"," +
                "\"criteria\":[{\"column\":" +
                "{\"title\":\"Primary\",\"type\":\"TEXT_NUMBER\",\"primary\":true}," +
                "\"operator\":\"EQUAL\"}]}," +
                "\"groupingCriteria\":[{\"column\":" +
                "{\"title\":\"Status\",\"type\":\"PICKLIST\"}," +
                "\"sortingDirection\":\"ASCENDING\",\"isExpanded\":true}]," +
                "\"summarizingCriteria\":[{\"column\":" +
                "{\"title\":\"Amount\",\"type\":\"TEXT_NUMBER\"}," +
                "\"aggregationType\":\"SUM\"}]," +
                "\"sortingCriteria\":[{\"column\":" +
                "{\"title\":\"Date\",\"type\":\"DATE\"}," +
                "\"sortingDirection\":\"DESCENDING\"}]}";
    }

    @Test
    void testUpdateReportDefinitionGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-definition/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().updateReportDefinition(TEST_REPORT_ID, testReportDefinition);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/definition");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.PUT);

        String requestBody = wiremockRequest.getBodyAsString();
        assertThat(requestBody).isEqualTo(testReportDefinitionJson);
    }

    @Test
    void testUpdateReportDefinitionAllResponseBodyProperties() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-definition/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        assertThatNoException().isThrownBy(() -> smartsheet.reportResources().updateReportDefinition(TEST_REPORT_ID, testReportDefinition));
    }

    @Test
    void testUpdateReportDefinitionInvalidArgument() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-definition/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.reportResources().updateReportDefinition(TEST_REPORT_ID, null);
        });
    }

    @Test
    void testUpdateReportDefinitionError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().updateReportDefinition(TEST_REPORT_ID, testReportDefinition);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testUpdateReportDefinitionError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().updateReportDefinition(TEST_REPORT_ID, testReportDefinition);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }

    @Test
    void testReportDefinitionSerializesToExpectedMap() throws JsonProcessingException {
        // Marshal the ReportDefinition to JSON using the same ObjectMapper configuration as the SDK
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = objectMapper.writeValueAsString(testReportDefinition);
        Map<String, Object> actualMap = objectMapper.readValue(json, Map.class);

        // Expected structure - this will catch if fields are added or removed

        // Expected filters
        Map<String, Object> expectedFilterColumn = Map.of(
                "title", "Primary",
                "type", "TEXT_NUMBER",
                "primary", true
        );
        Map<String, Object> expectedFilterCriterion = Map.of(
                "column", expectedFilterColumn,
                "operator", "EQUAL"
        );
        Map<String, Object> expectedFilters = Map.of(
                "operator", "AND",
                "criteria", List.of(expectedFilterCriterion)
        );

        // Expected grouping criteria
        Map<String, Object> expectedGroupingColumn = Map.of(
                "title", "Status",
                "type", "PICKLIST"
        );
        Map<String, Object> expectedGroupingCriterion = Map.of(
                "column", expectedGroupingColumn,
                "sortingDirection", "ASCENDING",
                "isExpanded", true
        );

        // Expected summarizing criteria
        Map<String, Object> expectedSummarizingColumn = Map.of(
                "title", "Amount",
                "type", "TEXT_NUMBER"
        );
        Map<String, Object> expectedSummarizingCriterion = Map.of(
                "column", expectedSummarizingColumn,
                "aggregationType", "SUM"
        );

        // Expected sorting criteria
        Map<String, Object> expectedSortingColumn = Map.of(
                "title", "Date",
                "type", "DATE"
        );
        Map<String, Object> expectedSortingCriterion = Map.of(
                "column", expectedSortingColumn,
                "sortingDirection", "DESCENDING"
        );

        Map<String, Object> expectedBody = Map.of(
                "filters", expectedFilters,
                "groupingCriteria", List.of(expectedGroupingCriterion),
                "summarizingCriteria", List.of(expectedSummarizingCriterion),
                "sortingCriteria", List.of(expectedSortingCriterion)
        );

        assertThat(actualMap).isEqualTo(expectedBody);
    }
}
