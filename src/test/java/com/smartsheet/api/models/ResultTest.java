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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {
    @Nested
    class BuilderTests {
        @Test
        void resultBuilder() {
            // Arrange

            // Act
            Result<String> resultNoArg = Result.<String>builder().build();
            resultNoArg.setResult("Test Result");
            resultNoArg.setResultCode(200);
            resultNoArg.setMessage("Success");
            resultNoArg.setVersion(1);

            Result<String> resultAllArg = Result.<String>builder()
                    .result("Test Result")
                    .resultCode(200)
                    .message("Success")
                    .version(1)
                    .build();

            // Assert
            assertThat(resultNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(resultAllArg);
        }
    }
}
