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

import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmartsheetImplTest extends ResourcesImplBase {

    private SmartsheetImpl smartsheet;
    private HttpClient httpClient;
    private final String baseURI = "http://localhost:9090/1.1/";
    private final String accessToken = "accessToken";

    @BeforeEach
    public void setUp() throws Exception {
        httpClient = new DefaultHttpClient();
        smartsheet = new SmartsheetImpl(baseURI, accessToken, httpClient, serializer);
    }

    @Test
    void testGetHttpClient() {
        assertThat(smartsheet.getHttpClient()).isEqualTo(httpClient);
    }

    @Test
    void testGetJsonSerializer() {
        assertThat(smartsheet.getJsonSerializer()).isNotNull();
    }

    @Test
    void testGetBaseURI() {
        assertThat(smartsheet.getBaseURI()).hasToString(baseURI);
    }

    @Test
    void testGetAssumedUser() {
        assertThat(smartsheet.getAssumedUser()).isNull();
    }

    @Test
    void testGetAccessToken() {
        assertThat(smartsheet.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void testHome() {
        assertThat(smartsheet.homeResources()).isNotNull();
    }

    @Test
    void testWorkspaces() {
        assertThat(smartsheet.workspaceResources()).isNotNull();
    }

    @Test
    void testFolders() {
        assertThat(smartsheet.folderResources()).isNotNull();
    }

    @Test
    void testTemplates() {
        assertThat(smartsheet.templateResources()).isNotNull();
    }

    @Test
    void testSheets() {
        assertThat(smartsheet.sheetResources()).isNotNull();
    }

    @Test
    void testfavorites() {
        assertThat(smartsheet.favoriteResources()).isNotNull();
    }

    @Test
    void testUsers() {
        assertThat(smartsheet.userResources()).isNotNull();
    }

    @Test
    void testSearch() {
        assertThat(smartsheet.searchResources()).isNotNull();
    }

    @Test
    void testReports() {
        assertThat(smartsheet.reportResources()).isNotNull();
    }

    @Test
    void testSetAssumedUser() {
        smartsheet.setAssumedUser("user");
    }

    @Test
    void testSetAccessToken() {
        smartsheet.setAccessToken("1234");
    }

    @Test
    void testSights() {
        assertThat(smartsheet.sightResources()).isNotNull();
    }

}
