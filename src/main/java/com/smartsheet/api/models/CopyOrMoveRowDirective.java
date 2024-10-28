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

import java.util.List;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class CopyOrMoveRowDirective {

    /**
     * Represents the IDs of the rows to move or copy from the source sheet.
     */
    private List<Long> rowIds;

    /**
     * Represents the CopyOrMoveRowDestination object that identifies the destination sheet
     */
    private CopyOrMoveRowDestination to;
}
