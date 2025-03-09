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

import java.util.List;

/**
 * A wrapper object used to Wrap the data that comes back from the API. It holds the paging info as well as a List
 * of objects of the specified type.
 *
 * @param <T> object
 */
@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class PagedResult<T> {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalCount;
    private Integer totalPages;
    private List<T> data;
}
