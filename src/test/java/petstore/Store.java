package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.registerParser;
import static org.hamcrest.Matchers.is;

public class Store {
    String urli = "https://petstore.swagger.io/v2/store/order";

    public String lerJson (String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test
    public void cadastroPet() throws IOException {
        String jsonBody = lerJson("db/store1.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(urli)
        .then()
                .log().all()
                .statusCode(200)
                .body("petId", is(10))
                .body("status", is("placed"))
        ;
    }
}
