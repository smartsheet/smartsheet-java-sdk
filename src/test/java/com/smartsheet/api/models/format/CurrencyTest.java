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

package com.smartsheet.api.models.format;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTest {
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GetCodeTests {
        @ParameterizedTest
        @MethodSource("getCodeArguments")
        void getCode(Currency currency, String expectedCode) {
            // Act
            String result = currency.getCode();

            // Assert
            assertThat(result).isEqualTo(expectedCode);

            // This will cause the test to fail if we ever add a new value.
            // Please remember to add the new value in the method below
            assertThat(Currency.values()).hasSize(24);
        }

        private Stream<Arguments> getCodeArguments() {
            return Stream.of(
                    Arguments.of(Currency.NONE, null),
                    Arguments.of(Currency.ARGENTINE_PESO, "ARS"),
                    Arguments.of(Currency.AUSTRALIAN_DOLLAR, "AUD"),
                    Arguments.of(Currency.BRAZIL_REAIS, "BRL"),
                    Arguments.of(Currency.CANADIAN_DOLLAR, "CAD"),
                    Arguments.of(Currency.CHILEAN_PESOS, "CLP"),
                    Arguments.of(Currency.EURO, "EUR"),
                    Arguments.of(Currency.BRITISH_POUND, "GBP"),
                    Arguments.of(Currency.ISRAEL_SHEKEL, "ILS"),
                    Arguments.of(Currency.INDIA_RUPEES, "INR"),
                    Arguments.of(Currency.JAPAN_YEN, "JPY"),
                    Arguments.of(Currency.MEXICAN_PESOS, "MXN"),
                    Arguments.of(Currency.RUSSIAN_RUBLES, "RUB"),
                    Arguments.of(Currency.USD, "USD"),
                    Arguments.of(Currency.SOUTHAFRICA_RAND, "ZAR"),
                    Arguments.of(Currency.SWISS_FRANC, "CHF"),
                    Arguments.of(Currency.CHINA_YUAN, "CNY"),
                    Arguments.of(Currency.DENMARK_KRONER, "DKK"),
                    Arguments.of(Currency.HONKKONG_DOLLAR, "HKD"),
                    Arguments.of(Currency.SOUTHKOREAN_WON, "KRW"),
                    Arguments.of(Currency.NORWAY_KRONER, "NOK"),
                    Arguments.of(Currency.NEWZEALAND_DOLLAR, "NZD"),
                    Arguments.of(Currency.SWEDEN_KRONOR, "SEK"),
                    Arguments.of(Currency.SINGAPORE_DOLLAR, "SGD")
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GetSymbolTests {
        @ParameterizedTest
        @MethodSource("getSymbolArguments")
        void getSymbol(Currency currency, String expectedSymbol) {
            // Act
            String result = currency.getSymbol();

            // Assert
            assertThat(result).isEqualTo(expectedSymbol);

            // This will cause the test to fail if we ever add a new value.
            // Please remember to add the new value in the method below
            assertThat(Currency.values()).hasSize(24);
        }

        private Stream<Arguments> getSymbolArguments() {
            return Stream.of(
                    Arguments.of(Currency.NONE, ""),
                    Arguments.of(Currency.ARGENTINE_PESO, "$"),
                    Arguments.of(Currency.AUSTRALIAN_DOLLAR, "$"),
                    Arguments.of(Currency.BRAZIL_REAIS, "R$"),
                    Arguments.of(Currency.CANADIAN_DOLLAR, "$"),
                    Arguments.of(Currency.CHILEAN_PESOS, "$"),
                    Arguments.of(Currency.EURO, "€"),
                    Arguments.of(Currency.BRITISH_POUND, "£"),
                    Arguments.of(Currency.ISRAEL_SHEKEL, "₪"),
                    Arguments.of(Currency.INDIA_RUPEES, "₨"),
                    Arguments.of(Currency.JAPAN_YEN, "¥"),
                    Arguments.of(Currency.MEXICAN_PESOS, "$"),
                    Arguments.of(Currency.RUSSIAN_RUBLES, "руб"),
                    Arguments.of(Currency.USD, "$"),
                    Arguments.of(Currency.SOUTHAFRICA_RAND, "R"),
                    Arguments.of(Currency.SWISS_FRANC, "CHF"),
                    Arguments.of(Currency.CHINA_YUAN, "元"),
                    Arguments.of(Currency.DENMARK_KRONER, "kr"),
                    Arguments.of(Currency.HONKKONG_DOLLAR, "HK$"),
                    Arguments.of(Currency.SOUTHKOREAN_WON, "₩"),
                    Arguments.of(Currency.NORWAY_KRONER, "kr"),
                    Arguments.of(Currency.NEWZEALAND_DOLLAR, "$"),
                    Arguments.of(Currency.SWEDEN_KRONOR, "kr"),
                    Arguments.of(Currency.SINGAPORE_DOLLAR, "$")
            );
        }
    }
}
