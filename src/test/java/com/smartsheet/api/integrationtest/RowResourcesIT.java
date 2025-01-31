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
import com.smartsheet.api.models.CopyOrMoveRowDestination;
import com.smartsheet.api.models.CopyOrMoveRowDirective;
import com.smartsheet.api.models.MultiRowEmail;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.PartialRowUpdateResult;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.ColumnInclusion;
import com.smartsheet.api.models.enums.ObjectExclusion;
import com.smartsheet.api.models.enums.RowCopyInclusion;
import com.smartsheet.api.models.enums.RowInclusion;
import com.smartsheet.api.models.enums.RowMoveInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RowResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    Sheet sheet;
    List<Row> newRows;
    Row row;
    Column addedColumn;
    Sheet copyToSheet;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testRowMethods() throws SmartsheetException, IOException {
        testAddRows();
        testGetRow();
        testCopyRow();
        testSendRows();
        testUpdateRows();
        // testPartialInsertRows(); covered by @Test annotation.
        // testPartialUpdateRows(); covered by @Test annotation.
        testMoveRow();
        testDeleteRows();
    }

    public void testAddRows() throws SmartsheetException, IOException {
        //create sheet
        sheet = smartsheet.sheetResources().createSheet(createSheetObject());

        //get column
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Column> wrapper = smartsheet
                .sheetResources()
                .columnResources()
                .listColumns(sheet.getId(), EnumSet.allOf(ColumnInclusion.class), parameters);

        Column addedColumn1 = wrapper.getData().get(0);
        Column addedColumn2 = wrapper.getData().get(1);

        // Specify cell values for first row.
        List<Cell> cellsA = new Cell.AddRowCellsBuilder()
                .addCell(addedColumn1.getId(), true)
                .addCell(addedColumn2.getId(), "New status")
                .build();

        // Specify contents of first row.
        row = new Row.AddRowBuilder().setCells(cellsA).setToBottom(true).build();

        // Specify cell values for second row.
        List<Cell> cellsB = new Cell.AddRowCellsBuilder()
                .addCell(addedColumn1.getId(), true)
                .addCell(addedColumn2.getId(), "New status")
                .build();

        // Specify contents of first row.
        Row rowA = new Row.AddRowBuilder().setCells(cellsB).setToBottom(true).build();

        newRows = smartsheet.sheetResources().rowResources().addRows(sheet.getId(), Arrays.asList(row, rowA));

        List<Column> columns = wrapper.getData();
        addedColumn = columns.get(1);
    }

    public void testGetRow() throws SmartsheetException {
        smartsheet.sheetResources().rowResources().getRow(sheet.getId(), newRows.get(0).getId(), null, null);
        row = smartsheet
                .sheetResources()
                .rowResources()
                .getRow(
                        sheet.getId(),
                        newRows.get(0).getId(),
                        EnumSet.of(RowInclusion.COLUMNS, RowInclusion.COLUMN_TYPE),
                        EnumSet.of(ObjectExclusion.NONEXISTENT_CELLS)
                );
        assertThat(row).isNotNull();
    }

    @Test
    void testUpdateRows() throws SmartsheetException, IOException {
        // create sheet
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Column> wrapper = smartsheet
                .sheetResources()
                .columnResources()
                .listColumns(sheet.getId(), EnumSet.allOf(ColumnInclusion.class), parameters);

        Column addedColumn1 = wrapper.getData().get(0);
        Column addedColumn2 = wrapper.getData().get(1);

        // Specify cell values for first row.
        List<Cell> cellsA = new Cell.AddRowCellsBuilder()
                .addCell(addedColumn1.getId(), true)
                .addCell(addedColumn2.getId(), "New status")
                .build();

        // Specify contents of first row.
        Row row = new Row.AddRowBuilder().setCells(cellsA).setToBottom(true).build();
        List<Row> newRows = smartsheet.sheetResources().rowResources().addRows(sheet.getId(), Arrays.asList(row));

        //Updated cells //correct
        List<Cell> cellsB = new Cell.UpdateRowCellsBuilder()
                .addCell(addedColumn1.getId(), true)
                .addCell(addedColumn2.getId(), "Updtaed status")
                .build();

        Row rowB = new Row.UpdateRowBuilder().setCells(cellsB).setRowId(newRows.get(0).getId()).build();

        List<Row> updatedRows = smartsheet.sheetResources().rowResources().updateRows(sheet.getId(), Arrays.asList(rowB));

        assertThat(updatedRows).isNotNull();
        deleteSheet(sheet.getId());
    }

    public void testCopyRow() throws SmartsheetException, IOException {
        //Create new sheet to copy to
        copyToSheet = smartsheet.sheetResources().createSheet(createSheetObject());

        CopyOrMoveRowDestination destination = new CopyOrMoveRowDestination.InsertCopyOrMoveRowDestinationBuilder()
                .setSheetId(copyToSheet.getId())
                .build();
        CopyOrMoveRowDirective copyOrMoveRowDirective = new CopyOrMoveRowDirective.InsertCopyOrMoveRowDirectiveBuilder()
                .setRowIds(Arrays.asList(newRows.get(0).getId())).setTo(destination)
                .build();

        smartsheet
                .sheetResources()
                .rowResources()
                .copyRows(sheet.getId(), null, null, copyOrMoveRowDirective);
        smartsheet
                .sheetResources()
                .rowResources()
                .copyRows(sheet.getId(), EnumSet.of(RowCopyInclusion.CHILDREN), false, copyOrMoveRowDirective);
    }

    public void testMoveRow() throws SmartsheetException, IOException {
        List<Long> rowIds = new ArrayList<>();
        rowIds.add(newRows.get(0).getId());

        CopyOrMoveRowDestination destination = new CopyOrMoveRowDestination.InsertCopyOrMoveRowDestinationBuilder()
                .setSheetId(copyToSheet.getId())
                .build();
        CopyOrMoveRowDirective directive = new CopyOrMoveRowDirective.InsertCopyOrMoveRowDirectiveBuilder()
                .setRowIds(rowIds)
                .setTo(destination)
                .build();

        smartsheet
                .sheetResources()
                .rowResources()
                .moveRows(
                        sheet.getId(),
                        EnumSet.of(RowMoveInclusion.ATTACHMENTS, RowMoveInclusion.DISCUSSIONS),
                        false,
                        directive
                );
    }

    public void testSendRows() throws SmartsheetException {
        // Specify individual recipient.
        RecipientEmail recipientEmail = new RecipientEmail.AddRecipientEmailBuilder().setEmail("john.doe@smartsheet.com").build();

        List<Recipient> recipients = new ArrayList<>();
        recipients.add(recipientEmail);

        MultiRowEmail multiRowEmail = new MultiRowEmail.AddMultiRowEmailBuilder()
                .setSendTo(recipients)
                .setSubject("some subject")
                .setMessage("some message")
                .setCcMe(false)
                .setRowIds(Arrays.asList(newRows.get(0).getId()))
                .setColumnIds(Arrays.asList(addedColumn.getId()))
                .setIncludeAttachments(false)
                .setIncludeDiscussions(false)
                .build();

        smartsheet.sheetResources().rowResources().sendRows(sheet.getId(), multiRowEmail);
    }

    @Test
    void testPartialInsertRows() throws SmartsheetException, IOException {
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObjectWithAutoNumberColumn());

        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Column> wrapper = smartsheet
                .sheetResources()
                .columnResources()
                .listColumns(sheet.getId(), EnumSet.allOf(ColumnInclusion.class), parameters);

        // Checkbox
        Column addedColumn1 = wrapper.getData().get(0);
        // Text number
        Column textNumberColumn = wrapper.getData().get(1);
        // AutoNumber column
        Column autoNumberColumn = wrapper.getData().get(3);

        List<Cell> cellsSucceed = new Cell.UpdateRowCellsBuilder().addCell(textNumberColumn.getId(), "Updated status").build();
        List<Cell> cellsFail = new Cell.UpdateRowCellsBuilder().addCell(autoNumberColumn.getId(), "Updated status").build();

        Row row = new Row.AddRowBuilder().setCells(cellsSucceed).setToBottom(true).build();
        Row row2 = new Row.AddRowBuilder().setCells(cellsFail).setToBottom(true).build();

        PartialRowUpdateResult result = smartsheet
                .sheetResources()
                .rowResources()
                .addRowsAllowPartialSuccess(sheet.getId(), Arrays.asList(row, row2));

        assertThat(result.getMessage()).isEqualTo("PARTIAL_SUCCESS");
        assertThat(result.getResult())
                .isNotNull()
                .hasSize(1);
        assertThat(result.getFailedItems())
                .isNotNull()
                .hasSize(1);

        // both rows should succeed in this test (i.e. failedItems = null)
        row2.setCells(cellsSucceed);
        result = smartsheet.sheetResources().rowResources().addRowsAllowPartialSuccess(sheet.getId(), Arrays.asList(row, row2));

        assertThat(result.getMessage()).isEqualTo("SUCCESS");
        assertThat(result.getResult())
                .isNotNull()
                .hasSize(2);
        assertThat(result.getFailedItems()).isNull();

        deleteSheet(sheet.getId());
    }

    @Test
    void testPartialUpdateRows() throws SmartsheetException, IOException {
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObjectWithAutoNumberColumn());

        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Column> wrapper = smartsheet
                .sheetResources()
                .columnResources()
                .listColumns(sheet.getId(), EnumSet.allOf(ColumnInclusion.class), parameters);

        // Checkbox
        Column addedColumn1 = wrapper.getData().get(0);
        // Text number
        Column textNumberColumn = wrapper.getData().get(1);
        // AutoNumber column
        Column autoNumberColumn = wrapper.getData().get(3);

        // Specify cell values for first row.
        List<Cell> cellsCreate = new Cell.AddRowCellsBuilder()
                .addCell(addedColumn1.getId(), true)
                .addCell(textNumberColumn.getId(), "New status").build();

        // Specify contents of first row.
        Row row = new Row.AddRowBuilder().setCells(cellsCreate).setToBottom(true).build();
        Row row2 = new Row.AddRowBuilder().setCells(cellsCreate).setToBottom(true).build();
        List<Row> newRows = smartsheet.sheetResources().rowResources().addRows(sheet.getId(), Arrays.asList(row, row2));

        //Updated cells
        List<Cell> cellUpdateSucceed = new Cell.UpdateRowCellsBuilder().addCell(textNumberColumn.getId(), "Updated status").build();
        List<Cell> cellUpdateFail = new Cell.UpdateRowCellsBuilder().addCell(autoNumberColumn.getId(), "Updated status").build();

        Row rowSucceeds = new Row.UpdateRowBuilder().setCells(cellUpdateSucceed).setRowId(newRows.get(0).getId()).build();
        Row rowFails = new Row.UpdateRowBuilder().setCells(cellUpdateFail).setRowId(newRows.get(1).getId()).build();

        PartialRowUpdateResult result = smartsheet
                .sheetResources()
                .rowResources()
                .updateRowsAllowPartialSuccess(sheet.getId(), Arrays.asList(rowSucceeds, rowFails));

        assertThat(result.getMessage()).isEqualTo("PARTIAL_SUCCESS");
        assertThat(result.getResult())
                .isNotNull()
                .hasSize(1);
        assertThat(result.getFailedItems())
                .isNotNull()
                .hasSize(1);

        Row rowSucceeds2 = new Row.UpdateRowBuilder().setCells(cellUpdateSucceed).setRowId(newRows.get(1).getId()).build();
        result = smartsheet
                .sheetResources()
                .rowResources()
                .updateRowsAllowPartialSuccess(sheet.getId(), Arrays.asList(rowSucceeds, rowSucceeds2));

        assertThat(result.getMessage()).isEqualTo("SUCCESS");
        assertThat(result.getResult())
                .isNotNull()
                .hasSize(2);
        assertThat(result.getFailedItems()).isNullOrEmpty();

        deleteSheet(sheet.getId());
    }

    public void testDeleteRows() throws SmartsheetException, IOException {
        testAddRows();
        smartsheet.sheetResources().rowResources().deleteRows(sheet.getId(), new HashSet(Arrays.asList(newRows.get(0).getId())), true);

        //clean up
        deleteSheet(sheet.getId());
        deleteSheet(copyToSheet.getId());
    }

    @Test
    public void testUpdateSingleCell() throws Exception {
        testAddRows();

        smartsheet.sheetResources().rowResources().updateCell(sheet.getId(), 1, 1, "Updated Value");

        Sheet checkSheet = smartsheet.sheetResources().getSheet(sheet.getId());
        assertThat(checkSheet.getRows().get(0).getCells().get(0).getValue()).isEqualTo("Updated Value");
    }
}
