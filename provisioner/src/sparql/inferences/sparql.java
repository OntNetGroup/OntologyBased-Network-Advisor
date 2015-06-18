package sparql.inferences;

import br.com.padtec.common.queries.QueryUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class sparql {
	public static RuleList test(OntModel model){
		System.out.println("\nExecuting test()...");
		String queryString = ""
				+ QueryUtil.PREFIXES
				+ "PREFIX swrl: <http://www.w3.org/2003/11/swrl#> \n"
				+ "select * \n"
				+ "where \n"
				+ "{ \n"
				+ "		?rule rdf:type swrl:Imp . \n"
				+ "		?rule ?atomListRel ?atomList . \n"
				+ "		FILTER ( ?atomListRel IN (swrl:body, swrl:head) ) . \n"
				+ "		?atomList rdf:rest*/rdf:first ?atom . \n"
				+ "		?atom rdf:type ?atomType . \n"
				+ "		OPTIONAL { \n"
				+ "			?atom ?predicateType ?predicate . \n"
				+ "			FILTER ( ?predicateType IN (swrl:propertyPredicate, swrl:classPredicate) ) . \n"
				+ "		} \n"
				+ "		?atom swrl:argument1 ?arg1 . \n"
				+ "		OPTIONAL {"
				+ "			?atom swrl:argument2 ?arg2 \n"
				+ "		} . \n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();	
		RuleList result = new RuleList();
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    String ruleStr = row.get("rule").toString();	
		    Rule rule = result.getRuleList(ruleStr);
		    
		    String atomListRelStr = row.get("atomListRel").toString();
		    AtomList atomList = rule.getAtomList(atomListRelStr);
		    
		    String atomStr = row.get("atom").toString();
		    Atom atom = atomList.getAtom(atomStr);
		    
		    String atomType = row.get("atomType").toString();
		    atom.setType(atomType);
		    String predicateType = row.get("predicateType").toString();
		    atom.setPredicateType(predicateType);
		    String predicate = row.get("predicate").toString();
		    atom.setPredicate(predicate);
		    String arg1 = row.get("arg1").toString();
		    atom.setArgument1(arg1);
		    String arg2 = row.get("arg2").toString();
		    atom.setArgument2(arg2);		    
		    
		}
		return result;
	}
}
