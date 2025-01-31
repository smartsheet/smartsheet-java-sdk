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

package com.smartsheet.api.internal;

import com.smartsheet.api.AuthorizationException;
import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.SearchResources;
import com.smartsheet.api.ServiceUnavailableException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.SearchResult;
import com.smartsheet.api.models.enums.SearchInclusion;
import com.smartsheet.api.models.enums.SearchLocation;
import com.smartsheet.api.models.enums.SearchScope;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the implementation of the SearchResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class SearchResourcesImpl extends AbstractResources implements SearchResources {

    /**
     * Constructor.
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public SearchResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * Performs a search across all Sheets to which user has access.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /search
     * <p>
     * Exceptions:
     * IllegalArgumentException : if query is null/empty string
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param query the query text
     * @return the search result (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public SearchResult search(String query) throws SmartsheetException {
        return search(query, null, null, null, null);
    }

    /**
     * Performs a search across all Sheets to which user has access.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /search
     *
     * @param query         the query text
     * @param includes      enum set of inclusions
     * @param location      when specified with a value of "personalWorkspace" limits response to only those
     *                      items in the user's Workspace
     * @param modifiedSince only return items modified since this date
     * @param scopes        enum set of search filters
     * @return the search result (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public SearchResult search(String query, EnumSet<SearchInclusion> includes, SearchLocation location,
                               Date modifiedSince, EnumSet<SearchScope> scopes) throws SmartsheetException {
        Util.throwIfNull(query);
        Util.throwIfEmpty(query);
        String path = "search";

        // Add the parameters to a map and build the query string at the end
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("include", QueryUtil.generateCommaSeparatedList(includes));
        parameters.put("location", location);
        if (modifiedSince != null) {
            String isoDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(modifiedSince);
            parameters.put("modifiedSince", isoDate);
        }
        parameters.put("scopes", QueryUtil.generateCommaSeparatedList(scopes));
        parameters.put("query", query);

        // Iterate through the map of parameters and generate the query string
        path += QueryUtil.generateUrl(null, parameters);
        return this.getResource(path, SearchResult.class);
    }

    /**
     * Performs a search within a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /search/sheet/{sheetId}
     * <p>
     * Exceptions:
     * IllegalArgumentException : if query is null/empty string
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the sheet id
     * @param query   the query
     * @return the search result (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public SearchResult searchSheet(long sheetId, String query) throws SmartsheetException {
        Util.throwIfNull(query);
        Util.throwIfEmpty(query);
        String path = "search/sheets/" + sheetId + "?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return this.getResource(path, SearchResult.class);
    }
}
