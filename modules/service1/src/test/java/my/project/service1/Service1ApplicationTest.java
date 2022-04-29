package my.project.service1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Service1ApplicationTest {

    @Test
    public void getClassesTest() {
        final Service1Application application = new Service1Application();
        assertEquals(1, application.getClasses().size());
    }
}
