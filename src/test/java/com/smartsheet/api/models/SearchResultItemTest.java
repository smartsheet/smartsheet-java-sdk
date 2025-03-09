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

import com.smartsheet.api.models.enums.AttachmentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchResultItemTest {
    @Nested
    class BuilderTests {
        @Test
        void searchResultItemBuilder() {
            // Act
            SearchResultItem itemNoArg = SearchResultItem.builder().build();
            itemNoArg.setText("Search Text");
            itemNoArg.setObjectId(123L);
            itemNoArg.setObjectType("row");
            itemNoArg.setParentObjectId(456L);
            itemNoArg.setParentObjectType("sheet");
            itemNoArg.setParentObjectName("Sheet Name");
            itemNoArg.setContextData(List.of("Context 1", "Context 2"));
            itemNoArg.setAttachmentType(AttachmentType.FILE);
            itemNoArg.setMimeType("application/pdf");
            itemNoArg.setFavorite(true);
            itemNoArg.setParentObjectFavorite(false);

            SearchResultItem itemAllArg = SearchResultItem.builder()
                    .text("Search Text")
                    .objectId(123L)
                    .objectType("row")
                    .parentObjectId(456L)
                    .parentObjectType("sheet")
                    .parentObjectName("Sheet Name")
                    .contextData(List.of("Context 1", "Context 2"))
                    .attachmentType(AttachmentType.FILE)
                    .mimeType("application/pdf")
                    .favorite(true)
                    .parentObjectFavorite(false)
                    .build();

            // Assert
            assertThat(itemNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(itemAllArg);
        }
    }
}
