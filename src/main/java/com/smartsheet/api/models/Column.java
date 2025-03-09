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

import com.smartsheet.api.models.enums.ColumnTag;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.Symbol;
import com.smartsheet.api.models.enums.SystemColumnType;
import com.smartsheet.api.models.format.Format;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents the Column object.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Jacksonized
@Builder
// We need to have a constructor with no arguments for the subclasses of this class to work
@NoArgsConstructor
// We need to have a constructor with all arguments for Lombok Builder to work
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Column {
    /**
     * Represents the ID.
     */
    private Long id;

    /**
     * Represents the system column type.
     */
    private SystemColumnType systemColumnType;

    /**
     * Represents the column type.
     */
    private ColumnType type;

    /**
     * Represents the format for the auto generated numbers (if the SystemColumnType is an AUTO_NUMBER).
     */
    private AutoNumberFormat autoNumberFormat;

    /**
     * List containing contact options
     */
    private List<Contact> contactOptions;

    /**
     * Column description
     */
    private String description;

    /**
     * Represents the {@link Format} for this column.
     */
    private Format format;

    /**
     * The formula for a column, if set, for instance '=data@row'.
     */
    private String formula;

    /**
     * Represents the hidden flag for the column.
     */
    private Boolean hidden;

    /**
     * Represents the position.
     */
    private Integer index;

    /**
     * Represents if the column is locked
     */
    private Boolean locked;

    /**
     * Represents if the column is locked for the user
     */
    private Boolean lockedForUser;

    /**
     * Represents the list of options for the column.
     */
    private List<String> options;

    /**
     * Represents the primary flag.
     */
    private Boolean primary;

    /**
     * Represents the symbol used for the column.
     */
    private Symbol symbol;

    /**
     * Represents the tags to indicate a special type of column.
     */
    private List<ColumnTag> tags;

    /**
     * Represents the title.
     */
    private String title;

    /**
     * Flag indicating whether validation has been enabled for the column
     */
    private Boolean validation;

    /**
     * Determines the compatibility level of this client, 0 for existing types, 1 for multi-assign,
     * greater than 1 for future types.
     */
    private Integer version;

    /**
     * The width of the cell.
     */
    private Integer width;
}
