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

import com.smartsheet.api.internal.http.DefaultHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiscussionAttachmentResourcesTest extends ResourcesImplBase {

    private DiscussionAttachmentResources discussionAttachmentResources;

    @BeforeEach
    public void setUp() throws Exception {
        discussionAttachmentResources = new DiscussionAttachmentResources(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testAttachFileLongFileString() {
        File file = new File("src/test/rescoures/getPDF.pdf");
        assertThatThrownBy(() -> discussionAttachmentResources.attachFile(1234L, file, "application/pdf"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testAttachFileLongFileStringLong() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        assertThatThrownBy(() -> discussionAttachmentResources.attachFile(1234L, inputStream, "application/pdf", 1234L, "file.pdf"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
