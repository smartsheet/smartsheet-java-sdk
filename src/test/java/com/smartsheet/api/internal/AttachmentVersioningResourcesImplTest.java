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
import com.smartsheet.api.models.enums.AttachmentParentType;
import com.smartsheet.api.models.enums.AttachmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class AttachmentVersioningResourcesImplTest extends ResourcesImplBase {

    private AttachmentVersioningResourcesImpl attachmentVersioningResources;

    @BeforeEach
    public void setUp() throws Exception {
        SmartsheetImpl smartsheetImpl = new SmartsheetImpl(
                "http://localhost:9090/1.1/",
                "accessToken",
                new DefaultHttpClient(),
                serializer
        );
        attachmentVersioningResources = new AttachmentVersioningResourcesImpl(smartsheetImpl);

    }

    @Test
    void testDeleteAllVersions() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteAttachment.json"));

        attachmentVersioningResources.deleteAllVersions(1234L, 5678L);
    }

    @Test
    void testListAllVersions() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listAttachmentVersions.json"));

        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Attachment> attachments = attachmentVersioningResources.listAllVersions(1234L, 456L, parameters);
        assertThat(attachments.getData().get(0).getName()).isNotNull();
        assertThat(attachments.getData().get(0).getId().longValue()).isEqualTo(4583173393803140L);
        assertThat(attachments.getData().get(1).getMimeType()).isEqualTo("image/png");
    }

    @Test
    void testAttachNewVersion() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/attachFile.json"));
        File file = new File("src/test/resources/large_sheet.pdf");
        Attachment attachment = attachmentVersioningResources.attachNewVersion(1234L, 345L, file,
                "application/pdf");
        assertThat(attachment.getId()).isEqualTo(7265404226692996L);
        assertThat(attachment.getName()).isEqualTo("Testing.PDF");
        assertThat(attachment.getAttachmentType()).isEqualTo(AttachmentType.FILE);
        assertThat(attachment.getMimeType()).isEqualTo("application/pdf");
        assertThat(attachment.getSizeInKb()).isEqualTo(1831L);
        assertThat(attachment.getParentType()).isEqualTo(AttachmentParentType.SHEET);
    }
}
