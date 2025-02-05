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

class WorkspaceCopyInclusionTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToStringTests {
        @ParameterizedTest
        @MethodSource("toStringArguments")
        void toString(WorkspaceCopyInclusion workspaceCopyInclusion, String expectedString) {
            // Act
            String result = workspaceCopyInclusion.toString();

            // Assert
            assertThat(result).isEqualTo(expectedString);

            // This will cause the test to fail if we ever add a new value.
            // Please remember to add the new value in the method below
            assertThat(WorkspaceCopyInclusion.values()).hasSize(11);
        }

        private Stream<Arguments> toStringArguments() {
            return Stream.of(
                    Arguments.of(WorkspaceCopyInclusion.ATTACHMENTS, "attachments"),
                    Arguments.of(WorkspaceCopyInclusion.BRAND, "brand"),
                    Arguments.of(WorkspaceCopyInclusion.CELLLINKS, "cellLinks"),
                    Arguments.of(WorkspaceCopyInclusion.DATA, "data"),
                    Arguments.of(WorkspaceCopyInclusion.DISCUSSIONS, "discussions"),
                    Arguments.of(WorkspaceCopyInclusion.FILTERS, "filters"),
                    Arguments.of(WorkspaceCopyInclusion.FORMS, "forms"),
                    Arguments.of(WorkspaceCopyInclusion.RULERECIPIENTS, "ruleRecipients"),
                    Arguments.of(WorkspaceCopyInclusion.RULES, "rules"),
                    Arguments.of(WorkspaceCopyInclusion.SHARES, "shares"),
                    Arguments.of(WorkspaceCopyInclusion.ALL, "all")
            );
        }
    }
}
