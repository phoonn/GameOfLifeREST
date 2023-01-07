package com.gameoflife.app.api;


import com.gameoflife.app.service.GameOfLifeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameOfLifeController.class)
public class GameOfLifeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameOfLifeService gameService;

    @Test
    void testResponse() throws Exception {
        final String result = "[][X][]";
        Mockito.when(gameService.printBoard(ArgumentMatchers.any())).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/game/play")
                        .queryParam("height", "0")
                        .queryParam("width", "10")
                        .queryParam("iterations", "10")
                        .queryParam("seed", "123321"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/game/play")
                        .queryParam("height", "10")
                        .queryParam("width", "0")
                        .queryParam("iterations", "10")
                        .queryParam("seed", "123321"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/game/play")
                        .queryParam("height", "10")
                        .queryParam("width", "10")
                        .queryParam("iterations", "10")
                        .queryParam("seed", "123321"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo(result)));

    }
}
