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

package com.smartsheet.api;

import com.smartsheet.api.models.Comment;

/**
 * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public interface CommentResources {
    /**
     * @param sheetId   the id
     * @param commentId the commentid
     * @return the comment (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    Comment getComment(long sheetId, long commentId);

    /**
     * @param sheetId   the id
     * @param commentId the commentid
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    void deleteComment(long sheetId, long commentId);

    /**
     * @return associated resources
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    AssociatedAttachmentResources attachments();
}
