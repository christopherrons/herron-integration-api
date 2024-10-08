dependencies {
    // Internal Libs
    implementation(libs.common.api)
    implementation(libs.common)

    // External Libs
    implementation(libs.tyrus.standalone.client)
    implementation(libs.javax.json.api)
    implementation(libs.javax.json)
    implementation(libs.datafaker)
    implementation(libs.commons.math)
    implementation(libs.spring.boot.starter.web)

    // External Test Libs
    testImplementation(testlibs.junit.jupiter.api)
    testImplementation(testlibs.junit.jupiter.engine)
}

tasks.test {
    useJUnitPlatform()
}