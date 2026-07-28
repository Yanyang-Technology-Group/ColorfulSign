import org.gradle.api.file.DuplicatesStrategy
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    kotlin("jvm") version "2.1.0"
}

group = "com.xiaobai"
version = "26.7.2-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "papermc-repo"
                url = uri("https://repo.papermc.io/repository/maven-public/")
            }
        }
        filter {
            includeGroup("io.papermc.paper")
            includeGroup("net.md-5")
        }
    }
    maven {
        name = "tencent"
        url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    testImplementation(kotlin("test"))
    testImplementation("net.kyori:adventure-text-serializer-plain:4.14.0")
    testImplementation("net.kyori:adventure-text-serializer-legacy:4.14.0")
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })
}
