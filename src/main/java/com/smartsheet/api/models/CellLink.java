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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class CellLink {

    /**
     * One of the following values:
     * OK: the link is in a good state
     * BROKEN: the row or sheet linked to was deleted
     * INACCESSIBLE: the sheet linked to cannot be viewed by this user
     * Several other values indicating unusual error conditions: NOT_SHARED, BLOCKED, CIRCULAR, INVALID, and DISABLED .
     */
    private String status;

    /**
     * The Sheet ID of the sheet that the linked cell belongs to.
     */
    private Long sheetId;

    /**
     * The Row ID of the linked cell.
     */
    private Long rowId;

    /**
     * The Column ID of the linked cell.
     */
    private Long columnId;

    /**
     * The Sheet name of the linked cell.
     */
    private String sheetName;

    /**
     * Checks if the sheet, row, and column ID in the CellLink are all null
     *
     * @return boolean based on whether the IDs in the cell link are null or not
     */
    @JsonIgnore
    public boolean isNull() {
        return this.columnId == null && this.rowId == null && this.sheetId == null;
    }
}
