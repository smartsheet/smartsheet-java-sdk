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

package com.smartsheet.api.sdktest.sheets;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.PathLeaf;
import com.smartsheet.api.models.SheetPathNode;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestGetSheetPath {

    private static final long TEST_SHEET_ID = 1234567890123456L;
    private static final String GET_NESTED_SHEET_PATH_STUB = "/sheets/get-nested-sheet-path/all-response-body-properties";
    private static final long WORKSPACE_ID = 4509918431602564L;
    private static final String WORKSPACE_NAME = "Sample Workspace";
    private static final String WORKSPACE_PERMALINK = "https://app.smartsheet.com/workspaces/mock_workspace_id";
    private static final long ROOT_SHEET_ID = 5678901234567890L;

    private static final SheetPathNode EXPECTED_NESTED_RESULT;
    private static final SheetPathNode EXPECTED_ROOT_RESULT;
    private static final SheetPathNode EXPECTED_REQUIRED_RESULT;

    static {
        PathLeaf nestedSheet = new PathLeaf();
        nestedSheet.setId(3456789012345678L);
        nestedSheet.setName("Project Plan");
        nestedSheet.setPermalink("https://app.smartsheet.com/sheets/3456789012345678");
        nestedSheet.setAccessLevel(AccessLevel.ADMIN);
        nestedSheet.setCreatedAt(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        nestedSheet.setModifiedAt(ZonedDateTime.parse("2024-06-01T00:00:00Z"));

        SheetPathNode subfolder = new SheetPathNode();
        subfolder.setId(2345678901234567L);
        subfolder.setName("Project Plans Subfolder");
        subfolder.setPermalink("https://app.smartsheet.com/folders/2345678901234567");
        subfolder.setSheets(List.of(nestedSheet));

        SheetPathNode folder = new SheetPathNode();
        folder.setId(1234567890123456L);
        folder.setName("Project Plans");
        folder.setPermalink("https://app.smartsheet.com/folders/1234567890123456");
        folder.setFolders(List.of(subfolder));

        EXPECTED_NESTED_RESULT = new SheetPathNode();
        EXPECTED_NESTED_RESULT.setId(WORKSPACE_ID);
        EXPECTED_NESTED_RESULT.setName(WORKSPACE_NAME);
        EXPECTED_NESTED_RESULT.setPermalink(WORKSPACE_PERMALINK);
        EXPECTED_NESTED_RESULT.setAccessLevel(AccessLevel.OWNER);
        EXPECTED_NESTED_RESULT.setFolders(List.of(folder));

        PathLeaf rootSheet = new PathLeaf();
        rootSheet.setId(ROOT_SHEET_ID);
        rootSheet.setName("Root Level Sheet");
        rootSheet.setPermalink("https://app.smartsheet.com/sheets/rootlevel");
        rootSheet.setAccessLevel(AccessLevel.ADMIN);
        rootSheet.setCreatedAt(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        rootSheet.setModifiedAt(ZonedDateTime.parse("2024-06-01T00:00:00Z"));

        EXPECTED_ROOT_RESULT = new SheetPathNode();
        EXPECTED_ROOT_RESULT.setId(WORKSPACE_ID);
        EXPECTED_ROOT_RESULT.setName(WORKSPACE_NAME);
        EXPECTED_ROOT_RESULT.setPermalink(WORKSPACE_PERMALINK);
        EXPECTED_ROOT_RESULT.setAccessLevel(AccessLevel.OWNER);
        EXPECTED_ROOT_RESULT.setSheets(List.of(rootSheet));

        EXPECTED_REQUIRED_RESULT = new SheetPathNode();
        EXPECTED_REQUIRED_RESULT.setId(WORKSPACE_ID);
        EXPECTED_REQUIRED_RESULT.setName(WORKSPACE_NAME);
        EXPECTED_REQUIRED_RESULT.setPermalink(WORKSPACE_PERMALINK);
        EXPECTED_REQUIRED_RESULT.setAccessLevel(AccessLevel.OWNER);
    }

    @Test
    void testGetSheetPathGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                GET_NESTED_SHEET_PATH_STUB,
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.sheetResources().getSheetPath(TEST_SHEET_ID);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/sheets/" + TEST_SHEET_ID + "/path");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testGetSheetPathAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                GET_NESTED_SHEET_PATH_STUB,
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SheetPathNode result = smartsheet.sheetResources().getSheetPath(TEST_SHEET_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_NESTED_RESULT);
        assertThat(result.getLeafSheetPath()).isEqualTo("/Sample Workspace/Project Plans/Project Plans Subfolder/Project Plan");
    }

    @Test
    void testGetSheetPathRootLevelAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sheets/get-root-sheet-path/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SheetPathNode result = smartsheet.sheetResources().getSheetPath(TEST_SHEET_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_ROOT_RESULT);
        assertThat(result.getLeafSheetPath()).isEqualTo("/Sample Workspace/Root Level Sheet");
    }

    @Test
    void testGetSheetPathRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sheets/get-sheet-path/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SheetPathNode result = smartsheet.sheetResources().getSheetPath(TEST_SHEET_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_RESULT);
    }

    @Test
    void testGetSheetPathError404Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/404-response",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sheetResources().getSheetPath(TEST_SHEET_ID)
        );
        assertThat(exception.getMessage()).isEqualTo("Not Found");
    }

    @Test
    void testGetSheetPathError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/500-response",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sheetResources().getSheetPath(TEST_SHEET_ID)
        );
        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }
}
