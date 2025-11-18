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
