package in.deloitte.screening.app.resume.document.entities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MapToJson implements AttributeConverter<Map<String, Long>, String>{

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, Long> attribute) {
		String value = "";
		try {
			value =  mapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public Map<String, Long> convertToEntityAttribute(String dbData) {
		
		Map<String, Long> map = new HashMap<>();
		System.out.println("dbData : " + dbData);
		try {
			map =  mapper.readValue(dbData, new TypeReference<Map<String, Long>>(){});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return map;
	}
}
