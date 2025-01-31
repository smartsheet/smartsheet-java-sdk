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

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.Event;
import com.smartsheet.api.models.EventResult;
import com.smartsheet.api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class EventResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    User user;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Disabled
    // TODO - need access token with Admin Level permissions for this test to work
    @Test
    void testListEvents() throws SmartsheetException {

        Date lastHour = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        EventResult eventResult = smartsheet.eventResources().listEvents(lastHour, null, 10, false);
        assertThat(eventResult.getData()).hasSizeLessThanOrEqualTo(10);
        for (Event event : eventResult.getData()) {
            assertThat(event.getObjectType()).isNotNull();
            assertThat(event.getAction()).isNotNull();
            assertThat(event.getObjectId()).isNotNull();
            assertThat(event.getEventId()).isNotNull();
            assertThat(event.getEventTimestamp()).isInstanceOf(Date.class);
            assertThat(event.getUserId()).isNotNull();
            assertThat(event.getRequestUserId()).isNotNull();
            assertThat(event.getSource()).isNotNull();
        }

        while (eventResult.getMoreAvailable()) {
            eventResult = smartsheet.eventResources().listEvents(null, eventResult.getNextStreamPosition(), 10, true);
            assertThat(eventResult.getData())
                    .isNotEmpty()
                    .hasSizeLessThanOrEqualTo(10);

            for (Event event : eventResult.getData()) {
                assertThat(event.getObjectType()).isNotNull();
                assertThat(event.getAction()).isNotNull();
                assertThat(event.getObjectId()).isNotNull();
                assertThat(event.getEventId()).isNotNull();
                assertThat(event.getEventTimestamp()).isInstanceOf(Long.class);
                assertThat(event.getUserId()).isNotNull();
                assertThat(event.getRequestUserId()).isNotNull();
                assertThat(event.getSource()).isNotNull();
            }
        }
    }

    @Test
    void testInvalidParams() {
        try {
            EventResult eventResult = smartsheet
                    .eventResources()
                    .listEvents(0, "2.1.0An4ZapaQaOXPdojlmediSZ1WqMdi5U_3l9gViOW7ic", 10, null);
        } catch (SmartsheetException e) {
            // Shouldn't be empty!
        }
        try {
            EventResult eventResult = smartsheet
                    .eventResources()
                    .listEvents(new Date(), null, 10, true);
        } catch (SmartsheetException e) {
            // Shouldn't be empty!
        }
    }
}
