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

package com.smartsheet.api.internal;

import com.smartsheet.api.GovernanceResources;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.util.QueryUtil;
import com.smartsheet.api.models.DataClassificationSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the implementation of GovernanceResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 */
public class GovernanceResourcesImpl extends AbstractResources implements GovernanceResources {

    /**
     * Constructor.
     *
     * @param smartsheet the smartsheet
     */
    public GovernanceResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * Get the data classification settings for a plan.
     *
     * @param planId the ID of the plan
     * @return the DataClassificationSettings
     * @throws SmartsheetException if there is any other error during the operation
     */
    @Override
    public DataClassificationSettings getDataClassificationSettings(long planId) throws SmartsheetException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("planId", planId);
        return this.getResource(
                "governance/data-classification/settings" + QueryUtil.generateUrl(null, parameters),
                DataClassificationSettings.class
        );
    }
}
