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

package com.smartsheet.api.integrationtest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.CellHistory;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.resilience4j.RetryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CellResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testGetCellHistory() throws SmartsheetException, IOException {
        //create sheet
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());

        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

        //get columns
        PagedResult<Column> columns = RetryUtil.callWithRetry(
                () -> smartsheet.sheetResources().columnResources().listColumns(sheet.getId(), null, parameters));
        //add rows
        Row row = addRows(sheet.getId());

        PagedResult<CellHistory> cellHistory = smartsheet
                .sheetResources()
                .rowResources()
                .cellResources()
                .getCellHistory(sheet.getId(), row.getId(), columns.getData().get(0).getId(), parameters);
        assertThat(cellHistory).isNotNull();

        //cleanup
        deleteSheet(sheet.getId());
    }

    @Test
    void testAddCellImage() throws SmartsheetException, IOException {
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

        //get columns
        PagedResult<Column> columns = RetryUtil.callWithRetry(
                () -> smartsheet.sheetResources().columnResources().listColumns(sheet.getId(), null, parameters));
        //add rows
        Row row = addRows(sheet.getId());

        smartsheet.sheetResources().rowResources().cellResources().addImageToCell(sheet.getId(), row.getId(),
                columns.getData().get(0).getId(), "src/test/resources/exclam.png", "image/png", false, "alt text");

        File file = new File("src/test/resources/exclam.png");
        smartsheet.sheetResources().rowResources().cellResources().addImageToCell(sheet.getId(), row.getId(),
                columns.getData().get(0).getId(), file, "image/png", false, file.getName());

        InputStream is = new FileInputStream(file);
        smartsheet.sheetResources().rowResources().cellResources().addImageToCell(sheet.getId(), row.getId(),
                columns.getData().get(0).getId(), is, "image/png", file.length(), true, file.getName());

        //cleanup
        deleteSheet(sheet.getId());
    }
}
