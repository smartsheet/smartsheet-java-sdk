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

package com.smartsheet.api.sdktest.sights;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.PathLeaf;
import com.smartsheet.api.models.SightPathNode;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.sdktest.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestGetSightPath {

    private static final long TEST_SIGHT_ID = 1234567890123456L;
    private static final String GET_NESTED_SIGHT_PATH_STUB = "/sights/get-nested-sight-path/all-response-body-properties";
    private static final long WORKSPACE_ID = 4509918431602564L;
    private static final String WORKSPACE_NAME = "Sample Workspace";
    private static final String WORKSPACE_PERMALINK = "https://app.smartsheet.com/workspaces/mock_workspace_id";
    private static final long NESTED_SIGHT_ID = 3456789012345678L;
    private static final long ROOT_SIGHT_ID = 5678901234567890L;

    @Test
    void testGetSightPathGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                GET_NESTED_SIGHT_PATH_STUB,
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.sightResources().getSightPath(TEST_SIGHT_ID);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo("/2.0/sights/" + TEST_SIGHT_ID + "/path");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(wiremockRequest.getQueryParams()).isEqualTo(Map.of());
    }

    @Test
    void testGetSightPathAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                GET_NESTED_SIGHT_PATH_STUB,
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SightPathNode result = smartsheet.sightResources().getSightPath(TEST_SIGHT_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(WORKSPACE_ID);
        assertThat(result.getName()).isEqualTo(WORKSPACE_NAME);
        assertThat(result.getPermalink()).isEqualTo(WORKSPACE_PERMALINK);
        assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(result.getFolders()).hasSize(1);

        PathLeaf leaf = result.getSight();
        assertThat(leaf).isNotNull();
        assertThat(leaf.getId()).isEqualTo(NESTED_SIGHT_ID);
        assertThat(leaf.getName()).isEqualTo("Project Dashboard");
        assertThat(leaf.getPermalink()).isEqualTo("https://app.smartsheet.com/dashboards/3456789012345678");
        assertThat(leaf.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(leaf.getCreatedAt()).isEqualTo(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        assertThat(leaf.getModifiedAt()).isEqualTo(ZonedDateTime.parse("2024-06-01T00:00:00Z"));
        assertThat(result.getSightPath()).isEqualTo("Sample Workspace/Project Plans/Project Plans Subfolder/Project Dashboard");
    }

    @Test
    void testGetSightPathRootLevelAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/sights/get-root-sight-path/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SightPathNode result = smartsheet.sightResources().getSightPath(TEST_SIGHT_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(WORKSPACE_ID);
        assertThat(result.getName()).isEqualTo(WORKSPACE_NAME);
        assertThat(result.getFolders()).isNullOrEmpty();

        PathLeaf leaf = result.getSight();
        assertThat(leaf).isNotNull();
        assertThat(leaf.getId()).isEqualTo(ROOT_SIGHT_ID);
        assertThat(leaf.getName()).isEqualTo("Root Level Dashboard");
        assertThat(leaf.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(result.getSightPath()).isEqualTo("Sample Workspace/Root Level Dashboard");
    }

    @Test
    void testGetSightPathError404Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/404-response",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sightResources().getSightPath(TEST_SIGHT_ID)
        );
        assertThat(exception.getMessage()).isEqualTo("Not Found");
    }

    @Test
    void testGetSightPathError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/errors/500-response",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.sightResources().getSightPath(TEST_SIGHT_ID)
        );
        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }
}
