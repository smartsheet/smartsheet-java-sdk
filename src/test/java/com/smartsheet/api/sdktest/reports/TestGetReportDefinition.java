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
import com.smartsheet.api.models.enums.SystemColumnType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestGetReportDefinition {

    private static final ReportDefinition EXPECTED_ALL_PROPERTIES;
    private static final ReportDefinition EXPECTED_REQUIRED_PROPERTIES;

    static {
        List<ReportFilterCriterion> criteria = new ArrayList<>();

        criteria.add(new ReportFilterCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Primary Column")
                        .setType(ColumnType.TEXT_NUMBER)
                        .setPrimary(true))
                .setOperator(ReportFilterOperator.EQUAL)
                .setValues(new ArrayList<>(List.of(ReportFilterObjectValue.string("Test Value")))));

        criteria.add(new ReportFilterCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Status")
                        .setType(ColumnType.PICKLIST))
                .setOperator(ReportFilterOperator.NOT_EQUAL)
                .setValues(new ArrayList<>(List.of(ReportFilterObjectValue.string("Complete")))));

        criteria.add(new ReportFilterCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Amount")
                        .setType(ColumnType.TEXT_NUMBER))
                .setOperator(ReportFilterOperator.GREATER_THAN)
                .setValues(new ArrayList<>(List.of(ReportFilterObjectValue.number(42)))));

        criteria.add(new ReportFilterCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setType(ColumnType.DATETIME)
                        .setSystemColumnType(SystemColumnType.MODIFIED_DATE))
                .setOperator(ReportFilterOperator.LESS_THAN)
                .setValues(new ArrayList<>(List.of(ReportFilterObjectValue.date("2025-01-14")))));

        criteria.add(new ReportFilterCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Assigned To")
                        .setType(ColumnType.CONTACT_LIST))
                .setOperator(ReportFilterOperator.EQUAL)
                .setValues(new ArrayList<>(List.of(ReportFilterObjectValue.currentUser()))));

        criteria.add(new ReportFilterCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Notes")
                        .setType(ColumnType.TEXT_NUMBER))
                .setOperator(ReportFilterOperator.EQUAL)
                .setValues(Arrays.asList((ReportFilterObjectValue) null)));

        ReportFilterExpression filters = new ReportFilterExpression()
                .setOperator(ReportFilterExpressionOperator.AND)
                .setCriteria(criteria);

        List<ReportGroupingCriterion> grouping = new ArrayList<>();
        grouping.add(new ReportGroupingCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Primary Column")
                        .setType(ColumnType.TEXT_NUMBER)
                        .setPrimary(true))
                .setSortingDirection(SortDirection.ASCENDING)
                .setIsExpanded(true));
        grouping.add(new ReportGroupingCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Category")
                        .setType(ColumnType.TEXT_NUMBER))
                .setSortingDirection(SortDirection.DESCENDING)
                .setIsExpanded(false));

        List<ReportSummarizingCriterion> summarizing = new ArrayList<>();
        summarizing.add(new ReportSummarizingCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Primary Column")
                        .setType(ColumnType.TEXT_NUMBER)
                        .setPrimary(true))
                .setAggregationType(ReportAggregationType.COUNT));
        summarizing.add(new ReportSummarizingCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Amount")
                        .setType(ColumnType.TEXT_NUMBER))
                .setAggregationType(ReportAggregationType.SUM));

        List<ReportSortingCriterion> sorting = new ArrayList<>();
        sorting.add(new ReportSortingCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setTitle("Primary Column")
                        .setType(ColumnType.TEXT_NUMBER)
                        .setPrimary(true))
                .setSortingDirection(SortDirection.ASCENDING));
        sorting.add(new ReportSortingCriterion()
                .setColumn(new ReportColumnIdentifier()
                        .setType(ColumnType.DATETIME)
                        .setSystemColumnType(SystemColumnType.MODIFIED_DATE))
                .setSortingDirection(SortDirection.DESCENDING));

        EXPECTED_ALL_PROPERTIES = new ReportDefinition()
                .setFilters(filters)
                .setGroupingCriteria(grouping)
                .setSummarizingCriteria(summarizing)
                .setSortingCriteria(sorting);

        EXPECTED_REQUIRED_PROPERTIES = new ReportDefinition();
    }

    @Test
    void testGetReportDefinitionGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-definition/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().getReportDefinition(TEST_REPORT_ID);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/definition");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testGetReportDefinitionAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-definition/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        ReportDefinition result = smartsheet.reportResources().getReportDefinition(TEST_REPORT_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_ALL_PROPERTIES);
    }

    @Test
    void testGetReportDefinitionRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-definition/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        ReportDefinition result = smartsheet.reportResources().getReportDefinition(TEST_REPORT_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_PROPERTIES);
    }

    @Test
    void testGetReportDefinitionError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().getReportDefinition(TEST_REPORT_ID);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testGetReportDefinitionError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().getReportDefinition(TEST_REPORT_ID);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
