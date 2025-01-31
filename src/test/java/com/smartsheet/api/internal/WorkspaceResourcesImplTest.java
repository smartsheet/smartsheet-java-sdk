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
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.Source;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.DestinationType;
import com.smartsheet.api.models.enums.SourceInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class WorkspaceResourcesImplTest extends ResourcesImplBase {

    private WorkspaceResourcesImpl workspaceResources;

    @BeforeEach
    public void setUp() throws Exception {
        workspaceResources = new WorkspaceResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListWorkspaces() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listWorkspaces.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Workspace> workspace = workspaceResources.listWorkspaces(parameters);
        assertThat(workspace.getPageNumber().longValue()).isEqualTo(1);
        assertThat(workspace.getPageSize().longValue()).isEqualTo(100);
        assertThat(workspace.getTotalPages().longValue()).isEqualTo(1);
        assertThat(workspace.getTotalCount().longValue()).isEqualTo(2);

        assertThat(workspace.getData()).hasSize(2);
        assertThat(workspace.getData().get(0).getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(workspace.getData().get(0).getId().longValue()).isEqualTo(3457273486960516L);
        assertThat(workspace.getData().get(0).getName()).isEqualTo("workspace 1");
        assertThat(workspace.getData().get(0).getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=JNL0bgXtXc0pzni9tzAc4g");
    }

    @Test
    void testGetWorkspace() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/getWorkspace.json"));

        Workspace workspace = workspaceResources.getWorkspace(1234L, true, EnumSet.allOf(SourceInclusion.class));
        assertThat(workspace.getSheets()).hasSize(1);

        Sheet sheet = workspace.getSheets().get(0);
        assertThat(sheet.getName()).isEqualTo("sheet 1");

        Source source = sheet.getSource();
        assertThat(source.getId()).isNotNull();
        assertThat(source.getType()).isNotNull();

        assertThat(workspace.getId().longValue()).isEqualTo(7116448184199044L);
        assertThat(workspace.getName()).isEqualTo("New workspace");

        assertThat(workspace.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(workspace.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=8Z0XuFUEAkxmHCSsMw4Zgg");
    }

    @Test
    void testCreateWorkspace() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/createWorkspace.json"));

        Workspace workspace = new Workspace();
        workspace.setName("New Workspace");
        Workspace newWorkspace = workspaceResources.createWorkspace(workspace);
        assertThat(newWorkspace.getId().longValue()).isEqualTo(2349499415848836L);
        assertThat(newWorkspace.getName()).isEqualTo("New Workspace");
        assertThat(newWorkspace.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newWorkspace.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=Jasdfa");
    }

    @Test
    void testUpdateWorkspace() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/updateWorkspace.json"));

        Workspace workspace = new Workspace();
        workspace.setName("New Workspace");
        Workspace newWorkspace = workspaceResources.updateWorkspace(workspace);
        assertThat(newWorkspace.getId().longValue()).isEqualTo(2349499415848836L);
        assertThat(newWorkspace.getName()).isEqualTo("New Workspace1");
        assertThat(newWorkspace.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(newWorkspace.getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=asdf");
    }

    @Test
    void testDeleteWorkspace() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteWorkspace.json"));
        assertThatCode(() -> workspaceResources.deleteWorkspace(1234L)).doesNotThrowAnyException();
    }

    @Test
    void testCopyWorkspace() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/copyWorkspace.json"));
        ContainerDestination containerDestination = new ContainerDestination();
        containerDestination.setDestinationType(DestinationType.WORKSPACE);

        Folder folder = workspaceResources.copyWorkspace(123L, containerDestination, null, null);
        assertThat(folder.getPermalink()).isEqualTo("https://{url}?lx=VL4YlIUnyYgASeX02grbLQ");
    }
}
