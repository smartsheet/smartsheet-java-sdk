/*
 * Smartsheet SDK for Java
 * Copyright (C) 2023 Smartsheet
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

package com.smartsheet.api.internal.util;

import org.apache.commons.io.input.CharSequenceInputStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class StreamUtilTest {
    @Test
    void testReadBytesFromStream() throws Exception {
        final String testString = "fuzzy wuzzy was a bear; fuzzy wuzzy had no hair...";
        final byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        final InputStream inputStream = new CharSequenceInputStream(testString, StandardCharsets.UTF_8);
        final ByteArrayOutputStream copyStream = new ByteArrayOutputStream();

        // this takes what was in inputStream, copies it into copyStream, and either resets inputStream (if supported)
        // or returns a new stream around the bytes read
        final InputStream backupStream = StreamUtil.cloneContent(inputStream, StreamUtil.ONE_MB, copyStream);
        if (backupStream == inputStream) {
            System.out.println("same stream returned (reset)");
            // verify readBytesFromStream gets everything from the inputStream (it also verifies cloneContent resets the source)
            byte[] streamBytes = StreamUtil.readBytesFromStream(inputStream);
            // it's all US-ASCII so it should match UTF-8 bytes
            assertThat(streamBytes).containsExactly(testBytes);
        } else {
            System.out.println("new stream returned");
            byte[] backupBytes = StreamUtil.readBytesFromStream(backupStream);
            assertThat(backupBytes).containsExactly(testBytes);
        }

        assertThat(copyStream.toByteArray()).containsExactly(testBytes);
    }

    @Test
    void testReadBytesFromStream_NullInputSource() throws Exception {
        final ByteArrayOutputStream copyStream = new ByteArrayOutputStream();
        assertThat(StreamUtil.cloneContent(null, StreamUtil.ONE_MB, copyStream)).isNull();
    }

    @Test
    void testToUtf8StringOrHex_MaxLength_EmptyUtf8String() {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        String result = StreamUtil.toUtf8StringOrHex(byteStream, -1);
        assertThat(result).isEmpty();
    }

    @Test
    void testToUtf8StringOrHex_MaxLength_NonEmptyUtf8String() throws IOException {
        String data = "This is a line of text. ";
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.writeBytes(dataBytes);
        byteStream.close();
        String result = StreamUtil.toUtf8StringOrHex(byteStream, -1);
        assertThat(result).isEqualTo(data);
    }

    @Test
    void testToUtf8StringOrHex_LengthSpecified_NonEmptyUtf8String() throws IOException {
        String data = "This is a line of text. ";
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.writeBytes(dataBytes);
        byteStream.close();
        String result = StreamUtil.toUtf8StringOrHex(byteStream, 10);
        assertThat(result).isEqualTo("This is a ...");
    }

}
