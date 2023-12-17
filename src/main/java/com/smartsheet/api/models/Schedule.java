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

import com.smartsheet.api.models.enums.DayDescriptor;
import com.smartsheet.api.models.enums.DayOrdinal;
import com.smartsheet.api.models.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Schedule {

    /**
     * Schedule type
     */
    private ScheduleType type;

    /**
     * The date, time and time zone at which the first delivery will start.
     */
    private Date startAt;

    /**
     * The date, time and time zone at which the delivery schedule will end.
     */
    private Date endAt;

    /**
     * The day within the month
     */
    private Integer dayOfMonth;

    /**
     * The day ordinal
     */
    private DayOrdinal dayOrdinal;

    /**
     * An array of day descriptors
     */
    private List<DayDescriptor> dayDescriptors;

    /**
     * Frequency on which the request will be delivered
     */
    private Integer repeatEvery;

    /**
     * The date and time for when the last request was sent.
     */
    private Date lastSentAt;

    /**
     * The date and time for when the next request is scheduled to send.
     */
    private Date nextSendAt;
}
