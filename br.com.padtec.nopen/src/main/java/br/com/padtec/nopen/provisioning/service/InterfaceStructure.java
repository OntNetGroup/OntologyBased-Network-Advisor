package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.jena.rdf.model.InfModel;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import java.lang.reflect.Type;

public class InterfaceStructure {
	
	public static String getPossibleTargetInputs(String sourceOutputId, String targetEquipmentId, OKCoUploader repository){
		String interfaces = getInterfacesFromEquipment(targetEquipmentId, "Input", repository);
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<String,ArrayList<HashMap<String,String>>>>(){}.getType();
		HashMap<String,ArrayList<HashMap<String,String>>> interfacesMap = gson.fromJson(interfaces, type);

		for (Entry<String, ArrayList<HashMap<String, String>>> entrySet : interfacesMap.entrySet()) {
			//for each layer, get the list of ports
			ArrayList<HashMap<String,String>> listPorts = entrySet.getValue();
			Iterator<HashMap<String,String>> iterator = listPorts.iterator();
			while(iterator.hasNext()){
				HashMap<String, String> it = iterator.next();
				String portId = it.get("id");
				System.out.println();
				String srcIndividualUri = repository.getNamespace() + portId;
				String relationURI = repository.getNamespace() + RelationEnum.INV_vertical_links_to_Input_Card_Output_Card.toString();
				String tgtClassURI = repository.getNamespace() + ConceptEnum.Output_Card.toString();
				boolean hasVerticalConnection = QueryUtil.hasTargetIndividualFromClass(repository.getBaseModel(), srcIndividualUri, relationURI, tgtClassURI);
				relationURI = repository.getNamespace() + RelationEnum.horizontal_links_to_Output_Card_Output_Card.toString();
				boolean hasHorizontalConnection = QueryUtil.hasTargetIndividualFromClass(repository.getBaseModel(), srcIndividualUri, relationURI, tgtClassURI);
				if(hasVerticalConnection || hasHorizontalConnection){
					//remove from arraylist
					it.remove("id");
					it.remove("name");
				}
			}
	    }
		//remount the json string and return
		String result = gson.toJson(interfacesMap);
		return result;
	}
	
	
	public static String[] getPossibleConnections(String sourceEquipmentId, String targetEquipmentId, String typeOfConnection, String typePort, OKCoUploader repository){
		String[] result = new String[2];
		if(typeOfConnection.equals("Horizontal")){
			String resultSource = getInterfacesFromEquipment(sourceEquipmentId, "Output", repository);
			String resultTarget = getInterfacesFromEquipment(targetEquipmentId, "Output", repository);
			result[0] = resultSource;
			result[1] = resultTarget;
		}
		else{
			String resultSource = getInterfacesFromEquipment(sourceEquipmentId, typePort, repository);
			String resultTarget = getInterfacesFromEquipment(targetEquipmentId, typePort, repository);
			result[0] = resultSource;
			result[1] = resultTarget;
		}
		return result;
	}
	
	public static String getTypeOfConnection(String sourceEquipmentId, String targetEquipmentId, OKCoUploader repository){
		String typeOfConnection = null;
		ArrayList<String> layersSourceEquip = getLayersFromEquipment(repository.getBaseModel(), sourceEquipmentId);
		ArrayList<String> layersTargetEquip = getLayersFromEquipment(repository.getBaseModel(), targetEquipmentId);
		if((layersSourceEquip.contains(repository.getNamespace() + "OTS")) && (layersTargetEquip.contains(repository.getNamespace() + "OTS"))){
			typeOfConnection = "Horizontal";
		}
		else{
			typeOfConnection = "Vertical";
		}	
		return typeOfConnection;
	}
	
	public static String getInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		return generateMappingInterfacesFromEquipment(equipmentId, typePort, repository);
	}
	
	public static String generateMappingInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		
		//create result hash
		HashMap<String, ArrayList<HashMap<String, String>>> result = new HashMap<String, ArrayList<HashMap<String, String>>>();

		//create relations array
		ArrayList<String> relationsNameList = new ArrayList<String>();
		
		//add Card > Card_layer relations in array  
		relationsNameList.add(RelationEnum.INV_A_Card_CardLayer.toString());
		
		//add Card > Output/Input relations in array  
		if(typePort == "Output") {
			relationsNameList.add(RelationEnum.A_Card_OutputCard.toString());
		}
		else if(typePort == "Input") {
			relationsNameList.add(RelationEnum.A_Card_InputCard.toString());
		}
		else {
			relationsNameList.add(RelationEnum.componentOf.toString());
		}
		
		//get layers
		ArrayList<String> layers = getLayersFromEquipment(repository.getBaseModel(), equipmentId);
		for(String layer : layers){

			//create layer array
			ArrayList<HashMap<String, String>> layerPortMapping = new ArrayList<HashMap<String, String>>();
			
			//get Output/Input ports by layer
			layer = layer.substring(layer.indexOf("#")+1);
			
			ArrayList<String> ports = new ArrayList<String>();
			ports = QueryUtil.endOfGraph(repository.getBaseModel(), layer, relationsNameList);
			for(String port : ports){
				if(!hasBinds(repository.getBaseModel(), port)){
					//create port hash
					HashMap<String, String> portMapping = new HashMap<String, String>();
					
					//replace port namespace 
					port = port.replace(repository.getNamespace(), "");
					
					//get label of port
					String label = QueryUtil.getLabelFromOWL(repository.getBaseModel(), port);
					
					//replace label language
					label = label.replace("@EN", "");
					
					//create port object
					portMapping.put("id", port);
					portMapping.put("name", label);
					
					//add port hash in layer array
					layerPortMapping.add(portMapping);
				}
				
				
			}
			
			//add layer in result hash
			if(layerPortMapping.size() > 0) {
				
				//replace layer namespace 
				layer = layer.replace(repository.getNamespace(), "");
				
				//put layer hash on result hash
				result.put(layer, layerPortMapping);
			}
			
		}
		
		//transform the result mapping in a JSON string
		Gson gson = new Gson(); 
		String json = gson.toJson(result);
		System.out.println(json);
				
		return json;
	}

	private static boolean hasBinds(InfModel model, String portId) {
		return NOpenQueryUtil.hasBinds(model, portId);
	}

	private static ArrayList<String> getLayersFromEquipment(InfModel model, String individualName) {
		ArrayList<String> relationsNameList = new ArrayList<String>();
		relationsNameList.add(RelationEnum.INV_supervises_Equipment_Supervisor.toString());
		relationsNameList.add(RelationEnum.supervises_card_Supervisor_Card.toString());
		relationsNameList.add(RelationEnum.A_Card_CardLayer.toString());
		return QueryUtil.endOfGraph(model, individualName, relationsNameList);
	}
}
