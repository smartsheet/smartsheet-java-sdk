package com.smartsheet.api.models;

import java.util.List;

public class ListAssetSharesResponse<T> {
    /**
     * The list of items of type T.
     */
    private List<T> items;

    /**
     * Token for retrieving the next page of results.
     */
    private String lastKey;

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
     * @return this ListAssetSharesResponse instance for method chaining
     */
    public ListAssetSharesResponse<T> setLastKey(String lastKey) {
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

    /**
     * Gets the list of items.
     *
     * @return the items
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Sets the list of items.
     *
     * @param items the items
     * @return this ListAssetSharesResponse instance for method chaining
     */
    public ListAssetSharesResponse<T> setItems(List<T> items) {
        this.items = items;
        return this;
    }
}
