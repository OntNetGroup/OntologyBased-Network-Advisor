package br.com.padtec.nopen.topology.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import com.jointjs.util.JointUtilManager;

import br.com.padtec.nopen.topology.model.NetworkTopology;
import br.com.padtec.nopen.topology.service.util.Util;

public class TopologyImporter {
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * Method to import a XML Topology file to JSON String
	 */
	public String importTopology(HttpServletRequest request) {
		
		// Read XML
		String xml = Util.readData(request);

		// Transform XML to Java Object
		NetworkTopology nt = new NetworkTopology();
		nt = (NetworkTopology) JointUtilManager.jaxbXMLToObject(xml, NetworkTopology.class);

		// Transform Java Object to JSON
		return JointUtilManager.getJSONFromJava((NetworkTopology) nt);
		
	}
	
}
