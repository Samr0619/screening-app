package in.deloitte.screening.app.document.utils;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface DocumentTextExtractor {
	
	public String extractText(MultipartFile file) throws IOException;
	
	/**
	 * 
	 * @param docText
	 * 
	 * @return Returns docText(Resume or JD) after removing invalid characters
	 * 
	 */
	public static String cleanDocumentText(String docText) {
		
		return docText.replaceAll(
				"[\\\r\n\\,\\/\\(\\)\\#\\%\\!\\$\\&\\*\\a\\-\\_\\=\\<\\>\\?\\'\\;\\[\\]\\{\\}\\|\\^:,]", " ");
	}
}
