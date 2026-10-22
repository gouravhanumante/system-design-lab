pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "system-design-lab"

// Every problem folder (levels/<level>/<NN-app-slug>/ with a src/ directory) becomes its own module.
// Module name: "l<level number>-<folder name without its NN- prefix>", e.g. ":l1-uber-nearest-driver".
file("levels").listFiles()?.filter { it.isDirectory }?.sortedBy { it.name }?.forEach { level ->
    val levelNo = level.name.removePrefix("level-").substringBefore("-")
    level.listFiles()?.filter { File(it, "src").isDirectory }?.sortedBy { it.name }?.forEach { dir ->
        val name = "l$levelNo-" + dir.name.substringAfter("-")
        include(name)
        project(":$name").projectDir = dir
    }
}
