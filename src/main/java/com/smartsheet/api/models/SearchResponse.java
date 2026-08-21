package com.smartsheet.api.models;

/*
 * Copyright (C) 2026 Smartsheet
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

import java.util.List;

/**
 * Top-level response returned by the search endpoint (unified search-service schema).
 *
 * <p>Use {@link SearchResultItem#getObjectType()} to distinguish result types and
 * access type-specific fields:
 * <ul>
 *   <li>{@code GRID_ROW} — {@link SearchResultItem#getPrimaryColumnCellText()}</li>
 *   <li>{@code ATTACHMENT} — {@link SearchResultItem#getAttachmentSource()}, {@link SearchResultItem#getAttachmentDescription()}</li>
 *   <li>{@code SHEET} — {@link SearchResultItem#getIsTemplate()}</li>
 * </ul>
 */
public class SearchResponse {

    /** Number of items in {@link #searchResults}. */
    private Integer totalCount;

    /** Array of matched search result items. */
    private List<SearchResultItem> searchResults;

    /** Workspaces associated with the results. */
    private List<Object> workspaces;

    /** The authenticated user's personal workspace ID, if present in results. */
    private String personalWorkspaceId;

    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }

    public List<SearchResultItem> getSearchResults() { return searchResults; }
    public void setSearchResults(List<SearchResultItem> searchResults) { this.searchResults = searchResults; }

    public List<Object> getWorkspaces() { return workspaces; }
    public void setWorkspaces(List<Object> workspaces) { this.workspaces = workspaces; }

    public String getPersonalWorkspaceId() { return personalWorkspaceId; }
    public void setPersonalWorkspaceId(String personalWorkspaceId) { this.personalWorkspaceId = personalWorkspaceId; }
}
