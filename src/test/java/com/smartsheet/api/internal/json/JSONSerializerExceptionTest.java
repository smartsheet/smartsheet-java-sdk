package com.smartsheet.api.internal.json;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2014 Smartsheet
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JSONSerializerExceptionTest {
    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void testJSONSerializerExceptionString() {
        Throwable throwable = assertThrows(JSONSerializerException.class, () -> {
            throw new JSONSerializerException("Test Exception");
        });
        assertEquals("Test Exception", throwable.getMessage());
    }



    @Test
    public void testJSONSerializerExceptionStringThrowable() throws JSONSerializerException {
        NullPointerException cause = new NullPointerException();
        Throwable throwable = assertThrows(JSONSerializerException.class, () -> {
            throw new JSONSerializerException("Test Exception1", cause);
        });
        assertEquals("Test Exception1", throwable.getMessage());
        assertEquals(cause, throwable.getCause());


    }

    @Test
    public void testJSONSerializerExceptionException() throws JSONSerializerException {
        NullPointerException cause = new NullPointerException();
        Throwable throwable = assertThrows(JSONSerializerException.class, () -> {
            throw new JSONSerializerException(cause);
        });
        assertEquals(cause, throwable.getCause());
    }

}
