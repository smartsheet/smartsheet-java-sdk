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
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sight;
import com.smartsheet.api.models.SightPublish;
import com.smartsheet.api.models.enums.AccessLevel;
import com.smartsheet.api.models.enums.SightInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        PagedResult<Sight> sightPagedResult = sightResourcesImpl.listSights(pagination, null);
        assertThat(sightPagedResult.getData()).isNotNull();
        assertThat(sightPagedResult.getData()).isNotEmpty();
        assertThat(sightPagedResult.getData()).hasSize(1);
        assertThat(sightPagedResult.getData().get(0).getAccessLevel()).isEqualTo(AccessLevel.VIEWER);
        assertThat(sightPagedResult.getData().get(0).getFavorite()).isFalse();
        assertThat(sightPagedResult.getData().get(0).getWidgets()).isEmpty();
        assertThat(sightPagedResult.getData().get(0).getWorkspace()).isNotNull();
        assertThat(sightPagedResult.getData().get(0).getPermalink()).isNotBlank();
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

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

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
