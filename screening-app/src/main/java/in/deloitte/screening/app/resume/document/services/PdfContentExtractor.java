package in.deloitte.screening.app.resume.document.services;

import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Qualifier("pdf")
public class PdfContentExtractor implements DocumentContentExtractionService {
	
	/**
	 * <p>
	 * Returns the document content as a String after removing junk words
	 * </p>
	 * 
	 * @param file
	 * 
	 * @return document content as a String 
	 * 
	 * @throws IOException
	 * 
	 */
	public String extractContent(MultipartFile file) throws IOException {

		PDDocument document = Loader.loadPDF(file.getBytes());
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String content = pdfStripper.getText(document).toLowerCase();
		
		return content.replaceAll(
				"[\\\r\n\\,\\/\\(\\)\\#\\%\\!\\$\\&\\*\\a\\+\\-\\_\\=\\<\\>\\?\\'\\;\\[\\]\\{\\}\\|\\^:,]", "");
	}

}
