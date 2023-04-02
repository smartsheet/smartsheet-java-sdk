package com.smartsheet.api.internal;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DiscussionResourcesImplTest extends ResourcesImplBase {

    private DiscussionResourcesImpl discussionResources;

    @BeforeEach
    public void setUp() throws Exception {
        discussionResources = new DiscussionResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testAddDiscussionComment() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/addDiscussionComment.json"));

        Comment comment = new Comment();
        comment.setText("Some new Text");

        Comment newComment = discussionResources.addDiscussionComment(1234L, comment);

        assertEquals("This is a new comment.",newComment.getText());
        assertEquals("John Doe", newComment.getCreatedBy().getName());
    }

    @Test
    void testAttachments() {
        assertNull(discussionResources.attachments());
    }
}
