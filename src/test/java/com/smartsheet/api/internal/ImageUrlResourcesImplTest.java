package com.smartsheet.api.internal;

import com.smartsheet.api.internal.http.DefaultHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageUrlResourcesImplTest extends ResourcesImplBase {

    @BeforeEach
    public void setUp() {
        // Create a folder resource
        folderResource = new FolderResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetImageUrls() {

    }

}