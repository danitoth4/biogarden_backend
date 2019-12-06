package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import model.repositories.GardenContentRepository;
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

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlantingControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GardenContentRepository gardenContentRepositoryMock;

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
        crop1.setUserId("demo");
        crop1.setType(CropType.FRUIT);
        crop1.setName("Tomato");
        crop1.setLength(2);
        crop1.setWidth(2);
        crop1.setId(1);

        Crop crop2 = new Crop();
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

        Garden garden = new Garden(100, 100, "demo");
        GardenContent gardenContent = garden.getGardenContents().get(0);

        List<ConcreteCrop> concreteCrops = new ArrayList<>();
        for(int i = 40; i < 60; i += 2)
        {
            for (int j = 40; j < 60; j += 2)
            {
                ConcreteCrop cc = new ConcreteCrop();
                cc.setCropTypeId(1);
                cc.setCropType(crop1);
                cc.setStartX(i);
                cc.setStartY(j);
                cc.setEndX(i + 2);
                cc.setEndY(j + 2);
                cc.setGardenContent(gardenContent);
                concreteCrops.add(cc);
            }
        }
        gardenContent.setPlantedCropsList(concreteCrops);

        when(gardenContentRepositoryMock.findByIdAndUserId(1, "demo")).thenReturn(Optional.of(gardenContent));
    }

    @Test
    public void test_zoom_and_window() throws Exception
    {
        MvcResult result = mockMvc.perform(get("/planting/1")
                .header("Authorization", "Bearer " + demoToken)
                .param("zoom", "4.0")
                .param("startX", "0")
                .param("startY", "0")
                .param("endX", "60")
                .param("endY", "60"))
                .andDo(print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ConcreteCrop> content = List.of(mapper.readValue(result.getResponse().getContentAsString(), ConcreteCrop[].class));
        System.out.println("Hello World");
    }

}
