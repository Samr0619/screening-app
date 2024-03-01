package in.deloitte.screening.app.resume.document.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
public class VectorizeDocumentContent {

	public Map<String, Long> getVector(List<String> tokens) {
		
		return tokens.stream()
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
	}
	
	/**
	 * <p>
	 * Returns a double[] of word frequencies
	 * </p>
	 * 
	 * @param commonWordMap
	 * 
	 * @return double[]
	 * 
	 */
	public double[] convertToDoubleVector(Map<String, Long> commonWordMap) {

		double[] vector = new double[commonWordMap.size()];
		int i = 0;
		for (long frequency : commonWordMap.values()) {
			vector[i++] = (int) frequency;
		}
//		vector[9] = 1;
		return vector;
	}
}
