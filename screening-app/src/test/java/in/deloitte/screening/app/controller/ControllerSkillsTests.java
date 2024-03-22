package in.deloitte.screening.app.controller;

import in.deloitte.screening.app.skills.controllers.SkillsController;
import in.deloitte.screening.app.skills.dto.AddSkillsRequest;
import in.deloitte.screening.app.skills.entities.Skills;
import in.deloitte.screening.app.skills.services.SkillsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ControllerSkillsTests {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private SkillsService service;
    @InjectMocks
    private SkillsController controller;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    @AfterEach
    public void tearDown() {
        mockMvc = null;
    }

    @Test
    @DisplayName("test for creating skills success")
    public void createSkillsSuccessTest() throws Exception {

        List<Skills> mockSkills = Arrays.asList(
                new Skills("Java"),
                new Skills("Python")
        );
        when(service.addSkills(any(AddSkillsRequest.class))).thenReturn(mockSkills);


        mockMvc.perform(post("/skills/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"skills\": [\"Java\", \"Python\"]}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].skillName").value("Java"))
                .andExpect(jsonPath("$[1].skillName").value("Python"));
    }

    @Test
    @DisplayName("test for getting skills success")
    public void getSkillsSuccessTest() throws Exception {
        List<String> mockSkills = Arrays.asList("Java", "Python", "SQL");
        when(service.getAllSkills()).thenReturn(mockSkills);


        mockMvc.perform(get("/skills/get/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("Java"))
                .andExpect(jsonPath("$[1]").value("Python"))
                .andExpect(jsonPath("$[2]").value("SQL"));
    }

    @Test
    @DisplayName("test for searching skills success")
    public void getSearchSkillSuccessTest() throws Exception {

        String skillToSearch = "Java";
        List<Skills> mockSkills = Arrays.asList(
                new Skills("Java"),
                new Skills("Java SE"),
                new Skills("Java EE")
        );
        when(service.findBySkillName(skillToSearch)).thenReturn(mockSkills);

        mockMvc.perform(get("/skills/get/search-skill")
                        .param("skill", skillToSearch)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // Verify the response status and content
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.list[0].skillName").value("Java"))
                .andExpect(jsonPath("$.list[1].skillName").value("Java SE"))
                .andExpect(jsonPath("$.list[2].skillName").value("Java EE"));
    }
}
