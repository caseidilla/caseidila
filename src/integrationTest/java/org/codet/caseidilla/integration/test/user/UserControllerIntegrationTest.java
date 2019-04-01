package org.codet.caseidilla.integration.test.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.codet.caseidilla.user.entity.User;
import org.codet.caseidilla.user.repository.UserRepository;
import org.codet.caseidilla.user.secret.entity.Secret;
import org.codet.caseidilla.user.secret.repository.SecretRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretRepository secretRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        //containsString is used because I am lazy
    }

    @Test
    @DirtiesContext
    public void register_success() {
        secretRepository.save(Secret.builder()
                .code("123")
                .timestamp(Instant.MAX)
                .user("loupa")
                .build());
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"login\":\"boi\"," +
                        "\"password\":\"111\"," +
                        "\"secret\":\"123\"" +
                        "}")
                .when()
                .post("/api/register")
                .then()
                .assertThat()
                .statusCode(200);

        Optional<User> optional = userRepository.findById("boi");
        assertThat(optional.isPresent(), equalTo(true));
        User actual = optional.get();
        assertThat(actual.getPassword(), equalTo("111"));
    }

    @Test
    @DirtiesContext
    public void login_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"login\":\"loupa\"," +
                        "\"password\":\"111\"" +
                        "}")
                .when()
                .post("/api/login")
                .then()
                .assertThat()
                .statusCode(200);
    }


    @Test
    @DirtiesContext
    public void invite_success() {
        Response response = given()
                .contentType(ContentType.JSON)
                .port(port)
                .when()
                .get("/api/loupa/invite");
        JsonPath jsonPath = response.jsonPath();
        String secret = jsonPath.get("secret");
        assertThat(secret.length(), equalTo(8));
        response.then()
                .statusCode(200);
    }

    @Test
    @DirtiesContext
    public void setPin_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"pin\":\"123\"," +
                        "\"oldPin\":\"11\"" +
                        "}")
                .when()
                .post("/api/loupa/setPin")
                .then()
                .assertThat()
                .statusCode(200);

        Optional<User> optional = userRepository.findById("loupa");
        assertThat(optional.isPresent(), equalTo(true));
        User actual = optional.get();
        assertThat(actual.getPin(), equalTo("123"));
    }
}
