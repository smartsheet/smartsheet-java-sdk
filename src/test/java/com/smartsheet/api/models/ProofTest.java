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

import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import com.smartsheet.api.models.enums.ProofType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProofTest {

    @Test
    void testProofGettersAndSetters() {
        Date now = new Date();
        User updatedBy = new User();
        updatedBy.setEmail("user@smartsheet.com");
        List<Attachment> attachments = new ArrayList<>();
        List<Discussion> discussions = new ArrayList<>();

        Proof proof = new Proof()
                .setOriginalId(123L)
                .setType(ProofType.IMAGE)
                .setDocumentType("PNG")
                .setProofRequestUrl("https://app.smartsheet.com/proofs/123")
                .setVersion(2)
                .setLastUpdatedAt(now)
                .setLastUpdatedBy(updatedBy)
                .setIsCompleted(true)
                .setAttachments(attachments)
                .setDiscussions(discussions);
        proof.setId(456L);
        proof.setName("My proof");

        assertThat(proof.getId()).isEqualTo(456L);
        assertThat(proof.getName()).isEqualTo("My proof");
        assertThat(proof.getOriginalId()).isEqualTo(123L);
        assertThat(proof.getType()).isEqualTo(ProofType.IMAGE);
        assertThat(proof.getDocumentType()).isEqualTo("PNG");
        assertThat(proof.getProofRequestUrl()).isEqualTo("https://app.smartsheet.com/proofs/123");
        assertThat(proof.getVersion()).isEqualTo(2);
        assertThat(proof.getLastUpdatedAt()).isEqualTo(now);
        assertThat(proof.getLastUpdatedBy()).isEqualTo(updatedBy);
        assertThat(proof.getIsCompleted()).isTrue();
        assertThat(proof.getAttachments()).isSameAs(attachments);
        assertThat(proof.getDiscussions()).isSameAs(discussions);
    }

    @Test
    void testProofSerializesTypeAndIsCompletedWithWireNames() throws Exception {
        Proof proof = new Proof().setType(ProofType.DOCUMENT).setIsCompleted(false);

        String json = new JacksonJsonSerializer().serialize(proof);

        assertThat(json).contains("\"type\":\"DOCUMENT\"");
        assertThat(json).contains("\"isCompleted\":false");
    }
}
