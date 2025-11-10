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
