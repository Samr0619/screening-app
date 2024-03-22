package in.deloitte.screening.app.document.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.deloitte.screening.app.document.utils.VectorizeDocumentText;


@Service
public class CosineSimilarityCalculationServiceImpl implements CosineSimilarityCalculationService {

	@Autowired
	VectorizeDocumentText vectorize;


	@Override
	public double getCosineSimilarity(Map<String, Long> cvMap, Map<String, Long> jdMap)
														throws IOException {

		List<Long> cv = new ArrayList<>();

		for (String s : jdMap.keySet()) {
			for (Map.Entry<String, Long> e : cvMap.entrySet()) {
				if (s.equals(e.getKey())) {
					cv.add(e.getValue());
				}
			}
		}
		
		double[] jdWordsVector = vectorize.convertToDoubleVector(jdMap);
		double[] resumeWordsVector = new double[jdMap.size()];
		for (int i = 0; i < cv.size(); i++) {
			resumeWordsVector[i] = (double) cv.get(i);
		}

		RealVector resumeWordsVectorResult = new ArrayRealVector(resumeWordsVector);
		RealVector jdWordsVectorResult = new ArrayRealVector(jdWordsVector);

		return resumeWordsVectorResult.cosine(jdWordsVectorResult);
	}
}
