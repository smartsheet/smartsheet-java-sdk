/*
 * Copyright (C) 2023 Smartsheet
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

import com.smartsheet.api.models.enums.AutomationActionFrequency;
import com.smartsheet.api.models.enums.AutomationActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AutomationAction {

    /**
     * The frequency to apply this automation action
     */
    private AutomationActionFrequency frequency;

    /**
     * Include all columns in email contents
     */
    private Boolean includeAllColumns;

    /**
     * include attachments in email
     */
    private Boolean includeAttachments;

    /**
     * include discussions in email
     */
    private Boolean includeDiscussions;

    /**
     * specifies which columns to include in message
     */
    private List<Long> includedColumnIds;

    /**
     * Email body
     */
    private String message;

    /**
     * notifications are sent to all users shared to the sheet
     */
    private Boolean notifyAllSharedUsers;

    /**
     * List of column ids from which to collect email recipients
     */
    private List<Long> recipientColumnIds;

    /**
     * List of Recipients
     */
    private List<Recipient> recipients;

    /**
     * Email subject line
     */
    private String subject;

    /**
     * AutomationActionType
     */
    private AutomationActionType type;
}
