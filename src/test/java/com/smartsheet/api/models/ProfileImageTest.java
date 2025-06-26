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

class ProfileImageTest {

    @Nested
    class BuilderTests {
        @Test
        void profileImageBuilder() {
            // Arrange
            String id = "image123";
            Long width = 800L;
            Long height = 600L;

            // Act
            ProfileImage imageNoArg = new ProfileImage();
            imageNoArg.setId(id);
            imageNoArg.setWidth(width);
            imageNoArg.setHeight(height);

            ProfileImage imageAllArg = new ProfileImage(id, width, height);

            ProfileImage imageBuilder = ProfileImage.builder()
                    .id(id)
                    .width(width)
                    .height(height)
                    .build();

            // Assert
            assertThat(imageNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(imageAllArg);

            assertThat(imageBuilder)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(imageAllArg);
        }
    }

    @Nested
    class EqualsAndHashCodeTests {
        @Test
        void equalsAndHashCode() {
            // Arrange
            ProfileImage image1 = ProfileImage.builder()
                    .id("image123")
                    .width(800L)
                    .height(600L)
                    .build();

            ProfileImage image2 = ProfileImage.builder()
                    .id("image123")
                    .width(800L)
                    .height(600L)
                    .build();

            ProfileImage image3 = ProfileImage.builder()
                    .id("image456")
                    .width(1024L)
                    .height(768L)
                    .build();

            // Assert
            assertThat(image1).isEqualTo(image2);
            assertThat(image1.hashCode()).isEqualTo(image2.hashCode());

            assertThat(image1).isNotEqualTo(image3);
            assertThat(image1.hashCode()).isNotEqualTo(image3.hashCode());
        }
    }

    @Nested
    class ToStringTests {
        @Test
        void toString_containsAllFields() {
            // Arrange
            ProfileImage image = ProfileImage.builder()
                    .id("image123")
                    .width(800L)
                    .height(600L)
                    .build();

            // Act
            String toString = image.toString();

            // Assert
            assertThat(toString).contains("id=image123");
            assertThat(toString).contains("width=800");
            assertThat(toString).contains("height=600");
        }
    }

    @Nested
    class BackwardCompatibilityTests {
        @Test
        void getImageId_returnsId() {
            // Arrange
            String id = "image123";
            ProfileImage image = ProfileImage.builder()
                    .id(id)
                    .build();

            // Act & Assert
            assertThat(image.getImageId()).isEqualTo(id);
        }
        
        // We removed the custom setImageId method that returned ProfileImage
    }
}
