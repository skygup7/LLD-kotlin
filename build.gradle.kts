/**
 * Root Gradle build file, intended to aggregate dependencies of other modules into a single distribution,
 * and set any top-level or repo-wide configurations.
 * NOTE: most configurations should go in `buildSrc`.
 */

plugins {
    id("lld.kotlin-application-conventions")
}