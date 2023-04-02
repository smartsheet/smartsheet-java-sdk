package com.smartsheet.api.internal;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShareResourcesImplTest extends ResourcesImplBase {

    private ShareResourcesImpl shareResourcesImpl;

    @BeforeEach
    public void setUp() throws Exception {
        shareResourcesImpl = new ShareResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/", "accessToken",
                new DefaultHttpClient(), serializer), "sheets");
    }

    @Test
    void testListShares() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listShares.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Share> shares = shareResourcesImpl.listShares(2906571706525572L, parameters, false);
        assertTrue(shares.getTotalCount() == 2, "The number of shares returned is incorrect.");

        assertEquals("john.doe@smartsheet.com", shares.getData().get(0).getEmail(), "Email attribute of the share is incorrect.");
        assertEquals(null, shares.getData().get(1).getEmail(), "Email attribute of the share is incorrect.");
    }

    @Test
    void testGetShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getShare.json"));

        Share share = shareResourcesImpl.getShare(1234L, "fhqwhgads");

        assertEquals("Group 1", share.getName());
        assertEquals(AccessLevel.ADMIN, share.getAccessLevel());
        assertEquals("AQAISF82FOeE", share.getId());
    }

    @Test
    void testUpdateShare() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateShare.json"));
        Share share = new Share();
        share.setAccessLevel(AccessLevel.ADMIN);
        Share newShare = shareResourcesImpl.updateShare(123L, share);
        assertEquals(share.getAccessLevel(), newShare.getAccessLevel());
    }

    @Test
    void testUpdateShareWithNullShare() {
        assertThrows(IllegalArgumentException.class, () -> shareResourcesImpl.updateShare(123L, null));
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
        assertEquals(1, shares.size());
        assertEquals("jane.doe@smartsheet.com", shares.get(0).getEmail());
        assertEquals("Jane Doe", shares.get(0).getName());
    }
}
