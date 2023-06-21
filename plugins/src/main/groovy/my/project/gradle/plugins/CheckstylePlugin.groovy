package my.project.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class CheckstylePlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply org.gradle.api.plugins.quality.CheckstylePlugin

        project.checkstyle {
            toolVersion = '10.10.0'
            maxWarnings = 0
        }
    }
}