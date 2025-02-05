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
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.UserResources;
import com.smartsheet.api.internal.http.HttpEntity;
import com.smartsheet.api.internal.http.HttpMethod;
import com.smartsheet.api.internal.http.HttpRequest;
import com.smartsheet.api.internal.http.HttpResponse;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.AlternateEmail;
import com.smartsheet.api.models.DeleteUserParameters;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserProfile;
import com.smartsheet.api.models.enums.ListUserInclusion;
import com.smartsheet.api.models.enums.UserInclusion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the implementation of the UserResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class UserResourcesImpl extends AbstractResources implements UserResources {

    private static final String USERS = "users";
    private static final String ALTERNATE_EMAILS = "alternateemails";

    /**
     * Constructor.
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public UserResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * List all users.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users
     *
     * @return the list of all users
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public PagedResult<User> listUsers() throws SmartsheetException {
        return this.listUsersInternal(null, null, null);
    }

    /**
     * List all users.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users
     * <p>
     * Exceptions:
     *   - InvalidRequestException : if there is any problem with the REST API request
     *   - AuthorizationException : if there is any problem with the REST API authorization(access token)
     *   - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     *   - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     *   - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param pagination the object containing the pagination query parameters
     * @return all users (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<User> listUsers(PaginationParameters pagination) throws SmartsheetException {
        return this.listUsersInternal(null, null, pagination);
    }

    /**
     * List all users.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param email      the list of email addresses
     * @param pagination the object containing the pagination query parameters
     * @return all users (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<User> listUsers(Set<String> email, PaginationParameters pagination) throws SmartsheetException {
        return this.listUsersInternal(email, null, pagination);
    }

    /**
     * List all users.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param email      the list of email addresses
     * @param includes   elements to include in the response
     * @param pagination the object containing the pagination query parameters
     * @return all users (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    public PagedResult<User> listUsers(Set<String> email, EnumSet<ListUserInclusion> includes,
                                       PaginationParameters pagination) throws SmartsheetException {
        return this.listUsersInternal(email, includes, pagination);
    }

    /**
     * List all users.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users
     * <p>
     * Exceptions:
     *   - InvalidRequestException : if there is any problem with the REST API request
     *   - AuthorizationException : if there is any problem with the REST API authorization(access token)
     *   - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     *   - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     *   - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param email the list of email addresses
     * @param includes elements to include in the response
     * @param pagination the object containing the pagination query parameters
     * @return all users (note that empty list will be returned if there is none)
     * @throws SmartsheetException the smartsheet exception
     */
    private PagedResult<User> listUsersInternal(Set<String> email, EnumSet<ListUserInclusion> includes,
                                       PaginationParameters pagination) throws SmartsheetException {
        String path = USERS;
        Map<String, Object> parameters = new HashMap<>();

        if (pagination != null) {
            parameters = pagination.toHashMap();
        }

        if (email != null) {
            parameters.put("email", QueryUtil.generateCommaSeparatedList(email));
        }

        if (includes != null) {
            parameters.put("include", QueryUtil.generateCommaSeparatedList(includes));
        }

        path += QueryUtil.generateUrl(null, parameters);
        return this.listResourcesWithWrapper(path, User.class);
    }

    /**
     * Add a user to the organization, without sending email.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /users
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param user the user object limited to the following attributes: * admin * email * licensedSheetCreator
     * @return the user
     * @throws SmartsheetException the smartsheet exception
     */
    public User addUser(User user) throws SmartsheetException {
        return this.createResource(USERS, User.class, user);
    }

    /**
     * Add a user to the organization, without sending email.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /users
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @param user      the created user
     * @param sendEmail whether to send email
     * @return the user object limited to the following attributes: * admin * email * licensedSheetCreator
     * @throws SmartsheetException the smartsheet exception
     */
    public User addUser(User user, boolean sendEmail) throws SmartsheetException {
        return this.createResource("users?sendEmail=" + sendEmail, User.class, user);
    }

    /**
     * Get the current user.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users/{userId}
     *
     * @param userId the user id
     * @return the user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public UserProfile getUser(long userId) throws SmartsheetException {
        return this.getResource(USERS + "/" + userId, UserProfile.class);
    }

    /**
     * Get the current user.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users/me
     * <p>
     * Exceptions:
     * - InvalidRequestException : if there is any problem with the REST API request
     * - AuthorizationException : if there is any problem with the REST API authorization(access token)
     * - ResourceNotFoundException : if the resource can not be found
     * - ServiceUnavailableException : if the REST API service is not available (possibly due to rate limiting)
     * - SmartsheetRestException : if there is any other REST API related error occurred during the operation
     * - SmartsheetException : if there is any other error occurred during the operation
     *
     * @return the resource (note that if there is no such resource, this method will throw ResourceNotFoundException
     * rather than returning null).
     * @throws SmartsheetException the smartsheet exception
     */
    public UserProfile getCurrentUser() throws SmartsheetException {
        return this.getResource("users/me", UserProfile.class);
    }

    /**
     * <p>Get the current user.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /user/me</p>
     *
     * @param includes used to specify the optional objects to include.
     * @return the current user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public UserProfile getCurrentUser(EnumSet<UserInclusion> includes) throws SmartsheetException {
        String path = "users/me";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("include", QueryUtil.generateCommaSeparatedList(includes));

        path += QueryUtil.generateUrl(null, parameters);
        return this.getResource(path, UserProfile.class);
    }

    /**
     * List all organisation sheets.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users/sheets
     *
     * @param pagination the object containing the pagination query parameters
     * @return the list of all organisation sheets
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public PagedResult<Sheet> listOrgSheets(PaginationParameters pagination, Date modifiedSince) throws SmartsheetException {
        String path = "users/sheets";

        Map<String, Object> parameters = new HashMap<>();
        if (pagination != null) {
            parameters = pagination.toHashMap();
        }
        if (modifiedSince != null) {
            String isoDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(modifiedSince);
            parameters.put("modifiedSince", isoDate);
        }
        path += QueryUtil.generateUrl(null, parameters);
        return this.listResourcesWithWrapper(path, Sheet.class);
    }

    public PagedResult<Sheet> listOrgSheets(PaginationParameters pagination) throws SmartsheetException {
        return this.listOrgSheets(pagination, null);
    }

    /**
     * List all user alternate emails.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users/{userId}/alternateemails
     *
     * @param userId     the id of the user
     * @param pagination the object containing the pagination query parameters
     * @return the list of all user alternate emails
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public PagedResult<AlternateEmail> listAlternateEmails(long userId, PaginationParameters pagination) throws SmartsheetException {
        String path = USERS + "/" + userId + "/" + ALTERNATE_EMAILS;

        if (pagination != null) {
            path += pagination.toQueryString();
        }
        return this.listResourcesWithWrapper(path, AlternateEmail.class);
    }

    /**
     * Get alternate email.
     * <p>
     * It mirrors to the following Smartsheet REST API method: GET /users/{userId}/alternateemails/{alternateEmailId}
     *
     * @param userId     the id of the user
     * @param altEmailId the alternate email id for the alternate email to retrieve.
     * @return the resource (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public AlternateEmail getAlternateEmail(long userId, long altEmailId) throws SmartsheetException {
        return this.getResource(USERS + "/" + userId + "/" + ALTERNATE_EMAILS + "/" + altEmailId, AlternateEmail.class);
    }

    /**
     * Add an alternate email.
     * <p>
     * It mirrors to the following Smartsheet REST API method: POST /users/{userId}/alternateemails
     *
     * @param userId    the id of the user
     * @param altEmails AlternateEmail alternate email address to add.
     * @return the resource (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public List<AlternateEmail> addAlternateEmail(long userId, List<AlternateEmail> altEmails) throws SmartsheetException {
        Util.throwIfNull(altEmails);
        if (altEmails.size() == 0) {
            return altEmails;
        }
        return this.postAndReceiveList(USERS + "/" + userId + "/" + ALTERNATE_EMAILS, altEmails, AlternateEmail.class);
    }

    /**
     * Delete an alternate email.
     * <p>
     * It mirrors to the following Smartsheet REST API method: DELETE /users/{userId}/alternateemails/{alternateEmailId}
     *
     * @param userId     the id of the user
     * @param altEmailId the alternate email id for the alternate email to retrieve.
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    public void deleteAlternateEmail(long userId, long altEmailId) throws SmartsheetException {
        this.deleteResource(USERS + "/" + userId + "/" + ALTERNATE_EMAILS + "/" + altEmailId, AlternateEmail.class);
    }

    /**
     * Promote and alternate email to primary.
     *
     * @param userId     id of the user
     * @param altEmailId alternate email id
     * @return alternateEmail of the primary
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         f there is any other error during the operation
     */
    public AlternateEmail promoteAlternateEmail(long userId, long altEmailId) throws SmartsheetException {

        HttpRequest request = createHttpRequest(smartsheet.getBaseURI().resolve(
                USERS + "/" + userId + "/" + ALTERNATE_EMAILS + "/" + altEmailId + "/makeprimary"), HttpMethod.POST);

        Object obj = null;
        try {
            HttpResponse response = this.smartsheet.getHttpClient().request(request);
            switch (response.getStatusCode()) {
                case 200:
                    obj = this.smartsheet.getJsonSerializer().deserializeResult(AlternateEmail.class,
                            response.getEntity().getContent());
                    break;
                default:
                    handleError(response);
            }
        } finally {
            smartsheet.getHttpClient().releaseConnection();
        }

        return (AlternateEmail) obj;
    }

    /**
     * Uploads a profile image for the specified user.
     *
     * @param userId   id of the user
     * @param file     path to the image file
     * @param fileType content type of the image file
     * @return user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         f there is any other error during the operation
     */
    public User addProfileImage(long userId, String file, String fileType) throws SmartsheetException, FileNotFoundException {
        return attachProfileImage(USERS + "/" + userId + "/profileimage", file, fileType);
    }

    private User attachProfileImage(String path, String file, String contentType) throws SmartsheetException, FileNotFoundException {
        Util.throwIfNull(file);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        Map<String, Object> parameters = new HashMap<>();
        path += QueryUtil.generateUrl(null, parameters);

        HttpRequest request = createHttpRequest(this.smartsheet.getBaseURI().resolve(path), HttpMethod.POST);
        String attachmentHeaderValue = "attachment; filename=\"" + URLEncoder.encode(file, StandardCharsets.UTF_8) + "\"";
        request.getHeaders().put("Content-Disposition", attachmentHeaderValue);

        File f = new File(file);
        InputStream is = new FileInputStream(f);

        HttpEntity entity = new HttpEntity();
        entity.setContentType(contentType);
        entity.setContent(is);
        entity.setContentLength(f.length());
        request.setEntity(entity);

        User obj = null;
        try {
            HttpResponse response = this.smartsheet.getHttpClient().request(request);
            switch (response.getStatusCode()) {
                case 200:
                    obj = this.smartsheet.getJsonSerializer().deserializeResult(User.class,
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

    @Override
    public User updateUser(User user) throws SmartsheetException {
        return this.updateResource(USERS + "/" + user.getId(), User.class, user);
    }

    @Override
    public void deleteUser(long userId, DeleteUserParameters parameters) throws SmartsheetException {
        String path = USERS + "/" + userId;

        if (parameters != null) {
            path += parameters.toQueryString();
        }

        this.deleteResource(path, User.class);
    }
}
