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

package com.smartsheet.api.models.enums;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AutomationActionFrequencyTest {
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToStringTests {
        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class ValueOfTests {
            @ParameterizedTest
            @MethodSource("valuesArguments")
            void valueOf(AutomationActionFrequency expectedAutomationActionFrequency, String value) {
                // Act
                AutomationActionFrequency result = AutomationActionFrequency.valueOf(value);

                // Assert
                assertThat(result).isEqualTo(expectedAutomationActionFrequency);

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in the method below
                assertThat(AutomationActionFrequency.values()).hasSize(4);
            }

            private Stream<Arguments> valuesArguments() {
                return Stream.of(
                        Arguments.of(AutomationActionFrequency.IMMEDIATELY, "IMMEDIATELY"),
                        Arguments.of(AutomationActionFrequency.HOURLY, "HOURLY"),
                        Arguments.of(AutomationActionFrequency.DAILY, "DAILY"),
                        Arguments.of(AutomationActionFrequency.WEEKLY, "WEEKLY")
                );
            }
        }
    }
}
