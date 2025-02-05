package my.project.service1.jpa;

import java.io.Serializable;

public class MyDataType extends JsonType<MyData>{

    @Override
    public Class<MyData> returnedClass() {
        return MyData.class;
    }
}
