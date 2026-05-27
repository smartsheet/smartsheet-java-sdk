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

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.WiremockClientWrapper;
import com.smartsheet.api.models.EventResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestListEvents {

    @Test
    void testListEventsNoMoreAvailable() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/events/list-events/required-response-body-properties-no-more-available",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        EventResult result = smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        assertThat(result).isNotNull();
        assertThat(result.getMoreAvailable()).isFalse();
        assertThat(result.getData()).hasSize(5);

        // Verify objectIdStr is deserialized correctly
        assertThat(result.getData().get(0).getObjectIdStr()).isEqualTo("1234567890123456");
        assertThat(result.getData().get(1).getObjectIdStr()).isEqualTo("9876543210987654");

        // Verify other fields on the first event
        assertThat(result.getData().get(0).getEventId()).isEqualTo("4f12345678901234");
        assertThat(result.getData().get(0).getObjectType().name()).isEqualTo("SHEET");
        assertThat(result.getData().get(0).getAction().name()).isEqualTo("UPDATE");
        assertThat(result.getData().get(0).getSource().name()).isEqualTo("WEB_APP");
        assertThat(result.getData().get(0).getUserId()).isEqualTo(12345678L);
        assertThat(result.getData().get(0).getRequestUserId()).isEqualTo(12345678L);
    }

    @Test
    void testListEventsWithMoreAvailable() throws SmartsheetException {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient(
                "/events/list-events/required-response-body-properties-with-more-available",
                requestId
        );
        Smartsheet smartsheet = wrapper.getSmartsheet();

        EventResult result = smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);

        assertThat(result).isNotNull();
        assertThat(result.getMoreAvailable()).isTrue();
        assertThat(result.getNextStreamPosition()).isNotBlank();
        assertThat(result.getData()).hasSize(5);

        // Verify objectIdStr is deserialized correctly
        assertThat(result.getData().get(0).getObjectIdStr()).isEqualTo("1234567890123456");
        assertThat(result.getData().get(1).getObjectIdStr()).isEqualTo("9876543210987654");
    }

    @Test
    void testListEvents404Error() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/404-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () ->
                smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false)
        );

        assertThat(exception.getMessage()).contains("Not Found");
    }

    @Test
    void testListEventsError400Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/400-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);
        });

        assertThat(exception.getMessage()).contains("Malformed Request");
    }

    @Test
    void testListEventsError500Response() {
        String requestId = UUID.randomUUID().toString();
        WiremockClientWrapper wrapper = Utils.createWiremockSmartsheetClient("/errors/500-response", requestId);
        Smartsheet smartsheet = wrapper.getSmartsheet();

        SmartsheetException exception = Assertions.assertThrows(SmartsheetException.class, () -> {
            smartsheet.eventResources().listEvents("2024-05-06T00:00:00Z", null, 5, false);
        });

        assertThat(exception.getMessage()).contains("Internal Server Error");
    }
}
