/*
 * Smartsheet SDK for Java
 * Copyright (C) 2023 Smartsheet
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

class SightResourcesImplTest extends ResourcesImplBase {
    private SightResourcesImpl sightResourcesImpl;

    @BeforeEach
    public void before() {
        SmartsheetImpl smartsheetImpl = new SmartsheetImpl(
                "http://localhost:9090/1.1/",
                "accessToken",
                new DefaultHttpClient(),
                serializer
        );
        sightResourcesImpl = new SightResourcesImpl(smartsheetImpl);
    }

    @Test
    void updateWithNullSight() {
        assertThatThrownBy(() -> sightResourcesImpl.updateSight(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
