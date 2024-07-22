package my.project.gradle.plugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply org.gradle.api.plugins.JavaPlugin

        project.java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            withJavadocJar()
            withSourcesJar()
        }

        project.compileJava {
            options.encoding = 'UTF-8'
            options.compilerArgs << '-parameters'
        }

        project.compileTestJava {
            options.encoding = 'UTF-8'
        }


    }
}