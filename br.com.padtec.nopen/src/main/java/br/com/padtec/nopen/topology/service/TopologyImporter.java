package br.com.padtec.nopen.topology.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.padtec.nopen.topology.model.NetworkTopology;
import br.com.padtec.nopen.topology.service.util.Util;

public class TopologyImporter {

	private static NetworkTopology jaxbXMLToObject(String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(NetworkTopology.class);
            Unmarshaller un = context.createUnmarshaller();
            
            StringReader xmlReader = new StringReader(xml);
            NetworkTopology nt = (NetworkTopology) un.unmarshal(xmlReader);
            return nt;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * Method to import a Topology file in a XML format
	 */
	public String importTopology(HttpServletRequest request) {
		
		// Read XML

		Util util = Util.getInstance();
		
		String xml = "";
		try {
			xml = util.readData(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		System.out.println(xml);
		
		NetworkTopology nt = new NetworkTopology();
		nt = jaxbXMLToObject(xml);
		
		System.out.println(nt.getTopology().getId());

		ObjectMapper mapper = new ObjectMapper();
		 
		try {
	 
			StringWriter json = new StringWriter();
			mapper.writeValue(json, nt);
			
			return json.toString();
	 
		} catch (Exception e) {
	 
			e.printStackTrace();
		}
		
		return "";
		
	}
	
}
