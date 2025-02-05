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

package com.smartsheet.api.internal.util;

import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryUtil {

    private QueryUtil() {
    }

    /**
     * Returns a comma seperated list of items as a string
     *
     * @param list the collection
     * @param <T>  the type
     * @return comma separated string
     */
    public static <T> String generateCommaSeparatedList(@Nullable Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    /**
     * Generate a URL
     */
    public static String generateUrl(@Nullable String baseUrl, @Nullable Map<String, ?> parameters) {
        if (baseUrl == null) {
            baseUrl = "";
        }
        return baseUrl + generateQueryString(parameters);
    }

    /**
     * Returns a query string.
     *
     * @param parameters the map of query string keys and values
     * @return the query string
     */
    private static String generateQueryString(@Nullable Map<String, ?> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, ?> entry : parameters.entrySet()) {
            // Check to see if the key/value isn't null or empty string
            if (entry.getKey() != null && entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                String encodedQueryParam = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
                String encodedQueryValue = URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8);
                result
                        .append('&')
                        .append(encodedQueryParam)
                        .append("=")
                        .append(encodedQueryValue);
            }
        }

        return result.length() == 0 ? "" : "?" + result.substring(1);
    }
}
