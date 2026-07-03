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
import com.smartsheet.api.ReportResources;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.ServiceUnavailableException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.HttpEntity;
import com.smartsheet.api.internal.http.HttpMethod;
import com.smartsheet.api.internal.http.HttpRequest;
import com.smartsheet.api.internal.http.HttpResponse;
import com.smartsheet.api.internal.json.JSONSerializerException;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.models.CreateReportRequest;
import com.smartsheet.api.models.CreateReportResult;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.ReportColumn;
import com.smartsheet.api.models.ReportDefinition;
import com.smartsheet.api.models.ReportPublish;
import com.smartsheet.api.models.Result;
import com.smartsheet.api.models.ReportScopeInclusion;
import com.smartsheet.api.models.SheetEmail;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.UpdateReportColumnRequest;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.enums.ReportInclusion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * This is the implementation of the ReportResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */

public class ReportResourcesImpl extends AbstractResources implements ReportResources {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String REPORTS_PATH = "reports/";
    private static final String REPORTS = "reports";
    private static final String SCOPE_PATH = "/scope";
    private static final String COLUMNS_PATH = "/columns/";

    /**
     * Constructor.
     * <p>
     * Parameters: - smartsheet : the SmartsheetImpl
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public ReportResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * Get a report.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{id}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param reportId the folder id
     * @param includes the optional objects to include in response
     * @param pageSize Number of rows per page
     * @param page     page number to return
     * @return the report (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null)
     * @throws SmartsheetException the smartsheet exception
     */
    public Report getReport(long reportId, EnumSet<ReportInclusion> includes, Integer pageSize, Integer page) throws SmartsheetException {
        return this.getReport(reportId, includes, pageSize, page, null);
    }

    /**
     * Get a report.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{id}
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param reportId the folder id
     * @param includes the optional objects to include in response
     * @param pageSize Number of rows per page
     * @param page     page number to return
     * @param level    compatibility level
     * @return the report (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null)
     * @throws SmartsheetException the smartsheet exception
     */
    public Report getReport(
            long reportId,
            EnumSet<ReportInclusion> includes,
            Integer pageSize,
            Integer page,
            Integer level
    ) throws SmartsheetException {
        String path = REPORTS_PATH + reportId;
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("include", QueryUtil.generateCommaSeparatedList(includes));
        if (pageSize != null) {
            parameters.put("pageSize", pageSize.toString());
        }

        if (page != null) {
            parameters.put("page", page.toString());
        }

        if (level != null) {
            parameters.put("level", level);
        }

        path += QueryUtil.generateUrl(null, parameters);
        return this.getResource(path, Report.class);
    }

    /**
     * Sends a report as a PDF attachment via email to the designated recipients.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /reports/{id}/emails
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param reportId the report id
     * @param email    the recipient email
     * @throws SmartsheetException the smartsheet exception
     */
    public void sendReport(long reportId, SheetEmail email) throws SmartsheetException {
        this.createResource(REPORTS_PATH + reportId + "/emails", SheetEmail.class, email);
    }

    /**
     * List all reports.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param pagination pagination parameters for paging result
     * @return all sheets (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<Report> listReports(PaginationParameters pagination) throws SmartsheetException {
        return this.listReports(pagination, null);
    }

    /**
     * List all reports.
     */
    public PagedResult<Report> listReports(PaginationParameters pagination, Date modifiedSince) throws SmartsheetException {
        String path = REPORTS;

        Map<String, Object> parameters = new HashMap<>();
        if (pagination != null) {
            parameters = pagination.toHashMap();
        }
        if (modifiedSince != null) {
            String isoDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(modifiedSince);
            parameters.put("modifiedSince", isoDate);
        }

        path += QueryUtil.generateUrl(null, parameters);
        return this.listResourcesWithWrapper(path, Report.class);
    }

    /**
     * Get a Report as an Excel file.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{id} with "application/vnd.ms-excel" Accept
     * HTTP header
     * <p>
     * Exceptions:
     * IllegalArgumentException : if outputStream is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param id           the id
     * @param outputStream the OutputStream to which the Excel file will be written
     * @throws SmartsheetException the smartsheet exception
     */
    public void getReportAsExcel(long id, OutputStream outputStream) throws SmartsheetException {
        getResourceAsFile(REPORTS_PATH + id, "application/vnd.ms-excel", outputStream);
    }

    /**
     * Get a Report as a csv file.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{id} with "text/csv" Accept
     * HTTP header
     * <p>
     * Exceptions:
     * IllegalArgumentException : if outputStream is null
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param id           the id
     * @param outputStream the OutputStream to which the Excel file will be written
     * @throws SmartsheetException the smartsheet exception
     */
    public void getReportAsCsv(long id, OutputStream outputStream) throws SmartsheetException {
        getResourceAsFile(REPORTS_PATH + id, "text/csv", outputStream);
    }

    /**
     * Get the publish status of a report.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{id}/publish
     * <p>
     * Exceptions:
     * InvalidRequestException : if there is any problem with the REST API request
     * AuthorizationException : if there is any problem with the REST API authorization(access token)
     * ResourceNotFoundException : if the resource can not be found
     * ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * SmartsheetException : if there is any other error occurred during the operation
     *
     * @param id the ID of the report
     * @return the report publish status (note that if there is no such resource, this method will
     * throw ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public ReportPublish getPublishStatus(long id) throws SmartsheetException {
        return this.getResource(REPORTS_PATH + id + "/publish", ReportPublish.class);
    }

    /**
     * Sets the publish status of a report and returns the new status, including the URLs of any
     * enabled publishing.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /reports/{id}/publish
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param id            the ID of the report
     * @param reportPublish the ReportPublish object
     * @return the updated ReportPublish (note that if there is no such resource, this method will
     * throw ResourceNotFoundException rather than returning null)
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public ReportPublish updatePublishStatus(long id, ReportPublish reportPublish) throws SmartsheetException {
        return this.updateResource(REPORTS_PATH + id + "/publish", ReportPublish.class, reportPublish);
    }

    /**
     * Gets a report's definition (filters, grouping, summarizing, and sorting).
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{id}/definition
     *
     * @param reportId the ID of the report
     * @return the ReportDefinition object
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public ReportDefinition getReportDefinition(long reportId) throws SmartsheetException {
        return this.getResource(REPORTS_PATH + reportId + "/definition", ReportDefinition.class);
    }

    /**
     * Updates a report's definition (filters, grouping, summarizing, and sorting).
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /reports/{id}/definition
     * <p>
     * This endpoint supports partial updates only on root level properties of the report definition,
     * such as filters, groupingCriteria, and summarizingCriteria. For example, you can update the
     * report's filters without affecting its grouping criteria. However, nested properties within
     * these objects, such as a specific filter or grouping criterion, cannot be updated individually
     * and require a full replacement of the respective section.
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param id         the ID of the report
     * @param reportDefinition the ReportDefinition object containing the updated definition
     * @throws IllegalArgumentException    if any argument is null
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public void updateReportDefinition(long id, ReportDefinition reportDefinition) throws SmartsheetException {
        String path = REPORTS_PATH + id + "/definition";
        this.putResource(path, Result.class, reportDefinition);
    }

    /**
     * <p>Deletes a report.</p>
     *
     * <p>Mirrors the following Smartsheet REST API method: DELETE /reports/{reportId}</p>
     *
     * @param id the id of the report
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public void deleteReport(long id) throws SmartsheetException {
        this.deleteResource(REPORTS_PATH + id, Report.class);
    }

    /**
     * <p>Adds one or more specified sheet or workspace to the report scope.</p>
     *
     * @param id          the ID of the report
     * @param scopes A list of one or more objects denoting the sheets or workspaces associated with
     *               the report to be added to the report scope.
     * @throws IllegalArgumentException    if any argument is null or empty
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    @Override
    public void addReportScope(long id, List<ReportScopeInclusion> scopes) throws SmartsheetException {
        Util.throwIfNull(scopes);

        if (scopes.isEmpty()) {
            throw new IllegalArgumentException("scopes should not be empty.");
        }

        String path = REPORTS_PATH + id + SCOPE_PATH;
        HttpRequest request = createHttpRequest(smartsheet.getBaseURI().resolve(path), HttpMethod.POST);
        setRequestEntity(request, scopes);

        try {
            HttpResponse response = this.smartsheet.getHttpClient().request(request);
            if (response.getStatusCode() != 200) {
                handleError(response);
            }
        } finally {
            smartsheet.getHttpClient().releaseConnection();
        }
    }

    /**
     * <p>Removes one or more specified sheet or workspace from the report scope.</p>
     *
     * @param id             the ID of the report
     * @param scopes A list of one or more objects denoting the sheets or workspaces associated with
     *               the report to be removed from the report scope.
     * @throws IllegalArgumentException    if any argument is null or empty
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    @Override
    public void removeReportScope(long id, List<ReportScopeInclusion> scopes) throws SmartsheetException {
        Util.throwIfNull(scopes);

        if (scopes.isEmpty()) {
            throw new IllegalArgumentException("scopes should not be empty.");
        }

        String path = REPORTS_PATH + id + SCOPE_PATH;
        HttpRequest request = createHttpRequest(smartsheet.getBaseURI().resolve(path), HttpMethod.DELETE);
        setRequestEntity(request, scopes);

        try {
            HttpResponse response = this.smartsheet.getHttpClient().request(request);
            if (response.getStatusCode() != 200) {
                handleError(response);
            }
        } finally {
            smartsheet.getHttpClient().releaseConnection();
        }
    }

    /**
     * <p>Add reportColumns to a report.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /reports/{reportId}/reportColumns</p>
     *
     * <p>Note: All indexes of the reportColumns must be equal.</p>
     *
     * @param reportId the ID of the report
     * @param reportColumns  the list of reportColumns to add (must contain 1-400 items)
     * @return the list of reportColumns that were added
     * @throws IllegalArgumentException    if any argument is null or empty
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    @Override
    public List<ReportColumn> addReportColumns(long reportId, List<ReportColumn> reportColumns) throws SmartsheetException {
        Util.throwIfNull(reportColumns);

        if (reportColumns.isEmpty()) {
            throw new IllegalArgumentException("reportColumns should not be empty.");
        }

        return this.postAndReceiveList(REPORTS_PATH + reportId + "/columns", reportColumns, ReportColumn.class);
    }

    /**
     * <p>List columns for a report.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /reports/{reportId}/columns</p>
     *
     * @param reportId the ID of the report
     * @param lastKey  token for retrieving the next page of results (optional)
     * @param maxItems maximum number of items to return (optional)
     * @return a TokenPaginatedResult containing the list of ReportColumn objects and a lastKey for pagination
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    @Override
    public TokenPaginatedResult<ReportColumn> listReportColumns(long reportId, String lastKey, Long maxItems) throws SmartsheetException {
        return listReportColumns(reportId, lastKey, maxItems, null);
    }

    /**
     * <p>List columns for a report.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /reports/{reportId}/columns</p>
     *
     * @param reportId the ID of the report
     * @param lastKey  token for retrieving the next page of results (optional)
     * @param maxItems maximum number of items to return (optional)
     * @param level compatibility level
     * @return a TokenPaginatedResult containing the list of ReportColumn objects and a lastKey for pagination
     * @throws SmartsheetException if there is any error during the operation
     */
    @Override
    public TokenPaginatedResult<ReportColumn> listReportColumns(long reportId, String lastKey, Long maxItems, Integer level) throws SmartsheetException {
        String path = REPORTS_PATH + reportId + "/columns";
        Map<String, Object> parameters = new HashMap<>();
        if (lastKey != null) {
            parameters.put("lastKey", lastKey);
        }
        if (maxItems != null) {
            parameters.put("maxItems", maxItems);
        }
        if (level != null) {
            parameters.put("level", level);
        }
        path += QueryUtil.generateUrl(null, parameters);
        return listResourcesWithTokenPagination(path, ReportColumn.class);
    }

    /**
     * <p>List the scope of a report (the sheets and workspaces included in the report).</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /reports/{reportId}/scope</p>
     *
     * @param reportId the ID of the report
     * @param lastKey  token for retrieving the next page of results (optional)
     * @param maxItems maximum number of items to return (optional)
     * @return a TokenPaginatedResult containing the list of ReportScopeInclusion objects
     * @throws SmartsheetException if there is any error during the operation
     */
    @Override
    public TokenPaginatedResult<ReportScopeInclusion> listReportScope(
            long reportId, String lastKey, Long maxItems) throws SmartsheetException {
        String path = REPORTS_PATH + reportId + SCOPE_PATH;
        Map<String, Object> parameters = new HashMap<>();
        if (lastKey != null) {
            parameters.put("lastKey", lastKey);
        }
        if (maxItems != null) {
            parameters.put("maxItems", maxItems);
        }
        path += QueryUtil.generateUrl(null, parameters);
        return listResourcesWithTokenPagination(path, ReportScopeInclusion.class);
    }

    /**
     * <p>Create a new report.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /reports</p>
     *
     * <p>Creates a new report by specifying name, destination, scope, columns and definition.</p>
     *
     * @param request the CreateReportRequest containing report specifications
     * @return the CreateReportResult containing the newly created report information
     * @throws IllegalArgumentException    if any argument is null
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    @Override
    public CreateReportResult createReport(CreateReportRequest request) throws SmartsheetException {
        Util.throwIfNull(request);

        return this.createResource(REPORTS, CreateReportResult.class, request);
    }

    /**
     * Get a single column from a report.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{reportId}/columns/{columnVirtualId}
     *
     * @param reportId        the ID of the report
     * @param columnVirtualId the virtual ID of the column
     * @return the ReportColumn
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public ReportColumn getReportColumn(long reportId, long columnVirtualId) throws SmartsheetException {
        return getReportColumn(reportId, columnVirtualId, null);
    }

    /**
     * Get a single column from a report.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /reports/{reportId}/columns/{columnVirtualId}
     *
     * @param reportId        the ID of the report
     * @param columnVirtualId the virtual ID of the column
     * @param level           compatibility level
     * @return the ReportColumn
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public ReportColumn getReportColumn(long reportId, long columnVirtualId, Integer level) throws SmartsheetException {
        String path = REPORTS_PATH + reportId + COLUMNS_PATH + columnVirtualId;
        Map<String, Object> parameters = new HashMap<>();
        if (level != null) {
            parameters.put("level", level);
        }
        path += QueryUtil.generateUrl(null, parameters);

        return this.getResource(path, ReportColumn.class);
    }

    /**
     * Delete a single column from a report.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /reports/{reportId}/columns/{columnVirtualId}
     *
     * @param reportId        the ID of the report
     * @param columnVirtualId the virtual ID of the column
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public void deleteReportColumn(long reportId, long columnVirtualId) throws SmartsheetException {
        this.deleteResource(REPORTS_PATH + reportId + COLUMNS_PATH + columnVirtualId, ReportColumn.class);
    }

    /**
     * Updates a report column.
     * <p>
     * It mirrors to the following Smartsheet REST API method: PUT /reports/{reportId}/columns/{columnVirtualId}
     *
     * @param reportId        the ID of the report
     * @param columnVirtualId the virtual ID of the column to update
     * @param request         the UpdateReportColumnRequest containing the fields to update
     * @return the updated ReportColumn
     * @throws SmartsheetException the smartsheet exception
     */
    @Override
    public ReportColumn updateReportColumn(
            long reportId, long columnVirtualId, UpdateReportColumnRequest request) throws SmartsheetException {
        return this.updateResource(REPORTS_PATH + reportId + COLUMNS_PATH + columnVirtualId,
                ReportColumn.class, request);
    }

    private void setRequestEntity(HttpRequest request, Object object) throws JSONSerializerException {
        ByteArrayOutputStream objectBytesStream = new ByteArrayOutputStream();
        this.smartsheet.getJsonSerializer().serialize(object, objectBytesStream);
        HttpEntity entity = new HttpEntity();
        entity.setContentType(JSON_CONTENT_TYPE);
        entity.setContent(new ByteArrayInputStream(objectBytesStream.toByteArray()));
        entity.setContentLength(objectBytesStream.size());
        request.setEntity(entity);
    }
}
