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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Result object to contain information about a PUT or POST request.
 *
 * @param <T> the generic type
 */
@Getter
@Setter
@ToString
@SuperBuilder
// We need to have a constructor with no arguments for the subclasses of this class to work
@NoArgsConstructor
// We need to have a constructor with all arguments for Lombok Builder to work
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {
    /**
     * Represents the result code from the request.
     */
    private Integer resultCode;

    /**
     * Represents the message from the request.
     */
    private String message;

    /**
     * Represents the object that was created or updated.
     */
    private T result;

    /**
     * Represents the new version of the sheet. It is only available on some operations.
     */
    private Integer version;
}
