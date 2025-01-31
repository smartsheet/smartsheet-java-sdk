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

import com.smartsheet.api.models.DateObjectValue;
import com.smartsheet.api.models.Duration;
import com.smartsheet.api.models.ObjectValue;
import com.smartsheet.api.models.PrimitiveObjectValue;
import com.smartsheet.api.models.enums.ObjectValueType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class ObjectValueDeserializerTest {
    private static final double DELTA = 0.000001;
    private static final float DELTA_FLOAT = 0.001f;
    private final JacksonJsonSerializer jacksonJsonSerializer = new JacksonJsonSerializer();

    @Test
    void unknownObjectType() throws IOException, JSONSerializerException {
        // If a new object type is introduced to the Smartsheet API, it shouldn't break existing integrations.
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"FUTURE_OBJECT_TYPE\"," +
                "                        \"value\": 1\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue).isNull();
    }

    @Test
    void unknownAttribute() throws IOException, JSONSerializerException {
        // Verify that unknown attributes are ignored by the SDK
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"DURATION\",\n" +
                "                        \"someNewAttributeAddedInFutureVersionOfAPI\": 1,\n" +
                "                        \"hours\": 7,\n" +
                "                        \"minutes\": 30\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue).isInstanceOf(Duration.class);

        assertSerializedAttributes(objectValue,
                new ExpectedAttributeValue("objectType", ObjectValueType.DURATION.name()),
                new ExpectedAttributeValue("hours", 7.0),
                new ExpectedAttributeValue("minutes", 30.0));
    }

    @Test
    void duration_someAttributes() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"DURATION\",\n" +
                "                        \"days\": 1,\n" +
                "                        \"hours\": 7,\n" +
                "                        \"minutes\": 30\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue).isInstanceOf(Duration.class);

        Duration durationObjectValue = (Duration) objectValue;
        assertThat(durationObjectValue.getDays().intValue()).isEqualTo(1);
        assertThat(durationObjectValue.getHours().intValue()).isEqualTo(7);
        assertThat(durationObjectValue.getMinutes().intValue()).isEqualTo(30);

        assertSerializedAttributes(objectValue,
                new ExpectedAttributeValue("objectType", ObjectValueType.DURATION.name()),
                new ExpectedAttributeValue("days", 1.0),
                new ExpectedAttributeValue("hours", 7.0),
                new ExpectedAttributeValue("minutes", 30.0));
    }

    @Test
    void duration_allAttributes() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"DURATION\",\n" +
                "                        \"negative\": true,\n" +
                "                        \"elapsed\": false,\n" +
                "                        \"weeks\": 2,\n" +
                "                        \"days\": 3,\n" +
                "                        \"hours\": 7,\n" +
                "                        \"minutes\": 30,\n" +
                "                        \"seconds\": 45,\n" +
                "                        \"milliseconds\": 500\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue).isInstanceOf(Duration.class);
        Duration durationObjectValue = (Duration) objectValue;
        assertThat(durationObjectValue.getNegative()).isTrue();
        assertThat(durationObjectValue.getElapsed()).isFalse();
        assertThat(durationObjectValue.getWeeks().intValue()).isEqualTo(2);
        assertThat(durationObjectValue.getDays().intValue()).isEqualTo(3);
        assertThat(durationObjectValue.getHours().intValue()).isEqualTo(7);
        assertThat(durationObjectValue.getMinutes().intValue()).isEqualTo(30);
        assertThat(durationObjectValue.getSeconds().intValue()).isEqualTo(45);
        assertThat(durationObjectValue.getMilliseconds().intValue()).isEqualTo(500);

        assertSerializedAttributes(objectValue,
                new ExpectedAttributeValue("objectType", ObjectValueType.DURATION.name()),
                new ExpectedAttributeValue("negative", true),
                new ExpectedAttributeValue("elapsed", false),
                new ExpectedAttributeValue("weeks", 2.0),
                new ExpectedAttributeValue("days", 3.0),
                new ExpectedAttributeValue("hours", 7.0),
                new ExpectedAttributeValue("minutes", 30.0),
                new ExpectedAttributeValue("seconds", 45.0),
                new ExpectedAttributeValue("milliseconds", 500.0));
    }

    @Test
    void duration_floatingPointValues() throws JSONSerializerException, IOException {
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"DURATION\",\n" +
                "                        \"negative\": true,\n" +
                "                        \"elapsed\": false,\n" +
                "                        \"weeks\": 2.3,\n" +
                "                        \"days\": 3.4,\n" +
                "                        \"hours\": 7.5,\n" +
                "                        \"minutes\": 30.6,\n" +
                "                        \"seconds\": 45.7,\n" +
                "                        \"milliseconds\": 500.8\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue).isInstanceOf(Duration.class);
        Duration durationObjectValue = (Duration) objectValue;
        assertThat(durationObjectValue.getNegative()).isTrue();
        assertThat(durationObjectValue.getElapsed()).isFalse();
        assertThat(durationObjectValue.getWeeks()).isEqualTo(2.3, within(DELTA));
        assertThat(durationObjectValue.getDays()).isEqualTo(3.4, within(DELTA));
        assertThat(durationObjectValue.getHours()).isEqualTo(7.5, within(DELTA));
        assertThat(durationObjectValue.getMinutes()).isEqualTo(30.6, within(DELTA));
        assertThat(durationObjectValue.getSeconds()).isEqualTo(45.7, within(DELTA));
        assertThat(durationObjectValue.getMilliseconds()).isEqualTo(500.8, within(DELTA));

        assertSerializedAttributes(objectValue,
                new ExpectedAttributeValue("objectType", ObjectValueType.DURATION.name()),
                new ExpectedAttributeValue("negative", true),
                new ExpectedAttributeValue("elapsed", false),
                new ExpectedAttributeValue("weeks", 2.3),
                new ExpectedAttributeValue("days", 3.4),
                new ExpectedAttributeValue("hours", 7.5),
                new ExpectedAttributeValue("minutes", 30.6),
                new ExpectedAttributeValue("seconds", 45.7),
                new ExpectedAttributeValue("milliseconds", 500.8));
    }

    @Test
    void abstractDateTime() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"ABSTRACT_DATETIME\",\n" +
                "                        \"value\": \"2017-07-01T16:30:07\"\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue.getObjectType()).isEqualTo(ObjectValueType.ABSTRACT_DATETIME);

        DateObjectValue dateObjectValue = (DateObjectValue) objectValue;
        assertThat(dateObjectValue.getValue()).isEqualTo("2017-07-01T16:30:07");

        assertSerializedAttributes(objectValue,
                new ExpectedAttributeValue("objectType", ObjectValueType.ABSTRACT_DATETIME.name()),
                new ExpectedAttributeValue("value", "2017-07-01T16:30:07"));
    }

    @Test
    void date() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"DATE\",\n" +
                "                        \"value\": \"2017-07-17\"\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue.getObjectType()).isEqualTo(ObjectValueType.DATE);

        DateObjectValue dateObjectValue = (DateObjectValue) objectValue;
        assertThat(dateObjectValue.getValue()).isEqualTo("2017-07-17");

        assertSerializedAttributes(objectValue,
                new ExpectedAttributeValue("objectType", ObjectValueType.DATE.name()),
                new ExpectedAttributeValue("value", "2017-07-17"));
    }

    @Test
    void dateObjectValue_toDate() throws IOException, JSONSerializerException, ParseException {
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"DATETIME\",\n" +
                "                        \"value\": \"2017-07-17T20:27:57Z\"\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue.getObjectType()).isEqualTo(ObjectValueType.DATETIME);

        DateObjectValue dateObjectValue = (DateObjectValue) objectValue;

        Date date = dateObjectValue.toDate();
        assertThat(date.getYear() + 1900).isEqualTo(2017);
        assertThat(date.getMonth() + 1).isEqualTo(7);
        assertThat(date.getDate()).isEqualTo(17);
        assertThat(date.getHours()).isEqualTo(20);
        assertThat(date.getMinutes()).isEqualTo(27);
        assertThat(date.getSeconds()).isEqualTo(57);
    }

    @Test
    void dateTime() throws IOException, JSONSerializerException, ParseException {
        String json = "{\"objectValue\": {\n" +
                "                        \"objectType\": \"DATETIME\",\n" +
                "                        \"value\": \"2017-07-17T20:27:57Z\"\n" +
                "                    }}";

        ObjectValue objectValue = getObjectValue(json);

        assertThat(objectValue.getObjectType()).isEqualTo(ObjectValueType.DATETIME);

        DateObjectValue dateObjectValue = (DateObjectValue) objectValue;

        assertThat(dateObjectValue.getValue()).isEqualTo("2017-07-17T20:27:57Z");

        Date date = dateObjectValue.toDate();
        assertThat(date.getYear() + 1900).isEqualTo(2017);
        assertThat(date.getMonth() + 1).isEqualTo(7);
        assertThat(date.getDate()).isEqualTo(17);
        assertThat(date.getHours()).isEqualTo(20);
        assertThat(date.getMinutes()).isEqualTo(27);
        assertThat(date.getSeconds()).isEqualTo(57);

        assertSerializedAttributes(objectValue,
                new ExpectedAttributeValue("objectType", ObjectValueType.DATETIME.name()),
                new ExpectedAttributeValue("value", "2017-07-17T20:27:57Z"));

    }

    @Test
    void longObjectValue() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\":123456}";
        ObjectValue actual = getObjectValue(json);
        PrimitiveObjectValue<Number> numberPrimitiveObjectValue = (PrimitiveObjectValue<Number>) actual;
        assertThat(numberPrimitiveObjectValue.getValue().longValue()).isEqualTo(123456L);

        assertThat(jacksonJsonSerializer.serialize(actual)).isEqualTo("123456");
    }

    @Test
    void numberObjectValue() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\":123456.789}";
        ObjectValue actual = getObjectValue(json);
        PrimitiveObjectValue<Number> numberPrimitiveObjectValue = (PrimitiveObjectValue<Number>) actual;
        assertThat(numberPrimitiveObjectValue.getValue().floatValue()).isEqualTo(123456.789f, within(DELTA_FLOAT));

        assertThat(jacksonJsonSerializer.serialize(actual)).isEqualTo("123456.789");
    }

    @Test
    void stringObjectValue() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\":\"foo\"}";
        ObjectValue actual = getObjectValue(json);
        PrimitiveObjectValue<String> primitiveObjectValue = (PrimitiveObjectValue<String>) actual;
        assertThat(primitiveObjectValue.getValue()).isEqualTo("foo");

        assertThat(jacksonJsonSerializer.serialize(actual)).isEqualTo("\"foo\"");
    }

    @Test
    void booleanObjectValue() throws IOException, JSONSerializerException {
        String json = "{\"objectValue\":true}";
        ObjectValue actual = getObjectValue(json);
        PrimitiveObjectValue<Boolean> primitiveObjectValue = (PrimitiveObjectValue<Boolean>) actual;
        assertThat(primitiveObjectValue.getValue()).isTrue();
        assertThat(jacksonJsonSerializer.serialize(actual)).isEqualTo("true");
    }

    private static class ExpectedAttributeValue {
        final String attributeName;
        final Object attributeValue;

        ExpectedAttributeValue(String attributeName, Object attributeValue) {
            this.attributeName = attributeName;
            this.attributeValue = attributeValue;
        }
    }

    /**
     * Serializes the objectValue and verifies that all expected attributes are serialized out and that no extra elements exist.
     */
    private void assertSerializedAttributes(ObjectValue objectValue, ExpectedAttributeValue... expected) throws JSONSerializerException {
        String json = jacksonJsonSerializer.serialize(objectValue);
        Map<String, Object> serializedMap = jacksonJsonSerializer.deserializeMap(new ByteArrayInputStream(json.getBytes()));

        for (ExpectedAttributeValue expectedAttribute : expected) {
            assertThat(serializedMap).containsEntry(expectedAttribute.attributeName, expectedAttribute.attributeValue);
        }

        assertThat(serializedMap).hasSameSizeAs(expected);
    }

    /**
     * Serializes in an ObjectValue using the jacksonJsonSerializer as configured by the rest of the SDK.
     */
    private ObjectValue getObjectValue(String json) throws IOException, JSONSerializerException {
        ContainingClass deserializedResult = jacksonJsonSerializer.deserialize(
                ContainingClass.class,
                new ByteArrayInputStream(json.getBytes())
        );

        // Since we are here, serialize it back out again to automatically test that it can be serialized out.
        String json2 = jacksonJsonSerializer.serialize(deserializedResult);
        ContainingClass deserializedResult2 = jacksonJsonSerializer.deserialize(
                ContainingClass.class,
                new ByteArrayInputStream(json2.getBytes())
        );

        return deserializedResult.getObjectValue();
    }

    /**
     * Simple class simulating an object value as a member
     */
    private static class ContainingClass {
        public ObjectValue objectValue;

        public ContainingClass() {
        }

        public ContainingClass(ObjectValue objectValue) {
            this.objectValue = objectValue;
        }

        public ObjectValue getObjectValue() {
            return objectValue;
        }

        public void setObjectValue(ObjectValue objectValue) {
            this.objectValue = objectValue;
        }
    }
}
