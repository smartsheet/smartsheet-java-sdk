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
import com.smartsheet.api.models.enums.ReportSystemColumnType;

/**
 * Object used to match a sheet column for a report.
 * <p>
 * Requires one of:
 * <ul>
 * <li>[{@code type}, {@code title}] for <b>regular columns</b></li>
 * <li>[{@code type}, {@code systemColumnType}] for <b>system columns</b></li>
 * <li>[{@code type=TEXT_NUMBER}, {@code primary=true}] for the <b>primary column</b></li>
 * <li>[{@code type=TEXT_NUMBER}, {@code sheetNameColumn=true}] for the special <b>sheet name report column</b></li>
 * </ul>
 * <p>
 * <b>Note:</b> You can combine multiple {@code CHECKBOX} columns or multiple {@code PICKLIST} columns from
 * different sheets into a single report column, even if their underlying symbols differ. However, you can't
 * combine a {@code CHECKBOX} column with a {@code PICKLIST} column, because they're different types.
 */
public class ReportColumnIdentifier {

    /**
     * Column title to be matched from the source sheets.
     * <p>
     * Note: If {@code primary} is true, then this property can be used to customize the primary column title.
     */
    private String title;

    /**
     * Column type to be matched from the source sheets.
     */
    private ColumnType type;

    /**
     * System column type to be matched from the source sheets.
     */
    private ReportSystemColumnType systemColumnType;

    /**
     * Indicates if the matched column is primary (default: false).
     */
    private Boolean primary;

    /**
     * Indicates if this is the special sheet name report column (default: false).
     */
    private Boolean sheetNameColumn;

    /**
     * Gets the column title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the column title.
     * <p>
     * If primary is true, this can be used to customize the primary column title in the report.
     *
     * @param title the title
     */
    public ReportColumnIdentifier setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Gets the column type.
     *
     * @return the type
     */
    public ColumnType getType() {
        return type;
    }

    /**
     * Sets the column type.
     *
     * @param type the type
     */
    public ReportColumnIdentifier setType(ColumnType type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the system column type.
     *
     * @return the system column type
     */
    public ReportSystemColumnType getSystemColumnType() {
        return systemColumnType;
    }

    /**
     * Sets the system column type.
     *
     * @param systemColumnType the system column type
     */
    public ReportColumnIdentifier setSystemColumnType(ReportSystemColumnType systemColumnType) {
        this.systemColumnType = systemColumnType;
        return this;
    }

    /**
     * Gets whether this identifies the primary column.
     *
     * @return true if this is the primary column, false otherwise
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     * Sets whether this identifies the primary column.
     *
     * @param primary true if this is the primary column, false otherwise
     */
    public ReportColumnIdentifier setPrimary(Boolean primary) {
        this.primary = primary;
        return this;
    }

    /**
     * Gets whether this identifies the sheet name report column.
     *
     * @return true if this is the sheet name column, false otherwise
     */
    public Boolean getSheetNameColumn() {
        return sheetNameColumn;
    }

    /**
     * Sets whether this identifies the sheet name report column.
     *
     * @param sheetNameColumn true if this is the sheet name column, false otherwise
     */
    public ReportColumnIdentifier setSheetNameColumn(Boolean sheetNameColumn) {
        this.sheetNameColumn = sheetNameColumn;
        return this;
    }
}
