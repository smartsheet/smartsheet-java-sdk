/*
* Copyright (C) 2024 Smartsheet
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
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class WorkspaceFolderResourcesImplTest extends ResourcesImplBase {

    private WorkspaceFolderResourcesImpl workspaceFolderResources;

    @BeforeEach
    public void setUp() throws Exception {
        workspaceFolderResources = new WorkspaceFolderResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListFolders() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/listWorkspaceFolders.json"));

        PaginationParameters parameters = new PaginationParameters(true, null, null);
        PagedResult<Folder> foldersWrapper = workspaceFolderResources.listFolders(1234L, parameters);
        assertThat(foldersWrapper.getData()).hasSize(2);
        assertThat(foldersWrapper.getData().get(0).getId().longValue()).isEqualTo(7116448184199044L);
        assertThat(foldersWrapper.getData().get(1).getId().longValue()).isEqualTo(7116448184188022L);
        assertThat(foldersWrapper.getData().get(0).getName()).isEqualTo("Folder 1");
        assertThat(foldersWrapper.getData().get(1).getName()).isEqualTo("Folder 2");
        assertThat(foldersWrapper.getData().get(0).getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=9sljohj8jEXqvJIbTrK2Hb");
        assertThat(foldersWrapper.getData().get(1).getPermalink()).isEqualTo("https://app.smartsheet.com/b/home?lx=xgDVrNNbi-O9XwINEpT5Er");
    }

    @Test
    void testCreateFolder() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/newWorkspaceFolder.json"));

        Folder folder = new Folder.CreateFolderBuilder().setName("New Folder").build();
        Folder newFolder = workspaceFolderResources.createFolder(1234L, folder);
        assertThat(newFolder.getId().longValue()).isEqualTo(8121709439018884L);
        assertThat(newFolder.getName()).isEqualTo("New Folder");
    }

}
