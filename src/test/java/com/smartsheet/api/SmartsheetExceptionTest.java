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

package com.smartsheet.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SmartsheetExceptionTest {
    @Test
    void testSmartsheetExceptionString() {
        assertThatThrownBy(() -> {
            throw new SmartsheetException("My Exception");
        })
                .isInstanceOf(SmartsheetException.class)
                .hasMessage("My Exception");
    }

    @Test
    void testSmartsheetExceptionStringThrowable() {
        NullPointerException cause = new NullPointerException();
        assertThatThrownBy(() -> {
            throw new SmartsheetException("My Exception", cause);
        })
                .isInstanceOf(SmartsheetException.class)
                .hasMessage("My Exception")
                .hasCause(cause);
    }

    @Test
    void testSmartsheetExceptionException() {
        NullPointerException cause = new NullPointerException();
        assertThatThrownBy(() -> {
            throw new SmartsheetException(cause);
        })
                .isInstanceOf(SmartsheetException.class)
                .hasCause(cause);
    }

}
