package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.ProofStatus;

import java.util.List;

/**
 *
 */
public class ProofRequest extends IdentifiableModel<Long>{

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
}
