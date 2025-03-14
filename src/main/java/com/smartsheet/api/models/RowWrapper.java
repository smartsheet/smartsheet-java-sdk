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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents the RowWrapper object that is used to specify the location for a {@link Row} or set of Rows.
 */
@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class RowWrapper {
    /**
     * Represents to-top flag that puts the row at the top of the sheet.
     */
    private Boolean toTop;

    /**
     * Represents to-bottom flag that puts the row at the bottom of the sheet.
     */
    private Boolean toBottom;

    /**
     * Represents the parent ID that puts the row as the first child of the specified id.
     */
    private Long parentId;

    /**
     * Represents the sibling ID that puts the row as the next row at the same hierarchical level of this row.
     */
    private Long siblingId;

    /**
     * Represents the rows.
     */
    private List<Row> rows;
}
