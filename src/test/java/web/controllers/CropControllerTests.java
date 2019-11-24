package web.controllers;

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
import util.Helper;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private static String token;

    @BeforeClass
    public static void setupToken()
    {
        token = Helper.getAccessToken("demo", "demo", "client", "secret");
        CropRepository cropRepositoryMock = mock(CropRepository.class);
    }

    @Before
    public void before()
    {
        Crop crop = new Crop();
        crop.setUserId("demo");
        crop.setType(CropType.FRUIT);
        crop.setName("Tomato");
        crop.setDiameter(1);
        crop.setId(1);

        when(cropRepositoryMock.findById(1)).thenReturn(Optional.of(crop));
    }

    @Test
    public void test_method() throws Exception
    {
        mockMvc.perform(get("/crop/1").header("Authorization", "Bearer " + token)).andDo(print()).andExpect(status().isOk());
    }
}
