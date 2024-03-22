package in.deloitte.screening.app.service;

import in.deloitte.screening.app.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.document.dto.SearchJdResponse;
import in.deloitte.screening.app.document.dto.UploadJdResponse;
import in.deloitte.screening.app.document.dto.UploadResumesResponse;
import in.deloitte.screening.app.document.entities.JobDescription;
import in.deloitte.screening.app.document.entities.Resume;
import in.deloitte.screening.app.document.entities.ResumeJobDescription;
import in.deloitte.screening.app.document.repositories.JobDescriptionRepository;
import in.deloitte.screening.app.document.repositories.ResumeJobDescriptionRespository;
import in.deloitte.screening.app.document.repositories.ResumeRepository;
import in.deloitte.screening.app.document.services.CosineSimilarityCalculationServiceImpl;
import in.deloitte.screening.app.document.services.JobDescriptionServiceImpl;
import in.deloitte.screening.app.document.services.ResumeServiceImpl;
import in.deloitte.screening.app.document.utils.DocumentTextAnalyzer;
import in.deloitte.screening.app.document.utils.DocumentTextExtractor;
import in.deloitte.screening.app.document.utils.VectorizeDocumentText;
import in.deloitte.screening.app.skills.repositories.SkillsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceDocumentTests {

    //    @Mock
//    private VectorizeDocumentContent vectorizeDocumentContent;
    @Mock
    private VectorizeDocumentText vectorizeDocumentText;
/*    @Mock
    private PdfContentExtractor pdfContentExtractor;*/

    @Mock
    private SkillsRepository skillsRepository;
    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private JobDescriptionRepository jobDescriptionRepository;
    @Mock
    private ResumeJobDescriptionRespository resumeJobDescriptionRespository;
    @Mock
    private EntityManager entityManager;
    @Mock
    @Qualifier("pdf")
    private DocumentTextExtractor pdfTextExtractor;
    @Mock
    @Qualifier("docx")
    private DocumentTextExtractor docxTextExtractor;

    @Mock
    private DocumentTextAnalyzer documentTextAnalyzer;
    @Mock
    private Query query;

    @InjectMocks
    private JobDescriptionServiceImpl jdService;
    @InjectMocks
    private ResumeServiceImpl resumeService;

    @InjectMocks
    private CosineSimilarityCalculationServiceImpl cosineService;

    @Test
    @DisplayName("test for cosine similarity of jd and resume Success")
    public void testGetCosineSimilaritySuccessTest() throws IOException {

        Map<String, Long> cvMap = new HashMap<>();
        cvMap.put("java", 2L);
        cvMap.put("python", 3L);
        cvMap.put("javascript", 1L);

        Map<String, Long> jdMap = new HashMap<>();
        jdMap.put("java", 3L);
        jdMap.put("python", 1L);
        jdMap.put("ruby", 2L);

        double[] jdWordsVector = {3.0, 1.0, 0.0};
        // double[] resumeWordsVector = {2.0, 3.0, 1.0};

        when(vectorizeDocumentText.convertToDoubleVector(jdMap)).thenReturn(jdWordsVector);


        double similarity = cosineService.getCosineSimilarity(cvMap, jdMap);

        double expectedSimilarity = 0.9647638212377322;

        Assertions.assertEquals(expectedSimilarity, similarity, 0.00001);
    }

    @Test
    @DisplayName("test for cosine similarity of jd and resume Failed")
    public void testGetCosineSimilarityFailedTest() {
        Map<String, Long> cvMap = Collections.emptyMap();
        Map<String, Long> jdMap = Collections.emptyMap();


        assertThrows(NullPointerException.class, () -> cosineService.getCosineSimilarity(cvMap, jdMap));
    }

    @Test
    @DisplayName("test for save Jd success")
    public void saveJobDescriptionSuccessTest() throws IOException {
        MockMultipartFile jd = new MockMultipartFile("jd", "test.pdf", "application/pdf", "pdf content".getBytes());

        when(pdfTextExtractor.extractText(any())).thenReturn("pdf content");
        List<ResumeJobDescription> list = new ArrayList<>();
        //when(DocumentTextAnalyzer.stopWords()).thenReturn("a an the");
        when(documentTextAnalyzer.tokenizeText(any(), anyString())).thenReturn(Collections.singletonList("pdf"));
        when(skillsRepository.getAllSkills()).thenReturn(Collections.singletonList("pdf"));
        when(jobDescriptionRepository.save(any(JobDescription.class))).thenReturn(new JobDescription());
        when(resumeJobDescriptionRespository.saveAll(any())).thenReturn(list);
        UploadJdResponse response = jdService.saveJobDescription(jd, "shwlandge@deloitte.com");

        Assertions.assertEquals("Job Description is uploaded", response.message());
    }

    @Test
    @DisplayName("test for save Jd failure")
    public void saveJobDescriptionFailureTest() throws IOException {
        MockMultipartFile jd = new MockMultipartFile("jd", "test.pdf", "application/pdf", "pdf content".getBytes());
        when(pdfTextExtractor.extractText(any())).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> jdService.saveJobDescription(jd, "shwlandge@deloitte.com"));
    }

    @Test
    @DisplayName("test for get all Jd info success")
    public void getAllJdInfoSuccessTest() {
        String jdFileName = "example.pdf";

        List<SearchJdResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new SearchJdResponse(jdFileName, new byte[1024]));
        when(entityManager.createNativeQuery(anyString(), eq(SearchJdResponse.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedResult);

        List<SearchJdResponse> actualResult = jdService.getAllJdInfo(jdFileName);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("test for get all Jd info failure")
    public void getAllJdInfoFailureTest() {
        String jdFileName = "jdFile.pdf";

        when(entityManager.createNativeQuery(anyString(), eq(SearchJdResponse.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(RuntimeException.class); // Simulate a runtime exception during query execution

        assertThrows(RuntimeException.class, () -> jdService.getAllJdInfo(jdFileName));
    }

    @Test
    @DisplayName("test for save Downloaded User Info Success")
    public void saveDownloadedUserInfoSuccessTest() {
        ResumeDownloadedUserRequest request = new ResumeDownloadedUserRequest();
        request.setApplicantEmail("shwlandge@deloitte.com");
        request.setUserEmail("user@deloitte.com");

        Resume resume = new Resume();
        resume.setId(1);

        when(resumeRepository.getResumeData(anyString())).thenReturn(resume);

        String result = resumeService.saveDownloadedUserInfo(request);

        verify(resumeRepository).saveDownloadedUserInfo(eq(1), eq("shwlandge@deloitte.com"),
                any(LocalDateTime.class), eq("user@deloitte.com"));

        Assertions.assertEquals("Updated...", result);
    }

    @Test
    @DisplayName("test for save Downloaded User Info Failed")
    public void saveDownloadedUserInfoFailedTest() {
        ResumeDownloadedUserRequest request = new ResumeDownloadedUserRequest();
        request.setApplicantEmail("applicant@example.com");
        request.setUserEmail("user@example.com");

        when(resumeRepository.getResumeData(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> resumeService.saveDownloadedUserInfo(request));
    }

    @Test
    public void saveResumeInfoSuccessTest() throws IOException {
        List<MultipartFile> resumes = Arrays.asList(
                new MockMultipartFile("resume1.pdf", "resume1.pdf", "application/pdf", "pdf content".getBytes()),
                new MockMultipartFile("resume2.docx", "resume2.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx content".getBytes())
        );
        String userEmail = "shwlandge@deloitte.com";

        when(skillsRepository.getAllSkills()).thenReturn(Arrays.asList("Java", "Python", "SQL"));
        when(pdfTextExtractor.extractText(any())).thenReturn("pdf text");
        when(docxTextExtractor.extractText(any())).thenReturn("docx text");
        when(documentTextAnalyzer.tokenizeText(any(), any())).thenReturn(Arrays.asList("Java", "Python"));
        when(vectorizeDocumentText.getVector(any())).thenReturn(Collections.singletonMap("Java", 1L));

        UploadResumesResponse response = resumeService.saveResumeInfo(resumes, userEmail);

        Assertions.assertEquals("2 resumes uploaded successfully...", response.getMessage());
        verify(resumeRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testSaveResumeInfo_Failure() throws IOException {
        // Mocking data
        List<MultipartFile> resumes = Arrays.asList(
                new MockMultipartFile("resume1.pdf", "resume1.pdf", "application/pdf", "pdf content".getBytes()),
                new MockMultipartFile("resume2.docx", "resume2.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx content".getBytes())
        );
        String userEmail = "shwlandge@deloitte.com";

        when(skillsRepository.getAllSkills()).thenReturn(Arrays.asList("Java", "Python", "SQL"));
        when(pdfTextExtractor.extractText(any())).thenThrow(IOException.class); // Simulating IOException during text extraction

        assertThrows(IOException.class, () -> resumeService.saveResumeInfo(resumes, userEmail));
        // verifyZeroInteractions(vectorize); // Ensure no interactions with vectorizeDocumentText
    }
}
