plugins {
    java
    `java-library`
    jacoco
    `maven-publish`
    alias(libs.plugins.publishdata)
}

group = "net.theevilreaper"
version = "0.2.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(1, "minutes")
}

dependencies {
    implementation(platform(libs.bom.base))
    implementation(libs.schem)
    compileOnly(libs.minestom)

    testImplementation(libs.minestom)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.engine)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release = 21
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
}

publishData {
    addBuildData()
    useGitlabReposForProject("86", "https://gitlab.onelitefeather.dev/")
    publishTask("jar")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // configure the publication as defined previously.
            publishData.configurePublication(this)
            version = publishData.getVersion(false)
        }
    }
    repositories {
        maven {
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }

            name = "Gitlab"
            // Get the detected repository from the publish data
            url = uri(publishData.getRepository())
        }
    }
}
