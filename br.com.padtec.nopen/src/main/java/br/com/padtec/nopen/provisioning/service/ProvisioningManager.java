package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.ProvisioningRelationEnum;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;

import com.google.gson.Gson;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.jointjs.util.JointUtilManager;

public class ProvisioningManager {

	/**
	 * Procedure to parse json elements to owl
	 * @param jsonElements
	 * @throws Exception
	 * @author Lucas Bassetti
	 */
	public static void createElementsInOWL(String jsonElements) throws Exception {
		
		OntModel ontModel = ProvisioningComponents.provisioningRepository.getBaseModel();
		String namespace = ProvisioningComponents.provisioningRepository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PElement[] elements = (PElement[]) JointUtilManager.getJavaFromJSON(jsonElements, PElement[].class);		
		for(PElement element : elements) {
			
			String individualURI = namespace + element.getId();
			String classURI = namespace + element.getType();
			
			//create new individual
			factoryUtil.createInstanceIndividualStatement(ontModel, individualURI, classURI, false);
			
			//set individual label
			Resource individual = ontModel.createResource(individualURI);
			
			Statement stmt = ontModel.createStatement(individual, RDFS.label, ontModel.createLiteral(element.getName()));
			factoryUtil.stmts.add(stmt);
		}	
		
		factoryUtil.processStatements(ontModel);
		
	}
	
	/**
	 * Procedure to parse json links to owl
	 * @param jsonLinks
	 * @throws Exception
	 * @author Lucas Bassetti
	 */
	public static void createLinksInOWL(String jsonLinks) throws Exception {
		
		OntModel ontModel = ProvisioningComponents.provisioningRepository.getBaseModel();
		String namespace = ProvisioningComponents.provisioningRepository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink link : links) {
			
			String subject = namespace + link.getSubject();
			String predicate = namespace + link.getPredicate();
			String object = namespace + link.getObject();
			
			factoryUtil.createInstanceRelationStatement(ontModel, subject, predicate, object, false);
			
		}
		
		factoryUtil.processStatements(ontModel);
		
	}
	
	/**
	 * Procedure to get connection type from owl by source and target equipments
	 * @param sourceEquipmentId
	 * @param targetEquipmentId
	 * @return
	 */
	public static String getConnectionType(String sourceEquipmentId, String targetEquipmentId){
		
		String namespace = ProvisioningComponents.provisioningRepository.getNamespace();
		
		ArrayList<String> physicalLayers = ProvisioningQuery.getBottomLayersFromOWL();
		String connectionType = "";
		
		ArrayList<String> layersSourceEquip = getLayersFromEquipment(sourceEquipmentId);
		ArrayList<String> layersTargetEquip = getLayersFromEquipment(targetEquipmentId);
		
		boolean isSourcePhysicalLayer = false, 
				isTargetPhysicalLayer = false; 
		
		for(String layer : layersSourceEquip) {
			layer = layer.replace(namespace, "");
			String layerLabel = ProvisioningQuery.getLabelFromOWL(layer);
			
			if(physicalLayers.contains(layerLabel)) {
				isSourcePhysicalLayer = true;
			}
		}
		
		for(String layer : layersTargetEquip) {
			layer = layer.replace(namespace, "");
			String layerLabel = ProvisioningQuery.getLabelFromOWL(layer);
			
			if(physicalLayers.contains(layerLabel)) {
				isTargetPhysicalLayer = true;
			}
		}
		
		if(isSourcePhysicalLayer && isTargetPhysicalLayer){
			connectionType = "Horizontal";
		}
		else{
			connectionType = "Vertical";
		}	
		
		return connectionType;
	}
	
	/**
	 * Procedure to get connection interfaces from OWL by source and target equipments and connection type.
	 * @param sourceEquipmentId
	 * @param targetEquipmentId
	 * @param connectionType
	 * @return
	 */
	public static String getConnectionInterfaces(String sourceEquipmentId, String targetEquipmentId, String connectionType) {
		
		HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>> connectionInterfaces = new HashMap<String,HashMap<String, ArrayList<HashMap<String, String>>>>();
		
		if(connectionType.equals("Horizontal")){
			connectionInterfaces.put(sourceEquipmentId, getPortsByLayerFromOWL(sourceEquipmentId, "Output_Card", connectionType, true));
			connectionInterfaces.put(targetEquipmentId, getPortsByLayerFromOWL(targetEquipmentId, "Output_Card", connectionType, true));
		}
		else{
			connectionInterfaces.put(sourceEquipmentId, getPortsByLayerFromOWL(sourceEquipmentId, "Output_Card", connectionType, true));
			connectionInterfaces.put(targetEquipmentId, getPortsByLayerFromOWL(targetEquipmentId, "Input_Card", connectionType, true));
		}
		
		Gson gson = new Gson();
		return gson.toJson(connectionInterfaces);
		
	}
	
	/**
	 * Procedure to get Layers of a equipment from OWL
	 * @param equipmentId
	 * @return
	 */
	public static ArrayList<String> getLayersFromEquipment(String equipmentId) {
		
		ArrayList<String> predicate = new ArrayList<String>();
		
		predicate.add(ProvisioningRelationEnum.A_Equipment_Card.toString());
		predicate.add(ProvisioningRelationEnum.A_Card_CardLayer.toString());
		
		return ProvisioningQuery.getObjectFromOWL(equipmentId, predicate);
		
	}
	
	/**
	 * Procedure to get ports of equipment by layer from OWL
	 * @param equipmentId
	 * @param portType
	 * @param excludeConnectedPorts
	 * @return
	 */
	public static HashMap<String, ArrayList<HashMap<String, String>>> getPortsByLayerFromOWL(String equipmentId, String portType, boolean excludeConnectedPorts) {
		return getPortsByLayerFromOWL(equipmentId, portType, null, excludeConnectedPorts);
	};
	
	/**
	 * Procedure to get ports of equipment by layer from OWL
	 * @param equipmentId
	 * @param portType
	 * @param connectionType
	 * @param excludeConnectedPorts
	 * @return
	 */
	public static HashMap<String, ArrayList<HashMap<String, String>>> getPortsByLayerFromOWL(String equipmentId, String portType, String connectionType, boolean excludeConnectedPorts) {
		
		String namespace = ProvisioningComponents.provisioningRepository.getNamespace();
		
		//create result hash
		HashMap<String, ArrayList<HashMap<String, String>>> outputPortsByLayer = new HashMap<String, ArrayList<HashMap<String, String>>>();
		
		ArrayList<String> predicates = new ArrayList<String>();
		
		predicates.add(ProvisioningRelationEnum.A_Equipment_Card.toString());
		predicates.add(ProvisioningRelationEnum.A_Card_CardLayer.toString());
		
		ArrayList<String> layers = ProvisioningQuery.getObjectFromOWL(equipmentId, predicates);
		
		String 	lastLayerLabel = "",
				layerLabel = "";
		ArrayList<HashMap<String, String>> layerPortMapping = new ArrayList<HashMap<String, String>>();
		
		
		ArrayList<String> possibleLayer = new ArrayList<String>();
		if(connectionType == null) {
			possibleLayer = ProvisioningQuery.getAllLayersFromOWL();
		}
		else if(connectionType.equals("Horizontal")) {
			possibleLayer = ProvisioningQuery.getBottomLayersFromOWL();
		}
		else if (connectionType.equals("Vertical")) {
			possibleLayer = ProvisioningQuery.getTopLayersFromOWL();
		}
		else {
			possibleLayer = ProvisioningQuery.getAllLayersFromOWL();
		}
		
		for(String layer : layers) {
			
			layer = layer.replace(namespace, "");
			//get label of layer
			layerLabel = ProvisioningQuery.getLabelFromOWL(layer);
			
			System.out.println(possibleLayer.toString());
			System.out.println("layerLabel: " + layerLabel);
			
			if(possibleLayer.contains(layerLabel)) {
			
				if(lastLayerLabel.equals("")) {
					lastLayerLabel = layerLabel;
				}
				
				predicates = new ArrayList<String>();
				if(portType == "Output_Card") {
					predicates.add(RelationEnum.INV_intermediates_up_Card_Layer_Transport_Function.toString());
					predicates.add(RelationEnum.INV_is_interface_of_Output_Card_Transport_Function.toString());
				}
				else if(portType == "Input_Card") {
					predicates.add(RelationEnum.INV_intermediates_down_Card_Layer_Transport_Function.toString());
					predicates.add(RelationEnum.INV_is_interface_of_Input_Card_Transport_Function.toString());
				}
				
				if(!lastLayerLabel.equals(layerLabel)) {
					
					//add layer in result hash
					if(layerPortMapping.size() > 0) {
						//put layer hash on result hash
						outputPortsByLayer.put(lastLayerLabel, layerPortMapping);
					}
					
					lastLayerLabel = layerLabel;
					layerPortMapping = new ArrayList<HashMap<String, String>>();
					
				}
				
				ArrayList<String> ports = ProvisioningQuery.getObjectFromOWL(layer, predicates);
				for(String port : ports) {
					
					//replace port namespace 
					port = port.replace(namespace, "");
					
					boolean excludePort = false;
					if(excludeConnectedPorts) {
						if(ProvisioningQuery.isConnectedPort(port)) {
							excludePort = true;
						}
					}
					
					if(!excludePort) {
						//create port hash
						HashMap<String, String> portMapping = new HashMap<String, String>();
						
						//get label of port
						String label = ProvisioningQuery.getLabelFromOWL(port);
						
//						System.out.println("PORT: " + label);
						
						//replace label language
						label = label.replace("@en", "");
						label = label.replace("@EN", "");
						
						//create port object
						portMapping.put("id", port);
						portMapping.put("name", label);
						portMapping.put("type", portType);
						
						//add port hash in layer array
						layerPortMapping.add(portMapping);
					}
					
				}
			}
		}
		
		if(!lastLayerLabel.equals("")) {
			
			//add layer in result hash
			if(layerPortMapping.size() > 0) {
				//put layer hash on result hash
				outputPortsByLayer.put(lastLayerLabel, layerPortMapping);
			}
			
		}
		
		return outputPortsByLayer;
	}
	
	
}
