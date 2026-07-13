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
import com.smartsheet.api.models.enums.ChildrenResourceType;
import com.smartsheet.api.models.enums.GetFolderChildrenInclusion;
import com.smartsheet.api.models.enums.GetFolderMetadataInclusion;
import com.smartsheet.api.models.enums.SourceType;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartsheet.api.models.Template;

class FolderTest {

    @Test
    void getFolderMetadata_NoParams() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Folder Metadata - No Params");
        Folder folder = ss.folderResources().getFolderMetadata(456L, null);
        assertThat(folder.getName()).isEqualTo("Project Folder");
        assertThat(folder.getId()).isEqualTo(456L);
    }

    @Test
    void getFolderMetadata_IncludeSource() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Folder Metadata - Include Source");
        Folder folder = ss.folderResources().getFolderMetadata(456L,
                EnumSet.of(GetFolderMetadataInclusion.SOURCE));
        assertThat(folder.getName()).isEqualTo("Project Folder");
        assertThat(folder.getId()).isEqualTo(456L);
        assertThat(folder.getSource()).isNotNull();
        assertThat(folder.getSource().getId()).isEqualTo(888L);
        assertThat(folder.getSource().getType()).isEqualTo(SourceType.FOLDER);
    }

    @Test
    void getFolderChildren_NoParams() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Folder Children - No Params");
        TokenPaginatedResult<Object> response = ss.folderResources().getFolderChildren(456L, null, null, null, null);
        assertThat(response.getData()).hasSize(4);

        // Check that we have different resource types
        Object firstItem = response.getData().get(0);
        assertThat(firstItem).isInstanceOf(Folder.class);
        Folder folder = (Folder) firstItem;
        assertThat(folder.getName()).isEqualTo("Subfolder");
        assertThat(folder.getId()).isEqualTo(987L);

        Object secondItem = response.getData().get(1);
        assertThat(secondItem).isInstanceOf(Sheet.class);
        Sheet sheet = (Sheet) secondItem;
        assertThat(sheet.getName()).isEqualTo("Task List");
        assertThat(sheet.getId()).isEqualTo(234L);

        Object thirdItem = response.getData().get(2);
        assertThat(thirdItem).isInstanceOf(Sight.class);
        Sight sight = (Sight) thirdItem;
        assertThat(sight.getName()).isEqualTo("Project Dashboard");
        assertThat(sight.getId()).isEqualTo(567L);

        Object fourthItem = response.getData().get(3);
        assertThat(fourthItem).isInstanceOf(Report.class);
        Report report = (Report) fourthItem;
        assertThat(report.getName()).isEqualTo("Status Report");
        assertThat(report.getId()).isEqualTo(890L);

        Object fifthItem = response.getData().get(4);
        assertThat(fifthItem).isInstanceOf(Template.class);
        Template template = (Template) fifthItem;
        assertThat(template.getName()).isEqualTo("Project Template");
        assertThat(template.getId()).isEqualTo(990L);
    }

    @Test
    void getFolderChildren_FilterSightsAndReports() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Folder Children - Filter Sights and Reports");
        TokenPaginatedResult<Object> response = ss.folderResources().getFolderChildren(456L,
                EnumSet.of(ChildrenResourceType.REPORTS, ChildrenResourceType.SIGHTS), null, null, null);
        assertThat(response.getData()).hasSize(3);

        // Verify specific items
        Sight firstSight = (Sight) response.getData().get(0);
        assertThat(firstSight.getName()).isEqualTo("Project Dashboard");
        assertThat(firstSight.getId()).isEqualTo(567L);

        Sight secondSight = (Sight) response.getData().get(1);
        assertThat(secondSight.getName()).isEqualTo("Executive Summary");
        assertThat(secondSight.getId()).isEqualTo(1567L);

        Report report = (Report) response.getData().get(2);
        assertThat(report.getName()).isEqualTo("Status Report");
        assertThat(report.getId()).isEqualTo(890L);
    }

    @Test
    void getFolderChildren_IncludeSourceAndOwnerInfo() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Folder Children - Include Source and OwnerInfo");
        TokenPaginatedResult<Object> response = ss.folderResources().getFolderChildren(456L,
                null, EnumSet.of(GetFolderChildrenInclusion.SOURCE, GetFolderChildrenInclusion.OWNERINFO), null, null);
        assertThat(response.getData()).hasSize(4);

        // Check folder with source
        Object firstItem = response.getData().get(0);
        assertThat(firstItem).isInstanceOf(Folder.class);
        Folder folder = (Folder) firstItem;
        assertThat(folder.getName()).isEqualTo("Subfolder");
        assertThat(folder.getSource()).isNotNull();
        assertThat(folder.getSource().getId()).isEqualTo(444L);
        assertThat(folder.getSource().getType()).isEqualTo(SourceType.FOLDER);

        // Check sheet with source and owner info
        Object secondItem = response.getData().get(1);
        assertThat(secondItem).isInstanceOf(Sheet.class);
        Sheet sheet = (Sheet) secondItem;
        assertThat(sheet.getName()).isEqualTo("Task List");
        assertThat(sheet.getSource()).isNotNull();
        assertThat(sheet.getSource().getId()).isEqualTo(333L);
        assertThat(sheet.getSource().getType()).isEqualTo(SourceType.SHEET);
        assertThat(sheet.getOwner()).isEqualTo("jane.smith@example.com");
        assertThat(sheet.getOwnerId()).isEqualTo(2002L);

        // Check sight with source
        Object thirdItem = response.getData().get(2);
        assertThat(thirdItem).isInstanceOf(Sight.class);
        Sight sight = (Sight) thirdItem;
        assertThat(sight.getName()).isEqualTo("Project Dashboard");
        assertThat(sight.getSource()).isNotNull();
        assertThat(sight.getSource().getId()).isEqualTo(222L);
        assertThat(sight.getSource().getType()).isEqualTo(SourceType.SIGHT);

        // Check report with source
        Object fourthItem = response.getData().get(3);
        assertThat(fourthItem).isInstanceOf(Report.class);
        Report report = (Report) fourthItem;
        assertThat(report.getName()).isEqualTo("Status Report");
        assertThat(report.getSource()).isNotNull();
        assertThat(report.getSource().getId()).isEqualTo(111L);
        assertThat(report.getSource().getType()).isEqualTo(SourceType.REPORT);
    }

    @Test
    void getFolderChildren_MaxItemsAndLastKey() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Folder Children - MaxItems and LastKey");
        TokenPaginatedResult<Object> response = ss.folderResources().getFolderChildren(456L, null, null,
                "aslkjf4wlkta4n4900sjfklf499sjwlk4356lkj", 100);
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getLastKey()).isEqualTo("xvmnw4mnx8v9wriot20574xvnjoqt4iuhnow490");
        assertThat(response.hasMorePages()).isTrue();

        // Verify the returned item
        Object firstItem = response.getData().get(0);
        assertThat(firstItem).isInstanceOf(Sight.class);
        Sight sight = (Sight) firstItem;
        assertThat(sight.getName()).isEqualTo("Project Dashboard");
        assertThat(sight.getId()).isEqualTo(567L);
        assertThat(sight.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=*****************");
    }
}
