package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.InfModel;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class InterfaceStructure {
	
	/*public String[] MapsToJSON(){
		Gson gson = new Gson(); 
		String json = gson.toJson(myObject); 
	}
	*/
	
	public static String getInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		return generateMappingInterfacesFromEquipment(equipmentId, typePort, repository);
	}
	
	public static String generateMappingInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		HashMap<String, ArrayList<String>> layerPortMapping = new HashMap<String, ArrayList<String>>();
		ArrayList<String> layers = getLayersFromEquipment(repository.getBaseModel(), equipmentId);
		// generate the mapping
		for(String layer : layers){
			System.out.println("LAYER -> " + layer);
			ArrayList<String> result = new ArrayList<String>();
			ArrayList<String> relationsNameList = new ArrayList<String>();
			
			relationsNameList.add(RelationEnum.intermediates_up_Transport_Function_Card_Layer.toString());
			relationsNameList.add(RelationEnum.INV_is_interface_of.toString());
			relationsNameList.add(RelationEnum.componentOf.toString());
			ArrayList<String> ports = QueryUtil.endOfGraph(repository.getBaseModel(), layer, relationsNameList);
			for(String port : ports){
				
				System.out.println("PORTA -> " + port);
				if(QueryUtil.getIndividualTypes(repository.getBaseModel(), port).contains(typePort)){
					result.add(port);
				}
			}
			if(!result.isEmpty()){
				layerPortMapping.put(layer, result);
			}
		}
		
		//transform the mapping in a json string
		Gson gson = new Gson(); 
		String json = gson.toJson(layerPortMapping);
		System.out.println("STRING FINAL -> " + json);
		return json;
	}

	private static ArrayList<String> getLayersFromEquipment(InfModel model, String individualName) {
		ArrayList<String> relationsNameList = new ArrayList<String>();
		relationsNameList.add(RelationEnum.INV_supervises_Equipment_Supervisor.toString());
		relationsNameList.add(RelationEnum.supervises_card_Supervisor_Card.toString());
		relationsNameList.add(RelationEnum.componentOf.toString());
		return QueryUtil.endOfGraph(model, individualName, relationsNameList);
	}
}
