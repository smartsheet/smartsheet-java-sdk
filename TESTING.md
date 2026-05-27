# Testing Guide

This guide establishes the mandatory patterns for mock API testing in the Smartsheet Java SDK.

## Table of Contents

- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running Mock API Tests](#running-mock-api-tests)
- [Mock API Test Standards](#mock-api-test-standards)
  - [Standardized Test Cases](#standardized-test-cases)
  - [Key Principles](#key-principles)
  - [Test Suite Structure](#test-suite-structure)
- [Additional Rules](#additional-rules)
- [Helper Functions](#helper-functions)

---

## Getting Started

### Prerequisites

Mock API tests require WireMock running locally on port 8080. The WireMock server provides simulated API responses for contract testing without hitting the live Smartsheet API.

For WireMock setup instructions, mapping documentation, and details on `x-request-id` and `x-test-name` header usage, see the [smartsheet-sdk-tests](https://github.com/smartsheet/smartsheet-sdk-tests) repository.

For complete test examples, see [src/test/java/com/smartsheet/api/sdktest/reports/](./src/test/java/com/smartsheet/api/sdktest/reports/).

### Running Mock API Tests

| Command | Purpose |
|---------|---------|
| `./gradlew test` | Run all tests |
| `./gradlew sdkTest` | Run mock API tests only |
| `./gradlew sdkTest --tests TestCreateReport` | Run specific test suite |
| `./gradlew sdkTest --tests TestCreateReport.testCreateReportGeneratedUrlIsCorrect` | Run specific test |

---

## Mock API Test Standards

### Standardized Test Cases

Every endpoint must implement these test cases (using Java naming conventions):

**Required Tests:**

1. **`testXxxGeneratedUrlIsCorrect`**
   - Asserts request method
   - Asserts URL path
   - Asserts query parameters (even if empty - use `assertThat(actualQueryParams).isEqualTo(expectedQueryParams)`)
   - Does NOT assert request/response body

2. **`testXxxAllResponseBodyProperties`**
   - Asserts request body (POST/PUT/PATCH/DELETE-with-body: the request object; GET/bodyless DELETE: empty string)
   - Asserts response body with all properties using `.usingRecursiveComparison().isEqualTo()`
   - Does NOT assert method, URL, or query parameters

3. **`testXxxError400Response`**
   - Asserts ONLY that SDK returns expected client error (exception type and message)

4. **`testXxxError500Response`**
   - Asserts ONLY that SDK returns expected server error (exception type and message)

**Optional Tests:**

- **`testXxxRequiredResponseBodyProperties`** - Include only if a corresponding WireMock mapping exists for the required-properties variant. Asserts request body and minimal response body.
- **Endpoint-specific tests** - Additional tests for unique endpoint behaviors (e.g., invalid argument handling, pagination logic, special characters)

### Key Principles

#### Full-Object Assertions

Assert objects as a whole using `.isEqualTo()` or `.usingRecursiveComparison().isEqualTo()`, not property-by-property. This ensures extra or missing properties cause test failures.

- **Query parameters:** Assert the entire query parameter map (convert QueryParameter objects to Map<String, List<String>>)
- **Request body:** Assert the entire request body object using ObjectMapper JSON comparison
- **Response body:** Assert the entire deserialized response object

#### Test Constants

- **Cross-file constants:** Use reusable constants from `CommonTestConstants.java` per subdirectory (e.g., `TEST_USER_ID`, `TEST_REPORT_ID`)
- **File-scoped constants:** Define expected responses, request bodies, and query params at the top of each test file (e.g., `EXPECTED_REQUEST_BODY`, `EXPECTED_QUERY_PARAMS`)

#### WireMock Integration

Each test uses custom headers for WireMock integration:

- **`x-request-id`:** UUID for request tracking (retrieve via `findWiremockRequest`)
- **`x-test-name`:** Targets specific WireMock mapping (e.g., `/reports/create-report/all-response-body-properties`)

See [smartsheet-sdk-tests](https://github.com/smartsheet/smartsheet-sdk-tests) for WireMock mapping conventions and header usage details.

### Test Suite Structure

- **One test file per endpoint:** `src/test/java/com/smartsheet/api/sdktest/<resource>/Test<Operation>.java`
- **One constants file per resource:** `src/test/java/com/smartsheet/api/sdktest/<resource>/CommonTestConstants.java`
- **Gold standard examples:** See Reports tests in `src/test/java/com/smartsheet/api/sdktest/reports/`

---

## Additional Rules

- **Enums:** Use enums in tests instead of raw values (e.g., `AccessLevel.OWNER` not `"OWNER"`)

---

## Helper Functions

**Available in `com.smartsheet.api.sdktest.Utils`:**

- **`createWiremockSmartsheetClient(String testName, String requestId)`** - Creates a Smartsheet client configured to point at WireMock server (http://localhost:8080). Returns `WiremockClientWrapper` containing both the Smartsheet client and WiremockClient for request verification.
- **`WiremockClient.findWiremockRequest(String requestId)`** - Retrieves a request from WireMock's admin API using the `x-request-id` header. Returns `LoggedRequest` object with `getUrl()`, `getBodyAsString()`, `getQueryParams()`, `getMethod()`, etc.
