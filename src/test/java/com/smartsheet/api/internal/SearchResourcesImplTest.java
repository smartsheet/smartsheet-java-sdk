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

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.SearchResult;
import com.smartsheet.api.models.SearchResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchResourcesImplTest  extends ResourcesImplBase  {

    private SearchResourcesImpl searchResources;

    @BeforeEach
    public void setUp() throws Exception {
        searchResources = new SearchResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testSearch() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/search.json"));

        SearchResult result = searchResources.search("brett");
        assertNotNull(result.getResults());
        List<SearchResultItem> results = result.getResults();
        assertNotNull(results);
        assertEquals(50, results.size());
        assertEquals(50, result.getTotalCount().intValue());
        assertEquals("Brett Task Sheet", results.get(0).getText());
        assertEquals("sheet", results.get(0).getObjectType());
        assertEquals(714377448974212L, results.get(0).getObjectId().longValue());
        assertEquals("Platform / Team", results.get(0).getContextData().get(0));
    }

    @Test
    void testSearchSheet() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/searchSheet.json"));

        SearchResult searchSheet = searchResources.searchSheet(1234L, "java");
        assertNotNull(searchSheet);
        List<SearchResultItem> results = searchSheet.getResults();
        assertEquals(100,results.size());
        assertEquals(130, searchSheet.getTotalCount().intValue());
        assertEquals("HomeResources.java", results.get(0).getText());
        assertEquals("row", results.get(0).getObjectType());
        assertEquals(7243572589160324L, results.get(0).getObjectId().longValue());
        assertEquals("Row 12", results.get(0).getContextData().get(0));
        assertEquals("sheet", results.get(0).getParentObjectType());
        assertEquals(2630121841551236L, results.get(0).getParentObjectId().longValue());
        assertEquals("SDK Code Checklist", results.get(0).getParentObjectName());
    }

    @Test
    void nullQuerySearchSheet() {
        assertThrows(IllegalArgumentException.class, () -> searchResources.searchSheet(1234L, null));
    }

    @Test
    void emptyQuerySearchSheet() {
        assertThrows(IllegalArgumentException.class, () -> searchResources.searchSheet(1234L, ""));
    }

    @Test
    void nullQueryOnSeaerch() {
        assertThrows(IllegalArgumentException.class, () -> searchResources.search(null));
    }

    @Test
    void emptyQuerySearch() {
        assertThrows(IllegalArgumentException.class, () -> searchResources.search(""));
    }
}
