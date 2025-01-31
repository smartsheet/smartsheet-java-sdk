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

package com.smartsheet.api.internal;

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.CopyOrMoveRowDestination;
import com.smartsheet.api.models.CopyOrMoveRowDirective;
import com.smartsheet.api.models.CopyOrMoveRowResult;
import com.smartsheet.api.models.MultiRowEmail;
import com.smartsheet.api.models.PartialRowUpdateResult;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.enums.ObjectExclusion;
import com.smartsheet.api.models.enums.RowCopyInclusion;
import com.smartsheet.api.models.enums.RowInclusion;
import com.smartsheet.api.models.enums.RowMoveInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SheetRowResourcesImplTest extends ResourcesImplBase {

    private SheetRowResourcesImpl sheetRowResource;

    @BeforeEach
    public void setUp() throws Exception {
        sheetRowResource = new SheetRowResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Test
    void testInsertRows() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/insertRows.json"));

        // Create a set of cells
        List<Cell> cells = new ArrayList<>();
        Cell cell = new Cell();
        cell.setDisplayValue("Testing");
        cell.setColumnId(8764071660021636L);
        cell.setRowId(1234L);
        cell.setFormula("=1+1");

        // Create a row and add the cells to it.
        List<Row> rows = new ArrayList<>();
        Row row = new Row();
        row.setCells(cells);
        rows.add(row);

        List<Row> newRows = sheetRowResource.addRows(1234L, rows);
        Row row1 = newRows.get(0);
        Row row2 = newRows.get(1);

        assertThat(newRows).hasSize(2);
        assertThat(row1.getId().longValue()).isEqualTo(7670198317672324L);
        assertThat(row1.getCells()).hasSize(2);
        assertThat(row1.getCells().get(0).getColumnType()).hasToString("CHECKBOX");
        assertThat(row2.getId().longValue()).isEqualTo(2040698783459204L);
        assertThat(row2.getCells()).hasSize(2);
        assertThat(row2.getCells().get(1).getValue()).isEqualTo("New status");
    }

    @Test
    void testGetRow() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getRow.json"));

        Row row = sheetRowResource.getRow(
                1234L,
                5678L,
                EnumSet.of(RowInclusion.COLUMNS, RowInclusion.FORMAT),
                EnumSet.of(ObjectExclusion.NONEXISTENT_CELLS)
        );

        assertThat(row).isNotNull();
        assertThat(row.getId().longValue()).isEqualTo(2361756178769796L);
        assertThat(row.getSheetId().longValue()).isEqualTo(4583173393803140L);
        assertThat(row.getCells()).hasSize(2);
        assertThat(row.getCells().get(0).getValue()).isEqualTo("Revision 1");
    }

    @Test
    void testSendRows_NoExceptionThrown() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/sendRow.json"));

        RecipientEmail recipient = new RecipientEmail();
        recipient.setEmail("johndoe@smartsheet.com");

        MultiRowEmail email = new MultiRowEmail();

        List<Recipient> to = new ArrayList<>();
        to.add(recipient);

        email.setSendTo(to);
        email.setMessage("Test Message");
        email.setSubject("Test Subject");
        email.setIncludeAttachments(true);
        email.setIncludeDiscussions(true);
        email.setCcMe(true);

        assertThatCode(() -> sheetRowResource.sendRows(1234L, email)).doesNotThrowAnyException();
    }

    @Test
    void testUpdateRows() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateRows.json"));

        List<Row> rows = new ArrayList<>();

        List<Row> updatedRows = sheetRowResource.updateRows(1234L, rows);
        Row row1 = updatedRows.get(0);

        assertThat(row1.getCells()).hasSize(6);
        assertThat(row1.getRowNumber().longValue()).isEqualTo(3L);
        assertThat(row1.getId().longValue()).isEqualTo(1231490655774596L);

        Cell cell = row1.getCells().get(0);
        assertThat(cell.getColumnId().longValue()).isEqualTo(7670639323572100L);
    }

    @Test
    void testMoveRows() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/moveRow.json"));
        CopyOrMoveRowDirective copyOrMoveRowDirective = new CopyOrMoveRowDirective();
        CopyOrMoveRowDestination copyOrMoveRowDestination = new CopyOrMoveRowDestination();
        copyOrMoveRowDestination.setSheetId(2258256056870788L);

        copyOrMoveRowDirective.setTo(copyOrMoveRowDestination);
        List<Long> rowIds = new ArrayList<>();
        rowIds.add(145417762563972L);
        copyOrMoveRowDirective.setRowIds(rowIds);
        CopyOrMoveRowResult copyOrMoveRowResult = sheetRowResource.moveRows(
                2258256056870788L,
                EnumSet.of(RowMoveInclusion.ATTACHMENTS),
                false,
                copyOrMoveRowDirective
        );

        assertThat(copyOrMoveRowResult).isNotNull();
        assertThat(copyOrMoveRowResult.getRowMappings())
                .isNotEmpty()
                .hasSize(2);
        assertThat(copyOrMoveRowResult.getDestinationSheetId())
                .isNotBlank()
                .isEqualTo("2258256056870788");
    }

    @Test
    void testCopyRows() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/moveRow.json"));
        CopyOrMoveRowDirective copyOrMoveRowDirective = new CopyOrMoveRowDirective();
        CopyOrMoveRowDestination copyOrMoveRowDestination = new CopyOrMoveRowDestination();
        copyOrMoveRowDestination.setSheetId(2258256056870788L);

        copyOrMoveRowDirective.setTo(copyOrMoveRowDestination);
        List<Long> rowIds = new ArrayList<>();
        rowIds.add(145417762563972L);
        copyOrMoveRowDirective.setRowIds(rowIds);
        CopyOrMoveRowResult copyOrMoveRowResult = sheetRowResource.copyRows(
                2258256056870788L,
                EnumSet.of(RowCopyInclusion.ATTACHMENTS),
                false,
                copyOrMoveRowDirective
        );

        assertThat(copyOrMoveRowResult).isNotNull();
        assertThat(copyOrMoveRowResult.getRowMappings())
                .isNotEmpty()
                .hasSize(2);
        assertThat(copyOrMoveRowResult.getDestinationSheetId())
                .isNotBlank()
                .isEqualTo("2258256056870788");
    }

    @Test
    void testCopyRows_IgnoreRowsNotFoundNull() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/moveRow.json"));
        CopyOrMoveRowDirective copyOrMoveRowDirective = new CopyOrMoveRowDirective();
        CopyOrMoveRowDestination copyOrMoveRowDestination = new CopyOrMoveRowDestination();
        copyOrMoveRowDestination.setSheetId(2258256056870788L);

        copyOrMoveRowDirective.setTo(copyOrMoveRowDestination);
        List<Long> rowIds = new ArrayList<>();
        rowIds.add(145417762563972L);
        copyOrMoveRowDirective.setRowIds(rowIds);
        CopyOrMoveRowResult copyOrMoveRowResult = sheetRowResource.copyRows(
                2258256056870788L,
                EnumSet.of(RowCopyInclusion.ATTACHMENTS),
                null,
                copyOrMoveRowDirective
        );

        assertThat(copyOrMoveRowResult).isNotNull();
        assertThat(copyOrMoveRowResult.getRowMappings())
                .isNotEmpty()
                .hasSize(2);
        assertThat(copyOrMoveRowResult.getDestinationSheetId())
                .isNotBlank()
                .isEqualTo("2258256056870788");
    }

    @Test
    void testDeleteRows() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteRow.json"));

        Set<Long> rowIds = new HashSet<>();
        rowIds.add(123456789L);
        rowIds.add(987654321L);

        List<Long> ids = sheetRowResource.deleteRows(123L, rowIds, true);
        assertThat(ids).isNotNull();
        assertThat(ids.size()).isEqualTo(2);
        assertThat(ids).contains(123456789L);
        assertThat(ids).contains(987654321L);
    }

    @Test
    void testUpdateRowsAllowPartialSuccess_NullRows() {
        assertThatThrownBy(() -> sheetRowResource.updateRowsAllowPartialSuccess(123L, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testUpdateRowsAllowPartialSuccess() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/partialRowUpdateRowResult.json"));

        List<Row> rows = new ArrayList<>();

        rows.add(new Row(123456789L));
        rows.add(new Row(987654321L));

        PartialRowUpdateResult partialRowUpdateResult = sheetRowResource.updateRowsAllowPartialSuccess(123L, rows);
        assertThat(partialRowUpdateResult).isNotNull();
        assertThat(partialRowUpdateResult.getResult()).isNotEmpty();
        assertThat(partialRowUpdateResult.getResult().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getResult().get(0).getRowId()).isEqualTo(123456789L);
        assertThat(partialRowUpdateResult.getFailedItems()).isNotEmpty();
        assertThat(partialRowUpdateResult.getFailedItems().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getFailedItems().get(0).getRowId()).isEqualTo(987654321L);
    }

    @Test
    void testUpdateRowsAllowPartialSuccess_WithSomeIncludesExcludes() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/partialRowUpdateRowResult.json"));

        List<Row> rows = new ArrayList<>();

        rows.add(new Row(123456789L));
        rows.add(new Row(987654321L));

        PartialRowUpdateResult partialRowUpdateResult = sheetRowResource.updateRowsAllowPartialSuccess(
                123L,
                rows,
                EnumSet.of(RowInclusion.DISCUSSIONS),
                EnumSet.of(ObjectExclusion.NONEXISTENT_CELLS)
        );
        assertThat(partialRowUpdateResult).isNotNull();
        assertThat(partialRowUpdateResult.getResult()).isNotEmpty();
        assertThat(partialRowUpdateResult.getResult().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getResult().get(0).getRowId()).isEqualTo(123456789L);
        assertThat(partialRowUpdateResult.getFailedItems()).isNotEmpty();
        assertThat(partialRowUpdateResult.getFailedItems().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getFailedItems().get(0).getRowId()).isEqualTo(987654321L);
    }

    @Test
    void testAddRowsAllowPartialSuccess_NullRows() {
        assertThatThrownBy(() -> sheetRowResource.addRowsAllowPartialSuccess(123L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAddRowsAllowPartialSuccess_WithSomeIncludesExcludes() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/partialRowUpdateRowResult.json"));

        List<Row> rows = new ArrayList<>();

        rows.add(new Row(123456789L));
        rows.add(new Row(987654321L));

        PartialRowUpdateResult partialRowUpdateResult = sheetRowResource.addRowsAllowPartialSuccess(
                123L,
                rows,
                EnumSet.of(RowInclusion.ATTACHMENTS),
                EnumSet.of(ObjectExclusion.NONEXISTENT_CELLS)
        );
        assertThat(partialRowUpdateResult).isNotNull();
        assertThat(partialRowUpdateResult.getResult()).isNotEmpty();
        assertThat(partialRowUpdateResult.getResult().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getResult().get(0).getRowId()).isEqualTo(123456789L);
        assertThat(partialRowUpdateResult.getFailedItems()).isNotEmpty();
        assertThat(partialRowUpdateResult.getFailedItems().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getFailedItems().get(0).getRowId()).isEqualTo(987654321L);
    }

    @Test
    void testAddRowsAllowPartialSuccess() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/partialRowUpdateRowResult.json"));

        List<Row> rows = new ArrayList<>();

        rows.add(new Row(123456789L));
        rows.add(new Row(987654321L));

        PartialRowUpdateResult partialRowUpdateResult = sheetRowResource.addRowsAllowPartialSuccess(123L, rows);
        assertThat(partialRowUpdateResult).isNotNull();
        assertThat(partialRowUpdateResult.getResult()).isNotEmpty();
        assertThat(partialRowUpdateResult.getResult().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getResult().get(0).getRowId()).isEqualTo(123456789L);
        assertThat(partialRowUpdateResult.getFailedItems()).isNotEmpty();
        assertThat(partialRowUpdateResult.getFailedItems().size()).isEqualTo(1);
        assertThat(partialRowUpdateResult.getFailedItems().get(0).getRowId()).isEqualTo(987654321L);
    }
}
