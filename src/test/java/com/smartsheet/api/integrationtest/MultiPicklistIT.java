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
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.MultiPicklistObjectValue;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.StringObjectValue;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.SheetInclusion;
import com.smartsheet.api.resilience4j.RetryUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiPicklistIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    Sheet sheet;
    List<Column> addCols;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();

        // create sheet object
        Sheet sheetHome = createSheetObject();

        //create sheet
        sheet = smartsheet.sheetResources().createSheet(sheetHome);
        assertThat(sheet.getColumns()).hasSameSizeAs(sheetHome.getColumns());
    }

    @AfterEach
    public void Teardown() throws Exception {
        smartsheet.sheetResources().deleteSheet(sheet.getId());
    }

    @Test
    void testMultiPicklistMethods() throws SmartsheetException, IOException {
        testAddMultiPicklistColumn();
        testListMultiPicklistColumn();
        testAddMultiPicklistRow();
        testGetMultiPicklistSheet();
    }

    public void testAddMultiPicklistColumn() throws SmartsheetException {
        Column mpl = new Column();
        mpl.setTitle("This is a multi-picklist column").setIndex(0).setType(ColumnType.MULTI_PICKLIST)
                .setOptions(Arrays.asList("Cat", "Rat", "Bat"));
        addCols = smartsheet.sheetResources().columnResources().addColumns(sheet.getId(), Arrays.asList(mpl));
        assertThat(addCols).hasSize(1);
    }

    public void testListMultiPicklistColumn() throws SmartsheetException {
        // sometimes fails to find the just-added resource
        PagedResult<Column> cols = RetryUtil.callWithRetry(
                () -> smartsheet.sheetResources().columnResources().listColumns(sheet.getId(), null, null, null));
        // should be TEXT_NUMBER since level not specified
        assertThat(cols.getData().get(0).getType()).isEqualTo(ColumnType.TEXT_NUMBER);

        cols = smartsheet.sheetResources().columnResources().listColumns(sheet.getId(),
                null, null, 2);
        // should be MULTI_PICKLIST since level 2 specified
        assertThat(cols.getData().get(0).getType()).isEqualTo(ColumnType.MULTI_PICKLIST);
    }

    public void testAddMultiPicklistRow() throws SmartsheetException {
        List<Cell> insertCells = Arrays.asList(new Cell().setColumnId(addCols.get(0).getId()).setObjectValue(
                new MultiPicklistObjectValue(Arrays.asList("Bat", "Cat"))));
        Row insertRow = new Row();
        insertRow.setToTop(true).setCells(insertCells);
        List<Row> insertRows = smartsheet.sheetResources().rowResources().addRows(sheet.getId(),
                Arrays.asList(insertRow));
        assertThat(insertRows).hasSize(1);
    }

    public void testGetMultiPicklistSheet() throws SmartsheetException {
        Sheet mpl = smartsheet.sheetResources().getSheet(sheet.getId(), EnumSet.of(SheetInclusion.OBJECT_VALUE),
                null, null, null, new HashSet<>(Arrays.asList(addCols.get(0).getId())), null,
                null, null, null);
        // should be TEXT_NUMBER since level not specified
        assertThat(mpl.getRows().get(0).getCells().get(0).getObjectValue()).isInstanceOf(StringObjectValue.class);

        mpl = smartsheet.sheetResources().getSheet(sheet.getId(), EnumSet.of(SheetInclusion.OBJECT_VALUE),
                null, null, null, new HashSet<>(Arrays.asList(addCols.get(0).getId())), null,
                null, null, 2);
        // should be MULTI_PICKLIST since level 2 specified
        assertThat(mpl.getRows().get(0).getCells().get(0).getObjectValue()).isInstanceOf(MultiPicklistObjectValue.class);
    }
}
