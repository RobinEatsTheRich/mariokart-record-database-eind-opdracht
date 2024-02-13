package Robin.group.MariokartBackend;

import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
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
        //Arrange
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
        assertEquals("Bonobo", result.get(0));
        assertEquals("-<xX_KingKoopa_Xx>-", result.get(1));
        assertEquals("Miyamoto_Shigeru", result.get(2));
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

    @Test
    void testEditUserAllowed(){
        //Arrange
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        UserInputDto userInputDto = new UserInputDto("Bonobo","4ctu4llyIpr3f3rCh3rri3sN0w","newTreeByWater@hotmail.com");
        userInputDto.setRoles(userRoles);
        setUserRole(bonobo,"USER");
        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(userRepos.findById("Bonobo"))
                .thenReturn(Optional.ofNullable(bonobo));
        Mockito
                .when(passwordEncoder.encode("4ctu4llyIpr3f3rCh3rri3sN0w"))
                .thenReturn("[ENCODED]4ctu4llyIpr3f3rCh3rri3sN0w[ENCODED]");

        //Act
        String result = userService.editUser(myUserDetails,"Bonobo", userInputDto).getPassword();

        //Assert
        assertEquals("[ENCODED]4ctu4llyIpr3f3rCh3rri3sN0w[ENCODED]", result);
    }

    @Test
    void testEditUserNotPossible(){
        //Arrange
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        UserInputDto userInputDto = new UserInputDto("-<X_KingKopa_Xx>-","P3achL0ver64","kingK@shellspin.com");
        userInputDto.setRoles(userRoles);
        setUserRole(bowser,"USER");
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(userRepos.findById("-<xX_KingKoopa_Xx>-"))
                .thenReturn(Optional.ofNullable(bowser));
        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> userService.editUser(myUserDetails,"-<xX_KingKoopa_Xx>-",userInputDto));

        //Assert
        assertEquals("Username cannot be changed, this is your ID", recordNotFoundException.getMessage());
    }

    @Test
    void testEditUserForbidden(){
        //Arrange
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        UserInputDto userInputDto = new UserInputDto("stinker","BadDad#2","Shigeru_Miyamoto@smellmyfarts.com");
        userInputDto.setRoles(userRoles);
        setUserRole(bowser,"USER");
        MyUserDetails myUserDetails = new MyUserDetails(bowser);

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> userService.editUser(myUserDetails,"Miyamoto_Shigeru",userInputDto));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
    }

//    @Test
//    void testDeleteUserAllowed(){
//        //Arrange
//        setUserRole(bonobo,"USER");
//        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
//        Profile profile = new Profile("Bonobo", bonobo);
//        Mockito
//                .when(userRepos.findById("Bonobo"))
//                .thenReturn(Optional.ofNullable(bonobo));
//        Mockito
//                .when(profileService.profileFromName("Bonobo"))
//                .thenReturn(profile);
//
//        //Act
//        userService.deleteUser(myUserDetails,"Bonobo");
//
//        //Assert
//        Mockito.verify(userRepos, Mockito.times(3)).save(Mockito.any());
////        Mockito.verify(userService, Mockito.times(1).{userService.deleteUser()(Mockito.any())});
//    }

    @Test
    void testDeleteUserForbidden(){
        //Arrange
        setUserRole(bowser,"USER");
        MyUserDetails myUserDetails = new MyUserDetails(bowser);

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> userService.deleteUser(myUserDetails,"Miyamoto_Shigeru"));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
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
        String result = userService.userFromName("Bonobo").getPassword();

        //Assert
        assertEquals("P3achL0ver", result);
    }

    @Test
    void testUserFromNameForbidden(){
        //Arrange
        setUserRole(bowser,"USER");
        Mockito
                .when(userRepos.findById("-<X_KingKopa_Xx>-"))
                .thenReturn(Optional.empty());

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> userService.userFromName("-<X_KingKopa_Xx>-"));

        //Assert
        assertEquals("User -<X_KingKopa_Xx>- could not be found in the database", recordNotFoundException.getMessage());
    }

    @Test
    void testDtoFromUser(){
        //Arrange
        setUserRole(bonobo,"USER");
        Profile profile = new Profile("Bonobo",bonobo);
        bonobo.setProfile(profile);

        //Act
        String result = userService.dtoFromUser(bonobo).getPassword();

        //Assert
        assertEquals("P3achL0ver", result);
    }

    @Test
    void testUserFromDto(){
        //Arrange
        UserInputDto userInputDto = new UserInputDto("Bonobo","P3achL0ver","oldTreeByWater@hotmail.com");
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        userInputDto.setRoles(userRoles);
        Mockito
                .when(passwordEncoder.encode("P3achL0ver"))
                .thenReturn("[ENCODED]P3achL0ver[ENCODED]");

        //Act
        String result = userService.userFromDto(userInputDto).getPassword();

        //Assert
        assertEquals("[ENCODED]P3achL0ver[ENCODED]", result);
    }


}
