package com.smartsheet.api.models;

/*
 * #[license]
 * Smartsheet Java SDK
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

import com.smartsheet.api.models.enums.ProofType;

import java.util.Date;
import java.util.List;

/**
 * Class representing the proof object.
 */
public class Proof extends NamedModel<Long> {
    /**
     * Proof id of the original proof version.
     */
    private Long originalId;

    /**
     * File type for the proof version.
     */
    private ProofType proofType;

    /**
     * URL to review a proofing request.
     */
    private String proofRequestUrl;

    /**
     * The version number of the proof.
     */
    private Long version;

    /**
     * The last update timestamp.
     */
    private Date lastUpdatedAt;

    /**
     * User object containing name and email of the user who last updated the proof
     */
    private User lastUpdatedBy;

    /**
     * Indicates whether the proof is completed.
     */
    private boolean isCompleted;

    /**
     * List of Attachment objects. Only returned if the include query string parameter contains attachments
     */
    private List<Attachment> attachments;

    /**
     * List of Discussion objects. Only returned if the include query string parameter contains discussions.
     */
    private List<Discussion> discussions;


    /**
     * Provide an 'override' of setName (returns Proof not NamedModel)
     *
     * @param name the new name
     */
    public Proof setName(String name){
        super.setName(name);
        return this;
    }

    /**
     * Get the original id.
     *
     * @return id.
     */
    public Long getOriginalId() {
        return originalId;
    }

    /**
     * Sets the original id.
     *
     * @param originalId
     */
    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public ProofType getProofType() {
        return proofType;
    }

    public void setProofType(ProofType proofType) {
        this.proofType = proofType;
    }

    public String getProofRequestUrl() {
        return proofRequestUrl;
    }

    public void setProofRequestUrl(String proofRequestUrl) {
        this.proofRequestUrl = proofRequestUrl;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<Discussion> discussions) {
        this.discussions = discussions;
    }
}
