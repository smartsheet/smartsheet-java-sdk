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

package com.smartsheet.api.internal.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.CellLink;

import java.io.IOException;

public class CellLinkSerializer extends JsonSerializer<CellLink> {

    private final com.fasterxml.jackson.databind.JsonSerializer<Object> defaultSerializer;

    /**
     * Constructor
     */
    public CellLinkSerializer(JsonSerializer<Object> defaultSerializer) {
        Util.throwIfNull(defaultSerializer);
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serialize(CellLink cellLink, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        if (cellLink.isNull()) {
            gen.writeNull();
        } else {
            defaultSerializer.serialize(cellLink, gen, serializers);
        }
    }
}
