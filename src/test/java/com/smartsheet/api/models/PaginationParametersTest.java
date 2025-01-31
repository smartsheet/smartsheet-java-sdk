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

package com.smartsheet.api.models;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationParametersTest {

    @Test
    void testPaginationParameters() {
        PaginationParameters parameters = new PaginationParameters(true, 1, 1);

        assertThat(parameters.isIncludeAll()).isTrue();
        assertThat(parameters.getPageSize().longValue()).isEqualTo(1);
        assertThat(parameters.getPage().longValue()).isEqualTo(1);
    }

    @Test
    void testToQueryString() {
        PaginationParameters parameters1 = new PaginationParameters(true, null, null);
        assertThat(parameters1.toQueryString()).isEqualTo("?includeAll=true");

        PaginationParameters parameters2 = new PaginationParameters(true, 1, 1);
        assertThat(parameters2.toQueryString()).isEqualTo("?includeAll=true");

        PaginationParameters parameters3 = new PaginationParameters(false, 1, 1);
        String[] matches1 = new String[]{"pageSize=1", "includeAll=false", "page=1"};
        for (String s : matches1) {
            assertThat(parameters3.toQueryString()).contains(s);
        }
    }

    @Test
    void testToHashMap() {
        PaginationParameters parameters1 = new PaginationParameters(true, null, null);
        Map<String, Object> map = parameters1.toHashMap();
        assertThat(map)
                .containsEntry("includeAll", "true")
                .doesNotContainKey("pageSize")
                .doesNotContainKey("page");

        PaginationParameters parameters2 = new PaginationParameters(true, 1, 1);
        map = parameters2.toHashMap();
        assertThat(map)
                .containsEntry("includeAll", "true")
                .doesNotContainKey("pageSize")
                .doesNotContainKey("page");

        PaginationParameters parameters3 = new PaginationParameters(false, 1, 1);
        map = parameters3.toHashMap();
        assertThat(map)
                .containsEntry("includeAll", "false")
                .containsEntry("pageSize", 1)
                .containsEntry("page", 1);
    }

    @Test
    void testBuilder() {
        PaginationParameters pagination = new PaginationParameters.PaginationParametersBuilder()
                .setIncludeAll(true)
                .setPageSize(2)
                .setPage(1)
                .build();
        assertThat(pagination.isIncludeAll()).isTrue();
        assertThat(pagination.getPageSize().longValue()).isEqualTo(2);
        assertThat(pagination.getPage().longValue()).isEqualTo(1);
    }
}
