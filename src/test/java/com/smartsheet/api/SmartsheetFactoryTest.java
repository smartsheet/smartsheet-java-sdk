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

package com.smartsheet.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmartsheetFactoryTest {
    @Test
    void testStaticStrings() {
        assertThat(SmartsheetFactory.DEFAULT_BASE_URI).isEqualTo("https://api.smartsheet.com/2.0/");
        assertThat(SmartsheetFactory.GOV_BASE_URI).isEqualTo("https://api.smartsheetgov.com/2.0/");
    }

    @Nested
    class CreateDefaultClientTests {
    }

    @Nested
    class CreateDefaultGovAccountClientTests {
    }

    @Nested
    class CustomTests {
        @Test
        void custom() {
            // Act
            SmartsheetBuilder result = SmartsheetFactory.custom();

            // Assert
            assertThat(result).isInstanceOf(SmartsheetBuilder.class);
        }
    }
}
