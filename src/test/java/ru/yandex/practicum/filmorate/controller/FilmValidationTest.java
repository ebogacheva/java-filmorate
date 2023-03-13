package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;


import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(FilmController.class)
class FilmValidationTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String valueAsString;

    @BeforeAll
    public static void beforeAll() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Autowired
    private MockMvc mockMvc;

    private HttpStatus postChecking(Film film) throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(valueAsString)).andReturn();
        return HttpStatus.valueOf(result.getResponse().getStatus());
    }

    private HttpStatus putChecking(Film film) throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/films")
                .contentType(MediaType.APPLICATION_JSON).content(valueAsString)).andReturn();
        return HttpStatus.valueOf(result.getResponse().getStatus());
    }

    @Test
    void shouldReturnOkCodeForValidData() throws Exception {
        Film film = TestDataProvider.getFilmWithValidData();
        valueAsString = objectMapper.writeValueAsString(film);
        assertEquals(HttpStatus.OK, postChecking(film));
        Film filmCreated = film.withId(1);
        valueAsString = objectMapper.writeValueAsString(filmCreated);
        assertEquals(HttpStatus.OK, putChecking(film));
    }

    @Test
    void shouldReturnBadRequestCodeForEmptyName() throws Exception {
        Film film = TestDataProvider.getFilmWithEmptyName();
        valueAsString = objectMapper.writeValueAsString(film);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(film));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(film));
    }

    @Test
    void shouldReturnBadRequestCodeForNullName() throws Exception {
        Film film = TestDataProvider.getFilmWithNullName();
        valueAsString = objectMapper.writeValueAsString(film);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(film));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(film));
    }

    @Test
    void shouldReturnBadRequestCodeForTooLongDescription() throws Exception {
        Film film = TestDataProvider.getFilmWithTooLongDescription();
        valueAsString = objectMapper.writeValueAsString(film);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(film));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(film));
    }

    @Test
    void shouldReturnBadRequestCodeForInvalidDate() throws Exception {
        Film film = TestDataProvider.getFilmWithInvalidReleaseDate();
        valueAsString = objectMapper.writeValueAsString(film);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(film));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(film));
    }


    @Test
    void shouldReturnOkForExactStartFilmHistoryDate() throws Exception {
        Film film = TestDataProvider.getFilmWithExactStartFilmHistoryDate();
        valueAsString = objectMapper.writeValueAsString(film);
        assertEquals(HttpStatus.OK, postChecking(film));
        assertEquals(HttpStatus.NOT_FOUND, putChecking(film));
    }

    @Test
    void validateFilmWithInvalidDuration() throws Exception {
        Film film = TestDataProvider.getFilmWithInvalidDuration();
        valueAsString = objectMapper.writeValueAsString(film);
        assertEquals(HttpStatus.BAD_REQUEST, postChecking(film));
        assertEquals(HttpStatus.BAD_REQUEST, putChecking(film));
    }

}