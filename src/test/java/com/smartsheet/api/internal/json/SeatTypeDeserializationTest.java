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

import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserPlan;
import com.smartsheet.api.models.enums.SeatType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SeatTypeDeserializationTest {
    JacksonJsonSerializer jjs = new JacksonJsonSerializer();

    @Test
    void userPlanViewerSeatTypeDeserializesToNull() throws JSONSerializerException, IOException {
        UserPlan plan = deserialize("{\"planId\":1,\"seatType\":\"VIEWER\"}", UserPlan.class);

        assertThat(plan.getPlanId()).isEqualTo(1L);
        assertThat(plan.getSeatType()).isNull();
    }

    @Test
    void userViewerSeatTypeDeserializesToNull() throws JSONSerializerException, IOException {
        User user = deserialize("{\"id\":1,\"seatType\":\"VIEWER\"}", User.class);

        assertThat(user.getSeatType()).isNull();
    }

    @Test
    void userPlanContributorSeatTypeDeserializes() throws JSONSerializerException, IOException {
        UserPlan plan = deserialize("{\"planId\":1,\"seatType\":\"CONTRIBUTOR\"}", UserPlan.class);

        assertThat(plan.getSeatType()).isEqualTo(SeatType.CONTRIBUTOR);
    }

    private <T> T deserialize(String json, Class<T> objectClass) throws JSONSerializerException, IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            return jjs.deserialize(objectClass, in);
        }
    }
}
