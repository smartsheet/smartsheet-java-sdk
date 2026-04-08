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
 * Helper class for creating filter values for {@link ReportFilterCriterion}.
 * <p>
 * Provides convenient factory methods for creating properly typed filter values.
 * <p>
 * Example usage:
 * <pre>
 * criterion.setValues(Arrays.asList(
 *     ReportFilterValue.string("value1"),
 *     ReportFilterValue.number(42),
 *     ReportFilterValue.date("2024-01-01"),
 *     ReportFilterValue.currentUser()
 * ));
 * </pre>
 */
public class ReportFilterValue {

    private ReportFilterValue() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates a string filter value.
     *
     * @param value the string value
     * @return a StringObjectValue
     */
    public static ObjectValue string(String value) {
        return new StringObjectValue(value);
    }

    /**
     * Creates a numeric filter value.
     *
     * @param value the numeric value
     * @return a NumberObjectValue
     */
    public static ObjectValue number(Number value) {
        return new NumberObjectValue(value);
    }

    /**
     * Creates a date filter value with DATE objectType.
     * <p>
     * The value should be in ISO 8601 format: "yyyy-MM-dd"
     *
     * @param value the date string in "yyyy-MM-dd" format
     * @return a DateObjectValue with DATE objectType
     */
    public static ObjectValue date(String value) {
        return new DateObjectValue(ObjectValueType.DATE, value);
    }

    /**
     * Creates a date filter value from a Java Date object.
     *
     * @param date the Date object
     * @return a DateObjectValue with DATE objectType
     */
    public static ObjectValue date(Date date) {
        return DateObjectValue.fromDate(ObjectValueType.DATE, date);
    }

    /**
     * Creates a CURRENT_USER filter value.
     * <p>
     * This represents a special filter value that matches the current authenticated user.
     * The value is typically empty or set to a placeholder as the server interprets this
     * based on the objectType.
     *
     * @return a CurrentUserObjectValue
     */
    public static ObjectValue currentUser() {
        return new CurrentUserObjectValue();
    }

    /**
     * Special ObjectValue implementation for CURRENT_USER filter type.
     * <p>
     * This is used in report filters to match against the current authenticated user.
     */
    public static class CurrentUserObjectValue implements ObjectValue {
        private final String objectType = "CURRENT_USER";
        private final String value = "";

        @Override
        public ObjectValueType getObjectType() {
            // Return null since CURRENT_USER is not in the standard ObjectValueType enum
            // The custom serialization will use the objectType field directly
            return null;
        }

        public String getObjectTypeString() {
            return objectType;
        }

        public String getValue() {
            return value;
        }
    }
}
