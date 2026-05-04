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
import com.smartsheet.api.models.DataClassification;
import com.smartsheet.api.models.enums.DataClassificationType;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static com.smartsheet.api.sdktest.sheets.CommonTestConstants.TEST_SHEET_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestSetDataClassification {

    @Test
    void testSetDataClassificationGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sheets/set-data-classification/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        DataClassification dataClassification = new DataClassification()
                .setDataClassification(DataClassificationType.CONFIDENTIAL);

        smartsheet.sheetResources().setDataClassification(TEST_SHEET_ID, dataClassification);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/sheets/" + TEST_SHEET_ID + "/dataclassification");
    }

    @Test
    void testSetDataClassificationAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sheets/set-data-classification/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        DataClassification dataClassification = new DataClassification()
                .setDataClassification(DataClassificationType.CONFIDENTIAL);

        Assertions.assertDoesNotThrow(() ->
                smartsheet.sheetResources().setDataClassification(TEST_SHEET_ID, dataClassification)
        );

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        assertThat(requestBody).isEqualTo("{\"dataClassification\":\"CONFIDENTIAL\"}");
    }

    @Test
    void testSetDataClassificationError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        DataClassification dataClassification = new DataClassification()
                .setDataClassification(DataClassificationType.CONFIDENTIAL);

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sheetResources().setDataClassification(TEST_SHEET_ID, dataClassification)
        );

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testSetDataClassificationError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        DataClassification dataClassification = new DataClassification()
                .setDataClassification(DataClassificationType.CONFIDENTIAL);

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sheetResources().setDataClassification(TEST_SHEET_ID, dataClassification)
        );

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }
}
