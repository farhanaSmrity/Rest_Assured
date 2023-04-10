package controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import model.UserModel;
import org.testng.annotations.Test;
import org.apache.commons.configuration.ConfigurationException;
import setup.Setup;
import utils.Utils;

import java.io.IOException;
import static io.restassured.RestAssured.given;
@Getter
@Setter
public class User extends Setup {
    public User() throws IOException {
        initConfig();
    }

    private String message;
    public void callingLogInAPI() throws ConfigurationException {
        UserModel loginModel = new UserModel("salman@roadtocareer.net", "1234");
        RestAssured.baseURI = prop.getProperty("base_url");
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel)
                .when()
                        .post("/user/login")
                .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonResponse = res.jsonPath();
        String token =  jsonResponse.get("token");
        String message = jsonResponse.get("message");

        Utils.setEnvVariable("token",token);

        System.out.println(token);
        System.out.println(message);
        setMessage(message);
    }

    public int getUserList(int pos)  {
        RestAssured.baseURI = prop.getProperty("base_url");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization",prop.getProperty("token"))
                .when()
                        .get("/user/list")
                .then()
                        .assertThat().statusCode(200).extract().response();
        JsonPath response = res.jsonPath();

        int id = Integer.parseInt(response.get("users["+pos+"].id").toString());
        return id;
    }
    public void getUserById(int id){
        RestAssured.baseURI = prop.getProperty("base_url");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization",prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY",System.getProperty("secretkey"))
                        .when()
                        .get("user/search/id/"+id+"")
                        .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        System.out.println(response.get("user").toString());
    }
    public  void createUser() throws ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        Utils utils = new Utils();
        utils.generateRandom();
        UserModel regModel = new UserModel(utils.getName(),utils.getEmail(),"1233",utils.generatePhoneNumber(),"100200300","customer");
        RestAssured.baseURI = prop.getProperty("base_url");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization",prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY",System.getProperty("secretkey"))
                        .body(regModel)
                        .when()
                        .post("/user/create")
                        .then()
                        .assertThat().statusCode(201).extract().response();

        JsonPath jsonResponse = res.jsonPath();
        System.out.println(jsonResponse.get().toString());
        Utils.setEnvVariable("id",jsonResponse.get("user.id").toString());
        Utils.setEnvVariable("name",jsonResponse.get("user.name"));
        Utils.setEnvVariable("email",jsonResponse.get("user.email"));
        Utils.setEnvVariable("phone_number",jsonResponse.get("user.phone_number"));
    }
}
