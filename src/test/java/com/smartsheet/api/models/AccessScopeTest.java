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

package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.AccessScope;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AccessScopeTest {

    @Test
    void test() {
        assertThat(AccessScope.valueOf("ADMIN_SHEETS")).isNotNull();
        assertThat(AccessScope.valueOf("ADMIN_SIGHTS")).isNotNull();
        assertThat(AccessScope.valueOf("ADMIN_USERS")).isNotNull();
        assertThat(AccessScope.valueOf("ADMIN_WEBHOOKS")).isNotNull();
        assertThat(AccessScope.valueOf("ADMIN_WORKSPACES")).isNotNull();
        assertThat(AccessScope.valueOf("CREATE_SHEETS")).isNotNull();
        assertThat(AccessScope.valueOf("CREATE_SIGHTS")).isNotNull();
        assertThat(AccessScope.valueOf("DELETE_SHEETS")).isNotNull();
        assertThat(AccessScope.valueOf("DELETE_SIGHTS")).isNotNull();
        assertThat(AccessScope.valueOf("READ_CONTACTS")).isNotNull();
        assertThat(AccessScope.valueOf("READ_SHEETS")).isNotNull();
        assertThat(AccessScope.valueOf("READ_SIGHTS")).isNotNull();
        assertThat(AccessScope.valueOf("READ_USERS")).isNotNull();
        assertThat(AccessScope.valueOf("SHARE_SHEETS")).isNotNull();
        assertThat(AccessScope.valueOf("SHARE_SIGHTS")).isNotNull();
        assertThat(AccessScope.valueOf("WRITE_SHEETS")).isNotNull();
        assertThat(AccessScope.values()).hasSize(16);
    }

    @Test
    void bothVersionsOfAccessScopeAreIdentical() {
        Set<String> scopeNames = new HashSet<>();
        for (AccessScope accessScope : AccessScope.values()) {
            scopeNames.add(accessScope.name());
        }

        for (com.smartsheet.api.oauth.AccessScope accessScope : com.smartsheet.api.oauth.AccessScope.values()) {
            assertThat(scopeNames).contains(accessScope.name());
        }

        assertThat(AccessScope.values()).hasSameSizeAs(com.smartsheet.api.oauth.AccessScope.values());
    }
}
