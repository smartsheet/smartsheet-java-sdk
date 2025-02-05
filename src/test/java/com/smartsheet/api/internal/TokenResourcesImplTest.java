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

import com.smartsheet.api.internal.http.DefaultHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class TokenResourcesImplTest extends ResourcesImplBase {

    private TokenResourcesImpl tokenResources;

    @BeforeEach
    public void setUp() throws Exception {
        tokenResources = new TokenResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetAccessToken() {
        assertThatThrownBy(() -> {
            tokenResources.getAccessToken();
        }).isInstanceOf(NoSuchMethodException.class);
    }

    @Test
    void testRevokeAccessToken() {
        server.setResponseBody("{\"resultCode\":0,\"message\":\"SUCCESS\"}");
        assertThatCode(() -> tokenResources.revokeAccessToken()).doesNotThrowAnyException();
    }

    @Test
    void testRefreshAccessToken() {
        assertThatThrownBy(() -> {
            tokenResources.refreshAccessToken();
        }).isInstanceOf(NoSuchMethodException.class);
    }

}
