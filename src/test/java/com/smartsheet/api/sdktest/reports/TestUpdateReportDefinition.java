package com.smartsheet.api.sdktest.reports;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.internal.json.JSONSerializerException;
import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import com.smartsheet.api.internal.json.JsonSerializer;
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

public class TestUpdateReportDefinition {

    private ReportDefinition testReportDefinition;
    private String testReportDefinitionJson;

    @BeforeEach
    void setUp() throws JSONSerializerException {
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

        testReportDefinitionJson = "{\"filters\":"
                +"{\"operator\":\"AND\","
                +"\"criteria\":[{\"column\":"
                +"{\"title\":\"Primary\",\"type\":\"TEXT_NUMBER\",\"primary\":true},"
                +"\"operator\":\"EQUAL\"}]}}";
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
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.PATCH);

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

        Assertions.assertDoesNotThrow(() -> {
            smartsheet.reportResources().updateReportDefinition(TEST_REPORT_ID, testReportDefinition);
        });
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
