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

package com.smartsheet.api.sdktest;

import com.github.tomakehurst.wiremock.http.QueryParameter;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserPlan;
import com.smartsheet.api.models.enums.SeatType;
import com.smartsheet.api.models.enums.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class WiremockTest {
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
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/users/12345678/plans");
        assertThat(receivedQueryParams.get("maxItems").getValues()).isEqualTo(List.of(Long.toString(maxItems)));
        assertThat(receivedQueryParams.get("lastKey").getValues()).isEqualTo(List.of(lastKey));
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
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient(
                "/users/list-user-plans/required-response-body-properties",
                requestId
        );
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
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/errors/500-response", requestId);
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
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        long userId = 1234567890123456L;

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources()
                    .listUserPlans(userId, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }

    @Test
    void testListUsersGeneratedUrlIsCorrectIncludeAllTrue() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/list-users/required-response-body-properties", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        Set<String> emails = Set.of("test.user@smartsheet.com");
        long planId = 1234567890123456L;
        SeatType seatType = SeatType.MEMBER;
        boolean includeAll = true;

        PaginationParameters pagination = new PaginationParameters()
                .setIncludeAll(includeAll);

        smartsheet.userResources().listUsers(emails, planId, seatType, pagination);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/users");
        assertThat(receivedQueryParams.get("seatType").getValues()).isEqualTo(List.of(seatType.toString()));
        assertThat(receivedQueryParams.get("page")).isNull();
        assertThat(receivedQueryParams.get("pageSize")).isNull();
        assertThat(receivedQueryParams.get("includeAll").getValues()).isEqualTo(List.of(Boolean.toString(includeAll)));
        assertThat(receivedQueryParams.get("email").getValues().size()).isEqualTo(emails.size());
        assertThat(receivedQueryParams.get("email").getValues().containsAll(emails)).isEqualTo(true);
    }

    @Test
    void testListUsersGeneratedUrlIsCorrectIncludeAllFalse() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/users/list-users/required-response-body-properties", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        Set<String> emails = Set.of("test.user@smartsheet.com");
        long planId = 1234567890123456L;
        SeatType seatType = SeatType.MEMBER;
        int page = 1;
        int pageSize = 100;
        boolean includeAll = false;

        PaginationParameters pagination = new PaginationParameters()
                .setPage(page)
                .setPageSize(pageSize)
                .setIncludeAll(includeAll);

        smartsheet.userResources().listUsers(emails, planId, seatType, pagination);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/users");
        assertThat(receivedQueryParams.get("seatType").getValues()).isEqualTo(List.of(seatType.toString()));
        assertThat(receivedQueryParams.get("page").getValues()).isEqualTo(List.of(Integer.toString(page)));
        assertThat(receivedQueryParams.get("pageSize").getValues()).isEqualTo(List.of(Integer.toString(pageSize)));
        assertThat(receivedQueryParams.get("includeAll")).isNull();
        assertThat(receivedQueryParams.get("email").getValues().size()).isEqualTo(emails.size());
        assertThat(receivedQueryParams.get("email").getValues().containsAll(emails)).isEqualTo(true);
    }

    @Test
    void testListUsersAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient(
                "/users/list-users/all-response-body-properties",
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
                "/users/list-users/required-response-body-properties",
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
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().listUsers(null, null, SeatType.MEMBER, null);
        });

        assertThat(exception.getMessage()).contains("Internal Server Error");
    }

    @Test
    void testListUsersError400Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().listUsers(null, null, SeatType.MEMBER, null);
        });

        assertThat(exception.getMessage()).contains("Malformed Request");
    }
}
