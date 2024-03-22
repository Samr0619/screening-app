package in.deloitte.screening.app.service;

import in.deloitte.screening.app.skills.dto.AddSkillsRequest;
import in.deloitte.screening.app.skills.entities.Skills;
import in.deloitte.screening.app.skills.repositories.SkillsRepository;
import in.deloitte.screening.app.skills.services.SkillsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceSkillsTests {
    @Mock
    private SkillsRepository repository;
    @InjectMocks
    private SkillsServiceImpl service;


    @Test
    @DisplayName("test for add skills success")
    public void addSkillsSuccessTest() {

        List<String> skillNames = Arrays.asList("Java", "Python", "SQL");
        AddSkillsRequest addSkillsRequest = new AddSkillsRequest(skillNames);

        when(repository.saveAll(anyList())).thenReturn(createMockSkillsList(skillNames.size()));

        List<Skills> savedSkills = service.addSkills(addSkillsRequest);

        verify(repository, times(1)).saveAll(anyList());

        Assertions.assertNotNull(savedSkills);
        Assertions.assertFalse(savedSkills.isEmpty());
        Assertions.assertEquals(skillNames.size(), savedSkills.size());
    }

    private List<Skills> createMockSkillsList(int size) {
        List<Skills> mockSkillsList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mockSkillsList.add(new Skills("MockSkill" + i));
        }
        return mockSkillsList;
    }

    @Test
    @DisplayName("test for get all skills success")
    public void getAllSkillsSuccessTest() {
        List<Skills> mockSkills = Arrays.asList(
                new Skills("Java"),
                new Skills("Python"),
                new Skills("Java"),
                new Skills("SQL")
        );
        when(repository.findAll()).thenReturn(mockSkills);

        List<String> allSkills = service.getAllSkills();

        Assertions.assertEquals(Arrays.asList("Java", "Python", "SQL"), allSkills);
    }

    @Test
    @DisplayName("test for find by skill name success")
    public void findBySkillNameSuccessTest() {
        String skillNameToFind = "Java";
        List<Skills> mockSkills = Arrays.asList(
                new Skills("Java"),
                new Skills("Python"),
                new Skills("Java"),
                new Skills("SQL")
        );
        when(repository.findBySkillName(skillNameToFind)).thenReturn(mockSkills);

        List<Skills> foundSkills = service.findBySkillName(skillNameToFind);

        Assertions.assertEquals(mockSkills, foundSkills);
    }
}
