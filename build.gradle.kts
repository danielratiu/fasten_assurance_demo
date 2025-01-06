import de.itemis.mps.gradle.*
import de.itemis.mps.gradle.tasks.MpsCheck
import de.itemis.mps.gradle.tasks.MpsMigrate
import de.itemis.mps.gradle.tasks.Remigrate
import java.util.Date

plugins {
    base
    id("co.riiid.gradle") version "0.4.2"
    val mpsGradlePluginVersion = "1.28.0.+"
    id("download-jbr") version mpsGradlePluginVersion
    id("de.itemis.mps.gradle.common") version mpsGradlePluginVersion
}

// detect if we are in a CI build
val ciBuild = (System.getenv("CI") != null && System.getenv("CI").toBoolean()) || project.hasProperty("forceCI") || project.hasProperty("teamcity")

val fastenVersion = "2023.2.2117.28403a9"
val rcpRepo = if (ciBuild) "linux.rcp" else "win.rcp"

configurations {
    val mps by creating
    dependencies {
	    mps("fasten:$rcpRepo:$fastenVersion")
    }
}
 
dependencyLocking { lockAllConfigurations() }

repositories {
    val dependencyRepositories = listOf("https://maven.pkg.github.com/mbeddr/*")

    for (repoUrl in dependencyRepositories) {
        maven {
            url = uri(repoUrl)
            if (repoUrl.startsWith("https://maven.pkg.github.com/")) {
                credentials {
                    username = project.property("gpr.user") as String
                    password = project.property("gpr.token") as String
                }
            }
        }
    }
    mavenCentral()
}

val skipResolveMps = project.hasProperty("mpsHomeDir")
val mpsHomeDir = rootProject.file(project.findProperty("mpsHomeDir")
    ?: layout.buildDirectory.dir("mps").get().asFile.path)

val resolveMps = if (skipResolveMps) {
        tasks.register("resolveMps") {
            doLast {
                logger.info("MPS resolution skipped")
                logger.info("MPS home: {}", mpsHomeDir.getAbsolutePath())
            }
        }
    } else {
        tasks.register("resolveMps", Copy::class) {
            dependsOn(configurations["mps"])
            from({
                configurations["mps"].resolve().map(::tarTree)
            })
            into(mpsHomeDir)
        }
    }

tasks {
    withType<MpsCheck>().configureEach {
        dependsOn(resolveMps)

        mpsVersion = fastenVersion
        mpsHome = mpsHomeDir
        folderMacros.put("fasten.demo.home", layout.projectDirectory)
        pluginRoots.addAll(
            layout.buildDirectory.map { buildDir ->
                listOf("mps/plugins").map { buildDir.dir(it) }
            })
        ignoreFailures = true
        maxHeapSize = "2G"
    }
}

tasks.register("runFastenModelChecks", MpsCheck::class) {
    projectLocation = file(".")
    modules = listOf("fasten.assurance.demo")
}
defaultTasks("runFastenModelChecks")
