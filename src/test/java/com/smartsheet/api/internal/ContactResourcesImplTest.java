package com.smartsheet.api.internal;

/*
 * #[license]
 * Smartsheet Java SDK
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Contact;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactResourcesImplTest extends ResourcesImplBase {
    private ContactResourcesImpl contactResources;

    @BeforeEach
    public void setUp() throws Exception {
        contactResources = new ContactResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetContact() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getContact.json"));
        Contact contact = contactResources.getContact("xyz");
        assertEquals("David Davidson", contact.getName());
    }

    @Test
    void testListContacts() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listContacts.json"));
        PagedResult<Contact> contacts = contactResources.listContacts(new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build());
        assertEquals("David Davidson", contacts.getData().get(0).getName());
        assertTrue(contacts.getTotalCount() == 2);
    }
}
