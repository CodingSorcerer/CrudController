package net.lonewolfcode.opensource.springutilities;

import net.lonewolfcode.opensource.springutilities.controllers.CrudController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(CrudController.class)
public class ApplicationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testApplicaitionTest() throws Exception {
        mvc.perform(get("/stories"));
    }
}
