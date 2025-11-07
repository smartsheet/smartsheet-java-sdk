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

import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.enums.DowngradeSeatType;
import com.smartsheet.api.models.enums.UpgradeSeatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_PLAN_ID;
import static com.smartsheet.api.sdktest.users.CommonTestConstants.TEST_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUserUpgradeDowngrade {
    private static final UpgradeSeatType TEST_UPGRADE_SEAT_TYPE = UpgradeSeatType.MEMBER;
    private static final DowngradeSeatType TEST_DOWNGRADE_SEAT_TYPE = DowngradeSeatType.VIEWER;
    private static final String TEST_UPGRADE_BODY = "\"seatType\":\"" + TEST_UPGRADE_SEAT_TYPE.name() + "\"";
    private static final String TEST_DOWNGRADE_BODY = "\"seatType\":\"" + TEST_DOWNGRADE_SEAT_TYPE.name() + "\"";

    @Test
    void testUpgradeUserGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/upgrade-user/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.userResources().upgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_UPGRADE_SEAT_TYPE);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        String requestBody = wiremockRequest.getBodyAsString();

        assertThat(path).isEqualTo("/2.0/users/" + TEST_USER_ID + "/plans/" + TEST_PLAN_ID + "/upgrade");
        assertThat(wiremockRequest.getMethod().getName()).isEqualTo("POST");
        assertThat(requestBody).contains(TEST_UPGRADE_BODY);
    }

    @Test
    void testUpgradeUserAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/upgrade-user/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        Assertions.assertDoesNotThrow(() -> {
            smartsheet.userResources().upgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_UPGRADE_SEAT_TYPE);
        });

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        assertThat(requestBody).contains(TEST_UPGRADE_BODY);
    }

    @Test
    void testUpgradeUserNoSeatType() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/upgrade-user/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertDoesNotThrow(() -> {
            smartsheet.userResources().upgradeUser(TEST_USER_ID, TEST_PLAN_ID, null);
        });
    }

    @Test
    void testUpgradeUserError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().upgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_UPGRADE_SEAT_TYPE);
        });

        assertThat(exception.getMessage()).contains("Internal Server Error");
    }

    @Test
    void testUpgradeUserError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().upgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_UPGRADE_SEAT_TYPE);
        });

        assertThat(exception.getMessage()).contains("Malformed Request");
    }

    @Test
    void testDowngradeUserGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/downgrade-user/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.userResources().downgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_DOWNGRADE_SEAT_TYPE);
        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        String requestBody = wiremockRequest.getBodyAsString();

        assertThat(path).isEqualTo("/2.0/users/" + TEST_USER_ID + "/plans/" + TEST_PLAN_ID + "/downgrade");
        assertThat(wiremockRequest.getMethod().getName()).isEqualTo("POST");
        assertThat(requestBody).contains(TEST_DOWNGRADE_BODY);
    }

    @Test
    void testDowngradeUserAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/downgrade-user/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        Assertions.assertDoesNotThrow(() -> {
            smartsheet.userResources().downgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_DOWNGRADE_SEAT_TYPE);
        });

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String requestBody = wiremockRequest.getBodyAsString();
        assertThat(requestBody).contains(TEST_DOWNGRADE_BODY);
    }

    @Test
    void testDowngradeUserNoSeatType() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/users/downgrade-user/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            smartsheet.userResources().downgradeUser(TEST_USER_ID, TEST_PLAN_ID, null);
        });
    }

    @Test
    void testDowngradeUserError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().downgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_DOWNGRADE_SEAT_TYPE);
        });

        assertThat(exception.getMessage()).contains("Internal Server Error");
    }

    @Test
    void testDowngradeUserError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.userResources().downgradeUser(TEST_USER_ID, TEST_PLAN_ID, TEST_DOWNGRADE_SEAT_TYPE);
        });

        assertThat(exception.getMessage()).contains("Malformed Request");
    }
}
