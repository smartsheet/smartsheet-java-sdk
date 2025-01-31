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
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.DestinationType;
import com.smartsheet.api.models.enums.FolderCopyInclusion;
import com.smartsheet.api.models.enums.FolderRemapExclusion;
import com.smartsheet.api.models.enums.SourceInclusion;
import com.smartsheet.api.resilience4j.RetryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

public class FolderResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    Folder newFolderHome;
    Folder newFolder;
    Folder newFolderWorkspace;
    Workspace workspace;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testFolderMethods() throws IOException, SmartsheetException {
        testCreateFolderInHome();
        testListFoldersInHome();
        testCreateFolderInFolder();
        testListFoldersInFolder();
        testCreateFolderInWorkspace();
        testListFoldersInWorkspace();
        testUpdateFolder();
        testGetFolder();
        testCopyFolder();
        testMoveFolder();
        testDeleteFolder();
    }

    public void testCreateFolderInHome() throws SmartsheetException {
        Folder folder = new Folder.CreateFolderBuilder().setName("New Folder in Home By Aditi").build();

        newFolderHome = smartsheet.homeResources().folderResources().createFolder(folder);
        assertThat(newFolderHome.getName()).isEqualTo("New Folder in Home By Aditi");
    }

    public void testCreateFolderInFolder() throws SmartsheetException {
        Folder folder = new Folder.CreateFolderBuilder().setName("New Folder in Folder By Aditi").build();
        newFolder = smartsheet.folderResources().createFolder(newFolderHome.getId(), folder);

        Folder folder1 = new Folder.CreateFolderBuilder().setName("New Folder 2 in Folder By Aditi").build();
        smartsheet.folderResources().createFolder(newFolder.getId(), folder1);

        assertThat(newFolder.getName()).isEqualTo(folder.getName());

        // FIXME this IT is flaky because of delay in the creation of a new folder being visible to all servers
        // possibly add a post-creation check loop with exponential backoff to verify folder is visible to later tests?
    }

    public void testCreateFolderInWorkspace() throws SmartsheetException {
        //calling helper method
        workspace = createWorkspace("New Workspace By Aditi");

        Folder folder = new Folder.CreateFolderBuilder().setName("New Folder in Workspace By Aditi").build();
        newFolderWorkspace = smartsheet.workspaceResources().folderResources().createFolder(workspace.getId(), folder);
        assertThat(folder.getName()).isEqualTo("New Folder in Workspace By Aditi");
    }

    public void testUpdateFolder() throws SmartsheetException {
        Folder folder = new Folder.UpdateFolderBuilder().setName("Updated Name By Aditi").setId(newFolderHome.getId()).build();
        Folder resultFolder = smartsheet.folderResources().updateFolder(folder);

        assertThat(resultFolder.getName()).isEqualTo(folder.getName());
    }

    public void testListFoldersInFolder() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters(true, 1, 1);
        PagedResult<Folder> foldersWrapper = RetryUtil.callWithRetry(
                () -> smartsheet.folderResources().listFolders(newFolder.getId(), parameters));

        assertThat(foldersWrapper.getTotalCount()).isEqualTo(1);
    }

    public void testListFoldersInHome() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters(true, 1, 1);
        PagedResult<Folder> foldersWrapper = smartsheet.homeResources().folderResources().listFolders(parameters);

        assertThat(foldersWrapper.getTotalCount()).isNotNull();
    }

    public void testListFoldersInWorkspace() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters(true, 1, 1);
        PagedResult<Folder> foldersWrapper = smartsheet.workspaceResources().folderResources().listFolders(workspace.getId(), parameters);

        assertThat(foldersWrapper.getTotalCount()).isNotNull();
    }

    public void testGetFolder() throws SmartsheetException {
        Folder folder = smartsheet.folderResources().getFolder(newFolder.getId(), EnumSet.of(SourceInclusion.SOURCE));
        smartsheet.folderResources().getFolder(newFolder.getId(), null);

        // Verify results
        assertThat(folder.getName()).isEqualTo("New Folder in Folder By Aditi");
    }

    public void testCopyFolder() throws SmartsheetException {
        ContainerDestination destination = new ContainerDestination.AddContainerDestinationBuilder()
                .setDestinationType(DestinationType.FOLDER)
                .setDestinationId(newFolderWorkspace.getId())
                .setNewName("New Copied folder")
                .build();
        Folder folder1 = smartsheet.folderResources().copyFolder(newFolderHome.getId(), destination, null, null);
        Folder folder2 = smartsheet
                .folderResources()
                .copyFolder(
                        newFolderHome.getId(),
                        destination,
                        EnumSet.of(FolderCopyInclusion.ALL),
                        EnumSet.of(FolderRemapExclusion.CELLLINKS)
                );
        smartsheet.folderResources().deleteFolder(folder1.getId());
        smartsheet.folderResources().deleteFolder(folder2.getId());
    }

    public void testMoveFolder() throws SmartsheetException {
        ContainerDestination destination = new ContainerDestination.AddContainerDestinationBuilder()
                .setDestinationType(DestinationType.FOLDER)
                .setDestinationId(newFolderWorkspace.getId())
                .build();
        Folder folder1 = smartsheet.folderResources().moveFolder(newFolderHome.getId(), destination);
        smartsheet.folderResources().deleteFolder(folder1.getId());
    }

    public void testDeleteFolder() throws SmartsheetException, IOException {
        //smartsheet.folderResources().deleteFolder(newFolderHome.getId());
        deleteWorkspace(workspace.getId());
    }
}
