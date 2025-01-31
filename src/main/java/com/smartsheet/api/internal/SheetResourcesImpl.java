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
import com.smartsheet.api.ShareResources;
import com.smartsheet.api.SheetAttachmentResources;
import com.smartsheet.api.SheetAutomationRuleResources;
import com.smartsheet.api.SheetColumnResources;
import com.smartsheet.api.SheetCommentResources;
import com.smartsheet.api.SheetCrossSheetReferenceResources;
import com.smartsheet.api.SheetDiscussionResources;
import com.smartsheet.api.SheetFilterResources;
import com.smartsheet.api.SheetResources;
import com.smartsheet.api.SheetRowResources;
import com.smartsheet.api.SheetSummaryResources;
import com.smartsheet.api.SheetUpdateRequestResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.SmartsheetRestException;
import com.smartsheet.api.UserResources;
import com.smartsheet.api.internal.http.HttpEntity;
import com.smartsheet.api.internal.http.HttpMethod;
import com.smartsheet.api.internal.http.HttpRequest;
import com.smartsheet.api.internal.http.HttpResponse;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.MultiRowEmail;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.SheetEmail;
import com.smartsheet.api.models.SheetPublish;
import com.smartsheet.api.models.SortSpecifier;
import com.smartsheet.api.models.UpdateRequest;
import com.smartsheet.api.models.enums.CopyExclusion;
import com.smartsheet.api.models.enums.ObjectExclusion;
import com.smartsheet.api.models.enums.PaperSize;
import com.smartsheet.api.models.enums.SheetCopyInclusion;
import com.smartsheet.api.models.enums.SheetInclusion;
import com.smartsheet.api.models.enums.SheetTemplateInclusion;
import com.smartsheet.api.models.enums.SourceInclusion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is the implementation of the SheetResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class SheetResourcesImpl extends AbstractResources implements SheetResources {

    /**
     * The Constant BUFFER_SIZE.
     */
    private static final int BUFFER_SIZE = 4098;

    private static final String SHEETS = "sheets";
    private static final String TEXT_CSV = "text/csv";
    private static final String FOLDERS = "folders";
    private static final String INCLUDE = "include";
    private static final String IMPORT = "import";
    private static final String WORKSPACES = "WORKSPACES";
    private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * Represents the ShareResources.
     * <p>
     * It will be initialized in constructor and will not change afterward.
     */
    private ShareResources shares;
    /**
     * Represents the SheetRowResources.
     * <p>
     * It will be initialized in constructor and will not change afterward.
     */
    private SheetRowResources rows;
    /**
     * Represents the SheetColumnResources.
     * <p>
     * It will be initialized in constructor and will not change afterward.
     */
    private SheetColumnResources columns;
    /**
     * Represents the AssociatedAttachmentResources.
     * <p>
     * It will be initialized in constructor and will not change afterward.
     */
    private SheetAttachmentResources attachments;
    /**
     * Represents the AssociatedDiscussionResources.
     * <p>
     * It will be initialized in constructor and will not change afterward.
     */
    private SheetDiscussionResources discussions;

    /**
     * Represents the SheetCommentResources.
     * <p>
     * It will be initialized in constructor and will not change afterward
     */
    private SheetCommentResources comments;

    /**
     * Represents the SheetUpdateRequestResources.
     * <p>
     * It will be initialized in constructor and will not change afterward
     */
    private SheetUpdateRequestResources updateRequests;

    /**
     * Represents the SheetFilterResources.
     * <p>
     * It will be initialized in constructor and will not change afterward
     */
    private SheetFilterResources filters;

    /**
     * Represents the AutomationRules.
     * <p>
     * It will be initialized in the constructor and will not change afterward
     */
    private SheetAutomationRuleResources automationRules;

    /**
     * Represents the CrossSheetReferences
     * <p>
     * It will be initialized in the constructor and will not change afterward
     */
    private SheetCrossSheetReferenceResources crossSheetReferences;

    /**
     * Represents the sheetSummary
     * <p>
     * It will be initialized in the constructor and will not change afterward
     */
    private SheetSummaryResources sheetSummary;

    /**
     * Constructor.
     *
     * @param smartsheet the smartsheet
     * @throws IllegalArgumentException : if any argument is null
     */
    public SheetResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
        this.shares = new ShareResourcesImpl(smartsheet, SHEETS);
        this.rows = new SheetRowResourcesImpl(smartsheet);
        this.columns = new SheetColumnResourcesImpl(smartsheet);
        this.attachments = new SheetAttachmentResourcesImpl(smartsheet);
        this.discussions = new SheetDiscussionResourcesImpl(smartsheet);
        this.comments = new SheetCommentResourcesImpl(smartsheet);
        this.updateRequests = new SheetUpdateRequestResourcesImpl(smartsheet);
        this.filters = new SheetFilterResourcesImpl(smartsheet);
        this.automationRules = new SheetAutomationRuleResourcesImpl(smartsheet);
        this.crossSheetReferences = new SheetCrossSheetReferenceResourcesImpl(smartsheet);
        this.sheetSummary = new SheetSummaryResourcesImpl(smartsheet);
    }

    /**
     * List all sheets.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets
     *
     * @return A list of all sheets (note that an empty list will be returned if there are none).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public PagedResult<Sheet> listSheets() throws SmartsheetException {
        return this.listSheets(null, null, null);
    }

    /**
     * List all sheets.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets
     *
     * @param includes   the source inclusion
     * @param pagination the object containing the pagination parameters
     * @return A list of all sheets (note that an empty list will be returned if there are none).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public PagedResult<Sheet> listSheets(EnumSet<SourceInclusion> includes, PaginationParameters pagination) throws SmartsheetException {
        return this.listSheets(includes, pagination, null);
    }

    /**
     * List Sheets
     */
    public PagedResult<Sheet> listSheets(
            EnumSet<SourceInclusion> includes,
            PaginationParameters pagination,
            Date modifiedSince
    ) throws SmartsheetException {
        String path = SHEETS;

        Map<String, Object> parameters = new HashMap<>();
        if (pagination != null) {
            parameters = pagination.toHashMap();
        }
        if (modifiedSince != null) {
            String isoDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(modifiedSince);
            parameters.put("modifiedSince", isoDate);
        }
        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));

        path += QueryUtil.generateUrl(null, parameters);
        return this.listResourcesWithWrapper(path, Sheet.class);
    }

    /**
     * List all sheets in the organization.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users/sheets
     *
     * @param parameters the object containing the pagination parameters
     * @return all sheets (note that empty list will be returned if there is none)
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     * @deprecated As of release 2.0. Please use {@link UserResources} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public PagedResult<Sheet> listOrganizationSheets(PaginationParameters parameters) throws SmartsheetException {
        String path = "users/sheets";

        if (parameters != null) {
            path += parameters.toQueryString();
        }
        return this.listResourcesWithWrapper(path, Sheet.class);
    }

    /**
     * Get a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{id}
     *
     * @param id         the id
     * @param includes   used to specify the optional objects to include, currently DISCUSSIONS and ATTACHMENTS are supported.
     * @param columnIds  the column ids
     * @param excludes   the exclude parameters
     * @param page       the page number
     * @param pageSize   the page size
     * @param rowIds     the row ids
     * @param rowNumbers the row numbers
     * @return the resource (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet getSheet(
            long id,
            EnumSet<SheetInclusion> includes,
            EnumSet<ObjectExclusion> excludes,
            Set<Long> rowIds,
            Set<Integer> rowNumbers,
            Set<Long> columnIds,
            Integer pageSize,
            Integer page
    ) throws SmartsheetException {
        return this.getSheet(id, includes, excludes, rowIds, rowNumbers, columnIds, pageSize, page, null, null);
    }

    /**
     * Get a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{id}
     *
     * @param id             the id
     * @param includes       used to specify the optional objects to include, currently DISCUSSIONS and ATTACHMENTS are supported.
     * @param columnIds      the column ids
     * @param excludes       the exclude parameters
     * @param page           the page number
     * @param pageSize       the page size
     * @param rowIds         the row ids
     * @param rowNumbers     the row numbers
     * @param ifVersionAfter only fetch Sheet if more recent version available
     * @return the resource (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet getSheet(
            long id,
            EnumSet<SheetInclusion> includes,
            EnumSet<ObjectExclusion> excludes,
            Set<Long> rowIds,
            Set<Integer> rowNumbers,
            Set<Long> columnIds,
            Integer pageSize,
            Integer page,
            Integer ifVersionAfter
    ) throws SmartsheetException {
        return this.getSheet(id, includes, excludes, rowIds, rowNumbers, columnIds, pageSize, page, ifVersionAfter, null);
    }

    /**
     * Get a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{id}
     *
     * @param id             the id of the sheet
     * @param includes       used to specify the optional objects to include.
     * @param columnIds      the column ids
     * @param excludes       the exclude parameters
     * @param page           the page number
     * @param pageSize       the page size
     * @param rowIds         the row ids
     * @param rowNumbers     the row numbers
     * @param ifVersionAfter only fetch Sheet if more recent version available
     * @param level          compatibility level
     * @return the sheet resource (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet getSheet(
            long id,
            EnumSet<SheetInclusion> includes,
            EnumSet<ObjectExclusion> excludes,
            Set<Long> rowIds,
            Set<Integer> rowNumbers,
            Set<Long> columnIds,
            Integer pageSize,
            Integer page,
            Integer ifVersionAfter,
            Integer level
    ) throws SmartsheetException {

        String path = SHEETS + "/" + id;

        // Add the parameters to a map and build the query string at the end
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put("exclude", QueryUtil.generateCommaSeparatedList(excludes));
        parameters.put("rowIds", QueryUtil.generateCommaSeparatedList(rowIds));
        parameters.put("rowNumbers", QueryUtil.generateCommaSeparatedList(rowNumbers));
        parameters.put("columnIds", QueryUtil.generateCommaSeparatedList(columnIds));
        parameters.put("pageSize", pageSize);
        parameters.put("page", page);
        parameters.put("ifVersionAfter", ifVersionAfter);
        parameters.put("level", level);

        // Iterate through the map of parameters and generate the query string
        path += QueryUtil.generateUrl(null, parameters);

        return this.getResource(path, Sheet.class);
    }

    /**
     * <p>Get sheet by ID. Load all row and column data</p>
     *
     * @param sheetId id of the sheet to fetch
     * @return the sheet resource (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet getSheet(long sheetId) throws SmartsheetException {
        return getSheet(sheetId, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Get a sheet as an Excel file.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{id} with "application/vnd.ms-excel" Accept
     * HTTP header
     *
     * @param id           the id
     * @param outputStream the OutputStream to which the Excel file will be written
     * @throws IllegalArgumentException    : if outputStream is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public void getSheetAsExcel(long id, OutputStream outputStream) throws SmartsheetException {
        getSheetAsFile(id, null, outputStream, "application/vnd.ms-excel");
    }

    /**
     * Get a sheet as a PDF file.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{id} with "application/pdf" Accept HTTP
     * header
     *
     * @param id           the id
     * @param outputStream the output stream to which the PDF file will be written.
     * @param paperSize    the optional paper size
     * @throws IllegalArgumentException    : if outputStream is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public void getSheetAsPDF(long id, OutputStream outputStream, PaperSize paperSize) throws SmartsheetException {
        getSheetAsFile(id, paperSize, outputStream, "application/pdf");
    }

    /**
     * Create a sheet in default "Sheets" collection.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets
     *
     * @param sheet the sheet to create, limited to the following required attributes: * name (string) *
     *              columns (array of Column objects, limited to the following attributes) - title - primary - type - symbol -
     *              options
     * @return the created sheet
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet createSheet(Sheet sheet) throws SmartsheetException {
        return this.createResource(SHEETS, Sheet.class, sheet);
    }

    /**
     * Create a sheet (from existing sheet or template) in default "Sheets" collection.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets
     *
     * @param sheet    the sheet to create, limited to the following required attributes: * name (string) * fromId
     *                 (number): ID of the Sheet or Template from which to create the sheet.
     * @param includes used to specify the optional objects to include, currently DATA, DISCUSSIONS and ATTACHMENTS are supported.
     * @return the sheet
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet createSheetFromTemplate(Sheet sheet, EnumSet<SheetTemplateInclusion> includes) throws SmartsheetException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        String path = QueryUtil.generateUrl(SHEETS, parameters);

        return this.createResource(path, Sheet.class, sheet);
    }

    /**
     * Imports a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/import
     *
     * @param file               path to the CSV file
     * @param sheetName          destination sheet name
     * @param headerRowIndex     index (0 based) of row to be used for column names
     * @param primaryColumnIndex index (0 based) of primary column
     * @return the created sheet
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet importCsv(String file, String sheetName, Integer headerRowIndex, Integer primaryColumnIndex) throws SmartsheetException {
        return importFile("sheets/import", file, TEXT_CSV, sheetName, headerRowIndex, primaryColumnIndex);
    }

    /**
     * Imports a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/import
     *
     * @param file               path to the XLSX file
     * @param sheetName          destination sheet name
     * @param headerRowIndex     index (0 based) of row to be used for column names
     * @param primaryColumnIndex index (0 based) of primary column
     * @return the created sheet
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet importXlsx(String file, String sheetName, Integer headerRowIndex, Integer primaryColumnIndex) throws SmartsheetException {
        return importFile("sheets/import", file, XLSX_CONTENT_TYPE,
                sheetName, headerRowIndex, primaryColumnIndex);
    }

    /**
     * Create a sheet in given folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/sheets
     *
     * @param folderId the folder id
     * @param sheet    the sheet to create, limited to the following required
     *                 attributes: * name (string) * columns (array of Column objects, limited to the following attributes) - title -
     *                 primary - type - symbol - options
     * @return the created sheet
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet createSheetInFolder(long folderId, Sheet sheet) throws SmartsheetException {

        return this.createResource(FOLDERS + "/" + folderId + "/" + SHEETS, Sheet.class, sheet);
    }

    /**
     * Create a sheet in given folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folder/{folderId}/sheets
     *
     * @param folderId the folder id
     * @param sheet    the sheet
     * @param includes the includes
     * @return the sheet to create, limited to the following required
     * attributes: * name (string) * columns (array of Column objects, limited to the following attributes) - title -
     * primary - type - symbol - options
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet createSheetInFolderFromTemplate(
            long folderId,
            Sheet sheet,
            EnumSet<SheetTemplateInclusion> includes
    ) throws SmartsheetException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        String path = QueryUtil.generateUrl(FOLDERS + "/" + folderId + "/" + SHEETS, parameters);

        return this.createResource(path, Sheet.class, sheet);
    }

    /**
     * Imports a sheet in given folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/sheets/import
     *
     * @param folderId           the folder id
     * @param file               path to the CSV file
     * @param sheetName          destination sheet name
     * @param headerRowIndex     index (0 based) of row to be used for column names
     * @param primaryColumnIndex index (0 based) of primary column
     * @return the created sheet
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet importCsvInFolder(
            long folderId,
            String file,
            String sheetName,
            Integer headerRowIndex,
            Integer primaryColumnIndex
    ) throws SmartsheetException {
        return importFile(
                FOLDERS + "/" + folderId + "/" + SHEETS + "/" + IMPORT,
                file,
                TEXT_CSV,
                sheetName,
                headerRowIndex,
                primaryColumnIndex
        );
    }

    /**
     * Imports a sheet in given folder.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/sheets/import
     *
     * @param folderId           the folder id
     * @param file               path to the XLSX file
     * @param sheetName          destination sheet name
     * @param headerRowIndex     index (0 based) of row to be used for column names
     * @param primaryColumnIndex index (0 based) of primary column
     * @return the created sheet
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet importXlsxInFolder(
            long folderId,
            String file,
            String sheetName,
            Integer headerRowIndex,
            Integer primaryColumnIndex
    ) throws SmartsheetException {
        return importFile(
                FOLDERS + "/" + folderId + "/" + SHEETS + "/" + IMPORT,
                file,
                XLSX_CONTENT_TYPE,
                sheetName,
                headerRowIndex,
                primaryColumnIndex
        );
    }

    /**
     * Create a sheet in given workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspace/{workspaceId}/sheets
     *
     * @param workspaceId the workspace id
     * @param sheet       the sheet to create, limited to the following required attributes: * name (string) * columns
     *                    (array of Column objects, limited to the following attributes) - title - primary - type - symbol - options
     * @return the created sheet
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet createSheetInWorkspace(long workspaceId, Sheet sheet) throws SmartsheetException {
        return this.createResource(WORKSPACES + "/" + workspaceId + "/" + SHEETS, Sheet.class, sheet);
    }

    /**
     * Create a sheet (from existing sheet or template) in given workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspace/{workspaceId}/sheets
     *
     * @param workspaceId the workspace id
     * @param sheet       the sheet to create, limited to the following required attributes:
     *                    <ul>
     *                        <li>name (string)</li>
     *                        <li>fromId (number): ID of the Sheet or Template from which to create the sheet.</li>
     *                        <li>includes : used to specify the optional objects to include;
     *                        currently DATA, DISCUSSIONS and ATTACHMENTS are supported.</li>
     *                    </ul>
     * @param includes    the includes
     * @return the created sheet
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet createSheetInWorkspaceFromTemplate(long workspaceId, Sheet sheet, EnumSet<SheetTemplateInclusion> includes)
            throws SmartsheetException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        String path = QueryUtil.generateUrl(WORKSPACES + "/" + workspaceId + "/" + SHEETS, parameters);

        return this.createResource(path, Sheet.class, sheet);
    }

    /**
     * Imports a sheet in given workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspaces/{workspaceId}/sheets/import
     *
     * @param workspaceId        the workspace id
     * @param file               path to the CSV file
     * @param sheetName          destination sheet name
     * @param headerRowIndex     index (0 based) of row to be used for column names
     * @param primaryColumnIndex index (0 based) of primary column
     * @return the created sheet
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet importCsvInWorkspace(
            long workspaceId,
            String file,
            String sheetName,
            Integer headerRowIndex,
            Integer primaryColumnIndex
    ) throws SmartsheetException {
        return importFile(
                WORKSPACES + "/" + workspaceId + "/" + SHEETS + "/" + IMPORT,
                file,
                TEXT_CSV,
                sheetName,
                headerRowIndex,
                primaryColumnIndex
        );
    }

    /**
     * Imports a sheet in given workspace.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /workspaces/{workspaceId}/sheets/import
     *
     * @param workspaceId        the workspace id
     * @param file               path to the XLSX file
     * @param sheetName          destination sheet name
     * @param headerRowIndex     index (0 based) of row to be used for column names
     * @param primaryColumnIndex index (0 based) of primary column
     * @return the created sheet
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet importXlsxInWorkspace(
            long workspaceId,
            String file,
            String sheetName,
            Integer headerRowIndex,
            Integer primaryColumnIndex
    ) throws SmartsheetException {
        return importFile(
                WORKSPACES + "/" + workspaceId + "/" + SHEETS + "/" + IMPORT,
                file,
                XLSX_CONTENT_TYPE,
                sheetName,
                headerRowIndex,
                primaryColumnIndex
        );
    }

    /**
     * Delete a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /sheet{id}
     *
     * @param id the ID of the sheet
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public void deleteSheet(long id) throws SmartsheetException {
        this.deleteResource(SHEETS + "/" + id, Sheet.class);
    }

    /**
     * Update a sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /sheet/{id}
     *
     * @param sheet the sheet to update
     * @return the updated sheet
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public Sheet updateSheet(Sheet sheet) throws SmartsheetException {
        return this.updateResource(SHEETS + "/" + sheet.getId(), Sheet.class, sheet);
    }

    /**
     * Get a sheet version.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{id}/version
     *
     * @param id the id
     * @return the sheet version (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public int getSheetVersion(long id) throws SmartsheetException {
        return this.getResource(SHEETS + "/" + id + "/version", Sheet.class).getVersion();
    }

    /**
     * Send a sheet as a PDF attachment via email to the designated recipients.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheet/{sheetId}/emails
     *
     * @param id    the id
     * @param email the email
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public void sendSheet(long id, SheetEmail email) throws SmartsheetException {
        this.createResource(SHEETS + "/" + id + "/emails", SheetEmail.class, email);
    }

    /**
     * Get a sheet as an Excel file.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{id} with "application/vnd.ms-excel" Accept HTTP header
     *
     * @param id           the id of the sheet
     * @param outputStream the output stream to which the Excel file will be written.
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public void getSheetAsCSV(long id, OutputStream outputStream) throws SmartsheetException {
        getSheetAsFile(id, null, outputStream, TEXT_CSV);
    }

    /**
     * Get the status of the Publish settings of the sheet, including the URLs of any enabled publishings.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheet/{sheetId}/publish
     *
     * @param id the id
     * @return the resource (note that if there is no such resource, this method will throw ResourceNotFoundException
     * * rather than returning null).
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public SheetPublish getPublishStatus(long id) throws SmartsheetException {
        return this.getResource(SHEETS + "/" + id + "/publish", SheetPublish.class);
    }

    /**
     * Sets the publish status of a sheet and returns the new status, including the URLs of any enabled publishings.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /sheet/{sheetId}/publish
     *
     * @param id      the id
     * @param publish the SheetPublish object limited to the following attributes *
     *                readOnlyLiteEnabled * readOnlyFullEnabled * readWriteEnabled * icalEnabled
     * @return the updated SheetPublish (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public SheetPublish updatePublishStatus(long id, SheetPublish publish) throws SmartsheetException {
        return this.updateResource(SHEETS + "/" + id + "/publish", SheetPublish.class, publish);
    }

    /**
     * Creates a copy of the specified sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/copy
     *
     * @param sheetId              the sheet id
     * @param containerDestination describes the destination container
     * @param includes             optional parameters to include
     * @return the sheet
     * @throws IllegalArgumentException    : if folder is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet copySheet(
            long sheetId,
            ContainerDestination containerDestination,
            EnumSet<SheetCopyInclusion> includes
    ) throws SmartsheetException {
        return copySheet(sheetId, containerDestination, includes, null);
    }

    /**
     * Creates a copy of the specified sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/copy
     *
     * @param sheetId              the sheet id
     * @param containerDestination describes the destination container
     * @param includes             optional parameters to include
     * @param excludes             optional parameters to exclude
     * @return the sheet
     * @throws IllegalArgumentException    : if folder is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet copySheet(
            long sheetId,
            ContainerDestination containerDestination,
            EnumSet<SheetCopyInclusion> includes,
            EnumSet<CopyExclusion> excludes
    ) throws SmartsheetException {

        String path = SHEETS + "/" + sheetId + "/copy";
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(INCLUDE, QueryUtil.generateCommaSeparatedList(includes));
        parameters.put("exclude", QueryUtil.generateCommaSeparatedList(excludes));

        path += QueryUtil.generateUrl(null, parameters);

        return this.createResource(path, Sheet.class, containerDestination);
    }

    /**
     * Moves the specified Sheet to another location.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /folders/{folderId}/move
     *
     * @param sheetId              the folder id
     * @param containerDestination describes the destination container
     * @return the sheet
     * @throws IllegalArgumentException    : if folder is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet moveSheet(long sheetId, ContainerDestination containerDestination) throws SmartsheetException {

        String path = SHEETS + "/" + sheetId + "/move";
        return this.createResource(path, Sheet.class, containerDestination);
    }

    /**
     * Creates an Update Request for the specified Row(s) within the Sheet.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/updaterequests
     *
     * @param sheetId the sheet id
     * @param email   the email
     * @return the update request object
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public UpdateRequest createUpdateRequest(long sheetId, MultiRowEmail email) throws SmartsheetException {
        return this.createResource(SHEETS + "/" + sheetId + "/updaterequests", UpdateRequest.class, email);
    }

    /**
     * Sort a sheet according to the sort criteria.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheet/{sheetId}/sort
     *
     * @param sheetId       the sheet id
     * @param sortSpecifier the sort criteria
     * @return the update request object
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet sortSheet(long sheetId, SortSpecifier sortSpecifier) throws SmartsheetException {
        return this.sortSheet(sheetId, sortSpecifier, null);
    }

    /**
     * Sort a sheet according to the sort criteria.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheet/{sheetId}/sort
     *
     * @param sheetId       the sheet id
     * @param sortSpecifier the sort criteria
     * @param level         compatibility level
     * @return the update request object
     * @throws SmartsheetException         the smartsheet exception
     * @throws IllegalArgumentException    : if any argument is null
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    public Sheet sortSheet(long sheetId, SortSpecifier sortSpecifier, Integer level) throws SmartsheetException {
        Util.throwIfNull(sortSpecifier);

        String path = SHEETS + "/" + sheetId + "/sort";
        if (level != null) {
            path += "?level=" + level;
        }

        HttpRequest request = createHttpRequest(smartsheet.getBaseURI().resolve(path), HttpMethod.POST);

        ByteArrayOutputStream objectBytesStream = new ByteArrayOutputStream();
        this.smartsheet.getJsonSerializer().serialize(sortSpecifier, objectBytesStream);

        HttpEntity entity = new HttpEntity();
        entity.setContentType("application/json");
        entity.setContent(new ByteArrayInputStream(objectBytesStream.toByteArray()));
        entity.setContentLength(objectBytesStream.size());
        request.setEntity(entity);

        Sheet obj = null;
        try {
            HttpResponse response = this.smartsheet.getHttpClient().request(request);
            switch (response.getStatusCode()) {
                case 200: {
                    InputStream inputStream = response.getEntity().getContent();
                    try {
                        obj = this.smartsheet.getJsonSerializer().deserialize(Sheet.class, inputStream);
                    } catch (IOException e) {
                        throw new SmartsheetException(e);
                    }
                    break;
                }
                default:
                    handleError(response);
            }
        } finally {
            smartsheet.getHttpClient().releaseConnection();
        }
        return obj;
    }

    /**
     * Return the ShareResources object that provides access to Share resources associated with Sheet resources.
     *
     * @return the ShareResources object
     */
    public ShareResources shareResources() {
        return this.shares;
    }

    /**
     * Return the SheetRowResources object that provides access to Row resources associated with Sheet resources.
     *
     * @return the sheet row resources
     */
    public SheetRowResources rowResources() {
        return this.rows;
    }

    /**
     * Return the SheetColumnResources object that provides access to Column resources associated with Sheet resources.
     *
     * @return the sheet column resources
     */
    public SheetColumnResources columnResources() {
        return this.columns;
    }

    /**
     * Return the AssociatedAttachmentResources object that provides access to attachment resources associated with
     * Sheet resources.
     *
     * @return the associated attachment resources
     */
    public SheetAttachmentResources attachmentResources() {
        return this.attachments;
    }

    /**
     * Return the AssociatedDiscussionResources object that provides access to discussion resources associated with
     * Sheet resources.
     *
     * @return the associated discussion resources
     */
    public SheetDiscussionResources discussionResources() {
        return this.discussions;
    }

    /**
     * Return the SheetCommentResources object that provides access to discussion resources associated with
     * Sheet resources.
     *
     * @return the associated comment resources
     */
    public SheetCommentResources commentResources() {
        return this.comments;
    }

    /**
     * Return the SheetUpdateRequestResources object that provides access to update request resources
     * associated with Sheet resources.
     *
     * @return the associated update request resources
     */
    public SheetUpdateRequestResources updateRequestResources() {
        return this.updateRequests;
    }

    /**
     * Return the SheetFilterResources object that provides access to sheet filter resources
     * associated with Sheet resources.
     *
     * @return the associated sheet filter resources
     */
    public SheetFilterResources filterResources() {
        return this.filters;
    }

    /**
     * Return the SheetAutomationRuleResources object that provides access to automation rule resources
     * associated with the Sheet resources.
     *
     * @return the associated automation rule resources
     */
    public SheetAutomationRuleResources automationRuleResources() {
        return automationRules;
    }

    /**
     * Return the SheetCrossSheetReferenceResources object that provides access to the cross sheet reference resources
     * associated with the Sheet resources.
     *
     * @return the cross sheet reference resources
     */
    public SheetCrossSheetReferenceResources crossSheetReferenceResources() {
        return crossSheetReferences;
    }

    /**
     * Return the SheetSummaryResources object that provides access to the sheet summary resources
     * associated with the Sheet resources.
     *
     * @return the sheet summary resources
     */
    public SheetSummaryResources summaryResources() {
        return sheetSummary;
    }

    /**
     * Internal function used by all the import routines
     *
     * @param path               endpoint for import
     * @param file               full path to file
     * @param contentType        content type of the file being imported (either CSV or XLSX)
     * @param sheetName          sheetName from caller (can be null)
     * @param headerRowIndex     headerRowIndex from caller (can be null)
     * @param primaryColumnIndex primaryColumnIndex from caller (can be null)
     * @return the new imported sheet
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    private Sheet importFile(String path, String file, String contentType, String sheetName, Integer headerRowIndex,
                             Integer primaryColumnIndex) throws SmartsheetException {
        Util.throwIfNull(path, file, contentType);
        Util.throwIfEmpty(path, file, contentType);

        File f = new File(file);
        Map<String, Object> parameters = new HashMap<>();
        if (sheetName == null) {
            sheetName = f.getName();
        }
        parameters.put("sheetName", sheetName);
        parameters.put("headerRowIndex", headerRowIndex);
        parameters.put("primaryColumnIndex", primaryColumnIndex);
        path = QueryUtil.generateUrl(path, parameters);
        HttpRequest request = createHttpRequest(this.smartsheet.getBaseURI().resolve(path), HttpMethod.POST);
        request.getHeaders().put("Content-Disposition", "attachment");
        request.getHeaders().put("Content-Type", contentType);

        InputStream is = null;
        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new SmartsheetException(e);
        }

        HttpEntity entity = new HttpEntity();
        entity.setContentType(contentType);
        entity.setContent(is);
        entity.setContentLength(f.length());
        request.setEntity(entity);

        Sheet obj = null;
        try {
            HttpResponse response = this.smartsheet.getHttpClient().request(request);
            switch (response.getStatusCode()) {
                case 200:
                    obj = this.smartsheet.getJsonSerializer().deserializeResult(Sheet.class,
                            response.getEntity().getContent()).getResult();
                    break;
                default:
                    handleError(response);
            }
        } finally {
            smartsheet.getHttpClient().releaseConnection();
        }

        return obj;
    }

    /**
     * Get a sheet as a file.
     * <p>
     *
     * @param id           the id
     * @param paperSize    the paper size
     * @param outputStream the OutputStream to which the Excel file will be written
     * @param contentType  the content type
     * @throws InvalidRequestException     : if there is any problem with the REST API request
     * @throws AuthorizationException      : if there is any problem with the REST API authorization(access token)
     * @throws ResourceNotFoundException   : if the resource can not be found
     * @throws ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetRestException     : if there is any other REST API related error occurred during the operation
     * @throws SmartsheetException         : if there is any other error occurred during the operation
     */
    private void getSheetAsFile(long id, PaperSize paperSize, OutputStream outputStream, String contentType)
            throws SmartsheetException {
        Util.throwIfNull(outputStream, contentType);

        String path = SHEETS + "/" + id;
        if (paperSize != null) {
            path += "?paperSize=" + paperSize;
        }

        HttpRequest request;
        request = createHttpRequest(this.getSmartsheet().getBaseURI().resolve(path), HttpMethod.GET);
        request.getHeaders().put("Accept", contentType);

        com.smartsheet.api.internal.http.HttpResponse response = getSmartsheet().getHttpClient().request(request);

        switch (response.getStatusCode()) {
            case 200:
                try {
                    copyStream(response.getEntity().getContent(), outputStream);
                } catch (IOException e) {
                    throw new SmartsheetException(e);
                }
                break;
            default:
                handleError(response);
        }

        getSmartsheet().getHttpClient().releaseConnection();
    }

    /**
     * Copy stream.
     *
     * @param input  the input
     * @param output the output
     * @throws IOException Signals that an I/O exception has occurred.
     * @deprecated replace with StreamUtil.copyContentIntoOutputStream()
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    private static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
    }

}
