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

import com.smartsheet.api.models.enums.AttachmentType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AttachmentTypeTest {

    @Test
    void test() {
        assertThat(AttachmentType.valueOf("FILE")).isNotNull();
        assertThat(AttachmentType.valueOf("GOOGLE_DRIVE")).isNotNull();
        assertThat(AttachmentType.valueOf("LINK")).isNotNull();
        assertThat(AttachmentType.valueOf("BOX_COM")).isNotNull();
        assertThat(AttachmentType.valueOf("DROPBOX")).isNotNull();
        assertThat(AttachmentType.valueOf("EVERNOTE")).isNotNull();
        assertThat(AttachmentType.valueOf("EGNYTE")).isNotNull();
        assertThat(AttachmentType.valueOf("ONEDRIVE")).isNotNull();
        assertThat(AttachmentType.valueOf("SMARTSHEET")).isNotNull();
        assertThat(AttachmentType.values()).hasSize(9);
    }

}
