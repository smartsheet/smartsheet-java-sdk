package com.smartsheet.api.sdktest.users;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;

import java.util.Map;

public class Utils {
    public static WiremockClientWrapper createWiremockSmartsheetClient(String testName, String requestId) {
        Map<String, String> headers = Map.of(
                "x-test-name", testName,
                "x-request-id", requestId
        );
        WiremockClient wiremockClient = new WiremockClient(headers);
        Smartsheet smartsheet = wiremockClient.getSmartsheetClient("test_token_123");
        return new WiremockClientWrapper(smartsheet, wiremockClient);
    }
}
