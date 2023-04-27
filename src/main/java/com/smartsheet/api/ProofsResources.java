package com.smartsheet.api;

import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Proof;

/**
 * <p>This interface provides methods to access Proof resources </p>
 *
 * <p>Thread Safety: Implementation of this interface must be thread safe.</p>
 */
public interface ProofsResources {

    /**
     * <p>List all proofs for a given sheet.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /sheets/{sheetId}/proofs </p>
     *
     Exceptions:
     *   IllegalArgumentException : if any argument is null
     *   InvalidRequestException : if there is any problem with the REST API request
     *   AuthorizationException : if there is any problem with the REST API authorization(access token)
     *   ResourceNotFoundException : if the resource can not be found
     *   ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     *   SmartsheetRestException : if there is any other REST API related error occurred during the operation
     *   SmartsheetException : if there is any other error occurred during the operation
     *
     * @param sheetId the id of the sheet
     * @param parameters the parameters for pagination
     * @return the proofs for the sheet id.
     * @throws SmartsheetException if there is any other error during the operation
     */
    PagedResult<Proof> listProofs(long sheetId, PaginationParameters parameters) throws SmartsheetException;

    Proof getProof(long sheetId, long proofId) throws SmartsheetException;

}
