package my.project.service3;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("service")
@ManagedBean
public class Service3Application extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(PluginResource.class);
        return set;
    }
}
