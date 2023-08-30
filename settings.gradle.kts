rootProject.name = "apis"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("minestom", "net.onelitefeather.microtus", "Minestom").version("1.1.0")
            library("minestom-test", "net.onelitefeather.microtus.testing", "testing").version("1.1.0")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").version("5.10.0")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").version("5.10.0")
            library("mockito-core", "org.mockito", "mockito-core").version("5.4.0")
            library("mockito-junit", "org.mockito", "mockito-junit-jupiter").version("5.4.0")
            library("schem", "com.github.hollow-cube.common", "schem").version("db5eab9512")

            plugin("sonarqube", "org.sonarqube").version("4.3.0.3225")
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")
        }
    }
}

