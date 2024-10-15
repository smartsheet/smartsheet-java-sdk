/*
 * Copyright (C) 2024 Smartsheet
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

class FormatTablesTest {
    @Nested
    class BuilderTests {
        @Test
        void formatTablesBuilder() {
            // Arrange
            List<Currency> currency = List.of(new Currency());
            List<FontFamily> fontFamily = List.of(new FontFamily());

            // Act
            FormatTables formatTablesNoArg = FormatTables.builder().build();
            formatTablesNoArg.setDefaults("default");
            formatTablesNoArg.setBold(List.of("bold"));
            formatTablesNoArg.setColor(List.of("color"));
            formatTablesNoArg.setCurrency(currency);
            formatTablesNoArg.setDateFormat(List.of("dateFormat"));
            formatTablesNoArg.setDecimalCount(List.of("decimalCount"));
            formatTablesNoArg.setFontFamily(fontFamily);
            formatTablesNoArg.setFontSize(List.of("fontSize"));
            formatTablesNoArg.setHorizontalAlign(List.of("horizontalAlign"));
            formatTablesNoArg.setItalic(List.of("italic"));
            formatTablesNoArg.setNumberFormat(List.of("numberFormat"));
            formatTablesNoArg.setStrikethrough(List.of("strikethrough"));
            formatTablesNoArg.setTextWrap(List.of("textWrap"));
            formatTablesNoArg.setThousandsSeparator(List.of("thousandsSeparator"));
            formatTablesNoArg.setUnderline(List.of("underline"));
            formatTablesNoArg.setVerticalAlign(List.of("verticalAlign"));

            FormatTables formatTablesAllArg = FormatTables.builder()
                    .defaults("default")
                    .bold(List.of("bold"))
                    .color(List.of("color"))
                    .currency(currency)
                    .dateFormat(List.of("dateFormat"))
                    .decimalCount(List.of("decimalCount"))
                    .fontFamily(fontFamily)
                    .fontSize(List.of("fontSize"))
                    .horizontalAlign(List.of("horizontalAlign"))
                    .italic(List.of("italic"))
                    .numberFormat(List.of("numberFormat"))
                    .strikethrough(List.of("strikethrough"))
                    .textWrap(List.of("textWrap"))
                    .thousandsSeparator(List.of("thousandsSeparator"))
                    .underline(List.of("underline"))
                    .verticalAlign(List.of("verticalAlign"))
                    .build();

            // Assert
            assertThat(formatTablesNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(formatTablesAllArg);
        }
    }
}
