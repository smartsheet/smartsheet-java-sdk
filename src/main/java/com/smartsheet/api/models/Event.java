/*
 * Copyright (C) 2023 Smartsheet
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

package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.EventAction;
import com.smartsheet.api.models.enums.EventObjectType;
import com.smartsheet.api.models.enums.EventSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Event {

    /**
     * Name of the access token embedded in the request.
     */
    private String accessTokenName;

    /**
     * The action applied to the specified object
     */
    private EventAction action;

    /**
     * Container object for additional event-specific properties.
     */
    private Map<String, Object> additionalDetails;

    /**
     * Unique event identifier
     */
    private String eventId;

    /**
     * Date and time of the event
     */
    private Object eventTimestamp;

    /**
     * The identifier of the object impacted by the event
     */
    private Object objectId;

    /**
     * The Smartsheet resource impacted by the event
     */
    private EventObjectType objectType;

    /**
     * User whose authentication credential is embedded in the request that initiated the event.
     */
    private Long requestUserId;

    /**
     * Identifies the type of action that triggered the event
     */
    private EventSource source;

    /**
     * User assumed as the one who initiated the event.
     */
    private Long userId;

    /**
     * Sets an event timestamp
     *
     * @param eventTimestamp String if Date, Long if numericDate true on API call.
     */
    public void setEventTimestamp(Object eventTimestamp) {
        if (eventTimestamp instanceof String) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                this.eventTimestamp = sdf.parse(eventTimestamp.toString());
            } catch (Exception e) {
                // Empty Catch Block
            }
        } else {
            this.eventTimestamp = eventTimestamp;
        }
    }

}
