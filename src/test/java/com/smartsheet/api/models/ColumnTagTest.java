package com.smartsheet.api.models;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2014 Smartsheet
 * %%
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
 * %[license]
 */

import com.smartsheet.api.models.enums.ColumnTag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ColumnTagTest {

    @Test
    void test() {
        assertNotNull(ColumnTag.valueOf("CALENDAR_START_DATE"));
        assertNotNull(ColumnTag.valueOf("CALENDAR_END_DATE"));
        assertNotNull(ColumnTag.valueOf("GANTT_START_DATE"));
        assertNotNull(ColumnTag.valueOf("GANTT_END_DATE"));
        assertNotNull(ColumnTag.valueOf("GANTT_PERCENT_COMPLETE"));
        assertNotNull(ColumnTag.valueOf("GANTT_DISPLAY_LABEL"));
        assertNotNull(ColumnTag.valueOf("GANTT_PREDECESSOR"));
        assertNotNull(ColumnTag.valueOf("GANTT_DURATION"));
        assertNotNull(ColumnTag.valueOf("GANTT_ASSIGNED_RESOURCE"));
        assertNotNull(ColumnTag.valueOf("GANTT_DURATION"));
        assertNotNull(ColumnTag.valueOf("CARD_DONE"));

        assertEquals(11,ColumnTag.values().length);
    }

}

