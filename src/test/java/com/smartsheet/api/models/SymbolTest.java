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

import com.smartsheet.api.models.enums.Symbol;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SymbolTest {

    @Test
    void test() {
        assertThat(Symbol.valueOf("FLAG")).isNotNull();
        assertThat(Symbol.valueOf("STAR")).isNotNull();
        assertThat(Symbol.valueOf("HARVEY_BALLS")).isNotNull();
        assertThat(Symbol.valueOf("RYG")).isNotNull();
        assertThat(Symbol.valueOf("PRIORITY")).isNotNull();
        assertThat(Symbol.valueOf("PRIORITY_HML")).isNotNull();
        assertThat(Symbol.valueOf("DECISION_SYMBOLS")).isNotNull();
        assertThat(Symbol.valueOf("DECISION_SHAPES")).isNotNull();
        assertThat(Symbol.valueOf("VCR")).isNotNull();
        assertThat(Symbol.valueOf("RYGB")).isNotNull();
        assertThat(Symbol.valueOf("RYGG")).isNotNull();
        assertThat(Symbol.valueOf("WEATHER")).isNotNull();
        assertThat(Symbol.valueOf("PROGRESS")).isNotNull();
        assertThat(Symbol.valueOf("ARROWS_3_WAY")).isNotNull();
        assertThat(Symbol.valueOf("ARROWS_4_WAY")).isNotNull();
        assertThat(Symbol.valueOf("ARROWS_5_WAY")).isNotNull();
        assertThat(Symbol.valueOf("DIRECTIONS_3_WAY")).isNotNull();
        assertThat(Symbol.valueOf("DIRECTIONS_4_WAY")).isNotNull();
        assertThat(Symbol.valueOf("SKI")).isNotNull();
        assertThat(Symbol.valueOf("SIGNAL")).isNotNull();
        assertThat(Symbol.valueOf("STAR_RATING")).isNotNull();
        assertThat(Symbol.valueOf("HEARTS")).isNotNull();
        assertThat(Symbol.valueOf("MONEY")).isNotNull();
        assertThat(Symbol.valueOf("EFFORT")).isNotNull();
        assertThat(Symbol.valueOf("PAIN")).isNotNull();

        assertThat(Symbol.values()).hasSize(25);
    }

}
