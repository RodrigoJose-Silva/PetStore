//1 - Pacote
package petstore;
//2-Biblioteca


import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

//3- Classe
public class Pet {
    //3.1 - Atributos
    String urli = "https://petstore.swagger.io/v2/pet"; //url da entidade (API) Pet


    //3.2 - Métodos e Funções
    public String lerJson (String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    //Incluir - Create - Post
    @Test(priority=1)  // Identifica o método ou função como "TESTE" para o TestNG
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("db/pet1.json");

        //Sintaxe Gherkin
        // DADO - QUANDO - ENTÃO
        //Given - When - Then

        given()//DADO
                .contentType("application/json") //comum em API REST - antigas eram "text / xml"
                .log().all()
                .body(jsonBody)
        .when() //QUANDO
                .post(urli)
        .then() //ENTÃO
                .log().all()
                .statusCode(200)
                .body("name", is("Pandora"))
                .body("status", is("available"))
                .body("category.name", is("RHTJTR154BTJ415NTR5"))
                .body("tags.name", contains("Curso de teste API"))// estrutura utilizado para validar dados de subcategorias
        ;
    }

    @Test(priority=2)
    public void consultarPet () {
        String petId = "2021081301";

        String token =
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(urli + "/" + petId)
        .then()
                .statusCode(200)
                .log().all()
                .body("name", is("Pandora"))
                .body("category.name", is("RHTJTR154BTJ415NTR5"))
                .body("tags.id", contains(2021))
                .body("status", is("available"))
        .extract()
                .path("category.name")
        ;

        System.out.println("A chave do token utilizada é: " + token);

    }

    @Test(priority=3)
    public void alterarPet () throws IOException {
        String jsonBody = lerJson("db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(urli)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Pandora"))
                .body("status", is("sold"))
        ;
    }

    @Test(priority=4)
    public void deletaPet () {
        String petId = "2021081301";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(urli + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;
    }
}
