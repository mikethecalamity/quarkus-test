package my.project.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class QuarkusPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply io.quarkus.gradle.QuarkusPlugin

        project.compileJava.dependsOn project.compileQuarkusGeneratedSourcesJava
        project.compileTestJava.dependsOn project.compileQuarkusTestGeneratedSourcesJava
        project.sourcesJar.dependsOn project.compileQuarkusGeneratedSourcesJava
    }
}