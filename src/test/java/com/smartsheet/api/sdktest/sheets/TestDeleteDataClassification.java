/*
 * Copyright (C) 2026 Smartsheet
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

package com.smartsheet.api.sdktest.sheets;

import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static com.smartsheet.api.sdktest.sheets.CommonTestConstants.TEST_SHEET_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestDeleteDataClassification {

    @Test
    void testDeleteDataClassificationGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sheets/delete-data-classification/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.sheetResources().deleteDataClassification(TEST_SHEET_ID);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/sheets/" + TEST_SHEET_ID + "/dataclassification");
    }

    @Test
    void testDeleteDataClassificationAllResponseBodyProperties() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sheets/delete-data-classification/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertDoesNotThrow(() ->
                smartsheet.sheetResources().deleteDataClassification(TEST_SHEET_ID)
        );
    }

    @Test
    void testDeleteDataClassificationError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sheetResources().deleteDataClassification(TEST_SHEET_ID)
        );

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testDeleteDataClassificationError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sheetResources().deleteDataClassification(TEST_SHEET_ID)
        );

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
