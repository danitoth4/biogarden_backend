package web.controllers;

import model.repositories.CropRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CropControllerTests
{
    @Before
    public void before()
    {
        CropRepository cropRepositoryMock = mock(CropRepository.class);
    }
}
