import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "info.mythicmc"
version = "1.0.0"

description = "A plugin which allows controlling phantoms, global and per-player."

repositories {
    jcenter()
    maven (url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven (url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven (url = "https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
