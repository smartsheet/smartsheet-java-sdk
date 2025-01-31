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

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.ImageUrl;
import com.smartsheet.api.models.ImageUrlMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageUrlResourcesImplTest extends ResourcesImplBase {

    private ImageUrlResourcesImpl imageUrlResources;

    @BeforeEach
    public void setUp() {
        // Create a folder resource
        imageUrlResources = new ImageUrlResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetImageUrls_RequestUrlsNull() {
        assertThatThrownBy(() -> imageUrlResources.getImageUrls(null)).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void testGetImageUrls() throws SmartsheetException, IOException {
        // Set a fake response
        server.setResponseBody(new File("src/test/resources/getImageUrls.json"));

        ImageUrl imageUrl1 = new ImageUrl("imageId1");
        imageUrl1.setUrl("https://foo.com/imageId1-url.png");
        ImageUrl imageUrl2 = new ImageUrl("imageId2");
        imageUrl2.setUrl("https://foo.com/imageId2-url.png");

        List<ImageUrl> requestUrls = Lists.newArrayList(imageUrl1, imageUrl2);

        ImageUrlMap imageUrls = imageUrlResources.getImageUrls(requestUrls);
        assertThat(imageUrls.getImageUrls().size()).isEqualTo(2);
        assertThat(imageUrls.getImageUrls().get(0).getUrl()).isNotBlank();
        assertThat(imageUrls.getImageUrls().get(1).getUrl()).isNotBlank();
        assertThat(imageUrls.getImageUrls().get(0).getImageId()).isNotBlank();
        assertThat(imageUrls.getImageUrls().get(1).getImageId()).isNotBlank();
        assertThat(imageUrls.getUrlExpiresInMillis()).isEqualTo(29837329487297392L);
    }
}
