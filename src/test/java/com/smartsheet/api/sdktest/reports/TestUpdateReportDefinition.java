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
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.ReportFilterExpressionOperator;
import com.smartsheet.api.models.enums.ReportFilterOperator;
import com.smartsheet.api.sdktest.users.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
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

        testReportDefinitionJson = "{\"filters\":" +
                "{\"operator\":\"AND\"," +
                "\"criteria\":[{\"column\":" +
                "{\"title\":\"Primary\",\"type\":\"TEXT_NUMBER\",\"primary\":true}," +
                "\"operator\":\"EQUAL\"}]}}";
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
}
