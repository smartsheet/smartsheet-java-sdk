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

package com.smartsheet.api.sdktest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.TokenPaginationParameters;
import com.smartsheet.api.models.Workspace;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkspacesTest {

    @Test
    void listWorkspaces_FirstPageWithTokenPagination() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Workspaces - First Page with Pagination");

        TokenPaginationParameters params = new TokenPaginationParameters(null, 100);
        TokenPaginatedResult<Workspace> workspaces = ss.workspaceResources().listWorkspaces(params);

        assertThat(workspaces.getData()).hasSize(2);
        assertThat(workspaces.getData().get(0).getId()).isEqualTo(1001L);
        assertThat(workspaces.getData().get(0).getName()).isEqualTo("Marketing Workspace");
        assertThat(workspaces.getData().get(1).getId()).isEqualTo(1002L);
        assertThat(workspaces.getData().get(1).getName()).isEqualTo("Sales Workspace");
        assertThat(workspaces.getLastKey()).isEqualTo("eyJsYXN0SWQiOjEwMDJ9");
    }

    @Test
    void listWorkspaces_MiddlePageWithTokenPagination() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Workspaces - Middle Page with Pagination");

        TokenPaginationParameters params = new TokenPaginationParameters("eyJsYXN0SWQiOjEwMDJ9", 100);
        TokenPaginatedResult<Workspace> workspaces = ss.workspaceResources().listWorkspaces(params);

        assertThat(workspaces.getData()).hasSize(2);
        assertThat(workspaces.getData().get(0).getId()).isEqualTo(1003L);
        assertThat(workspaces.getData().get(0).getName()).isEqualTo("Engineering Workspace");
        assertThat(workspaces.getData().get(1).getId()).isEqualTo(1004L);
        assertThat(workspaces.getData().get(1).getName()).isEqualTo("HR Workspace");
        assertThat(workspaces.getLastKey()).isEqualTo("eyJsYXN0SWQiOjEwMDR9");
    }

    @Test
    void listWorkspaces_FinalPageWithTokenPagination() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Workspaces - Final Page with Pagination");

        TokenPaginationParameters params = new TokenPaginationParameters("eyJsYXN0SWQiOjEwMDR9", 100);
        TokenPaginatedResult<Workspace> workspaces = ss.workspaceResources().listWorkspaces(params);

        assertThat(workspaces.getData()).hasSize(1);
        assertThat(workspaces.getData().get(0).getId()).isEqualTo(1005L);
        assertThat(workspaces.getData().get(0).getName()).isEqualTo("Compliance Workspace");
    }

    @Test
    void listWorkspaces_NoPaginationParameters() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Workspaces - No Pagination Parameters");

        TokenPaginatedResult<Workspace> workspaces = ss.workspaceResources().listWorkspaces(null);

        assertThat(workspaces.getData()).hasSize(5);
        assertThat(workspaces.getData().get(0).getId()).isEqualTo(1001L);
        assertThat(workspaces.getData().get(0).getName()).isEqualTo("Marketing Workspace");
        assertThat(workspaces.getData().get(4).getId()).isEqualTo(1005L);
        assertThat(workspaces.getData().get(4).getName()).isEqualTo("Compliance Workspace");
    }

}
