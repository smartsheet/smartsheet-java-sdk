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

package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.DayDescriptor;
import com.smartsheet.api.models.enums.DayOrdinal;
import com.smartsheet.api.models.enums.ScheduleType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleTest {
    @Nested
    class BuilderTests {
        @Test
        void scheduleBuilder() {
            // Arrange
            Date startAt = new Date();
            Date endAt = new Date();
            List<DayDescriptor> dayDescriptors = List.of(DayDescriptor.MONDAY, DayDescriptor.WEDNESDAY);
            Date lastSentAt = new Date();
            Date nextSendAt = new Date();

            // Act
            Schedule scheduleNoArg = Schedule.builder().build();
            scheduleNoArg.setType(ScheduleType.WEEKLY);
            scheduleNoArg.setStartAt(startAt);
            scheduleNoArg.setEndAt(endAt);
            scheduleNoArg.setDayOfMonth(15);
            scheduleNoArg.setDayOrdinal(DayOrdinal.FIRST);
            scheduleNoArg.setDayDescriptors(dayDescriptors);
            scheduleNoArg.setRepeatEvery(2);
            scheduleNoArg.setLastSentAt(lastSentAt);
            scheduleNoArg.setNextSendAt(nextSendAt);

            Schedule scheduleAllArg = Schedule.builder()
                    .type(ScheduleType.WEEKLY)
                    .startAt(startAt)
                    .endAt(endAt)
                    .dayOfMonth(15)
                    .dayOrdinal(DayOrdinal.FIRST)
                    .dayDescriptors(dayDescriptors)
                    .repeatEvery(2)
                    .lastSentAt(lastSentAt)
                    .nextSendAt(nextSendAt)
                    .build();

            // Assert
            assertThat(scheduleNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(scheduleAllArg);
        }
    }
}
