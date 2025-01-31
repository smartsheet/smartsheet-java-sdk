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

import com.smartsheet.api.HomeFolderResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Home;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.SourceInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HomeResourcesImplTest extends ResourcesImplBase {

    private HomeResourcesImpl homeResources;

    @BeforeEach
    public void setUp() throws Exception {
        homeResources = new HomeResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetHome() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getHome.json"));

        List<Home> homes = new ArrayList<>();
        homes.add(homeResources.getHome(EnumSet.of(SourceInclusion.SOURCE)));
        homes.add(homeResources.getHome(null));
        for (Home home : homes) {
            assertThat(home.getSheets()).isNotNull();
            assertThat(home.getSheets()).hasSize(7);
            assertThat(home.getFolders()).isNotNull();
            assertThat(home.getFolders()).hasSize(5);
            assertThat(home.getWorkspaces()).isNotNull();
            assertThat(home.getWorkspaces()).hasSize(7);
            assertThat(home.getTemplates()).isNull();
            home.setTemplates(new ArrayList<>());
        }
    }

    @Test
    void testFolders() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/getHomeFolders.json"));

        HomeFolderResources folders = homeResources.folderResources();
        PaginationParameters parameters = new PaginationParameters(true, 1, 1);
        assertThat(folders.listFolders(parameters)).isNotNull();
        assertThat(folders.listFolders(parameters).getTotalPages()).isEqualTo(1);
    }

}
