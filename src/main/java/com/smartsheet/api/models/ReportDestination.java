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

import com.smartsheet.api.models.enums.ReportDestinationType;

/**
 * Represents the destination container when creating a report.
 */
public class ReportDestination {

    /**
     * The ID of the destination container (folder or workspace).
     */
    private Long destinationId;

    /**
     * The type of the destination container.
     */
    private ReportDestinationType destinationType;

    /**
     * Gets the destination ID.
     *
     * @return the destination ID
     */
    public Long getDestinationId() {
        return destinationId;
    }

    /**
     * Sets the destination ID.
     *
     * @param destinationId the ID of the destination folder or workspace
     * @return this ReportDestination object for method chaining
     */
    public ReportDestination setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
        return this;
    }

    /**
     * Gets the destination type.
     *
     * @return the destination type
     */
    public ReportDestinationType getDestinationType() {
        return destinationType;
    }

    /**
     * Sets the destination type.
     *
     * @param destinationType the type of destination (FOLDER or WORKSPACE)
     * @return this ReportDestination object for method chaining
     */
    public ReportDestination setDestinationType(ReportDestinationType destinationType) {
        this.destinationType = destinationType;
        return this;
    }
}
