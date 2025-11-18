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

/**
 * HTTP client abstraction layer for the Smartsheet Java SDK.
 * <p>
 * This package provides an abstraction over HTTP communication, allowing the SDK to work with
 * different HTTP client implementations. It includes interfaces and classes for HTTP requests,
 * responses, and client configuration.
 * </p>
 * <p>
 * The default implementation uses Apache HttpComponents, but the abstraction allows for
 * alternative implementations if needed.
 * </p>
 * <p>
 * <strong>Note:</strong> This is an internal package and its classes should not be directly
 * referenced by SDK users.
 * </p>
 *
 * @see com.smartsheet.api.internal.http.HttpClient
 * @see com.smartsheet.api.internal.http.HttpRequest
 * @see com.smartsheet.api.internal.http.HttpResponse
 */
package com.smartsheet.api.internal.http;
