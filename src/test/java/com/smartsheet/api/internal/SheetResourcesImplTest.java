package com.smartsheet.api.internal;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2014 Smartsheet
 * %%
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
 * %[license]
 */

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.FormatDetails;
import com.smartsheet.api.models.MultiRowEmail;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.RecipientGroup;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.SheetEmail;
import com.smartsheet.api.models.SheetPublish;
import com.smartsheet.api.models.SortCriterion;
import com.smartsheet.api.models.SortSpecifier;
import com.smartsheet.api.models.Source;
import com.smartsheet.api.models.UpdateRequest;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.DestinationType;
import com.smartsheet.api.models.enums.ObjectExclusion;
import com.smartsheet.api.models.enums.PaperSize;
import com.smartsheet.api.models.enums.SheetEmailFormat;
import com.smartsheet.api.models.enums.SheetInclusion;
import com.smartsheet.api.models.enums.SheetTemplateInclusion;
import com.smartsheet.api.models.enums.SortDirection;
import com.smartsheet.api.models.enums.SourceInclusion;
import com.smartsheet.api.models.format.VerticalAlignment;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class SheetResourcesImplTest extends ResourcesImplBase {
    private SheetResourcesImpl sheetResource;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a folder resource
        sheetResource = new SheetResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Test
    void testListSheets() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listSheets.json"));
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(false).setPageSize(1).setPage(1).build();
        PagedResult<Sheet> sheets = sheetResource.listSheets(EnumSet.of(SourceInclusion.SOURCE), parameters, null);

        assertTrue(sheets.getPageNumber() == 1);
        assertTrue(sheets.getPageSize() == 100);
        assertTrue(sheets.getTotalPages() == 1);
        assertTrue(sheets.getTotalCount() == 2);
        assertTrue(sheets.getData().size() == 2);
        assertEquals("sheet 1", sheets.getData().get(0).getName());
        assertEquals("sheet 2", sheets.getData().get(1).getName());
    }

    @Test
    void testListOrganizationSheets() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listSheets.json"));
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Sheet> sheets = sheetResource.listOrganizationSheets(parameters);
        assertEquals(2, sheets.getData().size());
    }

    @Test
    void testGetSheet() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/getSheet.json"));
        Sheet sheet = sheetResource.getSheet(123123L, null, null, null, null, null, null, null);
        assertEquals(9,sheet.getColumns().size());
        assertEquals(0,sheet.getRows().size());

        Source source = sheet.getSource();
        assertNotNull(source.getId());
        assertNotNull(source.getType());

        Set<Long> rowIds = new HashSet<Long>();
        rowIds.add(123456789L);
        rowIds.add(987654321L);

        sheet = sheetResource.getSheet(123123L, EnumSet.allOf(SheetInclusion.class), EnumSet.allOf(ObjectExclusion.class), rowIds, null, null, 1, 1);
        assertEquals(9, sheet.getColumns().size());
        assertEquals(0,sheet.getRows().size());
    }

    @Test
    void testGetSheetWithFormat() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/getSheetWithFormat.json"));
        Sheet sheet = sheetResource.getSheet(123123L, null, null, null, null, null, null, null);

        assertNotNull(sheet.getColumnByIndex(0).getFormat());
        assertEquals(VerticalAlignment.TOP, sheet.getColumnByIndex(0).getFormat().getVerticalAlignment());
    }

    @Test
    void testGetSheetAsExcel() throws SmartsheetException, IOException {
        File file = new File("src/test/resources/getExcel.xls");
        server.setResponseBody(file);
        server.setContentType("application/vnd.ms-excel");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sheetResource.getSheetAsExcel(1234L, output);

        assertNotNull(output);
        assertTrue(output.toByteArray().length > 0);

        //byte[] original = IOUtils.toByteArray(new FileReader(file));
        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
        assertEquals(data.length, output.toByteArray().length);
    }

    @Test
    void testGetSheetAsPDF() throws SmartsheetException, IOException {

        File file = new File("src/test/resources/getPDF.pdf");
        server.setResponseBody(file);
        server.setContentType("application/pdf");

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        sheetResource.getSheetAsPDF(1234L, output, null);

        assertNotNull(output, "Downloaded PDF is null.");
        assertTrue(output.toByteArray().length > 0, "Downloaded PDF is empty.");
        assertEquals(107906,output.toByteArray().length, "Downloaded PDF does not match the original size.");

        //test a larger PDF
        file = new File("src/test/resources/large_sheet.pdf");
        server.setResponseBody(file);
        server.setContentType("application/pdf");
        output = new ByteArrayOutputStream();
        sheetResource.getSheetAsPDF(1234L, output, PaperSize.LEGAL);
        assertNotNull(output, "Downloaded PDF is null.");
        assertTrue(output.toByteArray().length > 0, "Downloaded PDF is empty.");
        assertEquals(936995,output.toByteArray().length, "Downloaded PDF does not match the original size.");
    }

    @Test
    void testCreateSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheet.json"));

        ArrayList<Column> list = new ArrayList<Column>();
        Column col1 = new Column.AddColumnToSheetBuilder().setTitle("Test Column 1").setType(ColumnType.TEXT_NUMBER).setPrimary(true).build();
        list.add(col1);
        Column col2 = new Column.AddColumnToSheetBuilder().setTitle("Test Column 2").setType(ColumnType.TEXT_NUMBER).setPrimary(false).build();
        col2.setPrimary(false);
        list.add(col2);

        Sheet sheet = new Sheet.CreateSheetBuilder().setName("New Test Sheet").setColumns(list).build();
        Sheet newSheet = sheetResource.createSheet(sheet);

        if (newSheet.getColumns().size() != 2) {
            fail("Issue creating a sheet");
        }
    }

    @Test
    void testCreateSheetFromTemplate() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/createSheetFromExisting.json"));

        Sheet sheet = new Sheet.CreateFromTemplateOrSheetBuilder().setFromId(7960873114331012L).setName("New test sheet from template").build();
        Sheet newSheet = sheetResource.createSheetFromTemplate(sheet, EnumSet.allOf(SheetTemplateInclusion.class));

        assertEquals(7960873114331012L, newSheet.getId().longValue());
        assertEquals(AccessLevel.OWNER, newSheet.getAccessLevel());
        assertEquals("https://app.smartsheet.com/b/home?lx=lbKEF1UakfTNJTZ5XkpxWg", newSheet.getPermalink());

    }

    @Test
    void testCreateSheetInFolder() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheet.json"));

        ArrayList<Column> list = new ArrayList<Column>();
        Column col = new Column.AddColumnToSheetBuilder().setTitle("column1").setType(ColumnType.TEXT_NUMBER).setPrimary(true).build();
        list.add(col);
        col = new Column.AddColumnToSheetBuilder().setTitle("column2").setType(ColumnType.TEXT_NUMBER).setPrimary(false).build();
        col.setId(4049365800118148L);
        list.add(col);

        Sheet sheet = new Sheet.CreateSheetBuilder().setName("NEW TEST SHEET").setColumns(list).build();
        Sheet newSheet = sheetResource.createSheetInFolder(12345L, sheet);

        assertEquals(2, newSheet.getColumns().size());
        assertEquals(col,newSheet.getColumnByIndex(1));
        assertNotEquals(col, newSheet.getColumnByIndex(0));
        assertNull((new Sheet()).getColumnByIndex(100));
        assertEquals(col, newSheet.getColumnById(4049365800118148L));
        assertNotEquals(col,newSheet.getColumnById(4032471613368196L));
        assertNull((new Sheet()).getColumnById(100));
    }

    @Test
    void testCreateSheetInFolderFromTemplate() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/createSheetFromExisting.json"));

        Sheet sheet = new Sheet();
        sheet.setFromId(2906571706525572L);
        Sheet newSheet = sheetResource.createSheetInFolderFromTemplate(1234L, sheet,
                EnumSet.allOf(SheetTemplateInclusion.class));

        if (newSheet.getId().toString().isEmpty() || newSheet.getAccessLevel() != AccessLevel.OWNER
                || newSheet.getPermalink().toString().isEmpty()) {
            fail("Sheet not correctly copied");
        }

        newSheet = sheetResource.createSheetInFolderFromTemplate(1234L, sheet, null);
    }

    @Test
    void testCreateSheetInWorkspace() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheet.json"));

        ArrayList<Column> list = new ArrayList<Column>();
        Column col = new Column.AddColumnToSheetBuilder().setTitle("column1").setType(ColumnType.TEXT_NUMBER).setPrimary(true).build();
        list.add(col);
        col = new Column.AddColumnToSheetBuilder().setTitle("column2").setType(ColumnType.TEXT_NUMBER).setPrimary(false).build();
        col.setId(4049365800118148L);
        list.add(col);

        Sheet sheet = new Sheet.CreateSheetBuilder().setName("NEW TEST SHEET").setColumns(list).build();
        Sheet newSheet = sheetResource.createSheetInWorkspace(1234L, sheet);
        assertEquals(2, newSheet.getColumns().size());
    }

    @Test
    void testCreateSheetInWorkspaceFromTemplate() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheetFromExisting.json"));

        Sheet sheet = new Sheet();
        sheet.setFromId(2906571706525572L);
        Sheet newSheet = sheetResource.createSheetInWorkspaceFromTemplate(1234L, sheet,
                EnumSet.allOf(SheetTemplateInclusion.class));

        assertEquals(7960873114331012L, newSheet.getId().longValue());
        assertEquals(AccessLevel.OWNER, newSheet.getAccessLevel());
        assertEquals("https://app.smartsheet.com/b/home?lx=lbKEF1UakfTNJTZ5XkpxWg", newSheet.getPermalink());

        newSheet = sheetResource.createSheetInWorkspaceFromTemplate(1234L, sheet, null);
    }

    @Test
    void testDeleteSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteSheet.json"));
        sheetResource.deleteSheet(1234L);
    }

    @Test
    void testUpdateSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateSheet.json"));

        Sheet sheet = new Sheet.UpdateSheetBuilder().setName("new name").build();
        Sheet newSheet = sheetResource.updateSheet(sheet);

        assertEquals(sheet.getName(), newSheet.getName(), "Sheet update (rename) failed.");
    }

    @Test
    void testGetSheetVersion() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSheetVersion.json"));
        int version = sheetResource.getSheetVersion(1234L);
        if (version != 1) {
            fail("Issue getting sheet version");
        }
    }

    @Test
    void testSendSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/sendEmails.json"));

        List<Recipient> recipients = new ArrayList<Recipient>();
        RecipientEmail recipientEmail = new RecipientEmail();
        recipientEmail.setEmail("johndoe@smartsheet.com");

        RecipientGroup recipientGroup = new RecipientGroup();
        recipientGroup.setGroupId(123456789L);

        recipients.add(recipientGroup);
        recipients.add(recipientEmail);

        SheetEmail email = new SheetEmail();
        email.setFormat(SheetEmailFormat.PDF);
        FormatDetails format = new FormatDetails();
        format.setPaperSize(PaperSize.A0);
        email.setFormatDetails(format);
        email.setSendTo(recipients);

        sheetResource.sendSheet(1234L, email);
    }

    @Test
    void testShares() {
        sheetResource.shareResources();
    }

    @Test
    void testRows() {
        sheetResource.rowResources();
    }

    @Test
    void testColumns() {
        sheetResource.columnResources();
    }

    @Test
    void testDiscussions() {
        sheetResource.discussionResources();
    }

    @Test
    void testGetPublishStatus() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getPublishStatus.json"));

        SheetPublish publishStatus = sheetResource.getPublishStatus(1234L);

        assertTrue(publishStatus.getReadOnlyLiteEnabled());
        assertTrue(publishStatus.getReadOnlyFullEnabled());
        assertTrue(publishStatus.getReadWriteEnabled());
        assertTrue(publishStatus.getIcalEnabled());
        assertEquals("https://publish.smartsheet.com/6d35fa6c99334d4892f9591cf6065", publishStatus.getReadOnlyLiteUrl());
    }

    @Test
    void testUpdatePublishStatus() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/setPublishStatus.json"));

        SheetPublish publish = new SheetPublish();
        publish.setIcalEnabled(true);
        publish.setReadOnlyFullEnabled(true);
        publish.setReadOnlyLiteEnabled(true);
        publish.setReadWriteEnabled(true);
        publish.setReadWriteEnabled(true);
        publish.setReadOnlyLiteUrl("http://somedomain.com");
        SheetPublish newPublish = sheetResource.updatePublishStatus(1234L, publish);

        assertTrue(newPublish.getIcalEnabled());
        assertTrue(newPublish.getReadOnlyFullEnabled());
        assertTrue(newPublish.getReadOnlyLiteEnabled());
        assertTrue(newPublish.getReadWriteEnabled());
        assertEquals("http://somedomain.com", newPublish.getReadOnlyLiteUrl());

    }

    @Test
    void testCopySheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/copySheet.json"));
        ContainerDestination containerDestination = new ContainerDestination();
        containerDestination.setDestinationType(DestinationType.FOLDER);

        Sheet sheet = sheetResource.copySheet(123L, containerDestination, null);
        assertEquals("newSheetName", sheet.getName());
    }

    @Test
    void testMoveSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/moveSheet.json"));
        ContainerDestination containerDestination = new ContainerDestination();
        containerDestination.setDestinationType(DestinationType.FOLDER);

        Sheet sheet = sheetResource.moveSheet(123L, containerDestination);
    }

    @Test
    void testGetSheetAsCSV() throws SmartsheetException, IOException {
        File file = new File("src/test/resources/getCsv.csv");
        server.setResponseBody(file);
        server.setContentType("text/csv");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sheetResource.getSheetAsExcel(1234L, output);

        assertNotNull(output);
        assertTrue(output.toByteArray().length > 0);

        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
        assertEquals(data.length, output.toByteArray().length);
    }

    @Test
    void testCreateUpdateRequest() throws Exception {
        server.setResponseBody(new File("src/test/resources/createUpdateRequest.json"));

        List<Recipient> recipients = new ArrayList<Recipient>();
        MultiRowEmail multiRowEmail = new MultiRowEmail();
        multiRowEmail.setSendTo(recipients);

        UpdateRequest updateRequest = sheetResource.createUpdateRequest(123L, multiRowEmail);
    }

    @Test
    void testSortSheet() throws Exception {
        server.setResponseBody(new File("src/test/resources/getSheet.json"));
        SortSpecifier specifier = new SortSpecifier();
        SortCriterion criterion = new SortCriterion();
        criterion.setColumnId(1234L);
        criterion.setDirection(SortDirection.DESCENDING);
        List<SortCriterion> criteria = new ArrayList<SortCriterion>();
        criteria.add(criterion);
        specifier.setSortCriteria(criteria);
        Sheet sheet = sheetResource.sortSheet(123L, specifier);
    }
}
