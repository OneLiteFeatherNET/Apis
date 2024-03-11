rootProject.name = "apis"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("mockito", "5.11.0")
            version("junit", "5.10.2")
            version("minestom", "1.3.1")
            library("minestom", "net.onelitefeather.microtus", "Minestom").versionRef("minestom")
            library("minestom-test", "net.onelitefeather.microtus.testing", "testing").versionRef("minestom")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("mockito-core", "org.mockito", "mockito-core").versionRef("mockito")
            library("mockito-junit", "org.mockito", "mockito-junit-jupiter").versionRef("mockito")
            library("schem", "com.github.hollow-cube.common", "schem").version("db5eab9512")

            plugin("sonarqube", "org.sonarqube").version("4.3.0.3225")
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")
        }
    }
}

