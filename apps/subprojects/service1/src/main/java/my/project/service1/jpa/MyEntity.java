package my.project.service1.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_table")
@NamedQueries({
    @NamedQuery(name = MyEntity.GET_LATEST_AT_TIME,
            query = "SELECT m FROM MyEntity AS m WHERE (m.associatedId, m.time) IN"
                    + " (SELECT innerM.associatedId, max(innerM.time) FROM MyEntity AS innerM"
                    + " WHERE innerM.time >= :start AND innerM.time <= :end "
                    + " AND innerM.deleted = false"
                    + " GROUP BY id)")
})
class MyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String GET_LATEST_AT_TIME = "my_table.getLatestAtTime";

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "time")
    private long time;

    @Column(name = "assc_id")
    private long associatedId;

    @Column(name = "deleted")
    private boolean deleted;
}