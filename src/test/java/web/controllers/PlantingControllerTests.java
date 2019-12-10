package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import model.repositories.CropRepository;
import model.repositories.GardenContentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import util.Helper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlantingControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GardenContentRepository gardenContentRepositoryMock;

    @MockBean
    private CropRepository cropRepositoryMock;

    private static String adminToken;

    private static String demoToken;

    private Crop crop1;

    private Crop crop2;

    @BeforeClass
    public static void setupTokens()
    {
        adminToken = Helper.getAccessToken("admin", "admin", "client", "secret");
        demoToken = Helper.getAccessToken("demo", "demo", "client", "secret");
    }

    @Before
    public void before()
    {
        crop1 = new Crop();
        crop1.setUserId("demo");
        crop1.setType(CropType.FRUIT);
        crop1.setName("Tomato");
        crop1.setLength(2);
        crop1.setWidth(2);
        crop1.setId(1);

        crop2 = new Crop();
        crop2.setUserId("demo");
        crop2.setType(CropType.ROOT);
        crop2.setName("Carrot");
        crop2.setLength(1);
        crop2.setWidth(1);
        crop2.setId(2);

        Companion companion1 = new Companion();
        companion1.setImpacted(crop2);
        crop2.addToImpactedBy(companion1);
        companion1.setImpacting(crop1);
        crop1.addToImpacts(companion1);
        companion1.setPositive(false);

        Garden garden = new Garden(8, 8, "demo");
        GardenContent gardenContent = garden.getGardenContents().get(0);

        List<ConcreteCrop> concreteCrops = new ArrayList<>();
        for(int i = 4; i < 8; i++)
        {
            for (int j = 4; j < 8; j++)
            {
                ConcreteCrop cc = new ConcreteCrop();
                cc.setCropTypeId(2);
                cc.setCropType(crop2);
                cc.setStartX(i);
                cc.setStartY(j);
                cc.setEndX(i + 1);
                cc.setEndY(j + 1);
                cc.setGardenContent(gardenContent);
                concreteCrops.add(cc);
            }
        }
        gardenContent.setPlantedCropsList(concreteCrops);
        when(cropRepositoryMock.findById(2)).thenReturn(Optional.of(crop2));
        when(cropRepositoryMock.findById(1)).thenReturn(Optional.of(crop1));
        when(gardenContentRepositoryMock.findByIdAndUserId(1, "demo")).thenReturn(Optional.of(gardenContent));
    }

    @Test
    public void test_zoom_and_window() throws Exception
    {
        MvcResult result = mockMvc.perform(get("/planting/1")
                .header("Authorization", "Bearer " + demoToken)
                .param("zoom", "2.0")
                .param("startX", "2")
                .param("startY", "2")
                .param("endX", "6")
                .param("endY", "6"))
                .andDo(print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ConcreteCrop> content = List.of(mapper.readValue(result.getResponse().getContentAsString(), ConcreteCrop[].class));
        Assert.assertTrue(content.size() == 4);
    }

    @Test
    public void test_cant_plant_on_taken_space() throws Exception
    {
        PlantingOperation po = new PlantingOperation();
        po.setX1(6);
        po.setY1(6);
        po.setY2(8);
        po.setX2(8);
        po.setCropId(2);
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(po);

        MvcResult result = mockMvc.perform(post("/planting/1")
                .header("Authorization", "Bearer " + demoToken)
                .header("Content-Type", "application/json").content(body)
                .param("zoom", "2.0")
                .param("startX", "2")
                .param("startY", "2")
                .param("endX", "6")
                .param("endY", "6"))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void test_can_plant_on_empty_space() throws Exception
    {
        PlantingOperation po = new PlantingOperation();
        po.setX1(0);
        po.setY1(0);
        po.setY2(4);
        po.setX2(8);
        po.setCropId(1);
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(po);

        MvcResult result = mockMvc.perform(post("/planting/1")
                .header("Authorization", "Bearer " + demoToken)
                .header("Content-Type", "application/json").content(body)
                .param("zoom", "1.0")
                .param("startX", "0")
                .param("startY", "0")
                .param("endX", "8")
                .param("endY", "8"))
                .andDo(print()).andExpect(status().isCreated()).andReturn();

        List<ConcreteCrop> content = List.of(mapper.readValue(result.getResponse().getContentAsString(), ConcreteCrop[].class));
        Assert.assertTrue(content.size() == 24);
    }

    @Test
    public void test_can_delete_crop() throws Exception
    {
        PlantingOperation po = new PlantingOperation();
        po.setX1(0);
        po.setY1(0);
        po.setY2(8);
        po.setX2(8);

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(po);

        MvcResult result = mockMvc.perform(delete("/planting/1")
                .header("Authorization", "Bearer " + demoToken)
                .header("Content-Type", "application/json").content(body)
                .param("zoom", "1.0")
                .param("startX", "0")
                .param("startY", "0")
                .param("endX", "8")
                .param("endY", "8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        List<ConcreteCrop> content = List.of(mapper.readValue(result.getResponse().getContentAsString(), ConcreteCrop[].class));
        Assert.assertTrue(content.size() == 0);
    }
}
