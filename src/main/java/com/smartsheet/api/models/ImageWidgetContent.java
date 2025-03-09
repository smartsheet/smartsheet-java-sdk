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

import com.smartsheet.api.models.enums.WidgetType;
import com.smartsheet.api.models.format.Format;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class ImageWidgetContent implements WidgetContent {

    /**
     * The image private Id
     */
    private String privateId;

    /**
     * Name of the image file
     */
    private String fileName;

    /**
     * Format Descriptor
     *
     * @see <a href="https://smartsheet.redoc.ly/#section/API-Basics/Formatting">FormatDescriptor</a>
     */
    private Format format;

    /**
     * Original height of the image in pixels
     */
    private Integer height;

    /**
     * The widget has when clicked attribute set to that hyperlink (if present and non-null)
     */
    private WidgetHyperlink hyperlink;

    /**
     * Original width of the image in pixels
     */
    private Integer width;

    /**
     * Returns the type for this widget content object
     *
     * @return IMAGE
     */
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.IMAGE;
    }
}
