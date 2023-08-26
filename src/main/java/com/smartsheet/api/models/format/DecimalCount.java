package com.smartsheet.api.models.format;

/*
 * Smartsheet SDK for Java
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

/**
 * An enumeration representing the Decimal count formats available in Smartsheet.
 */
public enum DecimalCount {
    COUNT_0(0),
    COUNT_1(1),
    COUNT_2(2),
    COUNT_3(3),
    COUNT_4(4),
    COUNT_5(5),
    ;

    private final int decimalCount;

    /**
     * The default setting when the {@link Format} for {@link DecimalCount} is null;
     */
    public static final DecimalCount DEFAULT = COUNT_0;

    DecimalCount(int count) {
        this.decimalCount = count;
    }

    /**
     * @return the decimalCount
     */
    public int getDecimalCount() {
        return decimalCount;
    }
}
