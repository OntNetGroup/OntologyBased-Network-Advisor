package br.com.padtec.nopen.studio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;



public class PerformBind {
	private OKCoUploader repository = StudioComponents.studioRepository ;
	private static PerformBind instance=new PerformBind();
	
	public static PerformBind getInstance(){
		return instance;
	}
	
	
	/*
	 * this method apply binds between tfs and between ports and tfs. 
	 * creates an output for the id_source 
	 * creates an input for the id_target and the component of 
	 * discover the rp for the binds and the component of
	 * 
	 */
	public boolean applyBinds(String id_source, String name_source, String id_target, String name_target, String tipo_source, String tipo_target, OKCoUploader repository){
		try{
			
			//First, verify if the ports can have a RP between them
			//if so, create the ports, RP and the relations between them
			
			String outputId = id_source + "_Output";
			String tipo_output = tipo_source + "_Output";
			String relation_source = null;
			HashMap<String, String> source_componentOfs = new HashMap<String, String>();
			
			String inputId = tipo_source + "_Input" + id_source;
			String tipo_input = tipo_source + "_Input";
			String relation_target = null;
			HashMap<String, String> target_componentOfs = new HashMap<String, String>();
			
			
			//create the Reference Point if exists and the relation between reference point and ports
			HashSet<String> rps_between_ports = new HashSet<String>();
			rps_between_ports = discoverRPBetweenPorts( tipo_output, tipo_input, repository);

			if(rps_between_ports.size() > 0){
				String tipo_rp;
				String rp_name;
				tipo_rp = rps_between_ports.iterator().next();
				rp_name = tipo_rp + "_" + tipo_output + "_" + tipo_input;
				String rpId = tipo_rp + rp_name;
				
				ArrayList<String> relationOutRp = QueryUtil.getRelationsBetweenClasses(repository.getBaseModel(), tipo_output, tipo_rp, RelationEnum.INV_links_output.toString());
				ArrayList<String> relationInRp = QueryUtil.getRelationsBetweenClasses(repository.getBaseModel(), tipo_rp, tipo_input, RelationEnum.links_input.toString());
				
				// create reference point
				FactoryUtil.createInstanceIndividual(
						repository.getBaseModel(), 
						repository.getNamespace()+rp_name, 
						repository.getNamespace()+tipo_rp
					);
				
				//create output
				FactoryUtil.createInstanceIndividual(
						repository.getBaseModel(), 
						repository.getNamespace()+outputId, 
						tipo_output
					);

				//create input
				FactoryUtil.createInstanceIndividual(
						repository.getBaseModel(), 
						repository.getNamespace()+inputId, 
						repository.getNamespace()+tipo_input
					);
				
				//create relation between output and reference point
				FactoryUtil.createInstanceRelation(
						repository.getBaseModel(), 
						repository.getNamespace()+id_source, 
						repository.getNamespace()+relationOutRp.get(0),
						repository.getNamespace()+rpId
					);
				
				//create relation between input and reference point
				FactoryUtil.createInstanceRelation(
						repository.getBaseModel(), 
						repository.getNamespace()+id_target, 
						repository.getNamespace()+relationInRp.get(0),
						repository.getNamespace()+rpId
					);
				
								
				source_componentOfs = NOpenQueryUtil.getAllComponentOFRelations(tipo_source, repository.getBaseModel());
				if (source_componentOfs.containsKey(tipo_source + "_Output")) {
					relation_source = source_componentOfs.get(tipo_source + "_Output");
					
					//create the relation between tf and output
					FactoryUtil.createInstanceRelation( 
							repository.getBaseModel(), 
							repository.getNamespace()+id_source, 
							relation_source,
							repository.getNamespace()+outputId
						);		
						
						NOpenLog.appendLine(repository.getName()+":  Output "+outputId+" created at "+ tipo_source + ": "+name_source);
					
					
				}
				
				
				
				
				//create the relation between tf and input
				target_componentOfs = NOpenQueryUtil.getAllComponentOFRelations(tipo_target, repository.getBaseModel());
				/*for(String c : target_componentOfs){
					if(c.equals(tipo_target+"_Input")){
						relation_target = c;
					}
				}*/
				FactoryUtil.createInstanceRelation(
					repository.getBaseModel(), 
					repository.getNamespace()+id_target, 
					repository.getNamespace()+relation_target,
					repository.getNamespace()+inputId
				);	
				
				NOpenLog.appendLine(repository.getName()+":  Input "+inputId+" created at "+ tipo_target + ": "+name_target);

				NOpenLog.appendLine("Success: Binds successfully made between (" + tipo_source + "::" + name_source +", " + tipo_target + "::" + name_target + ")");
			}else {
				
			}
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public OKCoUploader getRepository() {
		return repository;
	}

	public void setRepository(OKCoUploader repository) {
		this.repository = repository;
	}
	
	/*
	 * given two ports discover the rp between them.
	 */
	static HashSet<String> discoverRPBetweenPorts(String uri_type_output, String uri_type_input, OKCoUploader repository){
		HashSet<String> rp = new HashSet<String>();
		rp = NOpenQueryUtil.discoverRPBetweenPorts(uri_type_output, uri_type_input, repository.getBaseModel());
		
		return rp;
	}

	
}
