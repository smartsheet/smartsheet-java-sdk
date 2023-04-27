package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.ProofActionStatus;

/**
 *
 */
public class ProofRequestAction {

    /**
     * User performing the action.
     */
    private User user;

    /**
     * Status of the proof request action.
     */
    private ProofActionStatus actionStatus;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProofActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(ProofActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }
}
