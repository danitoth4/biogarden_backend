package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Companion;
import model.Crop;
import model.CropType;
import model.repositories.CompanionRepository;
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
public class CompanionControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CropRepository cropRepositoryMock;

    @MockBean
    private CompanionRepository companionRepositoryMock;

    private static String adminToken;

    private static String demoToken;

    private Crop crop1, crop2, crop3;

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
        crop1.setUserId("admin");
        crop1.setType(CropType.FRUIT);
        crop1.setName("Tomato");
        crop1.setDiameter(2);
        crop1.setId(1);

        crop2 = new Crop();
        crop2.setUserId("admin");
        crop2.setType(CropType.ROOT);
        crop2.setName("Carrot");
        crop2.setDiameter(1);
        crop2.setId(2);

        crop3 = new Crop();
        crop3.setUserId("admin");
        crop3.setType(CropType.LEAF);
        crop3.setName("Cabbage");
        crop3.setDiameter(1);
        crop3.setId(3);

        Companion companion1 = new Companion();
        companion1.setImpacted(crop2);
        crop2.addToImpactedBy(companion1);
        companion1.setImpacting(crop1);
        crop1.addToImpacts(companion1);
        companion1.setPositive(false);
        companion1.setId(4);

        Companion companion2 = new Companion();
        companion2.setImpacted(crop1);
        crop1.addToImpactedBy(companion2);
        companion2.setImpacting(crop3);
        crop3.addToImpacts(companion2);
        companion2.setPositive(true);
        companion2.setId(5);

        when(cropRepositoryMock.findById(1)).thenReturn(Optional.of(crop1));
        when(cropRepositoryMock.findByIdAndUserId(1, "admin")).thenReturn(Optional.of(crop1));
        when(cropRepositoryMock.findAllByUserId("admin")).thenReturn(List.of(new Crop[]{crop1, crop2, crop3}));
        when(companionRepositoryMock.findById(4)).thenReturn(Optional.of(companion1));
    }

    @Test
    public void test_get_all_companions_for_crop() throws Exception
    {
        mockMvc.perform(get("/companions/1").header("Authorization", "Bearer " + adminToken))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":5,\"impacting\":{\"id\":3,\"name\":\"Cabbage\",\"description\":null,\"diameter\":1,\"imageUrl\":null,\"type\":\"LEAF\",\"userId\":\"admin\"},\"impacted\":{\"id\":1,\"name\":\"Tomato\",\"description\":null,\"diameter\":2,\"imageUrl\":null,\"type\":\"FRUIT\",\"userId\":\"admin\"},\"positive\":true}]"));
    }

    @Test
    public void test_create_new_companion() throws Exception
    {
        Companion newCompanion = new Companion();
        newCompanion.setPositive(true);
        newCompanion.setImpacting(crop2);
        newCompanion.setImpacted(crop3);

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(newCompanion);

        when(companionRepositoryMock.save(newCompanion)).thenReturn(newCompanion);

        mockMvc.perform(post("/companions").header("Authorization", "Bearer " + adminToken).header("Content-Type", "application/json")
                .content(body))
                .andDo(print()).
                andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(newCompanion)));
    }

    @Test
    public void test_delete_companion() throws Exception
    {
        mockMvc.perform(delete("/companions/4").header("Authorization", "Bearer " + demoToken)).andExpect(status().isNotFound());
        mockMvc.perform(delete("/companions/4").header("Authorization", "Bearer " + adminToken)).andExpect(status().isNoContent());
    }

}
