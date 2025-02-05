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

package com.smartsheet.api.models.enums;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AutomationRuleDisabledReasonTest {
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValueOfTests {
        @ParameterizedTest
        @MethodSource("valuesArguments")
        void valueOf(AutomationRuleDisabledReason expectedAutomationRuleDisabledReason, String value) {
            // Act
            AutomationRuleDisabledReason result = AutomationRuleDisabledReason.valueOf(value);

            // Assert
            assertThat(result).isEqualTo(expectedAutomationRuleDisabledReason);

            // This will cause the test to fail if we ever add a new value.
            // Please remember to add the new value in the method below
            assertThat(AutomationRuleDisabledReason.values()).hasSize(7);
        }

        private Stream<Arguments> valuesArguments() {
            return Stream.of(
                    Arguments.of(AutomationRuleDisabledReason.AUTOMATION_NOT_ENABLED_FOR_ORG, "AUTOMATION_NOT_ENABLED_FOR_ORG"),
                    Arguments.of(AutomationRuleDisabledReason.COLUMN_MISSING, "COLUMN_MISSING"),
                    Arguments.of(AutomationRuleDisabledReason.COLUMN_TYPE_INCOMPATIBLE, "COLUMN_TYPE_INCOMPATIBLE"),
                    Arguments.of(AutomationRuleDisabledReason.NO_POTENTIAL_RECIPIENTS, "NO_POTENTIAL_RECIPIENTS"),
                    Arguments.of(AutomationRuleDisabledReason.NO_VALID_SELECTED_COLUMNS, "NO_VALID_SELECTED_COLUMNS"),
                    Arguments.of(AutomationRuleDisabledReason.APPROVAL_COLUMN_MISSING, "APPROVAL_COLUMN_MISSING"),
                    Arguments.of(AutomationRuleDisabledReason.APPROVAL_COLUMN_WRONG_TYPE, "APPROVAL_COLUMN_WRONG_TYPE")
            );
        }
    }
}
