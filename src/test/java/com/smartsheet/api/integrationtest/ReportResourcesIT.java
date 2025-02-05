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
import com.smartsheet.api.models.FormatDetails;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.ReportPublish;
import com.smartsheet.api.models.SheetEmail;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.PaperSize;
import com.smartsheet.api.models.enums.ReportInclusion;
import com.smartsheet.api.models.enums.SheetEmailFormat;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    private Long reportId;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();

        PagedResult<Report> reports = smartsheet.reportResources().listReports(null, null);
        for (Report report : reports.getData()) {
            if (report.getAccessLevel() == AccessLevel.OWNER) {
                reportId = report.getId();
                break;
            }
        }
        if (reportId == null) {
            throw new Exception("No valid reports found for the configured account. This test requires at least one owned report");
        }

    }

    @Test
    void testReportMethods() throws SmartsheetException, IOException {
        testListReports();
        testGetReport();
        testGetReportAsExcel();
        testGetReportAsCsv();
        testSendSheet();
        testPublishReport();
        testUnpublishReport();
    }

    public void testListReports() throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder()
                .setIncludeAll(true).build();

        PagedResult<Report> reports = smartsheet.reportResources().listReports(null, null);
        assertThat(reports).isNotNull();
        assertThat(reports.getData()).isNotEmpty();
    }

    public void testGetReport() throws SmartsheetException, IOException {
        EnumSet<ReportInclusion> reportInclusions = EnumSet.of(ReportInclusion.ATTACHMENTS, ReportInclusion.DISCUSSIONS);
        Report report = smartsheet.reportResources().getReport(reportId, reportInclusions, 1, 1);
        smartsheet.reportResources().getReport(reportId, null, null, null);
        assertThat(report).isNotNull();
    }

    public void testGetReportAsExcel() throws SmartsheetException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        smartsheet.reportResources().getReportAsExcel(reportId, output);
        assertThat(output).isNotNull();
    }

    public void testGetReportAsCsv() throws SmartsheetException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        smartsheet.reportResources().getReportAsCsv(reportId, output);
        assertThat(output).isNotNull();
    }

    public void testSendSheet() throws SmartsheetException, IOException {
        List<Recipient> recipients = new ArrayList<>();
        RecipientEmail recipientEmail = new RecipientEmail.AddRecipientEmailBuilder()
                .setEmail("aditi.nioding@smartsheet.com").build();
        //RecipientGroup recipientGroup = new RecipientGroup.AddRecipientGroupBuilder().setGroupId(1234L).build();

        //List<Recipient> recipients = Arrays.asList(recipientEmail, recipientGroup);
        recipients.add(recipientEmail);
        //recipients.add(recipientGroup);

        FormatDetails formatDetails = new FormatDetails();
        formatDetails.setPaperSize(PaperSize.A0);

        SheetEmail email = new SheetEmail.AddSheetEmailBuilder()
                .setFormat(SheetEmailFormat.PDF)
                .setFormatDetails(formatDetails)
                .setSubject("Check this report out!")
                .setMessage("something")
                .setCcMe(false)
                .setSendTo(recipients)
                .setFormatDetails(formatDetails)
                .build();

        smartsheet.reportResources().sendReport(reportId, email);
        //smartsheet.reportResources().sendReport(8623082916079492L, email);
    }

    public void testPublishReport() throws SmartsheetException, IOException {
        ReportPublish reportPublish = new ReportPublish();
        reportPublish.setReadOnlyFullEnabled(true);
        reportPublish.setReadOnlyFullShowToolbar(false);

        ReportPublish newReportPublish = smartsheet.reportResources().updatePublishStatus(reportId, reportPublish);
        assertThat(newReportPublish.getReadOnlyFullEnabled()).isTrue();
        assertThat(newReportPublish.getReadOnlyFullShowToolbar()).isFalse();
    }

    public void testUnpublishReport() throws SmartsheetException, IOException {
        ReportPublish reportPublish = new ReportPublish();
        reportPublish.setReadOnlyFullEnabled(false);
        reportPublish.setReadOnlyFullShowToolbar(false);

        ReportPublish newReportPublish = smartsheet.reportResources().updatePublishStatus(reportId, reportPublish);
        assertThat(newReportPublish.getReadOnlyFullEnabled()).isFalse();
    }
}
