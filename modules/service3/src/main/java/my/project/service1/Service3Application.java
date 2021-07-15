package my.project.service1;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import my.project.service3.PluginResource;

@ApplicationPath("service3")
@ManagedBean
public class Service3Application extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(PluginResource.class);
        return set;
    }
}
