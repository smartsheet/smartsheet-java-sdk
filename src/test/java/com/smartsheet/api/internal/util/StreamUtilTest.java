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

package com.smartsheet.api.internal.util;

import org.apache.commons.io.input.CharSequenceInputStream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StreamUtilTest {
    @Test
    void testPublicVariableValues() {
        assertThat(StreamUtil.ONE_MB).isEqualTo(1048576);
        assertThat(StreamUtil.ONE_KB).isEqualTo(1024);
        assertThat(StreamUtil.TEN_KB).isEqualTo(10240);
    }

    @Nested
    class ReadBytesFromStreamTests {
        @Test
        void readBytesFromStream() throws Exception {
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
    }

    @Nested
    class CloneContentTests {
        @Test
        void cloneContent_NullInputSource() throws Exception {
            // Arrange
            final ByteArrayOutputStream copyStream = new ByteArrayOutputStream();

            // Act
            InputStream result = StreamUtil.cloneContent(null, StreamUtil.ONE_MB, copyStream);

            // Assert
            assertThat(result).isNull();
        }

        @Test
        void cloneContent_sourceMarkNotSupported() throws Exception {
            // Arrange
            final ByteArrayOutputStream copyStream = new ByteArrayOutputStream();
            InputStream inputStream = mock(InputStream.class);
            when(inputStream.markSupported()).thenReturn(false);
            when(inputStream.read(any())).thenReturn(-1);

            // Act
            InputStream result = StreamUtil.cloneContent(inputStream, StreamUtil.ONE_MB, copyStream);

            // Assert
            assertThat(result).isNotNull().isEmpty();
        }
    }

    @Nested
    class ToUtf8StringOrHexTests {
        @Test
        void toUtf8StringOrHex_MaxLength_EmptyUtf8String() {
            // Arrange
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            // Act
            String result = StreamUtil.toUtf8StringOrHex(byteStream, -1);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        void toUtf8StringOrHex_MaxLength_NonEmptyUtf8String() throws IOException {
            // Arrange
            String data = "This is a line of text. ";
            final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.writeBytes(dataBytes);
            byteStream.close();

            // Act
            String result = StreamUtil.toUtf8StringOrHex(byteStream, -1);

            // Assert
            assertThat(result).isEqualTo(data);
        }

        @Test
        void toUtf8StringOrHex_LengthSpecified_NonEmptyUtf8String() throws IOException {
            // Arrange
            String data = "This is a line of text. ";
            final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.writeBytes(dataBytes);
            byteStream.close();

            // Act
            String result = StreamUtil.toUtf8StringOrHex(byteStream, 10);

            // Assert
            assertThat(result).isEqualTo("This is a ...");
        }

        @Test
        void toUtf8StringOrHex_LengthSpecified_nullByteStream() throws IOException {
            // Arrange
            ByteArrayOutputStream byteArrayOutputStream = mock(ByteArrayOutputStream.class);
            when(byteArrayOutputStream.toString(anyString())).thenReturn(null);

            // Act
            String result = StreamUtil.toUtf8StringOrHex(byteArrayOutputStream, 10);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        void toUtf8StringOrHex_LengthSpecified_notUtf8() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = mock(ByteArrayOutputStream.class);
            doThrow(new RuntimeException("bad!")).when(byteArrayOutputStream).toString(any(Charset.class));

            String data = Integer.toHexString(0);
            final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            when(byteArrayOutputStream.toByteArray()).thenReturn(dataBytes);

            // Act
            String result = StreamUtil.toUtf8StringOrHex(byteArrayOutputStream, 10);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEqualTo("30");
        }
    }

}
