package virtuoso;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SparqlQueries sparqlQueries = new SparqlQueries("http://www.semanticweb.org/fredd_000/ontologies/2014/9/untitled-ontology-225");
		
		ResultSet results = sparqlQueries.getAllTypesOfAnIndividual("c");
		while (results.next()) {
			String type = results.getString("type");
			//className = className.replace(SparqlQueries.g800UriHashTag, "");
			System.out.println(type);
		}

	}

}
