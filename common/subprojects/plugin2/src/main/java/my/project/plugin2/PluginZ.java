package my.project.plugin2;

import jakarta.enterprise.context.Dependent;

import my.project.plugin.Plugin;

@Dependent
public class PluginZ implements Plugin {

    @Override
    public String getName() {
        return "pluginZ";
    }
}