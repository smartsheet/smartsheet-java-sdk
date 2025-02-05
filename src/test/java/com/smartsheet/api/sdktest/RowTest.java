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
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.CellLink;
import com.smartsheet.api.models.Duration;
import com.smartsheet.api.models.Hyperlink;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Predecessor;
import com.smartsheet.api.models.PredecessorList;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RowTest {
    @Test
    void ListSheets_NoParams() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Sheets - No Params");
        PagedResult<Sheet> sheets = ss.sheetResources().listSheets();
        assertThat(sheets.getData().get(0).getName()).isEqualTo("Copy of Sample Sheet");
    }

    @Test
    void addRows_AssignValues_String() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Values - String");

        Cell cell1 = new Cell().setColumnId(101L).setValue("Apple");
        Cell cell2 = new Cell().setColumnId(102L).setValue("Red Fruit");
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        Cell cell3 = new Cell().setColumnId(101L).setValue("Banana");
        Cell cell4 = new Cell().setColumnId(102L).setValue("Yellow Fruit");
        Row rowB = new Row().setCells(Arrays.asList(cell3, cell4));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1L, Arrays.asList(rowA, rowB));

        assertThat(addedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
    }

    @Test
    void addRows_Location_Top() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Location - Top");

        Cell cell1 = new Cell().setColumnId(101L).setValue("Apple");
        Cell cell2 = new Cell().setColumnId(102L).setValue("Red Fruit");
        Row rowA = new Row().setToTop(true).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, List.of(rowA));

        assertThat(addedRows.get(0).getCells().get(0).getValue()).isEqualTo("Apple");
        assertThat(addedRows.get(0).getRowNumber().intValue()).isEqualTo(1);
        assertThat(addedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
    }

    @Test
    void addRows_Location_Bottom() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Location - Bottom");

        Cell cell1 = new Cell().setColumnId(101L).setValue("Apple");
        Cell cell2 = new Cell().setColumnId(102L).setValue("Red Fruit");
        Row rowA = new Row().setToBottom(true).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, List.of(rowA));

        assertThat(addedRows.get(0).getCells().get(0).getValue()).isEqualTo("Apple");
        assertThat(addedRows.get(0).getRowNumber().intValue()).isEqualTo(100);
        assertThat(addedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
    }

    @Test
    void addRows_AssignValues_Int() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Values - Int");

        Cell cell1 = new Cell().setColumnId(101L).setValue(100);
        Cell cell2 = new Cell().setColumnId(102L).setValue("One Hundred");
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        Cell cell3 = new Cell().setColumnId(101L).setValue(2.1);
        Cell cell4 = new Cell().setColumnId(102L).setValue("Two Point One");
        Row rowB = new Row().setCells(Arrays.asList(cell3, cell4));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, Arrays.asList(rowA, rowB));

        assertThat(addedRows.get(0).getCells().get(0).getValue()).isEqualTo(100);
        assertThat(addedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
    }

    @Test
    void addRows_AssignValues_Bool() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Values - Bool");

        Cell cell1 = new Cell().setColumnId(101L).setValue(true);
        Cell cell2 = new Cell().setColumnId(102L).setValue("This is True");
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        Cell cell3 = new Cell().setColumnId(101L).setValue(false);
        Cell cell4 = new Cell().setColumnId(102L).setValue("This is False");
        Row rowB = new Row().setCells(Arrays.asList(cell3, cell4));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, Arrays.asList(rowA, rowB));

        assertThat(addedRows.get(0).getId().intValue()).isEqualTo(10);
        assertThat(addedRows.get(0).getCells().get(0).getValue()).isEqualTo(true);
        assertThat(addedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
    }

    @Test
    void addRows_AssignFormulae() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Formulae");

        Cell cell1 = new Cell().setColumnId(101L).setFormula("=SUM([Column2]3, [Column2]4)*2");
        Cell cell2 = new Cell().setColumnId(102L).setFormula("=SUM([Column2]3, [Column2]3, [Column2]4)");
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, List.of(rowA));

        assertThat(addedRows.get(0).getCells().get(1).getFormula()).isEqualTo("=SUM([Column2]3, [Column2]3, [Column2]4)");
        assertThat(addedRows.get(0).getCells().get(1).getColumnId().longValue()).isEqualTo(102L);
    }

    @Test
    void addRows_AssignValues_Hyperlink() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Values - Hyperlink");

        Cell cell1 = new Cell().setColumnId(101L).setValue("Google").setHyperlink(new Hyperlink().setUrl("http://google.com"));
        Cell cell2 = new Cell().setColumnId(102L).setValue("Bing").setHyperlink(new Hyperlink().setUrl("http://bing.com"));
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, List.of(rowA));

        assertThat(addedRows.get(0).getCells().get(0).getValue()).isEqualTo("Google");
        assertThat(addedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(addedRows.get(0).getCells().get(0).getHyperlink().getUrl()).isEqualTo("http://google.com");
    }

    @Test
    void addRows_AssignValues_HyperlinkSheetID() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Values - Hyperlink SheetID");

        Cell cell1 = new Cell().setColumnId(101L).setValue("Sheet2").setHyperlink(new Hyperlink().setSheetId(2L));
        Cell cell2 = new Cell().setColumnId(102L).setValue("Sheet3").setHyperlink(new Hyperlink().setSheetId(3L));
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, List.of(rowA));

        assertThat(addedRows.get(0).getCells().get(1).getValue()).isEqualTo("Sheet3");
        assertThat(addedRows.get(0).getCells().get(1).getColumnId().longValue()).isEqualTo(102L);
        assertThat(addedRows.get(0).getCells().get(1).getHyperlink().getSheetId().longValue()).isEqualTo(3L);
    }

    @Test
    void addRows_AssignValues_HyperlinkReportID() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Values - Hyperlink ReportID");

        Cell cell1 = new Cell().setColumnId(101L).setValue("Report9").setHyperlink(new Hyperlink().setReportId(9L));
        Cell cell2 = new Cell().setColumnId(102L).setValue("Report8").setHyperlink(new Hyperlink().setReportId(8L));
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, List.of(rowA));

        assertThat(addedRows.get(0).getCells().get(1).getValue()).isEqualTo("Report8");
        assertThat(addedRows.get(0).getCells().get(1).getColumnId().longValue()).isEqualTo(102L);
        assertThat(addedRows.get(0).getCells().get(1).getHyperlink().getReportId().longValue()).isEqualTo(8L);
    }

    @Test
    void addRows_Invalid_AssignHyperlinkUrlAndSheetId() {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Invalid - Assign Hyperlink URL and SheetId");

        Cell cell1 = new Cell()
                .setColumnId(101L)
                .setValue("Google")
                .setHyperlink(new Hyperlink().setUrl("http://google.com").setSheetId(2L));
        Cell cell2 = new Cell().setColumnId(102L).setValue("Bing").setHyperlink(new Hyperlink().setUrl("http://bing.com"));
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        assertThatThrownBy(() -> ss.sheetResources().rowResources().addRows(1, List.of(rowA)))
                .isInstanceOf(SmartsheetException.class)
                .hasMessage("hyperlink.url must be null for sheet, report, or Sight hyperlinks.");
    }

    @Test
    void addRows_Invalid_AssignValueAndFormulae() {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Invalid - Assign Value and Formulae");

        Cell cell1 = new Cell().setColumnId(101L).setFormula("=SUM([Column2]3, [Column2]4)*2").setValue("20");
        Cell cell2 = new Cell().setColumnId(102L).setFormula("=SUM([Column2]3, [Column2]3, [Column2]4)");
        Row rowA = new Row().setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        assertThatThrownBy(() -> ss.sheetResources().rowResources().addRows(1, List.of(rowA)))
                .isInstanceOf(SmartsheetException.class)
                .hasMessage(
                        "If cell.formula is specified, then value, objectValue, image, hyperlink, and linkInFromCell must not be specified."
                );
    }

    @Test
    void addRows_AssignObjectValue_PredecessorList() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Add Rows - Assign Object Value - Predecessor List (using floats)");

        Duration duration = new Duration().setDays(2.5);
        Predecessor predecessor = new Predecessor().setRowId(10L).setType("FS").setLag(duration);
        PredecessorList predecessorList = new PredecessorList().setPredecessors(List.of(predecessor));
        Cell cell1 = new Cell().setColumnId(101L).setObjectValue(predecessorList);
        Row rowA = new Row().setCells(List.of(cell1));

        List<Row> addedRows = ss.sheetResources().rowResources().addRows(1, List.of(rowA));

        assertThat(addedRows.get(0).getCells().get(1).getColumnId().longValue()).isEqualTo(101L);
        assertThat(addedRows.get(0).getCells().get(1).getValue()).isEqualTo("2FS +2.5d");
    }

    @Test
    void updateRows_AssignValues_String() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Assign Values - String");

        Cell cell1 = new Cell().setColumnId(101L).setValue("Apple");
        Cell cell2 = new Cell().setColumnId(102L).setValue("Red Fruit");
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        Cell cell3 = new Cell().setColumnId(101L).setValue("Banana");
        Cell cell4 = new Cell().setColumnId(102L).setValue("Yellow Fruit");
        Row rowB = new Row().setRowId(11L).setCells(Arrays.asList(cell3, cell4));

        // Update rows in sheet
        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1L, Arrays.asList(rowA, rowB));

        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isEqualTo("Apple");
    }

    @Test
    void updateRows_AssignValues_Int() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Assign Values - Int");

        Cell cell1 = new Cell().setColumnId(101L).setValue(100);
        Cell cell2 = new Cell().setColumnId(102L).setValue("One Hundred");
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        Cell cell3 = new Cell().setColumnId(101L).setValue(2.1);
        Cell cell4 = new Cell().setColumnId(102L).setValue("Two Point One");
        Row rowB = new Row().setRowId(11L).setCells(Arrays.asList(cell3, cell4));

        // Update rows in sheet
        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1L, Arrays.asList(rowA, rowB));

        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isEqualTo(100);
    }

    @Test
    void updateRows_AssignValues_Bool() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Assign Values - Bool");

        Cell cell1 = new Cell().setColumnId(101L).setValue(true);
        Cell cell2 = new Cell().setColumnId(102L).setValue("This is True");
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        Cell cell3 = new Cell().setColumnId(101L).setValue(false);
        Cell cell4 = new Cell().setColumnId(102L).setValue("This is False");
        Row rowB = new Row().setRowId(11L).setCells(Arrays.asList(cell3, cell4));

        // Update rows in sheet
        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1L, Arrays.asList(rowA, rowB));

        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isEqualTo(true);
    }

    @Test
    void updateRows_AssignFormulae() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Assign Formulae");

        Cell cell1 = new Cell().setColumnId(101L).setFormula("=SUM([Column2]3, [Column2]4)*2");
        Cell cell2 = new Cell().setColumnId(102L).setFormula("=SUM([Column2]3, [Column2]3, [Column2]4)");
        Row rowA = new Row().setRowId(11L).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(1).getFormula()).isEqualTo("=SUM([Column2]3, [Column2]3, [Column2]4)");
        assertThat(updatedRows.get(0).getCells().get(1).getColumnId().longValue()).isEqualTo(102L);
    }

    @Test
    void updateRows_AssignValues_Hyperlink() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Assign Values - Hyperlink");

        Hyperlink hyperlink1 = new Hyperlink().setUrl("http://google.com");
        Cell cell1 = new Cell().setColumnId(101L).setValue("Google").setHyperlink(hyperlink1);
        Hyperlink hyperlink2 = new Hyperlink().setUrl("http://bing.com");
        Cell cell2 = new Cell().setColumnId(102L).setValue("Bing").setHyperlink(hyperlink2);
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isEqualTo("Google");
        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getHyperlink().getUrl()).isEqualTo("http://google.com");
    }

    @Test
    void updateRows_AssignValues_HyperlinkSheetID() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Assign Values - Hyperlink SheetID");

        Hyperlink hyperlink1 = new Hyperlink().setSheetId(2L);
        Cell cell1 = new Cell().setColumnId(101L).setValue("Sheet2").setHyperlink(hyperlink1);
        Hyperlink hyperlink2 = new Hyperlink().setSheetId(3L);
        Cell cell2 = new Cell().setColumnId(102L).setValue("Sheet3").setHyperlink(hyperlink2);
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(1).getValue()).isEqualTo("Sheet3");
        assertThat(updatedRows.get(0).getCells().get(1).getColumnId().longValue()).isEqualTo(102L);
        assertThat(updatedRows.get(0).getCells().get(1).getHyperlink().getSheetId().longValue()).isEqualTo(3L);
    }

    @Test
    void updateRows_AssignValues_HyperlinkReportID() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Assign Values - Hyperlink ReportID");

        Hyperlink hyperlink1 = new Hyperlink().setReportId(9L);
        Cell cell1 = new Cell().setColumnId(101L).setValue("Report9").setHyperlink(hyperlink1);
        Hyperlink hyperlink2 = new Hyperlink().setReportId(8L);
        Cell cell2 = new Cell().setColumnId(102L).setValue("Report8").setHyperlink(hyperlink2);
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(1).getValue()).isEqualTo("Report8");
        assertThat(updatedRows.get(0).getCells().get(1).getColumnId().longValue()).isEqualTo(102L);
        assertThat(updatedRows.get(0).getCells().get(1).getHyperlink().getReportId().longValue()).isEqualTo(8L);
    }

    @Test
    void updateRows_Invalid_AssignHyperlinkUrlAndSheetId() {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Invalid - Assign Hyperlink URL and SheetId");

        Hyperlink hyperlink1 = new Hyperlink().setUrl("http://google.com").setSheetId(2L);
        Cell cell1 = new Cell().setColumnId(101L).setValue("Google").setHyperlink(hyperlink1);
        Hyperlink hyperlink2 = new Hyperlink().setUrl("http://bing.com");
        Cell cell2 = new Cell().setColumnId(102L).setValue("Bing").setHyperlink(hyperlink2);
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        assertThatThrownBy(() -> ss.sheetResources().rowResources().updateRows(1, List.of(rowA)))
                .isInstanceOf(SmartsheetException.class)
                .hasMessage("hyperlink.url must be null for sheet, report, or Sight hyperlinks.");
    }

    @Test
    void updateRows_Invalid_AssignValueAndFormulae() {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Invalid - Assign Value and Formulae");

        Cell cell1 = new Cell().setColumnId(101L).setFormula("=SUM([Column2]3, [Column2]4)*2").setValue("20");
        Cell cell2 = new Cell().setColumnId(102L).setFormula("=SUM([Column2]3, [Column2]3, [Column2]4)");
        Row rowA = new Row().setRowId(10L).setCells(Arrays.asList(cell1, cell2));

        // Update rows in sheet
        assertThatThrownBy(() -> ss.sheetResources().rowResources().updateRows(1, List.of(rowA)))
                .isInstanceOf(SmartsheetException.class)
                .hasMessage(
                        "If cell.formula is specified, then value, objectValue, image, hyperlink, and linkInFromCell must not be specified."
                );
    }

    @Test
    void updateRows_ClearValue_TextNumber() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Clear Value - Text Number");

        Cell cell1 = new Cell().setColumnId(101L).setValue("");
        Row rowA = new Row().setRowId(10L).setCells(List.of(cell1));

        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isNull();
    }

    @Test
    void updateRows_ClearValue_Checkbox() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Clear Value - Checkbox");

        Cell cell1 = new Cell().setColumnId(101L).setValue("");
        Row rowA = new Row().setRowId(10L).setCells(List.of(cell1));

        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isEqualTo(false);
    }

    @Test
    void updateRows_ClearValue_Hyperlink() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Clear Value - Hyperlink");

        Cell cell1 = new Cell().setColumnId(101L).setValue("").setHyperlink(new Hyperlink());
        Row rowA = new Row().setRowId(10L).setCells(List.of(cell1));

        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isNull();
        assertThat(updatedRows.get(0).getCells().get(0).getHyperlink()).isNull();
    }

    @Test
    void updateRows_ClearValue_CellLink() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Clear Value - Cell Link");

        Cell cell1 = new Cell().setColumnId(101L).setValue("").setLinkInFromCell(new CellLink());
        Row rowA = new Row().setRowId(10L).setCells(List.of(cell1));

        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getCells().get(0).getColumnId().longValue()).isEqualTo(101L);
        assertThat(updatedRows.get(0).getCells().get(0).getValue()).isNull();
        assertThat(updatedRows.get(0).getCells().get(0).getLinkInFromCell()).isNull();
    }

    @Test
    void updateRows_Invalid_AssignHyperlinkAndCellLink() {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Invalid - Assign Hyperlink and Cell Link");

        Hyperlink hyperlink = new Hyperlink().setUrl("www.google.com");
        CellLink cellLink = new CellLink().setRowId(20L).setSheetId(2L).setColumnId(201L);
        Cell cell1 = new Cell().setColumnId(101L).setValue("").setHyperlink(hyperlink).setLinkInFromCell(cellLink);
        Row rowA = new Row().setRowId(10L).setCells(List.of(cell1));

        assertThatThrownBy(() -> ss.sheetResources().rowResources().updateRows(1, List.of(rowA)))
                .isInstanceOf(SmartsheetException.class)
                .hasMessage("Only one of cell.hyperlink or cell.linkInFromCell may be non-null.");
    }

    @Test
    void updateRows_Location_Top() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Location - Top");

        Row rowA = new Row().setRowId(10L).setToTop(true);

        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getId().longValue()).isEqualTo(10L);
        assertThat(updatedRows.get(0).getRowNumber().longValue()).isEqualTo(1L);
    }

    @Test
    void updateRows_Location_Bottom() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Rows - Location - Bottom");

        Row rowA = new Row().setRowId(10L).setToBottom(true);

        List<Row> updatedRows = ss.sheetResources().rowResources().updateRows(1, List.of(rowA));

        assertThat(updatedRows.get(0).getId().longValue()).isEqualTo(10L);
        assertThat(updatedRows.get(0).getRowNumber().longValue()).isEqualTo(100L);
    }
}
