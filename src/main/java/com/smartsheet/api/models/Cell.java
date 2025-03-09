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

import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.format.Format;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents the Cell object that holds data in a sheet.
 */
@Getter
@Setter
@ToString
@Jacksonized
@SuperBuilder
// We need to have a constructor with no arguments for the subclasses of this class to work
@NoArgsConstructor
// We need to have a constructor with all arguments for Lombok Builder to work
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cell {

    /**
     * Represents the column columnType.
     */
    private ColumnType columnType;

    /**
     * Represents the column columnType.
     */
    private ColumnType type;

    /**
     * Represents the value.
     */
    private Object value;

    /**
     * is an object representation of the cell's value and is currently used for adding or updating predecessor cell values
     */
    private ObjectValue objectValue;

    /**
     * Represents the display value.
     */
    private String displayValue;

    /**
     * Represents the column ID for this cell.
     */
    private Long columnId;

    /**
     * Represents the row ID for this cell.
     */
    private Long rowId;

    /**
     * Represents the hyperlink to a URL, sheet, or report.
     */
    private Hyperlink hyperlink;

    /**
     * Represents an inbound link from a cell in another sheet.
     */
    private CellLink linkInFromCell;

    /**
     * Represents an array of CellLink objects.
     */
    private List<CellLink> linksOutToCells;

    /**
     * Represents the format descriptor describing this cell’s conditional format.
     */
    private String conditionalFormat;

    /**
     * the image that the cell contains. Only returned if the cell contains an image.
     */
    private Image image;

    /**
     * The formula for the cell.
     */
    private String formula;

    /**
     * Represents the strict flag.
     */
    private Boolean strict;

    /**
     * Represents the {@link Format} for this cell.
     */
    private Format format;

    /**
     * ((Admin only) Flag indicating whether the cell value can contain a value outside of the validation
     * limits (value = true). When using this parameter, you must also set strict to false to bypass
     * value type checking. This property is honored for POST or PUT actions that update rows.
     */
    private Boolean overrideValidation;
}
