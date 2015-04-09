package br.com.padtec.nopen.topology.service;

import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import com.jointjs.util.JointUtilManager;

import br.com.padtec.nopen.topology.model.NetworkTopology;
import br.com.padtec.nopen.topology.model.TLink;
import br.com.padtec.nopen.topology.model.TNode;
import br.com.padtec.nopen.topology.model.Topology;
import br.com.padtec.nopen.topology.model.TopologyLinks;
import br.com.padtec.nopen.topology.model.TopologyNodes;

public class TopologyExporter {

	/**
	 *  Method to generate the Topology in a XML format
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 */
	public String exportTopology(String tnodeArray, String tlinkArray, String uuid) {	
		 
		TNode[] nodes = (TNode[]) JointUtilManager.getJavaFromJSON(tnodeArray, TNode[].class);
		TLink[] links = (TLink[]) JointUtilManager.getJavaFromJSON(tlinkArray, TLink[].class);
		
		TopologyNodes tnodes = new TopologyNodes(nodes);
		TopologyLinks tlinks = new TopologyLinks(links);
		Topology t = new Topology(uuid, tnodes, tlinks);
		NetworkTopology nt = new NetworkTopology(t);
			
		return JointUtilManager.jaxbObjectToXML((NetworkTopology) nt);
		
	}
	
}
