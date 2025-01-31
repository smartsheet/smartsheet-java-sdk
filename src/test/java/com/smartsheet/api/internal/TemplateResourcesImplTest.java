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
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Template;
import com.smartsheet.api.models.enums.AccessLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateResourcesImplTest extends ResourcesImplBase {

    private TemplateResourcesImpl templateResources;

    @BeforeEach
    public void setUp() throws Exception {
        templateResources = new TemplateResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListTemplates() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/listTemplates.json"));

        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Template> templates = templateResources.listUserCreatedTemplates(parameters);

        assertThat(templates).isNotNull();
        assertThat(templates.getData().get(0).getName()).isEqualTo("template 1");
        assertThat(templates.getData().get(0).getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(templates.getData().get(0).getId().longValue()).isEqualTo(3457273486960516L);
        assertThat(templates.getData().get(0).getDescription()).isEqualTo("This is template 1");
    }

    @Test
    void testListPublicTemplates() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/listTemplates.json"));

        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Template> templates = templateResources.listPublicTemplates(parameters);

        assertThat(templates).isNotNull();
        assertThat(templates.getData().get(0).getName()).isEqualTo("template 1");
        assertThat(templates.getData().get(0).getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(templates.getData().get(0).getId().longValue()).isEqualTo(3457273486960516L);
        assertThat(templates.getData().get(0).getDescription()).isEqualTo("This is template 1");
    }
}
