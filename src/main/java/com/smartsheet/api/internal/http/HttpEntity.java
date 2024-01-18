/*
* Copyright (C) 2024 Smartsheet
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

package com.smartsheet.api.internal.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.InputStream;

/**
 * This class represents an HTTP Entity (http://www.w3.org/Protocols/rfc2616/rfc2616-sec7.html).
 * <p>
 * Thread Safety: This class is not thread safe since it's mutable.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class HttpEntity {
    /**
     * Represents the content type.
     * <p>
     * It has a pair of setter/getter (not shown on class diagram for brevity).
     */
    private String contentType;

    /**
     * Represents the content length.
     * <p>
     * It has a pair of setter/getter (not shown on class diagram for brevity).
     */
    private long contentLength;

    /**
     * Represents the content as an InputStream.
     * <p>
     * It has a pair of setter/getter (not shown on class diagram for brevity).
     */
    private InputStream content;
}
