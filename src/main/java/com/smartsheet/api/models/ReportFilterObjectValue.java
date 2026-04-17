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

import com.smartsheet.api.models.enums.ObjectValueType;

import java.util.Date;

/**
 * Marker interface for filter values used in {@link ReportFilterCriterion}.
 * <p>
 * This interface extends {@link ObjectValue} and provides type safety for report filter values.
 * Implementations include:
 * <ul>
 * <li>{@link StringObjectValue} - for string values</li>
 * <li>{@link NumberObjectValue} - for numeric values</li>
 * <li>{@link DateObjectValue} - for date values</li>
 * <li>{@link CurrentUserObjectValue} - for current user filters</li>
 * </ul>
 * <p>
 * Static factory methods provide convenient ways to create filter values:
 * <pre>
 * criterion.setValues(Arrays.asList(
 *     ReportFilterObjectValue.string("value1"),
 *     ReportFilterObjectValue.number(42),
 *     ReportFilterObjectValue.date("2024-01-01"),
 *     ReportFilterObjectValue.currentUser()
 * ));
 * </pre>
 */
public interface ReportFilterObjectValue extends ObjectValue {

    /**
     * Creates a string filter value.
     *
     * @param value the string value
     * @return a StringObjectValue implementing ReportFilterObjectValue
     */
    static ReportFilterObjectValue string(String value) {
        return new StringObjectValue(value);
    }

    /**
     * Creates a numeric filter value.
     *
     * @param value the numeric value
     * @return a NumberObjectValue implementing ReportFilterObjectValue
     */
    static ReportFilterObjectValue number(Number value) {
        return new NumberObjectValue(value);
    }

    /**
     * Creates a date filter value with DATE objectType.
     * <p>
     * The value should be in ISO 8601 format: "yyyy-MM-dd"
     *
     * @param value the date string in "yyyy-MM-dd" format
     * @return a DateObjectValue with DATE objectType implementing ReportFilterObjectValue
     */
    static ReportFilterObjectValue date(String value) {
        return new DateObjectValue(ObjectValueType.DATE, value);
    }

    /**
     * Creates a date filter value from a Java Date object.
     *
     * @param date the Date object
     * @return a DateObjectValue with DATE objectType implementing ReportFilterObjectValue
     */
    static ReportFilterObjectValue date(Date date) {
        return DateObjectValue.fromDate(ObjectValueType.DATE, date);
    }

    /**
     * Creates a CURRENT_USER filter value.
     * <p>
     * This represents a special filter value that matches the current authenticated user.
     * The value is typically empty or set to a placeholder as the server interprets this
     * based on the objectType.
     *
     * @return a CurrentUserObjectValue implementing ReportFilterObjectValue
     */
    static ReportFilterObjectValue currentUser() {
        return new CurrentUserObjectValue();
    }
}
