import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test(){

        given()
                // hazırlık işlemlerini yapacağız (token,send body, parametreler)
                .when()
                // link i ve metodu veriyoruz
                .then()
                //  assertion ve verileri ele alma extract
        ;


    }

    @Test
    public void statusCodeTest(){

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .contentType(ContentType.JSON) // dönen sonuc JSON mi
        ;

    }

    @Test
    public void contentTypeTest(){

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .contentType(ContentType.JSON) // dönen sonuc JSON mi
        ;

    }

    @Test
    public void checkCountryInResponseBody(){

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .body("country", equalTo("United States"))
        ;

    }


//    pm (Postmann)                   RestAssured
//    body.country                    body("country",
//    body.'post code'                body("post code",
//    body.places[0].'place name'     body("places[0].'place name'")
//    body.places.'place name'        body("places.'place name'")   -> bütün place name leri verir
//                                    bir index verilmezse dizinin bütün elemanlarında arar


    @Test
    public void checkStateInResponseBody(){

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .body("places[0].state", equalTo("California")) //birebir esit mi
        ;

    }

    @Test
    public void bodyJsonPathTest3(){

        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .body("places.'place name'", hasItem("Dervişler Köyü"))  // verilen path deki liste de bu item e sahip mi, contains
        ;

    }

    @Test
    public void caglayanCeritTest3(){ //Caglayancerit mah. leri (köyleri)

        given()

                .when()
                .get("http://api.zippopotam.us/tr/46720")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .body("places.'place name'", hasItem("Küçüküngüt Köyü"))
        ;

    }

    @Test
    public void bodyArrayHasSizeTest(){

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .body("places", hasSize(1)) //palace in size 1 e esit mi
        ;

    }

    @Test
    public void combiningTest(){

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() bütün respons u gösterir
                .statusCode(200) //status kontrolü
                .body("places", hasSize(1)) //palace in size 1 e esit mi
                .body("places.state", hasItem("California"))  //verilen path deki list bu item e sahip mi
                .body("places[0].'place name'", equalTo("Beverly Hills"))
        ;

    }

    @Test
    public void pathParamTest() {

        given()
                .pathParam("Country", "us")
                .pathParam("ZipKod", 90210)
                .log().uri() //request link Request URI: http://api.zippopotam.us/us/90210

                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                .then()
                .log().body()
                .statusCode(200)
        ;

    }

    @Test
    public void pathParamTest2() {
        //90210 dan 90213 kadar test sonuclarinda places in size nin hepsinde 1 geldigini test ediniz.

        for(int i=90210; i<= 90213; i++) {
            given()
                    .pathParam("Country", "us")
                    .pathParam("ZipKod", i)
                    .log().uri() //request link Request URI: http://api.zippopotam.us/us/90210

                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("places", hasSize(1))
            ;
        }

    }

    @Test
    public void queryParamTest() {
     // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 1)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(1))
        ;

    }

    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfalari cagirdiginizda response daki dönen page degerlerinin
        // cagrilan page nosu ile ayni olup olmadigini kontrol ediniz.

        for(int pageNo=1; pageNo <=10; pageNo++) {
            given()
                    .param("page", pageNo)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(pageNo))
            ;
        }

    }


    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    void Setup(){

        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void RequestResponseSpecificationn() {
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 1)
                .spec(requestSpec)

                .when()
                .get("/users") //http olsa idi baseURI yi kullanmazdi

                .then()
                .body("meta.pagination.page", equalTo(1))
                .spec(responseSpec)
        ;

    }


    // JSON extract

    @Test
    public void extractingJsonPath()
    {
        String placeName=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                //.log().body()
                .extract().path("places[0].'place name'")

                // extract metodu ile given ile baslayan satir,
                // bir deger döndürür hale geldi, en sonda extract olmali
        ;

        System.out.println("placeName = " + placeName);

    }

    @Test
    public void extractingJsonPathInt(){
        // alinacak tipin degerine en uygun olan karsiliktaki tip yazilir

        int limit=
        given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit")
        ;

        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");
    }

    @Test
    public void extractingJsonPathList() {
        // alınacak tipin değerine en uygun olan karşılıktaki tip yazılır
        List<Integer> idler =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id")
                ;
        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(4180));

    }

    @Test
    public void extractingJsonPathStringList() {

        List<String> isimler =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()   //.log().body() acik oldugunda, bütün bilgileri verir
                        .statusCode(200)
                        .extract().path("data.name")
                ;
        System.out.println("isimler = " + isimler);
        Assert.assertTrue(isimler.contains("Mandaakin Patel")); //Isimler listesinde bu isim var mi

    }


    @Test
    public void extractingJsonPathResponsAll() {

        Response response =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response() //bütün body alindi  //.log().body() gözüken body
                ;

        List<Integer> idler = response.path("data.id");
        List<String> isimler = response.path("data.name");
        int limit = response.path("meta.pagination.limit");

        System.out.println("response = " + response.prettyPrint()); //.log().body(), ayni calisiyor
        System.out.println("idler = " + idler);
        System.out.println("isimler = " + isimler);
        System.out.println("limit = " + limit);

        Assert.assertTrue(idler.contains(4180));
        Assert.assertTrue(isimler.contains("Mandaakin Patel"));
        Assert.assertEquals(limit,10,"test sonucu");

    }


    @Test
    public void extractingJson()
    { //POJO (Plain Old Java Object)

//        Ogrenci ogr=new Ogrenci();
//        ogr.id=1;
//        ogr.isim="ismet temur";
//        ogr.tel="34343434";
//
//        System.out.println("ogr.tel = " + ogr.tel);

        Location yer=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .extract().as(Location.class)  //Location sablonuna göre
                
        ;

        System.out.println("yer = " + yer.getPostCode());
        System.out.println("yer.getPlaces().get(0).getPlaceName() = " +
                            yer.getPlaces().get(0).getPlaceName());

    }




}
