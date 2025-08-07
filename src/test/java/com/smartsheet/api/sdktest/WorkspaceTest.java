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
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.Sight;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.ChildrenResourceType;
import com.smartsheet.api.models.enums.GetWorkspaceChildrenInclusion;
import com.smartsheet.api.models.enums.GetWorkspaceMetadataInclusion;
import com.smartsheet.api.models.enums.SourceType;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

class WorkspaceTest {

    @Test
    void getWorkspaceMetadata_NoParams() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Workspace Metadata - No Params");
        Workspace workspace = ss.workspaceResources().getWorkspaceMetadata(123L, null);
        assertThat(workspace.getName()).isEqualTo("Sample Workspace");
        assertThat(workspace.getId()).isEqualTo(123L);
        assertThat(workspace.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
    }

    @Test
    void getWorkspaceMetadata_IncludeSource() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Workspace Metadata - Include Source");
        Workspace workspace = ss.workspaceResources().getWorkspaceMetadata(123L,
                EnumSet.of(GetWorkspaceMetadataInclusion.SOURCE));
        assertThat(workspace.getName()).isEqualTo("Sample Workspace");
        assertThat(workspace.getId()).isEqualTo(123L);
        assertThat(workspace.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(workspace.getSource()).isNotNull();
        assertThat(workspace.getSource().getId()).isEqualTo(999L);
        assertThat(workspace.getSource().getType()).isEqualTo(SourceType.WORKSPACE);

    }

    @Test
    void getWorkspaceChildren_NoParams() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Workspace Children - No Params");
        TokenPaginatedResult<Object> response = ss.workspaceResources().getWorkspaceChildren(123L, null, null, null, null);
        assertThat(response.getData()).hasSize(4);

        // Check that we have different resource types
        Object firstItem = response.getData().get(0);
        assertThat(firstItem).isInstanceOf(Folder.class);
        Folder folder = (Folder) firstItem;
        assertThat(folder.getName()).isEqualTo("Project Folder");
        assertThat(folder.getId()).isEqualTo(456L);

        Object secondItem = response.getData().get(1);
        assertThat(secondItem).isInstanceOf(Sheet.class);
        Sheet sheet = (Sheet) secondItem;
        assertThat(sheet.getName()).isEqualTo("Budget Sheet");
        assertThat(sheet.getId()).isEqualTo(789L);
        assertThat(sheet.getAccessLevel()).isEqualTo(AccessLevel.EDITOR);

        Object thirdItem = response.getData().get(2);
        assertThat(thirdItem).isInstanceOf(Sight.class);
        Sight sight = (Sight) thirdItem;
        assertThat(sight.getName()).isEqualTo("Dashboard Overview");
        assertThat(sight.getId()).isEqualTo(321L);
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);

        Object fourthItem = response.getData().get(3);
        assertThat(fourthItem).isInstanceOf(Report.class);
        Report report = (Report) fourthItem;
        assertThat(report.getName()).isEqualTo("Monthly Report");
        assertThat(report.getId()).isEqualTo(654L);
        assertThat(report.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);

    }

    @Test
    void getWorkspaceChildren_FilterSheetsAndFolders() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Workspace Children - Filter Sheets and Folders");
        TokenPaginatedResult<Object> response = ss.workspaceResources().getWorkspaceChildren(123L,
                EnumSet.of(ChildrenResourceType.SHEETS, ChildrenResourceType.FOLDERS), null, null, null);
        assertThat(response.getData()).hasSize(3);

        // Verify specific items
        Object firstItem = response.getData().get(0);
        assertThat(firstItem).isInstanceOf(Folder.class);
        Folder folder = (Folder) firstItem;
        assertThat(folder.getName()).isEqualTo("Project Folder");
        assertThat(folder.getId()).isEqualTo(456L);

        Object secondItem = response.getData().get(1);
        assertThat(secondItem).isInstanceOf(Sheet.class);
        Sheet firstSheet = (Sheet) secondItem;
        assertThat(firstSheet.getName()).isEqualTo("Budget Sheet");
        assertThat(firstSheet.getId()).isEqualTo(789L);
        assertThat(firstSheet.getAccessLevel()).isEqualTo(AccessLevel.EDITOR);

        Object thirdItem = response.getData().get(2);
        assertThat(thirdItem).isInstanceOf(Sheet.class);
        Sheet secondSheet = (Sheet) thirdItem;
        assertThat(secondSheet.getName()).isEqualTo("Project Timeline");
        assertThat(secondSheet.getId()).isEqualTo(1234L);
        assertThat(secondSheet.getAccessLevel()).isEqualTo(AccessLevel.EDITOR);
    }

    @Test
    void getWorkspaceChildren_IncludeSourceAndOwnerInfo() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Workspace Children - Include Source and OwnerInfo");
        TokenPaginatedResult<Object> response = ss.workspaceResources().getWorkspaceChildren(123L,
                null, EnumSet.of(GetWorkspaceChildrenInclusion.SOURCE, GetWorkspaceChildrenInclusion.OWNERINFO), null,
                null);
        assertThat(response.getData()).hasSize(4);

        // Check folder with source
        Object firstItem = response.getData().get(0);
        assertThat(firstItem).isInstanceOf(Folder.class);
        Folder folder = (Folder) firstItem;
        assertThat(folder.getName()).isEqualTo("Project Folder");
        assertThat(folder.getSource()).isNotNull();
        assertThat(folder.getSource().getId()).isEqualTo(888L);
        assertThat(folder.getSource().getType()).isEqualTo(SourceType.FOLDER);

        // Check sheet with source and owner info
        Object secondItem = response.getData().get(1);
        assertThat(secondItem).isInstanceOf(Sheet.class);
        Sheet sheet = (Sheet) secondItem;
        assertThat(sheet.getName()).isEqualTo("Budget Sheet");
        assertThat(sheet.getAccessLevel()).isEqualTo(AccessLevel.EDITOR);
        assertThat(sheet.getSource()).isNotNull();
        assertThat(sheet.getSource().getId()).isEqualTo(777L);
        assertThat(sheet.getSource().getType()).isEqualTo(SourceType.SHEET);
        assertThat(sheet.getOwner()).isEqualTo("john.doe@example.com");
        assertThat(sheet.getOwnerId()).isEqualTo(1001L);

        // Check sight with source
        Object thirdItem = response.getData().get(2);
        assertThat(thirdItem).isInstanceOf(Sight.class);
        Sight sight = (Sight) thirdItem;
        assertThat(sight.getName()).isEqualTo("Dashboard Overview");
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sight.getSource()).isNotNull();
        assertThat(sight.getSource().getId()).isEqualTo(666L);
        assertThat(sight.getSource().getType()).isEqualTo(SourceType.SIGHT);

        // Check report with source
        Object fourthItem = response.getData().get(3);
        assertThat(fourthItem).isInstanceOf(Report.class);
        Report report = (Report) fourthItem;
        assertThat(report.getName()).isEqualTo("Monthly Report");
        assertThat(report.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(report.getSource()).isNotNull();
        assertThat(report.getSource().getId()).isEqualTo(555L);
        assertThat(report.getSource().getType()).isEqualTo(SourceType.REPORT);
    }

    @Test
    void getWorkspaceChildren_MaxItemsAndLastKey() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Workspace Children - MaxItems and LastKey");
        TokenPaginatedResult<Object> response = ss.workspaceResources().getWorkspaceChildren(123L, null, null,
                "aslkjf4wlkta4n4900sjfklf499sjwlk4356lkj", 1000);
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getLastKey()).isEqualTo("xvmnw4mnx8v9wriot20574xvnjoqt4iuhnow490");
        assertThat(response.hasMorePages()).isTrue();

        // Verify the returned item
        Object firstItem = response.getData().get(0);
        assertThat(firstItem).isInstanceOf(Sheet.class);
        Sheet sheet = (Sheet) firstItem;
        assertThat(sheet.getName()).isEqualTo("Budget Sheet");
        assertThat(sheet.getId()).isEqualTo(789L);
        assertThat(sheet.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=*****************");
        assertThat(sheet.getAccessLevel()).isEqualTo(AccessLevel.EDITOR);
    }
}
