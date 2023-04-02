package com.smartsheet.api.internal;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        assertSame(httpClient, smartsheet.getHttpClient());
    }

    @Test
    void testGetJsonSerializer() {
        assertNotNull(smartsheet.getJsonSerializer());
    }

    @Test
    void testGetBaseURI() {
        assertEquals(baseURI, smartsheet.getBaseURI().toString());
    }

    @Test
    void testGetAssumedUser() {
        assertNull(smartsheet.getAssumedUser());
    }

    @Test
    void testGetAccessToken() {
        assertEquals(accessToken, smartsheet.getAccessToken());
    }

    @Test
    void testHome() {
        assertNotNull(smartsheet.homeResources());
    }

    @Test
    void testWorkspaces() {
        assertNotNull(smartsheet.workspaceResources());
    }

    @Test
    void testFolders() {
        assertNotNull(smartsheet.folderResources());
    }

    @Test
    void testTemplates() {
        assertNotNull(smartsheet.templateResources());
    }

    @Test
    void testSheets() {
        assertNotNull(smartsheet.sheetResources());
    }

    @Test
    void testfavorites() {
        assertNotNull(smartsheet.favoriteResources());
    }

    @Test
    void testUsers() {
        assertNotNull(smartsheet.userResources());
    }

    @Test
    void testSearch() {
        assertNotNull(smartsheet.searchResources());
    }

    @Test
    void testReports() {
        assertNotNull(smartsheet.reportResources());
    }

    @Test
    void testSetAssumedUser() { smartsheet.setAssumedUser("user"); }

    @Test
    void testSetAccessToken() { smartsheet.setAccessToken("1234"); }

    @Test
    void testSights() { assertNotNull(smartsheet.sightResources()); }
}
