package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.padtec.advisor.application.dto.DtoResultAjax;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.advisor.application.util.ApplicationQueryUtil;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.transformation.sindel.processor.BindsProcessor;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
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
			DtoInstance inputOrIntInput = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+inInt);
			for (String inputOrIntInputClass : inputOrIntInput.ListClasses) {
				if(inputOrIntInputClass.equals(OKCoUploader.getNamespace()+"Physical_Media_Input")){
					inputNs = inInt;
					inputIsPmInput = true;
					break;
				}
			}
		}
//		System.out.println();
		Boolean outputIsPmOutput = false;
		if(outputNs.equals("")){
			DtoInstance outputOrIntOutput = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+outputNs);
			for (String outputOrIntOutputClass : outputOrIntOutput.ListClasses) {
				if(outputOrIntOutputClass.equals(OKCoUploader.getNamespace()+"Physical_Media_Output")){
					outputNs = inInt;
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
			
//			ArrayList<String> tiposA=HomeController.Search.GetClassesFrom(OKCoUploader.getNamespace()+a.getLocalName(),OKCoUploader.getBaseModel());
//			ArrayList<String> tiposB=HomeController.Search.GetClassesFrom(OKCoUploader.getNamespace()+b.getLocalName(),OKCoUploader.getBaseModel());
//			tiposA.remove(OKCoUploader.getNamespace()+"Geographical_Element");
//			tiposA.remove(OKCoUploader.getNamespace()+"Bound_Input-Output");
//			tiposB.remove(OKCoUploader.getNamespace()+"Geographical_Element");
//			tiposB.remove(OKCoUploader.getNamespace()+"Bound_Input-Output");
//			rel = OKCoUploader.getBaseModel().getObjectProperty(OKCoUploader.getNamespace()+"binds");
//			stmt = OKCoUploader.getBaseModel().createStatement(a, rel, b);
//			OKCoUploader.getBaseModel().add(stmt);	
//			HashMap<String, String> hash = new HashMap<String, String>();
//			hash.put("INPUT", tiposB.get(0));
//			hash.put("OUTPUT", tiposA.get(0));
//			HashMap<String, String>element= Provisioning.values.get(hash);
//			Provisioning.bindsSpecific(a,b,tiposA.get(0),tiposB.get(0));
			//BindsProcessor.bindPorts(outputNs, inputNs);
			
			BindsProcessor.bindPorts(null, a, b, null, OKCoUploader.getNamespace(), OKCoUploader.getBaseModel(), listInstancesCreated);

		}

		OKCoUploader.substituteInferredModelFromBaseModel(false);
		
		
//		if(updateListsInTheEnd){
//			
//			try {
//				
////				for (String instanceUri : listInstancesCreated) {
////					HomeController.UpdateAddIntanceInLists(instanceUri);	
////				}
//				
//			} catch (InconsistentOntologyException e) {
//				
//				e.printStackTrace();
//				
//			} catch (OKCoExceptionInstanceFormat e) {
//				
//				e.printStackTrace();
//			}			
//		}
		
		dto.ok = true;
		dto.result = "ok";

		return dto;

	}

	public static ArrayList<String> getCandidateInterfacesForConnection(String outIntNs){
		
		ArrayList<String> allowedInputInterfaces = new ArrayList<String>();
		//find the instance of the output interface
		DtoInstance outputInterface = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(),outIntNs);

		//get all relations of the output interface
		List<DtoInstanceRelation> outIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), outputInterface.ns+outputInterface.name);
		
		//get namespaces of individuals of some output interface relations
		String outputNs = "";
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

		List<DtoInstance> inputInterfaces = DtoQueryUtil.getIndividualsFromClass(OKCoUploader.getInferredModel(),ConceptEnum.INPUT_INTERFACE.toString());
		List<DtoInstance> physicalMediaInputs = DtoQueryUtil.getIndividualsFromClass(OKCoUploader.getInferredModel(),ConceptEnum.PHYSICAL_MEDIA_INPUT.toString());
		
		//if the output interface does not maps an output, it can not connects
		if(outputNs.equals("")){
			for (DtoInstance inputInterface : inputInterfaces) {
				List<DtoInstanceRelation> inIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), inputInterface.ns+inputInterface.name);
				String eqInNs = "";
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"INV.componentOf")){
						eqInNs = inRelation.Target;		
						break;
					}
				}
				
				//if(!eqInNs.equals(eqOutNs)){
					String interfaceReturn = "";
					interfaceReturn += eqInNs; 
					interfaceReturn += "#";
					interfaceReturn += inputInterface.name;
					interfaceReturn += "#";
					interfaceReturn += "false";
					
					allowedInputInterfaces.add(interfaceReturn);
				//}
				
				
				
			}
			return allowedInputInterfaces;
		}
		
		//get the instance of the output mapped by the output interface
		DtoInstance output = null;
		List<DtoInstance> listAllInstances = DtoQueryUtil.getIndividuals(OKCoUploader.getInferredModel(), true, false, false);
		for (DtoInstance instance : listAllInstances) {
			if(outputNs.equals(instance.ns+instance.name)){
				output = instance;
				break;
			}
		}
		
		//get all relations of the output interface
		List<DtoInstanceRelation> outRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), output.ns+output.name);
		
		String bindsNs = "";
		for (DtoInstanceRelation outRel : outRelations) {
			if(outRel.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+RelationEnum.BINDS)){
				bindsNs = outRel.Target;
			}
		}
				
		//verify if the output interface is already binded
		Boolean outputInterfaceAlreadyBinded = false;
		if(!interfaceBindsNs.equals("") || !bindsNs.equals("")){
			outputInterfaceAlreadyBinded = true;
		}
		
		//get all input interfaces
		
//		ArrayList<Instance> inputInterfaces = new ArrayList<Instance>(); 
//		for (Instance instance : HomeController.ListAllInstances) {
//			for (String className : instance.ListClasses) {
//				className = className.replace(OKCoUploader.getNamespace(), "");
//				if(className.equalsIgnoreCase("Input_Interface")){
//					inputInterfaces.add(instance);
//					break;
//				}
//			}
//		}

		
		//now, the idea is compare all types of the output with all types of all inputs
		for (String outputClassName : output.ListClasses) {
			outputClassName = outputClassName.replace(OKCoUploader.getNamespace(), "");
			
			//here, I look for possible connections with input interfaces
			for (DtoInstance inputInterface : inputInterfaces) {
				if(inputInterface.name.equalsIgnoreCase("in_skeq1")){
					System.out.println();
				}
				List<DtoInstanceRelation> inIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), inputInterface.ns+inputInterface.name);
				String inputNs = "";
				String eqInNs = "";
				Boolean inputInterfaceAlreadyConnected = false;
				
				//get namespaces of individuals of some input interface relations
				for (DtoInstanceRelation inRelation : inIntRelations) {
					if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"maps_input")){
						inputNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"INV.componentOf")){
						eqInNs = inRelation.Target;						
					}else if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"INV.interface_binds")){
						inputInterfaceAlreadyConnected = true;
					}
				}

				//List<DtoInstance> allInstances = DtoQueryUtil.getIndividuals(OKCoUploader.getInferredModel(), true, false, false);
				
				//since I verify the inverse relation of interface_binds above, 
				//it's necessary to verify if some output interface has the interface_binds relation
				//with the actual input interface
				//the block below it's for this purpose
				if(!inputInterfaceAlreadyConnected){
					for(DtoInstance otherOutput : listAllInstances){
						for (String otherOutputClassName : otherOutput.ListClasses) {
							otherOutputClassName = otherOutputClassName.replace(OKCoUploader.getNamespace(), "");
							if(otherOutputClassName.equalsIgnoreCase("Output_Interface")){
								List<DtoInstanceRelation> otherOutputRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), otherOutput.ns+otherOutput.name);
								for (DtoInstanceRelation otherOutputRelation : otherOutputRelations) {
									if(otherOutputRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"interface_binds")){
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
						if(inputInterfaceAlreadyConnected){
							break;
						}
					}
				}
				
				Boolean hasAllowedRelation = false;
				if(inputNs != ""){
					//get the input mapped by the input interface 
					DtoInstance input = null;
					for (DtoInstance instance : listAllInstances) {
						if(inputNs.equals(instance.ns+instance.name)){
							input = instance;
							break;
						}
					}
					
					//for each input and output class names, I verify if exist a possible relation of binds
					for(String inputClassName : input.ListClasses){
						if(inputClassName.contains("Termination_Sink_Input") && outputClassName.contains("Adaptation_Sink_Output")){
							System.out.println();
						}
						inputClassName = inputClassName.replace(OKCoUploader.getNamespace(), ""); 
						HashMap<String, String> tf1 = new HashMap<String, String>();
						tf1.put("INPUT", inputClassName);
						tf1.put("OUTPUT", outputClassName);

						HashMap<String, String> allowedRelation = BindsProcessor.values.get(tf1);

						if(allowedRelation != null){
							hasAllowedRelation = true;
							break;
						}
					}
				}
				
				String interfaceReturn = "";
				eqInNs = eqInNs.replace(inputInterface.ns, "");
				inputNs = inputNs.replace(inputInterface.ns, "");
				eqOutNs = eqOutNs.replace(outputInterface.ns, "");
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
					hasCyclicalRel = hasCyclicalRelationship(outputInterface.ns, eqOutNs, inputInterface.ns, eqInNs);
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
					if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"INV.componentOf.Single_Physical_Media.Physical_Media_Input") || inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"INV.componentOf")){
						pmNs = inRelation.Target;
					}else if(inRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"INV.binds_PM_out_interface")){
						inputPMAlreadyConnected = true;
					}
				}
				
				//since I verify the inverse relation of binds_PM_out_interface above, 
				//it's necessary to verify if some output port has the binds_PM_out_interface relation
				//with the actual PM input
				//the block below it's for this purpose
				if(!inputPMAlreadyConnected){
					//List<DtoInstance> allInstances = DtoQueryUtil.getIndividuals(OKCoUploader.getInferredModel(), false, false, false);
					for(DtoInstance otherOutput : listAllInstances){
						for (String otherOutputClassName : otherOutput.ListClasses) {
							otherOutputClassName = otherOutputClassName.replace(OKCoUploader.getNamespace(), "");
							if(otherOutputClassName.equalsIgnoreCase("Output")){
								if(otherOutput.name.equals("out_sotf3")){
									System.out.println();
								}
								List<DtoInstanceRelation> otherOutputRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), otherOutput.ns+otherOutput.name);
								for (DtoInstanceRelation otherOutputRelation : otherOutputRelations) {
									if(otherOutputRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"interface_binds") || otherOutputRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"binds")){
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

					HashMap<String, String> allowedRelation = BindsProcessor.values.get(tf1);

					if(allowedRelation != null){
						hasAllowedRelation = true;
						break;
					}
				}
				
				String interfaceReturn = "";
				pmNs = pmNs.replace(pmInput.ns, "");
				//inputNs = inputNs.replace(pmInput.ns, "");
				eqOutNs = eqOutNs.replace(outputInterface.ns, "");
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
			if(eqRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"componentOf.Equipment.Output_Interface") || eqRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"componentOf")){
				DtoInstance outIntInstance = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(),eqRelation.Target);
				for (String classOutIntName : outIntInstance.ListClasses) {
					if(classOutIntName.equals(OKCoUploader.getNamespace()+"Output_Interface")){
						outIntNss.add(eqRelation.Target);
					}
				}
			}
		}
		
		for (String oiNs : outIntNss) {
			DtoInstance outputInterface = DtoQueryUtil.getIndividualByName(OKCoUploader.getInferredModel(),oiNs);
			
			List<DtoInstanceRelation> outIntRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), outputInterface.ns+outputInterface.name);
			String inIntNs = "";
			//get namespaces of individuals of some input interface relations
			for (DtoInstanceRelation outIntRelation : outIntRelations) {
				if(outIntRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"interface_binds")){
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
					if(inIntRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"INV.componentOf")){
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
		
	/**
	 * Bind specifics.
	 * 
	 * @param a: Individual
	 * @param b: Individual
	 * @param tipo_out: ?
	 * @param tipo_inp: ?
	 */
	public static void bindsSpecific(Individual a, Individual b, String tipo_out, String tipo_inp) 
	{		
		OntModel baseModel = OKCoUploader.getBaseModel();
		String namespace = OKCoUploader.getNamespace();
		
		HashMap<String, String> key = new HashMap<String, String>();
		key.put("INPUT", tipo_inp);
		key.put("OUTPUT", tipo_out);
		
		try{
			HashMap<String, String> value = possibleBinds.getMap().get(key);
			
			OntClass ClassImage = baseModel.getOntClass(namespace+value.get("RP"));
			Individual rp = baseModel.createIndividual(namespace+a.getLocalName()+"rp"+b.getLocalName(),ClassImage);
			Individual binding= baseModel.createIndividual(namespace+a.getLocalName()+"binding"+b.getLocalName(),baseModel.getResource(namespace+value.get("RP_BINDING")));
			
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			
			stmts.add(baseModel.createStatement(binding, baseModel.getProperty(namespace+value.get("RP_RELATION")), rp));
			stmts.add(baseModel.createStatement(binding, baseModel.getProperty(namespace+value.get("RP_BINDING_REL_IN")), b));
			stmts.add(baseModel.createStatement(binding, baseModel.getProperty(namespace+value.get("RP_BINDING_REL_OUT")), a));
			
			OKCoUploader.getBaseModel().add(stmts);
			
		}catch(Exception e){
			e = new Exception("not bound");
		}
	}
}
