package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.jena.rdf.model.InfModel;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.Util;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.ProvisioningRelationEnum;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.service.NOpenComponents;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.studio.service.PerformBind;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

import java.lang.reflect.Type;

public class InterfaceStructure {
	
/*	public static String getMatrixWithPorts(String equipmentId, OKCoUploader repository){
		ArrayList<String> matrixes = getMatrixesFromEquipment(repository.getBaseModel(), equipmentId);
		HashMap<String,ArrayList<HashMap<String,String>>> result = new HashMap<String,ArrayList<HashMap<String,String>>>();
		for(String matrix : matrixes){
			//create layer array
			ArrayList<HashMap<String, String>> matrixPortMapping = new ArrayList<HashMap<String, String>>();
			
			//get Output/Input ports by layer
			matrix = matrix.substring(matrix.indexOf("#")+1);
			
			ArrayList<String> ports = new ArrayList<String>();
			
			ArrayList<String> relationsNameList = new ArrayList<String>();
			//add the inputs to the mapping
			relationsNameList.add(repository.getNamespace() + RelationEnum.A_Matrix_MatrixInput.toString());
			ports = QueryUtil.endOfGraph(repository.getBaseModel(), matrix, relationsNameList );
			for(String port : ports){
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
				portMapping.put("type", "Input");

				//add port hash in layer array
				matrixPortMapping.add(portMapping);
			}
			

			//add the outputs to the mapping
			relationsNameList.remove(relationsNameList.size()-1);
			relationsNameList.add(repository.getNamespace() + RelationEnum.A_Matrix_MatrixOutput.toString());
			ports = QueryUtil.endOfGraph(repository.getBaseModel(), matrix, relationsNameList );
			for(String port : ports){
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
				portMapping.put("type", "Output");

				//add port hash in layer array
				matrixPortMapping.add(portMapping);
			}

			
			//add layer in result hash
			if(matrixPortMapping.size() > 0) {
				
				//replace layer namespace 
				matrix = matrix.replace(repository.getNamespace(), "");
				
				//put layer hash on result hash
				result.put(matrix, matrixPortMapping);
			}
		}
		
		//transform the result mapping in a JSON string
		Gson gson = new Gson(); 
		String json = gson.toJson(result);
		System.out.println(json);
				
		return json;
	}


	/*private static ArrayList<String> getMatrixesFromEquipment(InfModel model, String individualName) {
		ArrayList<String> relationsNameList = new ArrayList<String>();
		relationsNameList.add(RelationEnum.INV_supervises_Equipment_Supervisor.toString());
		relationsNameList.add(RelationEnum.supervises_card_Supervisor_Card.toString());
		relationsNameList.add(RelationEnum.A_Card_TFCardElement.toString());
		ArrayList<String> result = QueryUtil.endOfGraph(model, individualName, relationsNameList);
		Iterator<String> iterator = result.iterator();
		while(iterator.hasNext()){
			String tfCardElement = iterator.next();
			String classURI = model.getNsPrefixURI("") + ConceptEnum.Matrix.toString();
			if(!QueryUtil.isIndividualFromClass(model, tfCardElement, classURI )){
				iterator.remove();
			}
		}
		return result;
	}*/

	public static String getOutputsUpperLayerFromEquipment(String equipmentId, OKCoUploader repository){
		ArrayList<String> layers = getLayersFromEquipment(repository.getBaseModel(), equipmentId);
		Iterator it = layers.iterator();
		while(it.hasNext()){
			String layer = (String) it.next();
			//retira as camadas que tem clientes
			ArrayList<String> relations = new ArrayList<String>();
			relations.add(RelationEnum.instantiates_Card_Layer_Layer_Type.toString());
			ArrayList<String> layerTypes = QueryUtil.endOfGraph(repository.getBaseModel(), layer, relations);
			//pra camada ser a mais acima, ela n�o pode ter cliente no mesmo equipamento
			boolean hasClient = hasClientInEquipment(layerTypes.get(0), equipmentId, repository); 
			if(hasClient){
				it.remove();
			}
		}
		
		return null;
	}
	
	
	private static boolean hasClientInEquipment(String layerTypeId, String equipmentId, OKCoUploader repository) {
		// TODO Auto-generated method stub
		return false;
	}


	public static String getPossibleTargetInputs(String sourceOutputId, String targetEquipmentId, OKCoUploader repository){
		HashMap<String, ArrayList<HashMap<String, String>>> interfacesMap = getInterfacesFromEquipment(targetEquipmentId, "Input", repository);
//		Gson gson = new Gson();
//		Type type = new TypeToken<HashMap<String,ArrayList<HashMap<String,String>>>>(){}.getType();
//		HashMap<String,ArrayList<HashMap<String,String>>> interfacesMap = gson.fromJson(interfaces, type);

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
		Gson gson = new Gson();
		String result = gson.toJson(interfacesMap);
		return result;
	}
	
	
	public static String getPossibleConnections(String sourceEquipmentId, String targetEquipmentId, OKCoUploader repository, String typeOfConnection){
		//String typeOfConnection = getTypeOfConnection(sourceEquipmentId, targetEquipmentId, repository);
		HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>> map = new HashMap<String,HashMap<String, ArrayList<HashMap<String, String>>>>();
		String result = null;
		if(typeOfConnection.equals("Horizontal")){
			HashMap<String, ArrayList<HashMap<String, String>>> resultSource = getInterfacesFromEquipment(sourceEquipmentId, "Output", repository);
			HashMap<String, ArrayList<HashMap<String, String>>> resultTarget = getInterfacesFromEquipment(targetEquipmentId, "Output", repository);
			map.put(sourceEquipmentId, resultSource);
			map.put(targetEquipmentId, resultTarget);
		}
		else{
			HashMap<String, ArrayList<HashMap<String, String>>> resultSource = getInterfacesFromEquipment(sourceEquipmentId, "Output", repository);
			HashMap<String, ArrayList<HashMap<String, String>>> resultTarget = getInterfacesFromEquipment(targetEquipmentId, "Input", repository);
			map.put(sourceEquipmentId, resultSource);
			map.put(targetEquipmentId, resultTarget);
		}
		
		Gson gson = new Gson();
		result = gson.toJson(map);
		System.out.println(result);
		
		return result;
	}
	
	public static String getTypeOfConnection(String sourceEquipmentId, String targetEquipmentId, OKCoUploader repository){
		
		String physicalLayer = "OTS";
		String connectionType = "";
		
		ArrayList<String> layersSourceEquip = getLayersFromEquipment(repository.getBaseModel(), sourceEquipmentId);
		ArrayList<String> layersTargetEquip = getLayersFromEquipment(repository.getBaseModel(), targetEquipmentId);
		
		boolean sourceLayer = false, 
				targetLayer = false; 
		
		for(String layer : layersSourceEquip) {
			layer = layer.replace(repository.getNamespace(), "");
			String layerLabel = QueryUtil.getLabelFromOWL(repository.getBaseModel(), layer);
			
			if(layerLabel.equals(physicalLayer)) {
				sourceLayer = true;
			}
		}
		
		for(String layer : layersTargetEquip) {
			layer = layer.replace(repository.getNamespace(), "");
			String layerLabel = QueryUtil.getLabelFromOWL(repository.getBaseModel(), layer);
			
			if(layerLabel.equals(physicalLayer)) {
				targetLayer = true;
			}
		}
		
		if(sourceLayer && targetLayer){
			connectionType = "Horizontal";
		}
		else{
			connectionType = "Vertical";
		}	
		
		return connectionType;
	}
	
	public static HashMap<String, ArrayList<HashMap<String, String>>> getInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		return generateMappingInterfacesFromEquipment(equipmentId, typePort, repository);
	}
	
	private static HashMap<String, ArrayList<HashMap<String, String>>> generateMappingInterfacesFromEquipment(String equipmentId, String typePort, OKCoUploader repository){
		
		//create result hash
		HashMap<String, ArrayList<HashMap<String, String>>> result = new HashMap<String, ArrayList<HashMap<String, String>>>();
		
		//get layers
		
		ArrayList<String> layers = getLayersFromEquipment(repository.getBaseModel(), equipmentId);
		for(String layer : layers){

			//create relations array
			ArrayList<String> relationsNameList = new ArrayList<String>();
			
			//add Card > Output/Input relations in array  
			if(typePort == "Output") {
				relationsNameList.add(RelationEnum.INV_intermediates_down_Card_Layer_Transport_Function.toString());
				relationsNameList.add(RelationEnum.INV_is_interface_of_Output_Card_Transport_Function.toString());
			}
			else if(typePort == "Input") {
				relationsNameList.add(RelationEnum.INV_intermediates_up_Card_Layer_Transport_Function.toString());
				relationsNameList.add(RelationEnum.INV_is_interface_of_Input_Card_Transport_Function.toString());
			}
			else {
				relationsNameList.add(RelationEnum.componentOf.toString());
			}
			
			
			//create layer array
			ArrayList<HashMap<String, String>> layerPortMapping = new ArrayList<HashMap<String, String>>();
			
			//get Output/Input ports by layer
			layer = layer.substring(layer.indexOf("#")+1);
			
			ArrayList<String> ports = new ArrayList<String>();
			ports = QueryUtil.endOfGraph(repository.getBaseModel(), layer, relationsNameList);
			for(String port : ports){
				if(!hasBinds(repository.getBaseModel(), port)){ //na verdade � hasVerticalLinks or hasHorizontalLinks
					//create port hash
					HashMap<String, String> portMapping = new HashMap<String, String>();
					
					//replace port namespace 
					port = port.replace(repository.getNamespace(), "");
					
					//get label of port
					String label = QueryUtil.getLabelFromOWL(repository.getBaseModel(), port);
					
					//replace label language
					label = label.replace("@en", "");
					label = label.replace("@EN", "");
					
					//create port object
					portMapping.put("id", port);
					portMapping.put("name", label);
					portMapping.put("type", typePort + "_Card");
					
					//add port hash in layer array
					layerPortMapping.add(portMapping);
				}
				
				
			}
			
			//add layer in result hash
			if(layerPortMapping.size() > 0) {
				
				//replace layer namespace 
				layer = layer.replace(repository.getNamespace(), "");
				//get label of layer
				String layerLabel = QueryUtil.getLabelFromOWL(repository.getBaseModel(), layer);
				
				//put layer hash on result hash
				result.put(layerLabel, layerPortMapping);
			}
			
		}
		
		//transform the result mapping in a JSON string
		Gson gson = new Gson(); 
		String json = gson.toJson(result);
		System.out.println(json);
				
		return result;
	}

	private static boolean hasBinds(InfModel model, String portId) {
		return NOpenQueryUtil.hasBinds(model, portId);
	}

	private static ArrayList<String> getLayersFromEquipment(InfModel model, String individualName) {
		ArrayList<String> relationsNameList = new ArrayList<String>();/*
		relationsNameList.add(RelationEnum.INV_supervises_Equipment_Supervisor.toString());
		relationsNameList.add(RelationEnum.supervises_card_Supervisor_Card.toString());
		relationsNameList.add(RelationEnum.A_Card_CardLayer.toString());*/
		relationsNameList.add(ProvisioningRelationEnum.A_Equipment_Card.toString());
		relationsNameList.add(ProvisioningRelationEnum.A_Card_CardLayer.toString());
		return QueryUtil.endOfGraph(model, individualName, relationsNameList);
	}
	
	public static void applyPreProvisioningBinds(String typeOfConnection, String SourceId, String TargetId, OKCoUploader repository) throws Exception {
		if(typeOfConnection.equals("Vertical")){
			applyVerticalBinds(SourceId, TargetId, repository);
		}
		else{
			applyHorizontalBinds(SourceId, TargetId, repository);
		}
	}


	private static boolean applyVerticalBinds(String OutputCardSourceId, String InputCardTargetId, OKCoUploader repository) throws Exception {
		String rangeClassName = ConceptEnum.Transport_Function.toString();
		String sourceIndividualId = OutputCardSourceId;
		String property = RelationEnum.is_interface_of.toString();
		String targetIndividualId = InputCardTargetId;
		
		//first, we need to get the transport functions connected to the card's ports
		String[] tfSource = NOpenQueryUtil.getIndividualsNamesAtObjectPropertyRange(StudioComponents.studioRepository.getBaseModel(), sourceIndividualId, property, rangeClassName);
		String[] tfTarget = NOpenQueryUtil.getIndividualsNamesAtObjectPropertyRange(StudioComponents.studioRepository.getBaseModel(), targetIndividualId, property, rangeClassName);
		//then we need to know their specific classes
		List<String> typeTfSource = QueryUtil.getClassesURIFromIndividual(StudioComponents.studioRepository.getBaseModel(), StudioComponents.studioRepository.getNamespace() + tfSource[0]);
		List<String> typeTfTarget = QueryUtil.getClassesURIFromIndividual(StudioComponents.studioRepository.getBaseModel(), StudioComponents.studioRepository.getNamespace() + tfTarget[0]);
		
		Iterator<String> iterator = typeTfSource.iterator();
		while(iterator.hasNext()){
			String type = iterator.next();
			if(QueryUtil.hasSubClass(StudioComponents.studioRepository.getBaseModel(), type) && QueryUtil.SubClass(StudioComponents.studioRepository.getBaseModel(), type).size() > 1){
				iterator.remove();
			}
		}
		iterator = typeTfTarget.iterator();
		while(iterator.hasNext()){
			String type = iterator.next();
			if(QueryUtil.hasSubClass(StudioComponents.studioRepository.getBaseModel(), type) && QueryUtil.SubClass(StudioComponents.studioRepository.getBaseModel(), type).size() > 1){
				iterator.remove();
			}
		}
		
		//if there is no transport function we cannot make the connection
		if(tfSource.length == 0 || tfTarget.length == 0){
			NOpenLog.appendLine("Error: " + OutputCardSourceId + " cannot be bound to " + InputCardTargetId );
			throw new Exception("Error: Unexpected relation between " + OutputCardSourceId + " and " + InputCardTargetId );
		}
		
		String finalTypeTfSource = typeTfSource.get(0).substring(typeTfSource.get(0).indexOf("#")+1);
		String finalTypeTfTarget = typeTfTarget.get(0).substring(typeTfTarget.get(0).indexOf("#")+1);

		//then we create joint elements with the transport functions to call the binds procedure
		DtoJointElement newSource = new DtoJointElement();
		newSource.setId(tfSource[0]);
		newSource.setName(tfSource[0]);
		newSource.setType(finalTypeTfSource);
		DtoJointElement newTarget = new DtoJointElement();
		newTarget.setId(tfTarget[0]);
		newTarget.setName(tfTarget[0]);
		newTarget.setType(finalTypeTfTarget);
		System.out.println();
		boolean result = PerformBind.applyBinds(newSource, newTarget, "Provisioning", StudioComponents.studioRepository);

		String propertyURI = RelationEnum.vertical_links_to_Output_Card_Input_Card.toString();
		
		FactoryUtil.createInstanceRelation(
				repository.getBaseModel(), 
				repository.getNamespace() + OutputCardSourceId, 
				repository.getNamespace() + propertyURI  ,
				repository.getNamespace() + InputCardTargetId
			);			
	
		return result;
	}
	
	private static boolean applyHorizontalBinds(String OutputCardSourceId, String OutputCardTargetId, OKCoUploader repository) throws Exception {
		String rangeClassURI = ConceptEnum.Output.toString();

		String relationURI = repository.getNamespace() + RelationEnum.horizontal_links_to_Output_Card_Output_Card.toString();
		boolean sourceHasHorizontalConnection = QueryUtil.existsIndividualsAtPropertyRange(repository.getBaseModel(), repository.getNamespace() + OutputCardSourceId, relationURI , rangeClassURI);
		boolean targetHasHorizontalConnection = QueryUtil.existsIndividualsAtPropertyRange(repository.getBaseModel(), repository.getNamespace() + OutputCardTargetId, relationURI , rangeClassURI);

		if(!sourceHasHorizontalConnection && !targetHasHorizontalConnection){
			//get output connected to the output card
			String propertyURI = RelationEnum.binds_Output_Card_Output.toString();
			List<String> getOutputFromSource = QueryUtil.getIndividualsURIAtObjectPropertyRange(repository.getBaseModel(), repository.getNamespace() + OutputCardSourceId, propertyURI, rangeClassURI);
			List<String> getOutputFromTarget = QueryUtil.getIndividualsURIAtObjectPropertyRange(repository.getBaseModel(), repository.getNamespace() + OutputCardTargetId, propertyURI, rangeClassURI);
			
			//create the reference points between outputs and physical media
			String idReferencePointSource = Util.generateUUID();
			HashSet<String> rpsBetweenPortsSource = PerformBind.discoverRPBetweenPorts( ConceptEnum.Trail_Termination_Function_Output.toString(), ConceptEnum.PM_Input_So.toString(), NOpenComponents.nopenRepository);
			String rpTypeSourceURI = rpsBetweenPortsSource.iterator().next();
			String typeReferencePointSource = rpTypeSourceURI.substring(rpTypeSourceURI.indexOf("#")+1);
			
			FactoryUtil.createInstanceIndividual(
					repository.getBaseModel(), 
					repository.getNamespace() + idReferencePointSource, 
					repository.getNamespace() + typeReferencePointSource,
					true
				);			

			String idReferencePointTarget = Util.generateUUID();
			HashSet<String> rpsBetweenPortsTarget = PerformBind.discoverRPBetweenPorts( ConceptEnum.Trail_Termination_Function_Output.toString(), ConceptEnum.PM_Input_Sk.toString(), NOpenComponents.nopenRepository);
			String rpTypeTargetURI = rpsBetweenPortsTarget.iterator().next();
			String typeReferencePointTarget = rpTypeTargetURI.substring(rpTypeTargetURI.indexOf("#")+1);

			FactoryUtil.createInstanceIndividual(
					repository.getBaseModel(), 
					repository.getNamespace() + idReferencePointTarget, 
					repository.getNamespace() + typeReferencePointTarget,
					true
				);						
			
			String superPropertyURI = repository.getNamespace() + RelationEnum.links_input.toString();
			String sourceClassURI = repository.getNamespace() + ConceptEnum.Trail_Termination_Function_Output.toString();
			String targetClassURI = repository.getNamespace() + ConceptEnum.FEP.toString();
			ArrayList<String> relationOutRp = QueryUtil.getRelationsBetweenClasses(repository.getBaseModel(), sourceClassURI, targetClassURI, superPropertyURI);
			
			//link the output source to the reference point
			FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace() + getOutputFromSource.get(0), 
					repository.getNamespace() + relationOutRp ,
					repository.getNamespace() + idReferencePointSource
				);			
			
			//link the output target to the reference point
			FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace() + getOutputFromTarget.get(0), 
					repository.getNamespace() + relationOutRp ,
					repository.getNamespace() + idReferencePointTarget
				);
			
			
			//create PM Input So
			String idInputSo = Util.generateUUID();
			
			FactoryUtil.createInstanceIndividual(
					repository.getBaseModel(), 
					repository.getNamespace() + idInputSo, 
					repository.getNamespace() + ConceptEnum.PM_Input_So.toString(),
					true
				);						
			
			
			//link the PM Input So with the reference point
			superPropertyURI = repository.getNamespace() + RelationEnum.links_input.toString();
			sourceClassURI = repository.getNamespace() + ConceptEnum.FEP.toString();
			targetClassURI = repository.getNamespace() + ConceptEnum.PM_Input_So.toString();
			
			ArrayList<String> relationRpInputSo = QueryUtil.getRelationsBetweenClasses(repository.getBaseModel(), sourceClassURI, targetClassURI, superPropertyURI);
			
			FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace() + idReferencePointSource, 
					repository.getNamespace() + relationRpInputSo.get(0) ,
					repository.getNamespace() + idInputSo
				);			
			
			//create PM Input Sink
			String idInputSk = Util.generateUUID();
			
			FactoryUtil.createInstanceIndividual(
					repository.getBaseModel(), 
					repository.getNamespace() + idInputSk, 
					repository.getNamespace() + ConceptEnum.PM_Input_Sk.toString(),
					true
				);						
			
			//link the PM Input Sk with the reference point
			superPropertyURI = repository.getNamespace() + RelationEnum.links_input.toString();
			sourceClassURI = repository.getNamespace() + ConceptEnum.FEP.toString();
			targetClassURI = repository.getNamespace() + ConceptEnum.PM_Input_Sk.toString();
			
			ArrayList<String> relationRpInputSk = QueryUtil.getRelationsBetweenClasses(repository.getBaseModel(), sourceClassURI, targetClassURI, superPropertyURI);
			
			FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace() + idReferencePointTarget, 
					repository.getNamespace() + relationRpInputSk.get(0) ,
					repository.getNamespace() + idInputSk
				);			
			
			//create Physical Media

			String idPhysicalMedia = Util.generateUUID();
			
			FactoryUtil.createInstanceIndividual(
					repository.getBaseModel(), 
					repository.getNamespace() + idPhysicalMedia, 
					repository.getNamespace() + ConceptEnum.Physical_Media.toString(),
					true
				);						

			
			//link the physical media with the PM Input So
			
			superPropertyURI = repository.getNamespace() + RelationEnum.componentOf.toString();
			sourceClassURI = repository.getNamespace() + ConceptEnum.Physical_Media.toString();
			targetClassURI = repository.getNamespace() + ConceptEnum.PM_Input_So.toString();
			
			ArrayList<String> relationPM_PMInputSo = QueryUtil.getRelationsBetweenClasses(repository.getBaseModel(), sourceClassURI, targetClassURI, superPropertyURI);
			
			FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace() + idPhysicalMedia, 
					repository.getNamespace() + relationPM_PMInputSo.get(0) ,
					repository.getNamespace() + idInputSo
				);			

			//link the pysical media with the PM Input Sk

			superPropertyURI = repository.getNamespace() + RelationEnum.componentOf.toString();
			sourceClassURI = repository.getNamespace() + ConceptEnum.Physical_Media.toString();
			targetClassURI = repository.getNamespace() + ConceptEnum.PM_Input_Sk.toString();
			
			ArrayList<String> relationPM_PMInputSk = QueryUtil.getRelationsBetweenClasses(repository.getBaseModel(), sourceClassURI, targetClassURI, superPropertyURI);
			
			FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace() + idPhysicalMedia, 
					repository.getNamespace() + relationPM_PMInputSk.get(0) ,
					repository.getNamespace() + idInputSk
				);			

			
			//create the "horizontal links" relation
			propertyURI = RelationEnum.horizontal_links_to_Output_Card_Output_Card.toString();
			
			FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace() + OutputCardSourceId, 
					repository.getNamespace() + propertyURI ,
					repository.getNamespace() + OutputCardTargetId
				);			
			
			return true;
		} else{
			NOpenLog.appendLine("Error: " + OutputCardSourceId + "cannot be connected to " + OutputCardTargetId + "because one of them already is connected");
			throw new Exception("Error: Unexpected connection between " + OutputCardSourceId + "and " + OutputCardTargetId);		
		}
	}
	
	public static boolean verifyPossibleConnects(String OutputCardSourceId, String OutputCardTargetId, OKCoUploader repository) throws Exception {
		boolean hasPossibleConnects = false;
		List<String> relations = QueryUtil.getRelationsBetweenIndividuals(repository.getBaseModel(), OutputCardSourceId, OutputCardTargetId);
		if(relations.contains("possible_connects")){
			hasPossibleConnects = true;
		}
		
		if(hasPossibleConnects){
			return true;
		}
		return false;
	}
	
}
