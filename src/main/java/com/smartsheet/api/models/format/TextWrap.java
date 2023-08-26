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
 * An enumeration representing the available text wrap settings within Smartsheet.
 */
public enum TextWrap {
    NONE(false),
    ON(true),
    ;
    private final boolean wrapped;

    TextWrap(boolean wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * The default setting when the {@link Format} for {@link TextWrap} is null;
     */
    public static final TextWrap DEFAULT = NONE;

    /**
     * @return the wrapped
     */
    public boolean isWrapped() {
        return wrapped;
    }
}
