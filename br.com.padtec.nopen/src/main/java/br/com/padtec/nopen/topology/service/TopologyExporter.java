package br.com.padtec.nopen.topology.service;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.padtec.nopen.topology.model.NetworkTopology;
import br.com.padtec.nopen.topology.model.TLink;
import br.com.padtec.nopen.topology.model.TNode;
import br.com.padtec.nopen.topology.model.Topology;
import br.com.padtec.nopen.topology.model.TopologyLinks;
import br.com.padtec.nopen.topology.model.TopologyNodes;

public class TopologyExporter {

	
	private static String jaxbObjectToXML(NetworkTopology nt) {
		 
		StringWriter xml = new StringWriter();
		
        try {
            JAXBContext context = JAXBContext.newInstance(NetworkTopology.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
 
            // Write to System.out for debugging
            // m.marshal(emp, System.out);

            m.marshal(nt, xml);
            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
        return xml.toString();
    }
	
	
	/**
	 *  Method to generate the Topology in a XML format
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 */
	public String exportTopology(String tnodeArray, String tlinkArray, String uuid) {	

		ObjectMapper mapper = new ObjectMapper();
		 
		try {
	 
			TNode[] nodes = mapper.readValue(tnodeArray, TNode[].class);
			TLink[] links = mapper.readValue(tlinkArray, TLink[].class);

			TopologyNodes tnodes = new TopologyNodes(nodes);
			TopologyLinks tlinks = new TopologyLinks(links);
			Topology t = new Topology(uuid, tnodes, tlinks);
			NetworkTopology nt = new NetworkTopology(t);
			
			return jaxbObjectToXML(nt);
	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return "";
		
	}
	
}
