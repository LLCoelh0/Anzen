plugins {
    id("java")
}

group = "org.Lazy Rabbit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.formdev:flatlaf:3.5.4")
}

tasks.test {
    useJUnitPlatform()
}