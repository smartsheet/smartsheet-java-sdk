package com.smartsheet.api.models;

import java.util.List;

public class UserPlansResponse {
    private List<UserPlan> data;
    private String lastKey;

    public List<UserPlan> getData() {
        return data;
    }

    public void setData(List<UserPlan> data) {
        this.data = data;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }
}
