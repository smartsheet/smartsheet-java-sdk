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

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClient;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.Event;
import com.smartsheet.api.models.EventResult;
import com.smartsheet.api.models.enums.EventAction;
import com.smartsheet.api.models.enums.EventObjectType;
import com.smartsheet.api.models.enums.EventSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TestListEvents {

    // ===========================================================================================
    // Expected result constants — values match the WireMock fixtures in smartsheet-sdk-tests
    // under mappings/events/list-events/
    // ===========================================================================================

    private static final List<Event> EXPECTED_EVENTS;
    private static final EventResult EXPECTED_NO_MORE_RESULT;
    private static final EventResult EXPECTED_WITH_MORE_RESULT;

    static {
        Event e0 = new Event()
                .setEventId("4f12345678901234")
                .setObjectId(1234567890123456L)
                .setObjectIdStr("1234567890123456")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.UPDATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(12345678L)
                .setRequestUserId(12345678L)
                .setEventTimestamp("2024-05-06T13:30:00Z")
                .setAdditionalDetails(Map.<String, Object>of("emailAddress", "test@test.com"));

        Event e1 = new Event()
                .setEventId("4f12345678901235")
                .setObjectId(9876543210987654L)
                .setObjectIdStr("9876543210987654")
                .setObjectType(EventObjectType.WORKSPACE)
                .setAction(EventAction.CREATE)
                .setSource(EventSource.API_UNDEFINED_APP)
                .setUserId(12345679L)
                .setRequestUserId(12345679L)
                .setEventTimestamp("2024-05-06T12:15:00Z")
                .setAdditionalDetails(Map.<String, Object>of("emailAddress", "test@test.com"));

        Event e2 = new Event()
                .setEventId("2.1.Y-oF8RMroSCo4WLS9p78QEz-LXxxxyyyjnXA2hFnCN_w")
                .setObjectId(3573510329814916L)
                .setObjectIdStr("3573510329814916")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.PURGE)
                .setSource(EventSource.UNKNOWN)
                .setUserId(12345678L)
                .setRequestUserId(12345678L)
                .setEventTimestamp("2024-05-06T12:09:33Z")
                .setAdditionalDetails(Map.<String, Object>of("emailAddress", "test@test.com"));

        Event e3 = new Event()
                .setEventId("2.1.SuwpcrfUcr75nP591Hce4_zQxxxyyy_0CDfvRRx6V0")
                .setObjectId(4230707048648580L)
                .setObjectIdStr("4230707048648580")
                .setObjectType(EventObjectType.ATTACHMENT)
                .setAction(EventAction.CREATE)
                .setSource(EventSource.UNKNOWN)
                .setUserId(12345678L)
                .setRequestUserId(12345678L)
                .setEventTimestamp("2024-05-06T13:30:00Z")
                .setAdditionalDetails(Map.<String, Object>of(
                        "attachmentName", "picture.jpg",
                        "emailAddress", "test@test.com",
                        "sheetId", "102030405"));

        Event e4 = new Event()
                .setEventId("2.1.ifR6WlBin9DQVYHDkQEx1D3EAxxxyyyXtcLWa9Oio")
                .setObjectId(8462951303303044L)
                .setObjectIdStr("8462951303303044")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.LOAD)
                .setSource(EventSource.API_INTEGRATED_APP)
                .setUserId(8737233684457348L)
                .setRequestUserId(8737233684457348L)
                .setEventTimestamp("2024-05-06T13:35:15Z")
                .setAdditionalDetails(Map.<String, Object>of("emailAddress", "test@test.com"));

        EXPECTED_EVENTS = List.of(e0, e1, e2, e3, e4);

        EXPECTED_NO_MORE_RESULT = buildEventResult(false, null, EXPECTED_EVENTS);
        EXPECTED_WITH_MORE_RESULT = buildEventResult(
                true,
                "2.1.Y-oF8RMroSCo4WLS9p78QEz-LXzzzzzzjnXA2hFnCN_w",
                EXPECTED_EVENTS);
    }

    private static EventResult buildEventResult(Boolean moreAvailable, String nextStreamPosition, List<Event> events) {
        EventResult result = new EventResult();
        result.setMoreAvailable(moreAvailable);
        result.setNextStreamPosition(nextStreamPosition);
        result.setData(events);
        return result;
    }

    // ===========================================================================================
    // Tests
    // ===========================================================================================

    /**
     * Verifies the SDK generates the correct URL path, HTTP method, and query parameters.
     * Does NOT assert request/response body.
     */
    @Test
    void testListEventsGeneratedUrlIsCorrect() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/events/list-events/required-response-body-properties-no-more-available",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, List<String>> actualQueryParams = wiremockRequest.getQueryParams().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValues()));

        assertThat(path).isEqualTo("/2.0/events");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(actualQueryParams).isEqualTo(Map.of(
                "since", List.of("2024-05-06T00:00:00Z"),
                "maxCount", List.of("5"),
                "numericDates", List.of("false")
        ));
    }

    /**
     * Verifies full deserialization of all response body properties, including objectIdStr,
     * additionalDetails, and varied objectType/action/source values across events.
     * Does NOT assert URL, method, or query parameters.
     */
    @Test
    void testListEventsAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/events/list-events/required-response-body-properties-no-more-available",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        EventResult result = smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("data.eventTimestamp")
                .isEqualTo(EXPECTED_NO_MORE_RESULT);
        result.getData().forEach(event -> assertThat(event.getEventTimestamp()).isNotNull());
    }

    /**
     * Verifies that moreAvailable=true and a non-empty nextStreamPosition are returned
     * correctly, enabling callers to paginate through the event stream.
     * Does NOT assert URL, method, or query parameters.
     */
    @Test
    void testListEventsMoreAvailableNavigation() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/events/list-events/required-response-body-properties-with-more-available",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        EventResult result = smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("data.eventTimestamp")
                .isEqualTo(EXPECTED_WITH_MORE_RESULT);
        result.getData().forEach(event -> assertThat(event.getEventTimestamp()).isNotNull());
    }

    /**
     * Verifies that a 4xx client error response is surfaced as a SmartsheetException.
     */
    @Test
    void testListEventsError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false)
        );

        assertThat(exception.getMessage()).contains("Malformed Request");
    }

    /**
     * Verifies that a 5xx server error response is surfaced as a SmartsheetException.
     */
    @Test
    void testListEventsError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false)
        );

        assertThat(exception.getMessage()).contains("Internal Server Error");
    }
}
