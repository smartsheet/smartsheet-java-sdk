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

package com.smartsheet.api.internal;

import com.smartsheet.api.InvalidRequestException;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Webhook;
import com.smartsheet.api.models.WebhookSharedSecret;
import com.smartsheet.api.models.enums.WebhookStatus;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class WebhookResourcesImplTest extends ResourcesImplBase {

    private WebhookResourcesImpl webhookResources;

    @BeforeEach
    public void setUp() throws Exception {
        webhookResources = new WebhookResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListWebhooks() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/listWebhooks.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);

        PagedResult<Webhook> webhookPagedResults = webhookResources.listWebhooks(parameters);

        assertThat(webhookPagedResults.getData().size()).isEqualTo(2);
        assertThat(webhookPagedResults.getData().get(0).getApiClientId()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(1).getApiClientId()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(0).getVersion()).isEqualTo(1);
        assertThat(webhookPagedResults.getData().get(1).getVersion()).isEqualTo(1);
        assertThat(webhookPagedResults.getData().get(1).getApiClientId()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(0).getScope()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(1).getScope()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(0).getApiClientName()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(1).getApiClientName()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(0).getCallbackUrl()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(1).getCallbackUrl()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(0).getSharedSecret()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(1).getSharedSecret()).isNotBlank();
        assertThat(webhookPagedResults.getData().get(0).getEvents().size()).isEqualTo(2);
        assertThat(webhookPagedResults.getData().get(1).getEvents().size()).isEqualTo(0);
        assertThat(webhookPagedResults.getData().get(0).isEnabled()).isFalse();
        assertThat(webhookPagedResults.getData().get(1).isEnabled()).isTrue();
        assertThat(webhookPagedResults.getData().get(0).getStatus().name()).isEqualTo(WebhookStatus.DISABLED_BY_OWNER.name());
        assertThat(webhookPagedResults.getData().get(1).getStatus().name()).isEqualTo(WebhookStatus.ENABLED.name());
    }

    @Test
    void testGetWebhook() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getWebhook.json"));

        Webhook webhook = webhookResources.getWebhook(123L);

        assertThat(webhook.getApiClientId()).isNotBlank();
        assertThat(webhook.getVersion()).isEqualTo(1);
        assertThat(webhook.getApiClientId()).isNotBlank();
        assertThat(webhook.getScope()).isNotBlank();
        assertThat(webhook.getApiClientName()).isNotBlank();
        assertThat(webhook.getCallbackUrl()).isNotBlank();
        assertThat(webhook.getSharedSecret()).isNotBlank();
        assertThat(webhook.getEvents().size()).isEqualTo(2);
        assertThat(webhook.isEnabled()).isFalse();
        assertThat(webhook.getStatus().name()).isEqualTo(WebhookStatus.DISABLED_BY_OWNER.name());
    }

    @Test
    void testDeleteWebhook() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteWebhook.json"));
        assertThatCode(() -> webhookResources.deleteWebhook(123L)).doesNotThrowAnyException();
    }

    @Test
    void testCreateWebhook() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createWebhook.json"));
        Webhook webhookToCreate = new Webhook();
        webhookToCreate.setApiClientId("2382749284kjdskfhjsfhkjds");
        webhookToCreate.setEnabled(false);
        webhookToCreate.setScope("some-scope-string");
        webhookToCreate.setStatus(WebhookStatus.DISABLED_BY_OWNER);
        webhookToCreate.setDisabledDetails("disabled by owner for additional testing");
        webhookToCreate.setSharedSecret("sdklfldsjwe9oak232m4n22424kh2390283923");
        webhookToCreate.setCallbackUrl("https://foo-callback.smar.com/webhook");
        webhookToCreate.setEvents(Lists.newArrayList("add", "delete"));

        Webhook createdWebhook = webhookResources.createWebhook(webhookToCreate);

        assertThat(createdWebhook.getApiClientId()).isNotBlank();
        assertThat(createdWebhook.getVersion()).isEqualTo(1);
        assertThat(createdWebhook.getApiClientId()).isNotBlank();
        assertThat(createdWebhook.getScope()).isNotBlank();
        assertThat(createdWebhook.getApiClientName()).isNotBlank();
        assertThat(createdWebhook.getCallbackUrl()).isNotBlank();
        assertThat(createdWebhook.getSharedSecret()).isNotBlank();
        assertThat(createdWebhook.getEvents().size()).isEqualTo(2);
        assertThat(createdWebhook.isEnabled()).isFalse();
        assertThat(createdWebhook.getStatus().name()).isEqualTo(WebhookStatus.DISABLED_BY_OWNER.name());
    }

    @Test
    void testUpdateWebhook() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateWebhook.json"));
        Webhook webhookToUpdate = new Webhook();
        webhookToUpdate.setApiClientId("2382749284kjdskfhjsfhkjds");
        webhookToUpdate.setEnabled(false);
        webhookToUpdate.setScope("some-scope-string");
        webhookToUpdate.setStatus(WebhookStatus.ENABLED);
        webhookToUpdate.setDisabledDetails("");
        webhookToUpdate.setSharedSecret("sdklfldsjwe9oak232m4n22424kh2390283923");
        webhookToUpdate.setCallbackUrl("https://foo-callback.smar.com/webhook");
        webhookToUpdate.setEvents(Lists.newArrayList("add", "delete"));

        Webhook updatedWebhook = webhookResources.updateWebhook(webhookToUpdate);

        assertThat(updatedWebhook.getApiClientId()).isNotBlank();
        assertThat(updatedWebhook.getVersion()).isEqualTo(1);
        assertThat(updatedWebhook.getApiClientId()).isNotBlank();
        assertThat(updatedWebhook.getScope()).isNotBlank();
        assertThat(updatedWebhook.getApiClientName()).isNotBlank();
        assertThat(updatedWebhook.getCallbackUrl()).isNotBlank();
        assertThat(updatedWebhook.getSharedSecret()).isNotBlank();
        assertThat(updatedWebhook.getEvents().size()).isEqualTo(2);
        assertThat(updatedWebhook.isEnabled()).isTrue();
        assertThat(updatedWebhook.getStatus().name()).isEqualTo(WebhookStatus.ENABLED.name());
        assertThat(updatedWebhook.getDisabledDetails()).isBlank();
    }

    @Test
    void testResetSharedSecret_happyPath() throws IOException, SmartsheetException {
        server.setResponseBody(new File("src/test/resources/resetSharedSecret.json"));
        WebhookSharedSecret webhookSharedSecret = webhookResources.resetSharedSecret(123L);
        assertThat(webhookSharedSecret.getSharedSecret()).isNotBlank();
    }

    @Test
    void testResetSharedSecret_exception() throws IOException {
        server.setStatus(400);
        server.setResponseBody(new File("src/test/resources/webhookException.json"));
        assertThatThrownBy(() -> {
            webhookResources.resetSharedSecret(123L);
        })
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Something went wrong");
    }
}
