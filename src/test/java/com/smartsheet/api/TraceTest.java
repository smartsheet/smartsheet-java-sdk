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

package com.smartsheet.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TraceTest {

    @Test
    void testEnumValues() {
        assertThat(Trace.valueOf("RequestHeaders")).isNotNull();
        assertThat(Trace.valueOf("RequestBody")).isNotNull();
        assertThat(Trace.valueOf("RequestBodySummary")).isNotNull();
        assertThat(Trace.valueOf("ResponseHeaders")).isNotNull();
        assertThat(Trace.valueOf("ResponseBody")).isNotNull();
        assertThat(Trace.valueOf("ResponseBodySummary")).isNotNull();
        assertThat(Trace.valueOf("Request")).isNotNull();
        assertThat(Trace.valueOf("Response")).isNotNull();

        // This will cause the test to fail if we ever add a new value.
        // Please remember to add the new value in these tests
        assertThat(Trace.values()).hasSize(8);
    }

    @Nested
    class AddReplacementsTests {

        @Nested
        class DefaultAddReplacementsTests {
            @ParameterizedTest
            @ValueSource(strings = {
                    "RequestHeaders",
                    "RequestBody",
                    "RequestBodySummary",
                    "ResponseHeaders",
                    "ResponseBody",
                    "ResponseBodySummary"
            })
            void addReplacements_default_withSetReturnsFalse(String traceInput) {
                // Arrange
                Set<Trace> input = Set.of(Trace.RequestBodySummary, Trace.RequestBody);
                Trace trace = Trace.valueOf(traceInput);

                // Act
                boolean result = trace.addReplacements(input);

                // Assert
                assertThat(result).isFalse();
                assertThat(input).isEqualTo(Set.of(Trace.RequestBodySummary, Trace.RequestBody));

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "RequestHeaders",
                    "RequestBody",
                    "RequestBodySummary",
                    "ResponseHeaders",
                    "ResponseBody",
                    "ResponseBodySummary"
            })
            void addReplacements_default_emptyInputReturnsFalse(String traceInput) {
                // Arrange
                Set<Trace> input = Collections.emptySet();
                Trace trace = Trace.valueOf(traceInput);

                // Act
                boolean result = trace.addReplacements(input);

                // Assert
                assertThat(result).isFalse();
                assertThat(input).isEmpty();

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "RequestHeaders",
                    "RequestBody",
                    "RequestBodySummary",
                    "ResponseHeaders",
                    "ResponseBody",
                    "ResponseBodySummary"
            })
            void addReplacements_default_nullInputReturnsFalse(String traceInput) {
                // Arrange
                Trace trace = Trace.valueOf(traceInput);

                // Act
                boolean result = trace.addReplacements(null);

                // Assert
                assertThat(result).isFalse();

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }
        }

        @Nested
        class CustomAddReplacementsTests {

            @Test
            void addReplacements_custom_requestWithSetReturnsFalse() {
                // Arrange
                Set<Trace> input = new HashSet<>(Set.of(Trace.RequestBody, Trace.ResponseBody));

                // Act
                boolean result = Trace.Request.addReplacements(input);

                // Assert
                assertThat(result).isTrue();
                Set<Trace> expectedSet = Set.of(
                        Trace.RequestBody,
                        Trace.ResponseBody,
                        Trace.RequestHeaders,
                        Trace.RequestBodySummary
                );
                assertThat(input).containsExactlyInAnyOrderElementsOf(expectedSet);

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }

            @Test
            void addReplacements_custom_responseWithSetReturnsFalse() {
                // Arrange
                Set<Trace> input = new HashSet<>(Set.of(Trace.RequestBody, Trace.ResponseBody));

                // Act
                boolean result = Trace.Response.addReplacements(input);

                // Assert
                assertThat(result).isTrue();
                Set<Trace> expectedSet = Set.of(
                        Trace.RequestBody,
                        Trace.ResponseBody,
                        Trace.ResponseHeaders,
                        Trace.ResponseBodySummary
                );
                assertThat(input).containsExactlyInAnyOrderElementsOf(expectedSet);

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }

            @Test
            void addReplacements_custom_requestEmptyInputReturnsFalse() {
                // Arrange
                Set<Trace> input = new HashSet<>();

                // Act
                boolean result = Trace.Request.addReplacements(input);

                // Assert
                assertThat(result).isTrue();
                assertThat(input).containsExactlyInAnyOrderElementsOf(Set.of(Trace.RequestHeaders, Trace.RequestBodySummary));

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }

            @Test
            void addReplacements_custom_responseEmptyInputReturnsFalse() {
                // Arrange
                Set<Trace> input = new HashSet<>();

                // Act
                boolean result = Trace.Response.addReplacements(input);

                // Assert
                assertThat(result).isTrue();
                assertThat(input).containsExactlyInAnyOrderElementsOf(Set.of(Trace.ResponseHeaders, Trace.ResponseBodySummary));

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }

            @ParameterizedTest
            @ValueSource(strings = {"Request", "Response"})
            void addReplacements_custom_nullInputThrowsNpe(String traceInput) {
                // Arrange
                Trace trace = Trace.valueOf(traceInput);

                // Act & Assert
                assertThatThrownBy(() -> trace.addReplacements(null))
                        .isInstanceOf(NullPointerException.class);

                // This will cause the test to fail if we ever add a new value.
                // Please remember to add the new value in these tests
                assertThat(Trace.values()).hasSize(8);
            }
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ParseTests {

        private Stream<Arguments> parseBaseCasesArguments() {
            return Stream.of(
                    Arguments.of(null, Collections.emptySet()),
                    Arguments.of("", Collections.emptySet()),
                    Arguments.of("   ", Collections.emptySet()),
                    Arguments.of("RequestBodySummary", Set.of(Trace.RequestBodySummary)),
                    Arguments.of(
                            "RequestBodySummary,ResponseBodySummary",
                            Set.of(Trace.RequestBodySummary, Trace.ResponseBodySummary)
                    ),
                    Arguments.of(
                            "RequestBodySummary, ResponseBodySummary",
                            Set.of(Trace.RequestBodySummary, Trace.ResponseBodySummary)
                    ),
                    Arguments.of(
                            "Request",
                            Set.of(Trace.RequestHeaders, Trace.RequestBodySummary)
                    ),
                    Arguments.of(
                            "Response",
                            Set.of(Trace.ResponseHeaders, Trace.ResponseBodySummary)
                    ),
                    // Partial Error Cases
                    Arguments.of("RequestBodySummary,invalid", Set.of(Trace.RequestBodySummary)),
                    Arguments.of("invalid,RequestBodySummary", Set.of(Trace.RequestBodySummary)),
                    Arguments.of("RequestBodySummary,", Set.of(Trace.RequestBodySummary))

            );
        }

        @ParameterizedTest
        @MethodSource("parseBaseCasesArguments")
        void parse_baseCases(String input, Set<Trace> expectedSet) {
            // Act
            Set<Trace> result = Trace.parse(input);

            // Assert
            assertThat(result)
                    .hasSameSizeAs(expectedSet)
                    .containsExactlyInAnyOrderElementsOf(expectedSet);
        }

        @ParameterizedTest
        @ValueSource(strings = {",", ",,", " , ", "invalid"})
        void parse_baseCases(String input) {
            // Act
            Set<Trace> result = Trace.parse(input);

            // Assert
            assertThat(result).isEmpty();
        }
    }
}
