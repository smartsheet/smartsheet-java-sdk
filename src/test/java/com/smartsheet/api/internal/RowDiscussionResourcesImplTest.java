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
import com.smartsheet.api.models.Comment;
import com.smartsheet.api.models.Discussion;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.DiscussionInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RowDiscussionResourcesImplTest extends ResourcesImplBase {

    private RowDiscussionResourcesImpl discussionRowResources;

    @BeforeEach
    public void setUp() throws Exception {
        discussionRowResources = new RowDiscussionResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testCreateDiscussion() throws Exception {
        server.setResponseBody(new File("src/test/resources/createDiscussionOnRow.json"));

        Discussion discussion = new Discussion();
        discussion.setTitle("new discussion");
        Discussion newDiscussion = discussionRowResources.createDiscussion(1234L, 5678L, discussion);
        assertThat(newDiscussion.getTitle()).isEqualTo("This is a new discussion");
        assertThat(newDiscussion.getId()).isEqualTo(4583173393803140L);
    }

    @Test
    void testCreateDiscussionWithAttachment() throws Exception {
        server.setResponseBody(new File("src/test/resources/createDiscussionOnRow.json"));
        File file = new File("src/test/resources/large_sheet.pdf");
        Comment comment = new Comment.AddCommentBuilder().setText("New comment").build();
        Discussion discussion = new Discussion.CreateDiscussionBuilder().setComment(comment).setTitle("Some title").build();

        Discussion newDiscussion = discussionRowResources.createDiscussionWithAttachment(123L, 456L, discussion, file, "application/pdf");
        assertThat(newDiscussion.getTitle()).isEqualTo("This is a new discussion");
    }

    @Test
    void testCreateDiscussionWithAttachment_InputValidation() {
        File file = new File("src/test/resources/large_sheet.pdf");
        Comment comment = new Comment.AddCommentBuilder().setText("New comment").build();
        Discussion discussion = new Discussion.CreateDiscussionBuilder().setComment(comment).setTitle("Some title").build();

        assertThatThrownBy(() -> discussionRowResources.createDiscussionWithAttachment(123L, 456L, null, file, "application/pdf"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> discussionRowResources.createDiscussionWithAttachment(123L, 456L, discussion, null, "application/pdf"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> discussionRowResources.createDiscussionWithAttachment(123L, 456L, discussion, file, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testListDiscussions() throws Exception {
        server.setResponseBody(new File("src/test/resources/getRowDiscussions.json"));

        Discussion discussion = new Discussion();
        discussion.setTitle("new discussion");
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Discussion> newDiscussion = discussionRowResources.listDiscussions(
                1234L,
                5678L,
                parameters,
                EnumSet.of(DiscussionInclusion.COMMENTS)
        );
        assertThat(newDiscussion.getData().get(0).getTitle()).isEqualTo("Lincoln");
        assertThat(newDiscussion.getData().get(0).getId()).isEqualTo(3138415114905476L);
        assertThat(newDiscussion.getData().get(0).getComments().get(0).getAttachments().get(0).getName()).isEqualTo("test.html");
        assertThat(server.getLastRequestUrl())
                .isEqualTo("/1.1/sheets/1234/rows/5678/discussions?include=comments&pageSize=1&includeAll=false&page=1");
    }
}
