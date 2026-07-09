# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [x.x.x] - Unreleased

### Added
- Support for GET /2.0/reports/{reportId}/definition (`ReportResources.getReportDefinition`)
- Support for GET /2.0/reports/{reportId}/columns (`ReportResources.listReportColumns`)
- Support for GET /2.0/reports/{reportId}/columns/{columnVirtualId} (`ReportResources.getReportColumn`)
- Support for PUT /2.0/reports/{reportId}/columns/{columnVirtualId} (`ReportResources.updateReportColumn`)
- Support for DELETE /2.0/reports/{reportId}/columns/{columnVirtualId} (`ReportResources.deleteReportColumn`)
- Support for GET /2.0/reports/{reportId}/scope (`ReportResources.listReportScope`)

### Deprecated

- Deprecated `Event.getObjectId()`; use `Event.getObjectIdStr()` instead. `objectId` is numeric only and returns -1 for non-numeric identifiers. It is not scheduled for removal.

## [4.1.0] - 2026-06-26

### Fixed

- Deprecation related corrections
- `CurrentUserObjectValue` no longer sends a `value` for `CURRENT_USER` object values
- Added `ReportFilterObjectValueDeserializer` to correctly deserialize report filter object values

### Added

- Hardcode `paginationType=token` for `listWorkspaces`.
- Added support for GET /2.0/sheets/{sheetId}/path endpoint (`getSheetPath`)
- Added support for GET /2.0/reports/{reportId}/path endpoint (`getReportPath`)
- Added support for GET /2.0/sights/{sightId}/path endpoint (`getSightPath`)
- Added support for GET /2.0/folders/{folderId}/path endpoint (`getFolderPath`)
- Added helper methods `getLeaf<Asset>()` and `getLeaf<Asset>Path()` to the responses of the path endpoints for convenient traversal

## [4.0.0] - 2026-06-08
### Added
- Added `objectIdStr` field to Event model to support alphanumeric object identifiers in v2.0 events endpoint
- Field is optional and maintains full backward compatibility with existing implementations
- AI assisted workflows via claude skills (`implement-api-endpoint` and `review-api-endpoint`)

### Removed
- ⚠️ **BREAKING**: Removed deprecated offset-based pagination parameters (`includeAll`, `page`, `pageSize`) and `modifiedSince` from `listSights`. These parameters were [deprecated by the Smartsheet API](https://developers.smartsheet.com/api/smartsheet/changelog#deprecated-includeall-and-offset-based-pagination-for-dashboards) (sunset Jun-03-2026). The response no longer includes `pageNumber`, `pageSize`, `totalPages`, or `totalCount`. Use token-based pagination instead: `new TokenPaginationParameters(lastKey, maxItems)`.
- ⚠️ **BREAKING**: Removed deprecated offset-based pagination overload of `listWorkspaces`. `listWorkspaces(PaginationParameters)` and the `PagedResult<Workspace>` return type have been replaced with `listWorkspaces(TokenPaginationParameters)` returning `TokenPaginatedResult<Workspace>`. These offset parameters were [deprecated by the Smartsheet API](https://developers.smartsheet.com/api/smartsheet/changelog#2025-08-04) (sunset Jun-03-2026). Use `new TokenPaginationParameters(lastKey, maxItems)`. The new shape mirrors `listSights`.
- ⚠️ **BREAKING**: Removed `listPublicTemplates` and `listUserCreatedTemplates` from `TemplateResources`. The `TemplateResources` interface, implementation, and `Smartsheet.templateResources()` accessor have been removed entirely. The underlying `GET /templates` and `GET /templates/public` endpoints were [deprecated by the Smartsheet API](https://developers.smartsheet.com/api/smartsheet/changelog#2025-08-04) (sunset Jun-03-2026). Migrate to `getWorkspaceChildren` / `getFolderChildren` with `childrenResourceTypes` including `TEMPLATES,SHEETS` to list templates within a specific workspace or folder.
- ⚠️ **BREAKING**: Removed `getFolder` and `listFolders` from `FolderResources`, `getWorkspace` from `WorkspaceResources`, and `listFolders` from `WorkspaceFolderResources`. (`WorkspaceFolderResources.createFolder` and `Smartsheet.workspaceResources().folderResources()` are retained.) The underlying `GET /folders/{folderId}`, `GET /folders/{folderId}/folders`, `GET /workspaces/{workspaceId}`, and `GET /workspaces/{workspaceId}/folders` endpoints were [deprecated by the Smartsheet API](https://developers.smartsheet.com/api/smartsheet/changelog#2025-08-04) (sunset Jun-03-2026). Migrate to `getFolderMetadata` + `getFolderChildren` and `getWorkspaceMetadata` + `getWorkspaceChildren`. Use `childrenResourceTypes` to filter the children response (e.g., `FOLDERS` to replicate the old list-folders behavior).
- ⚠️ **BREAKING**: Removed the deprecated `ShareResources` interface and the `shareResources()` accessor from `SheetResources`, `ReportResources`, `SightResources`, and `WorkspaceResources`. The underlying asset-specific sharing endpoints were [deprecated by the Smartsheet API](https://developers.smartsheet.com/api/smartsheet/changelog#2025-08-04) (sunset Jun-03-2026). Migrate to `Smartsheet.assetShareResources()` (`listShares`, `getShare`, `shareTo`, `updateShare`, `deleteShare`), passing `assetId` and `assetType`. Note updates now use `PATCH` instead of `PUT`.

### Changed
- ⚠️ **BREAKING**: `TokenPaginationParameters` no longer exposes `paginationType` as a field, constructor argument, or getter/setter. The value is now hardcoded to `"token"` in the emitted query string, matching the C# SDK. The previous `TokenPaginationParameters(String paginationType, String lastKey, Integer maxItems)` constructor has been replaced with `TokenPaginationParameters(String lastKey, Integer maxItems)`.
- `listWebhooks` Javadoc updated to reflect Smartsheet API behavior changes effective Jun-03-2026: `includeAll` is no longer honored by the server for this endpoint and is ignored if set on `PaginationParameters` (`PaginationParameters` remains a shared class — other endpoints still support `includeAll`), `pageSize` is server-capped at 10,000, `totalCount` and `totalPages` are returned as `-1`, and webhooks are sorted by creation date (most recent first) instead of name. SDK signature unchanged. See [Smartsheet API changelog 2025-08-04](https://developers.smartsheet.com/api/smartsheet/changelog#2025-08-04).
- ⚠️ **BREAKING**: `ShareResponse.getScope()`/`setScope()` now use the `ShareScope` enum (`ITEM`/`WORKSPACE`) instead of `String`, and `AssetShareResources.listShares` now takes a `ShareScope sharingInclude` parameter instead of `String`. This corrects the share scope type in the unified sharing API.

## [3.11.0] - 2026-04-30
### Added
- Support for POST /2.0/reports/{reportId}/columns endpoint
- WireMock integration tests for contract testing for POST /2.0/reports/{reportId}/columns endpoint
- Support for POST /2.0/reports/{reportId}/scope endpoint
- Support for DELETE /2.0/reports/{reportId}/scope endpoint
- WireMock integration tests for contract testing for POST /2.0/reports/{reportId}/scope and DELETE /2.0/reports/{reportId}/scope endpoints
- Added support for PUT /reports/{reportId}/definition endpoint, Update Report Definition
- Support for the POST /2.0/reports/{id}/scope endpoint
- Support for the DELETE /2.0/reports/{id}/scope endpoint
- Support for the DELETE /2.0/reports/{id} endpoint
- WireMock integration tests for contract testing for POST /2.0/reports/{id}/scope and DELETE /2.0/reports/{id}/scope endpoints
- Support for the POST /2.0/reports endpoint
- WireMock integration tests for contract testing for POST /2.0/reports endpoint
- Added CONTRIBUTOR to SeatType enum
- Added CONTRIBUTOR to DowngradeSeatType enum
- Support for `displayContributorSeatType` query parameter in `listUsers` and `listUserPlans` methods
- Added WireMock integration tests for CONTRIBUTOR seat type in list users and downgrade user endpoints

## [3.10.1] - 2026-03-31
### Fixed
- Fixed WORKSPACES endpoint constant in SheetResourcesImpl to use lowercase "workspaces" instead of uppercase "WORKSPACES", resolving 404 errors when creating sheets in workspaces

## [3.10.0] - 2025-12-04
### Added
- WireMock integration tests for contract testing for GET /2.0/users/{userId}/plans and GET /2.0/users endpoints
- WireMock integration tests for contract testing for POST /2.0/users/{userId}/plans/{planId}/upgrade and POST /2.0/users/{userId}/plans/{planId}/downgrade
- WireMock integration tests for contract testing for DELETE /2.0/users/{userId}/plans/{planId} endpoint
- Add provisionalExpirationDate field to the User model
- Add provisionalExpirationDate field to the UserPlan model
- Remove integration tests from the sdk test suite and workflows
- Support for POST /2.0/users/{userId}/reactivate endpoint
- Support for POST /2.0/users/{userId}/deactivate endpoint
### Updated
- Folder structure for the Users related WireMock tests

## [3.8.0] - 2025-10-14
### Added
- Added ListAssetSharesResponse class
- Added new deserialization methods
### Updated
- Updated listShares method for token pagination
- Fixed tests

## [3.7.0] - 2025-09-25
### Added
- Support for GET /2.0/users/{userId}/plans
- Support for GET /2.0/users
- Support for DELETE /2.0/users/{userId}/plans/{planId}

## [3.6.0] - 2025-09-23
### Added
- Support for POST /users/{userId}/plans/{planId}/downgrade
- Support for POST /users/{userId}/plans/{planId}/upgrade

## [3.5.0] - 2025-08-08
### Added
  - Added support for token-based pagination in WorkspaceResources.listWorkspaces() method
  - PaginationParameters now accepts `paginationType`, `lastKey` and `maxItems` parameters for token-based pagination
  - PagedResult now includes `lastKey` field in response for token-based pagination
  - Maintains backward compatibility with existing page-based pagination
### Updated
  - Updated the deploy process to use JReleaser Gradle plugin and will no longer publish to Nexus2

## [3.4.0] - 2025-08-05
### Added
- New SDK methods for workspace and folder metadata and children endpoints:
  - `getWorkspaceMetadata()` - Get metadata for a workspace including access level, permalink, and dates
  - `getWorkspaceChildren()` - Get children of a workspace with filtering by resource type and pagination support
  - `getFolderMetadata()` - Get metadata for a folder including basic properties and optional source information
  - `getFolderChildren()` - Get children of a folder with filtering by resource type and pagination support
### Updated
- Updated Folder class to support the 'source' property. The property is thus also supported for Workspace.
### Deprecated
- `WorkspaceResources.getWorkspace()` method - replaced by `getWorkspaceMetadata()` and `getWorkspaceChildren()`
- `FolderResources.getFolder()` method - replaced by `getFolderMetadata()` and `getFolderChildren()`
- `FolderResources.listFolders()` method - use `getFolderChildren()` with resource type filtering
- `WorkspaceFolderResources.listFolders()` method - use `getWorkspaceChildren()` with resource type filtering
- All Home-related functions marked for removal in future version:
  - `HomeResources` interface and its methods and implementations (`getHome()`, `folderResources()`)
  - `HomeFolderResources` interface and its methods and implementations (`listFolders()`, `createFolder()`)

## [3.3.0] - 2025-06-30
### Added
- Added `AssetShareResources` interface and `AssetShareResourcesImpl` implementation for sharing various asset types
- Deprecated old sharing endpoints.

### Changed
- Deprecated `ShareResources` interface and `ShareResourcesImpl` implementation in favor of `AssetShareResources`
- All methods in `ShareResources` and `ShareResourcesImpl` are now marked with `@Deprecated(since = "2.0.0", forRemoval = true)`

## [3.2.3] - 2025-02-14
### Changed
- Update the copyright year to 2025
- Marked the modifiedDate field as deprecated in the Comment model.

## [3.2.2] - 2024-12-10
### Added
- Add support for using listUsers with pagination.
- Add support for the isFavorite endpoint in the public API
### Changed
- Added support for using listUsers with pagination.
- Marked the favorite field as deprecated in several classes

## [3.2.1] - 2024-10-02
### Added
- Added helper methods to do things like get a sheet by id
  - goes from `getSheet(sheetId, null, null, null, null, null, null, null, null, null)` to `getSheetById(sheetId)`
- Added more test coverage
- Added an EU url as a public variable
- Added tweaks to build with java21 and remove finalize()
- Added SDK tests to our pipeline
### Updated
- Updated mockito and junit test dependency versions
- Updated gradle versions
- Updated copyright year
### Removed
- Eclipse Config Files
### Fixed
- When fetching discussions, comments attachments were not included if pagination parameters were specified
- Fixed formatting issues
- Fixed redocly URL for API documentation
- Fixed broken CI badge
- Fixed deploy commands in CI pipeline


## [3.2.0] - 2023-11-15
### Added
- Added latest Checkstyle version,
  - for violations in `src/main/` the build WILL fail if we exceed 20 violations since we haven't fixed all existing ones yet
  - for violations in `src/test/` the build WILL fail if there is a single violation
- Added more test coverage
- Marked several deprecated features for removal
### Updated
- When we get a non 200 response from Smartsheet, we won't log the entire response to prevent logging PII. Clients can enable debug logging
  if they need more details

## [3.1.2] - 2023-07-25
### Changed
- Converted project to build via Gradle
- Added tests

## [3.1.1] - 2023-07-12
### Fixed
- Fixed Javadoc errors
- Update field annotations to use `@JsonIgnore` instead of `@JsonIgnore(false)` for `rowId` field

## [3.1.0] - 2023-06-16
### Added
- `AbstractRow::setRowId` so it can return a type that matches the child-type
  - `AbstractRow::getRowId` added for symmetry
- `AbstractSheet::setSheetName` and `AbstractSheet:setSheetId` return a type that can be implicitly cast to the child type
- Added `source` to `Sight` model

### Changed
- `AbstractRow` and `AbstractSheet` made `abstract`
- jacoco library upgraded to version that supports newer JVM byte-codes
- methods of `AbstractRow` and `AbstractSheet` that return `this` return a type that can be implicitly cast to the child type
- added streams to cases where they could be used to simplify code
- changed `UserStatus` enum to include `DEACTIVATED`
- upgraded to JUnit 5
- quality of life improvements to tests

### Fixed
- parameter names in `SheetResources`
- access modifiers

## [3.0.0] - 2022-12-09
### Updated
- Migrated SDK to new project
- Updated SDK to Java 11

### Added
- Add Github Actions pipeline

## [2.146.0] - 2021-11-12
### Added
- new field for the resourceManagementType

## [2.126.1] - 2021-08-09
### Changed
- updated several dependencies to remove vulnerabilities

## [2.126.0] - 2021-04-23
### Added
- add support for column formulas

### Changed
- add missing 'ADMIN_SIGHTS` and `READ_CONTACTS`

### Fixed
- add missing scopes `READ_CONTACTS` and `ADMIN_SIGHTS`
- fix typo in `image`

### Other
- build(deps-dev): bump jetty-server

## [2.120.0] - 2021-03-01
### Changed
- update jackson-databind to resolve security vulnerability
- build(deps-dev): bump junit from 4.12 to 4.13.1
- build(deps-dev): bump jetty-server

## [2.101.1] - 2020-08-31
### Changed
- add missing `rules` and `ruleRecipients` to create from template

### Fixed
- fix the double encoding of search terms

## [2.101.0] - 2020-07-29
### Fixed
- Separate ProfileImage model from Image model

## [2.93.0] - 2020-05-20
### Changed
- add support for Webhook subscope

## [2.86.2] - 2020-01-28
### Changed
- add overloads to addCellImage and addSheetSummaryFieldImage to take a
  File or InputStream.

## [2.86.1] - 2019-11-22
### Fixed
- fix NPE if failedItems is null

## [2.86.0] - 2019-11-08
### Added
- type and object definitions to support multi-picklist columns

### Changed
- add format and objectValue to CellHistoryInclusion
- additions to CellDataItem widget contents to support METRIC widgets
  containing sheet summary fields
- dashboards widget model to support widgets that are in an error state
- listColumns needs level support

## [2.83.0] - 2019-08-19
### Added
- support for sheet summary
- add dateFormat to format tables.
- add support for includes argument for ListUsers
- add column description property
- implement addRow, updateRow interfaces that allow includes and excludes

### Changed
- significant overhaul to Sights
- deprecated copyFolder, copyWorkspace methods with unsupported excludes
- serialize dates using ISO-8601 formatting

### Fixed
- resolve jackson-databind security vulnerability

## [2.68.4] - 2019-06-28
### Fixed
- removed errant plugin dependency

## [2.68.3] - 2019-06-27
### Added
- Initial support for an Android http client

### Fixed
- add CARD_DONE tag to supported list of column tags

## [2.68.2] - 2019-06-17
- Resolved Jackson security vulnerability

## [2.68.1] - 2019-06-15
- Travis build automation

## [2.68.0] - 2019-05-09
### Added
- Implement Event Reporting

## [2.2.9] - 2019-02-05
### Added
- Added BASE URI definition for Smartsheetgov
- Added group inclusion for GetCurrentUser

## [2.2.8] - 2019-01-15
### Changed
- Updated versions of Jackson in the POM to resolve security vulnerabilities

## [2.2.7] - 2018-11-06
### Changed
- Updated versions of Jetty and Jackson in the POM to resolve security vulnerabilities

## [2.2.6] - 2018-09-07
### Added
- Multi-contact list

## [2.2.5] - 2018-05-30
### Added
- Sheet import for XLSX and CSV (includes methods to import into Folders and Workspaces)
- Limited support for CHART widget types

### Changed
- Removed old Link model which was replaced by Hyperlink and CellLink

### Fixed
- Hyperlink and HyperlinkSerializer were missing the sightId property

## [2.2.4] - 2018-04-02
### Added
- SmartsheetFactory for creating Smartsheet client objects
- [Automation rules](http://smartsheet-platform.github.io/api-docs/?shell#automation-rules)
- [Cross sheet references](http://smartsheet-platform.github.io/api-docs/?shell#cross-sheet-references)
- Passthrough mechanism to pass raw JSON requests through to the API (documented in the README)
- Sheet filter implementation
- Row sort feature
- User profile properties (including profileImage) to UserModel
- Scope, location and favoriteFlag inclusion to search
- getSheet() ifVersionAfter parameter
- Client method to modify HTTP User-Agent header
- Bulk access to sheet version through sheetVersion inclusion
- Missing title widget for Sights
- Deserialization of error detail
- Logging examples for SimpleLogger and Log4j

### Changed
- HttpClient interface to allow SDK users to inject HTTP headers or implement an HTTP proxy by extending
DefaultHttpClient (a proxy sample is provided in the README)
- Removed ShouldRetry and CalcBackoff interfaces and replaced with HttpClient interface methods. You can now customize
shouldRetry or calcBackoff using the same method as proxy or request header injection (i.e. extend DefaultHttpClient).

### Fixed
- Several deserialization issues with Sights
- Share builders were improperly setting share type
- The rate-limit/backoff retry scenario did not work if the request contained a body because the body stream had not
been reset prior to the retry (PUT/POST).  The Apache HttpClient will raise a NonRepeatableRequestException that is now
handled by the SDK (which resets the body content stream).
- There is a keep-alive race condition that exists when the server disconnects idle connections. If a request is made
in the window in between when the server has disconnected, but before the client has detected the disconnect, it looks
to the client as if the server returned a blank HTTP status line. Idempotent methods (PUT, DELETE, GET) are retried
automatically. A POST request will not be retried automatically by the Apache HttpClient. This fix will handle
NoHttpResponseException exceptions and retry POSTs automatically after resetting the body content stream.


## [2.2.3] - 2017-12-7
### Added
- New `attachFile` overloads that accept an `inputStream`
- Constructors that accept object Id for Cell, Row, and Column
- Additional options when publishing Sheets or Reports

### Changed
- Mock tests
- Logging improvements

## [2.2.1] - 2017-02-13
### Changed
- Fixed flags for Folder/Workspace Copy or Remap

## [2.2.0] - 2017-08-02
### Added
- Logging
- Chained setters
- Sheet.ProjectSettings
- First class support for `PredecessorList` as an implementation of `ObjectValue`

    This is a breaking change if you use PredecessorList

    This code sample shows how to update PredecessorList after updating to 2.2.0:
    ```Java
      Predecessor predecessor = new Predecessor();
      predecessor.setRowId(265775251515268L);         // Id of row to depend on
      predecessor.setType("FS");

      PredecessorList pl = new PredecessorList(new ArrayList<Predecessor>(Arrays.asList(predecessor)));

      Cell cell = new Cell();
      cell.setColumnId(7984328786372484L);           // Id of 'Predecessors' column
      cell.setObjectValue(pl);

      Row row = new Row();
      row.setId(6146602304857988L);                  // Id of row to update
      row.setCells(new ArrayList<Cell>(Arrays.asList(cell)));
      ArrayList<Row> rows = new ArrayList<Row>(Arrays.asList(row));

      smartsheet.sheetResources().rowResources().updateRows(3102146867554180L, rows);  // Id of sheet
    ```

## Earlier releases
- Documented in [Github releases page](https://github.com/smartsheet-platform/smartsheet-java-sdk/releases)
