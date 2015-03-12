package br.com.padtec.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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

import br.com.padtec.networktopology.Link;
import br.com.padtec.networktopology.Node;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



//@Controller
public class TopologyExporterController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private static String PADTEC_URI = "http://www.padtec.com.br";
	private static String PADTEC_TOPOLOGY_URI = "http://www.padtec.com.br/topology/";
	private static String PADTEC_NODE_URI = "http://www.padtec.com.br/node/";
	private static String PADTEC_LINK_URI = "http://www.padtec.com.br/link/";
	

	
	//@RequestMapping(value = "/exportTopology", method = RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		
		request.setCharacterEncoding("UTF-8");
		

		//String simpleJSON = request.getParameter("json");
		
		StringBuilder simpleJSON = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	    	simpleJSON.append(str);
	    }    
		
	    //String simpleJSON = request.getParameter("json");
		
		int linkIdCounter = 1;
		ArrayList<String> nodesId = new ArrayList<String>();
		
		Map<String, Node> nodes = new HashMap<String, Node>();
		ArrayList<Link> links = new ArrayList<Link>();
		
		System.out.println(simpleJSON.toString());
		
		// JSON

		JsonParser crunhifyParser = new JsonParser();
		JsonObject jsonObject = crunhifyParser.parse(simpleJSON.toString()).getAsJsonObject();
		JsonArray jsonArray = jsonObject.getAsJsonArray("cells");
		
		for(int i = 0; i < jsonArray.size(); i++){
			
			// NODES
			if(jsonArray.get(i).getAsJsonObject().get("type").getAsString().equals("basic.Circle")){
			
				String nodeName = jsonArray.get(i).getAsJsonObject().getAsJsonObject("attrs").getAsJsonObject("text").get("text").getAsString();
				String nodeId = jsonArray.get(i).getAsJsonObject().get("id").getAsString();
				
				nodeName = nodeName.replaceAll(" ", "_");
				
				Node node = new Node(nodeId, nodeName);
				nodes.put(nodeId, node);
				nodesId.add(nodeId);
				
			}
			// LINKS
			else{
				
				JsonElement source = jsonArray.get(i).getAsJsonObject().getAsJsonObject("source").get("id");
				JsonElement target = jsonArray.get(i).getAsJsonObject().getAsJsonObject("target").get("id");
				
				if(source != null && target != null){
					Link link = new Link(linkIdCounter, nodes.get(source.getAsString()), nodes.get(target.getAsString()));
					links.add(link);
					
					linkIdCounter++;
				}

			}
		}
		
		
		// XML
		
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder = null;
		try {
			icBuilder = icFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Document doc = icBuilder.newDocument();
        
        Element networkTopology = doc.createElementNS(PADTEC_URI, "network-topology");
        doc.appendChild(networkTopology);
        
        Element topology = doc.createElement("topology");
        Element topologyId = doc.createElement("topology-id");
        
        networkTopology.appendChild(topology);
        topology.appendChild(topologyId);
        topologyId.appendChild(doc.createTextNode(PADTEC_TOPOLOGY_URI + "topology1"));

		
		for(int i = 0; i < nodesId.size(); i++){
			
			Element node = doc.createElement("node");
			Element nodeId = doc.createElement("node-id");
			
			topology.appendChild(node);
			node.appendChild(nodeId);
			 
			nodeId.appendChild(doc.createTextNode(PADTEC_NODE_URI + nodes.get(nodesId.get(i)).getName()));
			
		}
		
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
			
			linkId.appendChild(doc.createTextNode(PADTEC_LINK_URI + links.get(i).getId()));
			sourceNode.appendChild(doc.createTextNode(PADTEC_NODE_URI + links.get(i).getNodeSource().getName()));
			destNode.appendChild(doc.createTextNode(PADTEC_NODE_URI + links.get(i).getNodeTarget().getName()));
			
		}
		
		try {

			//StringWriter out = new StringWriter();
			response.setContentType("application/xml"); 
			//response.setHeader("Content-Disposition", "attachment;filename=topology.xml");  
			
			OutputStream out = response.getOutputStream();
			out = response.getOutputStream();
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(out);
			transformer.transform(source, console);
 
			//response.getWriter().print(out.toString());
			
			out.flush();
			out.close();
	       
	    } catch (Exception e){
	        e.printStackTrace();
	    }
		
	}
	
}


