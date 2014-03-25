package br.inf.nemo.padtec.graphplotting;

import java.util.ArrayList;
import java.util.HashMap;

import br.inf.nemo.padtec.manager.query.QueryManager;
import br.inf.nemo.padtec.owl2arborjs.ArborParser;
import br.inf.nemo.padtec.wokco.WOKCOGraphPlotting;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public abstract class GraphPlotting {
	public static void main(String[] args) {
		String modelPath = "combiner-p2_inferred.owl";
		String individual = "http://www.semanticweb.org/individuous.owl#ttf1";
		//Colocar no getTuplas as properties repetidas Tupla{Mick likes,has_pet,is_pet_of Rex)
//		String modelPath = "assassinato.owl";
//		String individual = "http://www.semanticweb.org/ontologies/2013/8/ontology.owl#fabio";

//		String modelPath = "assassinato_infered.owl";
//		String individual = "http://www.semanticweb.org/ontologies/2013/8/ontology.owl#fabio";
		
//		String modelPath = "Combiner 4x1 - p3_inferred.owl";
//		String individual = "http://www.semanticweb.org/individuous.owl#ODUj_TTF2";
		
//		String modelPath = "pizza.owl";
//		String individual = "http://www.semanticweb.org/individuous.owl#ODUj_TTF1";
		
//		String modelPath = "demo-ontologies/classes-as-values.owl";
//		String individual = "http://www.owl-ontologies.com/unnamed.owl#Lions_in_the_Life_of_the_Pride";
		
//		String modelPath = "mged.owl";
//		String individual = "http://cohse.semanticweb.org/ontologies/people#Rex";
		
//		String modelPath = "untitled-ontology-131.owl";
//		String individual = "http://persons.iis.nsk.su/files/persons/pages/einsteins_riddle.owl#house-1";
		
		
		Model model = FileManager.get().loadModel(modelPath);

		InfModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		ontModel.add(model);
		
		GraphPlotting graphPlotting = new WOKCOGraphPlotting();
//		GraphPlotting graphPlotting = new SOKCOGraphPlotting();
		
//			String arborStructure = graphPlotting.getArborStructureComingOutOf(ontModel, individual);
//			String arborStructure = graphPlotting.getArborStructureComingInOf(ontModel, individual);
			String arborStructure = graphPlotting.getArborStructureFor(ontModel);
			System.out.println(arborStructure);
			System.out.println("\n\nwidth=\""+graphPlotting.width+"\" height=\""+graphPlotting.height+"\"");
	}
	
	
	
	protected String VERDE = "#00FF00";
	protected String AZUL = "#0000FF";
	protected String ROXO = "#FF00FF";
	protected String VERMELHO = "#FF0000";

	public int width = 800;
	public int height = 600;
	
	//hash<individual,{classes}>
	public HashMap<String,ArrayList<String>> hash = new HashMap<String, ArrayList<String>>();
	
	public String getArborStructureComingOutOf(InfModel ontology, String centerIndividual){
		String query = QueryManager.getAllRelationsComingOutOf(centerIndividual);
		ResultSet resultSet = QueryManager.runQuery(ontology, query);

		ArborParser arborParser = new ArborParser(ontology,this);
		String arborStructure = arborParser.getArborJsStringFor(resultSet, centerIndividual);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	public String getArborStructureComingInOf(InfModel ontology, String centerIndividual){
		String query = QueryManager.getAllRelationsComingInOf(centerIndividual);
		ResultSet resultSet = QueryManager.runQuery(ontology, query);

		ArborParser arborParser = new ArborParser(ontology,this);
		String arborStructure = arborParser.getArborJsStringFor(resultSet, centerIndividual);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	public String getArborStructureFor(InfModel ontology){
		String query = QueryManager.getPropertiesBetweenAllIndividuals();
		ResultSet resultSet = QueryManager.runQuery(ontology, query);

		ArborParser arborParser = new ArborParser(ontology,this);
		String arborStructure = arborParser.getArborJsString(resultSet);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	private String callBack(String arborStructure, String arborHashStructure){
		return "	"
				+ "function addNodes(graph){"
				+ arborStructure
				+ "}"
				+ ""
				+ "function getHash(){"
				+ "		var hash = {};"
				+ arborHashStructure
				+ "		return hash;"
				+ "}";
	}
	
	public abstract String getArborNode(String elem, boolean isCenterNode);
	public abstract boolean isClass(String elem);
	public abstract String getSubtitle();
}
