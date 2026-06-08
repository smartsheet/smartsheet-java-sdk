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
import com.smartsheet.api.models.enums.DestinationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
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
}
