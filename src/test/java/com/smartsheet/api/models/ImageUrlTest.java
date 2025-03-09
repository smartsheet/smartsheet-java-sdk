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

class ImageUrlTest {
    @Nested
    class BuilderTests {
        @Test
        void imageUrlBuilder() {
            // Arrange
            Error error = Error.builder().build();

            // Act
            ImageUrl imageUrlNoArg = ImageUrl.builder().build();
            imageUrlNoArg.setImageId("imageId");
            imageUrlNoArg.setWidth(100L);
            imageUrlNoArg.setHeight(200L);
            imageUrlNoArg.setUrl("https://example.com/image.jpg");
            imageUrlNoArg.setError(error);

            ImageUrl imageUrlAllArg = ImageUrl.builder()
                    .imageId("imageId")
                    .width(100L)
                    .height(200L)
                    .url("https://example.com/image.jpg")
                    .error(error)
                    .build();

            // Assert
            assertThat(imageUrlNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(imageUrlAllArg);
        }
    }
}
