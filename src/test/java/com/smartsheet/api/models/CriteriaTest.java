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

package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.CriteriaTarget;
import com.smartsheet.api.models.enums.Operator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CriteriaTest {
    @Nested
    class BuilderTests {
        @Test
        void criteriaBuilder() {
            // Arrange
            List<Object> values = List.of(new Object());

            // Act
            Criteria criteriaNoArg = Criteria.builder().build();
            criteriaNoArg.setColumnId(5L);
            criteriaNoArg.setOperator(Operator.EQUAL);
            criteriaNoArg.setTarget(CriteriaTarget.ROW);
            criteriaNoArg.setValues(values);

            Criteria criteriaAllArg = Criteria.builder()
                    .columnId(5L)
                    .operator(Operator.EQUAL)
                    .target(CriteriaTarget.ROW)
                    .values(values)
                    .build();

            // Assert
            assertThat(criteriaNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(criteriaAllArg);
        }
    }
}
