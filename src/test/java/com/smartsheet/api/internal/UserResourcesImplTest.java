package com.smartsheet.api.internal;

/*
 * #[license]
 * Smartsheet SDK for Java
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
import com.smartsheet.api.models.Account;
import com.smartsheet.api.models.DeleteUserParameters;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserProfile;
import com.smartsheet.api.models.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserResourcesImplTest extends ResourcesImplBase {

    private UserResourcesImpl userResources;

    @BeforeEach
    public void setUp() throws Exception {
        userResources = new UserResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListUsers() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listUsers.json"));
        Set<String> email = new HashSet<>();
        email.add("test@example.com");
        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        PagedResult<User> userWrapper1 = userResources.listUsers();
        assertThat(userWrapper1.getData()).hasSize(2);

        PagedResult<User> userWrapper = userResources.listUsers(email, pagination);
        assertThat(userWrapper.getPageNumber()).isEqualTo(1);
        assertThat(userWrapper.getPageSize()).isEqualTo(100);
        assertThat(userWrapper.getTotalCount()).isEqualTo(418);
        assertThat(userWrapper.getTotalPages()).isEqualTo(5);

        List<User> users = userWrapper.getData();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getId().longValue()).isEqualTo(242165701390534L);
        assertThat(users.get(0).getAdmin()).isFalse();
        assertThat(users.get(0).getEmail()).isEqualTo("test@smartsheet.com");
        assertThat(users.get(0). getName()).isEqualTo("John Doe");
        assertThat(users.get(0).getLicensedSheetCreator()).isTrue();
        assertThat(users.get(0).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void testAddUserUser() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/addUser.json"));

        User user = new User();
        user.setAdmin(true);
        user.setEmail("test@test.com");
        user.setFirstName("test425");
        user.setLastName("test425");
        user.setLicensedSheetCreator(true);
        User newUser = userResources.addUser(user);

        assertThat(newUser.getEmail()).isEqualTo("test@test.com");
        assertThat(newUser.getName()).isEqualTo("test425 test425");
        assertThat(newUser.getAdmin()).isFalse();
        assertThat(newUser.getLicensedSheetCreator()).isTrue();
        assertThat(newUser.getId().longValue()).isEqualTo(3210982882338692L);
    }

    @Test
    void testAddUserUserBoolean() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/addUser.json"));

        User user = new User();
        user.setAdmin(true);
        user.setEmail("test@test.com");
        user.setFirstName("test425");
        user.setLastName("test425");
        user.setLicensedSheetCreator(true);
        User newUser = userResources.addUser(user, false);

        assertThat(newUser.getEmail()).isEqualTo("test@test.com");
        assertThat(newUser.getName()).isEqualTo("test425 test425");
        assertThat(newUser.getAdmin()).isFalse();
        assertThat(newUser.getLicensedSheetCreator()).isTrue();
        assertThat(newUser.getId().longValue()).isEqualTo(3210982882338692L);
    }

    @Test
    void testGetUser() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getUser.json"));

        UserProfile user = userResources.getUser(12345L);
        assertThat(user.getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(user.getId().longValue()).isEqualTo(48569348493401200L);
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getLocale()).isEqualTo("en_US");
        assertThat(user.getTimeZone()).isEqualTo("US/Pacific");
    }

    @Test
    void testGetCurrentUser() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getCurrentUser.json"));

        UserProfile user = userResources.getCurrentUser();
        assertThat(user.getEmail()).isEqualTo("test@smartsheet.com");
        assertThat(user.getId().longValue()).isEqualTo(2222222222L);
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getLocale()).isEqualTo("en_US");
        assertThat(user.getTimeZone()).isEqualTo("US/Pacific");

        Account account = user.getAccount();
        assertThat(account.getName()).isEqualTo("Smartsheet");
        assertThat(account.getId().longValue()).isEqualTo(111111111111L);
    }

    @Test
    void testUpdateUser() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateUser.json"));

        User user = new User();
        user.setId(1234L);
        user.setAdmin(true);
        user.setLicensedSheetCreator(true);
        User updatedUser = userResources.updateUser(user);
        assertThat(updatedUser.getEmail()).isEqualTo("email@email.com");
        assertThat(updatedUser.getAdmin()).isFalse();
        assertThat(updatedUser.getLicensedSheetCreator()).isTrue();
        assertThat(updatedUser.getId().longValue()).isEqualTo(8166691168380804L);
        assertThat(updatedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void testDeleteUser() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/deleteUser.json"));
        DeleteUserParameters parameters = new DeleteUserParameters(12345L, true, true);
        userResources.deleteUser(1234L, parameters);
    }

    @Test
    void testListOrgSheets() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listOrgSheets.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        PagedResult<Sheet> sheets = userResources.listOrgSheets(pagination, null);
    }
}
