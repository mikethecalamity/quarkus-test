package my.project.service1.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
                    + " WHERE innerM.time <= :time AND innerM.deleted = false"
                    + " GROUP BY innerM.associatedId)")
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