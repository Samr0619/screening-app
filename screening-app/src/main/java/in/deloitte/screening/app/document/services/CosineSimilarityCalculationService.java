package in.deloitte.screening.app.document.services;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface CosineSimilarityCalculationService {
	
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
