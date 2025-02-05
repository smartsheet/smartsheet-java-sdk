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
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HomeFolderResourcesImplTest extends ResourcesImplBase {

    private HomeFolderResourcesImpl homeFolderResources;

    @BeforeEach
    public void setUp() throws Exception {
        homeFolderResources = new HomeFolderResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Nested
    class ListFoldersTests {
        @Test
        void listFolders_withParameters() throws SmartsheetException, IOException {
            server.setResponseBody(new File("src/test/resources/listFolders.json"));

            PaginationParameters parameters = new PaginationParameters(true, null, null);
            PagedResult<Folder> foldersWrapper = homeFolderResources.listFolders(parameters);

            assertThat(foldersWrapper.getPageSize()).isEqualTo(100);
            assertThat(foldersWrapper.getData().get(0).getName()).isEqualTo("Folder 1");
            assertThat(foldersWrapper.getData().get(1).getName()).isEqualTo("Folder 2");
            assertThat(foldersWrapper.getData().get(0).getId()).isEqualTo(7116448184199044L);
        }

        @Test
        void listFolders_nullParameters() throws SmartsheetException, IOException {
            server.setResponseBody(new File("src/test/resources/listFolders.json"));

            PagedResult<Folder> foldersWrapper = homeFolderResources.listFolders(null);

            assertThat(foldersWrapper.getPageSize()).isEqualTo(100);
            assertThat(foldersWrapper.getData().get(0).getName()).isEqualTo("Folder 1");
            assertThat(foldersWrapper.getData().get(1).getName()).isEqualTo("Folder 2");
            assertThat(foldersWrapper.getData().get(0).getId()).isEqualTo(7116448184199044L);
        }
    }

    @Nested
    class CreateFolderTests {
        @Test
        void createFolder() throws IOException, SmartsheetException {
            server.setResponseBody(new File("src/test/resources/createFolders.json"));

            Folder folder = new Folder.CreateFolderBuilder().setName("Hello World").build();

            Folder newFolder = homeFolderResources.createFolder(folder);
            assertThat(newFolder.getId()).isEqualTo(6821399500220292L);
            assertThat(newFolder.getName()).isEqualTo("hello world");
        }
    }
}
