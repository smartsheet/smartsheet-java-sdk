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

import com.smartsheet.api.models.enums.ProofType;

import java.util.Date;
import java.util.List;

/**
 * Represents the Proof object. A proof is a container that holds attachments and comments for review, editing, or
 * approval.
 */
public class Proof extends NamedModel<Long> {

    /**
     * Represents the ID of the original proof version.
     */
    private Long originalId;

    /**
     * Represents the type of the proof.
     */
    private ProofType type;

    /**
     * Represents the document type of the proof.
     */
    private String documentType;

    /**
     * Represents the URL used to request the proof.
     */
    private String proofRequestUrl;

    /**
     * Represents the version number of the proof.
     */
    private Integer version;

    /**
     * Represents the date and time the proof was last updated.
     */
    private Date lastUpdatedAt;

    /**
     * The user who last updated the proof.
     */
    private User lastUpdatedBy;

    /**
     * Indicates whether the proof is completed.
     */
    private Boolean isCompleted;

    /**
     * Represents the attachments associated with the proof.
     */
    private List<Attachment> attachments;

    /**
     * Represents the discussions associated with the proof.
     */
    private List<Discussion> discussions;

    /**
     * Provide an 'override' of setName (returns Proof not NamedModel)
     *
     * @param name the new name
     */
    public Proof setName(String name) {
        super.setName(name);
        return this;
    }

    /**
     * Gets the ID of the original proof version.
     *
     * @return the original id
     */
    public Long getOriginalId() {
        return originalId;
    }

    /**
     * Sets the ID of the original proof version.
     *
     * @param originalId the new original id
     */
    public Proof setOriginalId(Long originalId) {
        this.originalId = originalId;
        return this;
    }

    /**
     * Gets the type of the proof.
     *
     * @return the proof type
     */
    public ProofType getType() {
        return type;
    }

    /**
     * Sets the type of the proof.
     *
     * @param type the new proof type
     */
    public Proof setType(ProofType type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the document type of the proof.
     *
     * @return the document type
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Sets the document type of the proof.
     *
     * @param documentType the new document type
     */
    public Proof setDocumentType(String documentType) {
        this.documentType = documentType;
        return this;
    }

    /**
     * Gets the URL used to request the proof.
     *
     * @return the proof request url
     */
    public String getProofRequestUrl() {
        return proofRequestUrl;
    }

    /**
     * Sets the URL used to request the proof.
     *
     * @param proofRequestUrl the new proof request url
     */
    public Proof setProofRequestUrl(String proofRequestUrl) {
        this.proofRequestUrl = proofRequestUrl;
        return this;
    }

    /**
     * Gets the version number of the proof.
     *
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the version number of the proof.
     *
     * @param version the new version
     */
    public Proof setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * Gets the date and time the proof was last updated.
     *
     * @return the last updated at
     */
    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    /**
     * Sets the date and time the proof was last updated.
     *
     * @param lastUpdatedAt the new last updated at
     */
    public Proof setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    /**
     * Gets the user who last updated the proof.
     *
     * @return the last updated by user
     */
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the user who last updated the proof.
     *
     * @param lastUpdatedBy the new last updated by user
     */
    public Proof setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    /**
     * Indicates whether the proof is completed.
     *
     * @return the completed status
     */
    public Boolean getIsCompleted() {
        return isCompleted;
    }

    /**
     * Sets whether the proof is completed.
     *
     * @param isCompleted the new completed status
     */
    public Proof setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    /**
     * Gets the attachments associated with the proof.
     *
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * Sets the attachments associated with the proof.
     *
     * @param attachments the new attachments
     */
    public Proof setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    /**
     * Gets the discussions associated with the proof.
     *
     * @return the discussions
     */
    public List<Discussion> getDiscussions() {
        return discussions;
    }

    /**
     * Sets the discussions associated with the proof.
     *
     * @param discussions the new discussions
     */
    public Proof setDiscussions(List<Discussion> discussions) {
        this.discussions = discussions;
        return this;
    }
}
