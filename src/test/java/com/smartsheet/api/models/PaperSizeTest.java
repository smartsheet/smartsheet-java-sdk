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

import com.smartsheet.api.models.enums.PaperSize;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaperSizeTest {

    @Test
    void test() {

        assertThat(PaperSize.valueOf("LETTER")).isNotNull();
        assertThat(PaperSize.valueOf("LEGAL")).isNotNull();
        assertThat(PaperSize.valueOf("WIDE")).isNotNull();
        assertThat(PaperSize.valueOf("ARCHD")).isNotNull();
        assertThat(PaperSize.valueOf("A4")).isNotNull();
        assertThat(PaperSize.valueOf("A3")).isNotNull();
        assertThat(PaperSize.valueOf("A2")).isNotNull();
        assertThat(PaperSize.valueOf("A1")).isNotNull();
        assertThat(PaperSize.valueOf("A0")).isNotNull();

        assertThat(PaperSize.values()).hasSize(9);
    }

}
