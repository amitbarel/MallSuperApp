//package superapp.utils;
//
//import java.util.Map;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import jakarta.persistence.AttributeConverter;
//
//public class ConverterOfMapToJson implements AttributeConverter<Map<String, Object>, String> {
//	
//private ObjectMapper jackson;
//	
//	public ConverterOfMapToJson() {
//		this.jackson = new ObjectMapper();
//	}
//	
//	@Override
//	public String convertToDatabaseColumn(Map<String, Object> attribute) {
//		try {
//			return this.jackson.writeValueAsString(attribute);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	@Override
//	public Map<String, Object> convertToEntityAttribute(String dbData) {
//		try {
//			return this.jackson.readValue(dbData, Map.class);
//		}catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//}
