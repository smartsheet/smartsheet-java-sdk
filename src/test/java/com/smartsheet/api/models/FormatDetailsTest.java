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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FormatDetailsTest {
    @Nested
    class BuilderTests {
        @Test
        void formatDetailsBuilder() {
            // Act
            FormatDetails formatDetailsNoArg = FormatDetails.builder().build();
            formatDetailsNoArg.setPaperSize(PaperSize.A4);

            FormatDetails formatDetailsAllArg = FormatDetails.builder()
                    .paperSize(PaperSize.A4)
                    .build();

            // Assert
            assertThat(formatDetailsNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(formatDetailsAllArg);
        }
    }
}
