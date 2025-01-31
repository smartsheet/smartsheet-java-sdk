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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder()
                .setIncludeAll(false)
                .setPageSize(1)
                .setPage(1)
                .build();
        PagedResult<Sheet> sheets = sheetResource.listSheets(EnumSet.of(SourceInclusion.SOURCE), parameters, null);

        assertThat(sheets.getPageNumber()).isEqualTo(1);
        assertThat(sheets.getPageSize()).isEqualTo(100);
        assertThat(sheets.getTotalPages()).isEqualTo(1);
        assertThat(sheets.getTotalCount()).isEqualTo(2);
        assertThat(sheets.getData()).hasSize(2);
        assertThat(sheets.getData().get(0).getName()).isEqualTo("sheet 1");
        assertThat(sheets.getData().get(1).getName()).isEqualTo("sheet 2");
    }

    @Test
    void testListOrganizationSheets() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listSheets.json"));
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Sheet> sheets = sheetResource.listOrganizationSheets(parameters);
        assertThat(sheets.getData()).hasSize(2);
    }

    @Test
    void testGetSheet() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/getSheet.json"));
        Sheet sheet = sheetResource.getSheet(123123L);
        assertThat(sheet.getColumns()).hasSize(9);
        assertThat(sheet.getRows()).isEmpty();

        Source source = sheet.getSource();
        assertThat(source.getId()).isNotNull();
        assertThat(source.getType()).isNotNull();

        Set<Long> rowIds = new HashSet<>();
        rowIds.add(123456789L);
        rowIds.add(987654321L);

        sheet = sheetResource.getSheet(
                123123L,
                EnumSet.allOf(SheetInclusion.class),
                EnumSet.allOf(ObjectExclusion.class),
                rowIds,
                null,
                null,
                1,
                1
        );
        assertThat(sheet.getColumns()).hasSize(9);
        assertThat(sheet.getRows()).isEmpty();
    }

    @Test
    void testGetSheetWithFormat() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/getSheetWithFormat.json"));
        Sheet sheet = sheetResource.getSheet(123123L);

        assertThat(sheet.getColumnByIndex(0).getFormat()).isNotNull();
        assertThat(sheet.getColumnByIndex(0).getFormat().getVerticalAlignment()).isEqualTo(VerticalAlignment.TOP);
    }

    @Test
    void testGetSheetAsExcel() throws SmartsheetException, IOException {
        File file = new File("src/test/resources/getExcel.xls");
        server.setResponseBody(file);
        server.setContentType("application/vnd.ms-excel");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sheetResource.getSheetAsExcel(1234L, output);

        assertThat(output).isNotNull();
        assertThat(output.toByteArray()).isNotEmpty();

        //byte[] original = IOUtils.toByteArray(new FileReader(file));
        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
        assertThat(output.toByteArray()).hasSameSizeAs(data);
    }

    @Test
    void testGetSheetAsPDF() throws SmartsheetException, IOException {

        File file = new File("src/test/resources/getPDF.pdf");
        server.setResponseBody(file);
        server.setContentType("application/pdf");

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        sheetResource.getSheetAsPDF(1234L, output, null);

        assertThat(output).isNotNull();
        assertThat(output.toByteArray()).isNotEmpty();
        assertThat(output.toByteArray()).hasSize(107906);

        //test a larger PDF
        file = new File("src/test/resources/large_sheet.pdf");
        server.setResponseBody(file);
        server.setContentType("application/pdf");
        output = new ByteArrayOutputStream();
        sheetResource.getSheetAsPDF(1234L, output, PaperSize.LEGAL);
        assertThat(output).isNotNull();
        assertThat(output.toByteArray()).isNotEmpty();
        assertThat(output.toByteArray()).hasSize(936995);
    }

    @Test
    void testCreateSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheet.json"));

        ArrayList<Column> list = new ArrayList<>();
        Column col1 = new Column.AddColumnToSheetBuilder()
                .setTitle("Test Column 1")
                .setType(ColumnType.TEXT_NUMBER)
                .setPrimary(true)
                .build();
        list.add(col1);
        Column col2 = new Column.AddColumnToSheetBuilder()
                .setTitle("Test Column 2")
                .setType(ColumnType.TEXT_NUMBER)
                .setPrimary(false)
                .build();
        list.add(col2);

        Sheet sheet = new Sheet.CreateSheetBuilder().setName("New Test Sheet").setColumns(list).build();
        Sheet newSheet = sheetResource.createSheet(sheet);

        assertThat(newSheet.getColumns()).hasSize(2);
    }

    @Test
    void testCreateSheetFromTemplate() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/createSheetFromExisting.json"));

        Sheet sheet = new Sheet.CreateFromTemplateOrSheetBuilder()
                .setFromId(7960873114331012L)
                .setName("New test sheet from template")
                .build();
        Sheet newSheet = sheetResource.createSheetFromTemplate(sheet, EnumSet.allOf(SheetTemplateInclusion.class));

        assertThat(newSheet.getId().longValue()).isEqualTo(7960873114331012L);
        assertThat(newSheet.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newSheet.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=lbKEF1UakfTNJTZ5XkpxWg");
    }

    @Test
    void testCreateSheetInFolder() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheet.json"));

        ArrayList<Column> list = new ArrayList<>();
        Column col = new Column.AddColumnToSheetBuilder().setTitle("column1").setType(ColumnType.TEXT_NUMBER).setPrimary(true).build();
        list.add(col);
        col = new Column.AddColumnToSheetBuilder().setTitle("column2").setType(ColumnType.TEXT_NUMBER).setPrimary(false).build();
        col.setId(4049365800118148L);
        list.add(col);

        Sheet sheet = new Sheet.CreateSheetBuilder().setName("NEW TEST SHEET").setColumns(list).build();
        Sheet newSheet = sheetResource.createSheetInFolder(12345L, sheet);

        assertThat(newSheet.getColumns()).hasSize(2);
        assertThat(newSheet.getColumnByIndex(1)).isEqualTo(col);
        assertThat(newSheet.getColumnByIndex(0)).isNotEqualTo(col);
        assertThat((new Sheet()).getColumnByIndex(100)).isNull();
        assertThat(newSheet.getColumnById(4049365800118148L)).isEqualTo(col);
        assertThat(newSheet.getColumnById(4032471613368196L)).isNotEqualTo(col);
        assertThat((new Sheet()).getColumnById(100)).isNull();
    }

    @Test
    void testCreateSheetInFolderFromTemplate() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/createSheetFromExisting.json"));

        Sheet sheet = new Sheet().setFromId(2906571706525572L);
        Sheet newSheet = sheetResource.createSheetInFolderFromTemplate(1234L, sheet,
                EnumSet.allOf(SheetTemplateInclusion.class));

        assertThat(newSheet.getId().toString()).isNotBlank();
        assertThat(newSheet.getId()).isEqualTo(7960873114331012L);
        assertThat(newSheet.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newSheet.getPermalink()).isNotBlank();

        // really just checking to see if this is null-safe, for now
        Sheet newSheetWithNoIncludesApplied = sheetResource.createSheetInFolderFromTemplate(1234L, sheet, null);
        assertThat(newSheetWithNoIncludesApplied.getId().toString()).isNotBlank();
        assertThat(newSheetWithNoIncludesApplied.getId()).isEqualTo(7960873114331012L);
        assertThat(newSheetWithNoIncludesApplied.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newSheetWithNoIncludesApplied.getPermalink()).isNotBlank();
    }

    @Test
    void testCreateSheetInWorkspace() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheet.json"));

        ArrayList<Column> list = new ArrayList<>();
        Column col = new Column.AddColumnToSheetBuilder().setTitle("column1").setType(ColumnType.TEXT_NUMBER).setPrimary(true).build();
        list.add(col);
        col = new Column.AddColumnToSheetBuilder().setTitle("column2").setType(ColumnType.TEXT_NUMBER).setPrimary(false).build();
        col.setId(4049365800118148L);
        list.add(col);

        Sheet sheet = new Sheet.CreateSheetBuilder().setName("NEW TEST SHEET").setColumns(list).build();
        Sheet newSheet = sheetResource.createSheetInWorkspace(1234L, sheet);
        assertThat(newSheet.getColumns()).hasSize(2);
    }

    @Test
    void testCreateSheetInWorkspaceFromTemplate() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createSheetFromExisting.json"));

        Sheet sheet = new Sheet().setFromId(2906571706525572L);
        Sheet newSheet = sheetResource.createSheetInWorkspaceFromTemplate(1234L, sheet,
                EnumSet.allOf(SheetTemplateInclusion.class));

        assertThat(newSheet.getId().longValue()).isEqualTo(7960873114331012L);
        assertThat(newSheet.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newSheet.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=lbKEF1UakfTNJTZ5XkpxWg");

        // really just checking to see if this is null-safe, for now
        newSheet = sheetResource.createSheetInWorkspaceFromTemplate(1234L, sheet, null);
        assertThat(newSheet.getId().longValue()).isEqualTo(7960873114331012L);
        assertThat(newSheet.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newSheet.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=lbKEF1UakfTNJTZ5XkpxWg");
    }

    @Test
    void testDeleteSheet() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteSheet.json"));
        assertThatCode(() -> sheetResource.deleteSheet(1234L)).doesNotThrowAnyException();
    }

    @Test
    void testUpdateSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateSheet.json"));

        Sheet sheet = new Sheet.UpdateSheetBuilder().setName("new name").build();
        Sheet newSheet = sheetResource.updateSheet(sheet);

        assertThat(newSheet.getName()).isEqualTo(sheet.getName());
    }

    @Test
    void testGetSheetVersion() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSheetVersion.json"));
        int version = sheetResource.getSheetVersion(1234L);
        assertThat(version).isEqualTo(1);
    }

    @Test
    void testSendSheet() throws IOException {
        server.setResponseBody(new File("src/test/resources/sendEmails.json"));

        List<Recipient> recipients = new ArrayList<>();
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

        assertThatCode(() -> sheetResource.sendSheet(1234L, email)).doesNotThrowAnyException();
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

        assertThat(publishStatus.getReadOnlyLiteEnabled()).isTrue();
        assertThat(publishStatus.getReadOnlyFullEnabled()).isTrue();
        assertThat(publishStatus.getReadWriteEnabled()).isTrue();
        assertThat(publishStatus.getIcalEnabled()).isTrue();
        assertThat(publishStatus.getReadOnlyLiteUrl()).isEqualTo("https://publish.smartsheet.com/6d35fa6c99334d4892f9591cf6065");
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

        assertThat(newPublish.getIcalEnabled()).isTrue();
        assertThat(newPublish.getReadOnlyFullEnabled()).isTrue();
        assertThat(newPublish.getReadOnlyLiteEnabled()).isTrue();
        assertThat(newPublish.getReadWriteEnabled()).isTrue();
        assertThat(newPublish.getReadOnlyLiteUrl()).isEqualTo("http://somedomain.com");

    }

    @Test
    void testCopySheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/copySheet.json"));
        ContainerDestination containerDestination = new ContainerDestination();
        containerDestination.setDestinationType(DestinationType.FOLDER);

        Sheet sheet = sheetResource.copySheet(123L, containerDestination, null);
        assertThat(sheet.getName()).isEqualTo("newSheetName");
    }

    @Test
    void testMoveSheet() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/moveSheet.json"));
        ContainerDestination containerDestination = new ContainerDestination();
        containerDestination.setDestinationType(DestinationType.FOLDER);

        Sheet sheet = sheetResource.moveSheet(123L, containerDestination);
        assertThat(sheet.getId().longValue()).isEqualTo(4583173393803140L);
        assertThat(sheet.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(sheet.getPermalink()).isNotBlank();
    }

    @Test
    void testGetSheetAsCSV() throws SmartsheetException, IOException {
        File file = new File("src/test/resources/getCsv.csv");
        server.setResponseBody(file);
        server.setContentType("text/csv");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sheetResource.getSheetAsExcel(1234L, output);

        assertThat(output).isNotNull();
        assertThat(output.toByteArray()).isNotEmpty();

        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
        assertThat(output.toByteArray()).hasSameSizeAs(data);
    }

    @Test
    void testCreateUpdateRequest() throws Exception {
        server.setResponseBody(new File("src/test/resources/createUpdateRequest.json"));

        List<Recipient> recipients = new ArrayList<>();
        MultiRowEmail multiRowEmail = new MultiRowEmail();
        multiRowEmail.setSendTo(recipients);

        UpdateRequest updateRequest = sheetResource.createUpdateRequest(123L, multiRowEmail);

        assertThat(updateRequest.getId()).isNotNull();
    }

    @Test
    void testSortSheet() throws Exception {
        server.setResponseBody(new File("src/test/resources/getSheet.json"));
        SortSpecifier specifier = new SortSpecifier();
        SortCriterion criterion = new SortCriterion();
        criterion.setColumnId(1234L);
        criterion.setDirection(SortDirection.DESCENDING);
        List<SortCriterion> criteria = new ArrayList<>();
        criteria.add(criterion);
        specifier.setSortCriteria(criteria);
        Sheet sheet = sheetResource.sortSheet(123L, specifier);
        assertThat(sheet.getColumns()).hasSize(9);
        assertThat(sheet.getRows()).isEmpty();
        assertThat(sheet.getId()).isNotNull();
        assertThat(sheet.getId().longValue()).isEqualTo(295123319904012164L);
        assertThat(sheet.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(sheet.getPermalink()).isNotBlank();
    }
}
