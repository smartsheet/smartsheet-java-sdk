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

import com.smartsheet.api.SmartsheetBuilder;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Home;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by kskeem on 3/1/16.
 */
class AbstractResourcesTest {

    private final String tokenValue = "somevalue";
    private final String changeAgent = "mychangeagent";

    @Test
    void testHeaders() {

        SmartsheetImpl smartsheet = new SmartsheetImpl("doesnt/matter", tokenValue, new DefaultHttpClient(), null);
        smartsheet.setChangeAgent(changeAgent);
        AbstractResources resources = new AbstractResources(smartsheet) {
        };

        Map<String, String> headers = resources.createHeaders();
        assertThat(headers)
                .containsEntry("Authorization", "Bearer " + tokenValue)
                .containsEntry("Smartsheet-Change-Agent", changeAgent);
    }

    @Test
    void createResourceWithObjectClassNull() {
        SmartsheetImpl smartsheetImpl = new SmartsheetImpl(
                SmartsheetBuilder.DEFAULT_BASE_URI,
                tokenValue,
                new DefaultHttpClient(),
                null
        );
        AbstractResources resources = new AbstractResources(smartsheetImpl) {
        };
        Home home = new Home();

        assertThatThrownBy(() -> resources.createResource("someValidPath", null, home))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
