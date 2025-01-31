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
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Share;
import com.smartsheet.api.models.enums.AccessLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShareResourcesImplTest extends ResourcesImplBase {

    private ShareResourcesImpl shareResourcesImpl;

    @BeforeEach
    public void setUp() throws Exception {
        shareResourcesImpl = new ShareResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer), "sheets");
    }

    @Test
    void testListShares_IncludeWorkspacesFalse() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listShares.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Share> shares = shareResourcesImpl.listShares(2906571706525572L, parameters, Boolean.FALSE);
        assertThat(shares.getTotalCount()).isEqualTo(2);

        assertThat(shares.getData().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(shares.getData().get(1).getEmail()).isNull();
    }

    @Test
    void testListShares_IncludeWorkspacesNull() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listShares.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Share> shares = shareResourcesImpl.listShares(2906571706525572L, parameters, null);
        assertThat(shares.getTotalCount()).isEqualTo(2);

        assertThat(shares.getData().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(shares.getData().get(1).getEmail()).isNull();
    }

    @Test
    void testListShares_IncludeWorkspacesTrue() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listShares.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Share> shares = shareResourcesImpl.listShares(2906571706525572L, parameters, Boolean.TRUE);
        assertThat(shares.getTotalCount()).isEqualTo(2);

        assertThat(shares.getData().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(shares.getData().get(1).getEmail()).isNull();
    }

    @Test
    void testListShares_DefaultIncludeWorkspacesFalse() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listShares.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Share> shares = shareResourcesImpl.listShares(2906571706525572L, parameters);
        assertThat(shares.getTotalCount()).isEqualTo(2);

        assertThat(shares.getData().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(shares.getData().get(1).getEmail()).isNull();
    }

    @Test
    void testGetShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getShare.json"));

        Share share = shareResourcesImpl.getShare(1234L, "fhqwhgads");

        assertThat(share.getName()).isEqualTo("Group 1");
        assertThat(share.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(share.getId()).isEqualTo("AQAISF82FOeE");
    }

    @Test
    void testUpdateShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateShare.json"));
        Share share = new Share();
        share.setAccessLevel(AccessLevel.ADMIN);
        Share newShare = shareResourcesImpl.updateShare(123L, share);
        assertThat(newShare.getAccessLevel()).isEqualTo(share.getAccessLevel());
    }

    @Test
    void testUpdateShareWithNullShare() {
        assertThatThrownBy(() -> shareResourcesImpl.updateShare(123L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDeleteShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteShare.json"));

        shareResourcesImpl.deleteShare(1234L, "fhqwhgads");
    }

    @Test
    void testShareTo() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/shareTo.json"));

        List<Share> shares = new ArrayList<>();
        shares.add(new Share.CreateUserShareBuilder().setEmailAddress("john.doe@smartsheet.com")
                .setAccessLevel(AccessLevel.EDITOR).build());
        shares.add(new Share.CreateUserShareBuilder().setEmailAddress("jane.doe@smartsheet.com")
                .setAccessLevel(AccessLevel.EDITOR).build());
        shares.add(new Share.CreateGroupShareBuilder().setGroupId(34343L)
                .setAccessLevel(AccessLevel.EDITOR).build());

        shares = shareResourcesImpl.shareTo(1234L, shares, true);
        assertThat(shares).hasSize(1);
        assertThat(shares.get(0).getEmail()).isEqualTo("jane.doe@smartsheet.com");
        assertThat(shares.get(0).getName()).isEqualTo("Jane Doe");
    }
}
