/*
 * Copyright (C) 2024 Smartsheet
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
import com.smartsheet.api.models.Comment;
import com.smartsheet.api.models.Discussion;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.DiscussionInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscussionResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    Sheet sheet;
    Discussion newDiscussionRow;
    Discussion newDiscussionSheet;
    Row row;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testDiscussionMethods() throws SmartsheetException, IOException {
        testCreateDiscussionOnSheet();
        testCreateDiscussionOnRow();
        testGetRowDiscussions();
        testGetAllDiscussions();
        testGetDiscussion();
        testDeleteDiscussion();
        testCreateDiscussionWithAttachmentOnRow();
    }

    public void testCreateDiscussionOnSheet() throws SmartsheetException, IOException {

        //create sheet
        sheet = smartsheet.sheetResources().createSheet(createSheetObject());

        //create comment to add to discussion
        Comment comment = new Comment.AddCommentBuilder().setText("This is a test comment").build();

        File file = new File("src/test/resources/small-text.txt");

        Discussion discussion = new Discussion.CreateDiscussionBuilder().setTitle("New Discussion").setComment(comment).build();
        newDiscussionSheet = smartsheet.sheetResources().discussionResources().createDiscussion(sheet.getId(), discussion);

        assertThat(newDiscussionSheet).isNotNull();
    }

    public void testCreateDiscussionOnRow() throws SmartsheetException, IOException {
        //add rows
        row = addRows(sheet.getId());

        //create comment to add to discussion
        Comment comment = new Comment.AddCommentBuilder().setText("This is a test comment").build();

        Discussion discussion = new Discussion.CreateDiscussionBuilder().setTitle("New Discussion").setComment(comment).build();
        Discussion newDiscussionWithAttachment = smartsheet
                .sheetResources()
                .rowResources()
                .discussionResources()
                .createDiscussion(sheet.getId(), row.getId(), discussion);

        assertThat(newDiscussionWithAttachment).isNotNull();
    }

    public void testCreateDiscussionWithAttachmentOnRow() throws SmartsheetException, IOException {

        //create sheet
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());

        //add rows
        row = addRows(sheet.getId());

        //create comment to add to discussion
        Comment comment = new Comment.AddCommentBuilder().setText("This is a test comment").build();

        Discussion discussion = new Discussion.CreateDiscussionBuilder().setTitle("New Discussion").setComment(comment).build();
        File file = new File("src/test/resources/small-text.txt");
        newDiscussionRow = smartsheet
                .sheetResources()
                .rowResources()
                .discussionResources()
                .createDiscussionWithAttachment(sheet.getId(), row.getId(), discussion, file, "text/plain");

        assertThat(newDiscussionRow).isNotNull();
    }

    public void testGetRowDiscussions() throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

        // Get all discussions (omit 'include' parameter and pagination parameters).
        smartsheet.sheetResources().rowResources().discussionResources().listDiscussions(sheet.getId(), row.getId(), null, null);

        // Get all discussions (specify 'include' parameter with values of 'comments' and 'attachments',
        // and 'includeAll' parameter with value of "true").
        EnumSet<DiscussionInclusion> discussionInclusions = EnumSet.of(DiscussionInclusion.COMMENTS, DiscussionInclusion.ATTACHMENTS);
        PagedResult<Discussion> newDiscussion = smartsheet
                .sheetResources()
                .rowResources()
                .discussionResources()
                .listDiscussions(sheet.getId(), row.getId(), parameters, discussionInclusions);
        assertThat(newDiscussion).isNotNull();
    }

    public void testGetAllDiscussions() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Discussion> newDiscussion = smartsheet
                .sheetResources()
                .discussionResources()
                .listDiscussions(sheet.getId(), null, null);
        newDiscussion = smartsheet
                .sheetResources()
                .discussionResources()
                .listDiscussions(sheet.getId(), parameters, EnumSet.of(DiscussionInclusion.COMMENTS, DiscussionInclusion.ATTACHMENTS));
        assertThat(newDiscussion).isNotNull();
    }

    public void testGetDiscussion() throws SmartsheetException {
        Discussion newDiscussion = smartsheet
                .sheetResources()
                .discussionResources()
                .getDiscussion(sheet.getId(), newDiscussionSheet.getId());

        assertThat(newDiscussion).isNotNull();
    }

    public void testDeleteDiscussion() throws SmartsheetException, IOException {
        smartsheet.sheetResources().discussionResources().deleteDiscussion(sheet.getId(), newDiscussionSheet.getId());
        //cleanup
        deleteSheet(sheet.getId());
    }
}
