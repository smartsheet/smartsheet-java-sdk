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
