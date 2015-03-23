package br.com.padtec.nopen.topology.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Util {

	private static Util instance;
	
	private Util(){
		
	}
	
	public static synchronized Util getInstance(){
		
		if(instance == null){
			instance = new Util();
		}
		
		return instance;
		
	}

	public String readData(HttpServletRequest request) throws IOException{
		
		request.setCharacterEncoding("UTF-8");

		StringBuilder data = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	    	data.append(str);
	    }    
	    
	    return data.toString();
	    
	}
	
	public Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  

        try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
       return null;
    }
	
}
