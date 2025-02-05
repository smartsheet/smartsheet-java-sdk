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

class EventSourceTest {
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValueOfTests {
        @ParameterizedTest
        @MethodSource("valuesArguments")
        void valueOf(EventSource expectedEventSource, String value) {
            // Act
            EventSource result = EventSource.valueOf(value);

            // Assert
            assertThat(result).isEqualTo(expectedEventSource);

            // This will cause the test to fail if we ever add a new value.
            // Please remember to add the new value in the method below
            assertThat(EventSource.values()).hasSize(6);
        }

        private Stream<Arguments> valuesArguments() {
            return Stream.of(
                    Arguments.of(EventSource.WEB_APP, "WEB_APP"),
                    Arguments.of(EventSource.MOBILE_IOS, "MOBILE_IOS"),
                    Arguments.of(EventSource.MOBILE_ANDROID, "MOBILE_ANDROID"),
                    Arguments.of(EventSource.API_INTEGRATED_APP, "API_INTEGRATED_APP"),
                    Arguments.of(EventSource.API_UNDEFINED_APP, "API_UNDEFINED_APP"),
                    Arguments.of(EventSource.UNKNOWN, "UNKNOWN")
            );
        }
    }
}
