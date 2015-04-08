package br.com.padtec.advisor.core.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.com.padtec.advisor.core.dto.DtoResultAjax;
import br.com.padtec.advisor.core.queries.AdvisorDtoQueryUtil;
import br.com.padtec.advisor.core.types.ConceptEnum;
import br.com.padtec.advisor.core.types.RelationEnum;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.transformation.sindel.processor.BindsProcessor;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Statement;

public class GeneralBinds extends AdvisorService {
	
	public PossibleBindsMap possibleBinds = new PossibleBindsMap();
		
	public GeneralBinds(OKCoUploader repository)
	{
		super(repository);			
	}
	
	/** Provisioning Binds */
	public DtoResultAjax provisioningBinds(String outInt, String inInt, Boolean updateListsInTheEnd, ArrayList<String> listInstancesCreated) 
	{
		DtoResultAjax dto = new DtoResultAjax();
		String namespace = repository.getNamespace();
		String outputNs = new String();
		String inputNs = new String();		
		
		List<DtoInstanceRelation> outIntRelations = DtoQueryUtil.getRelationsFrom(repository.getInferredModel(), namespace+outInt);
		List<DtoInstanceRelation> inIntRelations = DtoQueryUtil.getRelationsFrom(repository.getInferredModel(), namespace+inInt);		
		for (DtoInstanceRelation outIntRelation : outIntRelations) 
		{
			if(outIntRelation.Property.equalsIgnoreCase(namespace+RelationEnum.MAPS_OUTPUT))
			{
				outputNs = outIntRelation.Target;
				outputNs = outputNs.replace(namespace, "");
				break;
			}
		}				
		for (DtoInstanceRelation inIntRelation : inIntRelations) {
			if(inIntRelation.Property.equalsIgnoreCase(namespace+RelationEnum.MAPS_INPUT))
			{
				inputNs = inIntRelation.Target;
				inputNs = inputNs.replace(namespace, "");
				break;
			}
		}
		
		Boolean inputIsPmInput = false;
		Boolean outputIsPmOutput = false;
		if(inputNs.equals(""))
		{
			DtoInstance inputOrIntInput = DtoQueryUtil.getIndividualByName(repository.getInferredModel(),namespace+inInt, true, false, false);
			for (String inputOrIntInputClass : inputOrIntInput.ListClasses) 
			{
				if(inputOrIntInputClass.equals(namespace+ConceptEnum.PHYSICAL_MEDIA_INPUT))
				{
					inputNs = inInt;
					inputIsPmInput = true;
					break;
				}
			}
		}		
		if(outputNs.equals(""))
		{
			DtoInstance outputOrIntOutput = DtoQueryUtil.getIndividualByName(repository.getInferredModel(),namespace+outInt, true, false, false);
			for (String outputOrIntOutputClass : outputOrIntOutput.ListClasses) 
			{
				if(outputOrIntOutputClass.equals(namespace+ConceptEnum.PHYSICAL_MEDIA_OUTPUT))
				{
					outputNs = outInt;
					outputIsPmOutput = true;
					break;
				}
			}
		}
		
		/** binds Interface out with in */
		Individual a, b;
		ObjectProperty rel = null;
		if(inputIsPmInput && !outputIsPmOutput)
		{
			a = repository.getBaseModel().getIndividual(namespace+outInt);
			b = repository.getBaseModel().getIndividual(namespace+inputNs);
			rel = repository.getBaseModel().getObjectProperty(namespace+RelationEnum.BINDS_PM_OUT_INTERFACE);
		}
		else if(!inputIsPmInput && outputIsPmOutput)
		{
			a = repository.getBaseModel().getIndividual(namespace+inInt);
			b = repository.getBaseModel().getIndividual(namespace+outputNs);
			rel = repository.getBaseModel().getObjectProperty(namespace+RelationEnum.BINDS_PM_IN_INTERFACE);
		}
		else
		{
			a = repository.getBaseModel().getIndividual(namespace+outInt);
			b = repository.getBaseModel().getIndividual(namespace+inInt);
			rel = repository.getBaseModel().getObjectProperty(namespace+RelationEnum.INTERFACE_BINDS);
		}		
		Statement stmt = repository.getBaseModel().createStatement(a, rel, b);
		repository.getBaseModel().add(stmt);

		if(listInstancesCreated == null) listInstancesCreated = new ArrayList<String>();		
		listInstancesCreated.add(a.getNameSpace()+a.getLocalName());
		listInstancesCreated.add(b.getNameSpace()+b.getLocalName());
		
		if(!outputNs.equals("") && !inputNs.equals(""))
		{			
			a = repository.getBaseModel().getIndividual(namespace+outputNs);
			b = repository.getBaseModel().getIndividual(namespace+inputNs);
			
			BindsProcessor.bindPorts(null, a, b, null, namespace, repository.getBaseModel(), listInstancesCreated);
		}

		repository.substituteInferredModelFromBaseModel(false);		
		dto.ok = true;
		dto.result = "ok";
		return dto;

	}
	
	/** Filer Allowed Interfaces with Physical Media */
	private void filterAllowedInterfacesWithPhysicalMedia(String outputClassName,  List<String> allowedInputInterfaces, String eqOutNs, Boolean outputInterfaceAlreadyBinded)
	{
		String namespace = repository.getNamespace();	
		List<DtoInstance> outputs = AdvisorDtoQueryUtil.getIndividualsFromClass(ConceptEnum.OUTPUT);
		List<DtoInstance> physicalMediaInputs = AdvisorDtoQueryUtil.getIndividualsFromClass(ConceptEnum.PHYSICAL_MEDIA_INPUT);
		
		/** here, I look for possible connections with physical media inputs */
		for (DtoInstance dtoPmInput : physicalMediaInputs) 
		{
			List<DtoInstanceRelation> inPMRelations = AdvisorDtoQueryUtil.getRelationsFrom(dtoPmInput);
			String pmNs = new String();
			Boolean inputPMAlreadyConnected = false;
			
			/** get name spaces of individuals of some input interface relations */
			for (DtoInstanceRelation inRelation : inPMRelations) 
			{
				if(inRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_COMPONENTOF_SINGLE_PHYSICAL_MEDIA_PHYSICAL_MEDIA_INPUT) || inRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_COMPONENTOF))
				{
					pmNs = inRelation.Target;
				}else if(inRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_BINDS_PM_OUT_INTERFACE))
				{
					inputPMAlreadyConnected = true;
				}
			}
			
			/** since I verify the inverse relation of binds_PM_out_interface above, 
			    it's necessary to verify if some output port has the binds_PM_out_interface relation
			    with the actual PM input
			    the block below it's for this purpose */
			if(!inputPMAlreadyConnected)
			{
				for(DtoInstance dtoOtherOutput : outputs)
				{
					List<DtoInstanceRelation> otherOutputRelations = AdvisorDtoQueryUtil.getRelationsFrom(dtoOtherOutput);
					for (DtoInstanceRelation dtoOtherOutputRelation : otherOutputRelations) 
					{
						if(dtoOtherOutputRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INTERFACE_BINDS) || dtoOtherOutputRelation.Property.equalsIgnoreCase(namespace+RelationEnum.BINDS))
						{
							if((dtoPmInput.ns+dtoPmInput.name).equals(dtoOtherOutputRelation.Target))
							{
								inputPMAlreadyConnected = true;
								break;
							}
						}
					}
					if(inputPMAlreadyConnected) break;
				}
			}
			
			Boolean hasAllowedRelation = false;
			
			/**for each input and output class names, I verify if exist a possible relation of binds */
			for(String pmInputClassName : dtoPmInput.ListClasses)
			{
				pmInputClassName = pmInputClassName.replace(namespace, "");
				
				HashMap<String, String> tf1 = new HashMap<String, String>();
				tf1.put("INPUT", pmInputClassName);
				tf1.put("OUTPUT", outputClassName);
				HashMap<String, String> allowedRelation = BindsProcessor.allowedRelationsHash.get(tf1);
				if(allowedRelation != null)
				{
					hasAllowedRelation = true;
					break;
				}
			}				
			String interfaceReturn = "";
			pmNs = pmNs.replace(dtoPmInput.ns, "");
			eqOutNs = eqOutNs.replace(namespace, "");
			interfaceReturn += pmNs; 
			interfaceReturn += "#";
			interfaceReturn += dtoPmInput.name;
			interfaceReturn += "#";
			
			if(hasAllowedRelation && !pmNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputPMAlreadyConnected)
			{
				if(allowedInputInterfaces.contains(interfaceReturn+"false;")) allowedInputInterfaces.remove(interfaceReturn+"false;");					
				interfaceReturn += "true;";
			}else{
				if(!allowedInputInterfaces.contains(interfaceReturn.replace("true;", "false;"))) interfaceReturn += "false;";										
			}

			if(!allowedInputInterfaces.contains(interfaceReturn) && !allowedInputInterfaces.contains(interfaceReturn.replace("false;", "true;")))
			{
				allowedInputInterfaces.add(interfaceReturn);
			}	
		}
	}
	
	/** Filer Allowed Interfaces with Input Interfaces */
	private void filterAllowedInterfacesWithInputInterfaces(String outputClassName, List<String> allowedInputInterfaces, List<DtoInstance> inputInterfaces , String eqOutNs, Boolean outputInterfaceAlreadyBinded)
	{
		String namespace = repository.getNamespace();	
		List<DtoInstance> outputInterfaces = AdvisorDtoQueryUtil.getIndividualsFromClass(ConceptEnum.OUTPUT_INTERFACE);
		
		/** here, I look for possible connections with input interfaces */
		for (DtoInstance dtoInputInterface : inputInterfaces) 
		{
			List<DtoInstanceRelation> inIntRelations = AdvisorDtoQueryUtil.getRelationsFrom(dtoInputInterface);
			String inputNs = new String();	
			String eqInNs = new String();	
			Boolean inputInterfaceAlreadyConnected = false;	
			
			/** get name spaces of individuals of some input interface relations */
			for (DtoInstanceRelation inRelation : inIntRelations) 
			{
				if(inRelation.Property.equalsIgnoreCase(namespace+RelationEnum.MAPS_INPUT)) inputNs = inRelation.Target;
				else if(inRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_COMPONENTOF)) eqInNs = inRelation.Target;						
				else if(inRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_INTERFACE_BINDS)) inputInterfaceAlreadyConnected = true;					
			}
			
			/** since I verify the inverse relation of interface_binds above, 
			    it's necessary to verify if some output interface has the interface_binds relation
			    with the actual input interface
			    the block below it's for this purpose */
			if(!inputInterfaceAlreadyConnected)
			{
				for (DtoInstance dtoOtherOutputInt : outputInterfaces) 
				{
					List<DtoInstanceRelation> otherOutputRelations = AdvisorDtoQueryUtil.getRelationsFrom(dtoOtherOutputInt);
					for (DtoInstanceRelation dtoOtherOutputRelation : otherOutputRelations) 
					{
						if(dtoOtherOutputRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INTERFACE_BINDS))
						{
							if((dtoInputInterface.ns+dtoInputInterface.name).equals(dtoOtherOutputRelation.Target))
							{
								inputInterfaceAlreadyConnected = true;
								break;
							}
						}
					}
					if(inputInterfaceAlreadyConnected) break;						
				}
			}				
			
			Boolean hasAllowedRelation = false;
			if(inputNs != "")
			{
				/** get the input mapped by the input interface */ 
				DtoInstance input = AdvisorDtoQueryUtil.getIndividualByName(inputNs, true, false, false);					
				
				/** for each input and output class names, I verify if exist a possible relation of binds */
				for(String inputClassName : input.ListClasses)
				{
					inputClassName = inputClassName.replace(namespace, ""); 
					
					HashMap<String, String> tf1 = new HashMap<String, String>();
					tf1.put("INPUT", inputClassName);
					tf1.put("OUTPUT", outputClassName);
					HashMap<String, String> allowedRelation = BindsProcessor.allowedRelationsHash.get(tf1);
					if(allowedRelation != null)
					{
						hasAllowedRelation = true;
						break;
					}
				}
			}
			
			String interfaceReturn = new String();
			eqInNs = eqInNs.replace(dtoInputInterface.ns, "");
			inputNs = inputNs.replace(dtoInputInterface.ns, "");
			eqOutNs = eqOutNs.replace(namespace, "");
			
			interfaceReturn += eqInNs; 
			interfaceReturn += "#";
			interfaceReturn += dtoInputInterface.name;
			interfaceReturn += "#";

			/** the return only can be true if:
			    - has an allowed relation
			    - it is a different equipment
			    - the output interface it is not binded
			    - the input interface it is not binded */
			
			Boolean hasCyclicalRel = false;	
			if(hasAllowedRelation && !eqInNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputInterfaceAlreadyConnected)
			{
				hasCyclicalRel = hasCyclicalRelationship(namespace, eqOutNs, dtoInputInterface.ns, eqInNs);
			}				
			if(hasAllowedRelation && !eqInNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputInterfaceAlreadyConnected && !hasCyclicalRel)
			{
				if(allowedInputInterfaces.contains(interfaceReturn+"false;")) allowedInputInterfaces.remove(interfaceReturn+"false;");					
				interfaceReturn += "true;";
			}else{
				if(!allowedInputInterfaces.contains(interfaceReturn.replace("true;", "false;"))) interfaceReturn += "false;";										
			}
			if(!allowedInputInterfaces.contains(interfaceReturn) && !allowedInputInterfaces.contains(interfaceReturn.replace("false;", "true;")))
			{
				allowedInputInterfaces.add(interfaceReturn);
			}				
		}
	}
	
	/** Cannot Connect */
	private void cannotConnect(List<DtoInstance> inputInterfaces, List<String> allowedInputInterfaces) 
	{
		String namespace = repository.getNamespace();
		
		for (DtoInstance dtoInputInterface : inputInterfaces) 
		{
			List<DtoInstanceRelation> inIntRelations = AdvisorDtoQueryUtil.getRelationsFrom(dtoInputInterface);
			String eqInNs = new String();				
			/** get name spaces of individuals of some input interface relations */
			for (DtoInstanceRelation inRelation : inIntRelations) 
			{
				if(inRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_COMPONENTOF))
				{
					eqInNs = inRelation.Target;		
					break;
				}
			}				
			String interfaceReturn = new String();	
			interfaceReturn += eqInNs; 
			interfaceReturn += "#";
			interfaceReturn += dtoInputInterface.name;
			interfaceReturn += "#";
			interfaceReturn += "false";				
			allowedInputInterfaces.add(interfaceReturn);
		}		
	}
	
	/** Get candidate interfaces for connection */
	public List<String> getCandidateInterfacesForConnection(String sourceName)
	{
		Date beginDate = new Date();		
		String namespace = repository.getNamespace();		
		
		List<String> allowedInputInterfaces = new ArrayList<String>();		
		List<DtoInstance> inputInterfaces = AdvisorDtoQueryUtil.getIndividualsFromClass(ConceptEnum.INPUT_INTERFACE);
						
		/** ===================================
		 *  find the instance of the output interface 
		 *  =================================== */		
		DtoInstance dtoSource = AdvisorDtoQueryUtil.getIndividualByName(namespace+sourceName, true, false, false);
		DtoInstance dtoOutputInterface = null;
		DtoInstance dtoOutput = null;
		String outputName = new String();
		if(dtoSource.ListClasses.contains(namespace+ConceptEnum.OUTPUT))
		{
			dtoOutput = dtoSource;
			outputName = sourceName;
		}else{
			dtoOutputInterface = dtoSource;
		}		
		
		/** =====================================
		 *  get all relations of the output interface
		 *  ===================================== */
		List<DtoInstanceRelation> outIntRelations = new ArrayList<DtoInstanceRelation>();
		if(dtoOutputInterface != null) outIntRelations = AdvisorDtoQueryUtil.getRelationsFrom(dtoOutputInterface);
				
		/** ======================================
		 * get name spaces of individuals of some output interface relations
		 * ======================================= */
		String eqOutNs = new String();
		String interfaceBindsNs = new String();
		for (DtoInstanceRelation dtoOutRelation : outIntRelations) 
		{
			if(dtoOutRelation.Property.equalsIgnoreCase(namespace+RelationEnum.MAPS_OUTPUT)) outputName = dtoOutRelation.Target;
			else if(dtoOutRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_COMPONENTOF)) eqOutNs = dtoOutRelation.Target;
			else if(dtoOutRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INTERFACE_BINDS)) interfaceBindsNs = dtoOutRelation.Target;			
		}

		/** ======================================== 
		 * if the output interface does not maps an output, it can not connects 
		 *  ========================================*/
		if(outputName.equals(""))
		{
			cannotConnect(inputInterfaces, allowedInputInterfaces);
			return allowedInputInterfaces;
		}
		
		/** ========================================
		 * get the instance of the output mapped by the output interface
		 *  ======================================= */
		if(dtoOutput == null) dtoOutput = AdvisorDtoQueryUtil.getIndividualByName(outputName, true, false, false);
				
		/** ========================================= 
		 * get all relations of the output interface
		 *  ========================================= */
		List<DtoInstanceRelation> outRelations = AdvisorDtoQueryUtil.getRelationsFrom(dtoOutput);
		
		String bindsNs = new String();	
		@SuppressWarnings("unused")
		String outPmNs = new String();	
		for (DtoInstanceRelation outRel : outRelations) 
		{
			if(outRel.Property.equalsIgnoreCase(namespace+RelationEnum.BINDS)) bindsNs = outRel.Target;
			else if(outRel.Property.equalsIgnoreCase(namespace+RelationEnum.INV_COMPONENTOF))
			{
				DtoInstance outPm = AdvisorDtoQueryUtil.getIndividualByName(outRel.Target, true, false, false);				
				if(outPm.ListClasses.contains(namespace+ConceptEnum.PHYSICAL_MEDIA))
				{
					outPmNs = outRel.Target;
				}
			}
		}
				
		/**=========================================== 
		 * verify if the output interface is already binded
		 * =========================================== */
		Boolean outputInterfaceAlreadyBinded = false;
		if(!interfaceBindsNs.equals("") || !bindsNs.equals("")) outputInterfaceAlreadyBinded = true;
			
		/**============================================ 
		 * now, the idea is to compare all types of the output with all types of all inputs
		 * ============================================*/
		for (String outputClassName : dtoOutput.ListClasses) 
		{
			outputClassName = outputClassName.replace(repository.getNamespace(), "");
						
			filterAllowedInterfacesWithInputInterfaces(outputClassName, allowedInputInterfaces, inputInterfaces, eqOutNs, outputInterfaceAlreadyBinded);
			
			filterAllowedInterfacesWithPhysicalMedia(outputClassName, allowedInputInterfaces, eqOutNs, outputInterfaceAlreadyBinded);			
		}

		PerformanceUtil.printExecutionTime("getCandidateInterfacesForConnection()", beginDate);
				
		return allowedInputInterfaces;
	}
	
	/** Checks whether we have a cycle in relationship */
	public Boolean hasCyclicalRelationship(String searchForEquipNS, String searchForEquipName, String actualEquipNS, String actualEquipName)
	{
		String namespace = repository.getNamespace();	
		if(searchForEquipNS.equals(actualEquipNS) && searchForEquipName.equals(actualEquipName)) return true;
				
		List<DtoInstanceRelation> equipRelations = DtoQueryUtil.getRelationsFrom(repository.getInferredModel(), actualEquipNS+actualEquipName);		
		ArrayList<String> outIntNss = new ArrayList<String>();
		
		/** get name spaces of individuals of some input interface relations */
		for (DtoInstanceRelation eqRelation : equipRelations) 
		{
			if(eqRelation.Property.equalsIgnoreCase(namespace+RelationEnum.COMPONENTOF_EQUIPMENT_OUTPUT_INTERFACE) || eqRelation.Property.equalsIgnoreCase(namespace+RelationEnum.COMPONENTOF))
			{
				DtoInstance outIntInstance = AdvisorDtoQueryUtil.getIndividualByName(eqRelation.Target, true, false, false);
				for (String classOutIntName : outIntInstance.ListClasses) 
				{
					if(classOutIntName.equals(namespace+ConceptEnum.OUTPUT_INTERFACE))
					{
						outIntNss.add(eqRelation.Target);
					}
				}
			}
		}
		
		for (String oiNs : outIntNss) 
		{
			DtoInstance outputInterface = AdvisorDtoQueryUtil.getIndividualByName(oiNs, false, false, false);			
			List<DtoInstanceRelation> outIntRelations = AdvisorDtoQueryUtil.getRelationsFrom(outputInterface);
			String inIntNs = new String();			
			/** get name spaces of individuals of some input interface relations */
			for (DtoInstanceRelation outIntRelation : outIntRelations) 
			{
				if(outIntRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INTERFACE_BINDS))
				{
					inIntNs = outIntRelation.Target;
					break;
				}
			}			
			/** if inIntNs != null */
			if(!inIntNs.equals(""))
			{
				List<DtoInstanceRelation> inIntRelations = DtoQueryUtil.getRelationsFrom(repository.getInferredModel(), inIntNs);
				String inIntEquipNs = new String();
				
				/** get name spaces of individuals of some input interface relations */
				for (DtoInstanceRelation inIntRelation : inIntRelations) 
				{
					if(inIntRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INV_COMPONENTOF))
					{
						inIntEquipNs = inIntRelation.Target;
						break;
					}
				}				
				String newActualEquipNs = inIntEquipNs.split("#")[0]+"#";
				String newActualEquipName = inIntEquipNs.split("#")[1];				
				if(hasCyclicalRelationship(searchForEquipNS, searchForEquipName, newActualEquipNs, newActualEquipName))
				{
					return true;
				}
			}			
		}		
		return false;
	}
	
	public String autoBinds()
	{		
		String namespace = repository.getNamespace();
		ArrayList<DtoInstance> outputInterfaces = new ArrayList<DtoInstance>();
		
		/** get all instances of output interfaces not connected */				
		List<DtoInstance> allInstances = DtoQueryUtil.getIndividuals(repository.getInferredModel(), false, false, false);
		for (DtoInstance instance : allInstances) 
		{
			for (String className : instance.ListClasses) 
			{
				if(className.equalsIgnoreCase(namespace+ConceptEnum.OUTPUT_INTERFACE))
				{
					List<DtoInstanceRelation> outputInterfaceRelations = AdvisorDtoQueryUtil.getRelationsFrom(instance);
					boolean alreadyConnected = false;
					for (DtoInstanceRelation outputInterfaceRelation : outputInterfaceRelations) 
					{
						if(outputInterfaceRelation.Property.equalsIgnoreCase(namespace+RelationEnum.INTERFACE_BINDS))
						{
							if((instance.ns+instance.name).equals(outputInterfaceRelation.Target))
							{
								alreadyConnected  = true;
								break;
							}
						}
					}					
					if(!alreadyConnected)
					{
						outputInterfaces.add(instance);
						break;
					}					
				}
			}
		}
		
		HashMap<String, ArrayList<String>> uniqueCandidatesForBinds = new HashMap<String, ArrayList<String>>();
		
		for (DtoInstance outputInterface : outputInterfaces) 
		{
			List<String> candidatesForConnection = getCandidateInterfacesForConnection(outputInterface.ns+outputInterface.name);
			
			int noCandidates = 0;
			String inputCandidateName = new String();
			
			for (String candidate : candidatesForConnection) 
			{
				if(candidate.contains("true"))
				{
					noCandidates++;
					inputCandidateName = candidate.split("#")[1];
				}				
				if(noCandidates > 1) break;				
			}	
			
			if(uniqueCandidatesForBinds.containsKey(inputCandidateName))
			{
				uniqueCandidatesForBinds.get(inputCandidateName).add(outputInterface.name);
			}
			else if(noCandidates == 1)
			{
				ArrayList<String> outs = new ArrayList<String>();
				outs.add(outputInterface.name);
				uniqueCandidatesForBinds.put(inputCandidateName, outs);
			}
		}
		
		ArrayList<String> listInstancesCreated = new ArrayList<String>();
		int bindsMade = 0;
		String returnMessage = "Interfaces binded:<br>";
		
		for(Entry<String, ArrayList<String>> candidates : uniqueCandidatesForBinds.entrySet()) 
		{
			String inputInterface = candidates.getKey();
			ArrayList<String> outs = candidates.getValue();
			
			if(outs.size() == 1)
			{
				provisioningBinds(outs.get(0), inputInterface, false, listInstancesCreated);
				
				bindsMade++;
				returnMessage += outs.get(0);
				returnMessage += " -> ";
				returnMessage += inputInterface;
				returnMessage += "<br>";
			}			
		}
		
		if(bindsMade<=0) returnMessage = "No interfaces binded.";
		
		return returnMessage;
	}

}
