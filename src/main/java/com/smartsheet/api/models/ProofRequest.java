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

import com.smartsheet.api.models.enums.ProofStatus;

import java.util.List;

/**
 * Class representing the proof request object.
 */
public class ProofRequest extends IdentifiableModel<Long> {
    /**
     * Proof Id of the original proof
     */
    private Long proofId;

    /**
     * User object containing name and email of the sender
     */
    private User sentBy;

    /**
     *
     */
    private Object sentAt;
    /**
     * Indicates whether to send a copy of the email to the sender
     */
    private boolean isDownloadable;

    /**
     * Indicates the proof request status.
     */
    private ProofStatus status;

    /**
     * Indicates whether to send a copy of the email to the sender.
     */
    private boolean ccMe;

    /**
     * The message of the email.
     */
    private String message;

    /**
     * Array of recipients of the email.
     */
    private List<Recipient> sendTo;

    /**
     * The subject of the email.
     */
    private String subject;

    public Long getProofId() {
        return proofId;
    }

    public void setProofId(Long proofId) {
        this.proofId = proofId;
    }

    public User getSentBy() {
        return sentBy;
    }

    public void setSentBy(User sentBy) {
        this.sentBy = sentBy;
    }

    public Object getSentAt() {
        return sentAt;
    }

    public void setSentAt(Object sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        isDownloadable = downloadable;
    }

    public ProofStatus getStatus() {
        return status;
    }

    public void setStatus(ProofStatus status) {
        this.status = status;
    }

    public boolean isCcMe() {
        return ccMe;
    }

    public void setCcMe(boolean ccMe) {
        this.ccMe = ccMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Recipient> getSendTo() {
        return sendTo;
    }

    public void setSendTo(List<Recipient> sendTo) {
        this.sendTo = sendTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
