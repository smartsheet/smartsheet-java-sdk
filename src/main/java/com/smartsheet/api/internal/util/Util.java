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

package com.smartsheet.api.internal.util;

public class Util {

    private Util() {
    }

    /**
     * faster util method that avoids creation of array for single-arg cases
     */
    public static <T> T throwIfNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        return obj;
    }

    /**
     * faster util method that avoids creation of array for two-arg cases
     */
    public static void throwIfNull(Object obj1, Object obj2) {
        if (obj1 == null) {
            throw new IllegalArgumentException();
        }
        if (obj2 == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Helper function that throws an IllegalArgumentException if one of the parameters is null.
     *
     * @param objects the paramters to
     */
    public static void throwIfNull(Object... objects) {
        for (Object obj : objects) {
            throwIfNull(obj);
        }
    }

    /**
     * faster util method that avoids creation of array for single-arg cases
     */
    public static void throwIfEmpty(String string) {
        // FIXME - so... null isn't empty?
        if (string != null && string.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Throw an exception if any of the strings are empty
     */
    public static void throwIfEmpty(String... strings) {
        for (String string : strings) {
            throwIfEmpty(string);
        }
    }
}
