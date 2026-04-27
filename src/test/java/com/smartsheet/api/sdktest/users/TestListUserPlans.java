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
import com.smartsheet.api.models.TokenPaginatedResult;
import com.smartsheet.api.models.UserPlan;
import com.smartsheet.api.models.enums.SeatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_PLAN_ID;
import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestListUserPlans {
    private static final String TEST_LAST_KEY = "12345678901234569";
    private static final long TEST_MAX_ITEMS = 100L;
    private static final SeatType TEST_SEAT_TYPE = SeatType.MEMBER;
    private static final String TEST_SEAT_TYPE_LAST_CHANGED_AT = "2025-01-01T00:00:00.123456789Z";
    private static final String TEST_PROVISIONAL_EXPIRATION_DATE = "2026-12-13T12:17:52.525696Z";

    @Test
    void testListUserPlansGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/list-user-plans/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.userResources().listUserPlans(TEST_USER_ID, TEST_LAST_KEY, TEST_MAX_ITEMS);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/users/1234567890/plans");
        assertThat(receivedQueryParams.get("maxItems").getValues()).isEqualTo(List.of(Long.toString(TEST_MAX_ITEMS)));
        assertThat(receivedQueryParams.get("lastKey").getValues()).isEqualTo(List.of(TEST_LAST_KEY));
    }

    @Test
    void testListUserPlansAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/list-user-plans/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        TokenPaginatedResult<UserPlan> response = smartsheet.userResources()
                .listUserPlans(TEST_USER_ID, TEST_LAST_KEY, TEST_MAX_ITEMS);

        assertThat(response).isNotNull();
        assertThat(response.getLastKey()).isEqualTo(TEST_LAST_KEY);
        assertThat(response.getData()).hasSize(2);

        // Verify first plan (MEMBER)
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(TEST_PLAN_ID);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(TEST_SEAT_TYPE);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isEqualTo(TEST_SEAT_TYPE_LAST_CHANGED_AT);
        assertThat(response.getData().get(0).getProvisionalExpirationDate()).isEqualTo(TEST_PROVISIONAL_EXPIRATION_DATE);
        assertThat(response.getData().get(0).getIsInternal()).isFalse();

        // Verify second plan (CONTRIBUTOR)
        assertThat(response.getData().get(1).getSeatType()).isEqualTo(SeatType.CONTRIBUTOR);
        assertThat(response.getData().get(1).getSeatTypeLastChangedAt()).isEqualTo(TEST_SEAT_TYPE_LAST_CHANGED_AT);
        assertThat(response.getData().get(1).getProvisionalExpirationDate()).isEqualTo(TEST_PROVISIONAL_EXPIRATION_DATE);
        assertThat(response.getData().get(1).getIsInternal()).isFalse();
    }

    @Test
    void testListUserPlansRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/list-user-plans/required-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        TokenPaginatedResult<UserPlan> response = smartsheet.userResources()
                .listUserPlans(TEST_USER_ID, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getData().get(0).getPlanId()).isEqualTo(TEST_PLAN_ID);
        assertThat(response.getData().get(0).getSeatType()).isEqualTo(TEST_SEAT_TYPE);
        assertThat(response.getData().get(0).getSeatTypeLastChangedAt()).isNull();
        assertThat(response.getData().get(0).getProvisionalExpirationDate()).isNull();
        assertThat(response.getData().get(0).getIsInternal()).isFalse();
    }

    @Test
    void testListUserPlansError500Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources()
                    .listUserPlans(TEST_USER_ID, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    void testListUserPlansError400Response() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources()
                    .listUserPlans(TEST_USER_ID, null, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Malformed Request");
    }

}
