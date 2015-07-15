package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.InfModel;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class InterfaceStructure {
	
	
	public static String getInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		return generateMappingInterfacesFromEquipment(equipmentId, typePort, repository);
	}
	
	public static String generateMappingInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		
		//create result hash
		HashMap<String, ArrayList<HashMap<String, String>>> result = new HashMap<String, ArrayList<HashMap<String, String>>>();

		//get layers
		ArrayList<String> layers = getLayersFromEquipment(repository.getBaseModel(), equipmentId);
		for(String layer : layers){

			//create layer array
			ArrayList<HashMap<String, String>> layerPortMapping = new ArrayList<HashMap<String, String>>();
			
			//create relations array
			ArrayList<String> relationsNameList = new ArrayList<String>();
			
			//add Card > Card_layer relations in array  
			relationsNameList.add(RelationEnum.INV_A_Card_CardLayer.toString());
			
			//add Card > Output/Input relations in array  
			if(typePort == "Output") {
				relationsNameList.add(RelationEnum.A_Card_OutputCard.toString());
			}
			else if(typePort == "Intput") {
				relationsNameList.add(RelationEnum.A_Card_InputCard.toString());
			}
			else {
				relationsNameList.add(RelationEnum.componentOf.toString());
			}
			
			//get Output/Input ports by layer
			ArrayList<String> ports = QueryUtil.endOfGraph(repository.getBaseModel(), layer, relationsNameList);
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
