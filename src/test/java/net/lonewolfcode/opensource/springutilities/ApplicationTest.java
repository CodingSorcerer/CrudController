package net.lonewolfcode.opensource.springutilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.EmbeddableId;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.EmbededEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.ShapeEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationContext.class)
@AutoConfigureMockMvc
public class ApplicationTest {
    private static ObjectMapper mapper = new ObjectMapper();
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

    @Test
    public void testGetWithParamiters() throws Exception {
        Set<ShapeEntity> content = new HashSet<>();
        content.add(new ShapeEntity("triangle"));
        content.add(new ShapeEntity("Dodecahedron"));
        content.add(new ShapeEntity("square"));

        mvc.perform(post("/shapes").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(content)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(get("/shapes")).andReturn();
        Assert.assertEquals(content,mapper.readValue(result.getResponse().getContentAsString(),mapper.getTypeFactory().constructCollectionType(Set.class, ShapeEntity.class)));

        result = mvc.perform(get("/shapes?id=square")).andReturn();
        Assert.assertEquals("{\"name\":\"square\"}",result.getResponse().getContentAsString());
    }

    @Test
    public void testGetWithEmbeddedId() throws Exception {
        EmbededEntity expected = new EmbededEntity(new EmbeddableId("hi",123),"entity 3");
        Set<EmbededEntity> content = new HashSet<>();
        content.add(new EmbededEntity(new EmbeddableId("test",123),"entity 1"));
        content.add(new EmbededEntity(new EmbeddableId("hi",456),"entity 2"));
        content.add(expected);

        mvc.perform(post("/testid").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(content)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(get("/testid")).andReturn();
        Assert.assertEquals(content,mapper.readValue(result.getResponse().getContentAsString(),mapper.getTypeFactory().constructCollectionType(Set.class, EmbededEntity.class)));

        result = mvc.perform(get("/testid?key1=hi&key2=123")).andReturn();
        Assert.assertEquals(mapper.writeValueAsString(expected),result.getResponse().getContentAsString());

        mvc.perform(get("/testid?key1=hi&key2=hi")).andExpect(status().isNotFound());
    }
}
