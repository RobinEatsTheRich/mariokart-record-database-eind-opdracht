package Robin.MariokartBackend;

import Robin.MariokartBackend.dtos.*;
import Robin.MariokartBackend.enumerations.PartType;
import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.BadRequestException;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.inputDtos.RecordInputDto;
import Robin.MariokartBackend.model.*;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.repository.*;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

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

    User bonobo = new User();
    User bowser = new User();
    User miyamoto = new User();
    Record bonobosRecord = new Record();
    Record bowsersRecord = new Record();
    Record miyamotosRecord = new Record();
    CharacterDto marioDto = new CharacterDto();
    KartPartDto standardKartDto = new KartPartDto();
    CourseDtoForRecord rainbowRoadDto = new CourseDtoForRecord();
    Course rainbowRoad = new Course();

    CourseDtoForRecord babyParkDto = new CourseDtoForRecord();
    Course babyPark = new Course();


    @BeforeEach
    public void setUp(){


        List<UserRole> noAdmin = new ArrayList<>();
        noAdmin.add(UserRole.USER);
        List<UserRole> admin = new ArrayList<>();
        admin.add(UserRole.USER);
        admin.add(UserRole.ADMIN);
        bonobo.setUsername("Bonobo");
        bonobo.setPassword("P3achL0ver");
        bonobo.setEmail("oldTreeByWater@hotmail.com");
        bonobo.setUserRoles(noAdmin);

        bowser.setUsername("-<xX_KingKoopa_Xx>-");
        bowser.setPassword("P3achL0ver64");
        bowser.setEmail("kingK@shellspin.com");
        bowser.setUserRoles(noAdmin);

        miyamoto.setUsername("Miyamoto_Shigeru");
        miyamoto.setPassword("marioGuy#1");
        miyamoto.setEmail("Shigeru_Miyamoto@Nintendo.com");
        miyamoto.setUserRoles(admin);

        bonobosRecord.setId(1l);
        bonobosRecord.setTotalTime(1.35118f);
        bonobosRecord.setLap1(0.33148f);
        bonobosRecord.setLap2(0.31074f);
        bonobosRecord.setLap3(0.30896f);
        bonobosRecord.setIs200CC(false);
        bonobosRecord.setCharacterId(1001l);
        bonobosRecord.setBodyId(1001l);
        bonobosRecord.setWheelsId(2001l);
        bonobosRecord.setGliderId(3001l);

        bowsersRecord.setId(2l);
        bowsersRecord.setTotalTime(1.55118f);
        bowsersRecord.setLap1(0.33148f);
        bowsersRecord.setLap2(0.41074f);
        bowsersRecord.setLap3(0.40896f);
        bowsersRecord.setIs200CC(false);
        bowsersRecord.setCharacterId(1001l);
        bowsersRecord.setBodyId(1001l);
        bowsersRecord.setWheelsId(2001l);
        bowsersRecord.setGliderId(3001l);

        miyamotosRecord.setId(3l);
        miyamotosRecord.setTotalTime(0.44748f);
        miyamotosRecord.setLap1(0.07730f);
        miyamotosRecord.setLap2(0.06273f);
        miyamotosRecord.setLap3(0.06168f);
        miyamotosRecord.setLap4(0.06132f);
        miyamotosRecord.setLap5(0.06133f);
        miyamotosRecord.setLap6(0.06154f);
        miyamotosRecord.setLap7(0.06158f);
        miyamotosRecord.setIs200CC(false);
        miyamotosRecord.setCharacterId(1001l);
        miyamotosRecord.setBodyId(1001l);
        miyamotosRecord.setWheelsId(2001l);
        miyamotosRecord.setGliderId(3001l);

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
        rainbowRoad.setId(1097L);
        rainbowRoad.setName("Wii Rainbow Road");
        rainbowRoad.setImgLink("https://mario.wiki.gallery/images/thumb/0/0c/MK8D_Wii_Rainbow_Road_Course_Icon.png/228px-MK8D_Wii_Rainbow_Road_Course_Icon.png");

        babyParkDto.setId(1021L);
        babyParkDto.setName("Baby Park");
        babyParkDto.setImgLink("https://mario.wiki.gallery/images/thumb/3/34/MK8_GCN_Baby_Park_Course_Icon.png/228px-MK8_GCN_Baby_Park_Course_Icon.png");
        babyPark.setId(1021L);
        babyPark.setName("Baby Park");
        babyPark.setImgLink("https://mario.wiki.gallery/images/thumb/3/34/MK8_GCN_Baby_Park_Course_Icon.png/228px-MK8_GCN_Baby_Park_Course_Icon.png");

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
    @AfterEach
    public void tearDown(){
        bonobo = null;
        bowser = null;
        miyamoto = null;
        bonobosRecord = null;
        bowsersRecord = null;
        miyamotosRecord = null;
        marioDto = null;
        standardKartDto = null;
        rainbowRoadDto = null;
        rainbowRoad = null;

    }

    @Test
    void testGetAllRecords(){
        //Arrange
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
                .when(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(1001L)))
                .thenReturn(standardKartDto);

        //Act
        List<RecordDto> result = recordService.getAllRecords();

        //Assert
        assertEquals("1:35.118", result.get(0).getTotalTime());
        assertEquals("1:55.118", result.get(1).getTotalTime());
        assertEquals("0:44.748", result.get(2).getTotalTime());
    }

    @Test
    void testGetAllRivalRecords(){
        //Arrange
        List<Record> allRecords = new ArrayList<>();
        allRecords.add(bonobosRecord);
        allRecords.add(bowsersRecord);
        allRecords.add(miyamotosRecord);
        Profile bonoboProfile = new Profile("Bonobo",bonobo);
        bonobo.setProfile(bonoboProfile);
        Profile bowserProfile = new Profile("-<xX_KingKoopa_Xx>-",bowser);
        bowser.setProfile(bowserProfile);
        Profile miyamotoProfile = new Profile("Miyamoto_Shigeru",miyamoto);
        miyamoto.setProfile(miyamotoProfile);
        bonobosRecord.setProfile(bonoboProfile);
        bowsersRecord.setProfile(bowserProfile);
        miyamotosRecord.setProfile(miyamotoProfile);
        List<Profile> rivals = new ArrayList<>();
        rivals.add(bowserProfile);
        rivals.add(miyamotoProfile);
        bonoboProfile.setRivals(rivals);

        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(recordRepos.findAll())
                .thenReturn(allRecords);
        Mockito
                .when(characterService.dtoFromCharacter(characterService.characterFromId(1001L)))
                .thenReturn(marioDto);
        Mockito
                .when(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(1001L)))
                .thenReturn(standardKartDto);

        //Act
        List<RecordDto> result = recordService.getAllRivalRecords(myUserDetails);

        //Assert
        assertEquals("1:55.118", result.get(0).getTotalTime());
        assertEquals("0:44.748", result.get(1).getTotalTime());
    }

    @Test
    void testGetRecord(){
        //Arrange
        Mockito
                .when(recordRepos.findById(1L))
                .thenReturn(Optional.ofNullable(bonobosRecord));
        Mockito
                .when(characterService.dtoFromCharacter(characterService.characterFromId(1001L)))
                .thenReturn(marioDto);
        Mockito
                .when(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(1001L)))
                .thenReturn(standardKartDto);

        //Act
        String result = recordService.getRecord(1L).getTotalTime();

        //Assert
        assertEquals("1:35.118", result);
    }


    @Test
    void testCreateRecord(){
        //Arrange
        RecordInputDto dto = new RecordInputDto(1.35118f,0.33148f,0.31074f,0.30896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");

        Mockito
                .when(characterService.characterIdFromName("Mario"))
                .thenReturn(1001L);
        Mockito
                .when(courseService.courseIdFromName("Wii Rainbow Road"))
                .thenReturn(1096L);
        Mockito
                .when(kartpartService.kartPartIdFromName("Standard Kart"))
                .thenReturn(1001L);
        Mockito
                .when(courseRepos.getReferenceById(courseService.courseIdFromName(dto.getCourseName())))
                .thenReturn(rainbowRoad);
        //Act
        String result = recordService.createRecord(dto).getTotalTime();
        //Assert
        assertEquals("1:35.118", result);
    }

    @Test
    void testEditRecordAllowed(){
        //Arrange
        RecordInputDto dto = new RecordInputDto(1.15118f,0.33148f,0.31074f,0.10896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");
        MyUserDetails myUserDetails = new MyUserDetails(bonobo);

        Mockito
                .when(recordRepos.findById(1L))
                .thenReturn(Optional.ofNullable(bonobosRecord));
        Mockito
                .when(courseService.courseIdFromName("Wii Rainbow Road"))
                .thenReturn(1096L);
        Mockito
                .when(courseRepos.getReferenceById(courseService.courseIdFromName(dto.getCourseName())))
                .thenReturn(rainbowRoad);

        //Act
        String result = recordService.editRecord(myUserDetails,1l,dto).getTotalTime();

        //Assert
        assertEquals("1:15.118", result);
    }

    @Test
    void testEditRecordNotPossible(){
        //Arrange
        RecordInputDto inputDto = new RecordInputDto(1.15118f,0.33148f,0.31074f,0.10896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(recordRepos.findById(5L))
                .thenReturn(Optional.empty());

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> recordService.editRecord(myUserDetails,5l,inputDto));

        //Assert
        assertEquals("The record corresponding to ID:5 could not be found in the database.", recordNotFoundException.getMessage());
    }

    @Test
    void testEditRecordForbidden(){
        //Arrange
        RecordInputDto inputDto = new RecordInputDto(4.35118f,1.33148f,1.31074f,1.30896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(recordRepos.findById(3L))
                .thenReturn(Optional.ofNullable(miyamotosRecord));

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> recordService.editRecord(myUserDetails,3l,inputDto));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
    }

    @Test
    void testDeleteRecordAllowed(){
        //Arrange
        MyUserDetails myUserDetails = new MyUserDetails(bonobo);
        Mockito
                .when(recordRepos.findById(1l))
                .thenReturn(Optional.ofNullable(bonobosRecord));

        //Act
        recordService.deleteRecord(myUserDetails,1l);

        //Assert
        verify(recordRepos, Mockito.times(1)).deleteById(1l);
    }

    @Test
    void testDeleteRecordForbidden(){
        //Arrange
        MyUserDetails myUserDetails = new MyUserDetails(bowser);
        Mockito
                .when(recordRepos.findById(3l))
                .thenReturn(Optional.ofNullable(miyamotosRecord));

        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> recordService.deleteRecord(myUserDetails,3l));

        //Assert
        assertEquals("You are logged in as -<xX_KingKoopa_Xx>-, not as Miyamoto_Shigeru.", forbiddenException.getMessage());
    }

    @Test
    void testStringFromTimeFloat(){
        //Act
        String result = recordService.stringFromTimeFloat(1.41408f);

        //Assert
        assertEquals("1:41.408", result);
    }
    @Test
    void testCheckTimeForValidTrue(){
        //Act
        boolean result = recordService.checkTimeForValid(1.41408f);

        //Assert
        assertEquals(true, result);
    }
    @Test
    void testCheckTimeForValidFalse(){
        //Act
        boolean result1 = recordService.checkTimeForValid(1.81408f);
        boolean result2 = recordService.checkTimeForValid(103.41408f);
        boolean result3 = recordService.checkTimeForValid(1.41406789765453428f);

        //Assert
        assertEquals(false, result1);
        assertEquals(false, result2);
        assertEquals(false, result3);
    }
    @Test
    void testDtoListFromRecordListAllowed(){
        //Arrange
        List<Record> recordList = new ArrayList<>();
        recordList.add(bonobosRecord);
        recordList.add(bowsersRecord);
        recordList.add(miyamotosRecord);


        //Act
        List<RecordDto> result = recordService.dtoListFromRecordList(recordList);

        //Assert
        assertEquals("1:35.118", result.get(0).getTotalTime());
        assertEquals("1:55.118", result.get(1).getTotalTime());
        assertEquals("0:44.748", result.get(2).getTotalTime());
    }

    @Test
    void testDtoListFromRecordListNotAllowed(){
        //Arrange
        List<Record> recordList = new ArrayList<>();

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> recordService.dtoListFromRecordList(recordList));

        //Assert
        assertEquals("The list of records to be converted was empty.", recordNotFoundException.getMessage());
    }

    @Test
    void testDtoForCoursesListFromRecordList(){
        //Arrange
        List<Record> recordList = new ArrayList<>();
        recordList.add(bonobosRecord);
        recordList.add(bowsersRecord);
        recordList.add(miyamotosRecord);


        //Act
        List<RecordDtoForCourse> result = recordService.dtoForCoursesListFromRecordList(recordList);

        //Assert
        assertEquals("1:35.118", result.get(0).getTotalTime());
        assertEquals("1:55.118", result.get(1).getTotalTime());
        assertEquals("0:44.748", result.get(2).getTotalTime());
    }

    @Test
    void testDtoForCoursesListFromRecordListNotAllowed(){
        //Arrange
        List<Record> recordList = new ArrayList<>();

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> recordService.dtoForCoursesListFromRecordList(recordList));

        //Assert
        assertEquals("The list of records to be converted was empty.", recordNotFoundException.getMessage());
    }



    @Test
    void testRecordFromIdAllowed(){
        //Arrange
        Mockito
                .when(recordRepos.findById(1l))
                .thenReturn(Optional.ofNullable(bonobosRecord));

        //Act
        float result = recordService.recordFromId(1l).getTotalTime();

        //Assert
        assertEquals(1.35118f, result);
    }
    @Test
    void testRecordFromIdNotPossible(){
        //Arrange
        Mockito
                .when(recordRepos.findById(5l))
                .thenReturn(Optional.empty());

        //Act
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> recordService.recordFromId(5l));

        //Assert
        assertEquals("The record corresponding to ID:5 could not be found in the database.", recordNotFoundException.getMessage());
    }

    @Test
    void testDtoForCoursesFromRecordAllowed(){
        //Act
        String result = recordService.dtoForCoursesFromRecord(bonobosRecord).getTotalTime();

        //Assert
        assertEquals("1:35.118", result);
    }
    @Test
    void testDtoForCoursesFromRecord7Laps(){

        //Act
        String result = recordService.dtoForCoursesFromRecord(miyamotosRecord).getTotalTime();

        //Assert
        assertEquals("0:44.748", result);
    }
    @Test
    void testDtoForCoursesFromRecordNoProfile(){
        //Arrange
        bonobosRecord.setProfile(null);

        //Act
        String result = recordService.dtoForCoursesFromRecord(bonobosRecord).getTotalTime();

        //Assert
        assertEquals("1:35.118", result);
    }

    @Test
    void testDtoFromRecord(){
        //Act
        String result = recordService.dtoFromRecord(bonobosRecord).getTotalTime();

        //Assert
        assertEquals("1:35.118", result);
    }
    @Test
    void testDtoFromRecord7Laps(){
        //Act
        String result = recordService.dtoFromRecord(miyamotosRecord).getTotalTime();

        //Assert
        assertEquals("0:44.748", result);
    }
    @Test
    void testDtoFromRecordNoProfile(){
        //Arrange
        bonobosRecord.setProfile(null);

        //Act
        String result = recordService.dtoFromRecord(bonobosRecord).getTotalTime();

        //Assert
        assertEquals("1:35.118", result);
    }
    @Test
    void testDtoFromRecordNoCourse(){
        //Arrange
        bonobosRecord.setCourse(null);

        //Act
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> recordService.dtoFromRecord(bonobosRecord));

        //Assert
        assertEquals("A record needs a Course.", badRequestException.getMessage());
    }
    @Test
    void testRecordFromDto(){
        //Arange
        RecordInputDto inputDto = new RecordInputDto(1.35118f,0.33148f,0.31074f,0.30896f,false,"Wii Rainbow Road","Mario","Standard Kart","Standard Kart","Standard Kart");

        //Act
        float result = recordService.recordFromDto(inputDto).getTotalTime();

        //Assert
        assertEquals(1.35118f, result);
    }
    @Test
    void testRecordFromDto7Laps(){
        //Arrange
        RecordInputDto inputDto = new RecordInputDto(0.44748f,0.07730f,0.06273f,0.06168f,0.06132f,0.06133f,0.06154f,0.06158f,false,"Mario","Standard Kart","Standard Kart","Standard Kart");

        //Act
        float result = recordService.recordFromDto(inputDto).getTotalTime();

        //Assert
        assertEquals(0.44748f, result);
    }

    @Test
    void testRecordFromDtoForbiddenBecauseOfLaps(){
        //Arrange
        RecordInputDto inputDto = new RecordInputDto(1.35118f,0.33148f,0.31074f,0.30896f,false,"Baby Park","Mario","Standard Kart","Standard Kart","Standard Kart");


        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> recordService.recordFromDto(inputDto));

        //Assert
        assertEquals("The course 'Baby Park' has 7 Laps instead of 3, please adjust request appropriately.", forbiddenException.getMessage());
    }

    @Test
    void testRecordFromDtoForbiddenBecauseOfTimes(){
        //Arrange
        RecordInputDto inputDto = new RecordInputDto(1.35118f,93.33148f,0.310134674f,0.80896f,false,"Baby Park","Mario","Standard Kart","Standard Kart","Standard Kart");


        //Act
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> recordService.recordFromDto(inputDto));

        //Assert
        assertEquals("some of the total- or lap-times are not in proper format.", forbiddenException.getMessage());
    }

}

