package my.project.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class QuarkusExtensionPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply io.quarkus.extension.gradle.QuarkusExtensionPlugin
    }
}