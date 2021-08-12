plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("net.kyori.indra") version "2.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
    java
}

group = "fi.fabianadrian"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.10.10")
    implementation("co.aikar:taskchain-bukkit:3.7.2")
    implementation("co.aikar:idb-bukkit:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.0")
}

indra {
    javaVersions().target(16)
}

tasks {
    shadowJar {
        minimize()
        sequenceOf(
            "co.aikar.taskchain",
            "co.aikar.idb"
        ).forEach { pkg ->
            relocate(pkg, "${group}.${rootProject.name.toLowerCase()}.lib.$pkg")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    main = "fi.fabianadrian.mmstatbridge.MMStatBridge"
    name = rootProject.name
    apiVersion = "1.17"
    author = "FabianAdrian"
    depend = listOf("PlaceholderAPI")
}

