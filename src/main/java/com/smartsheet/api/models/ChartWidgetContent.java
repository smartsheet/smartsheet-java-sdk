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
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class ChartWidgetContent implements WidgetContent {

    /**
     * Report Id denoting container source, if applicable
     */
    private Long reportId;

    /**
     * Sheet Id denoting container source, if applicable
     */
    private Long sheetId;

    /**
     * Array of Axes
     */
    private List<Object> axes;

    /**
     * The widget has when clicked attribute set to that hyperlink (if present and non-null)
     */
    private WidgetHyperlink hyperlink;

    /**
     * Array of columnIds if the range was selected through the UI
     */
    private List<Long> includedColumnIds;

    /**
     * The location in the widget where Smartsheet renders the legend, for example, RIGHT
     */
    private Object legend;

    /**
     * selection range if the source is a sheet
     */
    private List<SelectionRange> selectionRanges;

    /**
     * Array of Series objects
     */
    private List<Object> series;

    /**
     * Returns the type for this widget content object
     *
     * @return CHART
     */
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.CHART;
    }
}
