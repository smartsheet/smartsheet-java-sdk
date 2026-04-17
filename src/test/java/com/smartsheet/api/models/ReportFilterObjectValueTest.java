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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsheet.api.models.enums.ObjectValueType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportFilterObjectValueTest {

    @Test
    void testStringValue() {
        ObjectValue value = ReportFilterObjectValue.string("test");
        assertThat(value).isInstanceOf(StringObjectValue.class);
        assertThat(((StringObjectValue) value).getValue()).isEqualTo("test");
    }

    @Test
    void testNumberValue() {
        ObjectValue value = ReportFilterObjectValue.number(42);
        assertThat(value).isInstanceOf(NumberObjectValue.class);
        assertThat(((NumberObjectValue) value).getValue()).isEqualTo(42);
    }

    @Test
    void testDateValue() {
        ObjectValue value = ReportFilterObjectValue.date("2024-01-01");
        assertThat(value).isInstanceOf(DateObjectValue.class);
        assertThat(((DateObjectValue) value).getValue()).isEqualTo("2024-01-01");
        assertThat(value.getObjectType()).isEqualTo(ObjectValueType.DATE);
    }

    @Test
    void testCurrentUserValue() {
        ObjectValue value = ReportFilterObjectValue.currentUser();
        assertThat(value).isInstanceOf(CurrentUserObjectValue.class);

        CurrentUserObjectValue currentUserValue = (CurrentUserObjectValue) value;
        assertThat(currentUserValue.getObjectTypeString()).isEqualTo("CURRENT_USER");
        assertThat(currentUserValue.getValue()).isEqualTo("");
    }

    @Test
    void testSerializationOfMixedValues() throws JsonProcessingException {
        ReportFilterCriterion criterion = new ReportFilterCriterion();
        criterion.setValues(Arrays.asList(
                ReportFilterObjectValue.string("value1"),
                ReportFilterObjectValue.number(42),
                ReportFilterObjectValue.date("2024-01-01")
        ));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = objectMapper.writeValueAsString(criterion.getValues());

        // Verify that primitives serialize as raw values and dates serialize with objectType
        assertThat(json).contains("\"value1\"");
        assertThat(json).contains("42");
        assertThat(json).contains("\"objectType\"");
        assertThat(json).contains("\"DATE\"");
    }

    @Test
    void testCurrentUserSerialization() throws JsonProcessingException {
        CurrentUserObjectValue currentUser = new CurrentUserObjectValue();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = objectMapper.writeValueAsString(currentUser);

        // Verify CURRENT_USER serializes with correct objectType
        assertThat(json).contains("\"objectType\"");
        assertThat(json).contains("\"CURRENT_USER\"");
        assertThat(json).contains("\"value\"");

        // Verify it does NOT serialize as null
        assertThat(json).doesNotContain("\"objectType\":null");

        // Verify exact structure
        assertThat(json).isEqualTo("{\"objectType\":\"CURRENT_USER\",\"value\":\"\"}");
    }

    @Test
    void testCurrentUserInFilterCriterion() throws JsonProcessingException {
        ReportFilterCriterion criterion = new ReportFilterCriterion();
        criterion.setValues(Arrays.asList(
                ReportFilterObjectValue.currentUser()
        ));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = objectMapper.writeValueAsString(criterion.getValues());

        // Verify CURRENT_USER serializes correctly in a list context
        assertThat(json).contains("\"objectType\":\"CURRENT_USER\"");
        assertThat(json).doesNotContain("\"objectType\":null");
    }
}
