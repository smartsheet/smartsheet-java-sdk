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

import com.smartsheet.api.RowColumnResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.HttpEntity;
import com.smartsheet.api.internal.http.HttpMethod;
import com.smartsheet.api.internal.http.HttpRequest;
import com.smartsheet.api.internal.http.HttpResponse;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.CellHistory;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.CellHistoryInclusion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the implementation of the RowColumnResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class RowColumnResourcesImpl extends AbstractResources implements RowColumnResources {
    private static final String SHEETS_PATH = "sheets/";
    private static final String ROWS_PATH = "/rows/";
    private static final String COLUMNS_PATH = "/columns/";
    private static final String CELL_IMAGES_PATH = "/cellimages";

    /**
     * Constructor.
     * <p>
     * Parameters: - smartsheet : the SmartsheetImpl
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public RowColumnResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * Get the cell modification history.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/rows/{rowId}/columns/{columnId}/history
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param rowId      the row id
     * @param columnId   the column id
     * @param sheetId    the sheet ID
     * @param parameters the pagination parameters
     * @return the modification history (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<CellHistory> getCellHistory(
            long sheetId,
            long rowId,
            long columnId,
            PaginationParameters parameters
    ) throws SmartsheetException {
        return this.getCellHistory(sheetId, rowId, columnId, parameters, null, null);
    }

    /**
     * Get the cell modification history.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/rows/{rowId}/columns/{columnId}/history
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param rowId      the row id
     * @param columnId   the column id
     * @param sheetId    the sheet ID
     * @param pagination the pagination parameters
     * @param includes   cell history inclusion
     * @param level      compatibility level
     * @return the modification history (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<CellHistory> getCellHistory(long sheetId, long rowId, long columnId, PaginationParameters pagination,
                                                   EnumSet<CellHistoryInclusion> includes, Integer level) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + ROWS_PATH + rowId + COLUMNS_PATH + columnId + "/history";

        Map<String, Object> parameters = new HashMap<>();
        if (pagination != null) {
            parameters = pagination.toHashMap();
        }
        if (level != null) {
            parameters.put("level", level);
        }
        parameters.put("include", QueryUtil.generateCommaSeparatedList(includes));

        path += QueryUtil.generateUrl(null, parameters);

        return this.listResourcesWithWrapper(path, CellHistory.class);
    }

    /**
     * Add an image to a cell.
     * <p>
     * It mirrors the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/{rowId}/columns/{columnId}/cellimages
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId     the sheet ID
     * @param rowId       the row id
     * @param columnId    the column id
     * @param file        the file path
     * @param contentType MIME type of the image
     * @throws SmartsheetException   the smartsheet exception
     * @throws FileNotFoundException image file not found
     */
    public void addImageToCell(
            long sheetId,
            long rowId,
            long columnId,
            String file,
            String contentType
    ) throws FileNotFoundException, SmartsheetException {
        Util.throwIfNull(file);
        File f = new File(file);
        String path = SHEETS_PATH + sheetId + ROWS_PATH + rowId + COLUMNS_PATH + columnId + CELL_IMAGES_PATH;
        addImage(path, new FileInputStream(f), contentType, f.length(), false, null, file);
    }

    /**
     * Add an image to a cell.
     * <p>
     * It mirrors the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/{rowId}/columns/{columnId}/cellimages
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId            the sheet ID
     * @param rowId              the row id
     * @param columnId           the column id
     * @param file               the file path
     * @param contentType        MIME type of the image
     * @param overrideValidation override column type validation if true
     * @param altText            alternate description for the image
     * @throws SmartsheetException   the smartsheet exception
     * @throws FileNotFoundException image file not found
     */
    public void addImageToCell(
            long sheetId,
            long rowId,
            long columnId,
            String file,
            String contentType,
            boolean overrideValidation,
            String altText
    ) throws FileNotFoundException, SmartsheetException {
        Util.throwIfNull(file);
        File f = new File(file);
        String path = SHEETS_PATH + sheetId + ROWS_PATH + rowId + COLUMNS_PATH + columnId + CELL_IMAGES_PATH;
        addImage(path, new FileInputStream(f), contentType, f.length(), overrideValidation, altText, file);
    }

    /**
     * Add an image to a cell.
     * <p>
     * It mirrors the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/{rowId}/columns/{columnId}/cellimages
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId            the sheet ID
     * @param rowId              the row id
     * @param columnId           the column id
     * @param file               the File object
     * @param contentType        MIME type
     * @param overrideValidation override column type validation if true
     * @param altText            alternate description for the image
     * @throws SmartsheetException   the smartsheet exception
     * @throws FileNotFoundException image file not found
     */
    public void addImageToCell(long sheetId, long rowId, long columnId, File file, String contentType,
                               boolean overrideValidation, String altText) throws FileNotFoundException, SmartsheetException {
        Util.throwIfNull(file);
        String path = SHEETS_PATH + sheetId + ROWS_PATH + rowId + COLUMNS_PATH + columnId + CELL_IMAGES_PATH;
        addImage(path, new FileInputStream(file), contentType, file.length(), overrideValidation, altText, file.getName());
    }

    /**
     * Add an image to a cell.
     * <p>
     * It mirrors the following Smartsheet REST API method: POST /sheets/{sheetId}/rows/{rowId}/columns/{columnId}/cellimages
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId            the sheet ID
     * @param rowId              the row id
     * @param columnId           the column id
     * @param inputStream        the input stream of the contents
     * @param contentType        MIME type
     * @param contentLength      length of the input stream
     * @param overrideValidation override column type validation if true
     * @param altText            alternate description for the image
     * @throws SmartsheetException the smartsheet exception
     */
    public void addImageToCell(long sheetId, long rowId, long columnId, InputStream inputStream, String contentType,
                               long contentLength, boolean overrideValidation, String altText) throws SmartsheetException {
        Util.throwIfNull(inputStream);
        String path = SHEETS_PATH + sheetId + ROWS_PATH + rowId + COLUMNS_PATH + columnId + CELL_IMAGES_PATH;
        addImage(path, inputStream, contentType, contentLength, overrideValidation, altText, altText);
    }

    private void addImage(String path, InputStream inputStream, String contentType, long contentLength,
                          boolean overrideValidation, String altText, String imageName) throws SmartsheetException {
        if (imageName == null) {
            inputStream.toString();
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        Map<String, Object> parameters = new HashMap<>();
        if (altText != null) {
            parameters.put("altText", altText);
        }
        if (overrideValidation) {
            parameters.put("overrideValidation", "true");
        }
        path += QueryUtil.generateUrl(null, parameters);

        HttpRequest request = createHttpRequest(this.smartsheet.getBaseURI().resolve(path), HttpMethod.POST);
        String contentDispositionValue = "attachment; filename=\"" + URLEncoder.encode(imageName, StandardCharsets.UTF_8) + "\"";
        request.getHeaders().put("Content-Disposition", contentDispositionValue);

        HttpEntity entity = new HttpEntity();
        entity.setContentType(contentType);
        entity.setContent(inputStream);
        entity.setContentLength(contentLength);
        request.setEntity(entity);

        HttpResponse response = this.smartsheet.getHttpClient().request(request);
        switch (response.getStatusCode()) {
            case 200:
                break;
            default:
                handleError(response);
        }
        smartsheet.getHttpClient().releaseConnection();
    }
}
