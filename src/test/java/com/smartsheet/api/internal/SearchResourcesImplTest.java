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
import com.smartsheet.api.models.SearchResult;
import com.smartsheet.api.models.SearchResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchResourcesImplTest extends ResourcesImplBase {

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
        assertThat(result.getResults()).isNotNull();
        List<SearchResultItem> results = result.getResults();
        assertThat(results).isNotNull().hasSize(50);
        assertThat(result.getTotalCount().intValue()).isEqualTo(50);
        assertThat(results.get(0).getText()).isEqualTo("Brett Task Sheet");
        assertThat(results.get(0).getObjectType()).isEqualTo("sheet");
        assertThat(results.get(0).getObjectId().longValue()).isEqualTo(714377448974212L);
        assertThat(results.get(0).getContextData().get(0)).isEqualTo("Platform / Team");
    }

    @Test
    void testSearchSheet() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/searchSheet.json"));

        SearchResult searchSheet = searchResources.searchSheet(1234L, "java");
        assertThat(searchSheet).isNotNull();
        List<SearchResultItem> results = searchSheet.getResults();
        assertThat(results).hasSize(100);
        assertThat(searchSheet.getTotalCount().intValue()).isEqualTo(130);
        assertThat(results.get(0).getText()).isEqualTo("HomeResources.java");
        assertThat(results.get(0).getObjectType()).isEqualTo("row");
        assertThat(results.get(0).getObjectId().longValue()).isEqualTo(7243572589160324L);
        assertThat(results.get(0).getContextData().get(0)).isEqualTo("Row 12");
        assertThat(results.get(0).getParentObjectType()).isEqualTo("sheet");
        assertThat(results.get(0).getParentObjectId().longValue()).isEqualTo(2630121841551236L);
        assertThat(results.get(0).getParentObjectName()).isEqualTo("SDK Code Checklist");
    }

    @Test
    void nullQuerySearchSheet() {
        assertThatThrownBy(() -> searchResources.searchSheet(1234L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void emptyQuerySearchSheet() {
        assertThatThrownBy(() -> searchResources.searchSheet(1234L, ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullQueryOnSearch() {
        assertThatThrownBy(() -> searchResources.search(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void emptyQuerySearch() {
        assertThatThrownBy(() -> searchResources.search(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
