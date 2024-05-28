import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep

plugins {
    id("com.android.application") version "8.4.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("app.cash.licensee") version "1.11.0" apply false
    id("com.diffplug.spotless") version "6.25.0" apply false
    id("org.gradle.android.cache-fix") version "3.0.1" apply false
}

allprojects {
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion = JavaLanguageVersion.of(21)
        }
    }

    apply(plugin = "com.diffplug.spotless")
    extensions.configure<SpotlessExtension> {
        format("xml") {
            target("app/assets/appfilter.xml")
            eclipseWtp(EclipseWtpFormatterStep.XML).configFile("$rootDir/spotless.xml.prefs")
        }
        kotlin {
            target("src/**/*.kt")
            ktlint().customRuleSets(
                listOf(
                    "io.nlopez.compose.rules:ktlint:0.4.2",
                ),
            ).editorConfigOverride(
                mapOf(
                    "ktlint_compose_compositionlocal-allowlist" to "disabled",
                ),
            )
        }
        kotlinGradle {
            ktlint()
        }
    }
}
