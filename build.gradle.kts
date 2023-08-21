plugins {
    java
    jacoco
    `maven-publish`
    id("org.sonarqube") version "4.3.0.3225"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.theevilreaper.apis"
val baseVersion = "0.1.0-SNAPSHOT"
val sonarKey = "dungeon_projects_apis_AYKTgGApdAa6ziWsmL8y"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven {
        val groupdId = 28 // Gitlab Group
        url = if (System.getenv().containsKey("CI")) {
            val ciApiv4Url = System.getenv("CI_API_V4_URL")
            uri("$ciApiv4Url/groups/$groupdId/-/packages/maven")
        } else {
            uri("https://gitlab.themeinerlp.dev/api/v4/groups/$groupdId/-/packages/maven")
        }
        name = "GitLab"
        credentials(HttpHeaderCredentials::class.java) {
            name = if (System.getenv().containsKey("CI")) {
                "Job-Token"
            } else {
                "Private-Token"
            }
            value = if (System.getenv().containsKey("CI")) {
                System.getenv("CI_JOB_TOKEN")
            } else {
                val gitLabPrivateToken: String? by project
                println(gitLabPrivateToken)
                gitLabPrivateToken
            }
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
}

dependencies {
    implementation(libs.schem)
    compileOnly(libs.minestom)

    testImplementation(libs.minestom)
    testImplementation(libs.minestom.test)
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks {
    processResources {
        filesMatching("extension.json") {
            expand(project.properties)
        }
    }

    jar {
        dependsOn("shadowJar")
    }

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

    getByName("sonar") {
        dependsOn(rootProject.tasks.test)
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", sonarKey)
    }
}

publishing {
    publications {

        create<MavenPublication>("maven") {
            from(components["java"])
        }

    }
    if (System.getenv().containsKey("CI")) {
        repositories {
            maven {
                name = "GitLab"
                val ciApiv4Url = System.getenv("CI_API_V4_URL")
                val projectId = System.getenv("CI_PROJECT_ID")
                url = uri("$ciApiv4Url/projects/$projectId/packages/maven")
                credentials(HttpHeaderCredentials::class.java) {
                    name = "Job-Token"
                    value = System.getenv("CI_JOB_TOKEN")
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }

}


version = if (System.getenv().containsKey("CI")) {
    "${baseVersion}+${System.getenv("CI_COMMIT_SHORT_SHA")}"
} else {
    baseVersion
}
