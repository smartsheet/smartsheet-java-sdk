/*
 * Copyright (C) 2024 Smartsheet
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
public class SightPublish {

    /**
     * If true, a rich version of the Sight is published with the ability to use shortcuts and widget interactions.
     */
    private Boolean readOnlyFullEnabled;

    /**
     * Flag to indicate who can access the 'Read-Only Full' view of the published Sight
     * If "ALL", it is available to anyone who has the link.
     * If "ORG", it is available only to members of the Sight owner's Smartsheet organization.
     */
    private String readOnlyFullAccessibleBy;

    /**
     * URL for 'Read-Only Full' view of the published Sight.
     */
    private String readOnlyFullUrl;
}
