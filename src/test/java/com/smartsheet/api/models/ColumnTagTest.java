package com.smartsheet.api.models;

/*
 * Smartsheet SDK for Java
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

import com.smartsheet.api.models.enums.ColumnTag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnTagTest {

    @Test
    void test() {
        assertThat(ColumnTag.valueOf("CALENDAR_START_DATE")).isNotNull();
        assertThat(ColumnTag.valueOf("CALENDAR_END_DATE")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_START_DATE")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_END_DATE")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_PERCENT_COMPLETE")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_DISPLAY_LABEL")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_PREDECESSOR")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_DURATION")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_ASSIGNED_RESOURCE")).isNotNull();
        assertThat(ColumnTag.valueOf("GANTT_DURATION")).isNotNull();
        assertThat(ColumnTag.valueOf("CARD_DONE")).isNotNull();

        assertThat(ColumnTag.values()).hasSize(11);
    }

}

