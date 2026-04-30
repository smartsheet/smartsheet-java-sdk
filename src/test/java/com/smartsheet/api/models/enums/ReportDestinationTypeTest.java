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

package com.smartsheet.api.models.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReportDestinationTypeTest {

    @Test
    void testSerializationToLowercase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String folderJson = mapper.writeValueAsString(ReportDestinationType.FOLDER);
        assertThat(folderJson).isEqualTo("\"folder\"");

        String workspaceJson = mapper.writeValueAsString(ReportDestinationType.WORKSPACE);
        assertThat(workspaceJson).isEqualTo("\"workspace\"");
    }

    @Test
    void testDeserializationFromLowercase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ReportDestinationType folder = mapper.readValue("\"folder\"", ReportDestinationType.class);
        assertThat(folder).isEqualTo(ReportDestinationType.FOLDER);

        ReportDestinationType workspace = mapper.readValue("\"workspace\"", ReportDestinationType.class);
        assertThat(workspace).isEqualTo(ReportDestinationType.WORKSPACE);
    }
}
