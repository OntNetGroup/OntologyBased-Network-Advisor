package br.ufes.inf.nemo.padtec.processors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.padtec.Sindel2OWL;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class BindsProcessor {
	private static List<String>  transportFunctions =  Arrays.asList("tf","so-tf","sk-tf","bi-tf","af","so-af","sk-af","bi-af","lpf","so-lpf","sk-lpf","bi-lpf","matrix","uni-matrix","so-matrix","sk-matrix","bi-matrix","pm", "sn");
	private static List<String>  ports =  Arrays.asList("input","output");
	private static List<String>  interfaces =  Arrays.asList("in_int","out_int");

	private static HashMap<String, String> simpleBindsHash = new HashMap<>();
	
	
	private static OntModel model; 
	private static String ClassNS;
	private static String IndNS;

	//Common used variables
	private static ObjectProperty componentOf;
	private static Individual a,b,x,k,p,q;
	private static Statement stmt;
	private static ObjectProperty rel = null;
	private static String toA;
	private static String toB;

	private static Individual createNewIndividual(String type){
		OntClass o_class = model.getOntClass(ClassNS+type);

		String indName = "_ind_"+Sindel2OWL.getCont();
		Individual aux = o_class.createIndividual(IndNS+indName);

		Sindel2OWL.warning += "\nNew individual "+indName+" created of type "+type;
		Sindel2OWL.hashIndividuals.put(indName, type);

		return aux;
	}

	//Simple
	public static void processBinds(OntModel model, String ClassNS, String IndNS, String binds){
		initializeHash();
		String[] declarations = binds.split(",");

		componentOf = model.getObjectProperty(ClassNS+"componentOf");
		BindsProcessor.model = model;
		BindsProcessor.ClassNS = ClassNS;
		BindsProcessor.IndNS = IndNS;

		for (String declaration : declarations) {
			String[] vars = declaration.split(":");

			boolean isTF1 = false, isTF2 = false;//for Transport Function
			boolean isP1 = false, isP2 = false;//for Port
			boolean isI1 = false, isI2 = false;//for Interface

			//Create the individual vars[0] and vars[1]
			a = model.getIndividual(IndNS+vars[0]);
			b = model.getIndividual(IndNS+vars[1]);

			toA = Sindel2OWL.hashIndividuals.get(vars[0]);
			toB = Sindel2OWL.hashIndividuals.get(vars[1]);

			isTF1 = transportFunctions.contains(toA);
			isP1 = ports.contains(toA);
			isI1 = interfaces.contains(toA);
			
			isTF2 = transportFunctions.contains(toB);
			isP2 = ports.contains(toB);
			isI2 = interfaces.contains(toB);
			
			if(vars.length == 2){
				//SimpleRelation
				if(isTF1 && isTF2){
					//IF a and b are Transport Functions
					processSimpleRelation_TFxTF();
				}else if(isTF1 && isP2){
					//IF a is a Transport Function and b is a Port
					processSimpleRelation_TFxPort();
				}else if(isP1 && isTF2){
					//IF a is a Port and b is a Transport Function
					processSimpleRelation_PortxTF();
				}else if(isP1 && isP2){
					//IF a and b are Ports
					processSimpleRelation_PortxPort();
				}else if(isI1 && isI2){
					//IF a and b are Ports
					processSimpleRelation_InterfacexInterface();
				}else if(isI1 && isP2){
					//IF a and b are Ports
					processSimpleRelation_InterfacexPort();
				}
			}else{
				x = model.getIndividual(IndNS+vars[2]);
				//AssignableRelation
				if(isP1 && isP2){
					//IF a and b are Ports 
					processAssignableRelation_PortxPort();
				}else if(isTF1 && isTF2){
					//IF a and b are Transport Functions
					processAssignableRelation_TFxTF();
				}else if(isTF1 && isP2){
					//IF a is a Transport Function and b is a Port
					processAssignableRelation_TFxPort();
				}else if(isP1 && isTF2){
					//IF a is a Port and b is a Transport Function
					processAssignableRelation_PortxTF();
				}
			}
		}
	}

	private static void initializeHash() {
		simpleBindsHash.put("Adaptation_Source"+"Input", "Adaptation_Source_Input");
		simpleBindsHash.put("Adaptation_Source"+"Output", "Adaptation_Source_Output");
		simpleBindsHash.put("Adaptation_Sink"+"Input", "Adaptation_Sink_Input");
		simpleBindsHash.put("Adaptation_Sink"+"Output", "Adaptation_Sink_Output");
		simpleBindsHash.put("Termination_Source"+"Input", "Termination_Source_Input");
		simpleBindsHash.put("Termination_Source"+"Output", "Termination_Source_Output");
		simpleBindsHash.put("Termination_Sink"+"Input", "Termination_Sink_Input");
		simpleBindsHash.put("Termination_Sink"+"Output", "Termination_Sink_Output");
		simpleBindsHash.put("Layer_Processor_Function_Source"+"Input", "Layer_Processor_Source_Input");
		simpleBindsHash.put("Layer_Processor_Function_Source"+"Output", "Layer_Processor_Source_Output");
		simpleBindsHash.put("Layer_Processor_Function_Sink"+"Input", "Layer_Processor_Sink_Input");
		simpleBindsHash.put("Layer_Processor_Function_Sink"+"Output", "Layer_Processor_Sink_Output");
		simpleBindsHash.put("Matrix"+"Input", "Marix_Input");
		simpleBindsHash.put("Matrix"+"Output", "Marix_Output");
		simpleBindsHash.put("Subnetwork"+"Input", "Subnetwork_Input");
		simpleBindsHash.put("Subnetwork"+"Output", "Subnetwork_Output");
		simpleBindsHash.put("Physical_Media"+"Input", "Physical_Media_Input");
		simpleBindsHash.put("Physical_Media"+"Output", "Physical_Media_Output");
	}

	private static void processSimpleRelation_InterfacexPort() {
		// TODO VERIFICAR!!!
		
	}

	private static void processSimpleRelation_InterfacexInterface() {
		//TODO WARNING IN x OUT??
		rel = model.getObjectProperty(ClassNS+"interface_binds");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
	}

	private static void processSimpleRelation_TFxTF(){
		rel = model.getObjectProperty(ClassNS+"tf_connection");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
	}

	private static void processSimpleRelation_TFxPort(){
		//If port is Input, create a Output individual
		String toP = "";
		if(toB.equals("input")){
			toP = "Output";
		}else{//If port is Output, create a Input individual
			toP = "Input";
		}

		k = createNewIndividual(toP);					

		//TF{new Port}
		stmt = model.createStatement(a, componentOf, k);
		model.add(stmt);

		//k binds a
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(k, rel, b);
		model.add(stmt);
	}

	private static void processSimpleRelation_PortxTF(){
		//If port is Input, create a Output individual
		String toP = "";
		if(toA.equals("input")){
			toP = "Output";
		}else{//If port is Output, create a Input individual
			toP = "Input";
		}

		k = createNewIndividual(toP);

		//TF{new Port}
		stmt = model.createStatement(b, componentOf, k);
		model.add(stmt);

//		if(simpleBindsHash.containsKey(key))
//		String specificRelation 
//		
		//a binds new Port
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(a, rel, k);
		model.add(stmt);
	}

	private static void processSimpleRelation_PortxPort(){
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);	
	}

	//Assignable
	private static void processAssignableRelation_TFxTF(){
		k = createNewIndividual("Binding");
		p = createNewIndividual("Binded_Input/Output");
		q = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(a, componentOf, p);
		model.add(stmt);

		stmt = model.createStatement(b, componentOf, q);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, p);
		model.add(stmt);

		stmt = model.createStatement(k, rel, q);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}

	private static void processAssignableRelation_TFxPort(){
		k = createNewIndividual("Binding");
		q = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(a, componentOf, q);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, q);
		model.add(stmt);

		stmt = model.createStatement(k, rel, b);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}

	private static void processAssignableRelation_PortxTF(){
		k = createNewIndividual("Binding");
		p = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(b, componentOf, p);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);

		stmt = model.createStatement(k, rel, p);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}

	private static void processAssignableRelation_PortxPort(){
		k = createNewIndividual("Binding");

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);

		stmt = model.createStatement(k, rel, b);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}
}
