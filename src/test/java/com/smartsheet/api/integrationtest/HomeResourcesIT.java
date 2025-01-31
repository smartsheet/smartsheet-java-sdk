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

package com.smartsheet.api.integrationtest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.Home;
import com.smartsheet.api.models.enums.SourceInclusion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testGetHome() throws SmartsheetException, IOException {
        List<Home> homes = new ArrayList<>();
        homes.add(smartsheet.homeResources().getHome(EnumSet.of(SourceInclusion.SOURCE)));
        homes.add(smartsheet.homeResources().getHome(null));

        for (Home home : homes) {
            assertThat(home.getSheets()).isNotNull();
        }
    }
}
