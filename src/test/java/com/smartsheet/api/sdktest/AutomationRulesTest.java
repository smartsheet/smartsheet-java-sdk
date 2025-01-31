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

package com.smartsheet.api.sdktest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.AutomationAction;
import com.smartsheet.api.models.AutomationRule;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.enums.AutomationActionFrequency;
import com.smartsheet.api.models.enums.AutomationActionType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AutomationRulesTest {

    @Test
    void listAutomationRules() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Automation Rules");
        PagedResult<AutomationRule> automationRules = ss.sheetResources().automationRuleResources().listAutomationRules(324, null);
        assertThat(automationRules.getTotalCount()).isEqualTo(2);
    }

    @Test
    void getAutomationRule() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Automation Rule");
        AutomationRule automationRule = ss.sheetResources().automationRuleResources().getAutomationRule(324, 284);
        assertThat(automationRule.getId()).isEqualTo(284);
    }

    @Disabled("awaiting API update to return Result object")
    @Test
    void updateAutomationRule() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Automation Rule");
        AutomationAction autoRuleAction = new AutomationAction();
        RecipientEmail recipient = new RecipientEmail();
        recipient.setEmail("jane@example.com");
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(recipient);
        autoRuleAction.setRecipients(recipients);
        autoRuleAction.setType(AutomationActionType.NOTIFICATION_ACTION);
        autoRuleAction.setFrequency(AutomationActionFrequency.WEEKLY);
        AutomationRule autoRule = new AutomationRule();
        autoRule.setId(284L);
        autoRule.setAction(autoRuleAction);
        AutomationRule automationRule = ss.sheetResources().automationRuleResources().updateAutomationRule(324, autoRule);
    }

    @Test
    void deleteAutomationRule() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Delete Automation Rule");
        ss.sheetResources().automationRuleResources().deleteAutomationRule(324, 284);
    }
}
