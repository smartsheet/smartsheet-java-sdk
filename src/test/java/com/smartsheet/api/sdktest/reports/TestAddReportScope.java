package com.smartsheet.api.sdktest.reports;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.ReportScopeInclusion;
import com.smartsheet.api.models.enums.ReportAssetType;
import com.smartsheet.api.sdktest.users.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_REPORT_ID;
import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_SHEET_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestAddReportScope {

    private List<ReportScopeInclusion> testScopes;

    @BeforeEach
    void setUp() {
        ReportScopeInclusion scope = new ReportScopeInclusion();
        scope.setAssetType(ReportAssetType.SHEET);
        scope.setAssetId(TEST_SHEET_ID);

        testScopes = new ArrayList<>();
        testScopes.add(scope);
    }

    @Test
    void testAddReportScopeGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/add-report-scope/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.reportResources().addReportScope(TEST_REPORT_ID, testScopes);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/reports/" + TEST_REPORT_ID + "/scope");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.POST);
    }

    @Test
    void testAddReportScopeAllResponseBodyProperties() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/add-report-scope/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertDoesNotThrow(() -> {
            smartsheet.reportResources().addReportScope(TEST_REPORT_ID, testScopes);
        });
    }

    @Test
    void testAddReportScopeInvalidArgument() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/reports/add-report-scope/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.reportResources().addReportScope(TEST_REPORT_ID, null);
        });

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.reportResources().addReportScope(TEST_REPORT_ID, new ArrayList<>());
        });
        assertThat(exception.getMessage()).isEqualTo("scopes should not be empty.");
    }

    @Test
    void testAddReportScopeError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().addReportScope(TEST_REPORT_ID, testScopes);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testAddReportScopeError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.reportResources().addReportScope(TEST_REPORT_ID, testScopes);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
