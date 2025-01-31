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

/**
 * An enum representing the available currency formats available in Smartsheet.
 */
public enum Currency {
    NONE(null, ""),
    ARGENTINE_PESO("ARS", "$"),
    AUSTRALIAN_DOLLAR("AUD", "$"),
    BRAZIL_REAIS("BRL", "R$"),
    CANADIAN_DOLLAR("CAD", "$"),
    CHILEAN_PESOS("CLP", "$"),
    EURO("EUR", "€"),
    BRITISH_POUND("GBP", "£"),
    ISRAEL_SHEKEL("ILS", "₪"),
    INDIA_RUPEES("INR", "₨"),
    JAPAN_YEN("JPY", "¥"),
    MEXICAN_PESOS("MXN", "$"),
    RUSSIAN_RUBLES("RUB", "руб"),
    USD("USD", "$"),
    SOUTHAFRICA_RAND("ZAR", "R"),
    SWISS_FRANC("CHF", "CHF"),
    CHINA_YUAN("CNY", "元"),
    DENMARK_KRONER("DKK", "kr"),
    HONKKONG_DOLLAR("HKD", "HK$"),
    SOUTHKOREAN_WON("KRW", "₩"),
    NORWAY_KRONER("NOK", "kr"),
    NEWZEALAND_DOLLAR("NZD", "$"),
    SWEDEN_KRONOR("SEK", "kr"),
    SINGAPORE_DOLLAR("SGD", "$");

    /**
     * The default setting when the {@link Format} for {@link Currency} is null;
     */
    public static final Currency DEFAULT = NONE;

    private final String code;
    private final String symbol;

    Currency(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }
}
