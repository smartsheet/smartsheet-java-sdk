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
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.UserProfile;
import com.smartsheet.api.models.Account;
import com.smartsheet.api.models.DeleteUserParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.AlternateEmail;
import com.smartsheet.api.models.enums.ListUserInclusion;
import com.smartsheet.api.models.enums.UserInclusion;
import com.smartsheet.api.models.enums.UserStatus;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        EnumSet<ListUserInclusion> includes = EnumSet.of(ListUserInclusion.LAST_LOGIN);

        PagedResult<User> userWrapper1 = userResources.listUsers();
        assertThat(userWrapper1.getData()).hasSize(2);

        PagedResult<User> userWrapper = userResources.listUsers(email, pagination);
        assertThat(userWrapper.getPageNumber()).isEqualTo(1);
        assertThat(userWrapper.getPageSize()).isEqualTo(100);
        assertThat(userWrapper.getTotalCount()).isEqualTo(418);
        assertThat(userWrapper.getTotalPages()).isEqualTo(5);

        PagedResult<User> userWrapper2 = userResources.listUsers(pagination);
        assertThat(userWrapper2.getPageNumber()).isEqualTo(1);
        assertThat(userWrapper2.getPageSize()).isEqualTo(100);
        assertThat(userWrapper2.getTotalCount()).isEqualTo(418);
        assertThat(userWrapper2.getTotalPages()).isEqualTo(5);

        PagedResult<User> userWrapper3 = userResources.listUsers(email, includes, pagination);
        Calendar expectedLoginCal = Calendar.getInstance();
        expectedLoginCal.set(2020, Calendar.AUGUST, 25, 12, 15, 47);
        expectedLoginCal.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar lastLoginCal = Calendar.getInstance();
        lastLoginCal.setTime(userWrapper3.getData().get(0).getLastLogin());
        lastLoginCal.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertThat(expectedLoginCal.get(Calendar.YEAR)).isEqualTo(lastLoginCal.get(Calendar.YEAR));
        assertThat(expectedLoginCal.get(Calendar.MONTH)).isEqualTo(lastLoginCal.get(Calendar.MONTH));
        assertThat(expectedLoginCal.get(Calendar.DAY_OF_MONTH)).isEqualTo(lastLoginCal.get(Calendar.DAY_OF_MONTH));
        assertThat(expectedLoginCal.get(Calendar.HOUR)).isEqualTo(lastLoginCal.get(Calendar.HOUR));
        assertThat(expectedLoginCal.get(Calendar.MINUTE)).isEqualTo(lastLoginCal.get(Calendar.MINUTE));
        assertThat(expectedLoginCal.get(Calendar.SECOND)).isEqualTo(lastLoginCal.get(Calendar.SECOND));

        List<User> users = userWrapper.getData();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getId().longValue()).isEqualTo(242165701390534L);
        assertThat(users.get(0).getAdmin()).isFalse();
        assertThat(users.get(0).getEmail()).isEqualTo("test@smartsheet.com");
        assertThat(users.get(0).getName()).isEqualTo("John Doe");
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
    void testGetCurrentUser_withIncludesExcludes() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getCurrentUserIncludesGroups.json"));

        EnumSet<UserInclusion> includes = EnumSet.of(UserInclusion.GROUPS);
        UserProfile user = userResources.getCurrentUser(includes);
        assertThat(user.getEmail()).isEqualTo("test@smartsheet.com");
        assertThat(user.getId().longValue()).isEqualTo(2222222222L);
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getLocale()).isEqualTo("en_US");
        assertThat(user.getTimeZone()).isEqualTo("US/Pacific");

        Account account = user.getAccount();
        assertThat(account.getName()).isEqualTo("Smartsheet");
        assertThat(account.getId().longValue()).isEqualTo(111111111111L);

        // has groups
        assertThat(user.getGroups()).hasSize(1);
        assertThat(user.getGroups().get(0).getId()).isEqualTo(456456L);
        assertThat(user.getGroups().get(0).getOwner()).isEqualTo("jane.doe@smartsheet.com");
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
    void testDeleteUser() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteUser.json"));
        DeleteUserParameters parameters = new DeleteUserParameters(12345L, true, true);
        assertThatCode(() -> userResources.deleteUser(1234L, parameters)).doesNotThrowAnyException();
    }

    @Test
    void testListOrgSheets() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listOrgSheets.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        PagedResult<Sheet> sheets = userResources.listOrgSheets(pagination, null);
        assertThat(sheets.getData()).isNotNull().isNotEmpty();
        assertThat(sheets.getData()).hasSize(1);
        assertThat(sheets.getData().get(0).getId()).isEqualTo(2894323533539204L);
        assertThat(sheets.getData().get(0).getOwner()).isEqualTo("john.doe@smartsheet.com");
    }

    @Test
    void testListAlternateEmails() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listAlternateEmails.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        PagedResult<AlternateEmail> alternateEmailPagedResult = userResources.listAlternateEmails(1234L, pagination);
        assertThat(alternateEmailPagedResult.getData()).isNotNull().isNotEmpty();
        assertThat(alternateEmailPagedResult.getData()).hasSize(2);
        assertThat(alternateEmailPagedResult.getData().get(0).getId()).isEqualTo(2894323533539204L);
        assertThat(alternateEmailPagedResult.getData().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        // since this is a Boolean, this is null if not set
        assertThat(alternateEmailPagedResult.getData().get(0).getConfirmed()).isNull();

        assertThat(alternateEmailPagedResult.getData().get(1).getId()).isEqualTo(787878L);
        assertThat(alternateEmailPagedResult.getData().get(1).getEmail()).isEqualTo("jane.doe@smartsheet.com");
        assertThat(alternateEmailPagedResult.getData().get(1).getConfirmed()).isFalse();
    }

    @Test
    void testGetAlternateEmail() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getAlternateEmail.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        AlternateEmail alternateEmail = userResources.getAlternateEmail(1234L, 7845112L);
        assertThat(alternateEmail).isNotNull();
        assertThat(alternateEmail.getId()).isEqualTo(2894323533539204L);
        assertThat(alternateEmail.getEmail()).isEqualTo("john.doe@smartsheet.com");
        // since this is a Boolean, this is null if not set
        assertThat(alternateEmail.getConfirmed()).isNull();
    }

    @Test
    void testAddAlternateEmail() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/addAlternateEmail.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        AlternateEmail alternateEmail = new AlternateEmail();
        alternateEmail.setEmail("foo.bar@smartsheet.com");

        List<AlternateEmail> alternateEmailList = Lists.newArrayList(alternateEmail);

        List<AlternateEmail> alternateEmails = userResources.addAlternateEmail(1234L, alternateEmailList);
        assertThat(alternateEmails).isNotNull();
        assertThat(alternateEmails).hasSize(2);
    }

    @Test
    void testAddAlternateEmail_AlternateEmailNull() throws IOException {
        server.setResponseBody(new File("src/test/resources/addAlternateEmail.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        assertThatThrownBy(() -> userResources.addAlternateEmail(1234L, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAddAlternateEmail_NoEmailSupplied() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/addAlternateEmail.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        List<AlternateEmail> alternateEmailList = Lists.newArrayList();

        List<AlternateEmail> alternateEmails = userResources.addAlternateEmail(1234L, alternateEmailList);
        assertThat(alternateEmails).isNotNull();
        assertThat(alternateEmails).hasSize(0);
    }

    @Test
    void testDeleteAlternateEmail() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteUser.json"));
        assertThatCode(() -> userResources.deleteAlternateEmail(1234L, 7878L)).doesNotThrowAnyException();
    }

    @Disabled("Not working with return type")
    @Test
    void testPromoteAlternateEmail() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/promoteAlternateEmail.json"));

        PaginationParameters pagination = new PaginationParameters();
        pagination.setIncludeAll(true);
        pagination.setPageSize(1);
        pagination.setPage(1);

        AlternateEmail alternateEmail = userResources.promoteAlternateEmail(1234L, 7878L);
        assertThat(alternateEmail).isNotNull();
    }

    @Test
    void testAddProfileImage_ImageNull() {
        assertThatThrownBy(() -> {
            userResources.addProfileImage(1234L, null, "application/octet-stream");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
