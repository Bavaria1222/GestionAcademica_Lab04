plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Agregar la dependencia para el driver de Oracle
    implementation("com.oracle.database.jdbc:ojdbc8:21.5.0.0")
    //Agregar dependencia de GSON
    implementation("com.google.code.gson:gson:2.10.1")



}

tasks.test {
    useJUnitPlatform()
}