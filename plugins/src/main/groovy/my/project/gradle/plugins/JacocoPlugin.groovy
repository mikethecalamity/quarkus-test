package my.project.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class JacocoPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply org.gradle.testing.jacoco.plugins.JacocoPlugin

        project.jacoco {
            toolVersion = '0.8.10'
        }

        project.test {
            finalizedBy project.jacocoTestReport
        }

        project.jacocoTestReport {
            dependsOn project.test

            reports {
                csv.required = false
                xml.required = true
            }
        }

        project.check {
            dependsOn project.jacocoTestReport
            dependsOn project.jacocoTestCoverageVerification
        }
    }
}