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
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Share;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.WorkspaceCopyInclusion;
import com.smartsheet.api.models.enums.WorkspaceRemapExclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkspaceResourcesIT extends ITResourcesImpl {

    Smartsheet smartsheet;
    long workspaceId;
    Workspace workspace;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testWorkspaceMethods() throws IOException, SmartsheetException {
        testCreateWorkspace();
        testCopyWorkspace();
        testShareWorkspace();
        testGetWorkspace();
        testListWorkspaces();
        testUpdateWorkspace();
        testDeleteWorkspace();
    }

    public void testCreateWorkspace() throws SmartsheetException {
        workspace = new Workspace.UpdateWorkspaceBuilder().setName("New Test Workspace").build();
        Workspace newWorkspace = smartsheet.workspaceResources().createWorkspace(workspace);

        workspaceId = newWorkspace.getId();
        assertThat(newWorkspace.getName()).isEqualTo("New Test Workspace");
        assertThat(newWorkspace.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
    }

    public void testCopyWorkspace() throws SmartsheetException, IOException {

        ContainerDestination destination = new ContainerDestination.AddContainerDestinationBuilder()
                .setNewName("New Copied workspace")
                .build();

        Workspace workspace = smartsheet.workspaceResources()
                .copyWorkspace(
                        workspaceId,
                        destination,
                        EnumSet.of(WorkspaceCopyInclusion.ALL),
                        EnumSet.of(WorkspaceRemapExclusion.CELLLINKS)
                );
        assertThat(workspace.getName()).isEqualTo("New Copied workspace");
        deleteWorkspace(workspace.getId());
    }

    public void testShareWorkspace() throws SmartsheetException {
        List<Share> shares = new ArrayList<>();
        shares.add(new Share.CreateUserShareBuilder()
                .setEmailAddress("jane.doe@smartsheet.com")
                .setAccessLevel(AccessLevel.ADMIN).build());
        shares.add(new Share.CreateUserShareBuilder()
                .setEmailAddress("aditi.test@smartsheet.com")
                .setAccessLevel(AccessLevel.ADMIN).build());

        shares = smartsheet.workspaceResources().shareResources().shareTo(workspaceId, shares, true);
        assertThat(shares).hasSize(2);
        assertThat(shares.get(0).getEmail()).isEqualTo("aditi.test@smartsheet.com");
    }

    public void testGetWorkspace() throws SmartsheetException {
        Workspace workspace = smartsheet.workspaceResources().getWorkspace(workspaceId, null, null);
        assertThat(workspace.getId()).isNotNull();
    }

    public void testListWorkspaces() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters(true, 10, 10);
        PagedResult<Workspace> workspace = smartsheet.workspaceResources().listWorkspaces(parameters);
        assertThat(workspace).isNotNull();
    }

    public void testUpdateWorkspace() throws SmartsheetException {
        Workspace workspace = new Workspace.UpdateWorkspaceBuilder().setName("Updated workspace").setId(workspaceId).build();
        Workspace newWorkspace = smartsheet.workspaceResources().updateWorkspace(workspace);
        assertThat(newWorkspace.getId().longValue()).isEqualTo(workspaceId);
    }

    public void testDeleteWorkspace() throws SmartsheetException {
        smartsheet.workspaceResources().deleteWorkspace(workspaceId);
    }
}
