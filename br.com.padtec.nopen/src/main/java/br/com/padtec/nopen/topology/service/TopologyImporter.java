package br.com.padtec.nopen.topology.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.padtec.nopen.topology.service.util.DataReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TopologyImporter {

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * Method to import a Topology file in a XML format
	 */
	public String importTopology(HttpServletRequest request) {
		
		// Read XML

		DataReader dataReader = DataReader.getInstance();
		
		String xml = "";
		try {
			xml = dataReader.readData(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		Document doc = dataReader.convertStringToDocument(xml);

		// Get topology tags
		NodeList nodeList = doc.getElementsByTagName("node");
		NodeList nodeIdList = doc.getElementsByTagName("node-id");
		NodeList linkIdList = doc.getElementsByTagName("link-id");
		NodeList linkList = doc.getElementsByTagName("link");
		NodeList sourceList = doc.getElementsByTagName("source");
		NodeList destList = doc.getElementsByTagName("destination");

		JsonObject topology = new JsonObject();
		JsonArray cells = new JsonArray();	
		
		// Read nodes
		for (int i = 0; i < nodeList.getLength(); i++){
			
			Node node = nodeList.item(i);
			String nodeId = "";
			
			if(nodeIdList.item(i) != null){
				Node nodeIdItem = nodeIdList.item(i);
				nodeId = nodeIdItem.getTextContent();
			}
			else{
				nodeId = node.getAttributes().getNamedItem("id").getTextContent();
			}
			
			JsonObject jsonNode = createNode(nodeId, nodeId);
			cells.add(jsonNode);
		}
		
		// Read links
		for (int i = 0; i < linkList.getLength(); i++){
			
			Node link = linkList.item(i);
			Node sourceNode = sourceList.item(i);
			Node destNode = destList.item(i);
			String linkId = "";
			
			if(linkIdList.item(i) != null){
				Node linkIdItem = linkIdList.item(i);
				linkId = linkIdItem.getTextContent();
			}
			else{
				linkId = link.getAttributes().getNamedItem("id").getTextContent();
			}
			
			String source = sourceNode.getTextContent();
			String target = destNode.getTextContent();
			
			JsonObject jsonLink = createLink(linkId, source, target);
			cells.add(jsonLink);
		}
		
		topology.add("cells", cells);
	
		return topology.toString();
		
	}
	
	/**
	 * 
	 * @param nodeName
	 * @param nodeId
	 * @return
	 * Method to create a Node element
	 */
	public JsonObject createNode(String nodeName, String nodeId){
		
		// Create node
		JsonObject node = new JsonObject();
		node.addProperty("type", "basic.Circle");
		
		// Add node ID
		node.addProperty("id", nodeId);
		
		// Add node attributes
		JsonObject attrs = createNodeAttributes(nodeName);
		node.add("attrs", attrs);
		
		return node;
		
	}
	
	public JsonObject createNodeAttributes(String nodeName){
		
		JsonObject attrs = new JsonObject();
		
		JsonObject circle = new JsonObject();
		JsonObject text = new JsonObject();
		
		// Set default properties
		circle.addProperty("fill", "#00c6ff");	
		text.addProperty("font-size", 8);
		
		// Add none name
		text.addProperty("text", nodeName);
		
		// Add json objects in attrs
		attrs.add("circle", circle);
		attrs.add("text", text);
		
		return attrs;
	}
	
	/**
	 * 
	 * @param linkId
	 * @param source
	 * @param target
	 * @return
	 * Method do create a Link element
	 */
	public JsonObject createLink(String linkId, String source, String target){
		
		// Create link
		JsonObject link = new JsonObject();
		link.addProperty("type", "link");
		
		// Add source and target nodes
		JsonObject nodeSource = new JsonObject();
		JsonObject nodeTarget = new JsonObject();
		
		nodeSource.addProperty("id", source);
		nodeTarget.addProperty("id", target);
		
		link.add("source", nodeSource);
		link.add("target", nodeTarget);
		
		// Add link ID
		link.addProperty("id", linkId);
		
		// Add link attributes
		JsonObject attrs = createLinkAttributes();
		link.add("attrs", attrs);
		
		return link;
		
	}
	
	public JsonObject createLinkAttributes(){
		
		JsonObject attrs = new JsonObject();
		
		//JsonObject markerSource = new JsonObject();
		JsonObject markerTarget = new JsonObject();
		
		//markerSource.addProperty("d", "M 10 0 L 0 5 L 10 10");
		markerTarget.addProperty("d", "M 10 0 L 0 5 L 10 10");
		
		//attrs.add(".marker-source", markerSource);
		attrs.add(".marker-target", markerTarget);
		
		return attrs;
	}
	
	
}
