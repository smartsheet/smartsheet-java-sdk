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

public class PaginationParameters {
    /**
     * Represents the includeAll option
     */
    private boolean includeAll;

    /**
     * Represents the page size
     */
    private Integer pageSize;

    /**
     * Represents the page
     */
    private Integer page;

    /**
     * Represents the lastKey for token-based pagination
     */
    private String lastKey;

    /**
     * Represents the maxItems for token-based pagination
     */
    private Integer maxItems;

    /**
     * Represents the pagination type (e.g. "token")
     */
    private String paginationType;

    public PaginationParameters() {
    }

    /**
     * Constructor
     */
    public PaginationParameters(boolean includeAll, Integer pageSize, Integer page) {
        this.includeAll = includeAll;
        this.pageSize = pageSize;
        this.page = page;
    }

    /**
     * Constructor with token-based pagination parameters
     */
    public PaginationParameters(String paginationType, String lastKey, Integer maxItems) {
        this.paginationType = paginationType;
        this.lastKey = lastKey;
        this.maxItems = maxItems;
    }

    /**
     * Gets includeAll
     *
     * @return includeAll
     */
    public boolean isIncludeAll() {
        return includeAll;
    }

    /**
     * Sets includeAll
     *
     * @param includeAll include all parameter
     */
    public PaginationParameters setIncludeAll(boolean includeAll) {
        this.includeAll = includeAll;
        return this;
    }

    /**
     * Gets the page size
     *
     * @return page size
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size
     *
     * @param pageSize the page size
     */
    public PaginationParameters setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * Gets the page
     *
     * @return page the page number
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Sets the page
     *
     * @param page the page number
     */
    public PaginationParameters setPage(Integer page) {
        this.page = page;
        return this;
    }

    /**
     * Gets the pagination type
     *
     * @return pagination type
     */
    public String getPaginationType() {
        return paginationType;
    }

    /**
     * Sets the pagination type
     *
     * @param paginationType the pagination type (e.g. "token")
     */
    public PaginationParameters setPaginationType(String paginationType) {
        this.paginationType = paginationType;
        return this;
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
    public PaginationParameters setLastKey(String lastKey) {
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
    public PaginationParameters setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    /**
     * Convert to a query string
     */
    public String toQueryString() {
        Map<String, Object> parameters = toHashMap();
        return QueryUtil.generateUrl(null, parameters);
    }

    /**
     * Convert to a hash map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (paginationType != null && paginationType.equals("token")) {
            parameters.put("paginationType", paginationType);
            if (lastKey != null) {
                parameters.put("lastKey", lastKey);
            }
            if (maxItems != null) {
                parameters.put("maxItems", maxItems);
            }
            return  parameters;
        }

        if (includeAll) {
            parameters.put("includeAll", Boolean.toString(includeAll));
            return parameters;
        }

        parameters.put("pageSize", pageSize);
        parameters.put("page", page);
        return parameters;

    }

    /**
     * A convenience class for creating a PaginationParameters object
     */
    public static class PaginationParametersBuilder {
        private boolean includeAll;
        private Integer pageSize;
        private Integer page;
        private String lastKey;
        private Integer maxItems;
        private String paginationType;

        /**
         * Gets the include all flag
         *
         * @return the include all flag
         */
        public boolean isIncludeAll() {
            return includeAll;
        }

        /**
         * Sets the include All Flag
         *
         * @param includeAll the include all flag
         * @return the builder
         */
        public PaginationParametersBuilder setIncludeAll(boolean includeAll) {
            this.includeAll = includeAll;
            return this;
        }

        /**
         * Gets the page
         *
         * @return the page
         */
        public Integer getPage() {
            return page;
        }

        /**
         * Sets the page
         *
         * @param page the page
         * @return the builder
         */
        public PaginationParametersBuilder setPage(Integer page) {
            this.page = page;
            return this;
        }

        /**
         * Gets the page size
         *
         * @return the page size
         */
        public Integer getPageSize() {
            return pageSize;
        }

        /**
         * Sets the page size
         *
         * @param pageSize the page size
         * @return the builder
         */
        public PaginationParametersBuilder setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * Gets the pagination type
         *
         * @return the pagination type
         */
        public String getPaginationType() {
            return paginationType;
        }

        /**
         * Sets the pagination type
         *
         * @param paginationType the pagination type (e.g. "token")
         * @return the builder
         */
        public PaginationParametersBuilder setPaginationType(String paginationType) {
            this.paginationType = paginationType;
            return this;
        }

        /**
         * Gets the lastKey
         *
         * @return the lastKey
         */
        public String getLastKey() {
            return lastKey;
        }

        /**
         * Sets the lastKey
         *
         * @param lastKey the lastKey for token-based pagination
         * @return the builder
         */
        public PaginationParametersBuilder setLastKey(String lastKey) {
            this.lastKey = lastKey;
            return this;
        }

        /**
         * Gets the maxItems
         *
         * @return the maxItems
         */
        public Integer getMaxItems() {
            return maxItems;
        }

        /**
         * Sets the maxItems
         *
         * @param maxItems the maxItems for token-based pagination
         * @return the builder
         */
        public PaginationParametersBuilder setMaxItems(Integer maxItems) {
            this.maxItems = maxItems;
            return this;
        }

        /**
         * Builds the PaginationParameters object
         *
         * @return pagination parameter object
         */
        public PaginationParameters build() {
            PaginationParameters pagination = new PaginationParameters();
            pagination.setIncludeAll(includeAll);
            pagination.setPageSize(pageSize);
            pagination.setPage(page);
            pagination.setLastKey(lastKey);
            pagination.setMaxItems(maxItems);
            pagination.setPaginationType(paginationType);

            return pagination;
        }
    }
}
