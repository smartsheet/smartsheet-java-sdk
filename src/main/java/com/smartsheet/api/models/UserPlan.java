package com.smartsheet.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the UserPlan object.
 */
public class UserPlan {
    private String planId;
    private String seatType;
    @JsonProperty
    private String seatTypeLastChangedAt;
    private boolean isInternal;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String seatTypeLastChangedAt() {
        return seatTypeLastChangedAt;
    }

    public void seatTypeLastChangedAt(String seatTypeLastChangedAt) {
        this.seatTypeLastChangedAt = seatTypeLastChangedAt;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setIsInternal(boolean isInternal) {
        this.isInternal = isInternal;
    }
}
