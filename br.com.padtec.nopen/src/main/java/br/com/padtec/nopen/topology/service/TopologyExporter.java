package br.com.padtec.nopen.topology.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.com.padtec.nopen.topology.model.TLink;
import br.com.padtec.nopen.topology.model.TNode;
import br.com.padtec.nopen.topology.service.util.DataReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TopologyExporter {

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * Method to generate the Topology in a XML format
	 */
	public void exportTopology(HttpServletRequest request, HttpServletResponse response) throws IOException{	

	    // Variables
	    
	    //int linkIdCounter = 1;
		ArrayList<String> nodesId = new ArrayList<String>();
		
		Map<String, TNode> nodes = new HashMap<String, TNode>();
		ArrayList<TLink> links = new ArrayList<TLink>();
		
		// READ JSON
		
		DataReader dataReader = DataReader.getInstance();
		String json = dataReader.readData(request);
		
		// Parse JSON

		JsonParser crunhifyParser = new JsonParser();
		JsonObject jsonObject = crunhifyParser.parse(json).getAsJsonObject();
		JsonArray jsonArray = jsonObject.getAsJsonArray("cells");
		
		for(int i = 0; i < jsonArray.size(); i++){
			
			// Create NODES array
			if(jsonArray.get(i).getAsJsonObject().get("type").getAsString().equals("basic.Circle")){
			
				String nodeName = jsonArray.get(i).getAsJsonObject().getAsJsonObject("attrs").getAsJsonObject("text").get("text").getAsString();
				String nodeId = jsonArray.get(i).getAsJsonObject().get("id").getAsString();
				
				nodeName = nodeName.replaceAll(" ", "_");
				
				TNode node = new TNode(nodeId, nodeName);
				nodes.put(nodeId, node);
				nodesId.add(nodeId);
				
			}
			// Create LINKS array
			else{
				
				String linkId = jsonArray.get(i).getAsJsonObject().get("id").getAsString();
				
				String source = jsonArray.get(i).getAsJsonObject().getAsJsonObject("source").get("id").getAsString();
				String target = jsonArray.get(i).getAsJsonObject().getAsJsonObject("target").get("id").getAsString();
				
				if(source != null && target != null){
					TLink link = new TLink(linkId, nodes.get(source), nodes.get(target));
					links.add(link);
					
					//linkIdCounter++;
				}

			}
		}
		
		
		// --- GENERATE XML --- 
		
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder = null;
		try {
			icBuilder = icFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
        Document doc = icBuilder.newDocument();
        
        Element networkTopology = doc.createElement("network-topology");
        doc.appendChild(networkTopology);
        
        Element topology = doc.createElement("topology");
        Element topologyId = doc.createElement("topology-id");
        
        networkTopology.appendChild(topology);
        topology.appendChild(topologyId);
        topologyId.appendChild(doc.createTextNode("topology1"));

		// NODES
		for(int i = 0; i < nodesId.size(); i++){
			
			Element node = doc.createElement("node");
			Element nodeId = doc.createElement("node-id");
			
			topology.appendChild(node);
			node.appendChild(nodeId);
			 
			nodeId.appendChild(doc.createTextNode(nodes.get(nodesId.get(i)).getName()));
			
		}
		
		// LINKS
		for(int i = 0; i < links.size(); i++){
			
			Element link = doc.createElement("link");
			Element linkId = doc.createElement("link-id");
			Element source = doc.createElement("source");
			Element sourceNode = doc.createElement("source-node");
			Element destination = doc.createElement("destination");
			Element destNode = doc.createElement("dest-node");
			
			topology.appendChild(link);
			link.appendChild(linkId);
			link.appendChild(source);
			link.appendChild(destination);
			source.appendChild(sourceNode);
			destination.appendChild(destNode);
			
			linkId.appendChild(doc.createTextNode(links.get(i).getId()));
			sourceNode.appendChild(doc.createTextNode(links.get(i).getNodeSource().getName()));
			destNode.appendChild(doc.createTextNode(links.get(i).getNodeTarget().getName()));
			
		}
		
		try {

			//StringWriter out = new StringWriter();	
			//response.setHeader("Content-Disposition", "attachment;filename=topology.xml");  
			//response.getWriter().print(out.toString());
			
			// Generate XML output
			
			response.setContentType("application/xml"); 
			
			OutputStream out = response.getOutputStream();
			out = response.getOutputStream();
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(out);
			transformer.transform(source, console);
			
			out.flush();
			out.close();
	       
	    } catch (Exception e){
	        e.printStackTrace();
	    }
		
	}
	
}
