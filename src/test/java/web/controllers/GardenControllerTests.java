package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Companion;
import model.Crop;
import model.CropType;
import model.Garden;
import model.repositories.CropRepository;
import model.repositories.GardenRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import util.Helper;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GardenControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GardenRepository gardenRepositoryMock;

    private static String adminToken;

    private static String demoToken;

    @BeforeClass
    public static void setupTokens()
    {
        adminToken = Helper.getAccessToken("admin", "admin", "client", "secret");
        demoToken = Helper.getAccessToken("demo", "demo", "client", "secret");
    }

    @Before
    public void before()
    {
        Garden garden1 = new Garden(100, 100, "demo");
        garden1.setName("Test Garden 1");
        garden1.setId(1);

        Garden garden2 = new Garden(150, 200, "demo");
        garden2.setName("Test Garden 2");
        garden2.setId(2);

        Garden garden3 = new Garden(150, 200, "admin");
        garden3.setName("Test Garden 2");
        garden3.setId(3);

        when(gardenRepositoryMock.findGardensByUserId("demo")).thenReturn(List.of(new Garden[]{garden1, garden2}));
        when(gardenRepositoryMock.findById(1)).thenReturn(Optional.of(garden1));
    }

    @Test
    public void test_get_all_gardens() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(get("/garden").header("Authorization", "Bearer " + demoToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Test Garden 1\",\"length\":100,\"width\":100,\"userId\":\"demo\",\"gardenContents\":[{\"id\":0,\"name\":\"Default\",\"before\":null,\"after\":null,\"userId\":\"demo\"}]},{\"id\":2,\"name\":\"Test Garden 2\",\"length\":150,\"width\":200,\"userId\":\"demo\",\"gardenContents\":[{\"id\":0,\"name\":\"Default\",\"before\":null,\"after\":null,\"userId\":\"demo\"}]}]"));
    }

    @Test
    public void test_get_single_garden() throws Exception
    {
        mockMvc.perform(get("/garden/1").header("Authorization", "Bearer " + demoToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Test Garden 1\",\"length\":100,\"width\":100,\"userId\":\"demo\",\"gardenContents\":[{\"id\":0,\"name\":\"Default\",\"before\":null,\"after\":null,\"userId\":\"demo\"}]}"));
    }

    @Test
    public void test_get_single_non_existing_garden() throws Exception
    {
        mockMvc.perform(get("/garden/10").header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_update_garden() throws Exception
    {
        Garden updated = new Garden(100, 100, "demo");
        updated.setName("Updated Name");
        updated.setId(1);

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(updated);

        when(gardenRepositoryMock.save(updated)).thenReturn(updated);

        mockMvc.perform(put("/garden/1")
                .header("Authorization", "Bearer " + demoToken).header("Content-Type", "application/json")
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updated)));
    }

    @Test
    public void test_create_new_garden() throws Exception
    {
        Garden newGarden = new Garden(1000, 1000, "");
        newGarden.setName("Test New Garden");

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(newGarden);

        newGarden.setUserId("demo");

        when(gardenRepositoryMock.save(newGarden)).thenReturn(newGarden);

        mockMvc.perform(post("/garden").header("Authorization", "Bearer " + demoToken).header("Content-Type", "application/json").content(body))
                .andDo(print())
                .andExpect(status().isCreated()).andExpect(content().json(mapper.writeValueAsString(newGarden)));
    }

    @Test
    public void test_delete_garden() throws Exception
    {
        mockMvc.perform(delete("/garden/1").header("Authorization", "Bearer " + demoToken)).andExpect(status().isNoContent());
    }

}
