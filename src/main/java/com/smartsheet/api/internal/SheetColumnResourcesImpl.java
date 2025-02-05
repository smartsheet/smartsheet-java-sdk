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
import com.smartsheet.api.ServiceUnavailableException;
import com.smartsheet.api.SheetColumnResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.ColumnInclusion;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation of the SheetColumnResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class SheetColumnResourcesImpl extends AbstractResources implements SheetColumnResources {

    private static final String SHEETS_PATH = "sheets/";
    private static final String COLUMNS = "columns";

    /**
     * Constructor.
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public SheetColumnResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * List columns of a given sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/columns
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId    the sheet id
     * @param includes   list of includes
     * @param pagination the object containing the pagination parameters
     * @return the columns (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<Column> listColumns(
            long sheetId,
            EnumSet<ColumnInclusion> includes,
            PaginationParameters pagination
    ) throws SmartsheetException {
        return this.listColumns(sheetId, includes, pagination, null);
    }

    /**
     * List columns of a given sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/columns
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId    the sheet id
     * @param includes   list of includes
     * @param pagination the object containing the pagination parameters
     * @param level      compatibility level
     * @return the columns (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<Column> listColumns(
            long sheetId,
            EnumSet<ColumnInclusion> includes,
            PaginationParameters pagination,
            Integer level
    ) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/" + COLUMNS;

        Map<String, Object> parameters = new HashMap<>();
        if (pagination != null) {
            parameters = pagination.toHashMap();
        }

        parameters.put("include", QueryUtil.generateCommaSeparatedList(includes));
        parameters.put("level", level);

        path += QueryUtil.generateUrl(null, parameters);
        return this.listResourcesWithWrapper(path, Column.class);
    }

    /**
     * Add column to a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/columns
     * <p>
     * Exceptions:
     * IllegalArgumentException : if any argument is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the sheet id
     * @param columns the list of columns object limited to the following attributes: *
     *                title * type * symbol (optional) * options (optional) - array of options * index (zero-based) * systemColumnType
     *                (optional) * autoNumberFormat (optional)
     * @return the list of created columns
     * @throws SmartsheetException the smartsheet exception
     */
    public List<Column> addColumns(long sheetId, List<Column> columns) throws SmartsheetException {
        return this.postAndReceiveList(SHEETS_PATH + sheetId + "/" + COLUMNS, columns, Column.class);
    }

    /**
     * Delete column.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /sheets/{sheetId}/columns/{columnId}
     *
     * @param sheetId  the sheet id
     * @param columnId the column id
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public void deleteColumn(long sheetId, long columnId) throws SmartsheetException {
        this.deleteResource(SHEETS_PATH + sheetId + "/" + COLUMNS + "/" + columnId, Column.class);
    }

    /**
     * Update a column.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /sheets/{sheetId}/columns/{columnId}
     * <p>
     * Exceptions:
     * IllegalArgumentException : if any argument is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the sheetId
     * @param column  the column to update limited to the following attributes: index (column's new index in the sheet),
     *                title, sheetId, type, options (optional), symbol (optional), systemColumnType (optional),
     *                autoNumberFormat (optional)
     * @return the updated sheet (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public Column updateColumn(long sheetId, Column column) throws SmartsheetException {
        Util.throwIfNull(column);
        return this.updateResource(SHEETS_PATH + sheetId + "/" + COLUMNS + "/" + column.getId(), Column.class, column);
    }

    /**
     * Gets the Column specified in the URL.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/columns/{columnId}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId  the sheet id
     * @param columnId the column id
     * @param includes list of includes
     * @return the column (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public Column getColumn(long sheetId, long columnId, EnumSet<ColumnInclusion> includes) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/" + COLUMNS + "/" + columnId;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("include", QueryUtil.generateCommaSeparatedList(includes));

        path = QueryUtil.generateUrl(path, parameters);
        return this.getResource(path, Column.class);
    }
}
