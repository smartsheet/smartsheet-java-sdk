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

import com.smartsheet.api.models.enums.ColumnType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnTypeTest {

    @Test
    void test() {

        assertThat(ColumnType.valueOf("TEXT_NUMBER")).isNotNull();
        assertThat(ColumnType.valueOf("PICKLIST")).isNotNull();
        assertThat(ColumnType.valueOf("DATE")).isNotNull();
        assertThat(ColumnType.valueOf("DATETIME")).isNotNull();
        assertThat(ColumnType.valueOf("CONTACT_LIST")).isNotNull();
        assertThat(ColumnType.valueOf("CHECKBOX")).isNotNull();
        assertThat(ColumnType.valueOf("DURATION")).isNotNull();
        assertThat(ColumnType.valueOf("PREDECESSOR")).isNotNull();
        assertThat(ColumnType.valueOf("ABSTRACT_DATETIME")).isNotNull();
        assertThat(ColumnType.valueOf("MULTI_CONTACT_LIST")).isNotNull();
        assertThat(ColumnType.valueOf("MULTI_PICKLIST")).isNotNull();

        assertThat(ColumnType.values()).hasSize(11);
    }

}
