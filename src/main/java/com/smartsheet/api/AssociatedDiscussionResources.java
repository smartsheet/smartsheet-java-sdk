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

import com.smartsheet.api.models.Discussion;

/**
 * @deprecated As of release 2.0. Please use {@link RowDiscussionResources} or {@link SheetDiscussionResources}
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public interface AssociatedDiscussionResources {
    /**
     * @param objectId   the object id (sheet id or row id)
     * @param discussion the discussion object
     * @return the created discussion
     * @throws SmartsheetException if there is any other error during the operation
     * @deprecated As of release 2.0. Please use {@link RowDiscussionResources} or {@link SheetDiscussionResources}
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    Discussion createDiscussion(long objectId, Discussion discussion) throws SmartsheetException;
}
