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

package com.smartsheet.api.internal.json;

import com.smartsheet.api.models.CurrentUserObjectValue;
import com.smartsheet.api.models.DateObjectValue;
import com.smartsheet.api.models.NumberObjectValue;
import com.smartsheet.api.models.ReportFilterObjectValue;
import com.smartsheet.api.models.StringObjectValue;
import com.smartsheet.api.models.enums.ObjectValueType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class ReportFilterObjectValueDeserializerTest {

    private static final float DELTA_FLOAT = 0.001f;
    private final JacksonJsonSerializer jacksonJsonSerializer = new JacksonJsonSerializer();

    @Test
    void stringValue() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values = getValues("[\"Test Value\"]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isInstanceOf(StringObjectValue.class);
        assertThat(((StringObjectValue) values.get(0)).getValue()).isEqualTo("Test Value");
    }

    @Test
    void numberValue_long() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values = getValues("[42]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isInstanceOf(NumberObjectValue.class);
        assertThat(((NumberObjectValue) values.get(0)).getValue().longValue()).isEqualTo(42L);
    }

    @Test
    void numberValue_decimal() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values = getValues("[123.456]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isInstanceOf(NumberObjectValue.class);
        assertThat(((NumberObjectValue) values.get(0)).getValue().floatValue())
                .isEqualTo(123.456f, within(DELTA_FLOAT));
    }

    @Test
    void dateValue() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values = getValues("[{\"objectType\":\"DATE\",\"value\":\"2024-01-01\"}]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isInstanceOf(DateObjectValue.class);
        DateObjectValue date = (DateObjectValue) values.get(0);
        assertThat(date.getObjectType()).isEqualTo(ObjectValueType.DATE);
        assertThat(date.getValue()).isEqualTo("2024-01-01");
    }

    @Test
    void dateTimeValue() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values =
                getValues("[{\"objectType\":\"DATETIME\",\"value\":\"2017-07-17T20:27:57Z\"}]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isInstanceOf(DateObjectValue.class);
        DateObjectValue date = (DateObjectValue) values.get(0);
        assertThat(date.getObjectType()).isEqualTo(ObjectValueType.DATETIME);
        assertThat(date.getValue()).isEqualTo("2017-07-17T20:27:57Z");
    }

    @Test
    void currentUserValue() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values =
                getValues("[{\"objectType\":\"CURRENT_USER\",\"value\":\"\"}]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isInstanceOf(CurrentUserObjectValue.class);
        CurrentUserObjectValue currentUser = (CurrentUserObjectValue) values.get(0);
        assertThat(currentUser.getObjectTypeString()).isEqualTo("CURRENT_USER");
        assertThat(currentUser.getValue()).isEqualTo("");
    }

    @Test
    void unknownObjectType_returnsNull() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values =
                getValues("[{\"objectType\":\"FUTURE_OBJECT_TYPE\",\"value\":1}]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isNull();
    }

    @Test
    void booleanPrimitive_returnsNull() throws IOException, JSONSerializerException {
        List<ReportFilterObjectValue> values = getValues("[true]");

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isNull();
    }

    @Test
    void mixedValues_roundTrip() throws IOException, JSONSerializerException {
        String json = "[\"value1\",42,{\"objectType\":\"DATE\",\"value\":\"2024-01-01\"},"
                + "{\"objectType\":\"CURRENT_USER\",\"value\":\"\"}]";

        List<ReportFilterObjectValue> values = getValues(json);

        assertThat(values).hasSize(4);
        assertThat(values.get(0)).isInstanceOf(StringObjectValue.class);
        assertThat(values.get(1)).isInstanceOf(NumberObjectValue.class);
        assertThat(values.get(2)).isInstanceOf(DateObjectValue.class);
        assertThat(values.get(3)).isInstanceOf(CurrentUserObjectValue.class);

        // Serialize back out and deserialize again to ensure stability.
        String serialized = jacksonJsonSerializer.serialize(new ContainingClass(values));
        ContainingClass reparsed = jacksonJsonSerializer.deserialize(
                ContainingClass.class, new ByteArrayInputStream(serialized.getBytes()));

        assertThat(reparsed.values).hasSize(4);
        assertThat(reparsed.values.get(0)).isInstanceOf(StringObjectValue.class);
        assertThat(reparsed.values.get(1)).isInstanceOf(NumberObjectValue.class);
        assertThat(reparsed.values.get(2)).isInstanceOf(DateObjectValue.class);
        assertThat(reparsed.values.get(3)).isInstanceOf(CurrentUserObjectValue.class);
    }

    private List<ReportFilterObjectValue> getValues(String valuesJson) throws IOException, JSONSerializerException {
        String json = "{\"values\":" + valuesJson + "}";
        ContainingClass result = jacksonJsonSerializer.deserialize(
                ContainingClass.class, new ByteArrayInputStream(json.getBytes()));
        return result.values;
    }

    private static class ContainingClass {
        public List<ReportFilterObjectValue> values;

        public ContainingClass() {
        }

        public ContainingClass(List<ReportFilterObjectValue> values) {
            this.values = values;
        }

        public List<ReportFilterObjectValue> getValues() {
            return values;
        }

        public void setValues(List<ReportFilterObjectValue> values) {
            this.values = values;
        }
    }
}
