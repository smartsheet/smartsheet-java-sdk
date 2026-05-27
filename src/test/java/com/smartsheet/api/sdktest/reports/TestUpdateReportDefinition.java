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
import com.smartsheet.api.models.ReportColumnIdentifier;
import com.smartsheet.api.models.ReportDefinition;
import com.smartsheet.api.models.ReportFilterCriterion;
import com.smartsheet.api.models.ReportFilterExpression;
import com.smartsheet.api.models.ReportFilterObjectValue;
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
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class TestUpdateReportDefinition {

    private ReportDefinition testReportDefinition;
    private String testReportDefinitionJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        testReportDefinition = new ReportDefinition();

        testReportDefinition.setFilters(
            new ReportFilterExpression()
                .setOperator(ReportFilterExpressionOperator.AND)
                .setCriteria(createFilterCriteria())
        );

        testReportDefinition.setGroupingCriteria(createGroupingCriteria());

        testReportDefinition.setSummarizingCriteria(createSummarizingCriteria());

        testReportDefinition.setSortingCriteria(createSortingCriteria());

        testReportDefinitionJson = new ObjectMapper().writeValueAsString(testReportDefinition);
    }

    private ArrayList<ReportFilterCriterion> createFilterCriteria() {
        ArrayList<ReportFilterCriterion> criteria = new ArrayList<>();

        criteria.add(
            new ReportFilterCriterion()
                    .setOperator(ReportFilterOperator.EQUAL)
                    .setColumn(
                            new ReportColumnIdentifier()
                                    .setPrimary(true)
                                    .setTitle("Primary")
                                    .setType(ColumnType.TEXT_NUMBER)
                    )
                    .setValues(Arrays.asList(
                            ReportFilterObjectValue.string("Active"),
                            ReportFilterObjectValue.string("In Progress")
                    ))
        );

        criteria.add(
            new ReportFilterCriterion()
                    .setOperator(ReportFilterOperator.GREATER_THAN)
                    .setColumn(
                            new ReportColumnIdentifier()
                                    .setTitle("Priority")
                                    .setType(ColumnType.TEXT_NUMBER)
                    )
                    .setValues(Arrays.asList(
                            ReportFilterObjectValue.number(5)
                    ))
        );

        criteria.add(
            new ReportFilterCriterion()
                    .setOperator(ReportFilterOperator.EQUAL)
                    .setColumn(
                            new ReportColumnIdentifier()
                                    .setTitle("Owner")
                                    .setType(ColumnType.CONTACT_LIST)
                    )
                    .setValues(Arrays.asList(
                            ReportFilterObjectValue.currentUser()
                    ))
        );

        criteria.add(
            new ReportFilterCriterion()
                    .setOperator(ReportFilterOperator.GREATER_THAN_OR_EQUAL)
                    .setColumn(
                            new ReportColumnIdentifier()
                                    .setTitle("Due Date")
                                    .setType(ColumnType.DATE)
                    )
                    .setValues(Arrays.asList(
                            ReportFilterObjectValue.date("2024-01-01")
                    ))
        );

        return criteria;
    }

    private ArrayList<ReportGroupingCriterion> createGroupingCriteria() {
        ArrayList<ReportGroupingCriterion> criteria = new ArrayList<>();
        criteria.add(
            new ReportGroupingCriterion()
                    .setColumn(
                            new ReportColumnIdentifier()
                                    .setTitle("Status")
                                    .setType(ColumnType.PICKLIST)
                    )
                    .setSortingDirection(SortDirection.ASCENDING)
                    .setIsExpanded(true)
        );
        return criteria;
    }

    private ArrayList<ReportSummarizingCriterion> createSummarizingCriteria() {
        ArrayList<ReportSummarizingCriterion> criteria = new ArrayList<>();
        criteria.add(
            new ReportSummarizingCriterion()
                    .setColumn(
                            new ReportColumnIdentifier()
                                    .setTitle("Amount")
                                    .setType(ColumnType.TEXT_NUMBER)
                    )
                    .setAggregationType(ReportAggregationType.SUM)
        );
        return criteria;
    }

    private ArrayList<ReportSortingCriterion> createSortingCriteria() {
        ArrayList<ReportSortingCriterion> criteria = new ArrayList<>();
        criteria.add(
            new ReportSortingCriterion()
                    .setColumn(
                            new ReportColumnIdentifier()
                                    .setTitle("Date")
                                    .setType(ColumnType.DATE)
                    )
                    .setSortingDirection(SortDirection.DESCENDING)
        );
        return criteria;
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
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testUpdateReportDefinitionAllResponseBodyProperties() throws JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/update-report-definition/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        Assertions.assertDoesNotThrow(() -> smartsheet.reportResources().updateReportDefinition(TEST_REPORT_ID, testReportDefinition));

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        assertThat(objectMapper.readTree(requestBody)).isEqualTo(objectMapper.readTree(testReportDefinitionJson));
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

}
