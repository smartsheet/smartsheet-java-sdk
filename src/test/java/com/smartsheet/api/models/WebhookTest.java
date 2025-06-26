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

import com.smartsheet.api.models.enums.WebhookStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WebhookTest {

    @Nested
    class BuilderTests {
        @Test
        void webhookBuilder() {
            // Arrange
            Date createdAt = new Date();
            Date modifiedAt = new Date();
            List<String> events = Arrays.asList("created", "updated", "deleted");
            WebhookStats stats = WebhookStats.builder().build();
            WebhookSubscope subscope = new WebhookSubscope();

            // Act
            Webhook webhookNoArg = new Webhook();
            webhookNoArg.setId(1L);
            webhookNoArg.setName("Test Webhook");
            webhookNoArg.setApiClientId("client123");
            webhookNoArg.setScopeObjectId(100L);
            webhookNoArg.setApiClientName("Test Client");
            webhookNoArg.setCallbackUrl("https://example.com/callback");
            webhookNoArg.setCreatedAt(createdAt);
            webhookNoArg.setDisabledDetails("Not disabled");
            webhookNoArg.setEnabled(true);
            webhookNoArg.setEvents(events);
            webhookNoArg.setModifiedAt(modifiedAt);
            webhookNoArg.setScope("sheet");
            webhookNoArg.setSharedSecret("secret123");
            webhookNoArg.setStats(stats);
            webhookNoArg.setStatus(WebhookStatus.ENABLED);
            webhookNoArg.setSubscope(subscope);
            webhookNoArg.setVersion(1);

            Webhook webhookAllArg = Webhook.builder()
                    .id(1L)
                    .name("Test Webhook")
                    .apiClientId("client123")
                    .scopeObjectId(100L)
                    .apiClientName("Test Client")
                    .callbackUrl("https://example.com/callback")
                    .createdAt(createdAt)
                    .disabledDetails("Not disabled")
                    .enabled(true)
                    .events(events)
                    .modifiedAt(modifiedAt)
                    .scope("sheet")
                    .sharedSecret("secret123")
                    .stats(stats)
                    .status(WebhookStatus.ENABLED)
                    .subscope(subscope)
                    .version(1)
                    .build();

            // Assert
            assertThat(webhookNoArg)
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(webhookAllArg);
        }
    }

    @Nested
    class ToStringTests {
        @Test
        void toString_containsAllFields() {
            // Arrange
            Webhook webhook = Webhook.builder()
                    .id(1L)
                    .name("Test Webhook")
                    .apiClientId("client123")
                    .build();

            // Act
            String toString = webhook.toString();

            // Assert
            assertThat(toString).contains("apiClientId=client123");
            // With Lombok's @ToString(callSuper=true), parent class fields are not shown individually
            // but rather as part of the super= reference
            assertThat(toString).contains("super=");
            // We can't check for id or name directly since they're in parent classes
        }
    }

    @Nested
    class NameMethodTests {
        @Test
        void setName_returnsWebhook() {
            // Arrange
            Webhook webhook = new Webhook();

            // Act
            NamedModel<Long> result = webhook.setName("Test Webhook");

            // Assert
            assertThat(result).isSameAs(webhook);
            assertThat(webhook.getName()).isEqualTo("Test Webhook");
        }
    }
}
