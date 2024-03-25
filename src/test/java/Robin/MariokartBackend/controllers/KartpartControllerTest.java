package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.enumerations.PartType;
import Robin.MariokartBackend.inputDtos.KartPartInputDto;
import Robin.MariokartBackend.model.KartPart;
import Robin.MariokartBackend.repository.KartPartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class KartPartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KartPartRepository kartPartRepository;

    KartPart standardBody = new KartPart();
    KartPart standardWheels = new KartPart();
    KartPart superGlider = new KartPart();
    KartPartDto standardBodyDto = new KartPartDto();
    KartPartDto standardWheelsDto = new KartPartDto();
    KartPartDto superGliderDto = new KartPartDto();
    @BeforeEach
    public void setUp() {
        standardBody = new KartPart(1001l, "Standard Kart",null, "https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png", PartType.BODY);
        standardWheels = new KartPart(2001l, "Standard",null, "https://mario.wiki.gallery/images/thumb/a/a8/StandardTiresMK8.png/180px-StandardTiresMK8.png", PartType.WHEELS);
        superGlider = new KartPart(3001l, "Super Glider",null, "https://mario.wiki.gallery/images/thumb/a/a8/SuperGliderMK8.png/180px-SuperGliderMK8.png", PartType.GLIDER);
        standardBodyDto.setId(1001l);
        standardBodyDto.setName("Standard Kart");
        standardBodyDto.setImgLink("https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png");
        standardBodyDto.setPartType(PartType.BODY);
        standardWheelsDto.setId(2001l);
        standardWheelsDto.setName("Standard");
        standardWheelsDto.setImgLink("https://mario.wiki.gallery/images/thumb/a/a8/StandardTiresMK8.png/180px-StandardTiresMK8.png");
        standardBodyDto.setPartType(PartType.WHEELS);
        superGliderDto.setId(3001l);
        superGliderDto.setName("Super Glider");
        superGliderDto.setImgLink("https://mario.wiki.gallery/images/thumb/a/a8/SuperGliderMK8.png/180px-SuperGliderMK8.png");
        standardBodyDto.setPartType(PartType.GLIDER);
        kartPartRepository.save(standardBody);
        kartPartRepository.save(standardWheels);
        kartPartRepository.save(superGlider);
    }

    @AfterEach
    void tearDown() {
        standardBody = null;
        standardWheels = null;
        superGlider = null;
        standardBodyDto = null;
        standardWheelsDto = null;
        superGliderDto = null;
    }

    @Test
    void getAllKartParts() throws Exception {

        MvcResult result = this.mockMvc
            .perform(get("/kartParts"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/kartParts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1001))
            .andExpect(jsonPath("$[0].name").value("Standard Kart"))
            .andExpect(jsonPath("$[0].imgLink").value("https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png"))
            .andExpect(jsonPath("$[0].partType").value("BODY"))
            .andExpect(jsonPath("$[2].id").value(2001l))    //The ID here is 2 because for some reason it throws the golden kart in the mix
            .andExpect(jsonPath("$[2].name").value("Standard"))
            .andExpect(jsonPath("$[2].imgLink").value("https://mario.wiki.gallery/images/thumb/a/a8/StandardTiresMK8.png/180px-StandardTiresMK8.png"))
            .andExpect(jsonPath("$[2].partType").value("WHEELS"))
            .andExpect(jsonPath("$[3].id").value(3001l))
            .andExpect(jsonPath("$[3].name").value("Super Glider"))
            .andExpect(jsonPath("$[3].imgLink").value("https://mario.wiki.gallery/images/thumb/a/a8/SuperGliderMK8.png/180px-SuperGliderMK8.png"))
            .andExpect(jsonPath("$[3].partType").value("GLIDER"));
    }

    @Test
    void getKartPart() throws Exception {

        mockMvc.perform(get("/kartParts/1001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(1001))
            .andExpect(jsonPath("name").value("Standard Kart"))
            .andExpect(jsonPath("imgLink").value("https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png"))
            .andExpect(jsonPath("partType").value("BODY"));
    }

    @Test
    void addKartPart() throws Exception  {
        KartPartInputDto goldInputDto = new KartPartInputDto(1014l, "Gold Standard", "Gold Kart", "https://mario.wiki.gallery/images/thumb/f/fe/Gold_Standard.png/180px-Gold_Standard.png","BODY");
        mockMvc.perform(post("/kartParts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(goldInputDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(1014l))
            .andExpect(jsonPath("name").value("Gold Standard"))
            .andExpect(jsonPath("imgLink").value("https://mario.wiki.gallery/images/thumb/f/fe/Gold_Standard.png/180px-Gold_Standard.png"))
            .andExpect(jsonPath("alternativeName").value("Gold Kart"))
            .andExpect(jsonPath("partType").value("BODY"));

    }

    @Test
    void updateKartPart() throws Exception  {
        KartPartInputDto luigiInputDto = new KartPartInputDto(1001l, "Bandard Kart", "https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png","BODY");
        mockMvc.perform(put("/kartParts/1001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(luigiInputDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(1001l))
            .andExpect(jsonPath("name").value("Bandard Kart"))
            .andExpect(jsonPath("imgLink").value("https://mario.wiki.gallery/images/thumb/0/05/StandardKartBodyMK8.png/180px-StandardKartBodyMK8.png"))
            .andExpect(jsonPath("partType").value("BODY"));
    }

    @Test
    void deleteKartPart() throws Exception  {
        mockMvc.perform(delete("/kartParts/1001"))
            .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}