package my.project.plugin1;

import jakarta.enterprise.context.Dependent;

import my.project.plugin.Plugin;

@Dependent
public class PluginB implements Plugin {

    @Override
    public String getName() {
        return "pluginB";
    }
}