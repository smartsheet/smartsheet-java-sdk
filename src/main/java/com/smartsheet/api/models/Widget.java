package com.smartsheet.api.models;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

import com.fasterxml.jackson.annotation.JsonSetter;
import com.smartsheet.api.models.enums.WidgetType;
import com.smartsheet.api.models.format.Format;

public class Widget extends IdentifiableModel<Long> {

    /**
     * Tyep of widget
     */
    private WidgetType type;

    /**
     * (Optional) Title of widget
     */
    private String title;

    /**
     * True indicates that the client should display the widget title.
     */
    private Boolean showTitle;

    /**
     * True indicates that the client should display the sheet icon in the widget
     */
    private Boolean showTitleIcon;

    /**
     * Contains the title format descriptor
     */
    private Format titleFormat;

    /**
     * X-coordinate of widget's position on the Sight
     */
    private Integer xPosition;

    /**
     * Y-coordinate of widget's position on the Sight
     */
    private Integer yPosition;

    /**
     * Number of rows that the widget occupies
     */
    private Integer height;

    /**
     * Number of columns that the widget occupies
     */
    private Integer width;

    /**
     * Widget version number
     */
    private Integer version;

    /**
     * Data that specifies the contents of the widget.
     * _Note: the type of WidgetContent object (and attributes within) will depend on the value of Widget.
     */
    private WidgetContent contents;

    /**
     * Present when the widget is in an error state.
     */
    private Error error;

    /**
     * Get the type of the widget
     *
     * @return type
     */
    public WidgetType getType() {
        return type;
    }

    /**
     * Set the type of the widget
     */
    public Widget setType(WidgetType type) {
        this.type = type;
        return this;
    }

    /**
     * Get the title of the widget.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the widget.
     */
    public Widget setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get flag indicating if the client should display the widget title.
     *
     * @return showTitle flag
     */
    public Boolean getShowTitle() {
        return showTitle;
    }

    /**
     * Set flag indicating if the client should display the widget title.
     */
    public Widget setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
        return this;
    }

    /**
     * Get flag indicating if the client should display the sheet icon.
     *
     * @return showTitleIcon flag
     */
    public Boolean getShowTitleIcon() {
        return showTitleIcon;
    }

    /**
     * Set flag indicating if the client should display the sheet icon.
     */
    public Widget setShowTitleIcon(Boolean showTitleIcon) {
        this.showTitleIcon = showTitleIcon;
        return this;
    }

    /**
     * Get the title format descriptor string
     *
     * @return titleFormat
     */
    public Format getTitleFormat() {
        return titleFormat;
    }

    /**
     * Set the title format descriptor string
     */
    public Widget setTitleFormat(Format titleFormat) {
        this.titleFormat = titleFormat;
        return this;
    }

    /**
     * Get the x-coordinate of widget's position on the sight.
     *
     * @return xPosition
     */
    public Integer getXPosition() {
        return xPosition;
    }

    /**
     * Set the x-coordinate of widget's position on the sight.
     */
    @JsonSetter("xPosition")
    public Widget setXPosition(Integer xPosition) {
        this.xPosition = xPosition;
        return this;
    }

    /**
     * Get the y-coordinate of widget's position on the sight.
     *
     * @return yPosition
     */
    public Integer getYPosition() {
        return yPosition;
    }

    /**
     * Set the y-coordinate of widget's position on the sight.
     */
    @JsonSetter("yPosition")
    public Widget setYPosition(Integer yPosition) {
        this.yPosition = yPosition;
        return this;
    }

    /**
     * Get the number of rows that the widget occupies
     *
     * @return height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Set the number of rows that the widget occupies
     */
    public Widget setHeight(Integer height) {
        this.height = height;
        return this;
    }

    /**
     * Get the number of columns that the widget occupies
     *
     * @return width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Set the number of columns that the widget occupies
     */
    public Widget setWidth(Integer width) {
        this.width = width;
        return this;
    }

    /**
     * Get the widget version number
     *
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Set the widget version number
     */
    public Widget setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * Get the data that specifies the contents of the widget.
     * See description of contents variable for valid objects
     *
     * @return contents
     */
    public WidgetContent getContents() {
        return contents;
    }

    /**
     * Set the data that specifies the contents of the widget.
     * See description of contents variable for valid objects
     */
    public Widget setContents(WidgetContent contents) {
        this.contents = contents;
        return this;
    }

    /**
     * Gets an Error object if the widget is in an error state
     *
     * @return error
     */
    public Error getError() {
        return error;
    }

    /**
     * Sets an Error object when the widget is in an error state
     */
    public Widget setError(Error error) {
        this.error = error;
        return this;
    }
}
