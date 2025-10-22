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

package com.smartsheet.api.integrationtest;

import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserProfile;
import com.smartsheet.api.models.Account;
import com.smartsheet.api.models.UserPlan;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.SeatType;
import com.smartsheet.api.models.enums.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    User user;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testGetCurrentUser() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getCurrentUser();
        Account account = user.getAccount();
        assertThat(user).isNotNull();
    }

    private WiremockClientWrapper createWiremockSmartsheetClient(String testName, String requestId) {
        Map<String, String> headers = Map.of(
                "x-test-name", testName,
                "x-request-id", requestId
        );
        WiremockClient wiremockClient = new WiremockClient(headers);
        Smartsheet smartsheet = wiremockClient.getSmartsheetClient("test_token_123");
        return new WiremockClientWrapper(smartsheet, wiremockClient);
    }

    @Test
    void testListUserPlansGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/list-user-plans/all-response-body-properties", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        long userId = 12345678L;
        String lastKey = "abcDefGhIjKlMnOpQrStUvWxYz";
        long maxItems = 100L;

        smartsheet.userResources().listUserPlans(userId, lastKey, maxItems);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);

        String path = URI.create(wiremockRequest.getUrl()).getPath();
        String queryParams = URI.create(wiremockRequest.getAbsoluteUrl()).getQuery();
        assertThat(path).isEqualTo("/2.0/users/12345678/plans");
        assertThat(queryParams).isEqualTo("maxItems=100&lastKey=abcDefGhIjKlMnOpQrStUvWxYz");
    }

    @Test
    void testListUserPlansAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/list-user-plans/all-response-body-properties", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        long userId = 12345678L;
        String lastKey = "abcDefGhIjKlMnOpQrStUvWxYz";
        long maxItems = 100L;

        TokenPaginatedResult<UserPlan> response = smartsheet.userResources()
                .listUserPlans(userId, lastKey, maxItems);

        assertThat(response).isNotNull();
        assertThat(response.getLastKey()).isEqualTo("12345678901234569");
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(1234567890123456L);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.MEMBER);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo("2025-01-01T00:00:00.123456789Z");
        assertThat(response.getData().get(0).getIsInternal()).isFalse();
    }

    @Test
    void testListUserPlansRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/list-user-plans/required-response-body-properties", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        long userId = 12345678L;

        TokenPaginatedResult<UserPlan> response = smartsheet.userResources()
                .listUserPlans(userId, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(1234567890123456L);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.MEMBER);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isNull();
        assertThat(response.getData().get(0).getIsInternal()).isFalse();
    }

    @Test
    void testListUserPlansError500Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/error-500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        long userId = 1234567890123456L;

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources()
                    .listUserPlans(userId, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testListUserPlansError400Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/error-400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        long userId = 1234567890123456L;

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources()
                    .listUserPlans(userId, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }

    @Test
    void testListUsersGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/list-users-for-plan/required-response-body-properties", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        long planId = 1234567890123456L;

        smartsheet.userResources().listUsers(null, planId, null, null);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);

        String path = URI.create(wiremockRequest.getUrl()).getPath();
        String queryParams = URI.create(wiremockRequest.getAbsoluteUrl()).getQuery();
        assertThat(path).isEqualTo("/2.0/users");
        assertThat(queryParams).isEqualTo("planId=" + planId);
    }

    @Test
    void testListUsersAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient(
                "/users/list-users-for-plan/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        long planId = 1234567890123456L;

        PagedResult<User> response = smartsheet.userResources()
                .listUsers(null, planId, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.MEMBER);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo("2025-06-14T09:55:30Z");
        assertThat(response.getData().get(0).getIsInternal()).isEqualTo(true);
        assertThat(response.getData().get(0).getName()).isEqualTo("Test User");
        assertThat(response.getData().get(0).getEmail()).isEqualTo("test.user@smartsheet.com");
        assertThat(response.getData().get(0).getFirstName()).isEqualTo("Test");
        assertThat(response.getData().get(0).getLastName()).isEqualTo("User");
        assertThat(response.getData().get(0).getAdmin()).isEqualTo(true);
        assertThat(response.getData().get(0).getLicensedSheetCreator()).isEqualTo(true);
        assertThat(response.getData().get(0).getResourceViewer()).isEqualTo(true);
        assertThat(response.getData().get(0).getGroupAdmin()).isEqualTo(true);
        assertThat(response.getData().get(0).getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(response.getData().get(0).getSheetCount()).isEqualTo(-1);
        assertThat(response.getData().get(0).getLastLogin()).isEqualTo("2020-10-04T18:32:47Z");
        assertThat(response.getData().get(0).getCustomWelcomeScreenViewed()).isEqualTo("2020-08-25T12:15:47Z");
        assertThat(response.getData().get(0).getId()).isEqualTo(1234567890123456L);
    }

    @Test
    void testListUsersRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient(
                "/users/list-users-for-plan/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        long planId = 1234567890123456L;

        PagedResult<User> response = smartsheet.userResources()
                .listUsers(null, planId, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.MEMBER);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isNull();
        assertThat(response.getData().get(0).getIsInternal()).isEqualTo(true);
        assertThat(response.getData().get(0).getName()).isEqualTo("Test User");
        assertThat(response.getData().get(0).getEmail()).isEqualTo("test.user@smartsheet.com");
        assertThat(response.getData().get(0).getFirstName()).isEqualTo("Test");
        assertThat(response.getData().get(0).getLastName()).isEqualTo("User");
        assertThat(response.getData().get(0).getAdmin()).isEqualTo(true);
        assertThat(response.getData().get(0).getLicensedSheetCreator()).isEqualTo(true);
        assertThat(response.getData().get(0).getResourceViewer()).isEqualTo(true);
        assertThat(response.getData().get(0).getGroupAdmin()).isEqualTo(true);
        assertThat(response.getData().get(0).getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(response.getData().get(0).getSheetCount()).isEqualTo(-1);
        assertThat(response.getData().get(0).getId()).isEqualTo(1234567890123456L);
    }

    @Test
    void testListUsersError500Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/error-500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().listUsers(null, null, SeatType.MEMBER, null);
        });

        assertThat(exception.getMessage()).contains("Internal Server Error");
    }

    @Test
    void testListUsersError400Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/error-400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().listUsers(null, null, SeatType.MEMBER, null);
        });

        assertThat(exception.getMessage()).contains("Malformed Request");
    }

    @Test
    void testGetUser() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getUser(smartsheet.userResources().getCurrentUser().getId());
        assertThat(user).isNotNull();
    }

    @Test
    void testListUsers() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

        PagedResult<User> userWrapper = smartsheet
                .userResources()
                .listUsers(new HashSet(Arrays.asList("aditi.nioding@gmail.com")), parameters);
        List<User> users = userWrapper.getData();

        //assertTrue(users.size() > 0);
    }

    @Test
    void testAddProfileImage() throws SmartsheetException, IOException {
        UserProfile me = smartsheet.userResources().getCurrentUser();
        assertThat(me).isNotNull();
        smartsheet.userResources().addProfileImage(me.getId(), "src/test/resources/exclam.png", "image/png");
        me = smartsheet.userResources().getCurrentUser();
        assertThat(me.getProfileImage()).isNotNull();
        final Long squareProfileImageSize = 1050L;
        assertThat(me.getProfileImage().getWidth()).isEqualTo(squareProfileImageSize);
        assertThat(me.getProfileImage().getHeight()).isEqualTo(squareProfileImageSize);
    }

    @Test
    //not executed in test due to low permission
    public void testAddUser() throws IOException, SmartsheetException {
        // User user = new User.AddUserBuilder()
        //     .setAdmin(false)
        //     .setEmail("aditi.nioding@gmail.com")
        //     .setFirstName("Aditi").setLastName("N")
        //     .setLicensedSheetCreator(true)
        //     .build();
        // User newUser = smartsheet.userResources().addUser(user);
        // String name = newUser.getFirstName();
        // assertTrue(name.equals("Aditi"));
        // testUpdateUser(newUser.getId());
    }

    //not executed in test due to low permission
    public void testUpdateUser(long userId) throws SmartsheetException, IOException {
        // User user = new User.UpdateUserBuilder()
        //     .setAdmin(true)
        //     .setUserId(userId)
        //     .setFirstName("Adi")
        //     .setLicensedSheetCreator(true)
        //     .build();
        // User updatedUser = smartsheet.userResources().updateUser(user);
        // assertThat(updatedUser).isNotNull();
    }

    @Test
    void testListOrgSheets() throws SmartsheetException, IOException {
        //PagedResult<Sheet> sheets = smartsheet.userResources().listOrgSheets();
        //not executed in test due to low permission
        //assertThat(sheets).isNotNull();
    }

    @Test
    //not executed in test due to low permission
    public void testDeleteUser() throws IOException, SmartsheetException {
        // User user = new User.AddUserBuilder()
        //     .setAdmin(false).setEmail("test@test.com")
        //     .setFirstName("Aditi")
        //     .setLastName("N")
        //     .setLicensedSheetCreator(true)
        //     .build();
        // User newUser = smartsheet.userResources().addUser(user);
        // Long toId = newUser.getId();
        //
        // DeleteUserParameters parameters = new DeleteUserParameters(toId, true, true);
    }
}
