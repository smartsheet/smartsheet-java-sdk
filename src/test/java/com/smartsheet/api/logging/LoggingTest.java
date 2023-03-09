package com.smartsheet.api.logging;

/*
 * #[license]
 * Smartsheet Java SDK
 * %%
 * Copyright (C) 2014 - 2017 Smartsheet
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

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetBuilder;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.Trace;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import com.smartsheet.api.models.Sheet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

/**
 *
 */
class LoggingTest {
    @BeforeAll
    public static void dontFailOnUnrecongnizedFields() {
        // Setup the serializer
        JacksonJsonSerializer.setFailOnUnknownProperties(false);    // no idea why we enable this in ResourcesImplBase.baseSetup
    }

    @AfterAll
    public static void failOnUnrecongnizedFields() {
        // Setup the serializer
        JacksonJsonSerializer.setFailOnUnknownProperties(true);    // put it back the way we found it
    }

    public void testConsoleLogging() throws Exception {
        ByteArrayOutputStream traceStream = new ByteArrayOutputStream();
        DefaultHttpClient.setTraceStream(traceStream);
        Smartsheet client = new SmartsheetBuilder().build();
        client.setTraces(Trace.Request, Trace.Response);    // should log entire request and response
        try {
            Sheet sheet = client.sheetResources().getSheet(42, null, null, null, null, null, 1, 1);
           Assertions.fail("expected SmartsheetException");
        } catch (SmartsheetException expected) {
            String output = traceStream.toString();
            // not super-robust but asserts some of the important parts
           Assertions.assertTrue(output.contains("\"request\" : {"), "request not found in - " + output);
           Assertions.assertTrue(output.contains("\"Authorization\" : \"Bearer ****null"), "Auth header not found in - " + output); // truncated Auth header
           Assertions.assertTrue(output.contains("\"response\" : {"), "response not found in - " + output);
           Assertions.assertTrue(output.contains("\"body\" : \"{\\n  \\\"errorCode\\\" : 1002,\\n  \\\"message\\\" : " +
                            "\\\"Your Access Token is invalid.\\\",\\n  \\\"refId\\\" :"), "response-body not found in - " + output);
           Assertions.assertTrue(output.contains("\"status\" : \"HTTP/1.1 401 Unauthorized\""), "status not found in - " + output);
        }
    }

    @Test
    void testCustomLogging() throws Exception {
        ByteArrayOutputStream traceStream = new ByteArrayOutputStream();
        DefaultHttpClient.setTraceStream(traceStream);
        Smartsheet client = new SmartsheetBuilder().setAccessToken("ll352u9jujauoqz4gstvsae05").build(); // using "null" as token results in NPE
        client.setTraces(Trace.Request, Trace.Response);    // should log entire request and response
        try {
            Sheet sheet = client.sheetResources().getSheet(42, null, null, null, null, null, 1, 1);
           Assertions.fail("expected SmartsheetException");
        } catch (SmartsheetException expected) {
            String output = traceStream.toString();
            // not super-robust but asserts some of the important parts
           Assertions.assertTrue(output.contains("request:{"), "request not found in - " + output);
           Assertions.assertTrue(output.contains("'Authorization':'Bearer ****ae05"), "Auth header not found in - " + output); // truncated Auth header
           Assertions.assertTrue(output.contains("response:{"), "response not found in - " + output);
           Assertions.assertTrue(output.contains("body:'{\n  \"errorCode\" : 1002,\n  \"message\" : \"Your Access Token is invalid.\",\n  \"refId\" :"), "response-body not found in - " + output);
           Assertions.assertTrue(output.contains("status:'HTTP/1.1 401 Unauthorized'"), "status not found in - " + output);
        }
    }
}
