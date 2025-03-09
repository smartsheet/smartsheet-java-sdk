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

import com.smartsheet.api.internal.util.QueryUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class DeleteUserParameters {
    /**
     * Represents the ID of the user to transfer ownership to
     */
    private Long transferToId;

    /**
     * Determines whether or not to transfer sheets
     */
    private Boolean transferSheets;

    /**
     * Determines to remove the user from sharing for all sheets/workspaces in the organization
     */
    private Boolean removeFromSharing;

    /**
     * Convert to a query string
     */
    public String toQueryString() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("transferTo", transferToId);
        parameters.put("transferSheets", transferSheets);
        parameters.put("removeFromSharing", removeFromSharing);

        return QueryUtil.generateUrl(null, parameters);
    }
}
