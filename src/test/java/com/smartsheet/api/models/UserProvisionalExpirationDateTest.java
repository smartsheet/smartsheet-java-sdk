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
