# Release Procedure

This document is the single source of truth for releasing the Smartsheet Java SDK. Publishing to Maven Central is automated via GitHub Actions, but the version bump and changelog update are done by hand.

## Overview

Releases follow [Semantic Versioning](https://semver.org/) and [Keep a Changelog](https://keepachangelog.com/) conventions. Every release consists of:

1. A "Prepare for release" PR that bumps the version and closes out the changelog.
2. A merged commit on `mainline` published as a GitHub Release (which also creates the tag).
3. Automated signing and publishing to Maven Central triggered by the GitHub Release event.

## Prerequisites

- Write access to the `smartsheet/smartsheet-java-sdk` repository.
- Maven Central credentials are not needed locally — publishing is done via GitHub Actions secrets (GPG signing + Sonatype credentials).

## Step-by-Step Process

### 1. Decide the version bump

Review the `## [x.x.x] - Unreleased` section in `CHANGELOG.md` and apply semver rules:

| Change type | Bump |
| --- | --- |
| New endpoints, non-breaking additions | `minor` |
| Bug fixes, dependency updates | `patch` |
| Breaking API changes, removed types/methods | `major` |

### 2. Create a "Prepare for release" pull request

Open a branch from `mainline` (e.g., `release/v4.2.0`) and make the following changes:

#### a. Update `CHANGELOG.md`

Feature PRs accumulate entries under `## [x.x.x] - Unreleased`. For the release, insert the new versioned header between that placeholder and its content:

```diff
 ## [x.x.x] - Unreleased

+## [4.2.0] - 2026-07-15
+
 ### Added
```

The `## [x.x.x] - Unreleased` placeholder header is never removed — it stays at the top of the file permanently so future PRs have somewhere to add entries.

#### b. Update `build.gradle`

```diff
-version = '4.1.0'
+version = '4.2.0'
```

#### c. PR title convention

```
Prepare for release vX.X.X
```

Example: `Prepare for release v4.2.0`

### 3. Merge the PR

CI must pass before merging. The `test-pr.yaml` workflow runs:

- Unit tests and Jacoco coverage
- Mock API SDK tests

### 4. Create and publish the GitHub Release

1. Go to **Releases → Draft a new release** in the GitHub UI.
2. In the **Choose a tag** field, type the new version (e.g. `v4.2.0`) and select **Create new tag on publish**.
3. Set the title to `v4.2.0`.
4. Set the description to the changelog entries for this version (copy from `CHANGELOG.md`).
5. Click **Publish release**.

The tag is created automatically when the release is published — no separate `git tag` step needed.

Publishing the release (not just saving as a draft) triggers `release.yml`, which runs two jobs in parallel:

- **publish-docs**: Builds Javadoc and deploys it to the `gh-pages` branch.
- **publish**: Signs artifacts with GPG and deploys to Maven Central via JReleaser + Sonatype.

### 5. Verify the publish workflow

Go to [Workflow runs](https://github.com/smartsheet/smartsheet-java-sdk/actions) and confirm both the `publish-docs` and `publish` jobs succeeded.

### 6. Verify on Maven Central

Maven Central syncs from Sonatype approximately once per day. After ~24 hours, verify:

```
https://central.sonatype.com/artifact/com.smartsheet/smartsheet-sdk-java
```

or check the standard Maven Central search at `https://search.maven.org`.

## Files Changed in Every Release

| File | What changes |
| --- | --- |
| `CHANGELOG.md` | New versioned header inserted below the permanent `Unreleased` placeholder |
| `build.gradle` | `version` field bumped |

## Automation

Publishing is fully automated once the GitHub Release is published:

```
GitHub Release (published) → release.yml → GPG sign → JReleaser → Sonatype → Maven Central (~1 day)
```

The workflow uses repository secrets for GPG signing and Sonatype credentials — no local setup needed.

## Troubleshooting

**CI fails on the PR**

Check the `test-pr.yaml` run for failing unit tests or mock API tests.

**Publish workflow fails after the release is published**

The tag and GitHub Release already exist — do not delete them. Instead:

1. Investigate the failure in the Actions log (common causes: GPG key expiry, Sonatype credential rotation).
2. Re-trigger via **Actions → Release Package → Re-run jobs** after fixing the root cause.
3. If the root cause requires a code fix, cut a patch release instead.

## Rollback

Maven Central does not support un-publishing releases. If a bad release ships:

1. Publish a patch release immediately with the fix.
2. If the artifact hasn't synced to Maven Central yet, contact Sonatype support to block promotion.

## Checklist

- [ ] Determined correct semver bump
- [ ] `CHANGELOG.md` versioned header inserted below the permanent `Unreleased` placeholder
- [ ] `build.gradle` version bumped
- [ ] PR title: `Prepare for release vX.X.X`
- [ ] CI passes on the PR
- [ ] PR merged to `mainline`
- [ ] GitHub Release created: tag `vX.X.X` set to **Create new tag on publish**, description set to changelog entries, **published** (not draft)
- [ ] `publish` and `publish-docs` workflow jobs verified as succeeded
- [ ] Version confirmed in Sonatype Nexus
- [ ] Version confirmed in Maven Central (~1 day later)
