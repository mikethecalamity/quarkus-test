package my.project.service3;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.type.TypeFactory;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(Service3Resource.class)
public class Service3ResourceTest {

    private static final List<String> EXPECTED_ORDER = List.of("shirt", "media", "entry", "buyer", "event", "steak",
            "actor", "uncle", "basis", "salad", "music", "owner", "video", "cheek", "hotel", "woman", "piano", "honey",
            "heart", "world");

    @Test
    public void test1() {
        final Type responseType = TypeFactory.defaultInstance().constructCollectionLikeType(ArrayList.class,
                String.class);
        final List<String> response = given().when().get("/order1").then().statusCode(200).extract().body()
                .as(responseType);

        assertThat(response).containsExactlyElementsOf(EXPECTED_ORDER);
    }

    @Test
    public void test2() {
        final Type responseType = TypeFactory.defaultInstance().constructCollectionLikeType(ArrayList.class,
                String.class);
        final List<String> response = given().when().get("/order2").then().statusCode(200).extract().body()
                .as(responseType);

        assertThat(response).containsExactlyElementsOf(EXPECTED_ORDER);
    }
}
