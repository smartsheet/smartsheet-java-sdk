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
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.FolderPathNode;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.DestinationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class FolderResourcesImplTest extends ResourcesImplBase {

    @BeforeEach
    public void setUp() {
        // Create a folder resource
        folderResource = new FolderResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Test
    void testUpdateFolder() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateFolder.json"));

        Folder newFolder = new Folder.UpdateFolderBuilder().setName("New Name").build();
        Folder resultFolder = folderResource.updateFolder(newFolder);

        assertThat(resultFolder.getName()).isEqualTo(newFolder.getName());
    }

    @Test
    void testDeleteFolder() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteFolder.json"));
        assertThatCode(() -> folderResource.deleteFolder(7752230582413188L)).doesNotThrowAnyException();
    }

    @Test
    void testCreateFolder() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createFolder.json"));

        Folder newFolder = new Folder.CreateFolderBuilder().setName("new folder by brett").build();
        Folder createdFolder = folderResource.createFolder(123L, newFolder);

        assertThat(createdFolder.getName()).isEqualTo(newFolder.getName());
    }

    @Test
    void testCopyFolder() throws Exception {
        server.setResponseBody(new File("src/test/resources/copyFolder.json"));
        ContainerDestination containerDestination = new ContainerDestination();
        containerDestination.setDestinationType(DestinationType.FOLDER);

        Folder folder = folderResource.copyFolder(123L, containerDestination, null, null);
        assertThat(folder.getPermalink()).isEqualTo("https://{base_url}?lx=lB0JaOh6AX1wGwqxsQIMaA");
    }

    @Test
    void testMoveFolder() throws Exception {
        server.setResponseBody(new File("src/test/resources/moveFolder.json"));
        ContainerDestination containerDestination = new ContainerDestination();
        containerDestination.setDestinationType(DestinationType.FOLDER);

        Folder folder = folderResource.moveFolder(123L, containerDestination);
        assertThat(folder.getId()).isEqualTo(4509918431602564L);
    }

    @Test
    void testGetFolderPath() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getFolderPath.json"));

        FolderPathNode result = folderResource.getFolderPath(1234567890L);

        // workspace root
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4509918431602564L);
        assertThat(result.getName()).isEqualTo("Sample Workspace");
        assertThat(result.getPermalink()).isEqualTo("https://app.smartsheet.com/workspaces/mock_workspace_id");
        assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        // level-1 folder
        assertThat(result.getFolders()).hasSize(1);
        FolderPathNode level1 = result.getFolders().get(0);
        assertThat(level1.getId()).isEqualTo(1234567890123456L);
        assertThat(level1.getName()).isEqualTo("Project Plans");
        assertThat(level1.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/1234567890123456");
        // level-2 folder
        assertThat(level1.getFolders()).hasSize(1);
        FolderPathNode level2 = level1.getFolders().get(0);
        assertThat(level2.getId()).isEqualTo(2345678901234567L);
        assertThat(level2.getName()).isEqualTo("Project Plans Subfolder");
        assertThat(level2.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/2345678901234567");
        // level-3 (leaf) folder
        assertThat(level2.getFolders()).hasSize(1);
        FolderPathNode level3 = level2.getFolders().get(0);
        assertThat(level3.getId()).isEqualTo(3456789012345678L);
        assertThat(level3.getName()).isEqualTo("Project Plans Sub-Subfolder");
        assertThat(level3.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/3456789012345678");
        assertThat(level3.getFolders()).isNullOrEmpty();
    }

    @Test
    void testGetFolderPath_getFolder() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getFolderPath.json"));

        FolderPathNode result = folderResource.getFolderPath(1234567890L);
        FolderPathNode leaf = result.getLeafFolder();

        assertThat(leaf).isNotNull();
        assertThat(leaf.getName()).isEqualTo("Project Plans Sub-Subfolder");
        assertThat(leaf.getId()).isEqualTo(3456789012345678L);
        assertThat(leaf.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/3456789012345678");
        assertThat(leaf.getFolders()).isNullOrEmpty();
    }

    @Test
    void testGetFolderPath_getFolderPath() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getFolderPath.json"));

        FolderPathNode result = folderResource.getFolderPath(1234567890L);

        assertThat(result.getLeafFolderPath())
                .isEqualTo("/Sample Workspace/Project Plans/Project Plans Subfolder/Project Plans Sub-Subfolder");
    }

    @Test
    void testGetFolderPath_404_throwsResourceNotFoundException() throws IOException {
        server.setStatus(404);
        server.setResponseBody(new File("src/test/resources/pathError.json"));

        assertThatThrownBy(() -> folderResource.getFolderPath(1234567890L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetFolderPath_500_throwsInvalidRequestException() throws IOException {
        server.setStatus(500);
        server.setResponseBody(new File("src/test/resources/pathError.json"));

        assertThatThrownBy(() -> folderResource.getFolderPath(1234567890L))
                .isInstanceOf(InvalidRequestException.class);
    }
}
