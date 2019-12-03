package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Companion;
import model.Crop;
import model.CropType;
import model.repositories.CropRepository;
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
public class CropControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CropRepository cropRepositoryMock;

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
        Crop crop1 = new Crop();
        crop1.setUserId("admin");
        crop1.setType(CropType.FRUIT);
        crop1.setName("Tomato");
        crop1.setDiameter(2);
        crop1.setId(1);

        Crop crop2 = new Crop();
        crop2.setUserId("admin");
        crop2.setType(CropType.ROOT);
        crop2.setName("Carrot");
        crop2.setDiameter(1);
        crop2.setId(2);

        Companion companion = new Companion();
        companion.setImpacted(crop2);
        crop2.addToImpactedBy(companion);
        companion.setImpacting(crop1);
        crop1.addToImpacts(companion);
        companion.setPositive(false);
        companion.setId(3);

        when(cropRepositoryMock.findById(1)).thenReturn(Optional.of(crop1));
        when(cropRepositoryMock.findByIdAndUserId(1, "admin")).thenReturn(Optional.of(crop1));
        when(cropRepositoryMock.findAllByUserId("admin")).thenReturn(List.of(new Crop[]{crop1, crop2}));
    }

    @Test
    public void test_get_all_crops() throws Exception
    {
        mockMvc.perform(get("/crop").header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Tomato\",\"description\":null,\"diameter\":2,\"imageUrl\":null,\"type\":\"FRUIT\",\"userId\":\"admin\"},{\"id\":2,\"name\":\"Carrot\",\"description\":null,\"diameter\":1,\"imageUrl\":null,\"type\":\"ROOT\",\"userId\":\"admin\"}]"));
    }

    @Test
    public void test_create_template() throws Exception
    {
        mockMvc.perform(get("/crop").header("Authorization", "Bearer " + demoToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":0,\"name\":\"Tomato\",\"description\":null,\"diameter\":2,\"imageUrl\":null,\"type\":\"FRUIT\",\"userId\":\"demo\"},{\"id\":0,\"name\":\"Carrot\",\"description\":null,\"diameter\":1,\"imageUrl\":null,\"type\":\"ROOT\",\"userId\":\"demo\"}]"));
    }

    @Test
    public void test_get_single_crop() throws Exception
    {
        mockMvc.perform(get("/crop/1").header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Tomato\",\"description\":null,\"diameter\":2,\"imageUrl\":null,\"type\":\"FRUIT\",\"userId\":\"admin\"}"));
    }

    @Test
    public void test_get_single_non_existing_crop() throws Exception
    {
        mockMvc.perform(get("/crop/10").header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_update_crop() throws Exception
    {
        Crop updated = new Crop();
        updated.setUserId("admin");
        updated.setType(CropType.FRUIT);
        updated.setName("Tomato");
        updated.setDiameter(2);
        updated.setId(1);

        updated.setDescription("description change test");
        updated.setImageUrl("http://123.com");

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(updated);

        when(cropRepositoryMock.save(updated)).thenReturn(updated);

        mockMvc.perform(put("/crop/1")
                .header("Authorization", "Bearer " + adminToken).header("Content-Type", "application/json").content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updated)));
    }

    @Test
    public void test_create_new_crop() throws Exception
    {
        Crop newCrop = new Crop();
        newCrop.setType(CropType.FRUIT);
        newCrop.setName("Potato");
        newCrop.setDescription("This is a test crop");
        newCrop.setDiameter(2);
        newCrop.setImageUrl("http://11.com");

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(newCrop);

        newCrop.setUserId("demo");

        when(cropRepositoryMock.save(newCrop)).thenReturn(newCrop);

        mockMvc.perform(post("/crop")
                .header("Authorization", "Bearer " + demoToken).header("Content-Type", "application/json").content(body))
                .andDo(print())
                .andExpect(status().isCreated()).andExpect(content().json(mapper.writeValueAsString(newCrop)));
    }

    @Test
    public void test_delete_crop() throws Exception
    {
        mockMvc.perform(delete("/crop/1").header("Authorization", "Bearer " + demoToken))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/crop/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

}
