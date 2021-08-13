import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("com.google.protobuf") version "0.8.17"
    id("org.sonarqube") version "3.3"
    id("jacoco")
    id("io.gitlab.arturbosch.detekt").version("1.18.0-RC3")
    application
}

version = "0.1"
group = "org.seariver"

repositories {
    mavenCentral()
}

val kotlinVersion = project.properties.get("kotlin_version")
val coroutinesVersion = project.properties.get("coroutines_version")
val protobufVersion = project.properties.get("protobuf_version")
val grpcVersion = project.properties.get("grpc_version")
val grpcKotlinVersion = project.properties.get("grpc_kotlin_version")

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

    // gRPC and Protobuf
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.8.17")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")

    // Database
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.postgresql:postgresql:42.2.23")
    implementation("org.flywaydb:flyway-core:7.12.0")

    // Log
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.5")

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.24")
    testImplementation("org.testcontainers:postgresql:1.16.0")

    // Lint
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.18.0-RC3")
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

application {
    mainClass.set("org.seariver.GrpcServerKt")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    test {
        useJUnitPlatform()
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
    }
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/grpckt")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
                id("grpckt")
            }
        }
    }
}

detekt {
    autoCorrect = true
    reports {
        xml {
            enabled = true
        }
    }
}

sonarqube {
    properties {
        property("sonar.language", "kotlin")
        property("sonar.core.codeCoveragePlugin", "jacoco")
        property("sonar.exclusions", "**/ShortCode.*,**/SourceUrl.*")
    }
}
