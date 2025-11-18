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
 * Core interfaces and classes for the Smartsheet Java SDK.
 * <p>
 * This package contains the main entry points for interacting with the Smartsheet API,
 * including the {@link com.smartsheet.api.Smartsheet} client interface and its factory classes,
 * resource interfaces for accessing different Smartsheet objects (sheets, workspaces, reports, etc.),
 * and exception classes for handling API errors.
 * </p>
 * <p>
 * To get started, use {@link com.smartsheet.api.SmartsheetFactory} to create a client instance:
 * </p>
 * <pre>
 * Smartsheet smartsheet = SmartsheetFactory.createDefaultClient(accessToken);
 * </pre>
 *
 * @see com.smartsheet.api.Smartsheet
 * @see com.smartsheet.api.SmartsheetFactory
 * @see com.smartsheet.api.SmartsheetBuilder
 */
package com.smartsheet.api;
