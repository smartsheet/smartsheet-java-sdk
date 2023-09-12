package com.smartsheet.api.internal;

import com.smartsheet.api.internal.http.DefaultHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassthroughResourcesImplTest extends ResourcesImplBase {

    private PassthroughResourcesImpl passthroughResources;

    @BeforeEach
    public void setUp() throws Exception {
        passthroughResources = new PassthroughResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetRequest() {

    }

    @Test
    void testPostRequest() {

    }

    @Test
    void testPutRequest() {

    }

    @Test
    void testDeleteRequest() {

    }

}