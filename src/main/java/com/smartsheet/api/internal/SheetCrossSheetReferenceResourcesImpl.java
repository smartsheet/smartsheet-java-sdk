/*
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

import com.smartsheet.api.AuthorizationException;
import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.ResourceNotFoundException;
import com.smartsheet.api.ServiceUnavailableException;
import com.smartsheet.api.SheetCrossSheetReferenceResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.models.CrossSheetReference;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;

import java.util.HashMap;
import java.util.Map;

public class SheetCrossSheetReferenceResourcesImpl extends AbstractResources implements SheetCrossSheetReferenceResources {
    private static final String SHEETS_PATH = "sheets/";

    /**
     * Constructor.
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public SheetCrossSheetReferenceResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * <p>Get all cross sheet references for this sheet</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/crosssheetreferences</p>
     *
     * Exceptions:
     *   IllegalArgumentException : if any argument is null
     *   InvalidRequestException : if there is any problem with the REST API request
     *   AuthorizationException : if there is any problem with the REST API authorization(access token)
     *   ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     *   SmartsheetRestException : if there is any other REST API related error occurred during the operation
     *   SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the sheet ID
     * @param pagination the pagination parameters
     * @return a list of cross sheet references
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<CrossSheetReference> listCrossSheetReferences(
            long sheetId,
            PaginationParameters pagination
    ) throws SmartsheetException {
        String path = SHEETS_PATH + sheetId + "/crosssheetreferences";
        Map<String, Object> parameters = new HashMap<>();

        if (pagination != null) {
            parameters = pagination.toHashMap();
        }
        path += QueryUtil.generateUrl(null, parameters);

        return this.listResourcesWithWrapper(path, CrossSheetReference.class);
    }

    /**
     * <p>Get a cross sheet reference.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/crosssheetreferences/{crossSheetReferenceId}</p>
     *
     * @param sheetId the sheet id
     * @param crossSheetReferenceId the cross sheet reference id
     * @return the cross sheet reference (note that if there is no such resource, this method will throw ResourceNotFoundException
     *     rather than returning null).
     * @throws IllegalArgumentException if any argument is null or empty string
     * @throws InvalidRequestException if there is any problem with the REST API request
     * @throws AuthorizationException if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException if there is any other error during the operation
     */
    public CrossSheetReference getCrossSheetReference(long sheetId, long crossSheetReferenceId) throws SmartsheetException {
        return this.getResource(SHEETS_PATH + sheetId + "/crosssheetreferences/" + crossSheetReferenceId, CrossSheetReference.class);
    }

    /**
     * <p>Create a cross sheet reference in the given sheet.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /sheets/{sheetId}/crosssheetreferences</p>
     *
     * @param sheetId the sheet id
     * @param crossSheetReference the cross sheet reference to create
     * @return the created cross sheet reference
     * @throws IllegalArgumentException if any argument is null or empty string
     * @throws InvalidRequestException if there is any problem with the REST API request
     * @throws AuthorizationException if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException if there is any other error during the operation
     */
    public CrossSheetReference createCrossSheetReference(long sheetId, CrossSheetReference crossSheetReference) throws SmartsheetException {
        return this.createResource(SHEETS_PATH + sheetId + "/crosssheetreferences", CrossSheetReference.class, crossSheetReference);
    }
}
