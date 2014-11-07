package virtuoso;

import instances.IndividualInstance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SparqlQueries {
	Connection connection;
	Statement st;
	
	public String ontologyUri;
	public String ontologyUriHashTag;
	public final String defineInference;
	public final String prefixes;
	
	
	public SparqlQueries(String ontologyUri) throws ClassNotFoundException, SQLException {
		this.ontologyUri = ontologyUri;
		this.ontologyUriHashTag = this.ontologyUri + "#";
		
		this.defineInference = ""
				+ "DEFINE input:inference <" + ontologyUri + ">"
				+ "\n";
		
		this.prefixes = 	""
				+ "PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "\n"
				+ "PREFIX myOntology: <" + ontologyUriHashTag + ">"
				+ "\n";
		
		this.connection = new ConnectionFactory().getConnection();
		this.st = connection.createStatement();
	}
	
	public ResultSet getAllIndividualInstanceClasses(String individualUri) throws SQLException{
		String sparql = ""
					+ "sparql\n"
					+ defineInference
					+ prefixes
					+ "SELECT * \n"
					+ "WHERE \n"
					+ "{ \n" 
					+ "\t" + "<" + individualUri + ">" + " rdfs:type " + "?class \n"
					+ "}";
		
		ResultSet results = this.st.executeQuery(sparql);
		
		return results;
	}
	
	public ResultSet getAllIndividualInstanceObjPropAsSource(String individualUri) throws SQLException{
		String sparql = ""
					+ "sparql\n"
					+ defineInference
					+ prefixes
					+ "SELECT * \n"
					+ "WHERE \n"
					+ "{ \n"
					+ "\t" + "<" + individualUri + ">" + " ?objProp " + "?target .\n"
					+ "\t" + "?objProp rdfs:type owl:ObjectProperty \n"
					+ "}";
		
		ResultSet results = this.st.executeQuery(sparql);
		
		return results;
	}
	
	public ResultSet getAllIndividualInstanceObjPropAsTarget(String individualUri) throws SQLException{
		String sparql = ""
					+ "sparql\n"
					+ defineInference
					+ prefixes
					+ "SELECT * \n"
					+ "WHERE \n"
					+ "{ \n"
					+ "\t" + "?source ?objProp "  + "<" + individualUri + ">" + " .\n"
					+ "\t" + "?objProp rdfs:type owl:ObjectProperty \n"
					+ "}";
		
		ResultSet results = this.st.executeQuery(sparql);
		
		return results;
	}
	
	public ResultSet getAllIndividuals() throws SQLException{
		String sparql = ""
				+ "sparql\n"
				+ defineInference
				+ prefixes
				+ "SELECT * \n"
				+ "WHERE \n"
				+ "{ \n"
				+ "\t?individualIri rdfs:type owl:NamedIndividual\n"
				+ "}";
		
		ResultSet results = this.st.executeQuery(sparql);
		
		return results;
		
	}
	
	public ArrayList<IndividualInstance> getAllIndividualInstances(Boolean getClassesEagerly, Boolean getObjectPropertiesEagerly, Boolean getDataPropertiesEagerly) throws Exception {
		ArrayList<IndividualInstance> individualInstanceList = new ArrayList<IndividualInstance>();
		
		ResultSet resSetIndividuals = getAllIndividuals();
		
		while(resSetIndividuals.next()){
			String individualIri = resSetIndividuals.getString("individualIri");
			
			IndividualInstance individualInstance = new IndividualInstance(individualIri);
			
			individualInstanceList.add(individualInstance);
		}
		
		for (IndividualInstance individualInstance : individualInstanceList) {
			if(getClassesEagerly){
				individualInstance.searchForClasses(this);
			}
			
			if(getObjectPropertiesEagerly){
				individualInstance.searchForObjectPropertiesAsSource(this);
				individualInstance.searchForObjectPropertiesAsTarget(this);
			}
			
			if(getDataPropertiesEagerly){
				individualInstance.searchForDataProperties(this);
			}			
		}		
		
		return individualInstanceList;
		
	}
	
}
