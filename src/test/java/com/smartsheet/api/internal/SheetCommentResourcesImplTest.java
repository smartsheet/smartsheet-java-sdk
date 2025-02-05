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
import com.smartsheet.api.models.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class SheetCommentResourcesImplTest extends ResourcesImplBase {
    private SheetCommentResourcesImpl sheetCommentResources;

    @BeforeEach
    public void setUp() throws Exception {
        sheetCommentResources = new SheetCommentResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetComment() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getComment.json"));

        Comment comment = sheetCommentResources.getComment(1234L, 1245L);
        assertThat(comment.getId().longValue()).isEqualTo(3831661625403268L);
        assertThat(comment.getText()).isEqualTo("This text is the body of the first comment");
        assertThat(comment.getCreatedBy().getName()).isEqualTo("Brett Batie");
        assertThat(comment.getCreatedBy().getEmail()).isEqualTo("email@email.com");

        // Test equals method
        Comment newComment = new Comment();
        newComment.setId(3831661625403268L);
        assertThat(newComment).hasSameHashCodeAs(comment);
    }

    @Test
    void testDeleteComment() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteComment.json"));

        sheetCommentResources.deleteComment(1234L, 2345L);
    }

}
