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

import com.smartsheet.api.models.enums.AutomationRuleDisabledReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AutomationRule {
    /**
     * Represents the ID.
     */
    private Long id;

    /**
     * Represents the name.
     */
    private String name;

    /**
     * AutomationAction object containing information for this rule.
     */
    private AutomationAction action;

    /**
     * A timestamp of when the rule was originally active.
     */
    private Date createdAt;

    /**
     * A User object containing the name and email of the creator of this rule.
     */
    private User createdBy;

    /**
     * Machine-readable reason a rule is disabled.
     */
    private AutomationRuleDisabledReason disabledReason;

    /**
     * Descriptive reason rule is disabled.
     */
    private String disabledReasonText;

    /**
     * Indicates if the rule is active
     */
    private Boolean enabled;

    /**
     * A timestamp indicating when the last change was made to the rule.
     */
    private Date modifiedAt;

    /**
     * User object containing the name and email of the user who last modified this rule.
     */
    private User modifiedBy;

    /**
     * Indicates that the current user can modify this rule.
     */
    private Boolean userCanModify;
}
