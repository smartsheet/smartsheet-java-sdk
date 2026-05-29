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

import com.smartsheet.api.internal.util.QueryUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents token-based pagination parameters for API requests
 */
public class TokenPaginationParameters {

    /**
     * Represents the lastKey for token-based pagination
     */
    private String lastKey;

    /**
     * Represents the maxItems for token-based pagination
     */
    private Integer maxItems;

    public TokenPaginationParameters() {
    }

    public TokenPaginationParameters(String lastKey, Integer maxItems) {
        this.lastKey = lastKey;
        this.maxItems = maxItems;
    }

    /**
     * Gets the lastKey
     *
     * @return lastKey for token-based pagination
     */
    public String getLastKey() {
        return lastKey;
    }

    /**
     * Sets the lastKey
     *
     * @param lastKey the lastKey for token-based pagination
     */
    public TokenPaginationParameters setLastKey(String lastKey) {
        this.lastKey = lastKey;
        return this;
    }

    /**
     * Gets the maxItems
     *
     * @return maxItems for token-based pagination
     */
    public Integer getMaxItems() {
        return maxItems;
    }

    /**
     * Sets the maxItems
     *
     * @param maxItems the maxItems for token-based pagination
     */
    public TokenPaginationParameters setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    /**
     * Convert to a query string
     */
    public String toQueryString() {
        return QueryUtil.generateUrl(null, toHashMap());
    }

    /**
     * Convert to a hash map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> parameters = new HashMap<>();
        if (lastKey != null) {
            parameters.put("lastKey", lastKey);
        }
        if (maxItems != null) {
            parameters.put("maxItems", maxItems);
        }
        parameters.put("paginationType", "token");
        return parameters;
    }
}
