package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(UserController.class)
public class UserValidationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String valueAsString;

    @BeforeAll
    public static void beforeAll() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserStorage userStorage;

    private HttpStatus postChecking(User user) throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(valueAsString)).andReturn();
        return HttpStatus.valueOf(result.getResponse().getStatus());
    }

    private HttpStatus putChecking(User user) throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON).content(valueAsString)).andReturn();
        return HttpStatus.valueOf(result.getResponse().getStatus());
    }

    @Test
    void shouldReturnOkForUserWithValidData() throws Exception {
        User user = TestDataProvider.getUserWithValidData();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.OK, postChecking(user));
        User userCreated = user.withId(1);
        valueAsString = OBJECT_MAPPER.writeValueAsString(userCreated);
        assertEquals(HttpStatus.OK, putChecking(user));

    }

    @Test
    void shouldReturnBadRequestForUserWithEmptyEmail() throws Exception {
        User user = TestDataProvider.getUserWithEmptyEmail();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(user));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(user));
    }

    @Test
    void shouldReturnBadRequestForUserWithNullEmail() throws Exception {
        User user = TestDataProvider.getUserWithNullEmail();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(user));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(user));
    }

    @Test
    void shouldReturnBadRequestForUserWithInvalidEmail() throws Exception {
        User user = TestDataProvider.getUserWithInvalidEmail();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(user));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(user));
    }

    @Test
    void shouldReturnBadRequestForUserWithInvalidLoginWithSpaces() throws Exception {
        User user = TestDataProvider.getUserWithInvalidLoginSpaces();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(user));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(user));
    }

    @Test
    void shouldReturnBadRequestForUserWithNullLogin() throws Exception {
        User user = TestDataProvider.getUserWithNullLoginSpaces();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(user));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(user));
    }

    @Test
    void shouldReturnBadRequestForUserWithEmptyLogin() throws Exception {
        User user = TestDataProvider.getUserWithEmptyLoginSpaces();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(user));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(user));
    }

    @Test
    void shouldReturnBadRequestForUserWithFutureBirthday() throws Exception {
        User user = TestDataProvider.getUserWithFutureBirthday();
        valueAsString = OBJECT_MAPPER.writeValueAsString(user);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(user));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(user));
    }

}
