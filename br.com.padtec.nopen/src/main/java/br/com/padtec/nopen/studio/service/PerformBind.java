package br.com.padtec.nopen.studio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.Util;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.service.NOpenComponents;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class PerformBind {
	private OKCoUploader repository = StudioComponents.studioRepository ;
	private static PerformBind instance = new PerformBind();
	
	/*
	 * this method apply binds between tfs and between ports and tfs. 
	 * creates an output for the id_source 
	 * creates an input for the id_target and the component of 
	 * discover the rp for the binds and the component of
	 * 
	 */
	public static boolean applyBinds(DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement) throws Exception {
			
			StudioComponents.studioRepository.getReasoner().run(StudioComponents.studioRepository.getBaseModel());
		
			String nameSource = dtoSourceElement.getName();
			String typeSource = dtoSourceElement.getType();
			String nameTarget = dtoTargetElement.getName();
			String idSource = dtoSourceElement.getId();
			String idTarget = dtoTargetElement.getId();
			String typeTarget = dtoTargetElement.getType();
			String property =  RelationEnum.binds.toString();

			String typeOutput = typeSource + "_Output";
			String outputId = Util.generateUUID();
			
			String relation_source = null;
			HashMap<String, String> source_componentOfs = new HashMap<String, String>();
			
			String typeInput = typeTarget + "_Input";
			String inputId = Util.generateUUID();
			String relation_target = null;
			HashMap<String, String> target_componentOfs = new HashMap<String, String>();

			if(canCreateBind(dtoSourceElement, dtoTargetElement)){
				
				if(typeTarget.equals(ConceptEnum.Output_Card.toString()) || typeTarget.equals(ConceptEnum.Input_Card.toString())){
					String idPort = Util.generateUUID();
					String typePort = typeSource + "_" + typeTarget.substring(0, typeTarget.indexOf("_"));
					String relation = RelationEnum.componentOf.toString();
					
					//cria portas
					FactoryUtil.createInstanceIndividual(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + idPort, 
							StudioComponents.studioRepository.getNamespace() + typePort,
							true
						);
					
					FactoryUtil.createInstanceIndividual(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + idTarget, 
							StudioComponents.studioRepository.getNamespace() + typeTarget,
							true
						);
					
					FactoryUtil.createInstanceRelation(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + idPort, 
							StudioComponents.studioRepository.getNamespace() + relation,
							StudioComponents.studioRepository.getNamespace() + idSource
						);
					
					
					FactoryUtil.createInstanceRelation(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + idTarget, 
							StudioComponents.studioRepository.getNamespace() + property,
							StudioComponents.studioRepository.getNamespace() + idPort
						);
					
					//create the relation between tf and input/output card
					FactoryUtil.createInstanceRelation(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + idTarget, 
							StudioComponents.studioRepository.getNamespace() + RelationEnum.is_interface_of.toString(),
							StudioComponents.studioRepository.getNamespace() + idSource
					);
					
					return true;
					
				} else {
					//Binds entre TFs 
					//First, verify if the ports can have a RP between them
					//if so, create the ports, RP and the relations between them
					
					HashSet<String> rpsBetweenPorts = discoverRPBetweenPorts( typeOutput, typeInput, NOpenComponents.nopenRepository);
					String rpTypeURI = rpsBetweenPorts.iterator().next();
					String rpType = rpTypeURI.substring(rpTypeURI.indexOf("#")+1);
					String rpId = Util.generateUUID();
					
					String rpTypeInNOpen = NOpenComponents.nopenRepository.getNamespace() + rpType;
					String typeOutputNOpen = NOpenComponents.nopenRepository.getNamespace() + typeOutput;
					String typeInputNOpen = NOpenComponents.nopenRepository.getNamespace() + typeInput;
					String propertyOutputRPInNOpen = NOpenComponents.nopenRepository.getNamespace() + RelationEnum.INV_links_output.toString();
					String propertyInputRPInNOpen = NOpenComponents.nopenRepository.getNamespace() + RelationEnum.links_input.toString();
					
					ArrayList<String> relationOutRpInNOpen = QueryUtil.getRelationsBetweenClasses(NOpenComponents.nopenRepository.getBaseModel(), typeOutputNOpen, rpTypeInNOpen, propertyOutputRPInNOpen);
					ArrayList<String> relationInRpInNOpen = QueryUtil.getRelationsBetweenClasses(NOpenComponents.nopenRepository.getBaseModel(), rpTypeInNOpen, typeInputNOpen, propertyInputRPInNOpen);
					
					String relationOutRp = relationOutRpInNOpen.get(0);
					relationOutRp = relationOutRp.substring(relationOutRp.indexOf("#") + 1);
					
					String relationInRp = relationInRpInNOpen.get(0);
					relationInRp = relationInRp.substring(relationInRp.indexOf("#") + 1);
					
					// create reference point
					FactoryUtil.createInstanceIndividual(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + rpId, 
							StudioComponents.studioRepository.getNamespace() + rpType,
							true
						);
					
					//create output
					FactoryUtil.createInstanceIndividual(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + outputId, 
							StudioComponents.studioRepository.getNamespace() + typeOutput,
							true
						);
	
					//create input
					FactoryUtil.createInstanceIndividual(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + inputId, 
							StudioComponents.studioRepository.getNamespace() + typeInput,
							true
						);
					
					//create relation between output and reference point
					FactoryUtil.createInstanceRelation(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + idSource, 
							StudioComponents.studioRepository.getNamespace() + relationOutRp,
							StudioComponents.studioRepository.getNamespace() + rpId
						);
					
					//create relation between input and reference point
					FactoryUtil.createInstanceRelation(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace() + idTarget, 
							StudioComponents.studioRepository.getNamespace() + relationInRp,
							StudioComponents.studioRepository.getNamespace() + rpId
						);
					
									
					source_componentOfs = NOpenQueryUtil.getAllComponentOFRelations(typeSource, NOpenComponents.nopenRepository.getBaseModel()); 
					target_componentOfs = NOpenQueryUtil.getAllComponentOFRelations(typeTarget, NOpenComponents.nopenRepository.getBaseModel()); 
	
					if ((source_componentOfs.containsKey(NOpenComponents.nopenRepository.getNamespace() + typeSource + "_Output")) && (target_componentOfs.containsKey(NOpenComponents.nopenRepository.getNamespace() + typeTarget + "_Input"))) {
						relation_source = source_componentOfs.get(NOpenComponents.nopenRepository.getNamespace() + typeSource + "_Output");
						relation_target = target_componentOfs.get(NOpenComponents.nopenRepository.getNamespace() + typeTarget + "_Input");
						
						relation_source = relation_source.replace("http://www.menthor.net/nOpenModel.owl", "http://www.menthor.net/nOpenModel_light.owl");
						relation_target = relation_target.replace("http://www.menthor.net/nOpenModel.owl", "http://www.menthor.net/nOpenModel_light.owl");
						
						//create the relation between tf and output
						FactoryUtil.createInstanceRelation( 
								StudioComponents.studioRepository.getBaseModel(), 
								StudioComponents.studioRepository.getNamespace() + idSource, 
								relation_source,
								StudioComponents.studioRepository.getNamespace() + outputId
								);		
						
						NOpenLog.appendLine(StudioComponents.studioRepository.getName()+":  Output "+outputId+" created at "+ typeSource + ": "+nameSource);
						
						
						//create the relation between tf and input
						FactoryUtil.createInstanceRelation(
								StudioComponents.studioRepository.getBaseModel(), 
								StudioComponents.studioRepository.getNamespace() + idTarget, 
								relation_target,
								StudioComponents.studioRepository.getNamespace() + inputId
						);
						
//						StudioComponents.studioRepository.getReasoner().run(StudioComponents.studioRepository.getBaseModel());
						
						NOpenLog.appendLine(StudioComponents.studioRepository.getName()+":  Input "+inputId+" created at "+ typeTarget + ": "+nameTarget);
	
						NOpenLog.appendLine("Success: Binds successfully made between (" + typeSource + "::" + nameSource +", " + typeTarget + "::" + nameTarget + ")");
	
						return true;
					}	
				}
			}else {
				NOpenLog.appendLine("Error: The Transport Function " + nameSource + "cannot be bound to " + nameTarget);
				throw new Exception("Error: Unexpected bind between " + nameSource + "and " + nameTarget);
			}
		return false;

	}
	
	/*
	 * given two ports discover the rp between them.
	 */
	static HashSet<String> discoverRPBetweenPorts(String type_output, String type_input, OKCoUploader repository){
		HashSet<String> rp = new HashSet<String>();
		rp = NOpenQueryUtil.discoverRPBetweenPorts(type_output, type_input, repository.getBaseModel());
		
		return rp;
	}
	
	
	/*
	 * verify if the source's layer is client of the target's layer.
	 */
	static boolean isClient(String sourceURI, String targetURI, OKCoUploader repository){ 
		//StudioComponents.studioRepository.getReasoner().run(StudioComponents.studioRepository.getBaseModel());
		String tgtClassURI = repository.getNamespace() + ConceptEnum.Card_Layer.toString();
		String relationSourceURI = repository.getNamespace() + RelationEnum.intermediates_up_Transport_Function_Card_Layer.toString();
		String relationTargetURI = repository.getNamespace() + RelationEnum.intermediates_down_Transport_Function_Card_Layer.toString();
		
		//verifica se o tf_source tem a relação de intermediates_up e se o tf_target tem a relação de intermediates_down
		boolean tfSourceHasIntermediatesUpRelation = QueryUtil.hasTargetIndividualFromClass(repository.getBaseModel(), sourceURI, relationSourceURI, tgtClassURI );
		
		boolean tfTargetHasIntermediatesDownRelation = QueryUtil.hasTargetIndividualFromClass(repository.getBaseModel(), targetURI, relationTargetURI, tgtClassURI );
		
		if(!tfSourceHasIntermediatesUpRelation || !tfTargetHasIntermediatesDownRelation){
			return true;
		}
		else if(tfSourceHasIntermediatesUpRelation && tfTargetHasIntermediatesDownRelation){
			//pega o card_layer do tf_source e o card_layer do tf_target
			String cardLayerUpSource = QueryUtil.getIndividualsURIAtPropertyRange(repository.getBaseModel(), sourceURI, relationSourceURI).get(0);
			String cardLayerDownTarget = QueryUtil.getIndividualsURIAtPropertyRange(repository.getBaseModel(), targetURI, relationTargetURI).get(0);
			
			//pega a camada do card_layer do tf_source e a camada do card_layer do tf_target
			String layerUpSource = QueryUtil.getIndividualsURIAtPropertyRange(repository.getBaseModel(), cardLayerUpSource, repository.getNamespace() + RelationEnum.instantiates_Card_Layer_Layer_Type.toString()).get(0);
			String layerDownTarget = QueryUtil.getIndividualsURIAtPropertyRange(repository.getBaseModel(), cardLayerDownTarget, repository.getNamespace() + RelationEnum.instantiates_Card_Layer_Layer_Type.toString()).get(0);
			
			//pega as relações entre as camadas
			ArrayList<String> relationsBetweenLayerSourceAndLayerTarget = new ArrayList<String>();
			relationsBetweenLayerSourceAndLayerTarget = QueryUtil.getRelationsBetweenIndividuals(repository.getBaseModel(), layerUpSource, layerDownTarget);

			//se entre a camada do tf_source e a camada do tf_target existir a relação de is_client, então retorna true
			if(relationsBetweenLayerSourceAndLayerTarget.contains(repository.getNamespace() + RelationEnum.is_client_Layer_Type_Layer_Type.toString())){
				return true;
			}
		}
		return false;
	}
	
	public static boolean canCreateBind(DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement ) throws Exception{
		String nameSource = dtoSourceElement.getName();
		String typeSource = dtoSourceElement.getType();
		String idSource = dtoSourceElement.getId();
		String nameTarget = dtoTargetElement.getName();
		String typeTarget = dtoTargetElement.getType();
		String idTarget = dtoTargetElement.getId();
		String sourceURI = StudioComponents.studioRepository.getNamespace() + dtoSourceElement.getId();
		String targetURI = StudioComponents.studioRepository.getNamespace() + dtoTargetElement.getId();
		
		if(typeTarget.equals(ConceptEnum.Output_Card.toString()) || typeTarget.equals(ConceptEnum.Input_Card.toString())){ //relação entre tf e porta de card
			String property = RelationEnum.binds.toString();
			String propertyURI = StudioComponents.studioRepository.getNamespace() + property;
			String typePort = typeSource + "_" + typeTarget.substring(0, typeTarget.indexOf("_"));

			String key = NOpenComponents.nopenRepository.getNamespace() + typeTarget + NOpenComponents.nopenRepository.getNamespace() + property + NOpenComponents.nopenRepository.getNamespace() + typePort; 
			String cardinality = BuildBindStructure.getInstance().getBindsTuple().get(key);
			if(cardinality == null){
				NOpenLog.appendLine("Error: The Transport Function " + nameSource + " cannot be bound to " + nameTarget + " because the relation between " + dtoSourceElement.getType() + " and " + dtoTargetElement.getType() + " does not exist.");
				throw new Exception("Error: Unexpected relation between " + nameSource + " and " + nameTarget + " because there is no \"binds\" relation between " + typeSource + " and " + typeTarget);
			}else{
				Integer numberOfAlreadyBoundPorts = QueryUtil.getNumberOfOccurrences(StudioComponents.studioRepository.getBaseModel(), targetURI, propertyURI, typeSource );
				Integer cardinalityInputTarget = Integer.parseInt(cardinality);
				if(cardinalityInputTarget == -1 || numberOfAlreadyBoundPorts <= cardinalityInputTarget){
					return true;

				}

			}
		}
		else{ //então, o binds é entre TFs

			String typeOutput = typeSource + "_Output";
			String typeInput = typeTarget + "_Input";
			String property = RelationEnum.binds.toString();
			String propertyURI = StudioComponents.studioRepository.getNamespace() + property;
			Integer numberOfAlreadyBoundPorts = QueryUtil.getNumberOfOccurrences(StudioComponents.studioRepository.getBaseModel(), sourceURI, propertyURI, typeTarget );
			String key = NOpenComponents.nopenRepository.getNamespace() + typeSource + NOpenComponents.nopenRepository.getNamespace() + property + NOpenComponents.nopenRepository.getNamespace() + typeTarget; 
			String cardinality = BuildBindStructure.getInstance().getBindsTuple().get(key);
			if(cardinality == null){
				NOpenLog.appendLine("Error: The Transport Function " + nameSource + " cannot be bound to " + nameTarget + " because the relation between " + dtoSourceElement.getType() + " and " + dtoTargetElement.getType() + " does not exist.");
				throw new Exception("Error: Unexpected relation between " + nameSource + " and " + nameTarget + " because there is no \"binds\" relation between " + typeSource + " and " + typeTarget);
			}
			Integer cardinalityInputTarget = Integer.parseInt(cardinality);
			
			HashSet<String> rpsBetweenPorts = new HashSet<String>();
			rpsBetweenPorts = discoverRPBetweenPorts( typeOutput, typeInput, NOpenComponents.nopenRepository);
			boolean isClient = false;
			isClient = isClient(idSource, idTarget, StudioComponents.studioRepository);
			if(rpsBetweenPorts.size() > 0){
				if((numberOfAlreadyBoundPorts < cardinalityInputTarget) || (cardinalityInputTarget == -1)){
					if(isClient){
						return true;
					} else{
						NOpenLog.appendLine("Error: The Transport Function " + nameSource + " cannot be bound to " + nameTarget + " because the source layer is not client of target layer.");
						throw new Exception("Error: Unexpected relation between " + nameSource + " and " + nameTarget + " because the source layer is not client of target layer.");
					}
				} else {
					NOpenLog.appendLine("Error: The Transport Function " + nameSource + " cannot be bound to " + nameTarget + " because the cardinality of the relation is already maximum.");
					throw new Exception("Error: Unexpected relation between " + nameSource + " and " + nameTarget + " because the cardinality of the relation is already maximum.");
				}
			}
			else{
				NOpenLog.appendLine("Error: The Transport Function " + nameSource + " cannot be bound to " + nameTarget + " because there is no Reference Point between " + dtoSourceElement.getType() + " and " + dtoTargetElement.getType() + " . ");
				throw new Exception("Error: Unexpected relation between " + nameSource + " and " + nameTarget + "  because there is no Reference Point between " + dtoSourceElement.getType() + " and " + dtoTargetElement.getType() + " . ");
			}
		}
		return false;
		
	}
	
	public static void applyEquipmentBinds(DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement) throws Exception{
		String rangeClassName = ConceptEnum.Transport_Function.toString();
		String sourceIndividualId = dtoSourceElement.getId();
		String property = RelationEnum.is_interface_of.toString();
		String targetIndividualId = dtoTargetElement.getId();
		String[] tfSource = NOpenQueryUtil.getIndividualsNamesAtObjectPropertyRange(instance.repository.getBaseModel(), sourceIndividualId, property, rangeClassName);
		String[] tfTarget = NOpenQueryUtil.getIndividualsNamesAtObjectPropertyRange(instance.repository.getBaseModel(), targetIndividualId, property, rangeClassName);
		System.out.println();
		if(tfSource == null || tfTarget == null){
			NOpenLog.appendLine("Error: The Transport Function " + dtoSourceElement.getName() + " cannot be bound to " + dtoTargetElement.getName() + "because the equipments are not defined in ITUStudio.");
			throw new Exception("Error: Unexpected relation between " + dtoSourceElement.getName() + " and " + dtoTargetElement.getName() + "because the equipments are not defined in ITUStudio. ");
		}
		DtoJointElement newSource = new DtoJointElement();
		newSource.setId(Util.generateUUID());
		newSource.setName(tfSource[0]);
		newSource.setType(rangeClassName);
		DtoJointElement newTarget = new DtoJointElement();
		newTarget.setId(Util.generateUUID());
		newTarget.setName(tfTarget[0]);
		newTarget.setType(rangeClassName);
		System.out.println();
		applyBinds(newSource, newTarget);
	}
	
	public static boolean canCreateEquipmentBinds(DtoJointElement dtoSourceElement, DtoJointElement dtoTargetElement) throws Exception{
		String rangeClassName = ConceptEnum.Transport_Function.toString();
		String sourceIndividualId = dtoSourceElement.getId();
		String property = RelationEnum.is_interface_of.toString();
		String targetIndividualId = dtoTargetElement.getId();
		String[] tfSource = NOpenQueryUtil.getIndividualsNamesAtObjectPropertyRange(instance.repository.getBaseModel(), sourceIndividualId, property, rangeClassName);
		String[] tfTarget = NOpenQueryUtil.getIndividualsNamesAtObjectPropertyRange(instance.repository.getBaseModel(), targetIndividualId, property, rangeClassName);
		if(tfSource == null || tfTarget == null){
			NOpenLog.appendLine("Error: The Transport Function " + dtoSourceElement.getName() + " cannot be bound to " + dtoTargetElement.getName() + "because the equipments are not defined in ITUStudio.");
			throw new Exception("Error: Unexpected relation between " + dtoSourceElement.getName() + " and " + dtoTargetElement.getName() + "because the equipments are not defined in ITUStudio. ");
		}
		System.out.println();
		DtoJointElement newSource = new DtoJointElement();
		newSource.setId(Util.generateUUID());
		newSource.setName(tfSource[0]);
		newSource.setType(rangeClassName);
		DtoJointElement newTarget = new DtoJointElement();
		newTarget.setId(Util.generateUUID());
		newTarget.setName(tfTarget[0]);
		newTarget.setType(rangeClassName);
		System.out.println();
		boolean result = canCreateBind(newSource, newTarget);
		return result;
		
	}

	public OKCoUploader getRepository() {
		return repository;
	}

	public void setRepository(OKCoUploader repository) {
		this.repository = repository;
	}

	public static PerformBind getInstance(){
		return instance;
	}
	
}
