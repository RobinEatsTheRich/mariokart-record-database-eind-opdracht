package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.CharacterDto;
import Robin.MariokartBackend.inputDtos.CharacterInputDto;
import Robin.MariokartBackend.model.Character;
import Robin.MariokartBackend.repository.CharacterRepository;
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
class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CharacterRepository characterRepository;

    Character luigi = new Character();
    Character peach = new Character();
    Character daisy = new Character();
    CharacterDto luigiDto = new CharacterDto();
    CharacterDto peachDto = new CharacterDto();
    CharacterDto daisyDto = new CharacterDto();
    @BeforeEach
    public void setUp() {
        luigi = new Character(1002l, "Luigi", "https://mario.wiki.gallery/images/thumb/5/51/MK8_Luigi_Icon.png/96px-MK8_Luigi_Icon.png");
        peach = new Character(1003l, "Peach", "https://mario.wiki.gallery/images/thumb/c/c2/MK8_Peach_Icon.png/96px-MK8_Peach_Icon.png");
        daisy = new Character(1004l, "Daisy", "https://mario.wiki.gallery/images/thumb/3/32/MK8_Daisy_Icon.png/96px-MK8_Daisy_Icon.png");
        luigiDto.setId(1002l);
        luigiDto.setName("Luigi");
        luigiDto.setImgLink("https://mario.wiki.gallery/images/thumb/5/51/MK8_Luigi_Icon.png/96px-MK8_Luigi_Icon.png");
        peachDto.setId(1003l);
        peachDto.setName("Peach");
        peachDto.setImgLink("https://mario.wiki.gallery/images/thumb/c/c2/MK8_Peach_Icon.png/96px-MK8_Peach_Icon.png");
        daisyDto.setId(1004l);
        daisyDto.setName("Daisy");
        daisyDto.setImgLink("https://mario.wiki.gallery/images/thumb/3/32/MK8_Daisy_Icon.png/96px-MK8_Daisy_Icon.png");
        characterRepository.save(luigi);
        characterRepository.save(peach);
        characterRepository.save(daisy);
    }

    @AfterEach
    void tearDown() {
        luigi = null;
        peach = null;
        daisy = null;
        luigiDto = null;
        peachDto = null;
        daisyDto = null;
    }

    @Test
    void getAllCharacters() throws Exception {

        MvcResult result = this.mockMvc
            .perform(get("/characters"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/characters"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1002l))
            .andExpect(jsonPath("$[0].name").value("Luigi"))
            .andExpect(jsonPath("$[0].imgLink").value("https://mario.wiki.gallery/images/thumb/5/51/MK8_Luigi_Icon.png/96px-MK8_Luigi_Icon.png"))
            .andExpect(jsonPath("$[1].id").value(1003l))
            .andExpect(jsonPath("$[1].name").value("Peach"))
            .andExpect(jsonPath("$[1].imgLink").value("https://mario.wiki.gallery/images/thumb/c/c2/MK8_Peach_Icon.png/96px-MK8_Peach_Icon.png"))
            .andExpect(jsonPath("$[2].id").value(1004l))
            .andExpect(jsonPath("$[2].name").value("Daisy"))
            .andExpect(jsonPath("$[2].imgLink").value("https://mario.wiki.gallery/images/thumb/3/32/MK8_Daisy_Icon.png/96px-MK8_Daisy_Icon.png"));
    }

    @Test
    void getCharacter() throws Exception {

        mockMvc.perform(get("/characters/1002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1002l))
                .andExpect(jsonPath("name").value("Luigi"))
                .andExpect(jsonPath("imgLink").value("https://mario.wiki.gallery/images/thumb/5/51/MK8_Luigi_Icon.png/96px-MK8_Luigi_Icon.png"));
    }

    @Test
    void addCharacter() throws Exception  {
        CharacterInputDto luigiInputDto = new CharacterInputDto(1001l, "Mario", "https://mario.wiki.gallery/images/thumb/5/51/MK8_Mario_Icon.png/96px-MK8_Mario_Icon.png");;
        mockMvc.perform(post("/characters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(luigiInputDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(1001l))
            .andExpect(jsonPath("name").value("Mario"))
            .andExpect(jsonPath("imgLink").value("https://mario.wiki.gallery/images/thumb/5/51/MK8_Mario_Icon.png/96px-MK8_Mario_Icon.png"));

    }

    @Test
    void updateCharacter() throws Exception  {
        CharacterInputDto luigiInputDto = new CharacterInputDto(1002l, "Muigi", "https://mario.wiki.gallery/images/thumb/5/51/MK8_Mario_Icon.png/96px-MK8_Mario_Icon.png");;
        mockMvc.perform(put("/characters/1002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(luigiInputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1002l))
                .andExpect(jsonPath("name").value("Muigi"))
                .andExpect(jsonPath("imgLink").value("https://mario.wiki.gallery/images/thumb/5/51/MK8_Mario_Icon.png/96px-MK8_Mario_Icon.png"));
    }

    @Test
    void deleteCharacter() throws Exception  {
        mockMvc.perform(delete("/characters/1002"))
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