# Advanced Topics for the Smartsheet SDK for Java

## Table of Contents

- [SDK Architecture](#sdk-architecture)
  - [Client Initialization](#client-initialization)
  - [Request Lifecycle](#request-lifecycle)
  - [Response Handling](#response-handling)
  - [Error Handling and Exceptions](#error-handling-and-exceptions)
  - [Retry Logic and Backoff](#retry-logic-and-backoff)
  - [Serialization and Deserialization](#serialization-and-deserialization)
  - [Pagination Handling](#pagination-handling)
  - [Model Object Construction](#model-object-construction)
  - [Resource Module Organization](#resource-module-organization)
  - [Passthrough Internals](#passthrough-internals)
  - [Logging Infrastructure](#logging-infrastructure)
  - [Authentication Flow](#authentication-flow)
- [Logging](#logging)
- [Passthrough Option](#passthrough-option)
- [Testing](#testing)
- [Working with Report Filter Values](#working-with-report-filter-values)
- [Android](#android)
- [Overriding HTTP Client Behavior](#overriding-http-client-behavior)
- [Event Reporting](#event-reporting)
- [Working with Smartsheetgov.com Accounts](#working-with-smartsheetgovcom-accounts)
- [Working with Smartsheet Regions Europe Accounts](#working-with-smartsheet-regions-europe-accounts)

## SDK Architecture

This section provides detailed insight into the internal architecture of the Smartsheet Java SDK, covering the core subsystems that power API interactions. Understanding these patterns enables advanced customization, troubleshooting, and integration work.

### Client Initialization

The SDK entry point begins with the `SmartsheetBuilder` class (`SmartsheetBuilder.java:29-276`), which provides a fluent API for configuring and constructing `Smartsheet` client instances. Token resolution follows a priority order: constructor parameter via `setAccessToken()` first, then `SMARTSHEET_ACCESS_TOKEN` environment variable in `build()` method (`SmartsheetBuilder.java:257-260`), with null tokens passed through to `SmartsheetImpl` for downstream validation. The builder supports three base URI constants for different Smartsheet environments: `US_BASE_URI` ("https://api.smartsheet.com/2.0/"), `EU_BASE_URI` ("https://api.smartsheet.eu/2.0/"), and `GOV_BASE_URI` ("https://api.smartsheetgov.com/2.0/"), with `DEFAULT_BASE_URI` aliasing to `US_BASE_URI` (`SmartsheetBuilder.java:79-96`). The `build()` method orchestrates construction by defaulting baseURI to `DEFAULT_BASE_URI` if not set, creating a `SmartsheetImpl` instance with the configured baseURI, accessToken, and optional httpClient/jsonSerializer, then applying optional configuration like changeAgent, assumedUser, and maxRetryTimeMillis via setters (`SmartsheetBuilder.java:252-274`). The `SmartsheetImpl` constructor (`SmartsheetImpl.java:309-344`) validates the baseURI (null check and empty check), stores the accessToken in an `AtomicReference<String>` for thread-safe updates, initializes the JsonSerializer (defaulting to `JacksonJsonSerializer` if not provided), initializes the HttpClient (defaulting to `DefaultHttpClient` with Apache HttpComponents if not provided), sets up AtomicReferences for assumedUser, changeAgent, and userAgent, and initializes all resource AtomicReferences to null for lazy loading. Thread-safety is achieved through extensive use of `AtomicReference` for all mutable fields—accessToken, assumedUser, changeAgent, userAgent, and all resource references—ensuring atomic modifications and safe concurrent access without explicit synchronization. Resource loading follows a lazy initialization pattern where resource getters (e.g., `sheetResources()`, `userResources()`) check if the AtomicReference contains null, and if so, use `compareAndSet(null, new ResourceImpl(this))` to atomically initialize the resource on first access, caching the instance for subsequent calls. The user agent is generated from SDK version properties and optional caller information, formatted as "Smartsheet SDK for Java/{version}" or with custom caller details if provided.

**Cross-reference:** See [Authentication Flow](#authentication-flow) for token handling details, [Resource Module Organization](#resource-module-organization) for lazy loading patterns, and [Logging Infrastructure](#logging-infrastructure) for client initialization logging.

### Request Lifecycle

API requests flow through a template method pattern where resource methods validate parameters and delegate to `AbstractResources` CRUD operations for consistent handling. Resource methods (in implementations like `SheetResourcesImpl`, `UserResourcesImpl`) begin by validating required parameters using `Util.throwIfNull()` and `Util.throwIfEmpty()`, then call `AbstractResources` template methods (`AbstractResources.java:212-500+`) such as `getResource(path, objectClass)` for GET requests, `createResource(path, objectClass, object)` for POST requests, `updateResource(path, objectClass, object)` for PUT requests, `deleteResource(path, objectClass)` for DELETE requests, and various `listResources()` variants for collection endpoints. These template methods construct `HttpRequest` objects via `createHttpRequest(uri, HttpMethod)`, which creates a request with the resolved URI and appropriate HTTP method (GET, POST, PUT, DELETE, PATCH). For POST/PUT/PATCH operations with request bodies, the template method serializes the object to JSON using `smartsheet.getJsonSerializer().serialize(object, outputStream)`, writes to a `ByteArrayOutputStream`, wraps the bytes in a `ByteArrayInputStream`, creates an `HttpEntity` with content type "application/json", sets the content and content length, and attaches the entity to the `HttpRequest`. Query parameters are encoded and appended to the URI during resolution against the base URI. Header injection occurs in `createHttpRequest()` where the method retrieves the access token from `smartsheet.getAccessToken().get()`, constructs the "Bearer {token}" Authorization header, adds the User-Agent header with SDK version information, and optionally includes Assume-User header (from `smartsheet.getAssumedUser().get()`) for admin impersonation and Smartsheet-Change-Agent header (from `smartsheet.getChangeAgent().get()`) for audit tracking. The constructed `HttpRequest` is then passed to `smartsheet.getHttpClient().request(request)` which executes the HTTP call with retry logic and returns an `HttpResponse`. Response handling branches on status code: 200 triggers deserialization via `smartsheet.getJsonSerializer().deserialize(objectClass, inputStream)` or `deserializeResult(objectClass, inputStream)` for Result-wrapped responses, while non-200 status codes invoke `handleError(response)` to throw appropriate exceptions. Connection lifecycle management occurs in try-finally blocks where `smartsheet.getHttpClient().releaseConnection()` ensures resources are freed regardless of success or failure.

**Cross-reference:** See [Response Handling](#response-handling) for status code processing and deserialization, [Serialization and Deserialization](#serialization-and-deserialization) for JSON handling, [Authentication Flow](#authentication-flow) for header injection details, and [Retry Logic and Backoff](#retry-logic-and-backoff) for HttpClient retry behavior.

```
┌──────────────────┐
│ Resource Method  │  e.g., sheetResources().getSheet(id)
└────────┬─────────┘
         │ Validates parameters
         ▼
┌──────────────────┐
│ AbstractResources│  getResource(path, Sheet.class)
└────────┬─────────┘
         │ Creates HttpRequest, injects headers
         ▼
┌──────────────────┐
│ HttpClient       │  request(httpRequest) with retry logic
└────────┬─────────┘
         │ Returns HttpResponse
         ▼
┌──────────────────┐
│ Response Handler │  Deserializes to Sheet object
└──────────────────┘
```

### Response Handling

Response handling in `AbstractResources` template methods branches on HTTP status codes using switch statements to separate success and error paths. The 200 status code triggers the success path (`AbstractResources.java:228-258`) where the method retrieves the response entity's InputStream, optionally clones the stream for logging purposes using `StreamUtil.cloneContent(inputStream, contentLength, ByteArrayOutputStream)` to preserve content for both logging and deserialization (when `log.isInfoEnabled()` returns true), deserializes the JSON to the target object type via `smartsheet.getJsonSerializer().deserialize(objectClass, inputStream)`, and returns the typed object. For create operations, the response is wrapped in a `Result<T>` envelope, requiring `deserializeResult(objectClass, inputStream)` to extract the nested result via `getResult()` method (`AbstractResources.java:296-324`). Non-200 status codes trigger the error path where `handleError(response)` is invoked to process the failure. Jackson deserialization flows through `JacksonJsonSerializer` which uses the static `ObjectMapper` to read the InputStream and map JSON to POJO via `ObjectMapper.readValue(inputStream, objectClass)`, automatically handling property mapping, type coercion, null handling per the configured inclusion policy, and custom deserializers for special types. When JSON parsing fails, the method catches `JsonParseException` or `JsonMappingException`, logs the failure with the captured content string (for diagnostic purposes), and wraps the exception in `SmartsheetException`. InputStream lifecycle management is critical—the finally block always calls `smartsheet.getHttpClient().releaseConnection()` to ensure underlying HTTP connections are properly released and returned to the connection pool, preventing connection leaks even when exceptions occur during deserialization. Response content cloning serves dual purposes: enabling INFO-level logging of response bodies (subject to truncation limits controlled by `Smartsheet.responseLogChars` system property) and preserving the original stream for deserialization, since InputStreams can only be read once and need to be copied for multiple consumers.

**Cross-reference:** See [Error Handling and Exceptions](#error-handling-and-exceptions) for handleError() processing, [Serialization and Deserialization](#serialization-and-deserialization) for Jackson deserialization details, [Logging Infrastructure](#logging-infrastructure) for response logging and content cloning, and [Request Lifecycle](#request-lifecycle) for the complete request-response flow.

### Error Handling and Exceptions

The SDK implements a typed exception hierarchy rooted at `SmartsheetException` (base class for all SDK exceptions), which extends to `SmartsheetRestException` (base for REST API errors), which further specializes into status-code-specific exceptions: `InvalidRequestException` (400, 405, 500), `AuthorizationException` (401, 403), `ResourceNotFoundException` (404), `ServiceUnavailableException` (503), with `AccessTokenExpiredException` extending `AuthorizationException` for expired token scenarios. The `ErrorCode` enum (`AbstractResources.java:88-170`) maps HTTP status codes to exception classes, defining each entry with an errorCode integer and an exceptionClass that implements `SmartsheetRestException`, providing `getException(Error)` method that uses reflection to instantiate the exception with the Error model payload via `exceptionClass.getConstructor(Error.class).newInstance(error)`. When non-200 responses are received, the `handleError()` method (`AbstractResources.java:1070-1120`) deserializes the response body to an `Error` model object containing errorCode, message, refId (correlation ID for support requests), and optional detail fields, looks up the `ErrorCode` enum entry via `ErrorCode.getErrorCode(statusCode)`, retrieves the exception instance via `errorCode.getException(error)`, and throws the constructed exception. The `Error` model (`com/smartsheet/api/models/Error.java`) captures complete error details from the API response including the numeric error code (distinct from HTTP status code), human-readable message, reference ID for debugging and support correlation, and additional detail information for specific error types. Exception construction through reflection enables the ErrorCode enum to maintain the mapping without explicit instantiation logic for each exception type, though it requires each exception class to provide a constructor accepting an Error parameter. Special handling exists for certain error scenarios: AccessTokenExpiredException specifically identifies expired tokens (typically 401 with specific error codes), allowing applications to distinguish token expiration from other authorization failures and trigger token refresh workflows.

**Cross-reference:** See [Response Handling](#response-handling) for error path invocation, [Retry Logic and Backoff](#retry-logic-and-backoff) for retry-eligible error codes (4001-4004).

**Implementation locations:**
- Exception classes: `com/smartsheet/api/SmartsheetException.java`, `com/smartsheet/api/SmartsheetRestException.java`, `com/smartsheet/api/*Exception.java`
- ErrorCode enum: `AbstractResources.java:88-170`
- handleError method: `AbstractResources.java:1070-1120`
- Error model: `com/smartsheet/api/models/Error.java`

### Retry Logic and Backoff

Retry logic is implemented in `DefaultHttpClient.request()` (`DefaultHttpClient.java:186-332`) through a while(true) loop that executes HTTP requests with automatic retry for transient failures. Before entering the retry loop, the method checks if the request body stream supports mark/reset via `bodyStream.markSupported()`, and if not, attempts to wrap it in a `ByteArrayInputStream` created from `StreamUtil.readBytesFromStream(bodyStream)` to enable retries by supporting stream reset (`DefaultHttpClient.java:202-213`). Within the retry loop, an attempt counter tracks retry attempts, the request is executed via `httpClient.execute(apacheHttpRequest, context)`, and the response is processed—200 status codes break the loop immediately, while non-200 responses enter retry evaluation. The `shouldRetry()` method (`DefaultHttpClient.java:475-512`) determines retry eligibility by first validating the response Content-Type starts with "application/json" (non-JSON responses cannot be parsed for error codes), deserializing the response body to an `Error` object to extract the errorCode, and checking the errorCode against retry-eligible values: 4001 (system maintenance—Smartsheet offline for maintenance), 4002 (server timeout—server-side timeout exceeded), 4003 (rate limit—too many requests), 4004 (unexpected error with retry recommendation—transient server error). If shouldRetry returns false, the loop breaks and the error response is returned to the caller. For retry-eligible errors, `calcBackoff()` (`DefaultHttpClient.java:450-464`) computes the delay using exponential backoff with jitter: `(2^previousAttempts * 1000) + random.nextInt(1000)`, producing delays of 1000-2000ms for attempt 0, 2000-3000ms for attempt 1, 4000-5000ms for attempt 2, 8000-9000ms for attempt 3, etc. The method enforces `maxRetryTimeMillis` (default 15000ms, configurable via `setMaxRetryTimeMillis()`) by checking if `totalElapsedTimeMillis + backoffMillis > maxRetryTimeMillis`, returning -1 to signal retry termination when the time budget is exhausted. Before retrying, both request and response body streams are reset via `bodyStream.reset()` and `contentStream.reset()` (after marking with `mark()` before the attempt) to allow re-reading, and the connection is released via `releaseConnection()` to return it to the pool. Special handling exists for non-idempotent methods (POST, PATCH) which are normally not auto-retried by Apache HttpClient, but the SDK retries them on `NoHttpResponseException` (empty response, safe to retry) after resetting the request body stream. All retry attempts are logged at INFO level with the calculated backoff duration for observability.

**Cross-reference:** See [Error Handling and Exceptions](#error-handling-and-exceptions) for error code details, [Request Lifecycle](#request-lifecycle) for request execution context, and [Logging Infrastructure](#logging-infrastructure) for retry logging.

**Backoff formula sequence:**
```
Attempt 0: 2^0 * 1000 + random(0-1000) = 1000-2000ms
Attempt 1: 2^1 * 1000 + random(0-1000) = 2000-3000ms
Attempt 2: 2^2 * 1000 + random(0-1000) = 4000-5000ms
Attempt 3: 2^3 * 1000 + random(0-1000) = 8000-9000ms
```

### Serialization and Deserialization

The SDK uses Jackson for JSON serialization and deserialization, configured through a static `ObjectMapper` instance in `JacksonJsonSerializer` (`JacksonJsonSerializer.java:70-126`) that is shared across all SDK operations for thread-safety and performance. The ObjectMapper configuration sets `FAIL_ON_UNKNOWN_PROPERTIES` to false (allowing API to add new fields without breaking deserialization), `READ_UNKNOWN_ENUM_VALUES_AS_NULL` to true (gracefully handling new enum values), `NON_NULL` serialization inclusion (omitting null fields from JSON), `WRITE_ENUMS_USING_TO_STRING` and `READ_ENUMS_USING_TO_STRING` (using enum's toString() method for serialization), and `WRITE_DATES_AS_TIMESTAMPS` to false (formatting dates as ISO 8601 strings). Date formatting uses `SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")` with UTC timezone, ensuring all timestamps serialize to ISO 8601 format with 'Z' suffix indicating UTC. Custom deserializers are registered via `SimpleModule` instances for polymorphic and special-case types: `FormatDeserializer` converts format strings to Format objects, `ObjectValueDeserializer` handles polymorphic ObjectValue subtypes (PrimitiveObjectValue, ContactObjectValue, etc.), `RecipientDeserializer` handles polymorphic Recipient subtypes (UserRecipient, GroupRecipient), `WidgetContentDeserializer` handles polymorphic WidgetContent subtypes for dashboard widgets, and `ErrorDeserializer` provides custom Error model deserialization. Custom serializers include `HyperlinkSerializer` for Hyperlink objects, `PrimitiveObjectValueSerializer` for primitive ObjectValue handling, and `CellSerializer` via `CellSerializerModifier` for Cell formula and value serialization logic. The `IdentifiableModelMixin` is registered for the `IdentifiableModel` base class to ignore `getId()` during serialization (via `@JsonIgnore` annotation in the mixin), preventing duplicate id fields when objects are serialized—the id field itself is serialized, but the getter is ignored. `JavaTimeModule` is registered to support java.time.* types (LocalDate, LocalDateTime, Instant, etc.) for modern date/time handling. The serializer exposes methods `serialize(Object, OutputStream)` to write objects to JSON streams, `deserialize(Class<T>, InputStream)` to read JSON into typed objects, `deserializeResult(Class<T>, InputStream)` to read Result-wrapped responses and extract the inner result, and `deserializeList(Class<T>, InputStream)` to read JSON arrays into typed lists. For generic collection deserialization (e.g., PagedResult<Sheet>, TokenPaginatedResult<UserPlan>), Jackson's `TypeReference` enables type-safe deserialization of parameterized types by capturing generic type information at compile time.

**Cross-reference:** See [Request Lifecycle](#request-lifecycle) for serialization in request construction, [Response Handling](#response-handling) for deserialization in response processing, [Model Object Construction](#model-object-construction) for POJO structure and Jackson annotations, and [Pagination Handling](#pagination-handling) for generic collection deserialization.

### Pagination Handling

The SDK supports three distinct pagination patterns implemented through specialized result wrapper classes, each optimized for different API endpoint requirements and use cases. `PagedResult<T>` (`PagedResult.java:1-82`) implements offset-based pagination with fields `pageNumber` (current page, 1-indexed), `pageSize` (items per page), `totalCount` (total items across all pages), `totalPages` (total number of pages), `lastKey` (optional optimization token for efficient large dataset navigation), and `data` (List<T> containing current page items). API consumers request specific pages via `page` and `pageSize` query parameters, or use `includeAll=true` to retrieve all results without pagination. This pattern suits browseable datasets where users need total count information for UI pagination controls (e.g., "Page 1 of 10") and the ability to jump to arbitrary pages. `TokenPaginatedResult<T>` (`TokenPaginatedResult.java:1-86`) implements cursor-based pagination with fields `data` (List<T> of items) and `lastKey` (continuation token for next page), plus convenience method `hasMorePages()` that returns true when lastKey is non-null and non-empty. API consumers pass `maxItems` to control page size and `lastKey` from the previous response to fetch the next page, iterating until `lastKey` is null. This pattern efficiently handles large datasets and real-time feeds where total count is unavailable or expensive to compute, supporting only forward iteration without random page access. `EventResult` (`EventResult.java:1-92`) implements stream-position pagination specifically for event streams, with fields `data` (List<Event> of events), `moreAvailable` (Boolean indicating if more events exist), and `nextStreamPosition` (String token for continuing the stream). The initial request uses `since` parameter (accepting UNIX timestamp or ISO 8601 date string) to specify the starting point in the event stream, subsequent requests use `streamPosition` parameter with the `nextStreamPosition` value from the previous response, and iteration continues while `moreAvailable` returns true. All three classes use Java generics (`<T>`) for type-safe collection handling—`PagedResult<Sheet>` contains sheets, `TokenPaginatedResult<UserPlan>` contains user plans, `EventResult` specifically contains Event objects. Jackson automatically deserializes JSON property names to Java fields during response processing, handling camelCase conversion seamlessly. Iteration patterns vary by type: PagedResult clients increment page numbers manually, TokenPaginatedResult clients check `hasMorePages()` and pass `lastKey`, EventResult clients loop while `moreAvailable` is true and pass `nextStreamPosition`.

**Cross-reference:** See [Response Handling](#response-handling) for deserialization of pagination wrapper objects, [Serialization and Deserialization](#serialization-and-deserialization) for generic type handling and Jackson property mapping, and [Model Object Construction](#model-object-construction) for POJO patterns.

**Pagination comparison:**

| Pattern | PagedResult | TokenPaginatedResult | EventResult |
|---------|-------------|----------------------|-------------|
| Type | Offset-based | Cursor-based | Stream position |
| Total count | Yes (totalCount) | No | No |
| Use case | Browseable lists | Large datasets | Event streams |
| Query params | page, pageSize | lastKey, maxItems | since, streamPosition |
| Continuation | Page numbers | lastKey token | nextStreamPosition |
| Direction | Bidirectional | Forward-only | Forward-only |

### Model Object Construction

Model objects follow the Plain Old Java Object (POJO) pattern with private fields, public getters/setters adhering to JavaBean conventions, and minimal Jackson annotations. Field declarations are private (e.g., `private String name;`, `private Long id;`), getters follow naming convention `getFieldName()` and return the field value, setters follow convention `setFieldName(Type value)` and assign the field, with some newer models implementing fluent setters that return `this` for method chaining (e.g., `TokenPaginatedResult.setData(data).setLastKey(key)`). Jackson annotations are used sparingly: `@JsonProperty` explicitly maps property names when they deviate from field names (rare—SDK relies on naming conventions), `@JsonIgnore` excludes properties from serialization/deserialization (commonly used on derived/computed properties), `@JsonInclude` controls null value handling (typically configured globally via ObjectMapper rather than per-property). The `IdentifiableModel` base class provides getId()/setId() methods for model objects with ID fields, with `IdentifiableModelMixin` applying `@JsonIgnore` to the `getId()` method to prevent duplicate id serialization—the private `id` field itself is serialized, but the getter is ignored. Builder pattern appears selectively for complex objects requiring many optional parameters, but is not pervasive across the SDK—most models use default constructors plus setters. Generic wrapper types provide type-safe API responses: `Result<T>` wraps single-object responses with result/error metadata, `PagedResult<T>` wraps paginated collections with page metadata, `TokenPaginatedResult<T>` wraps cursor-paginated collections, `BulkItemResult<T>` wraps bulk operation results with per-item success/failure. Enum types reside in `com/smartsheet/api/models/enums/` and implement `toString()` for serialization (Jackson configured with WRITE_ENUMS_USING_TO_STRING), with many providing `fromValue(String)` static methods for reverse lookup during deserialization. Object composition uses typed fields for relationships—`Sheet` contains `List<Row> rows`, `Row` contains `List<Cell> cells`, `User` contains `String email`—with Jackson handling recursive deserialization automatically. Model constructors are always no-argument (default constructors) to support Jackson deserialization via reflection, with object initialization occurring through setter calls during JSON mapping rather than constructor parameters.

**Cross-reference:** See [Serialization and Deserialization](#serialization-and-deserialization) for Jackson configuration and annotations, [Pagination Handling](#pagination-handling) for generic wrapper types, and [Response Handling](#response-handling) for deserialization to POJO instances.

### Resource Module Organization

The SDK organizes API operations through an interface/implementation separation pattern where public interfaces define API contracts and internal implementations provide concrete logic. Public interfaces (e.g., `SheetResources`, `UserResources`, `FolderResources`) located in `com/smartsheet/api/` declare all available operations with clear method signatures and Javadoc, while implementation classes (e.g., `SheetResourcesImpl`, `UserResourcesImpl`, `FolderResourcesImpl`) located in `com/smartsheet/api/internal/` extend `AbstractResources` and implement the corresponding interface. `AbstractResources` (`AbstractResources.java:1-1204`) serves as the base class providing template methods for common CRUD operations—`getResource(path, objectClass)` for GET requests, `createResource(path, objectClass, object)` for POST requests, `updateResource(path, objectClass, object)` for PUT requests, `deleteResource(path, objectClass)` for DELETE requests, `listResources(path, objectClass)` for collection GET requests—plus utility methods like `createHttpRequest()`, `handleError()`, and content cloning helpers. Resource implementations do not override these template methods; instead, they compose operations by calling the inherited templates with appropriate paths and parameters, then add domain-specific operations that don't fit the generic templates (e.g., `publishSheet()`, `sendSheet()`, `moveRow()`). Lazy instantiation occurs in `SmartsheetImpl` (`SmartsheetImpl.java:324-343`) where each resource type has a dedicated `AtomicReference` field (e.g., `private final AtomicReference<SheetResources> sheets`) initialized to null in the constructor. Resource getter methods (e.g., `sheetResources()`) implement a thread-safe lazy loading pattern: check if `sheets.get() == null`, and if so, call `sheets.compareAndSet(null, new SheetResourcesImpl(this))` to atomically initialize, then return `sheets.get()` which now contains the cached instance. The `compareAndSet()` ensures only one thread successfully initializes the resource even under concurrent access, with losing threads simply using the winner's instance. Constructor injection passes the `SmartsheetImpl` instance (typically `this`) to resource implementation constructors, which store it in a protected final field for accessing httpClient, jsonSerializer, baseURI, and other shared infrastructure. Nested resource accessors follow the same pattern—`SheetResources.rowResources()` returns `SheetRowResources`, `SheetResources.columnResources()` returns `SheetColumnResources`—often instantiated lazily on first access. Thread-safety derives from immutability: resource implementations hold no mutable state beyond the SmartsheetImpl reference, all request-specific data flows through method parameters and local variables, and lazy initialization via AtomicReference.compareAndSet() is atomic. Method naming conventions create predictable APIs: `list*()` for collection retrieval, `get*()` for single-object retrieval by ID, `add*()` for creation, `update*()` for modification, `delete*()` for removal, plus domain-specific verbs like `publish*()`, `send*()`, `share*()`.

**Cross-reference:** See [Client Initialization](#client-initialization) for SmartsheetImpl construction and resource initialization, [Request Lifecycle](#request-lifecycle) for how resource methods use AbstractResources templates, and [Response Handling](#response-handling) for template method response processing.

**Lazy loading pattern:**
```java
public SheetResources sheetResources() {
    if (sheets.get() == null) {
        sheets.compareAndSet(null, new SheetResourcesImpl(this));
    }
    return sheets.get();
}
```

### Passthrough Internals

The Passthrough mechanism provides raw JSON-level API access by bypassing the SDK's type system, enabling developers to call unsupported or experimental endpoints without waiting for SDK updates. `PassthroughResourcesImpl` implements four methods mapping to HTTP verbs: `postRequest(endpoint, payload, queryParams)`, `getRequest(endpoint, queryParams)`, `putRequest(endpoint, payload, queryParams)`, and `deleteRequest(endpoint)`. Each method constructs an `HttpRequest` by resolving the endpoint path against the base URI (e.g., `smartsheet.getBaseURI().resolve(endpoint)`), setting the appropriate `HttpMethod` enum value (POST, GET, PUT, DELETE), and for requests with payloads, accepting input as `String` (raw JSON), `Map<String,Object>`, or `JSONObject` instance. Payload handling wraps the input in a `JSONObject` if it's not already one, serializes via `JSONObject.serialize()` which simply returns the inner data structure unchanged (no POJO-to-JSON conversion), writes to an `HttpEntity`, and attaches to the request. Query parameters are passed as `Map<String,Object>` and converted to URL query string format during URI resolution. Request execution follows the standard flow—calling `smartsheet.getHttpClient().request(httpRequest)`—but response handling diverges: instead of deserializing to typed model objects, the response InputStream is parsed to a `Map<String,Object>` using Jackson's `ObjectMapper.readValue(inputStream, Map.class)`, wrapped in a `JSONObject`, and returned to the caller. The `JSONObject` class is a lightweight wrapper storing raw data in a `Map<String,Object>` or JSON string, providing `serialize()` returning the map unchanged (no transformation), `toDict()` returning the map directly, and `getResultObject()` extracting nested result from Result-wrapped responses. This design bypasses the entire type system: no Jackson annotations are consulted, no POJO validation occurs, no enum coercion happens, no date formatting is applied—developers receive and send raw JSON structures matching API contracts exactly. Use cases include accessing newly released API features not yet modeled in the SDK, testing experimental endpoints, constructing custom request formats not supported by typed methods, and debugging API contracts by inspecting raw JSON. Limitations include no compile-time type safety (typos caught only after API round-trip), no automatic serialization benefits (dates, enums, naming conventions handled manually), no typed return objects (generic JSONObject wrapper), no pagination helpers (manual navigation logic), API URL construction errors caught only at runtime, limited query parameter support (DELETE doesn't accept query params per implementation), and no response validation beyond Error detection (malformed responses may cause runtime failures). The migration path is straightforward: passthrough usage signals missing SDK features, maintainers add typed methods based on common passthrough patterns, and applications gain type safety by replacing passthrough calls with typed methods in subsequent SDK releases.

**Cross-reference:** See [Request Lifecycle](#request-lifecycle) for standard HttpRequest construction flow, [Serialization and Deserialization](#serialization-and-deserialization) for how passthrough bypasses Jackson type system, and [Response Handling](#response-handling) for comparison with typed response processing.

### Logging Infrastructure

The SDK employs a dual logging system: SLF4J for production logging with pluggable framework support (Logback, Log4j, java.util.logging), and Trace for console-based development/debugging with system property configuration. SLF4J integration occurs through `LoggerFactory.getLogger(ClassName.class)` calls in `AbstractResources` and `DefaultHttpClient`, logging at three levels: INFO for successful API requests with method/URI/status/duration (e.g., "GET https://api.smartsheet.com/2.0/sheets/123, Response Code:200, Request completed in 245 ms"), DEBUG for request/response summaries via `RequestAndResponseData` objects that capture headers and body summaries with JSON formatting, and WARN for failed requests with complete details including full request/response bodies for diagnostic purposes. The `logRequest()` hook in `DefaultHttpClient` (`DefaultHttpClient.java:171-177`) is invoked after every HTTP request completes, receiving the `HttpRequestBase`, `HttpEntitySnapshot` for request, `HttpResponse`, `HttpEntitySnapshot` for response, and duration in milliseconds, logging INFO message with basic request metadata, and calling `RequestAndResponseData.of(request, requestEntity, response, responseEntity, REQUEST_RESPONSE_SUMMARY)` to create a DEBUG-level detailed log entry. `RequestAndResponseData` captures request headers (e.g., Authorization, User-Agent, Content-Type), request body (full or summarized depending on Trace set), response headers (e.g., Content-Type, Content-Length, X-Smartsheet-RequestId), and response body (full or summarized), supporting selective logging via `Set<Trace>` parameter—the `REQUEST_RESPONSE_SUMMARY` constant includes RequestHeaders, RequestBodySummary, ResponseHeaders, and ResponseBodySummary for balanced verbosity. Trace console logging is configured via system properties: `Smartsheet.trace.parts` specifies what to log (comma-separated Trace enum values: RequestHeaders, RequestBody, RequestBodySummary, ResponseHeaders, ResponseBody, ResponseBodySummary, Request, Response), `Smartsheet.trace.pretty` enables/disables JSON pretty-printing (default true), and output is written to a configurable `PrintWriter` (default `System.out`). The `Trace` enum defines logging granularity flags, with `Trace.parse(String)` converting the property string to a `Set<Trace>` for evaluation. Pretty print formatting (`Smartsheet.trace.pretty=true`) indents JSON with proper nesting for human readability, while compact format (`false`) writes single-line JSON for machine parsing. Response log truncation prevents log overflow via the `Smartsheet.responseLogChars` system property (`AbstractResources.PROPERTY_RESPONSE_LOG_CHARS`) which sets a maximum character limit for logged response bodies (default implementation respects this limit). Performance implications vary by level: INFO logging adds negligible overhead (simple string concatenation), DEBUG adds moderate overhead (JSON parsing and formatting for every request/response), and Trace adds significant overhead (full request/response capture to console). Multipart request body logging is suppressed (via `isMultipart()` check) to avoid dumping large file uploads to logs, and download responses skip body logging when operation specifies a download path (`operation[dl_path]` check) to conserve memory on binary content.

**Cross-reference:** See [Client Initialization](#client-initialization) for logger setup, [Request Lifecycle](#request-lifecycle) for when logRequest() is invoked, [Response Handling](#response-handling) for content cloning that enables logging, and [Retry Logic and Backoff](#retry-logic-and-backoff) for retry attempt logging.

**Implementation locations:**
- SLF4J logging: `AbstractResources.java` (static Logger field), `DefaultHttpClient.java` (logRequest method lines 171-177)
- Trace logging: `DefaultHttpClient.java:97-130` (static initialization), `252-257` (trace logging in request method)
- RequestAndResponseData: `com/smartsheet/api/internal/http/RequestAndResponseData.java`
- Trace enum: `com/smartsheet/api/Trace.java`

### Authentication Flow

The SDK implements bearer token authentication where access tokens are transmitted in the Authorization header using the format "Bearer {token}" for every API request. Token resolution occurs in `SmartsheetBuilder.build()` (`SmartsheetBuilder.java:252-260`) following a two-stage priority: first checking if a token was explicitly set via `setAccessToken(token)` during builder configuration, then falling back to the `SMARTSHEET_ACCESS_TOKEN` environment variable via `System.getenv("SMARTSHEET_ACCESS_TOKEN")`, with null tokens passed through to `SmartsheetImpl` without throwing exceptions (downstream validation handles missing tokens). Token storage in `SmartsheetImpl` uses `AtomicReference<String>` (`SmartsheetImpl.java:77, 314`) for thread-safe access and updates, initialized in the constructor with `new AtomicReference<>(accessToken)` and accessible via `getAccessToken()` returning the AtomicReference, with `setAccessToken(token)` providing runtime token updates via `accessToken.set(token)`. Header injection happens in `AbstractResources.createHttpRequest()` where the method retrieves the token using `smartsheet.getAccessToken().get()`, constructs the Authorization header value as "Bearer " concatenated with the token, and adds it to the HttpRequest headers map before execution. Optional headers support specialized scenarios: Assume-User header enables admin impersonation by setting the value to an email address via `smartsheet.setAssumedUser(email)`, retrieved in createHttpRequest via `smartsheet.getAssumedUser().get()` and added as "Assume-User: {email}" header to allow system administrators to make API calls on behalf of other users; Smartsheet-Change-Agent header provides audit trail tracking by setting an identifier via `smartsheet.setChangeAgent(identifier)`, retrieved via `smartsheet.getChangeAgent().get()` and added as "Smartsheet-Change-Agent: {identifier}" header to tag API changes with integration/application identity for webhook tracking and audit logs; User-Agent header identifies the SDK and application, automatically generated from SDK version properties and optional caller information, retrieved via `smartsheet.getUserAgent().get()` and formatted as "Smartsheet SDK for Java/{version}" or with custom caller details. No automatic token refresh mechanism exists—the SDK assumes long-lived access tokens obtained externally (via OAuth2 flow or API access page), with applications responsible for detecting token expiration (401 responses with specific error codes, or `AccessTokenExpiredException`) and obtaining replacement tokens through appropriate channels. No OAuth flow is implemented within the SDK—token acquisition happens outside the SDK boundary via OAuth2 authorization code flow, client credentials flow, or personal access token generation, with the SDK only storing and transmitting pre-obtained tokens. Thread-safety for authentication state relies on `AtomicReference` wrappers for token, assumedUser, and changeAgent, enabling safe concurrent access and updates across multiple threads without explicit synchronization or locks—`get()` retrieves current value atomically, `set()` updates value atomically, ensuring no torn reads or writes even under contention. Token visibility through `getAccessToken()` enables debugging and token validation but should not be used for logging (security risk—tokens should never appear in log files), with careful handling required in any code accessing the token value.

**Cross-reference:** See [Client Initialization](#client-initialization) for token resolution in SmartsheetBuilder.build() and AtomicReference storage, [Request Lifecycle](#request-lifecycle) for header injection in createHttpRequest().

**Authentication header format:**
```
Authorization: Bearer ll352u9jujauoqz4gstvsae05
Assume-User: admin@example.com (optional)
Smartsheet-Change-Agent: MyIntegration/1.0 (optional)
User-Agent: Smartsheet SDK for Java/3.11.0
```

## Logging
There are two types of logging used by the Smartsheet Java SDK:

### Console Logger
The console logger logs REST API traffic directly to the console. The console logger is verbose, and as such is best 
used when developing new code or features. To use the console logger, set two system properties:
```java
System.setProperty("Smartsheet.trace.parts", "RequestBodySummary, ResponseBodySummary");
System.setProperty("Smartsheet.trace.pretty", "true");
```
*Smartsheet.trace.pretty* - if ```true```, formats log messages for improved JSON readability. 
If ```Smartsheet.trace.pretty``` is ```false``` the console logger will use a compact format. 

*Smartsheet.trace.parts* - determines what portions of the API traffic are logged. Valid trace parts entries include:
- RequestHeaders
- RequestBody
- RequestBodySummary
- ResponseHeaders
- ResponseBody
- ResponseBodySummary
- Request (RequestHeaders + RequestBodySummary)
- Response (ResponseHeaders + ResponseBodySummary)

By default, console log entries are truncated at 1024 characters. You can change the truncation limit by defining a 
system property ```Smartsheet.trace.truncateLen``` and setting it equal to the desired truncation limit, for example:
```java
System.setProperty("Smartsheet.trace.truncateLen", "512");
```

### Logging Framework
The Smartsheet Java SDK also has a dependency on the SLF4J facade. SLF4J is configurable at or post distribution and
is meant for production environments. More information about SLF4J and the supported logging frameworks is available 
[here](https://www.slf4j.org). 

Using SLF4J, the Smartsheet Java SDK logs all API queries including HTTP method, URI, HTTP status  and response time 
to `INFO`. API calls that fail (HTTP status != 200) are fully logged (request, response and full bodies) to `WARN`. 
Finally, successful (HTTP status 200) request and response summaries are logged to `DEBUG`.
 
The build file for the Smartsheet Java SDK also includes a test only dependency on the ```slf4j-simple``` logging framework.
Details on how to configure logging are framework dependant, however, a usage example for the Simple logger can be 
found in the *simplelogger.properties* file in the Sample folder. Alternately, a usage example for Log4j can 
be found in the java-read-write-sheet example [here](https://github.com/smartsheet-samples/java-read-write-sheet).

## Passthrough Option

If there is an API feature that is not yet supported by the Java SDK, there is a passthrough option that allows you to 
pass and receive raw JSON objects.

To invoke the passthrough, your code can call one of the following four methods:

`jsonResponse = smartsheet.passthroughResources().postRequest(endpoint, payload, parameters);`

`jsonResponse = smartsheet.passthroughResources().getRequest(endpoint, parameters);`

`jsonResponse = smartsheet.passthroughResources().putRequest(endpoint, payload, parameters);`

`jsonResponse = smartsheet.passthroughResources().deleteRequest(endpoint);`

* `endpoint (String)`: The specific API endpoint you wish to invoke. The client object base URL gets prepended to the caller’s 
endpoint URL argument, e.g., if endpoint is 'sheets' an HTTP GET is requested from the URL https://api.smartsheet.com/2.0/sheets
* `payload (String)`: The data to be passed through in the request payload as a string.
* `query_params (Hashmap<String, Object>)`: An optional list of query parameters.

All calls to passthrough methods return a JSON string result.

### Passthrough Example

The following example shows how to POST data to https://api.smartsheet.com/2.0/sheets using the passthrough method and 
a JSON string payload:
```
String payload =
    "{\"name\": \"my new sheet\"," +
        "\"columns\": [" +
            "{\"title\": \"Favorite\", \"type\": \"CHECKBOX\", \"symbol\": \"STAR\"}," +
            "{\"title\": \"Primary Column\", \"primary\": true, \"type\": \"TEXT_NUMBER\"}" +
        "]" +
    "}";
String jsonResponse = smartsheet.passthroughResources().postRequest("sheets", payload, null);
```

## Testing

For comprehensive testing documentation including mock API test patterns, WireMock setup, and complete examples, see [TESTING.md](TESTING.md).

**Quick commands:**
- Unit tests: `./gradlew test`
- Mock API tests: `./gradlew sdkTest` (requires [smartsheet-sdk-tests](https://github.com/smartsheet/smartsheet-sdk-tests) WireMock server)

## Working with Report Filter Values

When creating or updating report definitions with filters, the SDK provides a `ReportFilterObjectValue` interface with static factory methods to create properly typed filter values. This interface ensures type safety by restricting filter values to only the types supported by report filters.

### Available Filter Value Types

The `ReportFilterObjectValue` class provides factory methods for creating different types of filter values:

#### String Values
Use `ReportFilterObjectValue.string()` for text-based filters:
```java
ReportFilterObjectValue.string("Active")
ReportFilterObjectValue.string("In Progress")
```

#### Numeric Values
Use `ReportFilterObjectValue.number()` for numeric filters:
```java
ReportFilterObjectValue.number(5)
ReportFilterObjectValue.number(100.5)
```

#### Date Values
Use `ReportFilterObjectValue.date()` for date-based filters. You can provide either an ISO 8601 date string or a Java `Date` object:
```java
// Using ISO 8601 date string (yyyy-MM-dd)
ReportFilterObjectValue.date("2024-01-01")

// Using Java Date object
Date dueDate = new Date();
ReportFilterObjectValue.date(dueDate)
```

#### Current User
Use `ReportFilterObjectValue.currentUser()` to filter by the authenticated user. This is particularly useful for contact list columns:
```java
ReportFilterObjectValue.currentUser()
```

### Complete Example

Here’s a complete example showing how to create a report definition with multiple filter types:

```java
ReportDefinition reportDefinition = new ReportDefinition();

// Create filters using ReportFilterObjectValue helpers
reportDefinition.setFilters(
    new ReportFilterExpression()
        .setOperator(ReportFilterExpressionOperator.AND)
        .setCriteria(Arrays.asList(
            // String filter: filter for "Active" or "In Progress" status
            new ReportFilterCriterion()
                .setOperator(ReportFilterOperator.EQUAL)
                .setColumn(
                    new ReportColumnIdentifier()
                        .setTitle("Status")
                        .setType(ColumnType.PICKLIST)
                )
                .setValues(Arrays.asList(
                    ReportFilterObjectValue.string("Active"),
                    ReportFilterObjectValue.string("In Progress")
                )),
            
            // Numeric filter: priority greater than 5
            new ReportFilterCriterion()
                .setOperator(ReportFilterOperator.GREATER_THAN)
                .setColumn(
                    new ReportColumnIdentifier()
                        .setTitle("Priority")
                        .setType(ColumnType.TEXT_NUMBER)
                )
                .setValues(Arrays.asList(
                    ReportFilterObjectValue.number(5)
                )),
            
            // Current user filter: items assigned to me
            new ReportFilterCriterion()
                .setOperator(ReportFilterOperator.EQUAL)
                .setColumn(
                    new ReportColumnIdentifier()
                        .setTitle("Assigned To")
                        .setType(ColumnType.CONTACT_LIST)
                )
                .setValues(Arrays.asList(
                    ReportFilterObjectValue.currentUser()
                )),
            
            // Date filter: due date on or after 2024-01-01
            new ReportFilterCriterion()
                .setOperator(ReportFilterOperator.GREATER_THAN_OR_EQUAL)
                .setColumn(
                    new ReportColumnIdentifier()
                        .setTitle("Due Date")
                        .setType(ColumnType.DATE)
                )
                .setValues(Arrays.asList(
                    ReportFilterObjectValue.date("2024-01-01")
                ))
        ))
);

// Update the report definition
smartsheet.reportResources().updateReportDefinition(reportId, reportDefinition);
```

## Android
Google doesn’t support the Apache HTTP Client on Android (used as the default HTTP client by the SDK). In order to make it easier to use the Smartsheet Java SDK, the SDK contains a 2nd HTTP client class, AndroidHttpClient. The AndroidHttpClient class is included with version 2.68.4+ of the SDK. To use the Smartsheet Java SDK on Android, follow these steps:

1. Add to the module-level build.gradle's dependencies section:
```gradle
implementation 'com.smartsheet:smartsheet-sdk-java:2.68.4'
```
2. Add to the module-level build.gradle's android section:
```gradle
packagingOptions {
    exclude 'META-INF/DEPENDENCIES'
}
```
3. When you invoke the Smartsheet client, instruct it to use the AndroidHttpClient to access the Smartsheet API:
```java
Smartsheet smartsheet = SmartsheetFactory.custom().setHttpClient(new AndroidHttpClient())
        .setAccessToken("[TOKEN]").build();
```

## Overriding HTTP Client Behavior
You can provide a number of customizations to the default HTTP behavior by extending the DefaultHttpClient class and 
overriding one or more methods (examples below). If required, you can remove use of the Apache HTTP Client 
by implementing the HttpClient interface in a custom client (see 
[Android QRScanner](https://github.com/smartsheet-samples/QRScanner)). 

Common customizations may include:
- implementing an HTTP proxy
- injecting additional HTTP headers
- overriding default timeout or retry behavior
 
### Sample ProxyHttpClient
The following example shows how to enable a proxy by providing the SmartsheetBuilder with an HttpClient which extends 
DefaultHttpClient.  

Invoke the SmartsheetBuilder with a custom HttpClient:

```java
ProxyHttpClient proxyHttpClient = new ProxyHttpClient("localhost", 8080);
Smartsheet smartsheet = SmartsheetFactory.custom().setHttpClient(proxyHttpClient).build();
``` 

```java
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;

public class ProxyHttpClient extends DefaultHttpClient {

    private String proxyHost;
    private Integer proxyPort;

    public ProxyHttpClient(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    /** Override this method to inject additional headers, or setup proxy information
     * on the request.
     */
    @Override
    public HttpRequestBase createApacheRequest(HttpRequest smartsheetRequest) {
        HttpRequestBase apacheHttpRequest = super.createApacheRequest(smartsheetRequest);

        RequestConfig.Builder builder = RequestConfig.custom();
        if (apacheHttpRequest.getConfig() != null) {
            builder = RequestConfig.copy(apacheHttpRequest.getConfig());
        }
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
        builder.setProxy(proxy);
        RequestConfig config = builder.build();
        apacheHttpRequest.setConfig(config);
        return apacheHttpRequest;
    }
}
```
### Sample RetryHttpClient
The following example shows how to override the default retry/timeout logic.  

Invoke the SmartsheetBuilder with a custom HttpClient:
```java
Smartsheet smartsheet = SmartsheetFactory.custom().setHttpClient(new RetryHttpClient()).build();
smartsheet.setMaxRetryTimeMillis(30000);
```

```java
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.http.HttpResponse;
import com.smartsheet.api.models.Error;

import java.io.IOException;

public class RetryHttpClient extends DefaultHttpClient {

    /**
     * Override this method to perform API requests for special cases
     */
    @Override
    public boolean shouldRetry(int previousAttempts, long totalElapsedTimeMillis, HttpResponse response) {

        // HTTP Status available as response.getStatusCode()
        int httpStatus = response.getStatusCode();

        String contentType = response.getEntity().getContentType();
        if (contentType != null && !contentType.startsWith(JSON_MIME_TYPE)) {
            // it's not JSON; don't even try to parse it
            return false;
        }
        Error error;
        try {
            // Details about the Smartsheet API error condition
            error = jsonSerializer.deserialize(Error.class, response.getEntity().getContent());
        }
        catch (IOException e) {
            return false;
        }
        switch(error.getErrorCode()) {
            // The default shouldRetry, retries 4001, 4002, 4003, 4004 codes
            case 4001:
            case 4002:
            case 4003:
            case 4004:
            case 9999: // adding my fictional error code
                break;
            default:
                return false;
        }

        // The default calcBackoff uses exponential backoff, add custom behavior by overriding calcBackoff
        long backoffMillis = calcBackoff(previousAttempts, totalElapsedTimeMillis, error);
        if (backoffMillis < 0)
            return false;

        logger.info("HttpError StatusCode=" + response.getStatusCode() + ": Retrying in " + backoffMillis + " milliseconds");
        try {
            Thread.sleep(backoffMillis);
        }
        catch (InterruptedException e) {
            logger.warn("sleep interrupted", e);
            return false;
        }
        return true;
    }
}
```
## Event Reporting
The following sample demonstrates best practices for consuming the event stream from the Smartsheet Event Reporting
feature.

The sample uses the `smartsheet.eventResources().listEvents` method to request a list of events from the stream. The
first request sets the `since` parameter with the point in time (i.e. event occurrence datetime) in the stream from 
which to start consuming events. The `since` parameter can be set with a datetime value that is either formatted as 
ISO 8601 (e.g. 2010-01-01T00:00:00Z) or as UNIX epoch (in which case the `numericDates` parameter must also be set to 
`true`. By default the `numericDates` parameter is set to `false`).

To consume the next list of events after the initial list of events is returned, set the `streamPosition` parameter 
with the `nextStreamPosition` property obtained from the previous request and don't set the `since` parameter with 
any values. This is because when using the `listEvents` method, either the `since` parameter or the `streamPosition`
parameter should be set, but never both.

Note that the `moreAvailable` property in a response indicates whether more events are immediately available for
consumption. If events are not immediately available, they may still be generating so subsequent requests should keep
using the same `streamPosition` value until the next list of events is retrieved.

Many events have additional information available as part of the event. That information can be accessed using the 
HashMap stored in the `additionalDetails` property. Information about the additional details provided can be found
[here.](https://smartsheet.redoc.ly/tag/eventsDescription)

Each event identifies the object it affected. Use the `getObjectIdStr()` accessor, which returns the
object identifier as a `String` and supports both numeric and non-numeric identifiers. The older
`getObjectId()` accessor is deprecated and kept only for backward compatibility: when the identifier
is numeric it returns the number, and when the identifier is non-numeric it returns `-1` while the
real value is available from `getObjectIdStr()`. New code should read `getObjectIdStr()`.

```java
for (Event event : events) {
    // Preferred: works for all identifier types
    System.out.println(event.getObjectIdStr());

    // Deprecated: numeric only; returns -1 for non-numeric identifiers
    // System.out.println(event.getObjectId());
}
```

```java
public class Sample {
    
    public static void main(String[] args) throws SmartsheetException {
        SampleProgram();
    }
    
    // this example is looking specifically for new sheet events
    private static void printNewSheetEventsInList(List<Event> events)
    {
        //  enumerate all events in the list of returned events
        for (Event event: events) {
            // find all created sheets
            if (event.getObjectType() == EventObjectType.SHEET && event.getAction() == EventAction.CREATE) {
                // additional details are available for some events, they can be accessed as a HashMap
                // in the additionalDetails property
                System.out.println(event.getAdditionalDetails().get("sheetName"));
            }
        }
    }

    public static void SampleProgram() throws SmartsheetException{

        Smartsheet smartsheet = SmartsheetFactory.createDefaultClient();

        // begin listing events in the stream starting with the `since` parameter
        Date lastWeek = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7));
        // this example looks at the previous 7 days of events by providing a since argument set to last week's date 
        EventResult eventResult = smartsheet.eventResources().listEvents(lastWeek, null, 1000, false);
        printNewSheetEventsInList(eventResult.getData());

        // continue listing events in the stream by using the `streamPosition`, if the previous response indicates 
        // that more data is available.
        while(eventResult.getMoreAvailable()) {
            eventResult = smartsheet.eventResources().listEvents(null, eventResult.getNextStreamPosition(), 10000, true);
            printNewSheetEventsInList(eventResult.getData());
        }
    }
}
``` 

## Working with Smartsheetgov.com Accounts

If you need to access Smartsheetgov you will need to specify the Smartsheetgov API URI as the base URI during creation 
of the Smartsheet client object. SmartsheetGov uses a base URI of https://api.smartsheetgov.com/2.0/. The base URI is 
defined as a constant in both the SmartsheetBuilder and SmartsheetFactory classes 
(i.e. SmartsheetFactory.GOV_BASE_URI). The SmartsheetFactory also contains API to create default Smartsheet clients 
which point to the Smartsheetgov URI: 

```java
package com.smartsheet.api.sample;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.SmartsheetFactory;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;

import java.util.List;

/**
 *
 */
public class Sample {
    static {
        // Uncomment these lines to enable logging to console
        // System.setProperty("Smartsheet.trace.parts", "RequestBody,ResponseBodySummary");
        // System.setProperty("Smartsheet.trace.pretty", "true");

    }
    public static void main(String[] args) {
        try {
            // Create Smartsheet client
            // Set your access token in environment variable "SMARTSHEET_ACCESS_TOKEN", else update and uncomment here
            Smartsheet smartsheet = SmartsheetFactory.createDefaultGovAccountClient( /* "ll352u9jujauoqz4gstvsae05" */);

            // List all sheets
            PagedResult<Sheet> sheets = smartsheet.sheetResources().listSheets();
            System.out.println("\nFound " + sheets.getTotalCount() + " sheets\n");

            Long sheetId = sheets.getData().get(0).getId();            // Default to first sheet

            // TODO: Uncomment if you wish to read a specific sheet
            // sheetId = 239236234L;

            // Load entire sheet
            Sheet sheet = smartsheet.sheetResources().getSheet(sheetId);
            List<Row> rows = sheet.getRows();
            System.out.println("\nLoaded sheet id " + sheetId + " with " + rows.size() + " rows, title: " + sheet.getName());

            // Display the first 5 rows & columns
            for (int rowNumber = 0; rowNumber < rows.size() && rowNumber < 5; rowNumber++)
                DumpRow(rows.get(rowNumber), sheet.getColumns());
        } catch (SmartsheetException sx) {
            sx.printStackTrace();
        }
        System.out.println("done.");
    }

    static void DumpRow(Row row, List<Column> columns)
    {
        System.out.println("Row # " + row.getRowNumber() + ":");
        for (int columnNumber = 0; columnNumber < columns.size() && columnNumber < 5; columnNumber++) {
            System.out.println("    " + columns.get(columnNumber).getTitle() + ": " + row.getCells().get(columnNumber).getValue());
        }

    }
}

```

## Working with Smartsheet Regions Europe Accounts

If you need to access Smartsheet Regions Europe you will need to specify the Smartsheet.eu API URI as the base URI during creation of the Smartsheet client object. Smartsheet.eu uses a base URI of https://api.smartsheet.eu/2.0/. The base URI is defined as a constant in both the SmartsheetBuilder and SmartsheetFactory classes (i.e. SmartsheetFactory.EU_BASE_URI). The SmartsheetFactory also contains API to create default Smartsheet clients which point to the Smartsheet.eu URI: 

```java
package com.smartsheet.api.sample;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.SmartsheetFactory;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;

import java.util.List;

/**
 *
 */
public class Sample {
    static {
        // Uncomment these lines to enable logging to console
        // System.setProperty("Smartsheet.trace.parts", "RequestBody,ResponseBodySummary");
        // System.setProperty("Smartsheet.trace.pretty", "true");

    }
    public static void main(String[] args) {
        try {
            // Create Smartsheet client
            // Set your access token in environment variable "SMARTSHEET_ACCESS_TOKEN", else update and uncomment here
            Smartsheet smartsheet = SmartsheetFactory.createDefaultEUAccountClient( /* "ll352u9jujauoqz4gstvsae05" */);


```
