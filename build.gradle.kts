import org.sonarqube.gradle.SonarQubeTask

plugins {
    java
    alias(libs.plugins.sonarqube)
    jacoco
}

group = "net.theevilreaper.apis"
val baseVersion = "1.0.-SNAPSHOT"
val sonarKey = "dungeon_projects_apis_AYKTgGApdAa6ziWsmL8y"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.minestom)

    testImplementation(libs.minestom)
    testImplementation(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    jacocoTestReport {
        dependsOn(rootProject.tasks.test)
        reports {
            xml.required.set(true)
        }
    }

    test {
        finalizedBy(rootProject.tasks.jacocoTestReport)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    getByName<SonarQubeTask>("sonarqube") {
        dependsOn(rootProject.tasks.test)
    }

}

sonarqube {
    properties {
        property("sonar.projectKey", sonarKey)
    }
}

if (System.getenv().containsKey("CI")) {
    version = "${baseVersion}+${System.getenv("CI_COMMIT_SHORT_SHA")}"
} else {
    version = baseVersion
}
