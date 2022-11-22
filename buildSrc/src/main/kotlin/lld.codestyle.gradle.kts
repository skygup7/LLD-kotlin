/**
 * Gradle build plugin for defining code style checks across languages.
 */

plugins {
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint("0.43.2")
        endWithNewline()
    }
}