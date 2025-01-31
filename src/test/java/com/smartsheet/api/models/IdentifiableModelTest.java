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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifiableModelTest {

    @Test
    void testHashCode() {
        Row row = new Row();
        row.setId(1234L);
        Row row1 = new Row();
        row1.setId(1234L);
        assertThat(row)
                // Same id in two different objects
                .isEqualTo(row1)
                // Different Objects
                .isNotEqualTo(new Object());
    }

    @Test
    void testEqualsObject() {
        Row row = new Row();
        assertThat(row.hashCode()).isNotNull();
    }

}
