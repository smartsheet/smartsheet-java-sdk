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
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserProfile;
import com.smartsheet.api.models.Account;
import com.smartsheet.api.models.UserPlan;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.SeatType;
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

    @Test
    void testListUserPlansGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();

        Map<String, String> headers = Map.of(
                "smartsheet-integration-source", "AI,SampleOrg,My-AI-Connector-v2",
                "x-test-name", "/users/list-user-plans-all-properties",
                "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        long userId = 12345678L;
        String lastKey = "abcDefGhIjKlMnOpQrStUvWxYz";
        long maxItems = 100L;
        String expectedPath = "/2.0/users/12345678/plans";

        smartsheet.userResources().listUserPlans(userId, lastKey, maxItems);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);

        String path = URI.create(wiremockRequest.getUrl()).getPath();

        assertThat(path).isEqualTo(expectedPath);
    }

    @Test
    void testListUserPlansAllProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
            "smartsheet-integration-source", "AI,SampleOrg,My-AI-Connector-v2",
            "x-test-name", "/users/list-user-plans-all-properties",
            "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        long userId = 12345678L;
        String lastKey = "abcDefGhIjKlMnOpQrStUvWxYz";
        long maxItems = 100L;
        long expectedPlanId = 1234567890123456L;
        String expectedLastKey = "12345678901234569";

        TokenPaginatedResult<UserPlan> response = smartsheet.userResources()
                .listUserPlans(userId, lastKey, maxItems);

        assertThat(response).isNotNull();
        assertThat(response.getLastKey()).isEqualTo(expectedLastKey);
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(expectedPlanId);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.MEMBER);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo("2025-01-01T00:00:00.123456789Z");
        assertThat(response.getData().get(0).getIsInternal()).isFalse();
    }

    @Test
    void testListUserPlansRequiredProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
                "smartsheet-integration-source", "AI,SampleOrg,My-AI-Connector-v2",
                "x-test-name", "/users/list-user-plans-required-properties",
                "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        long userId = 12345678L;
        String expectedLastKey = "12345678901234569";
        long expectedPlanId = 1234567890123456L;
        String expectedDate = "2025-01-01T00:00:00.123456789Z";

        TokenPaginatedResult<UserPlan> response = smartsheet.userResources()
                .listUserPlans(userId, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getLastKey()).isEqualTo(expectedLastKey);
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(expectedPlanId);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.MEMBER);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo(expectedDate);
        assertThat(response.getData().get(0).getIsInternal()).isFalse();
    }

    @Test
    void testListUserPlansErrorResponse() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
                "smartsheet-integration-source", "AI,SampleOrg,My-AI-Connector-v2",
                "x-test-name", "/users/list-user-plans/error-response",
                "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        long userId = 1234567890123456L;

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources()
                    .listUserPlans(userId, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Your Access Token is invalid.");
    }

    @Test
    void testListUsersForPlanGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
                "x-test-name", "/users/list-users-for-plan-by-planId",
                "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        long planId = 1234567890123456L;
        String expectedPath = "/2.0/users";

        smartsheet.userResources().listUsers(null, planId, null, null);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);

        String path = URI.create(wiremockRequest.getUrl()).getPath();
        assertThat(path).isEqualTo(expectedPath);
    }

    @Test
    void testListUsersForPlanByPlanId() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
                "x-test-name", "/users/list-users-for-plan-by-planId",
                "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        long planId = 1234567890123456L;
        String expectedDate = "2025-10-13T12:17:52.525696Z";

        PagedResult<User> response = smartsheet.userResources()
                .listUsers(null, planId, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(planId);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.GUEST);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo(expectedDate);
        assertThat(response.getData().get(0).getIsInternal()).isFalse();
    }

    @Test
    void testListUsersForPlanBySeatType() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
                "x-test-name", "/users/list-users-for-plan-by-seatType",
                "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        PagedResult<User> response = smartsheet.userResources()
                .listUsers(null, null, SeatType.MEMBER, null);

        long expectedPlanId = 1234567890123456L;
        String expectedDate = "2025-10-13T12:17:52.525696Z";

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(expectedPlanId);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(SeatType.MEMBER);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo(expectedDate);
        assertThat(response.getData().get(0).getIsInternal()).isFalse();
    }

    @Test
    void testListUsersForPlanErrorResponse() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
                "x-test-name", "/users/list-users-for-plan/error-response",
                "x-request-id", requestId
        );

        WiremockClient wiremockClient = new WiremockClient(headers);

        Smartsheet smartsheet = wiremockClient.getSmartsheetClient(
                "test_token_123"
        );

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources()
                    .listUsers(null, null, SeatType.MEMBER, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Your Access Token is invalid.");
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
