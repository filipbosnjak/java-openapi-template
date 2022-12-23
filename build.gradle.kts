plugins {
    java
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"

    id("org.openapi.generator") version "5.4.0"
}

group = "com.nextodev"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

//Open api config
val springdocOpenapiVersion = "1.6.14"

val oasBasePackage = "com.nextodev.javaopenapirest"
val oasSpecBaseLocation = "src/main/resources/api/"
val oasGenOutputDir = project.layout.buildDirectory.dir("generated-oas")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocOpenapiVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiVersion")

    implementation("org.openapitools:jackson-databind-nullable:0.2.4")

    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.hibernate:hibernate-validator:8.0.0.Final")
    implementation("javax.el:el-api:2.2.1-b04")


    runtimeOnly("org.postgresql:postgresql")
    compileOnly("javax.servlet:servlet-api:2.5")

}

//Generate api
tasks.register("generateAPI", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
    println("Generating...")
    input = project.file("$oasSpecBaseLocation/api.specification.yaml").path
    inputSpec.set("$rootDir/$oasSpecBaseLocation/api.specification.yaml")
    outputDir.set(oasGenOutputDir.get().toString())
    modelPackage.set("$oasBasePackage.model")
    apiPackage.set("$oasBasePackage.api")
    packageName.set(oasBasePackage)
    generatorName.set("spring")
    configOptions.set(
        mapOf(
            "dateLibrary" to "java17",
            "interfaceOnly" to "true",
            "useTags" to "true"
        )
    )

    doLast {
        delete("$buildDir/generated-oas/src/main/kotlin/com/hilti/wfm/api/Exceptions.kt")
    }
}

tasks.withType<JavaCompile> {
    dependsOn("generateAPI")
}

//import api class from a generated openapi dependency
sourceSets {
    val main by getting
    main.java.srcDir("${oasGenOutputDir.get()}/src/main/java")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
