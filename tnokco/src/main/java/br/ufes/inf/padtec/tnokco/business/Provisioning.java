package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import br.ufes.inf.nemo.okco.model.DtoInstanceRelation;
import br.ufes.inf.padtec.tnokco.controller.HomeController;

import com.hp.hpl.jena.ontology.Individual;


public class Provisioning {

	
	HashMap<HashMap<String, String>, HashMap<String,String>> values = new HashMap<HashMap<String,String>, HashMap<String,String>>();
	HashMap<String, HashMap<String, String>> equipmentsReleations = new HashMap<String, HashMap<String,String>>();
	HashMap<String, String> equipmentRP = new HashMap<String, String>();
	HashMap<String, String> equipmentOut = new HashMap<String, String>();
	static ArrayList<String> equipments = new ArrayList<String>();
	private static ArrayList<Equipment> equipmentsList= new ArrayList<Equipment>();
	
	static Provisioning instance = new Provisioning();
	
	
	static public Provisioning getInstance(){
		
		return instance;
	}
	
	public static void main(String[] args) {
		Provisioning rp= new Provisioning();
	}
	
	
	public Provisioning(){
		
		setHashEquipment();
		
		HashMap<String, String> tf1= new HashMap<String, String>();
		HashMap<String, String> hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_A-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_A-FEP");
		hashrp.put("RP_BINDING", "Source_A-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_A-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_A-FEP_from");		
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Matrix_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FEP");
		hashrp.put("RP_BINDING", "Source M-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So M-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So M-FEP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FEP");
		hashrp.put("RP_BINDING", "Source SN-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So SN-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So SN-FEP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_LPF-FEP");
		hashrp.put("RP_BINDING", "Source LPF-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So LPF-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So LPF-FEP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_PM-FEP");
		hashrp.put("RP_BINDING", "Source PM-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So PM-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So PM-FEP from");	
		values.put(tf1, hashrp);
		
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_A-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_A-FEP");
		hashrp.put("RP_BINDING", "Sink_A-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_A-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_A-FEP_from");		
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Matrix_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FEP");
		hashrp.put("RP_BINDING", "Sink M-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk M-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk M-FEP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FEP");
		hashrp.put("RP_BINDING", "Sink SN-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk SN-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk SN-FEP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_LPF-FEP");
		hashrp.put("RP_BINDING", "Sink LPF-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk LPF-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk LPF-FEP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Source PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_PM-FEP");
		hashrp.put("RP_BINDING", "Sink PM-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk PM-FEP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk PM-FEP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination Source Input");
		tf1.put("OUTPUT", "Adaptation Source Output");
		hashrp.put("RP", "Source AP");
		hashrp.put("RP_RELATION", "is_represented_by_So_AP");
		hashrp.put("RP_BINDING", "Source AP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So AP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So AP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination Sink Input");
		tf1.put("OUTPUT", "Adaptation Sink Output");
		hashrp.put("RP", "Sink AP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_AP");
		hashrp.put("RP_BINDING", "Sink AP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk AP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk AP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation Source Input");
		tf1.put("OUTPUT", "Layer Processor Source Output");
		hashrp.put("RP", "Source LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_L-FP");
		hashrp.put("RP_BINDING", "Source L-FP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So L-FP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So L-FP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation Source Input");
		tf1.put("OUTPUT", "Layer Processor Sink Output");
		hashrp.put("RP", "Sink LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_L-FP");
		hashrp.put("RP_BINDING", "Sink L-FP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk L-FP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk L-FP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation Source Input");
		tf1.put("OUTPUT", "Adaptation Source Output");
		hashrp.put("RP", "Unidirectional A-FP");
		hashrp.put("RP_RELATION", "is_represented_by_A-FP");
		hashrp.put("RP_BINDING", "A-FP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds A-FP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds A-FP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation Source Input");
		tf1.put("OUTPUT", "Subnetwork Output");
		hashrp.put("RP", "Source SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FP");
		hashrp.put("RP_BINDING", "Source SN-FP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So SN-FP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So SN-FP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation Sink Input");
		tf1.put("OUTPUT", "Subnetwork Output");
		hashrp.put("RP", "Source SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FP");
		hashrp.put("RP_BINDING", "Sink SN-FP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk SN-FP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk SN-FP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation Source Input");
		tf1.put("OUTPUT", "Matrix Output");
		hashrp.put("RP", "Source SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FP");
		hashrp.put("RP_BINDING", "Source M-FP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds So M-FP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds So M-FP from");	
		values.put(tf1, hashrp);
		
		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation Sink Input");
		tf1.put("OUTPUT", "Matrix Output");
		hashrp.put("RP", "Source M-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FP");
		hashrp.put("RP_BINDING", "Sink M-FP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds Sk M-FP to");
		hashrp.put("RP_BINDING_REL_OUT", "binds Sk M-FP from");	
		values.put(tf1, hashrp);		
	}
	
	private void setHashEquipment() {
		// TODO Auto-generated method stub
		String equi;
		
	}


	public static ArrayList<String> getOutputPortsEquipment(String nameEquipment){
		
		Individual ind = HomeController.Model.getIndividual(nameEquipment);  
		ArrayList<DtoInstanceRelation> rps= HomeController.Search.GetInstanceRelations(HomeController.Model, HomeController.NS+nameEquipment);
		ArrayList<String> values = new ArrayList<String>();
		
		for (DtoInstanceRelation dtoInstanceRelation : rps) {
			if(dtoInstanceRelation.Property.equals(HomeController.NS+"INV.componentOf")){
				values.add(dtoInstanceRelation.Target);
			}
		}
		return values;
	}
	
	public void setEquipments(){
		equipments = HomeController.Search.GetInstancesFromClass(HomeController.Model, HomeController.InfModel, HomeController.NS+"Equipment");
	}
	
	public static ArrayList<Equipment> getEquipmentsConnectionsBinds(){
		equipments = HomeController.Search.GetInstancesFromClass(HomeController.Model, HomeController.InfModel, HomeController.NS+"Equipment");

	    for (String equipment: equipments) {
	    	Individual ind= HomeController.Model.getIndividual(equipment);
	    	Equipment e = new Equipment(ind.getLocalName());
	    	ArrayList<String> outInt= HomeController.Search.GetInstancesOfTargetWithRelation(HomeController.InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
	    	ArrayList<String> inpInt= HomeController.Search.GetInstancesOfTargetWithRelation(HomeController.InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
	    	int i=0;
	    	InterfaceInput input;
	    	for (String string : inpInt) {
	    		
	    		ind= HomeController.Model.getIndividual(string);
	    		input = new InterfaceInput(ind.getLocalName());
	    		input.setId(i);
	    		i++;
	    		e.addInp(input);
	    	}
	    	i=0;
	    	for (String string : outInt) {
	    		Individual individual= HomeController.Model.getIndividual(string);
	    		InterfaceOutput outputInt = new InterfaceOutput();
	    		outputInt.setId(i);
	    		outputInt.setName(individual.getLocalName());
	    		e.addOut(outputInt);
		    	String out= HomeController.Search.GetInstancesOfTargetWithRelation(HomeController.InfModel, string, HomeController.NS+"maps_output", HomeController.NS+"Mapped_TF_Output").get(0);
				//relações de output
		    	ArrayList<DtoInstanceRelation> rel= HomeController.Search.GetInstanceRelations(HomeController.InfModel, out);
 				for (DtoInstanceRelation dtoInstanceRelation : rel) {
					Individual indiv= HomeController.Model.getIndividual(dtoInstanceRelation.Target);
					ArrayList<String> classesOut= HomeController.Search.GetClassesFrom(dtoInstanceRelation.Target, HomeController.InfModel);
					for (String string2 : classesOut) {
						// pega o input em questão
						if(string2.equals(HomeController.NS+"Input")){
							//relações do input
							rel = HomeController.Search.GetInstanceRelations(HomeController.InfModel, dtoInstanceRelation.Target);
							//percorre as relações do input
							for (DtoInstanceRelation triple : rel) {
								//pega o target do input
									//pega as relações do target 
									ArrayList<String> classesIntInp= HomeController.Search.GetClassesFrom(triple.Target, HomeController.InfModel);
								for (String nameclasse : classesIntInp) {
									if(nameclasse.equals(HomeController.NS+"Input_Interface")){
										String eq= HomeController.Search.GetInstancesOfTargetWithRelation(HomeController.InfModel, triple.Target, HomeController.NS+"INV.componentOf", HomeController.NS+"Equipment").get(0);
										ind= HomeController.Model.getIndividual(nameclasse);
										HashMap<Equipment, InterfaceInput> hash= new HashMap<Equipment, InterfaceInput>();
										hash.put(new Equipment(eq), new InterfaceInput(ind.getLocalName()));
										ArrayList<String> out_int = new ArrayList<String>();
										out_int.add(outputInt.getName());
										out_int.add(triple.Target);
										e.put(out_int, e);
										equipmentsList.add(e);
									}
								}
							}
						}
					}
					
				}
				i++;
			}
	    }
		
		return equipmentsList;
		
	}
	
	
	
}
