package in.deloitte.screening.app.document.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
public class VectorizeDocumentContent {

	/**
	 * 
	 * @param tokens
	 * 
	 * @return Returns map of tokens and their frequencies
	 * 
	 */
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

		return vector;
	}
}
