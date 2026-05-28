/*
 * Copyright (C) 2026 Smartsheet
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

import static org.assertj.core.api.Assertions.assertThat;

public class TestListEvents {

    // ===========================================================================================
    // Expected result constants — values MUST match the corresponding WireMock fixtures in
    // smartsheet-sdk-tests under mappings/events/list-events/
    // ===========================================================================================

    /** Shared events for both required-response-body-properties fixture variants. */
    private static final List<Event> REQUIRED_EVENTS;

    /** Full EventResult for the no-more-available required-properties fixture. */
    private static final EventResult EXPECTED_REQUIRED_NO_MORE_RESULT;

    /** Full EventResult for the with-more-available required-properties fixture. */
    private static final EventResult EXPECTED_REQUIRED_WITH_MORE_RESULT;

    /** Full EventResult for the all-response-body-properties fixture (all optional fields set). */
    private static final EventResult EXPECTED_ALL_PROPERTIES_RESULT;

    static {
        // --- Events shared between the two required-properties fixtures ---
        // These contain only the core (non-optional) event fields.
        // Events 0-1 carry objectIdStr; events 2-4 do not, verifying null handling.
        Event r0 = new Event()
                .setEventId("4f12345678901234")
                .setObjectId(1234567890123456L)
                .setObjectIdStr("1234567890123456")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.UPDATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(12345678L)
                .setRequestUserId(12345678L)
                .setEventTimestamp("2024-05-06T10:00:00Z");

        Event r1 = new Event()
                .setEventId("5e23456789012345")
                .setObjectId(9876543210987654L)
                .setObjectIdStr("9876543210987654")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.CREATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(23456789L)
                .setRequestUserId(23456789L)
                .setEventTimestamp("2024-05-06T10:01:00Z");

        Event r2 = new Event()
                .setEventId("6d34567890123456")
                .setObjectId(1111111111111111L)
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.DELETE)
                .setSource(EventSource.WEB_APP)
                .setUserId(34567890L)
                .setRequestUserId(34567890L)
                .setEventTimestamp("2024-05-06T10:02:00Z");

        Event r3 = new Event()
                .setEventId("7c45678901234567")
                .setObjectId(2222222222222222L)
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.UPDATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(45678901L)
                .setRequestUserId(45678901L)
                .setEventTimestamp("2024-05-06T10:03:00Z");

        Event r4 = new Event()
                .setEventId("8b56789012345678")
                .setObjectId(3333333333333333L)
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.CREATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(56789012L)
                .setRequestUserId(56789012L)
                .setEventTimestamp("2024-05-06T10:04:00Z");

        REQUIRED_EVENTS = List.of(r0, r1, r2, r3, r4);

        EXPECTED_REQUIRED_NO_MORE_RESULT = buildEventResult(false, "required-stream-pos-12345", REQUIRED_EVENTS);
        EXPECTED_REQUIRED_WITH_MORE_RESULT = buildEventResult(true, "next-stream-pos-12345", REQUIRED_EVENTS);

        // --- Events for the all-response-body-properties fixture ---
        // Same core data as above, plus all optional fields (accessTokenName, additionalDetails).
        Event a0 = new Event()
                .setEventId("4f12345678901234")
                .setObjectId(1234567890123456L)
                .setObjectIdStr("1234567890123456")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.UPDATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(12345678L)
                .setRequestUserId(12345678L)
                .setEventTimestamp("2024-05-06T10:00:00Z")
                .setAccessTokenName("test-token-1")
                .setAdditionalDetails(Map.<String, Object>of("sheetName", "My Test Sheet 1"));

        Event a1 = new Event()
                .setEventId("5e23456789012345")
                .setObjectId(9876543210987654L)
                .setObjectIdStr("9876543210987654")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.CREATE)
                .setSource(EventSource.MOBILE_IOS)
                .setUserId(23456789L)
                .setRequestUserId(23456789L)
                .setEventTimestamp("2024-05-06T10:01:00Z")
                .setAccessTokenName("test-token-2")
                .setAdditionalDetails(Map.<String, Object>of("sheetName", "My Test Sheet 2"));

        Event a2 = new Event()
                .setEventId("6d34567890123456")
                .setObjectId(1111111111111111L)
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.DELETE)
                .setSource(EventSource.API_INTEGRATED_APP)
                .setUserId(34567890L)
                .setRequestUserId(34567890L)
                .setEventTimestamp("2024-05-06T10:02:00Z")
                .setAccessTokenName("test-token-3")
                .setAdditionalDetails(Map.<String, Object>of("sheetName", "My Test Sheet 3"));

        Event a3 = new Event()
                .setEventId("7c45678901234567")
                .setObjectId(2222222222222222L)
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.UPDATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(45678901L)
                .setRequestUserId(45678901L)
                .setEventTimestamp("2024-05-06T10:03:00Z")
                .setAccessTokenName("test-token-4")
                .setAdditionalDetails(Map.<String, Object>of("sheetName", "My Test Sheet 4"));

        Event a4 = new Event()
                .setEventId("8b56789012345678")
                .setObjectId(3333333333333333L)
                .setObjectIdStr("3333333333333333")
                .setObjectType(EventObjectType.SHEET)
                .setAction(EventAction.CREATE)
                .setSource(EventSource.WEB_APP)
                .setUserId(56789012L)
                .setRequestUserId(56789012L)
                .setEventTimestamp("2024-05-06T10:04:00Z")
                .setAccessTokenName("test-token-5")
                .setAdditionalDetails(Map.<String, Object>of("sheetName", "My Test Sheet 5"));

        EXPECTED_ALL_PROPERTIES_RESULT = buildEventResult(false, "all-stream-pos-12345", List.of(a0, a1, a2, a3, a4));
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
                "/events/list-events/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();
        WiremockClient wiremockClient = wrapper.getWiremockClient();

        smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        LoggedRequest wiremockRequest = wiremockClient.findWiremockRequest(requestId);
        String path = URI.create(wiremockRequest.getUrl()).getPath();
        Map<String, QueryParameter> receivedQueryParams = wiremockRequest.getQueryParams();

        assertThat(path).isEqualTo("/2.0/events");
        assertThat(wiremockRequest.getMethod()).isEqualTo(RequestMethod.GET);
        assertThat(receivedQueryParams.get("since").getValues()).isEqualTo(List.of("2024-05-06T00:00:00Z"));
        assertThat(receivedQueryParams.get("maxCount").getValues()).isEqualTo(List.of("5"));
        assertThat(receivedQueryParams.get("numericDates").getValues()).isEqualTo(List.of("false"));
        assertThat(receivedQueryParams).doesNotContainKey("streamPosition");
    }

    /**
     * Verifies full deserialization of a response where all optional fields are present.
     * Does NOT assert URL, method, or query parameters.
     */
    @Test
    void testListEventsAllResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/events/list-events/all-response-body-properties",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        EventResult result = smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_ALL_PROPERTIES_RESULT);
    }

    /**
     * Verifies deserialization of a minimal response (required fields only) when no more
     * events are available (moreAvailable = false).
     * Does NOT assert URL, method, or query parameters.
     */
    @Test
    void testListEventsRequiredResponseBodyProperties() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/events/list-events/required-response-body-properties-no-more-available",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        EventResult result = smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_NO_MORE_RESULT);
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

        assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED_REQUIRED_WITH_MORE_RESULT);
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
