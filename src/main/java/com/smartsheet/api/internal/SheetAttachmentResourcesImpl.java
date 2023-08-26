/*
 * Smartsheet SDK for Java
 * Copyright (C) 2023 Smartsheet
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

import com.smartsheet.api.AttachmentVersioningResources;
import com.smartsheet.api.AuthorizationException;
import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.ServiceUnavailableException;
import com.smartsheet.api.SheetAttachmentResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.Attachment;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This is the implementation of the SheetAttachmentResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class SheetAttachmentResourcesImpl extends AbstractResources implements SheetAttachmentResources {
    private AttachmentVersioningResources versioning;

    private static final String SHEETS_PATH = "sheets/";

    private static final String ATTACHMENTS_PATH = "/attachments";

    /**
     * Constructor
     */
    public SheetAttachmentResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
        this.versioning = new AttachmentVersioningResourcesImpl(smartsheet);
    }

    /**
     * Attach a URL to a sheet.
     * <p>
     * The URL can be a normal URL (attachmentType "URL"), a Google Drive URL (attachmentType "GOOGLE_DRIVE") or a
     * Box.com URL (attachmentType "BOX_COM").
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/attachments
     *
     * @param sheetId the sheet id
     * @param attachment the attachment object
     * @return the attachment object
     * @throws IllegalArgumentException if any argument is null or empty string
     * @throws InvalidRequestException if there is any problem with the REST API request
     * @throws AuthorizationException if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException if there is any other error during the operation
     */
    public Attachment attachUrl(long sheetId, Attachment attachment) throws SmartsheetException {
        return this.createResource(SHEETS_PATH + sheetId + ATTACHMENTS_PATH, Attachment.class, attachment);
    }

    /**
     * Delete an attachment.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /sheets/{sheetId}/attachments/{attachmentId}
     * <p>
     * Exceptions:
     *   InvalidRequestException : if there is any problem with the REST API request
     *   AuthorizationException : if there is any problem with the REST API authorization(access token)
     *   ResourceNotFoundException : if the resource can not be found
     *   ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     *   SmartsheetRestException : if there is any other REST API related error occurred during the operation
     *   SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the ID of the sheet
     * @param attachmentId the ID of the attachment
     * @throws SmartsheetException the smartsheet exception
     */
    public void deleteAttachment(long sheetId, long attachmentId) throws SmartsheetException {
        this.deleteResource(SHEETS_PATH + sheetId + "/attachments/" + attachmentId, Attachment.class);
    }

    /**
     * Get an attachment.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /attachment/{id}
     * <p>
     * Returns: the resource (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * <p>
     * Exceptions:
     *   InvalidRequestException : if there is any problem with the REST API request
     *   AuthorizationException : if there is any problem with the REST API authorization(access token)
     *   ResourceNotFoundException : if the resource can not be found
     *   ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     *   SmartsheetRestException : if there is any other REST API related error occurred during the operation
     *   SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the sheet id
     * @param attachmentId the attachment id
     * @return the resource (note that if there is no such resource, this method will throw ResourceNotFoundException
     *     rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public Attachment getAttachment(long sheetId, long attachmentId) throws SmartsheetException {
        return this.getResource(SHEETS_PATH + sheetId + "/attachments/" + attachmentId, Attachment.class);
    }

    /**
     * Gets a list of all Attachments that are on the Sheet, including Sheet, Row, and Discussion level Attachments.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/attachments
     * <p>
     * Exceptions:
     *   InvalidRequestException : if there is any problem with the REST API request
     *   AuthorizationException : if there is any problem with the REST API authorization(access token)
     *   ResourceNotFoundException : if the resource can not be found
     *   ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     *   SmartsheetRestException : if there is any other REST API related error occurred during the operation
     *   SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the ID of the sheet to which the attachments are associated
     * @param parameters the pagination parameters
     * @return the attachments (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<Attachment> listAttachments(long sheetId, PaginationParameters parameters) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + ATTACHMENTS_PATH;

        if (parameters != null) {
            path += parameters.toQueryString();
        }
        return this.listResourcesWithWrapper(path, Attachment.class);
    }

    /**
     * Attach a file to a sheet with simple upload.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/attachments
     *
     * @param sheetId the id of the sheet
     * @param file the file to attach
     * @param contentType the content type of the file
     * @return the created attachment
     * @throws FileNotFoundException the file not found exception
     * @throws IllegalArgumentException if any argument is null or empty string
     * @throws InvalidRequestException if there is any problem with the REST API request
     * @throws AuthorizationException if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException if there is any other error during the operation
     */
    public Attachment attachFile(long sheetId, File file, String contentType) throws FileNotFoundException,
            SmartsheetException {
        Util.throwIfNull(sheetId, file, contentType);
        Util.throwIfEmpty(contentType);

        return attachFile(sheetId, new FileInputStream(file), contentType, file.length(), file.getName());
    }

    /**
     * Attach file for simple upload.
     *
     * @param sheetId the sheet id
     * @param inputStream attachment data inputStream
     * @param contentType the content type
     * @param contentLength the content length
     * @param attachmentName the name of the attachment
     * @return the attachment
     * @throws SmartsheetException the smartsheet exception
     */
    public Attachment attachFile(long sheetId, InputStream inputStream, String contentType, long contentLength, String attachmentName)
            throws SmartsheetException {
        Util.throwIfNull(inputStream, contentType);
        return super.attachFile(SHEETS_PATH + sheetId + ATTACHMENTS_PATH, inputStream, contentType, contentLength, attachmentName);
    }

    /**
     * Creates an object of AttachmentVersioningResources for access to versioning through SheetAttachmentResources.
     *
     * @return the created attachment
     * @throws SmartsheetException if there is any other error during the operation
     */
    public AttachmentVersioningResources versioningResources() throws SmartsheetException {
        return versioning;
    }
}
