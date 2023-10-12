plugins {
	java
	id("maven-publish")
}

// Project Configs
allprojects {
	repositories {
		mavenLocal()
		maven {
			name = "bytesafe"
			url = uri("https://herron.bytesafe.dev/maven/herron/")
			credentials {
				username = extra["username"] as String?
				password = extra["password"] as String?
			}
		}
	}

	apply(plugin = "maven-publish")
	apply(plugin = "java-library")

	group = "com.herron.exchange"
	version = "1.0.0-SNAPSHOT"
	if (project.hasProperty("releaseVersion")) {
		val releaseVersion: String by project
		version = releaseVersion
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	publishing {
		publications {
			create<MavenPublication>("herron.exchange") {
				artifactId = project.name
				// artifact("build/libs/${artifactId}-${version}.jar")
				from(components["java"])
			}
			repositories {
				maven {
					name = "bytesafe"
					url = uri("https://herron.bytesafe.dev/maven/herron/")
					credentials {
						username = extra["username"] as String?
						password = extra["password"] as String?
					}
				}
			}
		}
	}
}


dependencies {
	//Project Modules
	implementation(project(":integrations"))

	implementation(libs.common.api)
	implementation(libs.common)

	// External Libs
	implementation(libs.tyrus.standalone.client)
	implementation(libs.javax.json.api)
	implementation(libs.javax.json)
	implementation(libs.javafaker)

	// External Test Libs
	testImplementation(testlibs.junit.jupiter.api)
	testImplementation(testlibs.junit.jupiter.engine)
}