package in.deloitte.screening.app.document.utils;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface DocumentContentExtractionService {
	
	public String extractContent(MultipartFile file) throws IOException;
	
	/**
	 * 
	 * @param content
	 * 
	 * @return Returns content after removing invalid characters
	 * 
	 */
	public static String cleanDocumentContent(String content) {
		
		return content.replaceAll(
				"[\\\r\n\\,\\/\\(\\)\\#\\%\\!\\$\\&\\*\\a\\+\\-\\_\\=\\<\\>\\?\\'\\;\\[\\]\\{\\}\\|\\^:,]", "");
	}
}
