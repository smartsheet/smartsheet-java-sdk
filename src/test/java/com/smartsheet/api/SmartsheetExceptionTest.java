package com.smartsheet.api;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class SmartsheetExceptionTest {
    @Test
    void testSmartsheetExceptionString() {
        Throwable throwable = assertThrows(SmartsheetException.class, () -> {
            throw new SmartsheetException("My Exception");
        });
        assertEquals("My Exception", throwable.getMessage());
    }

    @Test
    void testSmartsheetExceptionStringThrowable() {
        NullPointerException cause = new NullPointerException();
        Throwable throwable = assertThrows(SmartsheetException.class, () -> {
            throw new SmartsheetException("Throwable exception", cause);
        });
        assertEquals("Throwable exception", throwable.getMessage());
        assertEquals(cause, throwable.getCause());
    }

    @Test
    void testSmartsheetExceptionException() {
        NullPointerException cause = new NullPointerException();
        Throwable throwable = assertThrows(SmartsheetException.class, () -> {
            throw new SmartsheetException(cause);
        });
        assertEquals(cause, throwable.getCause());
    }

}
