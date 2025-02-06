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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FontFamilyTest {
    @Nested
    class BuilderTests {
        @Test
        void fontFamilyBuilder() {
            // Act
            FontFamily fontFamilyNoArg = FontFamily.builder().build();
            fontFamilyNoArg.setName("Arial");
            fontFamilyNoArg.setTraits(List.of("Bold", "Italic"));

            FontFamily fontFamilyAllArg = FontFamily.builder()
                    .name("Arial")
                    .traits(List.of("Bold", "Italic"))
                    .build();

            // Assert
            assertThat(fontFamilyNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(fontFamilyAllArg);
        }
    }
}
