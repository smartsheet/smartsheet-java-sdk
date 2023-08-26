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

package com.smartsheet.api;

import com.smartsheet.api.models.Contact;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;

public interface ContactResources {

    /**
     * <p>Gets the specified Contact.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /contacts/{contactId}</p>
     *
     * @param contactId the ID of the contact
     * @return the contact object
     * @throws SmartsheetException if there is any other error during the operation
     */
    Contact getContact(String contactId) throws SmartsheetException;

    /**
     * <p>Gets a list of the user’s Smartsheet Contacts.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: GET /contacts</p>
     *
     * @param parameters the pagination parameters
     * @return the contacts as a paged list
     * @throws SmartsheetException if there is any other error during the operation
     */
    PagedResult<Contact> listContacts(PaginationParameters parameters) throws SmartsheetException;
}
