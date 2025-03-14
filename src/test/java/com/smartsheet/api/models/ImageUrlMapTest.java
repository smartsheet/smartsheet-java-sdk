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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ImageUrlMapTest {
    @Nested
    class BuilderTests {
        @Test
        void imageUrlMapBuilder() {
            // Arrange
            List<ImageUrl> imageUrls = List.of(ImageUrl.builder().build());

            // Act
            ImageUrlMap imageUrlMapNoArg = ImageUrlMap.builder().build();
            imageUrlMapNoArg.setUrlExpiresInMillis(3600000L);
            imageUrlMapNoArg.setImageUrls(imageUrls);

            ImageUrlMap imageUrlMapAllArg = ImageUrlMap.builder()
                    .urlExpiresInMillis(3600000L)
                    .imageUrls(imageUrls)
                    .build();

            // Assert
            assertThat(imageUrlMapNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(imageUrlMapAllArg);
        }
    }
}
