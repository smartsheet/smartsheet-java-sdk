package com.smartsheet.api.internal;

import com.smartsheet.api.internal.http.DefaultHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventResourcesImplTest extends ResourcesImplBase {

    private EventResourcesImpl eventResources;

    @BeforeEach
    public void setUp() throws Exception {
        eventResources = new EventResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListEvents_happyPath() {

    }

    @Test
    void testListEvents_exception() {

    }

}