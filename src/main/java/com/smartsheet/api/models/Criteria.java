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

import com.smartsheet.api.models.enums.CriteriaTarget;
import com.smartsheet.api.models.enums.Operator;
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
public class Criteria {

    /**
     * column ID
     */
    private Long columnId;

    /**
     * Represents the operator type
     */
    private Operator operator;

    /**
     * The target for the filter query (currently only ROW for row filters)
     */
    private CriteriaTarget target;

    /**
     * Present if a custom filter criteria's operator has one or more arguments
     */
    private List<Object> values;
}
