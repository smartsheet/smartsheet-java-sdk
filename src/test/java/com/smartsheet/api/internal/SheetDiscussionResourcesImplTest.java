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

import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Comment;
import com.smartsheet.api.models.Discussion;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.enums.DiscussionInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SheetDiscussionResourcesImplTest extends ResourcesImplBase {
    private SheetDiscussionResourcesImpl sheetDiscussionResources;

    @BeforeEach
    public void setUp() throws Exception {
        sheetDiscussionResources = new SheetDiscussionResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testCreateDiscussion() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createDiscussion.json"));

        // Test success
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setText("This is a test.");
        comment.setAttachments(new ArrayList<>());
        comments.add(comment);
        Discussion discussion = new Discussion();
        discussion.setTitle("New Discussion");
        discussion.setComments(comments);
        discussion.setLastCommentedUser(new User());
        discussion.setLastCommentedAt(new Date());
        discussion.setCommentAttachments(new ArrayList<>());
        Discussion newDiscussion = sheetDiscussionResources.createDiscussion(1234L, discussion);

        assertThat(newDiscussion.getComments()).isNotNull();
        assertThat(newDiscussion.getComments()).hasSize(1);
        assertThat(newDiscussion.getComments().get(0).getCreatedBy().getName()).isEqualTo("Brett Batie");
        assertThat(newDiscussion.getComments().get(0).getCreatedBy().getEmail()).isEqualTo("email@email.com");

        // Test failure - CreatedBy not allowed & only one comment can be added when creating a discussion.
        server.setStatus(400);
        server.setResponseBody(new File("src/test/resources/createDiscussion_1032.json"));
        comment = new Comment();
        User user = new User();
        user.setName("John Doe");
        user.setEmail("email@email.com");
        comment.setCreatedBy(user);
        comment.setText("This is a test.");
        comments.add(comment);
        discussion.setComments(comments);
        assertThatThrownBy(() -> sheetDiscussionResources.createDiscussion(1234L, discussion))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void testCreateDiscussionWithAttachment() throws Exception {
        server.setResponseBody(new File("src/test/resources/createDiscussion.json"));
        File file = new File("src/test/resources/large_sheet.pdf");
        // Test success
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setText("This is a test.");
        comment.setAttachments(new ArrayList<>());
        comments.add(comment);
        Discussion discussion = new Discussion();
        discussion.setTitle("New Discussion");
        discussion.setComments(comments);
        discussion.setLastCommentedUser(new User());
        discussion.setLastCommentedAt(new Date());
        discussion.setCommentAttachments(new ArrayList<>());
        Discussion newDiscussion = sheetDiscussionResources.createDiscussionWithAttachment(1234L, discussion, file, "application/pdf");

        assertThat(newDiscussion.getComments()).isNotNull();
        assertThat(newDiscussion.getComments()).hasSize(1);
        assertThat(newDiscussion.getComments().get(0).getCreatedBy().getName()).isEqualTo("Brett Batie");
        assertThat(newDiscussion.getComments().get(0).getCreatedBy().getEmail()).isEqualTo("email@email.com");

        // Test failure - CreatedBy not allowed & only one comment can be added when creating a discussion.
        server.setStatus(400);
        server.setResponseBody(new File("src/test/resources/createDiscussion_1032.json"));
        comment = new Comment();
        User user = new User();
        user.setName("John Doe");
        user.setEmail("email@email.com");
        comment.setCreatedBy(user);
        comment.setText("This is a test.");
        comments.add(comment);
        discussion.setComments(comments);
        assertThatThrownBy(() -> sheetDiscussionResources.createDiscussion(1234L, discussion))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void testGetDiscussion() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getDiscussion.json"));

        Discussion discussion = sheetDiscussionResources.getDiscussion(1234L, 5678L);

        assertThat(discussion.getTitle()).isEqualTo("New Discussion");
        assertThat(discussion.getComments()).isNotNull();
        assertThat(discussion.getComments()).hasSize(3);
        assertThat(discussion.getComments().get(0).getText()).isEqualTo("This text is the body of the first comment4");
        assertThat(discussion.getComments().get(0).getCreatedBy()).isNotNull();
        assertThat(discussion.getComments().get(0).getCreatedBy().getName()).isEqualTo("Brett Batie");
        assertThat(discussion.getComments().get(0).getCreatedBy().getEmail()).isEqualTo("email@email.com");
    }

    @Test
    void testDeleteDiscussion() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteDiscussion.json"));

        sheetDiscussionResources.deleteDiscussion(1234L, 2345L);
    }

    @Test
    void testGetAllDiscussions() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getAllDiscussions.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);

        PagedResult<Discussion> newDiscussion = sheetDiscussionResources.listDiscussions(
                123L,
                parameters,
                EnumSet.of(DiscussionInclusion.COMMENTS)
        );
        assertThat(newDiscussion.getTotalPages()).isEqualTo(1);
        assertThat(newDiscussion.getPageSize()).isEqualTo(100);
        assertThat(newDiscussion.getTotalCount()).isEqualTo(1);
        assertThat(newDiscussion.getData()).hasSize(1);
    }
}
