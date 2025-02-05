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

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Contact;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ContactResourcesImplTest extends ResourcesImplBase {
    private ContactResourcesImpl contactResources;

    @BeforeEach
    public void setUp() throws Exception {
        contactResources = new ContactResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Nested
    class GetContactTests {
        @Test
        void getContact() throws SmartsheetException, IOException {
            // Arrange
            server.setResponseBody(new File("src/test/resources/getContact.json"));

            // Act
            Contact contact = contactResources.getContact("xyz");

            // Assert
            assertThat(contact.getName()).isEqualTo("David Davidson");
        }
    }

    @Nested
    class ListContactsTests {
        @Test
        void listContacts_withParameters() throws SmartsheetException, IOException {
            // Arrange
            server.setResponseBody(new File("src/test/resources/listContacts.json"));
            PaginationParameters paginationParameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

            // Act
            PagedResult<Contact> contacts = contactResources.listContacts(paginationParameters);

            // Assert
            assertThat(contacts.getData().get(0).getName()).isEqualTo("David Davidson");
            assertThat(contacts.getTotalCount()).isEqualTo(2);
        }

        @Test
        void listContacts_nullParameters() throws SmartsheetException, IOException {
            // Arrange
            server.setResponseBody(new File("src/test/resources/listContacts.json"));

            // Act
            PagedResult<Contact> contacts = contactResources.listContacts(null);

            // Assert
            assertThat(contacts.getData().get(0).getName()).isEqualTo("David Davidson");
            assertThat(contacts.getTotalCount()).isEqualTo(2);
        }
    }
}
