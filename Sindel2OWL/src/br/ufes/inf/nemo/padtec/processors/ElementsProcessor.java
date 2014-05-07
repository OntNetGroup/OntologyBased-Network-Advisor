package br.ufes.inf.nemo.padtec.processors;

import java.util.ArrayList;

import br.ufes.inf.nemo.padtec.Sindel2OWL;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class ElementsProcessor {

	public static void processElements(OntModel model, String ClassNS, String IndNS, String elements){
		String[] declarations = elements.split(";");
		ArrayList<OntClass> lst;
		for (String declaration : declarations) {
			String[] elems = declaration.split(":");
			String[] vars = elems[1].split(",");
			OntClass o_class;
			boolean isTF = false;
			for (String var : vars) {
				o_class = null;
				isTF = false;
				
				//Termination Functions
				if(elems[0].equals("tf")){
					o_class = model.getOntClass(ClassNS+"Termination_Function");
				}else if(elems[0].equals("so-tf")){
					o_class = model.getOntClass(ClassNS+"Termination_Source");
				}else if(elems[0].equals("sk-tf")){
					o_class = model.getOntClass(ClassNS+"Termination_Sink");
				}else if(elems[0].equals("bi-tf")){
					o_class = model.getOntClass(ClassNS+"Bidirectional_Termination");
				}
				
				//Adaptation Functions
				else if(elems[0].equals("af")){
					o_class = model.getOntClass(ClassNS+"Adaptation_Function");
				}else if(elems[0].equals("so-af")){
					o_class = model.getOntClass(ClassNS+"Adaptation_Source");
				}else if(elems[0].equals("sk-af")){
					o_class = model.getOntClass(ClassNS+"Adaptation_Sink");
				}else if(elems[0].equals("bi-af")){
					o_class = model.getOntClass(ClassNS+"Bidirectional_Adaptation");
				}
				
				//Layer Processor Functions
				else if(elems[0].equals("lpf")){
					o_class = model.getOntClass(ClassNS+"Layer_Processor_Function");
				}else if(elems[0].equals("so-lpf")){
					o_class = model.getOntClass(ClassNS+"Layer_Processor_Function_Source");
				}else if(elems[0].equals("sk-lpf")){
					o_class = model.getOntClass(ClassNS+"Layer_Processor_Function_Sink");
				}else if(elems[0].equals("bi-lpf")){
					o_class = model.getOntClass(ClassNS+"Bidirectional_Layer_Processor_Function");
				}
				
				//Matrices
				else if(elems[0].equals("matrix")){
					o_class = model.getOntClass(ClassNS+"Matrix");
				}else if(elems[0].equals("uni-matrix")){
					o_class = model.getOntClass(ClassNS+"Unidirectional_Matrix");
				}else if(elems[0].equals("so-matrix")){
					lst = new ArrayList<OntClass>();

					lst.add(model.getOntClass(ClassNS+"Source_Matrix"));
					lst.add(model.getOntClass(ClassNS+"Unidirectional_Sink-Source_Matrix"));

					UnionClass unionClass = model.createUnionClass(null, model.createList(lst.toArray(new RDFNode[]{})));
					unionClass.createIndividual(IndNS+var);
				}else if(elems[0].equals("sk-matrix")){
					lst = new ArrayList<OntClass>();

					lst.add(model.getOntClass(ClassNS+"Sink_Matrix"));
					lst.add(model.getOntClass(ClassNS+"Unidirectional_Sink-Source_Matrix"));

					UnionClass unionClass = model.createUnionClass(null, model.createList(lst.toArray(new RDFNode[]{})));
					unionClass.createIndividual(IndNS+var);
				}else if(elems[0].equals("bi-matrix")){
					o_class = model.getOntClass(ClassNS+"Bidirectional_Matrix");
				}
				
				//Subnetwork
				else if(elems[0].equals("sn")){
					o_class = model.getOntClass(ClassNS+"Subnetwork");
				}
				
				//Physical Media
				else if(elems[0].equals("pm")){
					o_class = model.getOntClass(ClassNS+"Physical_Media");
				}
				
				if(o_class != null){
					isTF = true;
				}
				
				
				//Port
				if(elems[0].equals("input")){
					o_class = model.getOntClass(ClassNS+"Input");
				}else if(elems[0].equals("output")){
					o_class = model.getOntClass(ClassNS+"Output");
				}
				
				//Reference Point
				else if(elems[0].equals("rp")){
					o_class = model.getOntClass(ClassNS+"Reference_Point");
				}else if(elems[0].equals("fep")){
					o_class = model.getOntClass(ClassNS+"Forwarding_End_Point");
				}else if(elems[0].equals("ap")){
					o_class = model.getOntClass(ClassNS+"Access_Point");
				}else if(elems[0].equals("fp")){
					o_class = model.getOntClass(ClassNS+"Forwarding_Point");
				}
				
				//Transport Entities
				else if(elems[0].equals("te")){
					o_class = model.getOntClass(ClassNS+"Transport_Entity");
				}else if(elems[0].equals("ate")){
					o_class = model.getOntClass(ClassNS+"Access_Transport_Entity");
				}else if(elems[0].equals("nc")){
					o_class = model.getOntClass(ClassNS+"Network_Connection");
				}else if(elems[0].equals("lc")){
					o_class = model.getOntClass(ClassNS+"Link_Connection");
				}else if(elems[0].equals("mc")){
					o_class = model.getOntClass(ClassNS+"Matrix_Connection");
				}else if(elems[0].equals("cfte")){
					o_class = model.getOntClass(ClassNS+"Channel_Forwarding_Transport_Entity");
				}
				
				//Layer Network
				else if(elems[0].equals("layer")){
					o_class = model.getOntClass(ClassNS+"Layer_Network");
				}
				
				//Equipment
				else if(elems[0].equals("equip")){
					o_class = model.getOntClass(ClassNS+"Equipment");
				}
				
				//Interfaces
				else if(elems[0].equals("in-int")){
					o_class = model.getOntClass(ClassNS+"Input_Interface");
				}else if(elems[0].equals("out-int")){
					o_class = model.getOntClass(ClassNS+"Output_Interface");
				}
				
				//Site
				else if(elems[0].equals("site")){
					o_class = model.getOntClass(ClassNS+"Site");
				}
				
				if(o_class != null){
					Individual i = o_class.createIndividual(IndNS+var);
					if(isTF)
						i.addOntClass(model.getOntClass(ClassNS+"Transport_Function"));
				}
				Sindel2OWL.hashIndividuals.put(var,elems[0]);
			}
		}
	}
}
