package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import br.ufes.inf.nemo.okco.model.DtoInstanceRelation;
import br.ufes.inf.padtec.tnokco.controller.HomeController;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;


public class Provisioning {


	public static HashMap<HashMap<String, String>, HashMap<String,String>> values = new HashMap<HashMap<String,String>, HashMap<String,String>>();
	HashMap<String, HashMap<String, String>> equipmentsReleations = new HashMap<String, HashMap<String,String>>();
	HashMap<String, String> equipmentRP = new HashMap<String, String>();
	HashMap<String, String> equipmentOut = new HashMap<String, String>();
	static ArrayList<String> equipments = new ArrayList<String>();
	private static ArrayList<Equipment> equipmentsList= new ArrayList<Equipment>();
	public static OntModel Model= HomeController.Model;
	public static InfModel InfModel = HomeController.InfModel;

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
		hashrp.put("RP", "Source_M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FEP");
		hashrp.put("RP_BINDING", "Source-M-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FEP-from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FEP");
		hashrp.put("RP_BINDING", "Source-SN-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_LPF-FEP");
		hashrp.put("RP_BINDING", "Source_LPF-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_LPF-FEP-from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_PM-FEP");
		hashrp.put("RP_BINDING", "Source_PM-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_PM-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_PM-FEP_from");	
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
		hashrp.put("RP", "Sink_M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FEP");
		hashrp.put("RP_BINDING", "Sink_M-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FEP");
		hashrp.put("RP_BINDING", "Sink_SN-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_LPF-FEP");
		hashrp.put("RP_BINDING", "Sink_LPF-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_LPF-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Source_PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_PM-FEP");
		hashrp.put("RP_BINDING", "Sink_PM-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_PM-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_PM-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination_Source_Input");
		tf1.put("OUTPUT", "Adaptation_Source_Output");
		hashrp.put("RP", "Source_AP");
		hashrp.put("RP_RELATION", "is_represented_by_So_AP");
		hashrp.put("RP_BINDING", "Source_AP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_AP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_AP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination_Sink_Input");
		tf1.put("OUTPUT", "Adaptation_Sink_Output");
		hashrp.put("RP", "Sink_AP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_AP");
		hashrp.put("RP_BINDING", "Sink_AP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_AP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_AP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Layer_Processor_Source_Output");
		hashrp.put("RP", "Source_LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_L-FP");
		hashrp.put("RP_BINDING", "Source_L-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_L-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Layer_Processor_Sink_Output");
		hashrp.put("RP", "Sink_LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_L-FP");
		hashrp.put("RP_BINDING", "Sink_L-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_L-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Adaptation_Source_Output");
		hashrp.put("RP", "Unidirectional_A-FP");
		hashrp.put("RP_RELATION", "is_represented_by_A-FP");
		hashrp.put("RP_BINDING", "A-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_A-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_A-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Subnetwork_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FP");
		hashrp.put("RP_BINDING", "Source_SN-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Subnetwork_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FP");
		hashrp.put("RP_BINDING", "Sink_SN-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Matrix_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FP");
		hashrp.put("RP_BINDING", "Source_M-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Matrix_Output");
		hashrp.put("RP", "Source_M-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FP");
		hashrp.put("RP_BINDING", "Sink_M-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FP_from");	
		values.put(tf1, hashrp);		
	}

	private void setHashEquipment() {
		// TODO Auto-generated method stub
		String equi;

	}


	public static ArrayList<String> getOutputPortsEquipment(String nameEquipment){

		Individual ind = Model.getIndividual(nameEquipment);  
		ArrayList<DtoInstanceRelation> rps= HomeController.Search.GetInstanceRelations(Model, HomeController.NS+nameEquipment);
		ArrayList<String> values = new ArrayList<String>();

		for (DtoInstanceRelation dtoInstanceRelation : rps) {
			if(dtoInstanceRelation.Property.equals(HomeController.NS+"INV.componentOf")){
				values.add(dtoInstanceRelation.Target);
			}
		}
		return values;
	}



	public static ArrayList<Equipment> getEquipmentsConnectionsBinds(){
		equipments = HomeController.Search.GetInstancesFromClass(Model, HomeController.InfModel, HomeController.NS+"Equipment");

		for (String equipment: equipments) {
			Individual ind= Model.getIndividual(equipment);
			Equipment e = new Equipment(ind.getLocalName());
			ArrayList<String> outInt= HomeController.Search.GetInstancesOfTargetWithRelation(HomeController.InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
			ArrayList<String> inpInt= HomeController.Search.GetInstancesOfTargetWithRelation(HomeController.InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
			int i=0;
			InterfaceInput input;
			for (String string : inpInt) {

				ind= Model.getIndividual(string);
				input = new InterfaceInput(ind.getLocalName());
				input.setId(i);
				i++;
				e.addInp(input.getName());
			}
			i=0;
			for (String string : outInt) {
				Individual individual= Model.getIndividual(string);
				InterfaceOutput outputInt = new InterfaceOutput();
				//outputInt.setId(i);
				outputInt.setName(individual.getLocalName());
				e.addOut(outputInt);
				String out= HomeController.Search.GetInstancesOfTargetWithRelation(HomeController.InfModel, string, HomeController.NS+"maps_output", HomeController.NS+"Mapped_TF_Output").get(0);
				//relações de output
				ArrayList<DtoInstanceRelation> rel= HomeController.Search.GetInstanceRelations(HomeController.InfModel, out);
				for (DtoInstanceRelation dtoInstanceRelation : rel) {
					Individual indiv= Model.getIndividual(dtoInstanceRelation.Target);
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
										String eq= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, triple.Target, HomeController.NS+"INV.componentOf", HomeController.NS+"Equipment").get(0);
										ind= Model.getIndividual(nameclasse);
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

	public static boolean bindsInterfaces(String output, String input){
		Individual ind= Model.getIndividual(output);

		return true;
	}
	
	public static ArrayList<Equipment> getAllEquipmentsandConnections(){
		Model = HomeController.Model;
		InfModel = HomeController.InfModel;
		HashMap<String, String> hashInputEquipment= new HashMap<String, String>();
		equipments = HomeController.Search.GetInstancesFromClass(Model, InfModel, HomeController.NS+"Equipment");
		Equipment e = null;
		Individual individual;
		ArrayList<Equipment> equips= new ArrayList<Equipment>();
		for (String equipment: equipments) {
			Individual indeq= Model.getIndividual(equipment);
			ArrayList<String> inpInt= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Input_Interface");
			int i=0;
			InterfaceInput input;
			for (String string : inpInt) {
				Individual ind= Model.getIndividual(string);
				hashInputEquipment.put(ind.getLocalName(), indeq.getLocalName());
			}
	
		}
		for (String equipment: equipments) {
			ArrayList<String> outInt= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
			int i=0;
			Individual ind= Model.getIndividual(equipment);
			e = new Equipment(ind.getLocalName());
			for (String string : outInt) {
				individual= Model.getIndividual(string);
				InterfaceOutput outputInt = new InterfaceOutput();
				outputInt.setName(individual.getLocalName());
				e.addOut(outputInt);
				String inputcon= null;
				if(!HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, string, HomeController.NS+"interface_binds", HomeController.NS+"Input_Interface").isEmpty()){
					inputcon= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, string, HomeController.NS+"interface_binds", HomeController.NS+"Input_Interface").get(0);
				}
				if(inputcon!=null){
					outputInt.setConnected(true);
					ArrayList<String> binds = new ArrayList<String>();
					binds.add(individual.getLocalName());
					Individual indiv= Model.getIndividual(inputcon);
					binds.add(indiv.getLocalName());
					Individual equipmentEl = Model.getIndividual(HomeController.NS+hashInputEquipment.get(indiv.getLocalName()));
					Equipment equip = new Equipment(equipmentEl.getLocalName());
					e.put(binds, equip);
				}
				
			}
			equips.add(e);	
		}
	
	return equips;
}



}
