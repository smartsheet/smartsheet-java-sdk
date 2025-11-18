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
