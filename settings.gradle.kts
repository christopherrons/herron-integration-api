rootProject.name = "integration-api"
include("integrations")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("spring.boot.starter.web", "org.springframework.boot:spring-boot-starter-web:3.0.1")
            library("spring.boot.starter.parent", "org.springframework.boot:spring-boot-starter-parent:3.0.1")
            library("tyrus.standalone.client", "org.glassfish.tyrus.bundles:tyrus-standalone-client:1.18")
            library("javax.json.api", "javax.json:javax.json-api:1.1.4")
            library("javax.json", "org.glassfish:javax.json:1.1.4")
            library("javafaker", "com.github.javafaker:javafaker:1.0.2")
            library("commons.math", "org.apache.commons:commons-math3:3.2")
            library("common.api", "com.herron.exchange:common-api:1.0.0-SNAPSHOT")
            library("common", "com.herron.exchange:common:1.0.0-SNAPSHOT")
        }

        create("testlibs") {
            library("spring.boot.starter.test", "org.springframework.boot:spring-boot-starter-test:3.0.1")
            library("junit.jupiter.api", "org.junit.jupiter:junit-jupiter-api:5.8.1")
            library("junit.jupiter.engine", "org.junit.jupiter:junit-jupiter-engine:5.8.1")
        }
    }
}

