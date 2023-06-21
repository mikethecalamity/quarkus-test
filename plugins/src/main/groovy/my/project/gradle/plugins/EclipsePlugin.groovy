package my.project.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class EclipsePlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply org.gradle.plugins.ide.eclipse.EclipsePlugin

        project.eclipse.classpath {
            downloadSources = true
            downloadJavadoc = true
        }

        project.eclipse.project {
            if (project.plugins.hasPlugin org.gradle.api.plugins.quality.CheckstylePlugin) {
                buildCommand 'net.sf.eclipsecs.core.CheckstyleBuilder'
                natures 'net.sf.eclipsecs.core.CheckstyleNature'
            }

            if (project.plugins.hasPlugin com.github.spotbugs.snom.SpotbugsPlugin) {
                buildCommand 'edu.umd.cs.findbugs.plugin.eclipse.findbugsBuilder'
                natures 'edu.umd.cs.findbugs.plugin.eclipse.findbugsNature'
            }
        }
    }
}