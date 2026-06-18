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

import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.PathLeaf;
import com.smartsheet.api.models.Sight;
import com.smartsheet.api.models.SightPathNode;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.TokenPaginationParameters;
import com.smartsheet.api.models.SightPublish;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.SightInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SightResourcesImplTest extends ResourcesImplBase {
    private SightResourcesImpl sightResourcesImpl;

    @BeforeEach
    public void before() {
        SmartsheetImpl smartsheetImpl = new SmartsheetImpl(
                "http://localhost:9090/1.1/",
                "accessToken",
                new DefaultHttpClient(),
                serializer
        );
        sightResourcesImpl = new SightResourcesImpl(smartsheetImpl);
    }

    @Test
    void testListSights() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listSights.json"));

        TokenPaginationParameters pagination = new TokenPaginationParameters(null, 10);

        TokenPaginatedResult<Sight> sightPagedResult = sightResourcesImpl.listSights(pagination);
        assertThat(sightPagedResult.getData()).isNotNull();
        assertThat(sightPagedResult.getData()).isNotEmpty();
        assertThat(sightPagedResult.getData()).hasSize(1);
        assertThat(sightPagedResult.getData().get(0).getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sightPagedResult.getData().get(0).getFavorite()).isFalse();
        assertThat(sightPagedResult.getData().get(0).getWidgets()).isEmpty();
        assertThat(sightPagedResult.getData().get(0).getWorkspace()).isNotNull();
        assertThat(sightPagedResult.getData().get(0).getPermalink()).isNotBlank();
        assertThat(sightPagedResult.hasMorePages()).isTrue();
        assertThat(sightPagedResult.getLastKey()).isEqualTo("abcDefGhIjKlMnOpQrStUvWxYz");
    }

    @Test
    void testGetSight() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSight.json"));

        Sight sight = sightResourcesImpl.getSight(12345L);
        assertThat(sight).isNotNull();
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sight.getFavorite()).isFalse();
        assertThat(sight.getWidgets()).isEmpty();
        assertThat(sight.getWorkspace()).isNotNull();
        assertThat(sight.getPermalink()).isNotBlank();
    }

    @Test
    void testGetSightWithLevel() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSight.json"));

        Sight sight = sightResourcesImpl.getSight(12345L, 1);
        assertThat(sight).isNotNull();
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sight.getFavorite()).isFalse();
        assertThat(sight.getWidgets()).isEmpty();
        assertThat(sight.getWorkspace()).isNotNull();
        assertThat(sight.getPermalink()).isNotBlank();
    }

    @Test
    void testGetSightWithLevelAndSightInclusion() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSight.json"));

        EnumSet<SightInclusion> includes = EnumSet.of(SightInclusion.SOURCE);

        Sight sight = sightResourcesImpl.getSight(12345L, includes, 1);
        assertThat(sight).isNotNull();
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sight.getFavorite()).isFalse();
        assertThat(sight.getWidgets()).isEmpty();
        assertThat(sight.getWorkspace()).isNotNull();
        assertThat(sight.getPermalink()).isNotBlank();
    }

    @Test
    void testUpdateSight_SightNull() {
        assertThatThrownBy(() -> {
            sightResourcesImpl.updateSight(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testUpdateSight() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateSight.json"));

        Sight sight = sightResourcesImpl.updateSight(new Sight());
        assertThat(sight).isNotNull();
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sight.getFavorite()).isFalse();
        assertThat(sight.getWidgets()).isEmpty();
        assertThat(sight.getWorkspace()).isNotNull();
        assertThat(sight.getPermalink()).isNotBlank();
    }

    @Test
    void updateWithNullSight() {
        assertThatThrownBy(() -> sightResourcesImpl.updateSight(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDeleteSight() throws IOException {
        server.setResponseBody(new File("src/test/resources/updateSight.json"));
        assertThatCode(() -> sightResourcesImpl.deleteSight(12345L)).doesNotThrowAnyException();
    }

    @Test
    void testSetPublishStatus() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/setSightPublishStatus.json"));
        SightPublish sightPublish = sightResourcesImpl.setPublishStatus(1234L, new SightPublish());
        assertThat(sightPublish.getReadOnlyFullAccessibleBy()).isEqualTo(Boolean.FALSE.toString());
        assertThat(sightPublish.getReadOnlyFullEnabled()).isEqualTo(Boolean.FALSE);
        assertThat(sightPublish.getReadOnlyFullUrl()).isNotBlank();
    }

    @Test
    void testGetPublishStatus() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/getSightPublishStatus.json"));
        SightPublish sightPublish = sightResourcesImpl.getPublishStatus(1234L);
        assertThat(sightPublish.getReadOnlyFullAccessibleBy()).isEqualTo(Boolean.FALSE.toString());
        assertThat(sightPublish.getReadOnlyFullEnabled()).isEqualTo(Boolean.FALSE);
        assertThat(sightPublish.getReadOnlyFullUrl()).isNotBlank();
    }

    @Test
    void testGetSightPath() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSightPath.json"));

        SightPathNode result = sightResourcesImpl.getSightPath(1234567890L);

        // workspace root
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4509918431602564L);
        assertThat(result.getName()).isEqualTo("Sample Workspace");
        assertThat(result.getPermalink()).isEqualTo("https://app.smartsheet.com/workspaces/mock_workspace_id");
        assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        // level-1 folder
        assertThat(result.getFolders()).hasSize(1);
        SightPathNode level1 = result.getFolders().get(0);
        assertThat(level1.getId()).isEqualTo(1234567890123456L);
        assertThat(level1.getName()).isEqualTo("Project Plans");
        assertThat(level1.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/1234567890123456");
        // level-2 folder (contains the leaf sight)
        assertThat(level1.getFolders()).hasSize(1);
        SightPathNode level2 = level1.getFolders().get(0);
        assertThat(level2.getId()).isEqualTo(2345678901234567L);
        assertThat(level2.getName()).isEqualTo("Project Plans Subfolder");
        assertThat(level2.getPermalink()).isEqualTo("https://app.smartsheet.com/folders/2345678901234567");
        // leaf sight
        assertThat(level2.getSights()).hasSize(1);
        PathLeaf sight = level2.getSights().get(0);
        assertThat(sight.getId()).isEqualTo(3456789012345678L);
        assertThat(sight.getName()).isEqualTo("Project Dashboard");
        assertThat(sight.getPermalink()).isEqualTo("https://app.smartsheet.com/sights/3456789012345678");
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(sight.getCreatedAt()).isEqualTo(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        assertThat(sight.getModifiedAt()).isEqualTo(ZonedDateTime.parse("2024-06-01T00:00:00Z"));
    }

    @Test
    void testGetSightPath_getSight() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSightPath.json"));

        SightPathNode result = sightResourcesImpl.getSightPath(1234567890L);
        PathLeaf leaf = result.getLeafSight();

        assertThat(leaf).isNotNull();
        assertThat(leaf.getName()).isEqualTo("Project Dashboard");
        assertThat(leaf.getId()).isEqualTo(3456789012345678L);
        assertThat(leaf.getPermalink()).isEqualTo("https://app.smartsheet.com/sights/3456789012345678");
        assertThat(leaf.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(leaf.getCreatedAt()).isEqualTo(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        assertThat(leaf.getModifiedAt()).isEqualTo(ZonedDateTime.parse("2024-06-01T00:00:00Z"));
    }

    @Test
    void testGetSightPath_getSightPath() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getSightPath.json"));

        SightPathNode result = sightResourcesImpl.getSightPath(1234567890L);

        assertThat(result.getLeafSightPath())
                .isEqualTo("/Sample Workspace/Project Plans/Project Plans Subfolder/Project Dashboard");
    }

    @Test
    void testGetSightPath_404_throwsResourceNotFoundException() throws IOException {
        server.setStatus(404);
        server.setResponseBody(new File("src/test/resources/notFoundError.json"));

        assertThatThrownBy(() -> sightResourcesImpl.getSightPath(1234567890L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetSightPath_500_throwsInvalidRequestException() throws IOException {
        server.setStatus(500);
        server.setResponseBody(new File("src/test/resources/notFoundError.json"));

        assertThatThrownBy(() -> sightResourcesImpl.getSightPath(1234567890L))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void testCopySight() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateSight.json"));

        Sight sight = sightResourcesImpl.copySight(12345L, new ContainerDestination());
        assertThat(sight).isNotNull();
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sight.getFavorite()).isFalse();
        assertThat(sight.getWidgets()).isEmpty();
        assertThat(sight.getWorkspace()).isNotNull();
        assertThat(sight.getPermalink()).isNotBlank();
    }

    @Test
    void testMoveSight() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateSight.json"));

        Sight sight = sightResourcesImpl.moveSight(12345L, new ContainerDestination());
        assertThat(sight).isNotNull();
        assertThat(sight.getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sight.getFavorite()).isFalse();
        assertThat(sight.getWidgets()).isEmpty();
        assertThat(sight.getWorkspace()).isNotNull();
        assertThat(sight.getPermalink()).isNotBlank();
    }
}
