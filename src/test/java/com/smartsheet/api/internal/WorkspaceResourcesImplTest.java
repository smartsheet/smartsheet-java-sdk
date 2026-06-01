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
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.TokenPaginationParameters;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.DestinationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

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
    void testListWorkspacesWithTokenPagination() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listWorkspacesTokenPagination.json"));

        TokenPaginationParameters pagination = new TokenPaginationParameters("token123", 500);

        TokenPaginatedResult<Workspace> workspaces = workspaceResources.listWorkspaces(pagination);

        assertThat(workspaces.getData()).isNotNull();
        assertThat(workspaces.getData()).hasSize(2);
        assertThat(workspaces.getData().get(0).getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(workspaces.getData().get(0).getId().longValue()).isEqualTo(3457273486960516L);
        assertThat(workspaces.getData().get(0).getName()).isEqualTo("workspace 1");
        assertThat(workspaces.getData().get(0).getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=JNL0bgXtXc0pzni9tzAc4g");
        assertThat(workspaces.getLastKey()).isEqualTo("nextToken456");
        assertThat(workspaces.hasMorePages()).isTrue();
        assertThat(server.getLastRequestUrl()).startsWith("/1.1/workspaces?");
        assertThat(server.getLastRequestUrl()).contains("lastKey=token123");
        assertThat(server.getLastRequestUrl()).contains("maxItems=500");
        assertThat(server.getLastRequestUrl()).contains("paginationType=token");
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
