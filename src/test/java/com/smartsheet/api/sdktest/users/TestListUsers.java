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

package com.smartsheet.api.sdktest.users;

import com.github.tomakehurst.wiremock.http.QueryParameter;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.enums.SeatType;
import com.smartsheet.api.models.enums.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_PLAN_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestListUsers {
    private WiremockClientWrapper createWiremockSmartsheetClient(String testName, String requestId) {
        Map<String, String> headers = Map.of(
                "x-test-name", testName,
                "x-request-id", requestId
        );
        WiremockClient wiremockClient = new WiremockClient(headers);
        Smartsheet smartsheet = wiremockClient.getSmartsheetClient("test_token_123");
        return new WiremockClientWrapper(smartsheet, wiremockClient);
    }

    private static final Set<String> TEST_EMAILS = Set.of("test.user@smartsheet.com");
    private static final SeatType TEST_SEAT_TYPE = SeatType.MEMBER;
    private static final int TEST_PAGE = 1;
    private static final int TEST_PAGE_SIZE = 100;
    private static final boolean TEST_INCLUDE_ALL = false;
    private static final String TEST_FIRST_NAME = "Test";
    private static final String TEST_LAST_NAME = "User";
    private static final String TEST_NAME = "Test User";
    private static final boolean TEST_ADMIN = true;
    private static final boolean TEST_LICENSED_SHEET_CREATOR = true;
    private static final boolean TEST_RESOURCE_VIEWER = true;
    private static final boolean TEST_GROUP_ADMIN = true;
    private static final UserStatus TEST_STATUS = UserStatus.ACTIVE;
    private static final int TEST_SHEET_COUNT = -1;
    private static final String TEST_SEAT_TYPE_LAST_CHANGED_AT = "2025-06-14T09:55:30Z";
    private static final String TEST_PROVISIONAL_EXPIRATION_DATE = "2026-12-13T12:17:52.525696Z";
    private static final boolean TEST_IS_INTERNAL_TRUE = true;
    private static final String TEST_LAST_LOGIN = "2020-10-04T18:32:47Z";
    private static final String TEST_CUSTOM_WELCOME_SCREEN_VIEWED = "2020-08-25T12:15:47Z";

    @Test
    void testListUsersGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/list-users/required-response-body-properties", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        PaginationParameters pagination = new PaginationParameters()
                .setPage(TEST_PAGE)
                .setPageSize(TEST_PAGE_SIZE)
                .setIncludeAll(TEST_INCLUDE_ALL);

        smartsheet.userResources().listUsers(TEST_EMAILS, TEST_PLAN_ID, TEST_SEAT_TYPE, pagination);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/users");
        assertThat(receivedQueryParams.get("seatType").getValues()).isEqualTo(List.of(TEST_SEAT_TYPE.toString()));
        assertThat(receivedQueryParams.get("page").getValues()).isEqualTo(List.of(Integer.toString(TEST_PAGE)));
        assertThat(receivedQueryParams.get("pageSize").getValues()).isEqualTo(List.of(Integer.toString(TEST_PAGE_SIZE)));
        assertThat(receivedQueryParams.get("includeAll")).isNull();
        assertThat(receivedQueryParams.get("email").getValues().size()).isEqualTo(TEST_EMAILS.size());
        assertThat(receivedQueryParams.get("email").getValues().containsAll(TEST_EMAILS)).isEqualTo(true);
    }

    @Test
    void testListUsersAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient(
                "/users/list-users/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        PagedResult<User> response = smartsheet.userResources()
                .listUsers(null, TEST_PLAN_ID, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(TEST_SEAT_TYPE);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo(TEST_SEAT_TYPE_LAST_CHANGED_AT);
        assertThat(response.getData().get(0).getProvisionalExpirationDate()).isEqualTo(TEST_PROVISIONAL_EXPIRATION_DATE);
        assertThat(response.getData().get(0).getIsInternal()).isEqualTo(TEST_IS_INTERNAL_TRUE);
        assertThat(response.getData().get(0).getName()).isEqualTo(TEST_NAME);
        assertThat(response.getData().get(0).getEmail()).isEqualTo(TEST_EMAILS.iterator().next());
        assertThat(response.getData().get(0).getFirstName()).isEqualTo(TEST_FIRST_NAME);
        assertThat(response.getData().get(0).getLastName()).isEqualTo(TEST_LAST_NAME);
        assertThat(response.getData().get(0).getAdmin()).isEqualTo(TEST_ADMIN);
        assertThat(response.getData().get(0).getLicensedSheetCreator()).isEqualTo(TEST_LICENSED_SHEET_CREATOR);
        assertThat(response.getData().get(0).getResourceViewer()).isEqualTo(TEST_RESOURCE_VIEWER);
        assertThat(response.getData().get(0).getGroupAdmin()).isEqualTo(TEST_GROUP_ADMIN);
        assertThat(response.getData().get(0).getStatus()).isEqualTo(TEST_STATUS);
        assertThat(response.getData().get(0).getSheetCount()).isEqualTo(TEST_SHEET_COUNT);
        assertThat(response.getData().get(0).getLastLogin()).isEqualTo(TEST_LAST_LOGIN);
        assertThat(response.getData().get(0).getCustomWelcomeScreenViewed()).isEqualTo(TEST_CUSTOM_WELCOME_SCREEN_VIEWED);
        assertThat(response.getData().get(0).getId()).isEqualTo(TEST_PLAN_ID);
    }

    @Test
    void testListUsersRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient(
                "/users/list-users/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        PagedResult<User> response = smartsheet.userResources()
                .listUsers(null, TEST_PLAN_ID, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(TEST_SEAT_TYPE);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isNull();
        assertThat(response.getData().get(0).getIsInternal()).isEqualTo(TEST_IS_INTERNAL_TRUE);
        assertThat(response.getData().get(0).getName()).isEqualTo(TEST_NAME);
        assertThat(response.getData().get(0).getEmail()).isEqualTo(TEST_EMAILS.iterator().next());
        assertThat(response.getData().get(0).getFirstName()).isEqualTo(TEST_FIRST_NAME);
        assertThat(response.getData().get(0).getLastName()).isEqualTo(TEST_LAST_NAME);
        assertThat(response.getData().get(0).getAdmin()).isEqualTo(TEST_ADMIN);
        assertThat(response.getData().get(0).getLicensedSheetCreator()).isEqualTo(TEST_LICENSED_SHEET_CREATOR);
        assertThat(response.getData().get(0).getResourceViewer()).isEqualTo(TEST_RESOURCE_VIEWER);
        assertThat(response.getData().get(0).getGroupAdmin()).isEqualTo(TEST_GROUP_ADMIN);
        assertThat(response.getData().get(0).getStatus()).isEqualTo(TEST_STATUS);
        assertThat(response.getData().get(0).getSheetCount()).isEqualTo(TEST_SHEET_COUNT);
        assertThat(response.getData().get(0).getId()).isEqualTo(TEST_PLAN_ID);
    }

    @Test
    void testListUsersError500Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().listUsers(null, null, TEST_SEAT_TYPE, null);
        });

        assertThat(exception.getMessage()).contains("Internal Server Error");
    }

    @Test
    void testListUsersError400Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().listUsers(null, null, TEST_SEAT_TYPE, null);
        });

        assertThat(exception.getMessage()).contains("Malformed Request");
    }
}
