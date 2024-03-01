package in.deloitte.screening.app.resume.document.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface CosineSimilarityCalculationService {
	
	/**
	 * This method extracts the content from PDF returns it as a Map with key as a
	 * word and value as it's frequencies
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
	public Map<String, HashMap<String, Double>> getCosineSimilarity(List<MultipartFile> resumes,
			List<MultipartFile> jobDescription, MultipartFile stopWordsFile) throws IOException;
	
	/**
	 * @param cvMap
	 * @param jdMap
	 * 
	 * @return cosine similarity
	 * 
	 * @throws IOException
	 * 
	 */
	public double getCosineSimilarity(Map<String, Long> cvMap, 
			Map<String, Long>jdMap) throws IOException;

}
