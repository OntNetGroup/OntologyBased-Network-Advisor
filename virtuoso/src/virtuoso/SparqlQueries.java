package virtuoso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX myOntology: <" + ontologyUriHashTag + ">"
				+ "\n";
		
		this.connection = new ConnectionFactory().getConnection();
		this.st = connection.createStatement();
		
		
	}
	
	public ResultSet getAllTypesOfAnIndividual(String individualUri) throws SQLException{
		String sparqlGetAllTypesOfAnIndividual = ""
									+ "sparql \n"
									+ defineInference
									+ prefixes
									+ "SELECT * \n"
									+ "WHERE \n"
									+ "{ \n" 
									+ "\t" + "myOntology:" + individualUri + " rdf:type " + "?type \n"
									+ "}";
		
		ResultSet results = this.st.executeQuery(sparqlGetAllTypesOfAnIndividual);
		
		return results;
	}
	
}
