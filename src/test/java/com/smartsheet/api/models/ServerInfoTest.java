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

class ServerInfoTest {
    @Nested
    class BuilderTests {
        @Test
        void serverInfoBuilder() {
            // Arrange
            FormatTables formats = FormatTables.builder().build();
            FeatureInfo featureInfo = FeatureInfo.builder().build();

            // Act
            ServerInfo serverInfoNoArg = ServerInfo.builder().build();
            serverInfoNoArg.setSupportedLocales(List.of("en_US", "fr_FR", "de_DE"));
            serverInfoNoArg.setFormats(formats);
            serverInfoNoArg.setFeatureInfo(featureInfo);

            ServerInfo serverInfoAllArg = ServerInfo.builder()
                    .supportedLocales(List.of("en_US", "fr_FR", "de_DE"))
                    .formats(formats)
                    .featureInfo(featureInfo)
                    .build();

            // Assert
            assertThat(serverInfoNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(serverInfoAllArg);
        }
    }
}
