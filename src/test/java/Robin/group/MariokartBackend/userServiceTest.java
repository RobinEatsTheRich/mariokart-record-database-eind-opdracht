package Robin.group.MariokartBackend;

import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.UserRepository;
import Robin.MariokartBackend.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes={UserService.class})
public class userServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @MockBean
    private UserRepository userRepos;
    private UserService userService;

    @Mock
    User user;

    @Test
    void testGetAllUsers(){
        //Arrange

        //Act

        //Assert

    }


}
