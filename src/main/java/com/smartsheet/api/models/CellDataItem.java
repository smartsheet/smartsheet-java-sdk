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

package com.smartsheet.api.models;

import com.smartsheet.api.models.format.Format;

public class CellDataItem {

    /**
     * Column Id for the cell
     */
    private Long columnId;

    /**
     * Row Id for each item
     */
    private Long rowId;

    /**
     * Sheet Id for each item
     */
    private Long sheetId;

    /**
     * The type of data returned will depend on the cell type and the data in the cell
     */
    private Object objectValue;

    /**
     * The cell object
     */
    private Cell cell;

    /**
     * CELL or SUMMARY_FIELD
     */
    private String dataSource;

    /**
     * Label for the data point. This will either be the column name or a user-provided string.
     */
    private String label;

    /**
     * Format descriptor for the label
     */
    private Format labelFormat;

    /**
     * The display order for the CellDataItem
     */
    private Integer order;

    /**
     * The format descriptor for the value
     */
    private Format valueFormat;

    /**
     * SummaryField object if dataSource is SUMMARY_FIELD
     */
    private SummaryField profileField;

    /**
     * Get the column Id for the cell.
     *
     * @return columnId
     */
    public Long getColumnId() {
        return columnId;
    }

    /**
     * Set the column Id for the cell.
     * @param columnId  id for a given column
     * @return data object for a given cell
     */
    public CellDataItem setColumnId(Long columnId) {
        this.columnId = columnId;
        return this;
    }

    /**
     * Get the row Id for the item
     *
     * @return rowId
     */
    public Long getRowId() {
        return rowId;
    }

    /**
     * Set the row Id for the item
     * @param rowId  id for a given row
     * @return data object for a given cell
     */
    public CellDataItem setRowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    /**
     * Get the sheet Id for the item
     */
    public Long getSheetId() {
        return sheetId;
    }

    /**
     * Set the sheet Id for the item
     * @param sheetId  id for a given sheet
     * @return data object for a given cell
     */
    public CellDataItem setSheetId(Long sheetId) {
        this.sheetId = sheetId;
        return this;
    }

    /**
     * Get the object for this CellDataItem. The type of the data returned will depend on
     * the cell type and the data in the cell.
     *
     * @return objectValue
     */
    public Object getObjectValue() {
        return objectValue;
    }

    /**
     * Set the object for this CellDataItem. The type of the data returned will depend on
     * the cell type and the data in the cell.
     * @param objectValue  data for a given cell
     * @return data object for a given cell
     */
    public CellDataItem setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
        return this;
    }

    /**
     * Get the cell object
     *
     * @return cell
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Set the cell object
     * @param cell  cell object that holds data in a sheet
     * @return data object for a given cell
     */
    public CellDataItem setCell(Cell cell) {
        this.cell = cell;
        return this;
    }

    /**
     * Gets the data source (currently CELL)
     *
     * @return CELL
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * Sets the data source
     * @param dataSource  data in a cell
     * @return data object for a given cell
     */
    public CellDataItem setDataSource(String dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    /**
     * Get the label for the data point.
     *
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label for the data point.
     * @param label  label for cell
     * @return data object for a given cell
     */
    public CellDataItem setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * Get the format descriptor for the label
     *
     * @return labelFormat
     */
    public Format getLabelFormat() {
        return labelFormat;
    }

    /**
     * Set the format descriptor for the label
     * @param labelFormat  label format for cell
     * @return data object for a given cell
     */
    public CellDataItem setLabelFormat(Format labelFormat) {
        this.labelFormat = labelFormat;
        return this;
    }

    /**
     * Get the display order for the CellDataItem
     *
     * @return order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Set the display order for the CellDataItem
     * @param order  order for cell
     * @return data object for a given cell
     */
    public CellDataItem setOrder(Integer order) {
        this.order = order;
        return this;
    }

    /**
     * Get the format descriptor for the cell value
     *
     * @return valueFormat
     */
    public Format getValueFormat() {
        return valueFormat;
    }

    /**
     * Set the format descriptor for the cell value
     * @param valueFormat  format for value in cell
     * @return data object for a given cell
     */
    public CellDataItem setValueFormat(Format valueFormat) {
        this.valueFormat = valueFormat;
        return this;
    }

    /**
     * Get the SummaryField when dataSource is SUMMARY_FIELD
     *
     * @return summaryField
     */
    public SummaryField getProfileField() {
        return profileField;
    }

    /**
     * Sets the SummaryField if dataSource is SUMMARY_FIELD
     * @param profileField  profile field
     * @return data object for a given cell
     */
    public CellDataItem setProfileField(SummaryField profileField) {
        this.profileField = profileField;
        return this;
    }
}
