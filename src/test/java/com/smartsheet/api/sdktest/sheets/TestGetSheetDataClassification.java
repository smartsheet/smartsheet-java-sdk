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

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.smartsheet.api.sdktest.sheets.CommonTestConstants.TEST_SHEET_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestGetSheetDataClassification {

    @Test
    void testGetSheetIncludesDataClassification() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sheets/get-sheet/data-classification",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Sheet sheet = smartsheet.sheetResources().getSheet(TEST_SHEET_ID);

        assertThat(sheet.getDataClassification()).isEqualTo("Confidential");
    }
}
