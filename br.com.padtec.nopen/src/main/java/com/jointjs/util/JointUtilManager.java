package com.jointjs.util;

import org.codehaus.jackson.map.ObjectMapper;

public class JointUtilManager {
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Transform a JSON String to a Generic Java object
	 * */
	public static <T> Object getJavaFromJSON(String json, Class<T> cls){
		try {
			return (T) mapper.readValue(json, cls);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
