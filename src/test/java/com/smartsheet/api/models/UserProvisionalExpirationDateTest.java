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

import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProvisionalExpirationDateTest {
    @Test
    void testProvisionalExpirationDateSerializationRoundTrip() throws Exception {
        JacksonJsonSerializer serializer = new JacksonJsonSerializer();

        User originalUser = new User();
        ZonedDateTime provisionalExpirationDate = ZonedDateTime.parse("2026-12-13T12:17:52.525696Z");
        originalUser.setProvisionalExpirationDate(provisionalExpirationDate);
        ZonedDateTime seatTypeLastChangedAt = ZonedDateTime.parse("2026-12-13T12:17:52.525696Z");
        originalUser.setSeatTypeLastChangedAt(seatTypeLastChangedAt);

        String json1 = serializer.serialize(originalUser);

        User deserializedUser = serializer.deserialize(User.class,
                new ByteArrayInputStream(json1.getBytes(StandardCharsets.UTF_8)));

        String json2 = serializer.serialize(deserializedUser);

        assertThat(json2).isEqualTo(json1);

        assertThat(deserializedUser).usingRecursiveComparison().isEqualTo(originalUser);
    }
}
