package my.project.service5;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("service5")
@ManagedBean
public class Service5Application extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(Service5Resource.class);
        return set;
    }
}
