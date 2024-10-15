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

import com.smartsheet.api.models.enums.DestinationType;
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
public class ErrorDetail {

    /**
     * User's alternate email address that was specified in the request.
     */
    private String alternateEmailAddress;

    /**
     * When allowPartialSuccess = false, index of the row that caused the error.
     */
    private Integer index;

    /**
     * The server-side limit on the number of sheets allowed in a single folder/workspace copy operation.
     */
    private Integer maxSheetCount;

    /**
     * User's primary email address that must instead by specified for the operation.
     */
    private String primaryEmailAddress;

    /**
     * When allowPartialSuccess = false, rowID of the row that caused the error.
     */
    private Long rowId;

    /**
     * The ID of the top level folder or workspace that was partially copied.
     */
    private Long topContainerId;

    /**
     * The destination type of the top level folder or workspace that was partially copied.
     */
    private DestinationType topContainerType;
}
