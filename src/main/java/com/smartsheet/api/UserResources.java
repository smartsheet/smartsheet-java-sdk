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

package com.smartsheet.api;

import com.smartsheet.api.models.AlternateEmail;
import com.smartsheet.api.models.DeleteUserParameters;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserProfile;
import com.smartsheet.api.models.enums.ListUserInclusion;
import com.smartsheet.api.models.enums.UserInclusion;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * <p>This interface provides methods to access User resources.</p>
 *
 * <p>Thread Safety: Implementation of this interface must be thread safe.</p>
 */
public interface UserResources {

    /**
     * <p>List all users.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users</p>
     *
     * @return the list of all users
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    PagedResult<User> listUsers() throws SmartsheetException;

    /**
     * <p>List all users.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users</p>
     *
     * @param email      the list of email addresses
     * @param pagination object containing pagination query parameters
     * @return the list of all users
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    PagedResult<User> listUsers(Set<String> email, PaginationParameters pagination) throws SmartsheetException;

    /**
     * <p>List all users.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users</p>
     *
     * @param email      the list of email addresses
     * @param includes   elements to include in response
     * @param pagination object containing pagination query parameters
     * @return the list of all users
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    PagedResult<User> listUsers(Set<String> email, EnumSet<ListUserInclusion> includes,
                                PaginationParameters pagination) throws SmartsheetException;

    /**
     * <p>Add a user to the organization, without sending email.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /users</p>
     *
     * @param user the user object
     * @return the user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    User addUser(User user) throws SmartsheetException;

    /**
     * <p>Add a user to the organization, without sending email.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /users</p>
     *
     * @param user      the user
     * @param sendEmail the send email flag
     * @return the user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    User addUser(User user, boolean sendEmail) throws SmartsheetException;

    /**
     * <p>Get the current user.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users/{userId}</p>
     *
     * @param userId the user id
     * @return the current user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    UserProfile getUser(long userId) throws SmartsheetException;

    /**
     * <p>Get the current user.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /user/me</p>
     *
     * @return the current user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    UserProfile getCurrentUser() throws SmartsheetException;

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
    UserProfile getCurrentUser(EnumSet<UserInclusion> includes) throws SmartsheetException;

    /**
     * <p>Update a user.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: PUT /user/{id}</p>
     *
     * @param user the user to update
     * @return the updated user
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    User updateUser(User user) throws SmartsheetException;

    /**
     * <p>Delete a user in the organization.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: DELETE /user/{id}</p>
     *
     * @param id         the id of the user
     * @param parameters the object containing parameters for deleting users
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    void deleteUser(long id, DeleteUserParameters parameters) throws SmartsheetException;

    /**
     * <p>List all organisation sheets.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users/sheets</p>
     *
     * @param pagination    the object containing the pagination query parameters
     * @param modifiedSince restrict to sheets modified on or after this date
     * @return the list of all organisation sheets
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    PagedResult<Sheet> listOrgSheets(PaginationParameters pagination, Date modifiedSince) throws SmartsheetException;

    /**
     * <p>List all organisation sheets.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users/sheets</p>
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
    PagedResult<Sheet> listOrgSheets(PaginationParameters pagination) throws SmartsheetException;

    /**
     * <p>List all user alternate email(s).</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users/{userId}/alternateemails</p>
     *
     * @param userId     the userID
     * @param pagination the pagination parameters
     * @return the list of all user alternate email(s)
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    PagedResult<AlternateEmail> listAlternateEmails(long userId, PaginationParameters pagination) throws SmartsheetException;

    /**
     * <p>Get alternate email.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /users/{userId}/alternateemails/{alternateEmailId}</p>
     *
     * @param userId     the id of the user
     * @param altEmailId the alternate email id for the alternate email to retrieve.
     * @return the resource. Note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null.
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    AlternateEmail getAlternateEmail(long userId, long altEmailId) throws SmartsheetException;

    /**
     * <p>Add an alternate email.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /users/{userId}/alternateemails</p>
     *
     * @param userId    the id of the user
     * @param altEmails List of alternate email address to add.
     * @return List of added alternate email(s).
     * @throws IllegalArgumentException    if any argument is null or empty string
     * @throws InvalidRequestException     if there is any problem with the REST API request
     * @throws AuthorizationException      if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException   if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException         if there is any other error during the operation
     */
    List<AlternateEmail> addAlternateEmail(long userId, List<AlternateEmail> altEmails) throws SmartsheetException;

    /**
     * <p>Delete an alternate email.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: DELETE /users/{userId}/alternateemails/{alternateEmailId}</p>
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
    void deleteAlternateEmail(long userId, long altEmailId) throws SmartsheetException;

    /**
     * <p>Promote and alternate email to primary.</p>
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
    AlternateEmail promoteAlternateEmail(long userId, long altEmailId) throws SmartsheetException;

    /**
     * <p>Uploads a profile image for the specified user.</p>
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
    User addProfileImage(long userId, String file, String fileType) throws SmartsheetException, FileNotFoundException;
}
