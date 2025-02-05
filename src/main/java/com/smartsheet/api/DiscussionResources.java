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
 * <p>This interface provides methods to access Discussion resources.</p>
 *
 * <p>Thread Safety: Implementation of this interface must be thread safe.</p>
 */
public interface DiscussionResources {

    /**
     * <p>Add a comment to a discussion.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /discussion/{discussionId}/comments</p>
     *
     * @param id      the discussion id
     * @param comment the comment to add
     * @return the created comment
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    Comment addDiscussionComment(long id, Comment comment) throws SmartsheetException;

    /**
     * @return associated resources
     * @deprecated As of release 2.0. Please use the corresponding method in the [Row/Sheet/etc]AttachmentResources classes
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    AssociatedAttachmentResources attachments();

    /**
     * <p>Represents the DiscussionCommentResources.</p>
     * <p>It will be initialized in constructor and will not change afterwards.</p>
     *
     * @return comments object
     */
    DiscussionCommentResources comments();
}
