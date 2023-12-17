/*
 * Copyright (C) 2023 Smartsheet
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class FormatTables {

    /**
     * Represents the format descriptor.
     */
    private String defaults;

    /**
     * Represents Possible bold values: none,on.
     */
    private List<String> bold;

    /**
     * Represents Color hex values.
     */
    private List<String> color;

    /**
     * Represents Currency codes and symbols.
     */
    private List<Currency> currency;

    /**
     * Array of strings containing available date formats
     */
    private List<String> dateFormat;

    /**
     * Represents All allowed decimal count values.
     */
    private List<String> decimalCount;

    /**
     * Represents Font families with additional font information.
     */
    private List<FontFamily> fontFamily;

    /**
     * Represents Font sizes in points.
     */
    private List<String> fontSize;

    /**
     * Represents Possible horizontalAlign values: none,on.
     */
    private List<String> horizontalAlign;

    /**
     * Represents Possible italic values: none,on.
     */
    private List<String> italic;

    /**
     * Represents Possible numberFormat values: none
     NUMBER
     CURRENCY
     PERCENT.
     */
    private List<String> numberFormat;

    /**
     * Represents Possible strikethrough values: none,on.
     */
    private List<String> strikethrough;

    /**
     * Represents Possible textWrap values: none,on.
     */
    private List<String> textWrap;

    /**
     * Represents Possible thousandsSeparator values: none,on.
     */
    private List<String> thousandsSeparator;

    /**
     * Represents Possible underline values: none,on.
     */
    private List<String> underline;

    /**
     * Represents Possible verticalAlign values: top, middle, bottom.
     */
    private List<String> verticalAlign;
}
