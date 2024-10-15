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

package com.smartsheet.api;

import com.smartsheet.api.models.Column;

/**
 * <p>This interface provides methods to access Column resources.</p>
 *
 * <p>Thread Safety: Implementation of this interface must be thread safe.</p>
 *
 * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public interface ColumnResources {

    /**
     * @param column the column to update
     * @return the updated Column (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    Column updateColumn(Column column);

    /**
     * @param id      id of the column
     * @param sheetId the sheet id
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    void deleteColumn(long id, long sheetId);
}
