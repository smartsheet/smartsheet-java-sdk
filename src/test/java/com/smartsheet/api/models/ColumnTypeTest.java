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

import com.smartsheet.api.models.enums.ColumnType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ColumnTypeTest {

    @Test
    void test() {

        assertNotNull(ColumnType.valueOf("TEXT_NUMBER"));
        assertNotNull(ColumnType.valueOf("PICKLIST"));
        assertNotNull(ColumnType.valueOf("DATE"));
        assertNotNull(ColumnType.valueOf("DATETIME"));
        assertNotNull(ColumnType.valueOf("CONTACT_LIST"));
        assertNotNull(ColumnType.valueOf("CHECKBOX"));
        assertNotNull(ColumnType.valueOf("DURATION"));
        assertNotNull(ColumnType.valueOf("PREDECESSOR"));
        assertNotNull(ColumnType.valueOf("ABSTRACT_DATETIME"));
        assertNotNull(ColumnType.valueOf("MULTI_CONTACT_LIST"));
        assertNotNull(ColumnType.valueOf("MULTI_PICKLIST"));

        assertEquals(11,ColumnType.values().length);
    }

}
