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

package com.smartsheet.api.internal.util;

import com.smartsheet.api.models.DeleteUserParameters;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteUserParametersTest {

    @Test
    void testDeleteUserParametersModel() {
        DeleteUserParameters parameters = new DeleteUserParameters();
        parameters.setTransferToId(12345L);
        parameters.setTransferSheets(true);
        parameters.setRemoveFromSharing(true);

        assertThat(parameters.getTransferToId().longValue()).isEqualTo(12345);
        assertThat(parameters.isTransferSheets()).isTrue();
        assertThat(parameters.isRemoveFromSharing()).isTrue();

        DeleteUserParameters parameters2 = new DeleteUserParameters(6789L, false, false);
        assertThat(parameters2.getTransferToId().longValue()).isEqualTo(6789);
        assertThat(parameters2.isTransferSheets()).isFalse();
        assertThat(parameters2.isRemoveFromSharing()).isFalse();
    }

    @Test
    void testToQueryString() {
        DeleteUserParameters parameters = new DeleteUserParameters(12345L, true, true);
        String[] matches1 = new String[]{"transferSheets=true", "removeFromSharing=true", "transferTo=12345"};
        for (String s : matches1) {
            assertThat(parameters.toQueryString()).contains(s);
        }

        DeleteUserParameters parameters2 = new DeleteUserParameters(null, true, true);
        String[] matches2 = new String[]{"transferSheets=true", "removeFromSharing=true"};
        for (String s : matches2) {
            assertThat(parameters2.toQueryString()).contains(s);
        }

        DeleteUserParameters parameters3 = new DeleteUserParameters(null, null, true);
        assertThat(parameters3.toQueryString()).isEqualTo("?removeFromSharing=true");

        DeleteUserParameters parameters4 = new DeleteUserParameters(null, null, null);
        assertThat(parameters4.toQueryString()).isEmpty();
    }
}
