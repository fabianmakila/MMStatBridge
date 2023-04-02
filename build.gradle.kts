plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.kyori.indra") version "3.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    java
}

group = "fi.fabianadrian"
version = "0.2.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.3")
    implementation("co.aikar:taskchain-bukkit:3.7.2")
    implementation("co.aikar:idb-bukkit:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.1")
}

indra {
    javaVersions().target(17)
}

tasks {
    shadowJar {
        minimize()
        sequenceOf(
            "co.aikar.taskchain",
            "co.aikar.idb"
        ).forEach { pkg ->
            relocate(pkg, "${group}.${rootProject.name.lowercase()}.lib.$pkg")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    main = "fi.fabianadrian.mmstatbridge.MMStatBridge"
    name = rootProject.name
    apiVersion = "1.19"
    author = "FabianAdrian"
    depend = listOf("PlaceholderAPI")
}

