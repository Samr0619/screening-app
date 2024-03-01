package in.deloitte.screening.app.resume.document.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class CosineSimilarityCalculationServiceImpl implements CosineSimilarityCalculationService {

	@Autowired
	DocumentContentExtractionService pdfContentExtractor;

	@Autowired
	DocumentContentAnalyzer documentContentAnalyzer;

	@Autowired
	VectorizeDocumentContent vectorize;

	/**
	 * <p>
	 * This method extracts the content from PDF returns it as a Map with key as a
	 * word and value as it's frequencies
	 * </p>
	 * 
	 * @param resumes
	 * @param jobDescription
	 * @param stopWordsFile
	 * 
	 * @return ConcurrentHashMap<String, Long> of Words and frequencies
	 * 
	 * @throws IOException
	 * 
	 */
	@Override
	public Map<String, HashMap<String, Double>> getCosineSimilarity(List<MultipartFile> resumes,
			List<MultipartFile> jobDescription, MultipartFile stopWordsFile) throws IOException {

		Map<String, HashMap<String, Double>> resultMap = new HashMap<>();
		Double cosineSimilarity = 0.0;

		String stopWords = documentContentAnalyzer.getStopWords(stopWordsFile);
		Analyzer analyzer = new StopAnalyzer(new CharArraySet(Arrays.asList(stopWords.split(" ")), true));

		for (MultipartFile jobDesc : jobDescription) {

			String jdContent = pdfContentExtractor.extractContent(jobDesc);
			List<String> jdTokens = documentContentAnalyzer.tokenizeContent(analyzer, jdContent);
			HashMap<String, Double> map = new HashMap<>();

			for (MultipartFile resume : resumes) {
				String experience = "";
				String email = "";
				String cvContent = pdfContentExtractor.extractContent(resume);

				int idx = cvContent.indexOf("years");
				System.out.println("idx : " + idx);
				for (int i = idx - 2; i >= idx - 7; i--) {
					if (cvContent.charAt(i) == ' ')
						break;
					experience = cvContent.charAt(i) + experience;
				}
				System.out.println("experience : " + experience);
				int idx2 = cvContent.indexOf("@");
				System.out.println("idx2 : " + idx2);
				for (int i = idx2 + 12; i >= idx2 - 20; i--) {
					if (cvContent.charAt(i) == ' ')
						break;
					email = cvContent.charAt(i) + email;
				}
				System.out.println("email : " + email);

				List<String> resumeTokens = documentContentAnalyzer.tokenizeContent(analyzer, cvContent);

				cosineSimilarity = calculateCosineSimilarity(resumeTokens, jdTokens);
				map.put(resume.getOriginalFilename(), cosineSimilarity);

			}
			resultMap.put(jobDesc.getOriginalFilename(), map);

		}

		analyzer.close();
		return resultMap;
	}

	/**
	 * <p>
	 * Returns cosine similarity of resume and job description documents as a double
	 * value. This method uses APACHE commons math3 library methods to calculate
	 * cosine similarity
	 * </p>
	 * 
	 * @param resume
	 * @param jd
	 * 
	 * @return Returns cosine similarity of resume and JD DOCS
	 * 
	 */
	public double calculateCosineSimilarity(List<String> resumeTokens, List<String> jdTokens) {

		Map<String, Long> resumeWordFrequency = vectorize.getVector(resumeTokens);
		Map<String, Long> jdWordFrequency = vectorize.getVector(jdTokens);

		Set<String> commonWords = new HashSet<>(resumeWordFrequency.keySet());
		commonWords.retainAll(jdWordFrequency.keySet());

		Map<String, Long> resumeCommonWordMap = new HashMap<>();
		Map<String, Long> jdCommonWordMap = new HashMap<>();

		for (String word : commonWords) {
			if (resumeWordFrequency.containsKey(word)) {
				resumeCommonWordMap.put(word, resumeWordFrequency.get(word));
			}
			if (jdWordFrequency.containsKey(word)) {
				jdCommonWordMap.put(word, jdWordFrequency.get(word));
			}
		}

		double[] resumeWordsVector = vectorize.convertToDoubleVector(resumeCommonWordMap);
		double[] jdWordsVector = vectorize.convertToDoubleVector(jdCommonWordMap);

		RealVector resumeWordsVectorResult = new ArrayRealVector(resumeWordsVector);
		RealVector jdWordsVectorResult = new ArrayRealVector(jdWordsVector);

		return resumeWordsVectorResult.cosine(jdWordsVectorResult);
	}

	@Override
	public double getCosineSimilarity(Map<String, Long> cvMap, Map<String, Long> jdMap) throws IOException {

		System.out.println("resumeCommonWordMap : " + cvMap);
		System.out.println("jdCommonWordMap : " + jdMap);

		List<Long> cv = new ArrayList<>();
//		Map<String, Long> cv = new LinkedHashMap<>();
//		Map<String, Long> cv = new HashMap<>();

//		for(Map.Entry<String, Long> e : cvMap.entrySet()) {
		for (String s : jdMap.keySet()) {
			for (Map.Entry<String, Long> e : cvMap.entrySet()) {
				if (s.equals(e.getKey())) {
//					cv.put(e.getKey(), e.getValue());
					cv.add(e.getValue());
				}
			}
//			if(!cv.containsKey(s))
//				cv.put(s, (long)0);
		}
		System.out.println("cv : " + cv);

		double[] jdWordsVector = vectorize.convertToDoubleVector(jdMap);
		double[] resumeWordsVector = new double[jdMap.size()];
		for (int i = 0; i < cv.size(); i++) {
			if(i <= cv.size())
			resumeWordsVector[i] = (double) cv.get(i);
		}

		System.out.println("resumeWordsVector : " + Arrays.toString(resumeWordsVector));
		System.out.println("jdWordsVector : " + Arrays.toString(jdWordsVector));

		System.out.println("resumeWordsVector length : " + resumeWordsVector.length);
		System.out.println("jdWordsVector length : " + jdWordsVector.length);

		RealVector resumeWordsVectorResult = new ArrayRealVector(resumeWordsVector);
		RealVector jdWordsVectorResult = new ArrayRealVector(jdWordsVector);
		
//		System.out.println("cvVector.size()/jdVector.size() : " + (double)8/(double)9);
//		System.out.println("cv.size() : " + cv.size());
//		System.out.println("jdWordsVector.length : " + jdWordsVector.length);

		return jdWordsVectorResult.cosine(resumeWordsVectorResult);
//		return (double)(cv.size()/jdWordsVector.length);
	}
}
