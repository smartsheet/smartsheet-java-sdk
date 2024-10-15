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

package com.smartsheet.api.models.format;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An enumeration representing the available Strikethrough state for a format.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Strikethrough {
    NONE(false),
    ON(true),
    ;
    private final boolean striken;

    /**
     * The default setting when the {@link Format} for {@link Strikethrough} is null;
     */
    public static final Strikethrough DEFAULT = NONE;
}
