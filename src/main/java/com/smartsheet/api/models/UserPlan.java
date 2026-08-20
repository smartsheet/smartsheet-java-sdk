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

import java.time.ZonedDateTime;
import com.smartsheet.api.models.enums.SeatType;

/**
 * Represents the UserPlan object.
 */
public class UserPlan {
    private Long planId;
    private String planName;
    private SeatType seatType;
    private ZonedDateTime seatTypeLastChangedAt;
    private ZonedDateTime provisionalExpirationDate;
    private Boolean isInternal;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    /**
     * <p>The name of the organization that owns this plan.</p>
     *
     * <p>Only populated when {@code planName} was requested via the {@code include} parameter of
     * {@code listUserPlans}. It is also null for a plan whose owning organization has no name, so
     * a null value does not by itself mean the enrichment was not requested.</p>
     *
     * <p>Organization names are cached server side for several hours, so a recently renamed
     * organization may briefly report its previous name.</p>
     *
     * @return the plan name, or null
     */
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public ZonedDateTime getSeatTypeLastChangedAt() {
        return seatTypeLastChangedAt;
    }

    public void setSeatTypeLastChangedAt(ZonedDateTime seatTypeLastChangedAt) {
        this.seatTypeLastChangedAt = seatTypeLastChangedAt;
    }

    public ZonedDateTime getProvisionalExpirationDate() {
        return provisionalExpirationDate;
    }

    public void setProvisionalExpirationDate(ZonedDateTime provisionalExpirationDate) {
        this.provisionalExpirationDate = provisionalExpirationDate;
    }

    public Boolean getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }
}
