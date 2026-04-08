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

import com.smartsheet.api.models.enums.ReportFilterOperator;

import java.util.List;

/**
 * Represents a single filter criterion.
 */
public class ReportFilterCriterion {

    /**
     * The column to filter on.
     */
    private ReportColumnIdentifier column;

    /**
     * The condition operator.
     */
    private ReportFilterOperator operator;

    /**
     * List of filter values.
     * <p>
     * Valid value types for report filters:
     * <ul>
     * <li>{@link StringObjectValue} - for string values</li>
     * <li>{@link NumberObjectValue} - for numeric values</li>
     * <li>{@link DateObjectValue} - for DATE objectType</li>
     * <li>null values are supported</li>
     * </ul>
     * <p>
     * Note: The ObjectValue implementations handle serialization correctly, with primitives
     * serialized as raw values and objects (like DATE) serialized with objectType structure.
     */
    private List<ObjectValue> values;

    /**
     * Gets the column identifier.
     *
     * @return the column
     */
    public ReportColumnIdentifier getColumn() {
        return column;
    }

    /**
     * Sets the column identifier.
     *
     * @param column the column
     */
    public ReportFilterCriterion setColumn(ReportColumnIdentifier column) {
        this.column = column;
        return this;
    }

    /**
     * Gets the condition operator.
     *
     * @return the operator
     */
    public ReportFilterOperator getOperator() {
        return operator;
    }

    /**
     * Sets the condition operator.
     *
     * @param operator the operator
     */
    public ReportFilterCriterion setOperator(ReportFilterOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Gets the filter values.
     * <p>
     * Values are ObjectValue implementations representing different types:
     * <ul>
     * <li>{@link StringObjectValue} - string values</li>
     * <li>{@link NumberObjectValue} - numeric values</li>
     * <li>{@link DateObjectValue} - date values with objectType</li>
     * <li>null - for null/empty values</li>
     * </ul>
     *
     * @return the values
     */
    public List<ObjectValue> getValues() {
        return values;
    }

    /**
     * Sets the filter values.
     * <p>
     * Values should be ObjectValue implementations:
     * <ul>
     * <li>{@link StringObjectValue} - for string values</li>
     * <li>{@link NumberObjectValue} - for numeric values</li>
     * <li>{@link DateObjectValue} - for date values with objectType</li>
     * <li>null - for null/empty values</li>
     * </ul>
     * <p>
     * Example usage:
     * <pre>
     * criterion.setValues(Arrays.asList(
     *     new StringObjectValue("value1"),
     *     new NumberObjectValue(42),
     *     new DateObjectValue(ObjectValueType.DATE, "2024-01-01")
     * ));
     * </pre>
     *
     * @param values the values
     */
    public ReportFilterCriterion setValues(List<ObjectValue> values) {
        this.values = values;
        return this;
    }
}
