package com.smartsheet.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListUsersWithFilters extends UserModelWithName {
    private String planId;
    private String seatType;
    private String seatTypeLastChangedAt;
    private boolean isInternal;

    @JsonProperty("planId")
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @JsonProperty("seatType")
    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    @JsonProperty("seatTypeLastChangedAt")
    public String getSeatTypeLastChangedAt() {
        return seatTypeLastChangedAt;
    }

    public void setSeatTypeLastChangedAt(String seatTypeLastChangedAt) {
        this.seatTypeLastChangedAt = seatTypeLastChangedAt;
    }

    @JsonProperty("isInternal")
    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }
}
