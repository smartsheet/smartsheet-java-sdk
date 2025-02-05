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

import com.smartsheet.api.AssociatedAttachmentResources;
import com.smartsheet.api.CommentResources;
import com.smartsheet.api.SheetCommentResources;
import com.smartsheet.api.models.Comment;

/**
 * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public class CommentResourcesImpl extends AbstractResources implements CommentResources {
    private static final String METHOD_MOVED_MESSAGE = "Method moved to SheetCommentResources.";

    /**
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public CommentResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public AssociatedAttachmentResources attachments() {
        throw new UnsupportedOperationException(METHOD_MOVED_MESSAGE);
    }

    /**
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public Comment getComment(long sheetId, long commentId) {
        throw new UnsupportedOperationException(METHOD_MOVED_MESSAGE);
    }

    /**
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public void deleteComment(long sheetId, long commentId) {
        throw new UnsupportedOperationException(METHOD_MOVED_MESSAGE);
    }
}
