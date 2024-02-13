package Robin.group.MariokartBackend;

import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.inputDtos.UserInputDto;
import Robin.MariokartBackend.model.Profile;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.UserRepository;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.ProfileService;
import Robin.MariokartBackend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class userServiceTest {
    @Mock
    private UserRepository userRepos;
    @Mock
    private ProfileService profileService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    User bonobo = new User("Bonobo","P3achL0ver","oldTreeByWater@hotmail.com");

    User bowser = new User("-<xX_KingKoopa_Xx>-","P3achL0ver64","kingK@shellspin.com");

    User miyamoto = new User("Miyamoto_Shigeru","marioGuy#1","Shigeru_Miyamoto@Nintendo.com");

    public void setUserRole(User user, String userRole){
        List<UserRole> noAdmin = new ArrayList<>();
        noAdmin.add(UserRole.USER);
        List<UserRole> admin = new ArrayList<>();
        admin.add(UserRole.USER);
        admin.add(UserRole.ADMIN);
        switch (userRole){
            case "USER" -> user.setUserRoles(noAdmin);
            case "ADMIN" -> user.setUserRoles(admin);
        }
    }

    @Test
    void testGetAllUsers(){
//        Arrange
        setUserRole(bonobo,"USER");
        setUserRole(bowser,"USER");
        setUserRole(miyamoto,"USER");
        List<User> allUsers = new ArrayList<>();
        allUsers.add(bonobo);
        allUsers.add(bowser);
        allUsers.add(miyamoto);
        Mockito
                .when(userRepos.findAll())
                .thenReturn(allUsers);

        //Act
        List<String> result = userService.getAllUsers();

        //Assert
        assertEquals(bonobo.getUsername(), result.get(0));
        assertEquals(bowser.getUsername(), result.get(1));
        assertEquals(miyamoto.getUsername(), result.get(2));
    }

    @Test
    void testGetUserAllowed(){
        //Arrange
        setUserRole(bonobo,"USER");
        MyUserDetails bonoboUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(userRepos.findById(bonobo.getUsername()))
                .thenReturn(Optional.ofNullable(bonobo));

        //Act
        String result = userService.getUser(bonoboUserDetails,"Bonobo").getPassword();

        //Assert
        assertEquals("P3achL0ver", result);
    }

    @Test
    void testGetUserForbidden(){
        //Arrange
        setUserRole(bonobo,"USER");
        setUserRole(bowser,"USER");
        MyUserDetails bowserUserDetails = new MyUserDetails(bowser);

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> userService.getUser(bowserUserDetails,"Bonobo"));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Bonobo.", forbiddenException.getMessage());
    }

    @Test
    void testCreateUserAllowed(){
        //Arrange
        UserInputDto userInputDto = new UserInputDto("Bonobo","P3achL0ver","oldTreeByWater@hotmail.com");
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        userInputDto.setRoles(userRoles);
        Profile profile = new Profile("Bonobo", bonobo);
        Mockito
                .when(userRepos.findById("Bonobo"))
                .thenReturn(Optional.empty());
        Mockito
                .when(passwordEncoder.encode("P3achL0ver"))
                .thenReturn("[ENCODED]P3achL0ver[ENCODED]");

        Mockito
                .when(profileService.profileFromName("Bonobo"))
                .thenReturn(profile);

        //Act
        String result = userService.createUser(userInputDto).getPassword();

        //Assert
        assertEquals("[ENCODED]P3achL0ver[ENCODED]", result);
    }

    @Test
    void testCreateUserForbidden(){
        //Arrange
        UserInputDto userInputDto = new UserInputDto("-<xX_KingKoopa_Xx>-","P3achL0ver64","kingK@shellspin.com");
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        userInputDto.setRoles(userRoles);
        Mockito
                .when(userRepos.findById(userInputDto.getUsername()))
                .thenReturn(Optional.ofNullable(bowser));

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> userService.createUser(userInputDto));

        //Assert
        assertEquals("The username -<xX_KingKoopa_Xx>- is no longer available, pick a different username.", forbiddenException.getMessage());
    }


}
