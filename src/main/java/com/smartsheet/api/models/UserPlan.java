package com.smartsheet.api.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartsheet.api.models.enums.SeatType;

/**
 * Represents the UserPlan object.
 */
public class UserPlan {
    private long planId;
    private SeatType seatType;
    @JsonProperty
    private Date seatTypeLastChangedAt;
    private boolean isInternal;

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public Date seatTypeLastChangedAt() {
        return seatTypeLastChangedAt;
    }

    public void seatTypeLastChangedAt(Date seatTypeLastChangedAt) {
        this.seatTypeLastChangedAt = seatTypeLastChangedAt;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setIsInternal(boolean isInternal) {
        this.isInternal = isInternal;
    }
}
