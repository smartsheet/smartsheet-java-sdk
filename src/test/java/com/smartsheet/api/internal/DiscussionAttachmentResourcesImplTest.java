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
import com.smartsheet.api.models.Attachment;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class DiscussionAttachmentResourcesImplTest extends ResourcesImplBase {

    private DiscussionAttachmentResourcesImpl discussionAttachmentResources;

    @BeforeEach
    public void setUp() throws Exception {
        discussionAttachmentResources = new DiscussionAttachmentResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Nested
    class GetAttachmentsTests {
        @Test
        void getAttachments_withParameters() throws SmartsheetException, IOException {
            server.setResponseBody(new File("src/test/resources/listAssociatedAttachments.json"));
            PaginationParameters parameters = new PaginationParameters(false, 1, 1);

            PagedResult<Attachment> attachments = discussionAttachmentResources.getAttachments(1234L, 456L, parameters);
            assertThat(attachments.getTotalCount()).isEqualTo(2);
            assertThat(attachments.getData().get(0).getId()).isEqualTo(4583173393803140L);
        }

        @Test
        void getAttachments_nullParameters() throws SmartsheetException, IOException {
            server.setResponseBody(new File("src/test/resources/listAssociatedAttachments.json"));

            PagedResult<Attachment> attachments = discussionAttachmentResources.getAttachments(1234L, 456L, null);
            assertThat(attachments.getTotalCount()).isEqualTo(2);
            assertThat(attachments.getData().get(0).getId()).isEqualTo(4583173393803140L);
        }
    }
}
