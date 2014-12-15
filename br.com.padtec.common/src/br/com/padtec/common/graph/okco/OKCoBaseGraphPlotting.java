package br.com.padtec.common.graph.okco;

import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;

public abstract class OKCoBaseGraphPlotting {
	
	protected String VERDE = "#00FF00";
	protected String AZUL = "#0000FF";
	protected String ROXO = "#FF00FF";
	protected String VERMELHO = "#FF0000";

	public int width = 800;
	public int height = 600;
	
	//hash<individual,{classes}>
	public HashMap<String,ArrayList<String>> hash = null;
	
	public String getArborStructureComingOutOf(InfModel ontology, String centerIndividual){
		String query = OKCoQueryManager.getAllRelationsComingOutOf(centerIndividual);
		ResultSet resultSet = OKCoQueryManager.runQuery(ontology, query);

		OKCoArborParser arborParser = new OKCoArborParser(ontology,this);
		String arborStructure = arborParser.getArborJsStringFor(resultSet, centerIndividual);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	public String getArborStructureComingInOf(InfModel ontology, String centerIndividual){
		String query = OKCoQueryManager.getAllRelationsComingInOf(centerIndividual);
		ResultSet resultSet = OKCoQueryManager.runQuery(ontology, query);

		OKCoArborParser arborParser = new OKCoArborParser(ontology,this);
		String arborStructure = arborParser.getArborJsStringFor(resultSet, centerIndividual);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	public String getArborStructureFor(InfModel ontology){
		
		String query = OKCoQueryManager.getPropertiesBetweenAllIndividuals();
		ResultSet resultSet = OKCoQueryManager.runQuery(ontology, query);

		OKCoArborParser arborParser = new OKCoArborParser(ontology,this);
		String arborStructure = arborParser.getArborJsString(resultSet,true);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	protected String callBack(String arborStructure, String arborHashStructure){
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
