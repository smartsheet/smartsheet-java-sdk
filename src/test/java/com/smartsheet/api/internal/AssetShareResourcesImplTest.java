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
import com.smartsheet.api.models.CreateShareRequest;
import com.smartsheet.api.models.ShareResponse;
import com.smartsheet.api.models.UpdateShareRequest;
import com.smartsheet.api.models.ListAssetSharesResponse;
import com.smartsheet.api.models.enums.AccessLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssetShareResourcesImplTest extends ResourcesImplBase {

    private AssetShareResourcesImpl assetShareResourcesImpl;

    @BeforeEach
    public void setUp() throws Exception {
        assetShareResourcesImpl = new AssetShareResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer));
    }

    @Test
    void testListShares_IncludeWorkspacesFalse() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listSharesUpdated.json"));
        ListAssetSharesResponse<ShareResponse> shares = assetShareResourcesImpl.listShares("2906571706525572", "sheet", null, 3L, "ITEM");
        assertThat(shares.getItems().size()).isEqualTo(2);

        assertThat(shares.getItems().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(shares.getItems().get(1).getEmail()).isNull();
    }

    @Test
    void testListShares_IncludeWorkspacesNull() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listSharesUpdated.json"));
        ListAssetSharesResponse<ShareResponse> shares = assetShareResourcesImpl.listShares("2906571706525572", "sheet", null, 3L, "ITEM");
        assertThat(shares.getItems().size()).isEqualTo(2);

        assertThat(shares.getItems().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(shares.getItems().get(1).getEmail()).isNull();
    }

    @Test
    void testListShares_IncludeWorkspacesTrue() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listSharesUpdated.json"));
        ListAssetSharesResponse<ShareResponse> shares = assetShareResourcesImpl.listShares("2906571706525572", "sheet", null, 3L, "ITEM");
        assertThat(shares.getItems().size()).isEqualTo(2);

        assertThat(shares.getItems().get(0).getEmail()).isEqualTo("john.doe@smartsheet.com");
        assertThat(shares.getItems().get(1).getEmail()).isNull();
    }

    @Test
    void testGetShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getShare.json"));

        ShareResponse share = assetShareResourcesImpl.getShare("AQAISF82FOeE", "1234", "sheet");

        assertThat(share.getName()).isEqualTo("Group 1");
        assertThat(share.getAccessLevel()).isEqualTo(AccessLevel.ADMIN);
        assertThat(share.getId()).isEqualTo("AQAISF82FOeE");
    }

    @Test
    void testUpdateShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/assetUpdateShare.json"));
        UpdateShareRequest updateRequest = new UpdateShareRequest();
        updateRequest.setAccessLevel(AccessLevel.ADMIN);
        ShareResponse newShare = assetShareResourcesImpl.updateShare("123", "456", "sheet", updateRequest);
        assertThat(newShare.getAccessLevel()).isEqualTo(updateRequest.getAccessLevel());
    }

    @Test
    void testUpdateShareWithNullShare() {
        assertThatThrownBy(() -> assetShareResourcesImpl.updateShare("123", "456", "sheet", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDeleteShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteShare.json"));

        assetShareResourcesImpl.deleteShare("1234", "5678", "sheet");
    }

    @Test
    void testShareTo() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/shareTo.json"));

        List<CreateShareRequest> shareRequests = new ArrayList<>();
        CreateShareRequest request1 = new CreateShareRequest();
        request1.setEmail("john.doe@smartsheet.com");
        request1.setAccessLevel(AccessLevel.EDITOR);

        CreateShareRequest request2 = new CreateShareRequest();
        request2.setEmail("jane.doe@smartsheet.com");
        request2.setAccessLevel(AccessLevel.EDITOR);

        CreateShareRequest request3 = new CreateShareRequest();
        request3.setGroupId(34343L);
        request3.setAccessLevel(AccessLevel.EDITOR);

        shareRequests.add(request1);
        shareRequests.add(request2);
        shareRequests.add(request3);

        List<ShareResponse> shares = assetShareResourcesImpl.shareTo("1234", "sheet", shareRequests, true);
        assertThat(shares).hasSize(1);
        assertThat(shares.get(0).getEmail()).isEqualTo("jane.doe@smartsheet.com");
        assertThat(shares.get(0).getName()).isEqualTo("Jane Doe");
    }
}
