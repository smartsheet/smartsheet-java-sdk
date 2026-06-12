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
import com.smartsheet.api.models.ReportDefinition;
import com.smartsheet.api.models.StringObjectValue;
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
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.reports.CommonTestConstants.TEST_REPORT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestGetReportDefinition {

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

        // filters
        assertThat(result.getFilters()).isNotNull();
        assertThat(result.getFilters().getOperator()).isEqualTo(ReportFilterExpressionOperator.AND);
        assertThat(result.getFilters().getCriteria()).isNotNull();
        assertThat(result.getFilters().getCriteria().size()).isEqualTo(2);

        // filters.criteria[0]
        assertThat(result.getFilters().getCriteria().get(0).getColumn()).isNotNull();
        assertThat(result.getFilters().getCriteria().get(0).getColumn().getTitle()).isEqualTo("Primary Column");
        assertThat(result.getFilters().getCriteria().get(0).getColumn().getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getFilters().getCriteria().get(0).getColumn().getPrimary()).isTrue();
        assertThat(result.getFilters().getCriteria().get(0).getOperator()).isEqualTo(ReportFilterOperator.EQUAL);
        assertThat(result.getFilters().getCriteria().get(0).getValues()).isNotNull();
        assertThat(result.getFilters().getCriteria().get(0).getValues().size()).isEqualTo(1);
        assertThat(((StringObjectValue) result.getFilters().getCriteria().get(0).getValues().get(0)).getValue())
                .isEqualTo("Test Value");

        // filters.criteria[1]
        assertThat(result.getFilters().getCriteria().get(1).getColumn()).isNotNull();
        assertThat(result.getFilters().getCriteria().get(1).getColumn().getTitle()).isEqualTo("Status");
        assertThat(result.getFilters().getCriteria().get(1).getColumn().getType()).isEqualTo(ColumnType.PICKLIST);
        assertThat(result.getFilters().getCriteria().get(1).getOperator()).isEqualTo(ReportFilterOperator.NOT_EQUAL);
        assertThat(result.getFilters().getCriteria().get(1).getValues()).isNotNull();
        assertThat(result.getFilters().getCriteria().get(1).getValues().size()).isEqualTo(1);
        assertThat(((StringObjectValue) result.getFilters().getCriteria().get(1).getValues().get(0)).getValue())
                .isEqualTo("Complete");

        // groupingCriteria
        assertThat(result.getGroupingCriteria()).isNotNull();
        assertThat(result.getGroupingCriteria().size()).isEqualTo(2);

        // groupingCriteria[0]
        assertThat(result.getGroupingCriteria().get(0).getColumn()).isNotNull();
        assertThat(result.getGroupingCriteria().get(0).getColumn().getTitle()).isEqualTo("Primary Column");
        assertThat(result.getGroupingCriteria().get(0).getColumn().getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getGroupingCriteria().get(0).getColumn().getPrimary()).isTrue();
        assertThat(result.getGroupingCriteria().get(0).getSortingDirection()).isEqualTo(SortDirection.ASCENDING);
        assertThat(result.getGroupingCriteria().get(0).getIsExpanded()).isTrue();

        // groupingCriteria[1]
        assertThat(result.getGroupingCriteria().get(1).getColumn()).isNotNull();
        assertThat(result.getGroupingCriteria().get(1).getColumn().getTitle()).isEqualTo("Category");
        assertThat(result.getGroupingCriteria().get(1).getColumn().getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getGroupingCriteria().get(1).getSortingDirection()).isEqualTo(SortDirection.DESCENDING);
        assertThat(result.getGroupingCriteria().get(1).getIsExpanded()).isFalse();

        // summarizingCriteria
        assertThat(result.getSummarizingCriteria()).isNotNull();
        assertThat(result.getSummarizingCriteria().size()).isEqualTo(2);

        // summarizingCriteria[0]
        assertThat(result.getSummarizingCriteria().get(0).getColumn()).isNotNull();
        assertThat(result.getSummarizingCriteria().get(0).getColumn().getTitle()).isEqualTo("Primary Column");
        assertThat(result.getSummarizingCriteria().get(0).getColumn().getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getSummarizingCriteria().get(0).getColumn().getPrimary()).isTrue();
        assertThat(result.getSummarizingCriteria().get(0).getAggregationType()).isEqualTo(ReportAggregationType.COUNT);

        // summarizingCriteria[1]
        assertThat(result.getSummarizingCriteria().get(1).getColumn()).isNotNull();
        assertThat(result.getSummarizingCriteria().get(1).getColumn().getTitle()).isEqualTo("Amount");
        assertThat(result.getSummarizingCriteria().get(1).getColumn().getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getSummarizingCriteria().get(1).getAggregationType()).isEqualTo(ReportAggregationType.SUM);

        // sortingCriteria
        assertThat(result.getSortingCriteria()).isNotNull();
        assertThat(result.getSortingCriteria().size()).isEqualTo(2);

        // sortingCriteria[0]
        assertThat(result.getSortingCriteria().get(0).getColumn()).isNotNull();
        assertThat(result.getSortingCriteria().get(0).getColumn().getTitle()).isEqualTo("Primary Column");
        assertThat(result.getSortingCriteria().get(0).getColumn().getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getSortingCriteria().get(0).getColumn().getPrimary()).isTrue();
        assertThat(result.getSortingCriteria().get(0).getSortingDirection()).isEqualTo(SortDirection.ASCENDING);

        // sortingCriteria[1]
        assertThat(result.getSortingCriteria().get(1).getColumn()).isNotNull();
        assertThat(result.getSortingCriteria().get(1).getColumn().getType()).isEqualTo(ColumnType.DATETIME);
        assertThat(result.getSortingCriteria().get(1).getColumn().getSystemColumnType())
                .isEqualTo(SystemColumnType.MODIFIED_DATE);
        assertThat(result.getSortingCriteria().get(1).getSortingDirection()).isEqualTo(SortDirection.DESCENDING);
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

        assertThat(result).isNotNull();
        assertThat(result.getFilters()).isNull();
        assertThat(result.getGroupingCriteria()).isNull();
        assertThat(result.getSummarizingCriteria()).isNull();
        assertThat(result.getSortingCriteria()).isNull();
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
    void testGetReportDefinitionNoQueryParams() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/get-report-definition/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().getReportDefinition(TEST_REPORT_ID);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);

        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
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
