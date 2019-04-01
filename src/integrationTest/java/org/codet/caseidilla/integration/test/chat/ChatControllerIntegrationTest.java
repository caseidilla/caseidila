package org.codet.caseidilla.integration.test.chat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.codet.caseidilla.chat.entity.Dialog;
import org.codet.caseidilla.chat.entity.Message;
import org.codet.caseidilla.chat.repository.DialogRepository;
import org.codet.caseidilla.chat.repository.MessageRepository;
import org.codet.caseidilla.user.entity.User;
import org.codet.caseidilla.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.toList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        //containsString is used because I am lazy
    }

    @Test
    @DirtiesContext
    public void listDialogs_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .when()
                .get("/api/loupa/dialogs")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("[{\"login\":\"poupa\",\"name\":\"dialog\",\"secret\":false,\"hidden\":false}]"));
    }

    @Test
    @DirtiesContext
    public void listHiddenDialogs_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"pin\":\"11\"" +
                        "}")
                .when()
                .post("/api/loupa/dialogs/hidden")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("[{\"login\":\"behindloupa\",\"name\":\"behindloupa\",\"secret\":false,\"hidden\":true},{\"login\":\"poupa\",\"name\":\"dialog\",\"secret\":false,\"hidden\":false}]"));
    }

    @Test
    @DirtiesContext
    public void getDialog_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .when()
                .get("/api/loupa/dialog?participant=poupa")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("[{\"type\":\"sent\",\"body\":\"hi\",\"timestamp\":\"2012-09-17T14:47:52.069Z\"},{\"type\":\"received\",\"body\":\"hello\",\"timestamp\":\"2013-09-17T14:47:52.069Z\"}]"));
    }

    @Test
    @DirtiesContext
    public void sendMessage_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"participant\":\"poupa\"," +
                        "\"body\":\"where are you?\"" +
                        "}")
                .when()
                .post("/api/loupa/dialog/send")
                .then()
                .assertThat()
                .statusCode(200);

        Message actual = toList(messageRepository.findAll()).get(2);

        assertThat(actual.getBody(), equalTo("where are you?"));
        assertThat(actual.getReceiver(), equalTo("poupa"));
        assertThat(actual.getSender(), equalTo("loupa"));
    }

    @Test
    @DirtiesContext
    public void changeDialogName_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"participant\":\"poupa\"," +
                        "\"name\":\"dialoge\"" +
                        "}")
                .when()
                .post("/api/loupa/dialog/changeName")
                .then()
                .assertThat()
                .statusCode(200);

        Dialog actual = toList(dialogRepository.findAll()).get(1);
        assertThat(actual.getName(), equalTo("dialoge"));
    }

    @Test
    @DirtiesContext
    public void hideDialog_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"participant\":\"poupa\"," +
                        "\"pin\":\"11\"" +
                        "}")
                .when()
                .post("/api/loupa/dialog/hide")
                .then()
                .assertThat()
                .statusCode(200);

        Dialog actual = toList(dialogRepository.findAll()).get(1);
        assertThat(actual.isHidden(), equalTo(true));
    }

    @Test
    @DirtiesContext
    public void createDialog_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"participant\":\"poupa\"," +
                        "\"secret\":false" +
                        "}")
                .when()
                .post("/api/behindloupa/dialog/new")
                .then()
                .assertThat()
                .statusCode(200);

        Dialog actual = toList(dialogRepository.findAll()).get(2);
        ArrayList<User> users = new ArrayList<>(actual.getUsers());
        assertThat(users.get(0).getLogin(), equalTo("poupa"));
        assertThat(users.get(1).getLogin(), equalTo("behindloupa"));
    }


    @Test
    @DirtiesContext
    public void deleteDialog_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body("{" +
                        "\"participant\":\"loupa\"" +
                        "}")
                .when()
                .post("/api/poupa/dialog/delete")
                .then()
                .assertThat()
                .statusCode(200);

        assertThat(dialogRepository.findAll().spliterator().estimateSize(), equalTo(1L));
    }

    @Test
    @DirtiesContext
    public void findDialog_success() {
        given()
                .contentType(ContentType.JSON)
                .port(port)
                .when()
                .get("/api/loupa/find?participant=poupa")
                .then()
                .assertThat()
                .body(containsString("{\"found\":true}"))
                .statusCode(200);
    }
}
