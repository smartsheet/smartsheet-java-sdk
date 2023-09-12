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