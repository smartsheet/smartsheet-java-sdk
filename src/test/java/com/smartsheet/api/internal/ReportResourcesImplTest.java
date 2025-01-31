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
import com.smartsheet.api.models.FormatDetails;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.RecipientGroup;
import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.SheetEmail;
import com.smartsheet.api.models.enums.PaperSize;
import com.smartsheet.api.models.enums.ReportInclusion;
import com.smartsheet.api.models.enums.SheetEmailFormat;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        EnumSet<ReportInclusion> reportInclusions = EnumSet.of(ReportInclusion.ATTACHMENTS, ReportInclusion.DISCUSSIONS);
        Report report = reportResources.getReport(4583173393803140L, reportInclusions, 1, 1);
        assertThat(report.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=pWNSDH9itjBXxBzFmyf-5w");
        assertThat(report.getColumns().get(0).getVirtualId()).isEqualTo(4583173393803140L);
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
}
