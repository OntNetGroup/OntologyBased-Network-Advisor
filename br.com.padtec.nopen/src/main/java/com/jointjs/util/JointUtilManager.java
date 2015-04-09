package com.jointjs.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
	
	/**
	 * * Transform a Generic Java object to JSON String
	 * @param obj
	 * @return
	 */
	public static String getJSONFromJava(Object obj){		 
		try {
			
			StringWriter json = new StringWriter();
			mapper.writeValue(json, obj);
			
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	
	/**
	 * Transform Java Object to XML String
	 * @param nt
	 * @return
	 */
	public static String jaxbObjectToXML(Object obj) {
		 
		StringWriter xml = new StringWriter();
		
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(obj, xml);
            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
        return xml.toString();
    }
	
	/**
	 * Transform XML String to Java Object
	 * @param nt
	 * @return
	 */
	public static <T> Object jaxbXMLToObject(String xml, Class<T> cls) {
        try {
            JAXBContext context = JAXBContext.newInstance(cls);
            Unmarshaller un = context.createUnmarshaller();
            
            StringReader xmlReader = new StringReader(xml);
            
            @SuppressWarnings("unchecked")
			T obj = (T) un.unmarshal(xmlReader);
            return obj;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
