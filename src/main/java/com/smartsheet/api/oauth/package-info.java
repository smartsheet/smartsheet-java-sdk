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
 * OAuth 2.0 authentication support for the Smartsheet Java SDK.
 * <p>
 * This package provides classes and interfaces for implementing OAuth 2.0 authentication flows
 * with the Smartsheet API. OAuth 2.0 is the recommended authentication method for applications
 * that need to access Smartsheet on behalf of users.
 * </p>
 * <p>
 * Key components include:
 * </p>
 * <ul>
 *   <li>{@link com.smartsheet.api.oauth.OAuthFlow} - Interface for OAuth flow operations</li>
 *   <li>{@link com.smartsheet.api.oauth.Token} - Represents an OAuth access token</li>
 *   <li>{@link com.smartsheet.api.oauth.AccessScope} - Defines OAuth access scopes</li>
 *   <li>Various exception classes for OAuth-specific errors</li>
 * </ul>
 * <p>
 * Use these classes to implement the OAuth authorization code flow to obtain access tokens
 * for API requests.
 * </p>
 *
 * @see com.smartsheet.api.oauth.OAuthFlow
 * @see com.smartsheet.api.oauth.Token
 */
package com.smartsheet.api.oauth;
