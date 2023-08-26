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

package com.smartsheet.api.models.format;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An enumeration representing the available font families within Smartsheet.
 */
public enum FontFamily {
    ARIAL("Arial", List.of("sans-serif")),
    TAHOMA("Tahoma", List.of("sans-serif")),
    TIMES_NEW_ROMAN("Times New Roman", List.of("serif")),
    VERDANA("Verdana", List.of("sans-serif")),
    ;
    private final String fontName;
    private final Set<String> traits;

    /**
     * The default setting when the {@link Format} for {@link FontFamily} is null;
     */
    public static final FontFamily DEFAULT = ARIAL;

    FontFamily(String name, List<String> traits) {
        this.fontName = name;
        this.traits = new HashSet<>(traits);
    }

    /**
     * @return the fontName
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * @return the traits
     */
    public Set<String> getTraits() {
        return traits;
    }
}
