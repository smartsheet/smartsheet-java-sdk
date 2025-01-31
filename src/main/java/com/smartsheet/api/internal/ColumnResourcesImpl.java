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

import com.smartsheet.api.ColumnResources;
import com.smartsheet.api.SheetCommentResources;
import com.smartsheet.api.models.Column;

/**
 * This is the implementation of the ColumnResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 *
 * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public class ColumnResourcesImpl implements ColumnResources {

    /**
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public ColumnResourcesImpl() {

    }

    /**
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public Column updateColumn(Column column) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated As of release 2.0. Please use {@link SheetCommentResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public void deleteColumn(long id, long sheetId) {
        throw new UnsupportedOperationException();
    }
}
