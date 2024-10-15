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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the AutoNumberFormat object. It describes how the the System Column type of "AUTO_NUMBER" is auto-generated
 *
 * @see <a href="https://smartsheet.redoc.ly/tag/commonObjects/#section/AutoNumberFormat-Object">Auto Number Format API Documentation</a>
 * @see <a href="http://help.smartsheet.com/customer/portal/articles/1108408-auto-numbering">Auto Number Format Help</a>
 */
@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class AutoNumberFormat {

    /**
     * Represents the prefix.
     */
    private String prefix;

    /**
     * Represents the suffix.
     */
    private String suffix;

    /**
     * Represents the fill.
     */
    private String fill;

    /**
     * Represents the starting number.
     */
    private Long startingNumber;
}
