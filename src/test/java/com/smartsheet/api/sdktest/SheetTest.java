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

package com.smartsheet.api.sdktest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.SourceInclusion;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SheetTest {

    @Test
    void listSheets_NoParams() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Sheets - No Params");
        PagedResult<Sheet> sheets = ss.sheetResources().listSheets();
        assertThat(sheets.getData().get(0).getName()).isEqualTo("Copy of Sample Sheet");
    }

    @Test
    void listSheets_IncludeOwnerInfo() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Sheets - Include Owner Info");
        PagedResult<Sheet> sheets = ss.sheetResources().listSheets(EnumSet.of(SourceInclusion.OWNERINFO), null, null);
        assertThat(sheets.getData().get(0).getOwner()).isEqualTo("john.doe@smartsheet.com");
    }

    @Test
    void createSheet__Invalid_NoColumns() {
        Smartsheet ss = HelperFunctions.SetupClient("Create Sheet - Invalid - No Columns");

        Sheet sheetA = new Sheet().setSheetName("New Sheet").setColumns(new ArrayList<>());
        assertThatThrownBy(() -> ss.sheetResources().createSheet(sheetA))
                .isInstanceOf(SmartsheetException.class)
                .hasMessage("The new sheet requires either a fromId or columns.");
    }
}
