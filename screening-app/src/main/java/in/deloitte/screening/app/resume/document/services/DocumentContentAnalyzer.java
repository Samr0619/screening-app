package in.deloitte.screening.app.resume.document.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class DocumentContentAnalyzer {

	public String getStopWords(MultipartFile stopWordsFile) throws IOException {

		PDDocument stopWordsdoc = Loader.loadPDF(stopWordsFile.getBytes());
		PDFTextStripper pdfTextStripper = new PDFTextStripper();

		return pdfTextStripper.getText(stopWordsdoc).toLowerCase();
	}

	/**
	 * 
	 * 
	 * @param analyzer
	 * @param cleanedContent
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 */
	public List<String> tokenizeContent(Analyzer analyzer, String cv) throws IOException {

		TokenStream result = analyzer.tokenStream(null, cv.toLowerCase());
//		result = new PorterStemFilter(result);
		CharTermAttribute resultAttr = result.addAttribute(CharTermAttribute.class);
		List<String> tokens = new ArrayList<>();
		result.reset();

		while (result.incrementToken()) {
			tokens.add(resultAttr.toString());
		}
		result.close();

		return tokens;
	}

}
