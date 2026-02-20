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
 * One of [{@code type}, {@code systemColumnType}] or [{@code primary=true}] is required.
 * <p>
 * {@code systemColumnType} should be specified if you want to match a system column. Use {@code primary=true}
 * to match primary columns. When matching primary columns, {@code title} can be used to customize the primary
 * column name in the rendered report.
 * <p>
 * <b>Note:</b> Columns in the report are matched by the combination of {@code title} and {@code type}
 * (and {@code systemColumnType} if specified).
 * <p>
 * <b>Note:</b> {@code symbol} is not used for matching and as a result {@code CHECKBOX} or {@code PICKLIST}
 * columns with different symbols (from different sheets) can be combined into the same column in the report.
 * You cannot combine {@code CHECKBOX} with {@code PICKLIST} into the same column in the report because they
 * are different types.
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
}
