package my.project.service1.jpa;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@IfBuildProfile("dev")
public class DevDataLoader {

    private final EntityManager em;

    @Inject
    public DevDataLoader(final EntityManager em) {
        this.em = em;
    }

    @Startup
    @Transactional
    void init() {
        em.persist(new MyEntity(1, 100, 20, false));
        em.persist(new MyEntity(2, 100, 20, false));
        em.persist(new MyEntity(3, 100, 20, false));
        em.persist(new MyEntity(4, 100, 20, false));
        em.persist(new MyEntity(5, 100, 20, true));
        em.persist(new MyEntity(6, 100, 20, false));
        em.persist(new MyEntity(7, 100, 20, false));
        em.persist(new MyEntity(8, 100, 20, true));
        em.persist(new MyEntity(9, 100, 20, false));
        em.persist(new MyEntity(10, 100, 20, false));
        em.persist(new MyEntity(11, 100, 20, false));
    }
}