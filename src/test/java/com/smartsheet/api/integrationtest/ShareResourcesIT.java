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
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.Share;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.ReportInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ShareResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    PagedResult<Report> reportsWrapper;
    long reportId;
    long sheetId;
    long workspaceId;
    List<Share> workspaceShares = new ArrayList<>();
    List<Share> reportShares = new ArrayList<>();
    List<Share> sheetShares = new ArrayList<>();

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testShareMethods() throws SmartsheetException, IOException {
        //Commenting report sharing testing as report ID needs to be provided
        //testReportShareTo();
        testSheetShareTo();
        testWorkspaceShareTo();
        //testGetReportShare();
        testGetSheetShare();
        testGetWorkspaceShare();
        testListShares();
        testUpdateShare();
        testDeleteShare();
    }

    public void testReportShareTo() throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        reportsWrapper = smartsheet.reportResources().listReports(parameters);

        if (reportsWrapper != null) {
            Report report = smartsheet
                    .reportResources()
                    .getReport(
                            reportsWrapper.getData().get(0).getId(),
                            EnumSet.of(ReportInclusion.ATTACHMENTS, ReportInclusion.DISCUSSIONS),
                            1,
                            1
                    );
            assertThat(report).isNotNull();
        }

        Share share = new Share.CreateUserShareBuilder()
                .setEmailAddress("jane.doe@smartsheet.com")
                .setAccessLevel(AccessLevel.EDITOR)
                .build();
        List<Share> reportShares = Collections.singletonList(share);

        Share anotherShare = new Share.CreateUserShareBuilder()
                .setEmailAddress("jane.doe@smartsheet.com")
                .setAccessLevel(AccessLevel.VIEWER)
                .build();
        reportShares.add(anotherShare);

        if (reportsWrapper != null) {
            this.reportShares = smartsheet.reportResources().shareResources().shareTo(8623082916079492L, reportShares, true);
            assertThat(reportShares).isNotNull();
        }

        reportId = reportsWrapper.getData().get(0).getId();
    }

    public void testSheetShareTo() throws SmartsheetException, IOException {
        //create sheet
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());
        sheetId = sheet.getId();

        sheetShares = Arrays.asList(
                new Share.CreateUserShareBuilder().setEmailAddress("anioding@smartsheet.com").setAccessLevel(AccessLevel.VIEWER).build(),
                new Share.CreateUserShareBuilder().setEmailAddress("jane.doe@smartsheet.com").setAccessLevel(AccessLevel.VIEWER).build()
        );

        sheetShares = smartsheet.sheetResources().shareResources().shareTo(sheet.getId(), sheetShares, true);
        assertThat(sheetShares).isNotNull();
    }

    public void testWorkspaceShareTo() throws SmartsheetException, IOException {
        //create sheet
        Workspace workspace = smartsheet
                .workspaceResources()
                .createWorkspace(new Workspace.UpdateWorkspaceBuilder().setName("New Test Workspace").build());
        workspaceId = workspace.getId();

        workspaceShares.add(
                new Share.CreateUserShareBuilder().setEmailAddress("anioding@smartsheet.com").setAccessLevel(AccessLevel.VIEWER).build()
        );
        workspaceShares.add(
                new Share.CreateUserShareBuilder().setEmailAddress("jane.doe@smartsheet.com").setAccessLevel(AccessLevel.VIEWER).build()
        );

        workspaceShares = smartsheet.workspaceResources().shareResources().shareTo(workspace.getId(), workspaceShares, true);
        assertThat(workspaceShares).isNotNull();
    }

    public void testGetReportShare() throws SmartsheetException, IOException {
        Share share = smartsheet.reportResources().shareResources().getShare(8623082916079492L, reportShares.get(0).getId());

        assertThat(share).isNotNull();
    }

    public void testGetSheetShare() throws SmartsheetException, IOException {
        Share share = smartsheet.sheetResources().shareResources().getShare(sheetId, sheetShares.get(0).getId());

        assertThat(share).isNotNull();
    }

    public void testGetWorkspaceShare() throws SmartsheetException, IOException {
        Share share = smartsheet.workspaceResources().shareResources().getShare(workspaceId, workspaceShares.get(0).getId());

        assertThat(share).isNotNull();
    }

    public void testListShares() throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

        //PagedResult<Share> reportShares = smartsheet.reportResources().shareResources().listShares(8623082916079492L, parameters);
        PagedResult<Share> sheetShares = smartsheet.sheetResources().shareResources().listShares(sheetId, parameters);
        PagedResult<Share> workspaceShares = smartsheet.workspaceResources().shareResources().listShares(workspaceId, parameters);
        assertThat(sheetShares).isNotNull();
        assertThat(workspaceShares).isNotNull();
    }

    public void testUpdateShare() throws SmartsheetException, IOException {
        //Share sheet
        Share shareSheet = new Share.UpdateShareBuilder().setAccessLevel(AccessLevel.EDITOR).setShareId(sheetShares.get(1).getId()).build();

        Share newShareSheet = smartsheet.sheetResources().shareResources().updateShare(sheetId, shareSheet);
        assertThat(newShareSheet.getAccessLevel()).isEqualTo(shareSheet.getAccessLevel());

        //Share workspace
        Share shareWorkspace = new Share.UpdateShareBuilder()
                .setAccessLevel(AccessLevel.EDITOR)
                .setShareId(workspaceShares.get(1).getId())
                .build();
        Share newShareWorkspace = smartsheet.workspaceResources().shareResources().updateShare(workspaceId, shareWorkspace);

        assertThat(newShareWorkspace.getAccessLevel()).isEqualTo(shareWorkspace.getAccessLevel());
    }

    public void testDeleteShare() throws SmartsheetException, IOException {
        smartsheet.workspaceResources().shareResources().deleteShare(workspaceId, workspaceShares.get(1).getId());
        smartsheet.sheetResources().shareResources().deleteShare(sheetId, sheetShares.get(1).getId());
        //smartsheet.reportResources().shareResources().deleteShare(reportId, reportShares.get(1).getId());

        //cleanup
        deleteSheet(sheetId);
        deleteWorkspace(workspaceId);
    }
}
