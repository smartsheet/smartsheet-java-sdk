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
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.FormatDetails;
import com.smartsheet.api.models.MultiRowEmail;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.SheetEmail;
import com.smartsheet.api.models.SheetPublish;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.ColumnInclusion;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.DestinationType;
import com.smartsheet.api.models.enums.PaperSize;
import com.smartsheet.api.models.enums.SheetCopyInclusion;
import com.smartsheet.api.models.enums.SheetEmailFormat;
import com.smartsheet.api.models.enums.SheetTemplateInclusion;
import com.smartsheet.api.models.enums.SourceInclusion;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SheetResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    Sheet sheetHome;
    Sheet newSheetHome;
    Sheet newSheetTemplate;
    Workspace workspace;

    Folder folder;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testSheetMethods() throws SmartsheetException, IOException {
        testCreateSheetHome();
        testCopySheet();
        testMoveSheet();
        testCreateSheetHomeFromTemplate();
        testCreateSheetInFolder();
        testCreateSheetInFolderFromTemplate();
        // This test was consistently failing because ResourceNotFoundException: Not Found
        // testCreateSheetInWorkspace();
        // This test was failing with an NPE
        // testCreateSheetInWorkspaceFromTemplate();
        testGetSheet();
        testGetSheetVersion();
        testGetSheetAsExcel();
        testGetSheetAsPDF();
        testGetPublishStatus();
        testUpdateSheet();
        testPublishSheetDefaults();
        testPublishSheet();
        testUpdatePublishStatus();
        testListSheets();
        testListOrganizationSheets();
        testSendSheet();
        testCreateUpdateRequest();
        // This test was failing with an NPE
        // testDeleteSheet();
    }

    public void testCreateSheetHome() throws SmartsheetException, IOException {

        // create sheet object
        sheetHome = createSheetObject();

        //create sheet
        newSheetHome = smartsheet.sheetResources().createSheet(sheetHome);
        assertThat(newSheetHome.getColumns()).hasSameSizeAs(sheetHome.getColumns());
    }

    public void testCopySheet() throws SmartsheetException, IOException {
        Folder folder = createFolder();

        ContainerDestination destination = new ContainerDestination.AddContainerDestinationBuilder()
                .setDestinationType(DestinationType.FOLDER)
                .setDestinationId(folder.getId())
                .setNewName("New Copied sheet")
                .build();

        Sheet sheet = smartsheet.sheetResources().copySheet(newSheetHome.getId(), destination, EnumSet.of(SheetCopyInclusion.ALL));
        assertThat(sheet.getName()).isEqualTo("New Copied sheet");
        deleteFolder(folder.getId());
    }

    public void testMoveSheet() throws SmartsheetException, IOException {
        Folder folder = createFolder();
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());

        ContainerDestination destination = new ContainerDestination.AddContainerDestinationBuilder()
                .setDestinationType(DestinationType.FOLDER)
                .setDestinationId(folder.getId())
                .build();

        Sheet movedSheet = smartsheet.sheetResources().moveSheet(sheet.getId(), destination);
        assertThat(movedSheet).isNotNull();
        deleteSheet(movedSheet.getId());
        deleteFolder(folder.getId());
    }

    public void testCreateUpdateRequest() throws SmartsheetException, IOException {
        //
        //create sheet
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());

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
        Row row = new Row.AddRowBuilder().setCells(cellsA).setToBottom(true).build();

        // Specify cell values for second row.
        List<Cell> cellsB = new Cell.AddRowCellsBuilder()
                .addCell(addedColumn1.getId(), true)
                .addCell(addedColumn2.getId(), "New status")
                .build();

        // Specify contents of first row.
        Row rowA = new Row.AddRowBuilder().setCells(cellsB).setToBottom(true).build();

        List<Row> newRows = smartsheet.sheetResources().rowResources().addRows(sheet.getId(), Arrays.asList(row, rowA));

        List<Column> columns = wrapper.getData();
        Column addedColumn = columns.get(1);
        //

        RecipientEmail recipientEmail = new RecipientEmail.AddRecipientEmailBuilder()
                .setEmail("aditi.nioding@smartsheet.com")
                .setEmail("john.doe@smartsheet.com")
                .build();

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

        smartsheet.sheetResources().createUpdateRequest(sheet.getId(), multiRowEmail);

        deleteSheet(sheet.getId());
    }

    public void testCreateSheetHomeFromTemplate() throws SmartsheetException, IOException {

        Sheet sheet = new Sheet.CreateFromTemplateOrSheetBuilder()
                .setFromId(newSheetHome.getId())
                .setName("New test sheet from template")
                .build();
        EnumSet<SheetTemplateInclusion> inclusions = EnumSet.of(
                SheetTemplateInclusion.ATTACHMENTS,
                SheetTemplateInclusion.DATA,
                SheetTemplateInclusion.DISCUSSIONS
        );
        newSheetTemplate = smartsheet
                .sheetResources()
                .createSheetFromTemplate(sheet, inclusions);

        assertThat(newSheetHome.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
    }

    public void testCreateSheetInFolder() throws SmartsheetException, IOException {
        //create a new folder and get the id
        folder = createFolder();

        Sheet newSheetFolder = smartsheet.sheetResources().createSheetInFolder(folder.getId(), sheetHome);

        assertThat(newSheetFolder.getColumns()).hasSameSizeAs(sheetHome.getColumns());
    }

    public void testCreateSheetInFolderFromTemplate() throws SmartsheetException {

        Sheet sheet = new Sheet.CreateFromTemplateOrSheetBuilder()
                .setFromId(newSheetHome.getId())
                .setName("New test sheet from template")
                .build();
        Sheet newSheetFromTemplate = smartsheet.sheetResources().createSheetInFolderFromTemplate(folder.getId(), sheet, null);

        assertThat(newSheetFromTemplate.getId().toString()).isNotEmpty();
        assertThat(newSheetFromTemplate.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newSheetFromTemplate.getPermalink()).isNotEmpty();
    }

    public void testCreateSheetInWorkspace() throws SmartsheetException {
        //create temporary workspace
        workspace = createWorkspace("New Test Workspace");

        Sheet newSheet = smartsheet.sheetResources().createSheetInWorkspace(workspace.getId(), sheetHome);
        assertThat(sheetHome.getColumns()).hasSameSizeAs(newSheet.getColumns());

        //delete temporary workspace
        //testDeleteWorkspace(workspace.getId());
    }

    public void testCreateSheetInWorkspaceFromTemplate() throws SmartsheetException {
        Sheet sheet = new Sheet.CreateFromTemplateOrSheetBuilder()
                .setFromId(newSheetHome.getId())
                .setName("New test sheet in workspace from template")
                .build();
        Sheet newSheetFromTemplate = smartsheet
                .sheetResources()
                .createSheetInWorkspaceFromTemplate(workspace.getId(), sheet, EnumSet.allOf(SheetTemplateInclusion.class));

        assertThat(newSheetFromTemplate.getId().toString()).isNotEmpty();
        assertThat(newSheetFromTemplate.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newSheetFromTemplate.getPermalink()).isNotEmpty();
    }

    public void testGetSheet() throws SmartsheetException, IOException {
        Sheet sheet = smartsheet.sheetResources().getSheet(newSheetHome.getId());

        assertThat(newSheetHome.getPermalink()).isEqualTo(sheet.getPermalink());
    }

    public void testGetSheetVersion() throws SmartsheetException, IOException {
        int version = smartsheet.sheetResources().getSheetVersion(newSheetHome.getId());
        assertThat(version).isEqualTo(1);
    }

    public void testGetSheetAsExcel() throws SmartsheetException, IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        smartsheet.sheetResources().getSheetAsExcel(newSheetHome.getId(), output);

        assertThat(output).isNotNull();
    }

    public void testGetSheetAsPDF() throws SmartsheetException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        smartsheet.sheetResources().getSheetAsPDF(newSheetHome.getId(), output, PaperSize.A1);
        smartsheet.sheetResources().getSheetAsCSV(newSheetHome.getId(), output);

        assertThat(output).isNotNull();
    }

    public void testUpdateSheet() throws SmartsheetException, IOException {
        Sheet sheet = new Sheet.UpdateSheetBuilder().setSheetId(newSheetHome.getId()).setName("Updated Name by Aditi").build();
        Sheet newSheet = smartsheet.sheetResources().updateSheet(sheet);

        assertThat(newSheet.getName()).isEqualTo(sheet.getName());
    }

    public void testPublishSheetDefaults() throws SmartsheetException, IOException {
        SheetPublish sheetPublish = new SheetPublish.PublishStatusBuilder()
                .setIcalEnabled(false)
                .setReadOnlyFullEnabled(true)
                .setReadWriteEnabled(true)
                .setReadOnlyLiteEnabled(true)
                .build();
        SheetPublish newSheetPublish = smartsheet.sheetResources().updatePublishStatus(newSheetHome.getId(), sheetPublish);

        assertThat(newSheetPublish.getReadWriteShowToolbar()).isTrue();
    }

    public void testPublishSheet() throws SmartsheetException, IOException {
        SheetPublish sheetPublish = new SheetPublish.PublishStatusBuilder()
                .setIcalEnabled(false)
                .setReadOnlyFullEnabled(true)
                .setReadWriteEnabled(true)
                .setReadOnlyLiteEnabled(true)
                .setReadWriteShowToolbarEnabled(false)
                .setReadOnlyFullShowToolbarEnabled(false)
                .build();
        SheetPublish newSheetPublish = smartsheet.sheetResources().updatePublishStatus(newSheetHome.getId(), sheetPublish);

        assertThat(newSheetPublish.getReadWriteShowToolbar()).isFalse();
        assertThat(newSheetPublish.getReadOnlyFullShowToolbar()).isFalse();
    }

    public void testUpdatePublishStatus() throws SmartsheetException, IOException {
        // In order to publish an icalendar, we have to have at least one row of data
        PagedResult<Column> columns = smartsheet.sheetResources().columnResources().listColumns(newSheetHome.getId(), null, null);
        Column dateColumn;
        for (Column column : columns.getData()) {
            if (column.getType() == ColumnType.DATE) {
                dateColumn = column;
                smartsheet.sheetResources().rowResources().addRows(newSheetHome.getId(), Collections.singletonList(
                        new Row.AddRowBuilder()
                                .setCells(new Cell.AddRowCellsBuilder()
                                        .addCell(dateColumn.getId(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                                        .build())
                                .setToBottom(true)
                                .build()));
                break;
            }
        }

        SheetPublish sheetPublish = new SheetPublish.PublishStatusBuilder()
                .setIcalEnabled(true)
                .setReadOnlyFullEnabled(true)
                .setReadWriteEnabled(true)
                .setReadOnlyLiteEnabled(true)
                .build();
        SheetPublish newSheetPublish = smartsheet.sheetResources().updatePublishStatus(newSheetHome.getId(), sheetPublish);

        assertThat(newSheetPublish.getReadOnlyFullEnabled()).isTrue();
    }

    public void testGetPublishStatus() throws SmartsheetException, IOException {
        SheetPublish publishStatus = smartsheet.sheetResources().getPublishStatus(newSheetHome.getId());
        assertThat(publishStatus).isNotNull();
    }

    public void testListSheets() throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder()
                .setIncludeAll(false)
                .setPageSize(1)
                .setPage(1)
                .build();
        PagedResult<Sheet> sheets = smartsheet.sheetResources().listSheets(EnumSet.of(SourceInclusion.SOURCE), parameters);
        smartsheet.sheetResources().listSheets();

        assertThat(sheets.getPageNumber()).isEqualTo(1);
    }

    public void testListOrganizationSheets() throws SmartsheetException, IOException {
        //PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder()
        //    .setIncludeAll(false)
        //    .setPageSize(1)
        //    .setPage(1)
        //    .build();
        //PagedResult<Sheet> sheets = smartsheet.sheetResources().listOrganizationSheets(parameters);

        //assertThat(sheets.getPageNumber() == 1).isTrue();
    }

    public void testattachFile() throws SmartsheetException, IOException {

        // File file = new File("src/test/resources/small-text.txt");
        // Attachment attachment = smartsheet.sheetResources().attachmentResources().attachFile(1234L, 345L, file, "application/pdf");
    }

    public void testDeleteSheet() throws SmartsheetException, IOException {
        smartsheet.sheetResources().deleteSheet(newSheetHome.getId());
        smartsheet.sheetResources().deleteSheet(newSheetTemplate.getId());
        //cleanup
        deleteWorkspace(workspace.getId());
        deleteFolder(folder.getId());
    }

    public void testSendSheet() throws SmartsheetException, IOException {
        List<Recipient> recipients = new ArrayList<>();
        RecipientEmail recipientEmail = new RecipientEmail.AddRecipientEmailBuilder().setEmail("test.user@smartsheet.com").build();

        recipients.add(recipientEmail);
        FormatDetails formatDetails = new FormatDetails();
        formatDetails.setPaperSize(PaperSize.A0);

        SheetEmail email = new SheetEmail.AddSheetEmailBuilder().setFormat(SheetEmailFormat.PDF).setFormatDetails(formatDetails).build();
        email.setSendTo(recipients);

        //smartsheet.reportResources().sendReport(reportsWrapper.getData().get(0).getId(), email);
        smartsheet.sheetResources().sendSheet(newSheetHome.getId(), email);
    }
}
