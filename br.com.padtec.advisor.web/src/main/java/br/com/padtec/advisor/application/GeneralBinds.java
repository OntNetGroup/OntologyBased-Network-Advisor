package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.padtec.advisor.application.dto.DtoResultAjax;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.advisor.application.util.ApplicationQueryUtil;
import br.com.padtec.advisor.application.util.PerformanceUtil;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.transformation.sindel.processor.BindsProcessor;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Statement;

public class GeneralBinds extends AdvisorService {
	
	public static PossibleBindsMap possibleBinds = new PossibleBindsMap();
	
	//==============================================
	//These are factory methods because they modify the ontology
	//==============================================
	
	public static DtoResultAjax provisioningBinds(String outInt, String inInt, HttpServletRequest request, Boolean updateListsInTheEnd, ArrayList<String> listInstancesCreated) {

		DtoResultAjax dto = new DtoResultAjax();

		String outputNs = "";
		String inputNs = "";
		
		List<DtoInstanceRelation> outIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+outInt);
		for (DtoInstanceRelation outIntRelation : outIntRelations) {
			if(outIntRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"maps_output")){
				outputNs = outIntRelation.Target;
				outputNs = outputNs.replace(OKCoUploader.getNamespace(), "");
				break;
			}
		}		

		List<DtoInstanceRelation> inIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+inInt);
		for (DtoInstanceRelation inIntRelation : inIntRelations) {
			if(inIntRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"maps_input")){
				inputNs = inIntRelation.Target;
				inputNs = inputNs.replace(OKCoUploader.getNamespace(), "");
				break;
			}
		}
		
		Boolean inputIsPmInput = false;
		if(inputNs.equals("")){
			DtoInstance inputOrIntInput = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+inInt, true, false, false);
			for (String inputOrIntInputClass : inputOrIntInput.ListClasses) {
				if(inputOrIntInputClass.equals(OKCoUploader.getNamespace()+"Physical_Media_Input")){
					inputNs = inInt;
					inputIsPmInput = true;
					break;
				}
			}
		}

		Boolean outputIsPmOutput = false;
		if(outputNs.equals("")){
			DtoInstance outputOrIntOutput = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+outInt, true, false, false);
			for (String outputOrIntOutputClass : outputOrIntOutput.ListClasses) {
				if(outputOrIntOutputClass.equals(OKCoUploader.getNamespace()+"Physical_Media_Output")){
					outputNs = outInt;
					outputIsPmOutput = true;
					break;
				}
			}
		}
		
		//binds Interface out with in
		Individual a, b;
		ObjectProperty rel = null;
		if(inputIsPmInput && !outputIsPmOutput){
			a = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+outInt);
			b = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+inputNs);
			rel = OKCoUploader.getBaseModel().getObjectProperty(OKCoUploader.getNamespace()+"binds_PM_out_interface");
		}else if(!inputIsPmInput && outputIsPmOutput){
			a = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+inInt);
			b = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+outputNs);
			rel = OKCoUploader.getBaseModel().getObjectProperty(OKCoUploader.getNamespace()+"binds_PM_in_Interface");
		}else{
			a = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+outInt);
			b = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+inInt);
			rel = OKCoUploader.getBaseModel().getObjectProperty(OKCoUploader.getNamespace()+"interface_binds");
		}
		
		Statement stmt = OKCoUploader.getBaseModel().createStatement(a, rel, b);
		OKCoUploader.getBaseModel().add(stmt);

		if(listInstancesCreated == null){
			listInstancesCreated = new ArrayList<String>();
		}
		listInstancesCreated.add(a.getNameSpace()+a.getLocalName());
		listInstancesCreated.add(b.getNameSpace()+b.getLocalName());
		
		if(!outputNs.equals("") && !inputNs.equals("")){
			
			a = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+outputNs);
			b = OKCoUploader.getBaseModel().getIndividual(OKCoUploader.getNamespace()+inputNs);
			
			BindsProcessor.bindPorts(null, a, b, null, OKCoUploader.getNamespace(), OKCoUploader.getBaseModel(), listInstancesCreated);
		}

		OKCoUploader.substituteInferredModelFromBaseModel(false);
		
		dto.ok = true;
		dto.result = "ok";

		return dto;

	}

	public static ArrayList<String> getCandidateInterfacesForConnection(String sourceNs){
		Date beginDate = new Date();
		
		ArrayList<String> allowedInputInterfaces = new ArrayList<String>();
		
		List<DtoInstance> inputInterfaces = DtoQueryUtil.getIndividualsFromClass(OKCoUploader.getInferredModel(),ConceptEnum.INPUT_INTERFACE.toString());
		List<DtoInstance> physicalMediaInputs = DtoQueryUtil.getIndividualsFromClass(OKCoUploader.getInferredModel(),ConceptEnum.PHYSICAL_MEDIA_INPUT.toString());
		List<DtoInstance> outputInterfaces = DtoQueryUtil.getIndividualsFromClass(OKCoUploader.getInferredModel(),ConceptEnum.OUTPUT_INTERFACE.toString());
		List<DtoInstance> outputs = DtoQueryUtil.getIndividualsFromClass(OKCoUploader.getInferredModel(),ConceptEnum.OUTPUT.toString());
				
		//find the instance of the output interface
		String ns = OKCoUploader.getInferredRepository().getNameSpace();
		
		DtoInstance source = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), ns+sourceNs, true, false, false);
		DtoInstance outputInterface = null;
		DtoInstance output = null;
		String outputNs = "";
		if(source.ListClasses.contains(ns+ConceptEnum.OUTPUT)){
			output = source;
			outputNs = sourceNs;
		}else{
			outputInterface = source;
		}
		
		//get all relations of the output interface
		List<DtoInstanceRelation> outIntRelations = new ArrayList<DtoInstanceRelation>();
		if(outputInterface != null){
			outIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), outputInterface.ns+outputInterface.name);
		}		
		
		//get namespaces of individuals of some output interface relations
		String eqOutNs = "";
		String interfaceBindsNs = "";
		for (DtoInstanceRelation outRelation : outIntRelations) {
			if(outRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.MAPS_OUTPUT)){
				outputNs = outRelation.Target;
			}else if(outRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_COMPONENTOF)){
				eqOutNs = outRelation.Target;
			}else if(outRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INTERFACE_BINDS)){
				interfaceBindsNs = outRelation.Target;
			}
		}

		//if the output interface does not maps an output, it can not connects
		if(outputNs.equals("")){
			for (DtoInstance inputInterface : inputInterfaces) {
				List<DtoInstanceRelation> inIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), inputInterface.ns+inputInterface.name);
				String eqInNs = "";
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_COMPONENTOF)){
						eqInNs = inRelation.Target;		
						break;
					}
				}
				
				String interfaceReturn = "";
				interfaceReturn += eqInNs; 
				interfaceReturn += "#";
				interfaceReturn += inputInterface.name;
				interfaceReturn += "#";
				interfaceReturn += "false";
				
				allowedInputInterfaces.add(interfaceReturn);
			}
			return allowedInputInterfaces;
		}
		
		//get the instance of the output mapped by the output interface
		if(output == null){
			output = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), outputNs, true, false, false);
		}		
		
		//get all relations of the output interface
		List<DtoInstanceRelation> outRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), output.ns+output.name);
		
		String bindsNs = "";
		String outPmNs = "";
		for (DtoInstanceRelation outRel : outRelations) {
			if(outRel.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.BINDS)){
				bindsNs = outRel.Target;
			}else if(outRel.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_COMPONENTOF)){
				DtoInstance outPm = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), outRel.Target, true, false, false);
				
				if(outPm.ListClasses.contains(ns+ConceptEnum.PHYSICAL_MEDIA)){
					outPmNs = outRel.Target;
				}
			}
		}
				
		//verify if the output interface is already binded
		Boolean outputInterfaceAlreadyBinded = false;
		if(!interfaceBindsNs.equals("") || !bindsNs.equals("")){
			outputInterfaceAlreadyBinded = true;
		}
	
		//now, the idea is compare all types of the output with all types of all inputs
		for (String outputClassName : output.ListClasses) {
			outputClassName = outputClassName.replace(OKCoUploader.getNamespace(), "");
			
			//here, I look for possible connections with input interfaces
			for (DtoInstance inputInterface : inputInterfaces) {
				List<DtoInstanceRelation> inIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), inputInterface.ns+inputInterface.name);
				String inputNs = "";
				String eqInNs = "";
				Boolean inputInterfaceAlreadyConnected = false;
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.MAPS_INPUT)){
						inputNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_COMPONENTOF)){
						eqInNs = inRelation.Target;						
					}else if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_INTERFACE_BINDS)){
						inputInterfaceAlreadyConnected = true;
					}
				}
				
				//since I verify the inverse relation of interface_binds above, 
				//it's necessary to verify if some output interface has the interface_binds relation
				//with the actual input interface
				//the block below it's for this purpose
				if(!inputInterfaceAlreadyConnected){
					for (DtoInstance otherOutputInt : outputInterfaces) {
						List<DtoInstanceRelation> otherOutputRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), otherOutputInt.ns+otherOutputInt.name);
						for (DtoInstanceRelation otherOutputRelation : otherOutputRelations) {
							if(otherOutputRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INTERFACE_BINDS)){
								if((inputInterface.ns+inputInterface.name).equals(otherOutputRelation.Target)){
									inputInterfaceAlreadyConnected = true;
									break;
								}
							}
						}
						if(inputInterfaceAlreadyConnected){
							break;
						}
					}
				}
				
				Boolean hasAllowedRelation = false;
				if(inputNs != ""){
					//get the input mapped by the input interface 
					DtoInstance input = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), inputNs, true, false, false);
					
					//for each input and output class names, I verify if exist a possible relation of binds
					for(String inputClassName : input.ListClasses){
						inputClassName = inputClassName.replace(OKCoUploader.getNamespace(), ""); 
						HashMap<String, String> tf1 = new HashMap<String, String>();
						tf1.put("INPUT", inputClassName);
						tf1.put("OUTPUT", outputClassName);

						HashMap<String, String> allowedRelation = BindsProcessor.allowedRelationsHash.get(tf1);

						if(allowedRelation != null){
							hasAllowedRelation = true;
							break;
						}
					}
				}
				
				String interfaceReturn = "";
				eqInNs = eqInNs.replace(inputInterface.ns, "");
				inputNs = inputNs.replace(inputInterface.ns, "");
				eqOutNs = eqOutNs.replace(ns, "");
				interfaceReturn += eqInNs; 
				interfaceReturn += "#";
				interfaceReturn += inputInterface.name;
				interfaceReturn += "#";

				//the return only can be true if:
				// - has an allowed relation
				// - it is a different equipment
				// - the output interface it is not binded
				// - the input interface it is not binded
				Boolean hasCyclicalRel = false;
				
				if(hasAllowedRelation && !eqInNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputInterfaceAlreadyConnected){
					hasCyclicalRel = hasCyclicalRelationship(ns, eqOutNs, inputInterface.ns, eqInNs);
				}
				
				if(hasAllowedRelation && !eqInNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputInterfaceAlreadyConnected && !hasCyclicalRel){
					if(allowedInputInterfaces.contains(interfaceReturn+"false;")){
						allowedInputInterfaces.remove(interfaceReturn+"false;");
					}
					interfaceReturn += "true;";
				}else{
					if(!allowedInputInterfaces.contains(interfaceReturn.replace("true;", "false;"))){
						interfaceReturn += "false;";
					}					
				}

				if(!allowedInputInterfaces.contains(interfaceReturn) && !allowedInputInterfaces.contains(interfaceReturn.replace("false;", "true;"))){
					allowedInputInterfaces.add(interfaceReturn);
				}				
			}
			
			//here, I look for possible connections with physical media inputs
			for (DtoInstance pmInput : physicalMediaInputs) {
				List<DtoInstanceRelation> inPMRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), pmInput.ns+pmInput.name);
				String pmNs = "";
				Boolean inputPMAlreadyConnected = false;
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inPMRelations) {
					if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_COMPONENTOF_SINGLE_PHYSICAL_MEDIA_PHYSICAL_MEDIA_INPUT) || inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_COMPONENTOF)){
						pmNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_BINDS_PM_OUT_INTERFACE)){
						inputPMAlreadyConnected = true;
					}
				}
				
				//since I verify the inverse relation of binds_PM_out_interface above, 
				//it's necessary to verify if some output port has the binds_PM_out_interface relation
				//with the actual PM input
				//the block below it's for this purpose
				if(!inputPMAlreadyConnected){
					//List<DtoInstance> allInstances = DtoQueryUtil.getIndividuals(OKCoUploader.getInferredModel(), false, false, false);
					for(DtoInstance otherOutput : outputs){
						List<DtoInstanceRelation> otherOutputRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), otherOutput.ns+otherOutput.name);
						for (DtoInstanceRelation otherOutputRelation : otherOutputRelations) {
							if(otherOutputRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INTERFACE_BINDS) || otherOutputRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.BINDS)){
								if((pmInput.ns+pmInput.name).equals(otherOutputRelation.Target)){
									inputPMAlreadyConnected = true;
									break;
								}
							}
						}
						if(inputPMAlreadyConnected){
							break;
						}

					}
				}
				
				Boolean hasAllowedRelation = false;
				
				//for each input and output class names, I verify if exist a possible relation of binds
				for(String pmInputClassName : pmInput.ListClasses){
					pmInputClassName = pmInputClassName.replace(OKCoUploader.getNamespace(), ""); 
					HashMap<String, String> tf1 = new HashMap<String, String>();
					tf1.put("INPUT", pmInputClassName);
					tf1.put("OUTPUT", outputClassName);

					HashMap<String, String> allowedRelation = BindsProcessor.allowedRelationsHash.get(tf1);

					if(allowedRelation != null){
						hasAllowedRelation = true;
						break;
					}
				}
				
				String interfaceReturn = "";
				pmNs = pmNs.replace(pmInput.ns, "");
				eqOutNs = eqOutNs.replace(ns, "");
				interfaceReturn += pmNs; 
				interfaceReturn += "#";
				interfaceReturn += pmInput.name;
				interfaceReturn += "#";
				
				if(hasAllowedRelation && !pmNs.equals(eqOutNs) && !outputInterfaceAlreadyBinded && !inputPMAlreadyConnected){
					if(allowedInputInterfaces.contains(interfaceReturn+"false;")){
						allowedInputInterfaces.remove(interfaceReturn+"false;");
					}
					interfaceReturn += "true;";
				}else{
					if(!allowedInputInterfaces.contains(interfaceReturn.replace("true;", "false;"))){
						interfaceReturn += "false;";
					}					
				}

				if(!allowedInputInterfaces.contains(interfaceReturn) && !allowedInputInterfaces.contains(interfaceReturn.replace("false;", "true;"))){
					allowedInputInterfaces.add(interfaceReturn);
				}	
			}
		}

		PerformanceUtil.printExecutionTime("getCandidateInterfacesForConnection()", beginDate);
				
		return allowedInputInterfaces;
	}
	
	public static Boolean hasCyclicalRelationship(String searchForEquipNS, String searchForEquipName, String actualEquipNS, String actualEquipName){
		if(searchForEquipNS.equals(actualEquipNS) && searchForEquipName.equals(actualEquipName)){
			return true;
		}
		
		List<DtoInstanceRelation> equipRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), actualEquipNS+actualEquipName);
		
		ArrayList<String> outIntNss = new ArrayList<String>();
		//get namespaces of individuals of some input interface relations
		for (DtoInstanceRelation eqRelation : equipRelations) {
			if(eqRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.COMPONENTOF_EQUIPMENT_OUTPUT_INTERFACE) || eqRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.COMPONENTOF)){
				DtoInstance outIntInstance = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), eqRelation.Target, true, false, false);
				for (String classOutIntName : outIntInstance.ListClasses) {
					if(classOutIntName.equals(OKCoUploader.getNamespace()+ConceptEnum.OUTPUT_INTERFACE)){
						outIntNss.add(eqRelation.Target);
					}
				}
			}
		}
		
		for (String oiNs : outIntNss) {
			DtoInstance outputInterface = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(), oiNs, false, false, false);
			
			List<DtoInstanceRelation> outIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), outputInterface.ns+outputInterface.name);
			String inIntNs = "";
			//get namespaces of individuals of some input interface relations
			for (DtoInstanceRelation outIntRelation : outIntRelations) {
				if(outIntRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INTERFACE_BINDS)){
					inIntNs = outIntRelation.Target;
					break;
				}
			}
			//se inIntNs != null
			if(!inIntNs.equals("")){
				List<DtoInstanceRelation> inIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), inIntNs);
				String inIntEquipNs = "";
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inIntRelation : inIntRelations) {
					if(inIntRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.INV_COMPONENTOF)){
						inIntEquipNs = inIntRelation.Target;
						break;
					}
				}
				
				String newActualEquipNs = inIntEquipNs.split("#")[0]+"#";
				String newActualEquipName = inIntEquipNs.split("#")[1];
				
				if(hasCyclicalRelationship(searchForEquipNS, searchForEquipName, newActualEquipNs, newActualEquipName)){
					return true;
				}
			}
			
		}		
		
		return false;
	}

}
