package my.project.service1.jpa;

import java.io.Serializable;

import lombok.Data;

@Data
public class MyData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String name;
    private final int value;
}
