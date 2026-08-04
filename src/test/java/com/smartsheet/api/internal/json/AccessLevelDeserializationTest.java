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

package com.smartsheet.api.internal.json;

import com.smartsheet.api.models.Report;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.ShareResponse;
import com.smartsheet.api.models.Workspace;
import com.smartsheet.api.models.enums.AccessLevel;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that a COMMENTER access level returned by the API deserializes to
 * {@link AccessLevel#COMMENTER} rather than silently becoming null.
 *
 * <p>The serializer sets READ_UNKNOWN_ENUM_VALUES_AS_NULL, so an access level missing from the
 * enum is dropped without an error, leaving callers unable to tell "this is a Commenter share"
 * apart from "this share has no access level". See
 * <a href="https://github.com/smartsheet/smartsheet-csharp-sdk/issues/218">csharp-sdk#218</a>.
 */
class AccessLevelDeserializationTest {
    JacksonJsonSerializer jjs = new JacksonJsonSerializer();

    @Test
    void shareResponseDeserializesCommenter() throws JSONSerializerException, IOException {
        ShareResponse share = deserialize(
                "{\"accessLevel\":\"COMMENTER\",\"email\":\"user@example.com\"}",
                ShareResponse.class
        );

        assertThat(share.getAccessLevel()).isEqualTo(AccessLevel.COMMENTER);
        assertThat(share.getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void sheetDeserializesCommenter() throws JSONSerializerException, IOException {
        Sheet sheet = deserialize("{\"accessLevel\":\"COMMENTER\"}", Sheet.class);

        assertThat(sheet.getAccessLevel()).isEqualTo(AccessLevel.COMMENTER);
    }

    @Test
    void reportDeserializesCommenter() throws JSONSerializerException, IOException {
        Report report = deserialize("{\"accessLevel\":\"COMMENTER\"}", Report.class);

        assertThat(report.getAccessLevel()).isEqualTo(AccessLevel.COMMENTER);
    }

    @Test
    void workspaceDeserializesCommenter() throws JSONSerializerException, IOException {
        Workspace workspace = deserialize("{\"accessLevel\":\"COMMENTER\"}", Workspace.class);

        assertThat(workspace.getAccessLevel()).isEqualTo(AccessLevel.COMMENTER);
    }

    @Test
    void unmappedAccessLevelStillDeserializesToNull() throws JSONSerializerException, IOException {
        // Forward-compatibility is intentional: an access level this SDK version does not know
        // must not fail the whole response.
        Sheet sheet = deserialize("{\"accessLevel\":\"NOT_A_REAL_ACCESS_LEVEL\"}", Sheet.class);

        assertThat(sheet.getAccessLevel()).isNull();
    }

    private <T> T deserialize(String json, Class<T> objectClass) throws JSONSerializerException, IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            return jjs.deserialize(objectClass, in);
        }
    }
}
