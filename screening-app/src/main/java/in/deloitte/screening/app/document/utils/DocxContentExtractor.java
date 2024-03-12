package in.deloitte.screening.app.document.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("docx")
public class DocxContentExtractor implements DocumentContentExtractionService {

	@Override
	public String extractContent(MultipartFile file) throws IOException {

		InputStream inputStream = new ByteArrayInputStream(file.getBytes());
		XWPFDocument document = new XWPFDocument(inputStream);
		XWPFWordExtractor extractor = new XWPFWordExtractor(document);
		String content = extractor.getText().toLowerCase();
		extractor.close();
		document.close();
		
		return DocumentContentExtractionService.cleanDocumentContent(content);

	}

}
