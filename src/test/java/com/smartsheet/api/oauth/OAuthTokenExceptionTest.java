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

package com.smartsheet.api.oauth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OAuthTokenExceptionTest {

    @Test
    void testOAuthTokenExceptionString() {
        try {
            throw new OAuthTokenException("message");
        } catch (OAuthTokenException e) {
            assertThat(e.getMessage()).isEqualTo("message");
        }
    }

    @Test
    void testOAuthTokenExceptionStringThrowable() {
        try {
            throw new OAuthTokenException("message", null);
        } catch (OAuthTokenException ex) {
            assertThat(ex.getMessage()).isEqualTo("message");
        }
    }
}
