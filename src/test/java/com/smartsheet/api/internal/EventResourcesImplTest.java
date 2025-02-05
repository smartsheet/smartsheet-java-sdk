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

package com.smartsheet.api.internal;

import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.EventResult;
import com.smartsheet.api.models.enums.EventObjectType;
import com.smartsheet.api.models.enums.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventResourcesImplTest extends ResourcesImplBase {

    private EventResourcesImpl eventResources;

    @BeforeEach
    public void setUp() throws Exception {
        eventResources = new EventResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListEvents_sinceExists_happyPath() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/listEvents.json"));
        String since = "2023-01-30T11:42:30Z";
        Integer maxCount = 5;
        Boolean numericDates = true;

        EventResult eventResult = eventResources.listEvents(since, null, maxCount, numericDates);

        assertThat(eventResult).isNotNull();
        assertThat(eventResult.getData()).isNotNull();
        assertThat(eventResult.getMoreAvailable()).isFalse();
        assertThat(eventResult.getNextStreamPosition()).isNotBlank();
        assertThat(eventResult.getData().size()).isEqualTo(2);

        assertThat(eventResult.getData().get(0).getEventId()).isNotBlank();
        assertThat(eventResult.getData().get(0).getUserId()).isNotNull();
        assertThat(eventResult.getData().get(0).getRequestUserId()).isNotNull();
        assertThat(eventResult.getData().get(0).getAccessTokenName()).isNotBlank();
        assertThat(eventResult.getData().get(0).getEventTimestamp()).isNotNull();
        assertThat(eventResult.getData().get(0).getSource().name()).isEqualTo(EventSource.WEB_APP.name());
        assertThat(eventResult.getData().get(0).getObjectType().name()).isEqualTo(EventObjectType.ACCESS_TOKEN.name());

        assertThat(eventResult.getData().get(1).getEventId()).isNotBlank();
        assertThat(eventResult.getData().get(1).getUserId()).isNotNull();
        assertThat(eventResult.getData().get(1).getRequestUserId()).isNotNull();
        assertThat(eventResult.getData().get(1).getAccessTokenName()).isNotBlank();
        assertThat(eventResult.getData().get(1).getEventTimestamp()).isNotNull();
        assertThat(eventResult.getData().get(1).getSource().name()).isEqualTo(EventSource.WEB_APP.name());
        assertThat(eventResult.getData().get(1).getObjectType().name()).isEqualTo(EventObjectType.ACCESS_TOKEN.name());
    }

    @Test
    void testListEvents_streamPosExists_happyPath() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/listEvents.json"));
        String streamPos = "2023-01-30T11:42:30Z";
        Integer maxCount = 5;
        Boolean numericDates = true;

        EventResult eventResult = eventResources.listEvents(null, streamPos, maxCount, numericDates);

        assertThat(eventResult).isNotNull();
        assertThat(eventResult.getData()).isNotNull();
        assertThat(eventResult.getMoreAvailable()).isFalse();
        assertThat(eventResult.getNextStreamPosition()).isNotBlank();
        assertThat(eventResult.getData().size()).isEqualTo(2);

        assertThat(eventResult.getData().get(0).getEventId()).isNotBlank();
        assertThat(eventResult.getData().get(0).getUserId()).isNotNull();
        assertThat(eventResult.getData().get(0).getRequestUserId()).isNotNull();
        assertThat(eventResult.getData().get(0).getAccessTokenName()).isNotBlank();
        assertThat(eventResult.getData().get(0).getEventTimestamp()).isNotNull();
        assertThat(eventResult.getData().get(0).getSource().name()).isEqualTo(EventSource.WEB_APP.name());
        assertThat(eventResult.getData().get(0).getObjectType().name()).isEqualTo(EventObjectType.ACCESS_TOKEN.name());

        assertThat(eventResult.getData().get(1).getEventId()).isNotBlank();
        assertThat(eventResult.getData().get(1).getUserId()).isNotNull();
        assertThat(eventResult.getData().get(1).getRequestUserId()).isNotNull();
        assertThat(eventResult.getData().get(1).getAccessTokenName()).isNotBlank();
        assertThat(eventResult.getData().get(1).getEventTimestamp()).isNotNull();
        assertThat(eventResult.getData().get(1).getSource().name()).isEqualTo(EventSource.WEB_APP.name());
        assertThat(eventResult.getData().get(1).getObjectType().name()).isEqualTo(EventObjectType.ACCESS_TOKEN.name());
    }

    @Test
    void testListEvents_exception() {
        String since = "2023-01-30T11:42:30Z";
        Integer maxCount = 5;
        Boolean numericDates = true;

        server.setStatus(400);
        server.setResponseBody("{\"errorCode\":1032,\"message\":\"Something went wrong\"}");
        assertThatThrownBy(() -> {
            eventResources.listEvents(since, null, maxCount, numericDates);
        })
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Something went wrong");
    }

}
