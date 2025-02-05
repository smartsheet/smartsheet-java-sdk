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

package com.smartsheet.api.internal.oauth;

import com.smartsheet.api.HttpTestServer;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.http.HttpClient;
import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import com.smartsheet.api.internal.json.JsonSerializer;
import com.smartsheet.api.oauth.AccessDeniedException;
import com.smartsheet.api.oauth.AccessScope;
import com.smartsheet.api.oauth.AuthorizationResult;
import com.smartsheet.api.oauth.InvalidOAuthClientException;
import com.smartsheet.api.oauth.InvalidScopeException;
import com.smartsheet.api.oauth.OAuthAuthorizationCodeException;
import com.smartsheet.api.oauth.OAuthTokenException;
import com.smartsheet.api.oauth.Token;
import com.smartsheet.api.oauth.UnsupportedResponseTypeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Note this is an IT test because at least one of the tests requires an internet connection
class OAuthFlowImplTest {

    OAuthFlowImpl oauth;
    HttpClient httpClient = new DefaultHttpClient();
    JsonSerializer json = new JacksonJsonSerializer();

    HttpTestServer server;

    @AfterEach
    public void baseTearDown() throws Exception {
        server.stop();
    }

    @BeforeEach
    public void setUp() throws Exception {
        oauth = new OAuthFlowImpl(
                "clientID",
                "clientSecret",
                "redirectURL",
                "authorizationURL",
                "tokenURL",
                httpClient,
                json
        );

        // Setup test server
        server = new HttpTestServer();
        server.setPort(9090);
        server.start();

        // Setup the serializer
        JacksonJsonSerializer serializer = new JacksonJsonSerializer();
        serializer.setFailOnUnknownProperties(true);
    }

    @Test
    void testOAuthFlowImpl() {

        assertThat(oauth.getClientId()).isEqualTo("clientID");
        assertThat(oauth.getClientSecret()).isEqualTo("clientSecret");
        assertThat(oauth.getRedirectURL()).isEqualTo("redirectURL");
        assertThat(oauth.getAuthorizationURL()).isEqualTo("authorizationURL");
        assertThat(oauth.getTokenURL()).isEqualTo("tokenURL");
        assertThat(oauth.getHttpClient()).isEqualTo(httpClient);
        assertThat(oauth.getJsonSerializer()).isEqualTo(json);
    }

    @Test
    void testNewAuthorizationURL() {
        assertThatThrownBy(() -> oauth.newAuthorizationURL(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        oauth.newAuthorizationURL(EnumSet.of(AccessScope.READ_SHEETS), null);
        String authURL = oauth.newAuthorizationURL(EnumSet.of(AccessScope.READ_SHEETS), "state");

        assertThat(authURL).isEqualTo(
                "authorizationURL?scope=READ_SHEETS&response_type=code&redirect_uri=redirectURL&state=state&client_id=clientID"
        );
    }

    @Test
    void testExtractAuthorizationResult() throws URISyntaxException, OAuthAuthorizationCodeException {

        assertThatThrownBy(() -> oauth.extractAuthorizationResult(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> oauth.extractAuthorizationResult(""))
                .isInstanceOf(IllegalArgumentException.class);

        // Null query
        assertThatThrownBy(() -> oauth.extractAuthorizationResult("https://smartsheet.com"))
                .isInstanceOf(OAuthAuthorizationCodeException.class);

        assertThatThrownBy(() -> oauth.extractAuthorizationResult("https://smartsheet.com?error=access_denied"))
                .isInstanceOf(AccessDeniedException.class);

        assertThatThrownBy(() -> oauth.extractAuthorizationResult("https://smartsheet.com?error=unsupported_response_type"))
                .isInstanceOf(UnsupportedResponseTypeException.class);

        assertThatThrownBy(() -> oauth.extractAuthorizationResult("https://smartsheet.com?error=invalid_scope"))
                .isInstanceOf(InvalidScopeException.class);

        assertThatThrownBy(() -> oauth.extractAuthorizationResult("https://smartsheet.com?error=something_undefined"))
                .isInstanceOf(OAuthAuthorizationCodeException.class);

        // No valid parameters (empty result)
        oauth.extractAuthorizationResult("https://smartsheet.com?a=b");

        // Empty Error (empty result)
        oauth.extractAuthorizationResult("https://smartsheet.com?error=");

        // All parameters set (good response)
        oauth.extractAuthorizationResult("https://smartsheet.com?code=code&state=state&expires_in=10");
    }

    @Test
    void testObtainNewToken() throws OAuthAuthorizationCodeException, URISyntaxException {
        // Arrange
        server.setStatus(403);
        server.setContentType("application/x-www-form-urlencoded");
        server.setResponseBody("{\"errorCode\":1004,\"message\":\"You are not authorized to perform this action.\"}");

        oauth.setTokenURL("http://localhost:9090/1.1/token");
        AuthorizationResult authorizationResult = oauth.extractAuthorizationResult("http://localhost?a=b");

        // Act & Assert
        // 403 access forbidden
        assertThatThrownBy(() -> oauth.obtainNewToken(authorizationResult))
                .isInstanceOf(OAuthTokenException.class);
    }

    @Test
    void testRefreshToken() {
        server.setStatus(401);
        server.setContentType("application/x-www-form-urlencoded");
        server.setResponseBody(
                "{" +
                        "\"errorCode\": \"1067\"," +
                        "\"message\": \"Invalid client_id\"," +
                        "\"error\": \"invalid_client\"," +
                        "\"refId\":\"someRefId\"" +
                        "}"
        );

        oauth.setTokenURL("http://localhost:9090/1.1/token");

        Token token = new Token();
        token.setAccessToken("AccessToken");
        token.setExpiresInSeconds(10L);
        token.setRefreshToken("refreshToken");
        token.setTokenType("tokenType");
        assertThat(token.getAccessToken()).isEqualTo("AccessToken");
        assertThat(token.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(token.getExpiresInSeconds()).isEqualTo(10L);
        assertThat(token.getTokenType()).isEqualTo("tokenType");

        assertThatThrownBy(() -> oauth.refreshToken(token))
                .isInstanceOf(InvalidOAuthClientException.class);
    }

    @Test
    void testRevokeAccessToken() {
        server.setStatus(401);
        server.setContentType("application/x-www-form-urlencoded");
        server.setResponseBody(
                "{" +
                        "\"errorCode\": \"1067\"," +
                        "\"message\": \"Invalid client_id\"," +
                        "\"error\": \"invalid_client\"," +
                        "\"refId\":\"someRefId\"" +
                        "}"
        );

        oauth.setTokenURL("http://localhost:9090/1.1/token");

        Token token = new Token();
        token.setAccessToken("AccessToken");
        token.setExpiresInSeconds(10L);
        token.setRefreshToken("refreshToken");
        token.setTokenType("tokenType");

        assertThatThrownBy(() -> oauth.revokeAccessToken(token))
                .isInstanceOf(OAuthTokenException.class);
    }
}
