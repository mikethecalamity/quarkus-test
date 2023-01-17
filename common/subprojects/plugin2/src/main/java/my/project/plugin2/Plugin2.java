package my.project.plugin2;

import javax.enterprise.context.Dependent;

import my.project.plugin.Plugin;

@Dependent
public class Plugin2 implements Plugin {

    @Override
    public String getName() {
        return "plugin2";
    }
}