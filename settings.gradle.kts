rootProject.name = "apis"

dependencyResolutionManagement {
    pluginManagement {
        repositories {
            gradlePluginPortal()
            maven("https://eldonexus.de/repository/maven-public/")
        }
    }
    repositories {
        if (System.getenv("CI") != null) {
            repositoriesMode = RepositoriesMode.PREFER_SETTINGS
            repositories {
                maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                maven("https://repo.htl-md.schule/repository/Gitlab-Runner/")
                maven {
                    val groupdId = 28 // Gitlab Group
                    val ciApiv4Url = System.getenv("CI_API_V4_URL")
                    url = uri("$ciApiv4Url/groups/$groupdId/-/packages/maven")
                    name = "GitLab"
                    credentials(HttpHeaderCredentials::class.java) {
                        name = "Job-Token"
                        value = System.getenv("CI_JOB_TOKEN")
                    }
                    authentication {
                        create<HttpHeaderAuthentication>("header")
                    }
                }
            }
        } else {
            repositories {
                mavenCentral()
                maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                maven {
                    val groupdId = 28 // Gitlab Group
                    url = uri("https://gitlab.onelitefeather.dev/api/v4/groups/$groupdId/-/packages/maven")
                    name = "GitLab"
                    credentials(HttpHeaderCredentials::class.java) {
                        name = "Private-Token"
                        value = providers.gradleProperty("gitLabPrivateToken").get()
                    }
                    authentication {
                        create<HttpHeaderAuthentication>("header")
                    }
                }

            }
        }
    }
    versionCatalogs {
        create("libs") {
            version("publishdata", "1.4.0")
            version("schem", "1.3.1")

            library("dungeon.base.bom", "net.theevilreaper.dungeon.bom", "base").version("1.1.1")
            library("minestom", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("minestom-test", "net.onelitefeather.microtus.testing", "testing").withoutVersion()
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").withoutVersion()
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").withoutVersion()
            library("mockito-core", "org.mockito", "mockito-core").withoutVersion()
            library("mockito-junit", "org.mockito", "mockito-junit-jupiter").withoutVersion()
            library("schem", "dev.hollowcube", "schem").versionRef("schem")

            plugin("publishdata", "de.chojo.publishdata").versionRef("publishdata")
        }
    }
}

