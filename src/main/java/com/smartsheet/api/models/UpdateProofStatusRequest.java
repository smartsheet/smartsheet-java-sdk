package com.smartsheet.api.models;

/**
 * Represents the request sent to update proof status.
 */
public class UpdateProofStatusRequest {

    /**
     * Indicates whether the proof is completed.
     */
    private boolean isCompleted;

    /**
     * Returns whether proof status is completed.
     * @return
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Sets the proof status.
     * @param completed
     */
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
