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

import java.util.List;

/**
 * Represents a generic token-based paginated result that can contain any type of data.
 *
 * @param <T> the type of data contained in the paginated result
 */
public class TokenPaginatedResult<T> {

    /**
     * The list of data items of type T.
     */
    private List<T> data;

    /**
     * Token for retrieving the next page of results.
     */
    private String lastKey;

    /**
     * Gets the list of data items.
     *
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * Sets the list of data items.
     *
     * @param data the data
     * @return this TokenPaginatedResult instance for method chaining
     */
    public TokenPaginatedResult<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    /**
     * Gets the token for retrieving the next page of results.
     *
     * @return the last key
     */
    public String getLastKey() {
        return lastKey;
    }

    /**
     * Sets the token for retrieving the next page of results.
     *
     * @param lastKey the last key
     * @return this TokenPaginatedResult instance for method chaining
     */
    public TokenPaginatedResult<T> setLastKey(String lastKey) {
        this.lastKey = lastKey;
        return this;
    }

    /**
     * Checks if there are more pages available.
     *
     * @return true if there are more pages (lastKey is not null and not empty), false otherwise
     */
    public boolean hasMorePages() {
        return lastKey != null && !lastKey.trim().isEmpty();
    }
}
