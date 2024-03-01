package in.deloitte.screening.app.resume.document.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface DocumentContentExtractionService {
	
	public String extractContent(MultipartFile file) throws IOException;
}
