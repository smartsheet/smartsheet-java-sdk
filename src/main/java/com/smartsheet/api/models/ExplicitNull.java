package com.smartsheet.api.models;

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

import com.fasterxml.jackson.core.JsonGenerator;
import com.smartsheet.api.models.enums.ObjectValueType;

import java.io.IOException;

public class ExplicitNull implements PrimitiveObjectValue<Object> {

    public Object getValue() {
        return null;
    }

    public ExplicitNull setValue(Object value) {
        return this;
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeNull();
    }

    @Override
    public ObjectValueType getObjectType() {
        return ObjectValueType.NULL;
    }
}
