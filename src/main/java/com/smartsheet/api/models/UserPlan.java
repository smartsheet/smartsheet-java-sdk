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
    private SeatType seatType;
    private ZonedDateTime seatTypeLastChangedAt;
    private Boolean isInternal;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
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

    public Boolean getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }
}
