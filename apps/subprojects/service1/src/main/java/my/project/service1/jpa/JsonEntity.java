package my.project.service1.jpa;

import java.io.Serializable;

import org.hibernate.annotations.Type;

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
@Table(name = "json_table")
@NamedQueries({
    @NamedQuery(name = JsonEntity.GET_DATA_WITH_FILTER,
            query = "SELECT j FROM JsonEntity AS j WHERE j.id = :id AND j.data.value = :value")
})
class JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String GET_DATA_WITH_FILTER = "json_table.getDataWithFilter";

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "data")
    @Type(MyDataType.class)
    private MyData data;
}