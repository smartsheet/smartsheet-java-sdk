# Reports Tests: Whole-Object Comparison Conformance

**Date:** 2026-06-12
**Status:** Approved (design)

## Problem

Six report endpoint test files assert response bodies property-by-property instead of
building an expected object and comparing the whole object, violating TESTING.md
§"Full-Object Assertions". Property-by-property assertions silently pass when the SDK
returns extra or missing fields.

Affected files:

- `TestListReportScope` — GET /2.0/reports/{reportId}/scope
- `TestListReportColumns` — GET /2.0/reports/{reportId}/columns
- `TestUpdateReportColumn` — PUT /2.0/reports/{reportId}/columns/{columnVirtualId}
- `TestGetReportColumn` — GET /2.0/reports/{reportId}/columns/{columnVirtualId}
- `TestDeleteReportColumn` — DELETE /2.0/reports/{reportId}/columns/{columnVirtualId}
- `TestGetReportDefinition` — GET /2.0/reports/{reportId}/definition

## Goal

Bring all six files into conformance with TESTING.md, using the gold-standard pattern
already present in `TestCreateReport` and `TestUpdateReportDefinition`:

- Build `static final` expected objects (static initializer for complex graphs).
- Assert the whole response with `assertThat(result).usingRecursiveComparison().isEqualTo(EXPECTED)`.
- For paginated results, build the full `TokenPaginatedResult<T>` expected object.
- Normalize test method names and structure to TESTING.md's standardized set.

## Standardized test set (per TESTING.md)

Required per endpoint:

1. `testXxxGeneratedUrlIsCorrect` — method, path, full query-param map. No body asserts.
2. `testXxxAllResponseBodyProperties` — request body (where applicable) + whole response
   via `usingRecursiveComparison().isEqualTo()`. No method/URL/query asserts.
3. `testXxxError400Response` — exception type + message only.
4. `testXxxError500Response` — exception type + message only.

Optional:

- `testXxxRequiredResponseBodyProperties` — only where a required-properties mapping exists.
- Endpoint-specific tests (e.g. invalid-argument) retained where they add value.

Normalization actions:

- Remove redundant `testXxxOmitsQueryParamsWhenNotProvided` / `testXxxNoQueryParams`
  tests — query-param assertion belongs in `GeneratedUrlIsCorrect` (already covered there).
  Keep only genuine endpoint-specific behavior tests (invalid argument).
- Remove stray `assertThat(x).isInstanceOf(...)` lines that recursive comparison subsumes.

## Source of truth for expected values

WireMock mapping JSON in the sibling repo `../smartsheet-sdk-tests/mappings/reports/<endpoint>/`.
Expected objects must be built to exactly match the deserialized mapping response,
including nested objects and `objectType` discriminators.

## Per-file plan

### TestDeleteReportColumn
No response body. `AllResponseBodyProperties` keeps `assertDoesNotThrow`. Remove the
redundant `NoQueryParams` test (covered by `GeneratedUrlIsCorrect`). Otherwise conformant.

### TestGetReportColumn
Build expected `ReportColumn` (all-properties) incl. `AutoNumberFormat`; recursive-compare.
Build expected `ReportColumn` (required-properties); recursive-compare. Remove `NoQueryParams`.

### TestUpdateReportColumn
Keep request-body JSON assertion. Replace field asserts with expected `ReportColumn`
recursive-compare for all + required variants. Keep `InvalidArgument`.

### TestListReportScope
Build expected `TokenPaginatedResult<ReportScopeInclusion>` (3 items, null lastKey) and
required variant (1 item); recursive-compare each. Remove `OmitsQueryParams`.

### TestListReportColumns
Build expected `TokenPaginatedResult<ReportColumn>` (4 cols incl. AutoNumberFormat) and
required variant (2 cols); recursive-compare each. Remove `OmitsQueryParams`.

### TestGetReportDefinition
Build full expected `ReportDefinition` via static initializer: `ReportFilterExpression`
with 6 criteria (string, string, number, date, current-user, null value),
`groupingCriteria` (2), `summarizingCriteria` (2), `sortingCriteria` (2). Use
`ReportColumnIdentifier` and `ReportFilterObjectValue` factory helpers as in
`TestUpdateReportDefinition`. Required variant = `ReportDefinition` with all four
sections null. Recursive-compare each.

## Risks / implementation notes

- **Numeric type strictness:** `usingRecursiveComparison` is type-strict. `NumberObjectValue`
  deserializes JSON numbers to a concrete type (likely `Integer`/`Double`); the expected
  object must use the same runtime type. Verify by running the test, not by assuming.
- **`objectType` discriminators:** ObjectValue subclasses carry `objectType`. Recursive
  comparison compares them even though the old field-asserts ignored most. Expected objects
  must set them to match the deserialized values.
- **Null list element:** `filters.criteria[5]` has a null value entry; the expected `values`
  list must contain a single null.
- **Factory helpers vs. setters:** Prefer `ReportFilterObjectValue.string/number/date/currentUser`
  factories (as in TestUpdateReportDefinition) so discriminators are set correctly.

## Verification

`./gradlew sdkTest --tests "TestListReportScope" --tests "TestListReportColumns"
--tests "TestGetReportColumn" --tests "TestUpdateReportColumn" --tests "TestDeleteReportColumn"
--tests "TestGetReportDefinition"` must pass. Then full `./gradlew sdkTest` for regressions.

## Out of scope

- Changes to SDK production code or model classes.
- Changes to WireMock mappings.
- Other report test files already conformant (`TestCreateReport`, `TestAddReportColumns`,
  `TestAddReportScope`, `TestRemoveReportScope`, `TestUpdateReportDefinition`).
