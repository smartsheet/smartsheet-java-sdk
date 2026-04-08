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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Dedicated test to validate CurrentUserObjectValue serialization.
 * This ensures the objectType field serializes as "CURRENT_USER" and not null.
 */
public class CurrentUserObjectValueSerializationTest {

    @Test
    void testCurrentUserSerializesWithCorrectObjectType() throws JsonProcessingException {
        CurrentUserObjectValue currentUser = new CurrentUserObjectValue();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = objectMapper.writeValueAsString(currentUser);

        System.out.println("Serialized CurrentUserObjectValue: " + json);

        // Verify the exact JSON structure
        assertThat(json).isEqualTo("{\"objectType\":\"CURRENT_USER\",\"value\":\"\"}");
    }

    @Test
    void testCurrentUserDoesNotSerializeObjectTypeAsNull() throws JsonProcessingException {
        CurrentUserObjectValue currentUser = new CurrentUserObjectValue();

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(currentUser);

        System.out.println("Serialized CurrentUserObjectValue (with nulls): " + json);

        // Verify objectType is NOT null
        assertThat(json).contains("\"objectType\":\"CURRENT_USER\"");
        assertThat(json).doesNotContain("\"objectType\":null");
    }

    @Test
    void testCurrentUserWithCustomValue() throws JsonProcessingException {
        CurrentUserObjectValue currentUser = new CurrentUserObjectValue("custom");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = objectMapper.writeValueAsString(currentUser);

        System.out.println("Serialized CurrentUserObjectValue with custom value: " + json);

        assertThat(json).isEqualTo("{\"objectType\":\"CURRENT_USER\",\"value\":\"custom\"}");
    }

    @Test
    void testCurrentUserInReportFilterCriterion() throws JsonProcessingException {
        ReportFilterCriterion criterion = new ReportFilterCriterion();
        criterion.setColumn(new ReportColumnIdentifier().setTitle("Assigned To"));
        criterion.setOperator(com.smartsheet.api.models.enums.ReportFilterOperator.EQUAL);
        criterion.setValues(java.util.Arrays.asList(new CurrentUserObjectValue()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = objectMapper.writeValueAsString(criterion);

        System.out.println("Serialized ReportFilterCriterion with CurrentUser: " + json);

        // Verify the structure includes CURRENT_USER correctly
        assertThat(json).contains("\"objectType\":\"CURRENT_USER\"");
        assertThat(json).doesNotContain("\"objectType\":null");
    }
}
