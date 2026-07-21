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

import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.CreateReportRequest;
import com.smartsheet.api.models.CreateReportResult;
import com.smartsheet.api.models.PathLeaf;
import com.smartsheet.api.models.ReportPathNode;
import com.smartsheet.api.models.FormatDetails;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Proof;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.RecipientGroup;
import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.ReportColumn;
import com.smartsheet.api.models.ReportDestination;
import com.smartsheet.api.models.ReportScopeInclusion;
import com.smartsheet.api.models.SheetEmail;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.PaperSize;
import com.smartsheet.api.models.enums.ReportAssetType;
import com.smartsheet.api.models.enums.ReportDestinationType;
import com.smartsheet.api.models.enums.ReportInclusion;
import com.smartsheet.api.models.enums.ProofType;
import com.smartsheet.api.models.enums.SheetEmailFormat;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class ReportResourcesImplTest extends ResourcesImplBase {

    private ReportResourcesImpl reportResources;

    @BeforeEach
    public void setUp() throws Exception {
        reportResources = new ReportResourcesImpl(new SmartsheetImpl("http://localhost:9090/2.0/",
                "accessToken", new DefaultHttpClient(), serializer));

    }

    @Test
    void testGetReport() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getReport.json"));
        EnumSet<ReportInclusion> reportInclusions = EnumSet.of(
                ReportInclusion.ATTACHMENTS,
                ReportInclusion.DISCUSSIONS,
                ReportInclusion.PROOFS);
        Report report = reportResources.getReport(4583173393803140L, reportInclusions, 1, 1);
        assertThat(report.getPermalink())
                .isEqualTo("https://app.smartsheet.com/b/home?lx=pWNSDH9itjBXxBzFmyf-5w");
        assertThat(report.getColumns().get(0).getVirtualId()).isEqualTo(4583173393803140L);

        Proof proof = report.getRows().get(0).getProof();
        assertThat(proof).isNotNull();
        assertThat(proof.getId()).isEqualTo(8834704717089156L);
        assertThat(proof.getName()).isEqualTo("Design mockup");
        assertThat(proof.getType()).isEqualTo(ProofType.IMAGE);
        assertThat(proof.getIsCompleted()).isFalse();
    }

    @Test
    void testSendSheet() throws Exception {
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
        assertThatCode(() -> reportResources.sendReport(1234L, email)).doesNotThrowAnyException();

    }

    @Test
    void testListReports() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listReports.json"));
        PaginationParameters pagination = new PaginationParameters(true, null, null);
        PagedResult<Report> reportsWrapper = reportResources.listReports(pagination, null);

        assertThat(reportsWrapper.getTotalPages()).isEqualTo(1);
        assertThat(reportsWrapper.getData().get(0).getName()).isEqualTo("r1");
        assertThat(reportsWrapper.getData().get(1).getName()).isEqualTo("r2");
        assertThat(reportsWrapper.getData().get(0).getId()).isEqualTo(6761305928427396L);
    }

    @Test
    void testGetReportAsExcel() throws SmartsheetException, IOException {
        File file = new File("src/test/resources/getExcel.xls");
        server.setResponseBody(file);
        server.setContentType("application/vnd.ms-excel");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        reportResources.getReportAsExcel(4583173393803140L, output);
        assertThat(output).isNotNull();

        assertThat(output.toByteArray()).isNotEmpty();

        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
        assertThat(output.toByteArray()).hasSameSizeAs(data);
    }

    @Test
    void testGetReportAsCsv() throws SmartsheetException, IOException {
        File file = new File("src/test/resources/getExcel.xls");
        server.setResponseBody(file);
        server.setContentType("text/csv");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        reportResources.getReportAsExcel(4583173393803140L, output);
        assertThat(output).isNotNull();

        assertThat(output.toByteArray()).isNotEmpty();

        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
        assertThat(output.toByteArray()).hasSameSizeAs(data);
    }

    @Test
    void testDeleteReport() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteReport.json"));
        assertThatCode(() -> reportResources.deleteReport(1122334L)).doesNotThrowAnyException();
    }

    @Test
    void testAddReportColumns() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/addReportColumns.json"));

        List<ReportColumn> columnsToAdd = new ArrayList<>();

        ReportColumn column1 = new ReportColumn();
        column1.setTitle("Item selected");
        column1.setType(ColumnType.CHECKBOX);
        column1.setIndex(4);

        ReportColumn column2 = new ReportColumn();
        column2.setTitle("Sheet name");
        column2.setType(ColumnType.TEXT_NUMBER);
        column2.setIndex(5);
        column2.setSheetNameColumn(true);

        columnsToAdd.add(column1);
        columnsToAdd.add(column2);

        List<ReportColumn> addedColumns = reportResources.addReportColumns(4583173393803140L, columnsToAdd);

        assertThat(addedColumns).isNotNull();
        assertThat(addedColumns).hasSize(2);
        assertThat(addedColumns.get(0).getVirtualId()).isEqualTo(12345L);
        assertThat(addedColumns.get(0).getTitle()).isEqualTo("Item selected");
        assertThat(addedColumns.get(0).getType()).isEqualTo(ColumnType.CHECKBOX);
        assertThat(addedColumns.get(0).getIndex()).isEqualTo(4);
        assertThat(addedColumns.get(1).getVirtualId()).isEqualTo(12346L);
        assertThat(addedColumns.get(1).getTitle()).isEqualTo("Sheet name");
        assertThat(addedColumns.get(1).getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(addedColumns.get(1).getSheetNameColumn()).isTrue();
    }

    @Test
    void testCreateReport() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createReport.json"));

        ReportDestination destination = new ReportDestination();
        destination.setDestinationId(12345L);
        destination.setDestinationType(ReportDestinationType.FOLDER);

        List<ReportColumn> columns = new ArrayList<>();
        ReportColumn column = new ReportColumn();
        column.setTitle("Task Name");
        column.setType(ColumnType.TEXT_NUMBER);
        column.setPrimary(true);
        column.setIndex(0);
        columns.add(column);

        List<ReportScopeInclusion> scope = new ArrayList<>();
        ReportScopeInclusion scopeItem = new ReportScopeInclusion();
        scopeItem.setAssetType(ReportAssetType.SHEET);
        scopeItem.setAssetId(67890L);
        scope.add(scopeItem);

        CreateReportRequest request = new CreateReportRequest();
        request.setName("Q2 Earnings");
        request.setDestination(destination);
        request.setColumns(columns);
        request.setScope(scope);
        request.setIsSummaryReport(false);

        CreateReportResult result = reportResources.createReport(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(987654321L);
        assertThat(result.getName()).isEqualTo("Q2 Earnings");
        assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(result.getPermalink())
                .isEqualTo("https://app.smartsheet.com/reports/c8gJxw87cXpRCvCC5PPw6jFhFRrf5r8PxCrxvW21");
        assertThat(result.getIsSummaryReport()).isFalse();

        // Verify columns are returned
        assertThat(result.getColumns()).isNotNull();
        assertThat(result.getColumns()).hasSize(1);
        assertThat(result.getColumns().get(0).getVirtualId()).isEqualTo(1234567890123456L);
        assertThat(result.getColumns().get(0).getTitle()).isEqualTo("Primary column");
        assertThat(result.getColumns().get(0).getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(result.getColumns().get(0).getPrimary()).isTrue();
    }

    @Test
    void testCreateReportNullRequest() {
        assertThatThrownBy(() -> reportResources.createReport(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testGetReportPath() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getReportPath.json"));

        ReportPathNode result = reportResources.getReportPath(1234567890L);

        // workspace root
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4509918431602564L);
        assertThat(result.getName()).isEqualTo("Sample Workspace");
        assertThat(result.getPermalink()).isEqualTo("https://app.smartsheet.com/workspaces/mock_workspace_id");
        assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        // level-1 folder
        assertThat(result.getFolders()).hasSize(1);
        ReportPathNode level1 = result.getFolders().get(0);
        assertThat(level1.getId()).isEqualTo(1234567890123456L);
        assertThat(level1.getName()).isEqualTo("Project Plans");
        assertThat(level1.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/1234567890123456");
        // level-2 folder (contains the leaf report)
        assertThat(level1.getFolders()).hasSize(1);
        ReportPathNode level2 = level1.getFolders().get(0);
        assertThat(level2.getId()).isEqualTo(2345678901234567L);
        assertThat(level2.getName()).isEqualTo("Project Plans Subfolder");
        assertThat(level2.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/2345678901234567");
        // leaf report
        assertThat(level2.getReports()).hasSize(1);
        PathLeaf report = level2.getReports().get(0);
        assertThat(report.getId()).isEqualTo(3456789012345678L);
        assertThat(report.getName()).isEqualTo("Project Report");
        assertThat(report.getPermalink()).isEqualTo("https://app.smartsheet.com/reports/3456789012345678");
        assertThat(report.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(report.getCreatedAt()).isEqualTo(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        assertThat(report.getModifiedAt()).isEqualTo(ZonedDateTime.parse("2024-06-01T00:00:00Z"));
    }

    @Test
    void testGetReportPath_getReport() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getReportPath.json"));

        ReportPathNode result = reportResources.getReportPath(1234567890L);
        PathLeaf leaf = result.getLeafReport();

        assertThat(leaf).isNotNull();
        assertThat(leaf.getName()).isEqualTo("Project Report");
        assertThat(leaf.getId()).isEqualTo(3456789012345678L);
        assertThat(leaf.getPermalink()).isEqualTo("https://app.smartsheet.com/reports/3456789012345678");
        assertThat(leaf.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(leaf.getCreatedAt()).isEqualTo(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        assertThat(leaf.getModifiedAt()).isEqualTo(ZonedDateTime.parse("2024-06-01T00:00:00Z"));
    }

    @Test
    void testGetReportPath_getReportPath() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getReportPath.json"));

        ReportPathNode result = reportResources.getReportPath(1234567890L);

        assertThat(result.getLeafReportPath())
                .isEqualTo("/Sample Workspace/Project Plans/Project Plans Subfolder/Project Report");
    }

    @Test
    void testGetReportPath_404_throwsResourceNotFoundException() throws IOException {
        server.setStatus(404);
        server.setResponseBody(new File("src/test/resources/notFoundError.json"));

        assertThatThrownBy(() -> reportResources.getReportPath(1234567890L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetReportPath_500_throwsInvalidRequestException() throws IOException {
        server.setStatus(500);
        server.setResponseBody(new File("src/test/resources/notFoundError.json"));

        assertThatThrownBy(() -> reportResources.getReportPath(1234567890L))
                .isInstanceOf(InvalidRequestException.class);
    }
}
