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

package com.smartsheet.api.integrationtest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.Attachment;
import com.smartsheet.api.models.Comment;
import com.smartsheet.api.models.Discussion;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.AttachmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AttachmentResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    File file;
    long sheetId;
    long rowId;
    long commentId;
    long discussionId;
    long sheetAttachmentId;
    long attachmentWithVersionId;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testAttachmentMethods() throws SmartsheetException, IOException {
        //smartsheet.setAssumedUser("ericyan99@gmail.com");
        //UserProfile user= smartsheet.userResources().getCurrentUser();
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());
        sheetId = sheet.getId();

        file = new File("src/test/resources/small-text.txt");

        testattachFileSheet();
        testattachFileRow();
        testattachFileComment();
        testattachUrl();
        testListAttachments();
        testAttachNewVersion();
        testListAllVersions();
        testDeleteAllVersions();
        testDeleteAttachment();
    }

    public void testattachFileSheet() throws SmartsheetException, IOException {

        //attach file to sheet
        Attachment attachment = smartsheet.sheetResources().attachmentResources().attachFile(sheetId, file,
                "text/plain");
        testGetAttachmentSheet(attachment.getId());
    }

    public void testGetAttachmentSheet(long attachmentId) throws SmartsheetException {
        Attachment attachment = smartsheet.sheetResources().attachmentResources().getAttachment(sheetId, attachmentId);
        assertThat(attachment).isNotNull();
        sheetAttachmentId = attachment.getId();
    }

    public void testattachFileRow() throws SmartsheetException, IOException {
        //add rows
        Row row = addRows(sheetId);
        rowId = row.getId();

        //attach file to row
        Attachment attachment = smartsheet
                .sheetResources()
                .rowResources()
                .attachmentResources()
                .attachFile(sheetId, rowId, file, "text/plain");
        testGetAttachmentRow();
    }

    public void testGetAttachmentRow() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Attachment> attachments = smartsheet
                .sheetResources()
                .rowResources()
                .attachmentResources()
                .getAttachments(sheetId, rowId, parameters);
        assertThat(attachments).isNotNull();
    }

    public void testattachFileComment() throws SmartsheetException, IOException {
        //create comment to add to discussion
        Comment comment = new Comment.AddCommentBuilder().setText("This is a test comment").build();

        Discussion discussion = new Discussion.CreateDiscussionBuilder().setTitle("New Discussion").setComment(comment).build();
        discussion = smartsheet.sheetResources().discussionResources().createDiscussion(sheetId, discussion);

        //comment = smartsheet.sheetResources().discussionResources().comments().addComment(sheetId,discussion.getId(), comment);
        comment = discussion.getComments().get(0);
        commentId = comment.getId();
        discussionId = discussion.getId();

        File file1 = new File("src/test/resources/small-text.txt");
        //attach file to comment
        Attachment attachment = smartsheet.sheetResources().commentResources().attachmentResources().attachFile(sheetId, commentId, file1,
                "text/plain");
        testGetAttachmentComment(attachment.getId());
    }

    public void testGetAttachmentComment(long attachmentId) throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Attachment> attachments = smartsheet
                .sheetResources()
                .discussionResources()
                .attachmentResources()
                .getAttachments(sheetId, discussionId, parameters);
        assertThat(attachments).isNotNull();
    }

    public void testattachUrl() throws SmartsheetException {

        Attachment attachment = new Attachment.CreateAttachmentBuilder()
                .setUrl("https://www.smartsheet.com")
                .setAttachmentType(AttachmentType.LINK)
                .setName("New Name")
                .build();

        //attach file to sheet
        Attachment attachedUrl = smartsheet.sheetResources().attachmentResources().attachUrl(sheetId, attachment);
        assertThat(attachedUrl).isNotNull();

        //attach file to row
        attachedUrl = smartsheet.sheetResources().rowResources().attachmentResources().attachUrl(sheetId, rowId, attachment);
        assertThat(attachedUrl).isNotNull();

        //attach file to comment
        attachedUrl = smartsheet.sheetResources().commentResources().attachmentResources().attachUrl(sheetId, commentId, attachment);
        assertThat(attachedUrl).isNotNull();

    }

    public void testListAttachments() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

        PagedResult<Attachment> attachments = smartsheet.sheetResources().attachmentResources().listAttachments(sheetId, parameters);
        assertThat(attachments).isNotNull();
    }

    public void testDeleteAttachment() throws SmartsheetException, IOException {
        deleteSheet(sheetId);
    }

    public void testAttachNewVersion() throws SmartsheetException, IOException {
        Attachment attachment = smartsheet
                .sheetResources()
                .attachmentResources()
                .versioningResources()
                .attachNewVersion(sheetId, sheetAttachmentId, file, "text/plain");
        assertThat(attachment).isNotNull();
        attachmentWithVersionId = attachment.getId();
    }

    public void testListAllVersions() throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Attachment> attachments = smartsheet
                .sheetResources()
                .attachmentResources()
                .versioningResources()
                .listAllVersions(sheetId, attachmentWithVersionId, parameters);
        PagedResult<Attachment> attachments1 = smartsheet
                .sheetResources()
                .attachmentResources()
                .versioningResources()
                .listAllVersions(sheetId, attachmentWithVersionId, null);
        assertThat(attachments).isNotNull();
    }

    public void testDeleteAllVersions() throws SmartsheetException {
        smartsheet.sheetResources().attachmentResources().versioningResources().deleteAllVersions(sheetId, attachmentWithVersionId);
    }
}
