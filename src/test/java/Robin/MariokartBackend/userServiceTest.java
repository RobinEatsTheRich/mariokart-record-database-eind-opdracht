package Robin.MariokartBackend;

import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.BadRequestException;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.inputDtos.UserInputDto;
import Robin.MariokartBackend.model.Profile;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.UserRepository;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.ProfileService;
import Robin.MariokartBackend.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

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
    User bonobo = new User();
    User bowser = new User();
    User miyamoto = new User();

    @BeforeEach
    public void setUp(){
        bonobo.setUsername("Bonobo");
        bonobo.setPassword("P3achL0ver");
        bonobo.setEmail("oldTreeByWater@hotmail.com");
        setUserRole(bonobo,"USER");

        bowser.setUsername("-<xX_KingKoopa_Xx>-");
        bowser.setPassword("P3achL0ver64");
        bowser.setEmail("kingK@shellspin.com");
        setUserRole(bowser,"USER");

        miyamoto.setUsername("Miyamoto_Shigeru");
        miyamoto.setPassword("marioGuy#1");
        miyamoto.setEmail("Shigeru_Miyamoto@Nintendo.com");
        setUserRole(miyamoto,"ADMIN");
    }

    @AfterEach
    public void tearDown(){
    bonobo = null;
    bowser = null;
    miyamoto = null;
    }

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
        //Arrange
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
        assertEquals("Bonobo", result.get(0));
        assertEquals("-<xX_KingKoopa_Xx>-", result.get(1));
        assertEquals("Miyamoto_Shigeru", result.get(2));
    }

    @Test
    void testGetUserAllowed(){
        //Arrange
        MyUserDetails bonoboUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(userRepos.findById(bonobo.getUsername()))
                .thenReturn(Optional.ofNullable(bonobo));

        //Act
        String result = userService.getUser(bonoboUserDetails,"Bonobo").getEmail();

        //Assert
        assertEquals("oldTreeByWater@hotmail.com", result);
    }

    @Test
    void testGetUserForbidden(){
        //Arrange
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
                .when(profileService.profileFromName("Bonobo"))
                .thenReturn(profile);

        //Act
        String result = userService.createUser(userInputDto).getEmail();

        //Assert
        assertEquals("oldTreeByWater@hotmail.com", result);
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

    @Test
    void testEditUserAllowed(){
        //Arrange
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        UserInputDto userInputDto = new UserInputDto("Bonobo","4ctu4llyIpr3f3rCh3rri3sN0w","newTreeByWater@hotmail.com");
        userInputDto.setRoles(userRoles);
        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(userRepos.findById("Bonobo"))
                .thenReturn(Optional.ofNullable(bonobo));
        //Act
        String result = userService.editUser(myUserDetails,"Bonobo", userInputDto).getEmail();

        //Assert
        assertEquals("newTreeByWater@hotmail.com", result);
    }

    @Test
    void testEditUserNotPossible(){
        //Arrange
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        UserInputDto userInputDto = new UserInputDto("-<X_KingKopa_Xx>-","P3achL0ver64","kingK@shellspin.com");
        userInputDto.setRoles(userRoles);
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(userRepos.findById("-<xX_KingKoopa_Xx>-"))
                .thenReturn(Optional.ofNullable(bowser));
        //Act
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> userService.editUser(myUserDetails,"-<xX_KingKoopa_Xx>-",userInputDto));

        //Assert
        assertEquals("Username cannot be changed, this is your ID.", badRequestException.getMessage());
    }

    @Test
    void testEditUserForbidden(){
        //Arrange
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        UserInputDto userInputDto = new UserInputDto("stinker","BadDad#2","Shigeru_Miyamoto@smellmyfarts.com");
        userInputDto.setRoles(userRoles);
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(userRepos.findById("Miyamoto_Shigeru"))
                .thenReturn(Optional.ofNullable(miyamoto));
        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> userService.editUser(myUserDetails,"Miyamoto_Shigeru",userInputDto));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
    }

    @Test
    void testDeleteUserAllowed(){
        //Arrange
        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(userRepos.findById("Bonobo"))
                .thenReturn(Optional.ofNullable(bonobo));

        //Act
        userService.deleteUser(myUserDetails,"Bonobo");

        //Assert

        verify(userRepos, Mockito.times(1)).deleteById("Bonobo");
    }

    @Test
    void testDeleteUserForbidden(){
        //Arrange
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(userRepos.findById("Miyamoto_Shigeru"))
                .thenReturn(Optional.ofNullable(miyamoto));

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> userService.deleteUser(myUserDetails,"Miyamoto_Shigeru"));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
    }

    @Test
    void testDeleteUserNotPossible(){
        //Arrange
        MyUserDetails myUserDetails = new MyUserDetails(bowser);

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> userService.deleteUser(myUserDetails,"iyamoto_Shigeru"));

        //Assert
        assertEquals("User iyamoto_Shigeru could not be found in the database.", recordNotFoundException.getMessage());
    }

    @Test
    void testUserRoleFromName(){
        //Arrange
        List<String> stringList = new ArrayList<>();
        stringList.add("USER");
        stringList.add("ADMIN");
        List<UserRole> userRoleList = new ArrayList<>();
        userRoleList.add(UserRole.USER);
        userRoleList.add(UserRole.ADMIN);

        //Act
        List<UserRole> result = userService.userRoleFromName(stringList);

        //Assert
        assertEquals(userRoleList, result);
    }

    @Test
    void testUserFromNameAllowed(){
        //Arrange
        Mockito
                .when(userRepos.findById("Bonobo"))
                .thenReturn(Optional.ofNullable(bonobo));

        //Act
        String result = userService.userFromName("Bonobo").getEmail();

        //Assert
        assertEquals("oldTreeByWater@hotmail.com", result);
    }

    @Test
    void testUserFromNameForbidden(){
        //Arrange
        Mockito
                .when(userRepos.findById("-<X_KingKopa_Xx>-"))
                .thenReturn(Optional.empty());

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> userService.userFromName("-<X_KingKopa_Xx>-"));

        //Assert
        assertEquals("User -<X_KingKopa_Xx>- could not be found in the database.", recordNotFoundException.getMessage());
    }

    @Test
    void testDtoFromUser(){
        //Arrange
        Profile profile = new Profile("Bonobo",bonobo);
        bonobo.setProfile(profile);

        //Act
        String result = userService.dtoFromUser(bonobo).getEmail();

        //Assert
        assertEquals("oldTreeByWater@hotmail.com", result);
    }

    @Test
    void testUserFromDto(){
        //Arrange
        UserInputDto userInputDto = new UserInputDto("Bonobo","P3achL0ver","oldTreeByWater@hotmail.com");
        List<String> userRoles = new ArrayList<>();
        userInputDto.setRoles(userRoles);

        //Act
        String result = userService.userFromDto(userInputDto).getEmail();

        //Assert
        assertEquals("oldTreeByWater@hotmail.com", result);
    }


}
