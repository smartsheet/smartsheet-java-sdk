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

package com.smartsheet.api.models;

/**
 * Created by kskeem on 3/1/16.
 */
public class BulkRowFailedItem {
    private int index;
    private Error error;
    private Long rowId;

    public int getIndex() {
        return index;
    }

    /**
     * Set the index
     */
    public BulkRowFailedItem setIndex(int index) {
        this.index = index;
        return this;
    }

    public Error getError() {
        return error;
    }

    /**
     * Set the Error
     */
    public BulkRowFailedItem setError(Error error) {
        this.error = error;
        return this;
    }

    public Long getRowId() {
        return rowId;
    }

    /**
     * Set the Row ID
     */
    public BulkRowFailedItem setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }
}
