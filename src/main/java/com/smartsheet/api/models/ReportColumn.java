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
import com.smartsheet.api.models.enums.Symbol;
import com.smartsheet.api.models.enums.SystemColumnType;
import com.smartsheet.api.models.format.Format;

import java.util.List;

/**
 * Represents the “virtual” Column object for Report.
 */
public class ReportColumn extends Column {

    /**
     * Represents the virtual ID of the report column.
     */
    private Long virtualId;

    /**
     * Represents the special “Sheet Name” report column (value = “true”).
     */
    private boolean sheetNameColumn;

    /**
     * Gets the sheet name for the column.
     *
     * @return the sheet name
     */
    public boolean getSheetNameColumn() {
        return sheetNameColumn;
    }

    /**
     * Sets the sheet name for the column.
     *
     * @param sheetNameColumn the sheetname for column
     */
    public ReportColumn setSheetNameColumn(boolean sheetNameColumn) {
        this.sheetNameColumn = sheetNameColumn;
        return this;
    }

    /**
     * Gets the virtual id for the column.
     *
     * @return the virtual id
     */
    public Long getVirtualId() {
        return virtualId;
    }

    /**
     * Sets the virtual id for the column.
     *
     * @param virtualId the virtual id
     */

    public ReportColumn setVirtualId(Long virtualId) {
        this.virtualId = virtualId;
        return this;
    }

    /**
     * A convenience class to help create a ReportColumn object with the appropriate fields for adding to a report.
     */
    public static class AddReportColumnBuilder {
        private String title;
        private Integer index;
        private ColumnType type;
        private List<String> options;
        private Symbol symbol;
        private SystemColumnType systemColumnType;
        private AutoNumberFormat autoNumberFormat;
        private Integer width;
        private Boolean primary;
        private Format format;
        private Boolean validation;
        private boolean sheetNameColumn;

        /**
         * Sets the title for the column.
         *
         * @param title the title
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Gets the title.
         *
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Sets the index for the column.
         *
         * @param index the index
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setIndex(Integer index) {
            this.index = index;
            return this;
        }

        /**
         * Gets the index.
         *
         * @return the index
         */
        public Integer getIndex() {
            return index;
        }

        /**
         * Sets the type for the column.
         *
         * @param type the type
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setType(ColumnType type) {
            this.type = type;
            return this;
        }

        /**
         * Gets the type.
         *
         * @return the type
         */
        public ColumnType getType() {
            return type;
        }

        /**
         * Sets the options for the column.
         *
         * @param options the options
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setOptions(List<String> options) {
            this.options = options;
            return this;
        }

        /**
         * Gets the options.
         *
         * @return the options
         */
        public List<String> getOptions() {
            return options;
        }

        /**
         * Sets the symbol for the column.
         *
         * @param symbol the symbol
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setSymbol(Symbol symbol) {
            this.symbol = symbol;
            return this;
        }

        /**
         * Gets the symbol.
         *
         * @return the symbol
         */
        public Symbol getSymbol() {
            return symbol;
        }

        /**
         * Sets the system column type.
         *
         * @param systemColumnType the system column type
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setSystemColumnType(SystemColumnType systemColumnType) {
            this.systemColumnType = systemColumnType;
            return this;
        }

        /**
         * Gets the system column type.
         *
         * @return the system column type
         */
        public SystemColumnType getSystemColumnType() {
            return systemColumnType;
        }

        /**
         * Sets the format for an auto number column.
         *
         * @param autoNumberFormat the auto number format
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setAutoNumberFormat(AutoNumberFormat autoNumberFormat) {
            this.autoNumberFormat = autoNumberFormat;
            return this;
        }

        /**
         * Gets the auto number format.
         *
         * @return the auto number format
         */
        public AutoNumberFormat getAutoNumberFormat() {
            return autoNumberFormat;
        }

        /**
         * Sets the width for the column.
         *
         * @param width the width
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setWidth(Integer width) {
            this.width = width;
            return this;
        }

        /**
         * Gets the width.
         *
         * @return the width
         */
        public Integer getWidth() {
            return width;
        }

        /**
         * Sets the primary flag for the column.
         *
         * @param primary the primary flag
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setPrimary(Boolean primary) {
            this.primary = primary;
            return this;
        }

        /**
         * Gets the primary flag.
         *
         * @return the primary flag
         */
        public Boolean getPrimary() {
            return primary;
        }

        /**
         * Sets the format for the column.
         *
         * @param format the format
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setFormat(Format format) {
            this.format = format;
            return this;
        }

        /**
         * Gets the format.
         *
         * @return the format
         */
        public Format getFormat() {
            return format;
        }

        /**
         * Sets the validation flag for the column.
         *
         * @param validation the validation flag
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setValidation(Boolean validation) {
            this.validation = validation;
            return this;
        }

        /**
         * Gets the validation flag.
         *
         * @return the validation flag
         */
        public Boolean getValidation() {
            return validation;
        }

        /**
         * Sets the sheet name column flag.
         *
         * @param sheetNameColumn the sheet name column flag
         * @return the AddReportColumnBuilder
         */
        public AddReportColumnBuilder setSheetNameColumn(boolean sheetNameColumn) {
            this.sheetNameColumn = sheetNameColumn;
            return this;
        }

        /**
         * Gets the sheet name column flag.
         *
         * @return the sheet name column flag
         */
        public boolean getSheetNameColumn() {
            return sheetNameColumn;
        }

        /**
         * Builds the ReportColumn.
         *
         * @return the ReportColumn
         */
        public ReportColumn build() {
            if (title == null || type == null) {
                throw new InstantiationError();
            }

            ReportColumn column = new ReportColumn();
            column.setTitle(title);
            column.setType(type);
            column.setIndex(index);
            column.setOptions(options);
            column.setSymbol(symbol);
            column.setSystemColumnType(systemColumnType);
            column.setAutoNumberFormat(autoNumberFormat);
            column.setWidth(width);
            column.setPrimary(primary);
            column.setFormat(format);
            column.setValidation(validation);
            column.setSheetNameColumn(sheetNameColumn);
            return column;
        }
    }
}
