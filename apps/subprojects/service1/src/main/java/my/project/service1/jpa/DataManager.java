package my.project.service1.jpa;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Dependent
public class DataManager {

    private final EntityManager em;

    @Inject
    public DataManager(final EntityManager em) {
        this.em = em;
    }

    public List<Long> getAll(final long time) {
        final TypedQuery<MyEntity> q = em.createNamedQuery(MyEntity.GET_LATEST_AT_TIME, MyEntity.class);
        q.setParameter("time", time);
        return q.getResultStream().map(MyEntity::getId).collect(Collectors.toList());
    }
}