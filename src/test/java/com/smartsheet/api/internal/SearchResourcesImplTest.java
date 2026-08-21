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
import com.smartsheet.api.models.SearchResponse;
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
        server.setResponseBody(new File("src/test/resources/searchResponse.json"));

        SearchResponse result = searchResources.search("budget");
        assertThat(result).isNotNull();
        assertThat(result.getTotalCount()).isEqualTo(9);
        assertThat(result.getSearchResults()).isNotNull().hasSize(9);
    }

    @Test
    void testSearchSheet() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/searchResponse.json"));

        SearchResponse result = searchResources.searchSheet(1234L, "budget");
        assertThat(result).isNotNull();
        assertThat(result.getTotalCount()).isEqualTo(9);
        assertThat(result.getSearchResults()).isNotNull();
    }

    @Test
    void testSearchResponse_allNineTypes() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/searchResponse.json"));

        SearchResponse result = searchResources.search("budget");
        List<SearchResultItem> items = result.getSearchResults();

        assertThat(items.get(0).getObjectType()).isEqualTo("GRID_ROW");
        assertThat(items.get(1).getObjectType()).isEqualTo("ATTACHMENT");
        assertThat(items.get(2).getObjectType()).isEqualTo("SHEET");
        assertThat(items.get(5).getObjectType()).isEqualTo("FORM");
        assertThat(items.get(6).getObjectType()).isEqualTo("COLLECTION_TITLE");
        assertThat(items.get(7).getObjectType()).isEqualTo("PORTFOLIO_TITLE");
        assertThat(items.get(8).getObjectType()).isEqualTo("PROJECT_TITLE");
    }

    @Test
    void testSearchResponse_typeSpecificFields() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/searchResponse.json"));

        SearchResponse result = searchResources.search("budget");
        List<SearchResultItem> items = result.getSearchResults();

        // GRID_ROW: primaryColumnCellText populated
        assertThat(items.get(0).getPrimaryColumnCellText()).isEqualTo("Budget Review");
        assertThat(items.get(0).getAttachmentSource()).isNull();

        // ATTACHMENT: attachmentSource and attachmentDescription populated
        assertThat(items.get(1).getAttachmentSource()).isEqualTo("GRIDROW");
        assertThat(items.get(1).getAttachmentDescription()).isEqualTo("Monthly budget forecast spreadsheet");
        assertThat(items.get(1).getPrimaryColumnCellText()).isNull();

        // SHEET (non-template)
        assertThat(items.get(2).getIsTemplate()).isFalse();

        // SHEET (template)
        assertThat(items.get(3).getIsTemplate()).isTrue();
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
