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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class Predecessor {
    /**
     * The Id of the predecessor row
     */
    private Long rowId;

    /**
     * The row number of the predecessor row
     */
    private Integer rowNumber;

    /**
     * The type of the predecessor - one of FS, FF, SS, or SF
     */
    private String type;

    /**
     * The lag value of this predecessor. Omitted if there is no lag.
     */
    private Duration lag;

    /**
     * True if the row referenced by rowId is not a valid row in this sheet, or there is a circular reference
     */
    private Boolean invalid;

    /**
     * True if this predecessor is in the critical path
     */
    private Boolean inCriticalPath;
}

