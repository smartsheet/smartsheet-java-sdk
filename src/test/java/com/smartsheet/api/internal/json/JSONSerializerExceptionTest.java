package com.smartsheet.api.internal.json;

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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JSONSerializerExceptionTest {

    @Test
    void testJSONSerializerExceptionString() {
        assertThatThrownBy(() -> {
            throw new JSONSerializerException("Test Exception");
        })
                .isInstanceOf(JSONSerializerException.class)
                        .hasMessage("Test Exception");
    }

    @Test
    void testJSONSerializerExceptionStringThrowable() throws JSONSerializerException {
        NullPointerException cause = new NullPointerException();
        assertThatThrownBy(() -> {
            throw new JSONSerializerException("Test Exception", cause);
        })
                .isInstanceOf(JSONSerializerException.class)
                .hasMessage("Test Exception")
                .hasCause(cause);
    }

    @Test
    void testJSONSerializerExceptionException() throws JSONSerializerException {
        NullPointerException cause = new NullPointerException();
        assertThatThrownBy(() -> {
            throw new JSONSerializerException(cause);
        })
                .isInstanceOf(JSONSerializerException.class)
                .hasCause(cause);
    }

}
