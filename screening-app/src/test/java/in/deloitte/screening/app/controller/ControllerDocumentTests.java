package in.deloitte.screening.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.deloitte.screening.app.document.controllers.DocumentController;
import in.deloitte.screening.app.document.controllers.JobDescriptionController;
import in.deloitte.screening.app.document.controllers.ResumeController;
import in.deloitte.screening.app.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.document.dto.SearchJdResponse;
import in.deloitte.screening.app.document.dto.UploadJdResponse;
import in.deloitte.screening.app.document.dto.UploadResumesResponse;
import in.deloitte.screening.app.document.services.JobDescriptionServiceImpl;
import in.deloitte.screening.app.document.services.ResumeService;
import in.deloitte.screening.app.exceptions.BadInputException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ControllerDocumentTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ResumeService resumeService;
    @Mock
    private JobDescriptionServiceImpl jobDescriptionService;
    @InjectMocks
    private ResumeController resumeController;
    @InjectMocks
    private JobDescriptionController jobDescriptionController;
    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(resumeController).build();

    }

    @AfterEach
    public void tearDown() {
        mockMvc = null;
    }

    @Test
    @DisplayName("test for saving resume downloaded user info success")
    public void saveResumeDownloadedUserInfoSuccessTest() throws Exception {

        ResumeDownloadedUserRequest request = new ResumeDownloadedUserRequest();
        request.setUserEmail("shwlandge@deloitte.com");
        request.setApplicantEmail("user@deloitte.com");

        String mockResponse = "Updated...";
        when(resumeService.saveDownloadedUserInfo(any(ResumeDownloadedUserRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/resume/download/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Updated..."));
    }

    @Test
    @DisplayName("test for search job description by title success")
    public void searchJobDescriptionByTitleSuccessTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(jobDescriptionController).build();
        List<SearchJdResponse> mockResponse = new ArrayList<>();
        when(jobDescriptionService.getAllJdInfo(anyString())).thenReturn(mockResponse);

        String jdFileName = "jdFile.pdf";
        String requestUrl = "/jd/search/" + jdFileName;

        System.out.println("Request URL: " + requestUrl);

        mockMvc.perform(get(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(convertJsonToString(mockResponse)));

    }

//    @Test()
//    @DisplayName("test for save applicant resume failure due to no resume")
//    public void saveApplicantResumeNoResumesTest() {
//        List<MultipartFile> resumes = new ArrayList<>();
//        assertThrows(IndexOutOfBoundsException.class, () -> documentController.saveApplicantResume(resumes, "shwlandge@deloitte.com"));
//    }

    @Test()
    @DisplayName("test for save applicant resume failure due to tooMany resume")
    public void saveApplicantResumeTooManyResumesTest() {
        List<MultipartFile> resumes = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            resumes.add(new MockMultipartFile("cv", "cv" + i + ".pdf", "application/pdf", new byte[1024]));
        }
        assertThrows(BadInputException.class, () -> {
            documentController.saveApplicantResume(resumes, "shwlandge@deloitte.com");
        });
    }

    @Test()
    @DisplayName("test for save applicant resume failure due to null email")
    public void saveApplicantResumeNullEmailTest() {
        MockMultipartFile file = new MockMultipartFile("cv", "cv.pdf", "application/pdf", new byte[1024]);
        List<MultipartFile> resumes = new ArrayList<>();
        resumes.add(file);
        assertThrows(BadInputException.class, () -> {
            documentController.saveApplicantResume(resumes, "null");
        });
    }

    @Test()
    @DisplayName("test for save applicant resume failure due to empty email")
    public void saveApplicantResumeEmptyEmailTest() {
        MockMultipartFile file = new MockMultipartFile("cv", "cv.pdf", "application/pdf", new byte[1024]);
        List<MultipartFile> resumes = new ArrayList<>();
        resumes.add(file);
        assertThrows(BadInputException.class, () -> {
            documentController.saveApplicantResume(resumes, "\"\"");
        });

    }

    @Test
    @DisplayName("test for save applicant resume success")
    public void saveApplicantResumeSuccessTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile("cv", "cv.pdf", "application/pdf", new byte[1024]);
        List<MultipartFile> resumes = new ArrayList<>();
        resumes.add(file);

        ResponseEntity<UploadResumesResponse> responseEntity = documentController.saveApplicantResume(resumes, "shwlandge@deloitte.com");

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("test for save job description failure due to invalid file format")
    public void saveJobDescriptionInvalidFileFormatTest() {
        MockMultipartFile file = new MockMultipartFile("jd", "file.txt", "text/plain", "Some text".getBytes());
        assertThrows(BadInputException.class, () -> {
            documentController.saveJobDescription(file, "shwlandge@deloitte.com");
        });
    }

    @Test
    @DisplayName("test for save job description failure due to null email")
    public void saveJobDescriptionNullEmailTest() {
        MockMultipartFile file = new MockMultipartFile("jd", "cv.pdf", "application/pdf", new byte[1024]);
        assertThrows(BadInputException.class, () -> {
            documentController.saveJobDescription(file, "null");
        });
    }

    @Test
    @DisplayName("test for save job description failure due to empty email")
    public void saveJobDescriptionEmptyEmailTest() {
        MockMultipartFile file = new MockMultipartFile("jd", "cv.pdf", "application/pdf", new byte[1024]);
        assertThrows(BadInputException.class, () -> {
            documentController.saveJobDescription(file, "\"\"");
        });
    }

    @Test
    @DisplayName("test for save job description success")
    public void saveJobDescriptionSuccessTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile("jd", "cv.pdf", "application/pdf", new byte[1024]);
        when(jobDescriptionService.saveJobDescription(any(MultipartFile.class), anyString()))
                .thenReturn(new UploadJdResponse("message", "jd.pdf", HttpStatus.CREATED));

        ResponseEntity<UploadJdResponse> responseEntity = documentController.saveJobDescription(file, "shwlandge@deloitte.comm");

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    private String convertJsonToString(final Object obj) {

        String jsonString;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonString = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }
}
