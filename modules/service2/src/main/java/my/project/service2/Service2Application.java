package my.project.service2;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import my.project.service1.PluginResource;

@ApplicationPath("service")
@ManagedBean
public class Service2Application extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(PluginResource.class);
        return set;
    }
}
