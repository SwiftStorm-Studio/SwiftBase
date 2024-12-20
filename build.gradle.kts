import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id ("org.jetbrains.kotlin.jvm")
    id("cl.franciscosolis.sonatype-central-upload")
    `maven-publish`
}

group = "net.rk4z.s1"
version = "1.1.0"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "cl.franciscosolis.sonatype-central-upload")
    apply(plugin = "maven-publish")

    group = rootProject.group

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.20")
        implementation("org.json:json:20240303")
        implementation("org.reflections:reflections:0.10.2")
        implementation("org.yaml:snakeyaml:2.3")
    }

    java {
        withSourcesJar()
        withJavadocJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType<JavaCompile> {
        options.release.set(21)
    }

    tasks.named<Javadoc>("javadoc") {
        isFailOnError = false
    }
}

tasks.create<Jar>("buildAll") {
    dependsOn(":integrations:core:jout", ":integrations:fabric:jout", ":integrations:paper:jout")
}