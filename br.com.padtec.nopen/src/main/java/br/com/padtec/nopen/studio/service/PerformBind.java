package br.com.padtec.nopen.studio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.RelationEnum;
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
	public static boolean applyBinds(DtoJointElement dtoContainer, DtoJointElement dtoContent){
		try{
			String sourceURI = StudioComponents.studioRepository.getNamespace() + dtoContainer.getId();
			String name_source = dtoContainer.getName();
			String tipo_source = StudioComponents.studioRepository.getNamespace() + dtoContainer.getType();
			String targetURI = StudioComponents.studioRepository.getNamespace() + dtoContent.getId();
			String name_target = dtoContent.getName();
			String tipo_target = StudioComponents.studioRepository.getNamespace() + dtoContent.getType();
			String propertyURI = StudioComponents.studioRepository.getNamespace() + RelationEnum.binds.toString();
			String id_source = dtoContainer.getId();
			String id_target = dtoContent.getId();
			
			//First, verify if the ports can have a RP between them
			//if so, create the ports, RP and the relations between them
			StudioComponents.studioRepository.getReasoner().run(StudioComponents.studioRepository.getBaseModel());
			String outputId = id_source + "_Output";
			String tipo_output = tipo_source + "_Output";
			String relation_source = null;
			HashMap<String, String> source_componentOfs = new HashMap<String, String>();
			
			String inputId = id_target + "_Input" ;
			String tipo_input = tipo_target + "_Input";
			String relation_target = null;
			HashMap<String, String> target_componentOfs = new HashMap<String, String>();

			if(canCreateBind(dtoContainer, dtoContent)){
				HashSet<String> rps_between_ports = new HashSet<String>();
				rps_between_ports = discoverRPBetweenPorts( tipo_output, tipo_input, StudioComponents.studioRepository);
				String tipo_rp;
				String rp_name;
				tipo_rp = rps_between_ports.iterator().next();
				rp_name = tipo_rp + "_" + tipo_output.substring(tipo_output.indexOf("#")+1) + "_" + tipo_input.substring(tipo_input.indexOf("#")+1);
				String rpId = rp_name;
				
				ArrayList<String> relationOutRp = QueryUtil.getRelationsBetweenClasses(StudioComponents.studioRepository.getBaseModel(), tipo_output, tipo_rp, StudioComponents.studioRepository.getNamespace()+RelationEnum.INV_links_output.toString());
				ArrayList<String> relationInRp = QueryUtil.getRelationsBetweenClasses(StudioComponents.studioRepository.getBaseModel(), tipo_rp, tipo_input, StudioComponents.studioRepository.getNamespace()+RelationEnum.links_input.toString());
				
				// create reference point
				FactoryUtil.createInstanceIndividual(
						StudioComponents.studioRepository.getBaseModel(), 
						rp_name, 
						tipo_rp,
						true
					);
				
				//create output
				FactoryUtil.createInstanceIndividual(
						StudioComponents.studioRepository.getBaseModel(), 
						StudioComponents.studioRepository.getNamespace()+outputId, 
						tipo_output,
						true
					);

				//create input
				FactoryUtil.createInstanceIndividual(
						StudioComponents.studioRepository.getBaseModel(), 
						StudioComponents.studioRepository.getNamespace()+inputId, 
						tipo_input,
						true
					);
				
				//create relation between output and reference point
				FactoryUtil.createInstanceRelation(
						StudioComponents.studioRepository.getBaseModel(), 
						StudioComponents.studioRepository.getNamespace()+id_source, 
						relationOutRp.get(0),
						rpId
					);
				
				//create relation between input and reference point
				FactoryUtil.createInstanceRelation(
						StudioComponents.studioRepository.getBaseModel(), 
						StudioComponents.studioRepository.getNamespace()+id_target, 
						relationInRp.get(0),
						rpId
					);
				
								
				source_componentOfs = NOpenQueryUtil.getAllComponentOFRelations(tipo_source, StudioComponents.studioRepository.getBaseModel()); 
				target_componentOfs = NOpenQueryUtil.getAllComponentOFRelations(tipo_target, StudioComponents.studioRepository.getBaseModel()); 

				if ((source_componentOfs.containsKey(tipo_source + "_Output")) && (target_componentOfs.containsKey(tipo_target + "_Input"))) {
					relation_source = source_componentOfs.get(tipo_source + "_Output");
					relation_target = target_componentOfs.get(tipo_target + "_Input");
					//create the relation between tf and output
					FactoryUtil.createInstanceRelation( 
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace()+id_source, 
							relation_source,
							StudioComponents.studioRepository.getNamespace()+outputId
							);		
					
					NOpenLog.appendLine(StudioComponents.studioRepository.getName()+":  Output "+outputId+" created at "+ tipo_source + ": "+name_source);
					
					
					//create the relation between tf and input
					FactoryUtil.createInstanceRelation(
							StudioComponents.studioRepository.getBaseModel(), 
							StudioComponents.studioRepository.getNamespace()+id_target, 
							StudioComponents.studioRepository.getNamespace()+relation_target,
						StudioComponents.studioRepository.getNamespace()+inputId
					);
					
					StudioComponents.studioRepository.getReasoner().run(StudioComponents.studioRepository.getBaseModel());
					
					NOpenLog.appendLine(StudioComponents.studioRepository.getName()+":  Input "+inputId+" created at "+ tipo_target + ": "+name_target);

					NOpenLog.appendLine("Success: Binds successfully made between (" + tipo_source + "::" + name_source +", " + tipo_target + "::" + name_target + ")");

					return true;
				}	

			}else {
				NOpenLog.appendLine("Error: The Transport Function " + name_source + "cannot be bound to " + name_target);
				throw new Exception("Error: Unexpected bind between " + name_source + "and " + name_target);
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
	
		return false;

	}
	
	/*
	 * given two ports discover the rp between them.
	 */
	static HashSet<String> discoverRPBetweenPorts(String uri_type_output, String uri_type_input, OKCoUploader repository){
		HashSet<String> rp = new HashSet<String>();
		rp = NOpenQueryUtil.discoverRPBetweenPorts(uri_type_output, uri_type_input, repository.getBaseModel());
		
		return rp;
	}
	
	
	/*
	 * verify if the source's layer is client of the target's layer.
	 */
	static boolean isClient(String sourceURI, String targetURI, OKCoUploader repository){ 
		StudioComponents.studioRepository.getReasoner().run(StudioComponents.studioRepository.getBaseModel());
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
	
	public static boolean canCreateBind(DtoJointElement dtoContainer, DtoJointElement dtoContent/*String id_source, String name_source, String id_target, String name_target, String tipo_source, String tipo_target, OKCoUploader repository*/ ){
		String sourceURI = StudioComponents.studioRepository.getNamespace() + dtoContainer.getId();
		String name_source = dtoContainer.getName();
		String tipo_source = StudioComponents.studioRepository.getNamespace() + dtoContainer.getType();
		String targetURI = StudioComponents.studioRepository.getNamespace() + dtoContent.getId();
		String name_target = dtoContent.getName();
		String tipo_target = StudioComponents.studioRepository.getNamespace() + dtoContent.getType();
		
		String id_source = dtoContainer.getId();
		String id_target = dtoContent.getId();
		
		String tipo_output = tipo_source + "_Output";
		String tipo_input = tipo_target + "_Input";
		
		String propertyURI = StudioComponents.studioRepository.getNamespace() + RelationEnum.binds.toString();
		Integer numberOfAlreadyBoundPorts = QueryUtil.getNumberOfOccurrences(StudioComponents.studioRepository.getBaseModel(), name_source, propertyURI, tipo_target );
			
		String key = tipo_source + propertyURI + tipo_target;
		String cardinality = BuildBindStructure.getInstance().getBindsTuple().get(key);
		if(cardinality == null){
			NOpenLog.appendLine("Error: The Transport Function " + name_source + " cannot be bound to " + name_target + " because the relation between " + dtoContainer.getType() + " and " + dtoContent.getType() + " does not exist.");
			return false;
		}
		Integer cardinality_input_target = Integer.parseInt(cardinality);
		//create the Reference Point if exists and the relation between reference point and ports
		HashSet<String> rps_between_ports = new HashSet<String>();
		rps_between_ports = discoverRPBetweenPorts( tipo_output, tipo_input, StudioComponents.studioRepository);
		boolean isClient = false;
		isClient = isClient(id_source, id_target, StudioComponents.studioRepository);
		if(rps_between_ports.size() > 0){
			if((numberOfAlreadyBoundPorts < cardinality_input_target) || (cardinality_input_target == -1)){
				if(isClient){
					return true;
				} else{
					NOpenLog.appendLine("Error: The Transport Function " + name_source + " cannot be bound to " + name_target + " because the source layer is not client of target layer.");
					return false;
				}
			} else {
				NOpenLog.appendLine("Error: The Transport Function " + name_source + " cannot be bound to " + name_target + " because the cardinality of the relation is already maximum.");
				return false;
			}
		}
		else{
			NOpenLog.appendLine("Error: The Transport Function " + name_source + " cannot be bound to " + name_target + " because there is no Reference Point between " + dtoContainer.getType() + " and " + dtoContent.getType() + " . ");
			return false;
		}
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
