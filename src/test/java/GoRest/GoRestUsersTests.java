package GoRest;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {

    int userID;
    User newUser;

    @BeforeClass
    void Setup()
    {
        baseURI = "https://gorest.co.in/public/v2/users";
    }


    public String getRandomName()
    {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomEmail()
    {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@gmail.com";
    }


    @Test
    public void createUserObject()
    {
       // baslangic islemleri
       // token aldim
       // users JSON i hazirladim.

        int userID=
        given()
                .header("Authorization", "Bearer ad72d8d6e33e1a050a01d304c58b7bd711ba4d5ed735a0456ff40e1c1e6815ce")
                .contentType(ContentType.JSON)
                .body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                // üst taraf request özellikleridir : hazirlik islemleri POSTMAN deki Authorization ve request BODY kismi

                .log().uri()
                .log().body()
                .when()  // request in oldugu nokta POSTMAN deki SEND butonu
                .post("")  // respons un olustugu nokta CREATE islemi POST metodu ile cagiriyoruz POSTMAN deki gibi

                // alt taraf response sonrasi POSTMAN deki test penceresi
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
        ;

        System.out.println("userID = " + userID);

    }


    @Test
    public void createUserObject2WithMap()
    {
        Map<String,String> neuUser= new HashMap<>();
        neuUser.put("name",getRandomName());
        neuUser.put("gender","male");
        neuUser.put("email",getRandomEmail());
        neuUser.put("status","active");

        int userID=
                given()
                        .header("Authorization", "Bearer ad72d8d6e33e1a050a01d304c58b7bd711ba4d5ed735a0456ff40e1c1e6815ce")
                        .contentType(ContentType.JSON)
                        .body(neuUser)
                        .log().uri()
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;

        System.out.println("userID = " + userID);

    }


    @Test
    public void createUserObject3WithObject()
    {
        newUser= new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        userID=
                given()
                        .header("Authorization", "Bearer ad72d8d6e33e1a050a01d304c58b7bd711ba4d5ed735a0456ff40e1c1e6815ce")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().uri()
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id")
                        .extract().jsonPath().getInt("id")
                ;

        System.out.println("userID = " + userID);

        // path : class veya tip dönüsümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüsümüne ve tip dönüsümüne izin vererek, veriyi istedigimiz formatta verir.
    }


    @Test(dependsOnMethods = "createUserObject3WithObject")
    public void getUserByID()
    {

                given()

                        .header("Authorization", "Bearer ad72d8d6e33e1a050a01d304c58b7bd711ba4d5ed735a0456ff40e1c1e6815ce")
                        .pathParam("userId", userID)
                        .log().uri()

                        .when()
                        .get("/{userId}")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("id", equalTo(userID))
                ;

    }

}

class User{
    private String name;
    private String gender;
    private String email;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}