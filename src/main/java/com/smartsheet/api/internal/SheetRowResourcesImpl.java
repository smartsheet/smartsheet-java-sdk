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
import com.smartsheet.api.RowAttachmentResources;
import com.smartsheet.api.RowColumnResources;
import com.smartsheet.api.RowDiscussionResources;
import com.smartsheet.api.ServiceUnavailableException;
import com.smartsheet.api.SheetRowResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.SmartsheetRestException;
import com.smartsheet.api.internal.http.HttpEntity;
import com.smartsheet.api.internal.http.HttpMethod;
import com.smartsheet.api.internal.http.HttpRequest;
import com.smartsheet.api.internal.http.HttpResponse;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.BulkItemFailure;
import com.smartsheet.api.models.BulkItemResult;
import com.smartsheet.api.models.BulkRowFailedItem;
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.CopyOrMoveRowDirective;
import com.smartsheet.api.models.CopyOrMoveRowResult;
import com.smartsheet.api.models.MultiRowEmail;
import com.smartsheet.api.models.PartialRowUpdateResult;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.RowEmail;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.ObjectExclusion;
import com.smartsheet.api.models.enums.RowCopyInclusion;
import com.smartsheet.api.models.enums.RowInclusion;
import com.smartsheet.api.models.enums.RowMoveInclusion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the implementation of the SheetRowResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class SheetRowResourcesImpl extends AbstractResources implements SheetRowResources {
    RowAttachmentResources attachments;
    RowDiscussionResources discussions;
    RowColumnResources columns;

    private static final String IGNORE_ROWS_NOT_FOUND = "ignoreRowsNotFound";
    private static final String SHEETS_PATH = "sheets/";
    private static final String ROWS = "rows";
    private static final String INCLUDE = "include";
    private static final String EXCLUDE = "exclude";

    /**
     * Constructor.
     *
     * @param smartsheet the SmartsheetImpl
     * @throws IllegalArgumentException : if any argument is null
     */
    public SheetRowResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
        this.attachments = new RowAttachmentResourcesImpl(smartsheet);
        this.discussions = new RowDiscussionResourcesImpl(smartsheet);
        this.columns = new RowColumnResourcesImpl(smartsheet);
    }

    /**
     * Insert rows to a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/rows
     *
     * @param sheetId the sheet id
     * @param rows    the list of rows to create
     * @return the created rows
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public List<Row> addRows(long sheetId, List<Row> rows) throws SmartsheetException {
        return this.addRows(sheetId, rows, null, null);
    }

    /**
     * Insert rows to a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/rows
     *
     * @param sheetId  the sheet id
     * @param rows     the list of rows to create
     * @param includes optional objects to include
     * @param excludes optional objects to exclude
     * @return the created rows
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public List<Row> addRows(
            long sheetId,
            List<Row> rows,
            EnumSet<RowInclusion> includes,
            EnumSet<ObjectExclusion> excludes
    ) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/" + ROWS;

        Map<String, Object> parameters = new HashMap<>();

        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put(EXCLUDE, QueryUtil.generateCommaSeparatedList(excludes));

        path += QueryUtil.generateUrl(null, parameters);
        return this.postAndReceiveList(path, rows, Row.class);
    }

    /**
     * Insert rows to a sheet, allowing partial success. If a row cannot be inserted, it will fail, while the others may succeed.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{id}/rows
     *
     * @param sheetId the sheet id
     * @param rows    the list of rows to create
     * @return the list of created rows
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    @Override
    public PartialRowUpdateResult addRowsAllowPartialSuccess(long sheetId, List<Row> rows) throws SmartsheetException {
        return doPartialRowOperation(sheetId, rows, null, null, HttpMethod.POST);
    }

    /**
     * Insert rows to a sheet, allowing partial success. If a row cannot be inserted, it will fail, while the others may succeed.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{id}/rows
     *
     * @param sheetId  the sheet id
     * @param rows     the list of rows to create
     * @param includes optional objects to include
     * @param excludes optional objects to exclude
     * @return the list of created rows
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    @Override
    public PartialRowUpdateResult addRowsAllowPartialSuccess(
            long sheetId,
            List<Row> rows,
            EnumSet<RowInclusion> includes,
            EnumSet<ObjectExclusion> excludes
    ) throws SmartsheetException {
        return doPartialRowOperation(sheetId, rows, includes, excludes, HttpMethod.POST);
    }

    /**
     * Get a row.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/rows/{rowId}
     *
     * @param sheetId  the id of the sheet
     * @param rowId    the id of the row
     * @param includes optional objects to include
     * @param excludes optional objects to exclude
     * @return the row (note that if there is no such resource, this method will throw ResourceNotFoundException rather
     * than returning null).
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Row getRow(
            long sheetId,
            long rowId,
            EnumSet<RowInclusion> includes,
            EnumSet<ObjectExclusion> excludes
    ) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/" + ROWS + "/" + rowId;

        Map<String, Object> parameters = new HashMap<>();

        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put(EXCLUDE, QueryUtil.generateCommaSeparatedList(excludes));

        path += QueryUtil.generateUrl(null, parameters);
        return this.getResource(path, Row.class);
    }

    /**
     * Delete a row.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /sheets/{sheetId}/rows/{rowId}
     *
     * @param sheetId the sheet id
     * @param rowId   the ID of the row
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     * @deprecated as of API 2.0.2 release, replaced by {@link #deleteRows(long, Set, boolean)}
     */
    @Deprecated(since = "2.0.2", forRemoval = true)
    public void deleteRow(long sheetId, long rowId) throws SmartsheetException {
        this.deleteResource(SHEETS_PATH + sheetId + "/" + ROWS + "/" + rowId, Row.class);
    }

    /**
     * Deletes one or more row(s) from the Sheet specified in the URL.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /sheets/{sheetId}/rows/{rowId}
     *
     * @param sheetId            the sheet id
     * @param rowIds             the row ids
     * @param ignoreRowsNotFound boolean for ignoring row ids not found
     * @return a list of deleted rows
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public List<Long> deleteRows(long sheetId, Set<Long> rowIds, boolean ignoreRowsNotFound) throws SmartsheetException {
        Util.throwIfNull(rowIds);
        Map<String, Object> parameters = new HashMap<>();
        String path = SHEETS_PATH + sheetId + "/" + ROWS + "/";
        parameters.put("ids", QueryUtil.generateCommaSeparatedList(rowIds));
        parameters.put(IGNORE_ROWS_NOT_FOUND, ignoreRowsNotFound);

        path += QueryUtil.generateUrl(null, parameters);

        return this.deleteListResources(path, Long.class);
    }

    /**
     * Send a row via email to the designated recipients.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/{rowId}/emails
     *
     * @param sheetId the id of the sheet
     * @param rowId   the id of the row
     * @param email   the row email
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     * @deprecated as of API V2.0.2, replaced by {@link #sendRows(long, MultiRowEmail)}
     */
    @Deprecated(since = "2.0.2", forRemoval = true)
    public void sendRow(long sheetId, long rowId, RowEmail email) throws SmartsheetException {
        this.createResource(SHEETS_PATH + sheetId + "/" + ROWS + "/" + rowId + "/emails", RowEmail.class, email);
    }

    /**
     * Send a row via email to the designated recipients.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/emails
     *
     * @param sheetId the id of the sheet
     * @param email   the multi row email
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public void sendRows(long sheetId, MultiRowEmail email) throws SmartsheetException {
        this.createResource(SHEETS_PATH + sheetId + "/rows/emails", MultiRowEmail.class, email);
    }

    /**
     * Update rows.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /sheets/{sheetId}/rows
     *
     * @param sheetId the id of the sheet
     * @param rows    the list of rows
     * @return a list of rows
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public List<Row> updateRows(long sheetId, List<Row> rows) throws SmartsheetException {
        return this.updateRows(sheetId, rows, null, null);
    }

    /**
     * Update rows.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /sheets/{sheetId}/rows
     *
     * @param sheetId  the id of the sheet
     * @param rows     the list of rows
     * @param includes optional objects to include
     * @param excludes optional objects to exclude
     * @return a list of rows
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public List<Row> updateRows(
            long sheetId,
            List<Row> rows,
            EnumSet<RowInclusion> includes,
            EnumSet<ObjectExclusion> excludes
    ) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/" + ROWS;

        Map<String, Object> parameters = new HashMap<>();

        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put(EXCLUDE, QueryUtil.generateCommaSeparatedList(excludes));

        path += QueryUtil.generateUrl(null, parameters);
        return this.putAndReceiveList(path, rows, Row.class);
    }

    /**
     * <p>Helper method: Update a single cell</p>
     *
     * @param sheetId the sheet ID the cell should be written to
     * @param cell    the cell object to be written. Must include a rowId and columnId
     * @return The returned Row object from the api
     * @throws SmartsheetException the smartsheet exception
     */
    public Row updateCell(long sheetId, Cell cell) throws SmartsheetException {
        if (cell.getRowId() == null || cell.getColumnId() == null) {
            throw new SmartsheetException("Cell must include rowId and columnId");
        }

        Row updateRow = new Row();
        updateRow.setCells(List.of(cell));
        updateRow.setId(cell.getRowId());

        List<Row> rows = updateRows(sheetId, List.of(updateRow));
        return rows.isEmpty() ? null : rows.get(0);
    }

    /**
     * <p>Helper method: Update a single with a string value</p>
     * <p>NOTE: This method internally fetches the sheet. To avoid this step, fetch the sheet in
     * advance and use the method by the same name</p>
     *
     * @param sheetId  the sheet ID the cell should be written to
     * @param rowIdx   the row index of the cell (base 1 indexed)
     * @param colIdx   the column index of the cell (base 1 indexed)
     * @param newValue the new value of the cell
     * @return The returned Row object from the api
     * @throws SmartsheetException the smartsheet exception
     */
    public Row updateCell(long sheetId, int rowIdx, int colIdx, String newValue) throws SmartsheetException {
        Sheet sheet = smartsheet.sheetResources().getSheet(sheetId);
        return updateCell(sheet, colIdx, rowIdx, newValue);
    }

    /**
     * <p>Helper method: Update a single with a string value</p>
     *
     * @param sheet    The sheet to update the cell in. Must include rowId and cell information
     * @param rowIdx   The row index of the cell (base 1 indexed)
     * @param colIdx   The column index of the cell (base 1 indexed)
     * @param newValue The new value of the cell
     * @return The returned Row object from the api
     * @throws SmartsheetException the smartsheet exception
     */
    public Row updateCell(Sheet sheet, int rowIdx, int colIdx, String newValue) throws SmartsheetException {
        Row row = sheet.getRowByRowNumber(rowIdx);
        if (row == null) {
            throw new SmartsheetException("Sheet does not contain row at index" + rowIdx);
        }
        long rowId = row.getId();
        if (row.getCells().size() < colIdx) {
            throw new SmartsheetException("Sheet does not contain column at index" + colIdx);
        }
        long colId = row.getCells().get(colIdx - 1).getColumnId();

        Cell cell = new Cell();
        cell.setColumnId(colId);
        cell.setRowId(rowId);
        cell.setValue(newValue);
        return updateCell(sheet.getId(), cell);
    }

    /**
     * Update rows, but allow partial success. The PartialRowUpdateResult will contain the successful
     * rows and those that failed, with specific messages for each.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /sheets/{sheetId}/rows
     *
     * @param sheetId the id of the sheet
     * @param rows    the list of rows
     * @return a list of rows
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    @Override
    public PartialRowUpdateResult updateRowsAllowPartialSuccess(long sheetId, List<Row> rows) throws SmartsheetException {
        return doPartialRowOperation(sheetId, rows, null, null, HttpMethod.PUT);
    }

    /**
     * Update rows, but allow partial success. The PartialRowUpdateResult will contain the successful
     * rows and those that failed, with specific messages for each.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /sheets/{sheetId}/rows
     *
     * @param sheetId  the id of the sheet
     * @param rows     the list of rows
     * @param includes optional objects to include
     * @param excludes optional objects to exclude
     * @return a list of rows
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    @Override
    public PartialRowUpdateResult updateRowsAllowPartialSuccess(
            long sheetId,
            List<Row> rows,
            EnumSet<RowInclusion> includes,
            EnumSet<ObjectExclusion> excludes
    ) throws SmartsheetException {
        return doPartialRowOperation(sheetId, rows, includes, excludes, HttpMethod.PUT);
    }

    private PartialRowUpdateResult doPartialRowOperation(
            long sheetId,
            List<Row> rows,
            EnumSet<RowInclusion> includes,
            EnumSet<ObjectExclusion> excludes,
            HttpMethod method
    ) throws SmartsheetException {
        Util.throwIfNull(rows, method);
        if (method != HttpMethod.POST && method != HttpMethod.PUT) {
            throw new IllegalArgumentException();
        }

        String path = SHEETS_PATH + sheetId + "/" + ROWS;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("allowPartialSuccess", "true");
        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put(EXCLUDE, QueryUtil.generateCommaSeparatedList(excludes));

        path = QueryUtil.generateUrl(path, parameters);

        HttpRequest request;
        request = createHttpRequest(smartsheet.getBaseURI().resolve(path), method);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        this.smartsheet.getJsonSerializer().serialize(rows, baos);

        HttpEntity entity = new HttpEntity();
        entity.setContentType("application/json");
        entity.setContent(new ByteArrayInputStream(baos.toByteArray()));
        entity.setContentLength(baos.size());
        request.setEntity(entity);

        HttpResponse response = this.smartsheet.getHttpClient().request(request);

        PartialRowUpdateResult result = null;
        switch (response.getStatusCode()) {
            case 200:
                BulkItemResult<Row> bulkItemResult;
                bulkItemResult = this.smartsheet.getJsonSerializer().deserializeBulkItemResult(Row.class,
                        response.getEntity().getContent());
                result = new PartialRowUpdateResult();
                result.setResult(bulkItemResult.getResult());
                result.setResultCode(bulkItemResult.getResultCode());
                result.setMessage(bulkItemResult.getMessage());
                result.setVersion(bulkItemResult.getVersion());
                if (bulkItemResult.getFailedItems() != null) {
                    List<BulkRowFailedItem> failedItems = new ArrayList<>();
                    for (BulkItemFailure bulkItemFailure : bulkItemResult.getFailedItems()) {
                        BulkRowFailedItem bulkRowFailedItem = new BulkRowFailedItem();
                        bulkRowFailedItem.setError(bulkItemFailure.getError());
                        bulkRowFailedItem.setIndex(bulkItemFailure.getIndex());
                        bulkRowFailedItem.setRowId(bulkItemFailure.getRowId());
                        failedItems.add(bulkRowFailedItem);
                    }
                    result.setFailedItems(failedItems);
                }
                break;
            default:
                handleError(response);
        }

        smartsheet.getHttpClient().releaseConnection();

        return result;
    }

    /**
     * Moves Row(s) from the Sheet specified in the URL to (the bottom of) another sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/move
     *
     * @param sheetId            the sheet ID to move
     * @param includes           the parameters to include
     * @param ignoreRowsNotFound optional,specifying row Ids that do not exist within the source sheet
     * @param moveParameters     CopyOrMoveRowDirective object
     * @return the result object
     * @throws IllegalArgumentException    : if any argument is null, or path is empty string
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public CopyOrMoveRowResult moveRows(
            Long sheetId,
            EnumSet<RowMoveInclusion> includes,
            Boolean ignoreRowsNotFound,
            CopyOrMoveRowDirective moveParameters
    ) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/rows/move";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));

        if (ignoreRowsNotFound != null) {
            parameters.put(IGNORE_ROWS_NOT_FOUND, ignoreRowsNotFound.toString());
        }

        path += QueryUtil.generateUrl(null, parameters);
        return this.postAndReceiveRowObject(path, moveParameters);
    }

    /**
     * Copies Row(s) from the Sheet specified in the URL to (the bottom of) another sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/copy
     *
     * @param sheetId            the sheet ID to move
     * @param includes           the parameters to include
     * @param ignoreRowsNotFound optional,specifying row Ids that do not exist within the source sheet
     * @param copyParameters     CopyOrMoveRowDirective object
     * @return the result object
     * @throws IllegalArgumentException    : if any argument is null, or path is empty string
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public CopyOrMoveRowResult copyRows(
            Long sheetId,
            EnumSet<RowCopyInclusion> includes,
            Boolean ignoreRowsNotFound,
            CopyOrMoveRowDirective copyParameters
    ) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/rows/copy";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));

        if (ignoreRowsNotFound != null) {
            parameters.put(IGNORE_ROWS_NOT_FOUND, ignoreRowsNotFound.toString());
        }

        path += QueryUtil.generateUrl(null, parameters);
        return this.postAndReceiveRowObject(path, copyParameters);
    }

    /**
     * /**
     * Update the values of the Cells in a Row.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /row/{id}/cells
     *
     * @param rowId the row id
     * @param cells the cells to update (Cells must have the following attributes set: *
     *              columnId * value * strict (optional)
     * @return the updated cells (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     * @deprecated replaced by {@link #updateRows(long, List)}
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public List<Cell> updateCells(long rowId, List<Cell> cells) throws SmartsheetException {
        return this.putAndReceiveList("row/" + rowId + "/cells", cells, Cell.class);
    }

    /**
     * Creates an object of RowAttachmentResources.
     *
     * @return the created RowAttachmentResources object
     */
    public RowAttachmentResources attachmentResources() {
        return attachments;
    }

    /**
     * Creates an object of RowDiscussionResources.
     *
     * @return the created RowDiscussionResources object
     */
    public RowDiscussionResources discussionResources() {
        return discussions;
    }

    /**
     * Creates an object of RowColumnResources.
     *
     * @return the created RowColumnResources object
     */
    public RowColumnResources cellResources() {
        return columns;
    }
}
