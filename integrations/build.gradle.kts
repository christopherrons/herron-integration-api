dependencies {
    // Internal Libs
    implementation(libs.common.api)
    implementation(libs.common)

    // External Libs
    implementation(libs.javax.json.api)
    implementation(libs.javax.json)
    implementation(libs.commons.math)
    // External Test Libs
    testImplementation(testlibs.junit.jupiter.api)
    testImplementation(testlibs.junit.jupiter.engine)
}

tasks.test {
    useJUnitPlatform()
}