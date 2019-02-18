package net.lonewolfcode.opensource.springutilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationContext.class)
@AutoConfigureMockMvc
public class ApplicationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void notFoundExceptionThrows404() throws Exception {
        mvc.perform(get("/denied")).andExpect(status().isNotFound());
    }

    @Test
    public void unprocessableEntityIsThrown() throws Exception {
        mvc.perform(post("/shapes").contentType(MediaType.APPLICATION_JSON).content("bad JSON"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void postReturnsCreatedOnSuccess() throws Exception {
        mvc.perform(post("/shapes").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"triangle\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void typeConversionErrorThrows404() throws Exception {
        mvc.perform(get("/people/notanint")).andExpect(status().isNotFound());
    }
}
