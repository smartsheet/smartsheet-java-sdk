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

import com.smartsheet.api.models.enums.ReportFilterExpressionOperator;

import java.util.List;

/**
 * Report filter expression. It is a recursive object that allows at most 3 levels.
 * <p>
 * At least one of {@code criteria} or {@code nestedCriteria} has to be provided in addition to {@code operator}.
 * <p>
 * Example: A filter that matches rows where (Price > 11 AND Primary CONTAINS "PROJ-1") OR (Quantity < 12 AND "Sold Out" IS_CHECKED)
 */
public class ReportFilterExpression {

    /**
     * The boolean operator that will be applied to the list of criteria and nestedCriteria.
     */
    private ReportFilterExpressionOperator operator;

    /**
     * A recursive list of report filter expressions. Each item will be joined to the filter
     * expression with the AND/OR operator defined on this level.
     */
    private List<ReportFilterExpression> nestedCriteria;

    /**
     * Criteria objects specifying custom criteria against which to match cell values. Each item
     * will be joined to the filter expression with the AND/OR operator defined on this level.
     */
    private List<ReportFilterCriterion> criteria;

    /**
     * Gets the boolean operator.
     *
     * @return the operator
     */
    public ReportFilterExpressionOperator getOperator() {
        return operator;
    }

    /**
     * Sets the boolean operator.
     *
     * @param operator the operator
     */
    public ReportFilterExpression setOperator(ReportFilterExpressionOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Gets the nested criteria list.
     *
     * @return the nested criteria
     */
    public List<ReportFilterExpression> getNestedCriteria() {
        return nestedCriteria;
    }

    /**
     * Sets the nested criteria list.
     *
     * @param nestedCriteria the nested criteria
     */
    public ReportFilterExpression setNestedCriteria(List<ReportFilterExpression> nestedCriteria) {
        this.nestedCriteria = nestedCriteria;
        return this;
    }

    /**
     * Gets the criteria list.
     *
     * @return the criteria
     */
    public List<ReportFilterCriterion> getCriteria() {
        return criteria;
    }

    /**
     * Sets the criteria list.
     *
     * @param criteria the criteria
     */
    public ReportFilterExpression setCriteria(List<ReportFilterCriterion> criteria) {
        this.criteria = criteria;
        return this;
    }
}
