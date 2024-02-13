package Robin.group.MariokartBackend;

import Robin.MariokartBackend.dtos.CharacterDto;
import Robin.MariokartBackend.dtos.CourseDtoForRecord;
import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.enumerations.PartType;
import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.inputDtos.RecordInputDto;
import Robin.MariokartBackend.inputDtos.UserInputDto;
import Robin.MariokartBackend.model.*;
import Robin.MariokartBackend.model.Character;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.repository.*;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class recordServiceTest {
    @Mock
    private RecordRepository recordRepos;
    @Mock
    private CharacterService characterService;
    @Mock
    private KartPartService kartpartService;
    @Mock
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepos;
    @InjectMocks
    private RecordService recordService;

    User bonobo = new User("Bonobo","P3achL0ver","oldTreeByWater@hotmail.com");
    User bowser = new User("-<xX_KingKoopa_Xx>-","P3achL0ver64","kingK@shellspin.com");
    User miyamoto = new User("Miyamoto_Shigeru","marioGuy#1","Shigeru_Miyamoto@Nintendo.com");
    Record bonobosRecord = new Record(1L, 1.35118f,0.33148f,0.31074f,0.30896f,false,1001L,1001L,2001L,3001L);
    Record bowsersRecord = new Record(2L,1.65118f,0.43148f,0.41074f,0.40896f,false,1001L,1001L,2001L,3001L);
    Record miyamotosRecord = new Record(3L,0.95118f,0.23148f,0.21074f,0.20896f,false,1001L,1001L,2001L,3001L);
    CharacterDto marioDto = new CharacterDto();
    Character mario = new Character(1001L,"Mario","https://mario.wiki.gallery/images/thumb/d/d9/MK8_Mario_Icon.png/96px-MK8_Mario_Icon.png");
    KartPartDto standardKartDto = new KartPartDto();
    KartPart standardKart = new KartPart(1001L,"Standard Kart","https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png",PartType.BODY);
    CourseDtoForRecord rainbowRoadDto = new CourseDtoForRecord();
    Course rainbowRoad = new Course(1096L, "Wii Rainbow Road", "https://mario.wiki.gallery/images/thumb/0/0c/MK8D_Wii_Rainbow_Road_Course_Icon.png/228px-MK8D_Wii_Rainbow_Road_Course_Icon.png");



    public void setValues(){
        List<UserRole> noAdmin = new ArrayList<>();
        noAdmin.add(UserRole.USER);
        List<UserRole> admin = new ArrayList<>();
        admin.add(UserRole.USER);
        admin.add(UserRole.ADMIN);
        bonobo.setUserRoles(noAdmin);
        bowser.setUserRoles(noAdmin);
        miyamoto.setUserRoles(admin);
        marioDto.setId(1001L);
        marioDto.setName("Mario");
        marioDto.setImgLink("https://mario.wiki.gallery/images/thumb/d/d9/MK8_Mario_Icon.png/96px-MK8_Mario_Icon.png");
        standardKartDto.setId(1001L);
        standardKartDto.setName("Standard Kart");
        standardKartDto.setImgLink("https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png");
        standardKartDto.setPartType(PartType.BODY);
        rainbowRoadDto.setId(1097L);
        rainbowRoadDto.setName("Wii Rainbow Road");
        rainbowRoadDto.setImgLink("https://mario.wiki.gallery/images/thumb/0/0c/MK8D_Wii_Rainbow_Road_Course_Icon.png/228px-MK8D_Wii_Rainbow_Road_Course_Icon.png");
        bonobosRecord.setCourse(rainbowRoad);
        bowsersRecord.setCourse(rainbowRoad);
        miyamotosRecord.setCourse(rainbowRoad);
        Profile bonobosProfile = new Profile("Bonobo", bonobo);
        bonobo.setProfile(bonobosProfile);
        bonobosRecord.setProfile(bonobosProfile);
        Profile bowsersProfile = new Profile("-<xX_KingKoopa_Xx>-", bowser);
        bowser.setProfile(bowsersProfile);
        bowsersRecord.setProfile(bowsersProfile);
        Profile miyamotosProfile = new Profile("Miyamoto_Shigeru", miyamoto);
        miyamoto.setProfile(miyamotosProfile);
        miyamotosRecord.setProfile(miyamotosProfile);
    }

    @Test
    void testGetAllRecords(){
        //Arrange
        setValues();
        List<Record> allRecords = new ArrayList<>();
        allRecords.add(bonobosRecord);
        allRecords.add(bowsersRecord);
        allRecords.add(miyamotosRecord);
        Mockito
                .when(recordRepos.findAll())
                .thenReturn(allRecords);
        Mockito
                .when(characterService.dtoFromCharacter(characterService.characterFromId(1001L)))
                .thenReturn(marioDto);
        Mockito
                .when(courseService.dtoForRecordFromCourse(courseService.courseFromId(1096L)))
                .thenReturn(rainbowRoadDto);
        Mockito
                .when(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(1001L)))
                .thenReturn(standardKartDto);

        //Act
        List<RecordDto> result = recordService.getAllRecords();

        //Assert
        assertEquals("1:35.118", result.get(0).getTotalTime());
        assertEquals("1:65.118", result.get(1).getTotalTime());
        assertEquals("0:95.118", result.get(2).getTotalTime());
    }

    @Test
    void testGetRecord(){
        //Arrange
        setValues();
        Mockito
                .when(recordRepos.findById(1L))
                .thenReturn(Optional.ofNullable(bonobosRecord));
        Mockito
                .when(characterService.dtoFromCharacter(characterService.characterFromId(1001L)))
                .thenReturn(marioDto);
        Mockito
                .when(courseService.dtoForRecordFromCourse(courseService.courseFromId(1096L)))
                .thenReturn(rainbowRoadDto);
        Mockito
                .when(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(1001L)))
                .thenReturn(standardKartDto);

        //Act
        String result = recordService.getRecord(1L).getTotalTime();

        //Assert
        assertEquals("1:35.118", result);
    }


    @Test
    void testCreateUser(){
        //Arrange
        setValues();
        RecordInputDto inputDto = new RecordInputDto(1.35118f,0.33148f,0.31074f,0.30896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");

        Mockito
                .when(characterService.characterIdFromName("Mario"))
                .thenReturn(1001L);
        Mockito
                .when(courseService.courseIdFromName("Wii Rainbow Road"))
                .thenReturn(1096L);
        Mockito
                .when(kartpartService.kartPartIdFromName("Standard Kart"))
                .thenReturn(1001L);
        //Act
        String result = recordService.createRecord(inputDto).getTotalTime();

        //Assert
        assertEquals("1:35.118", result);
    }

    @Test
    void testEditRecordAllowed(){
        //Arrange
        setValues();
        RecordInputDto inputDto = new RecordInputDto(1.15118f,0.33148f,0.31074f,0.20896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");
        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(recordRepos.findById(1L))
                .thenReturn(Optional.ofNullable(bonobosRecord));

        //Act
        String result = recordService.editRecord(myUserDetails,1l,inputDto).getTotalTime();

        //Assert
        assertEquals("1:15.118", result);
    }

    @Test
    void testEditRecordNotPossible(){
        //Arrange
        setValues();
        RecordInputDto inputDto = new RecordInputDto(1.15118f,0.33148f,0.31074f,0.20896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(recordRepos.findById(5L))
                .thenReturn(Optional.empty());

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> recordService.editRecord(myUserDetails,5l,inputDto));

        //Assert
        assertEquals("The record corresponding to ID:5 could not be found in the database", recordNotFoundException.getMessage());
    }

    @Test
    void testEditRecordForbidden(){
        //Arrange
        setValues();
        RecordInputDto inputDto = new RecordInputDto(4.35118f,1.53148f,1.51074f,1.50896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(recordRepos.findById(3L))
                .thenReturn(Optional.ofNullable(miyamotosRecord));

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> recordService.editRecord(myUserDetails,3l,inputDto));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
    }

////    @Test
////    void testDeleteUserAllowed(){
////        //Arrange
////        setUserRole(bonobo,"USER");
////        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
////        Profile profile = new Profile("Bonobo", bonobo);
////        Mockito
////                .when(userRepos.findById("Bonobo"))
////                .thenReturn(Optional.ofNullable(bonobo));
////        Mockito
////                .when(profileService.profileFromName("Bonobo"))
////                .thenReturn(profile);
////
////        //Act
////        userService.deleteUser(myUserDetails,"Bonobo");
////
////        //Assert
////        Mockito.verify(userRepos, Mockito.times(3)).save(Mockito.any());
//////        Mockito.verify(userService, Mockito.times(1).{userService.deleteUser()(Mockito.any())});
////    }
//
//    @Test
//    void testDeleteUserForbidden(){
//        //Arrange
//        setUserRole(bowser,"USER");
//        MyUserDetails myUserDetails = new MyUserDetails(bowser);
//        Mockito
//                .when(userRepos.findById("-<xX_KingKoopa_Xx>-"))
//                .thenReturn(Optional.ofNullable(bowser));
//
//        //Act
//        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> userService.deleteUser(myUserDetails,"Miyamoto_Shigeru"));
//
//        //Assert
//        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
//    }
//
//    @Test
//    void testUserRoleFromName(){
//        //Arrange
//        List<String> stringList = new ArrayList<>();
//        stringList.add("USER");
//        stringList.add("ADMIN");
//        List<UserRole> userRoleList = new ArrayList<>();
//        userRoleList.add(UserRole.USER);
//        userRoleList.add(UserRole.ADMIN);
//
//        //Act
//        List<UserRole> result = userService.userRoleFromName(stringList);
//
//        //Assert
//        assertEquals(userRoleList, result);
//    }
//
//    @Test
//    void testUserFromNameAllowed(){
//        //Arrange
//        Mockito
//                .when(userRepos.findById("Bonobo"))
//                .thenReturn(Optional.ofNullable(bonobo));
//
//        //Act
//        String result = userService.userFromName("Bonobo").getPassword();
//
//        //Assert
//        assertEquals("P3achL0ver", result);
//    }
//
//    @Test
//    void testUserFromNameForbidden(){
//        //Arrange
//        setUserRole(bowser,"USER");
//        Mockito
//                .when(userRepos.findById("-<X_KingKopa_Xx>-"))
//                .thenReturn(Optional.empty());
//
//        //Act
//        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> userService.userFromName("-<X_KingKopa_Xx>-"));
//
//        //Assert
//        assertEquals("User -<X_KingKopa_Xx>- could not be found in the database", recordNotFoundException.getMessage());
//    }
//
//    @Test
//    void testDtoFromUser(){
//        //Arrange
//        setUserRole(bonobo,"USER");
//        Profile profile = new Profile("Bonobo",bonobo);
//        bonobo.setProfile(profile);
//
//        //Act
//        String result = userService.dtoFromUser(bonobo).getPassword();
//
//        //Assert
//        assertEquals("P3achL0ver", result);
//    }
//
//    @Test
//    void testUserFromDto(){
//        //Arrange
//        UserInputDto userInputDto = new UserInputDto("Bonobo","P3achL0ver","oldTreeByWater@hotmail.com");
//        List<String> userRoles = new ArrayList<>();
//        userRoles.add("USER");
//        userInputDto.setRoles(userRoles);
//        Mockito
//                .when(passwordEncoder.encode("P3achL0ver"))
//                .thenReturn("[ENCODED]P3achL0ver[ENCODED]");
//
//        //Act
//        String result = userService.userFromDto(userInputDto).getPassword();
//
//        //Assert
//        assertEquals("[ENCODED]P3achL0ver[ENCODED]", result);
//    }


}

