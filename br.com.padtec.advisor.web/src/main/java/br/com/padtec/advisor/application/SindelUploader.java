package br.com.padtec.advisor.application;

import java.util.List;

import br.com.padtec.advisor.application.util.ApplicationQueryUtil;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.trasnformation.sindel.Sindel2OWL;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class SindelUploader {

	public static String sindelCode = new String();
	
	public static void uploadSindelModel(String sindelCode)
	{
		SindelUploader.sindelCode = sindelCode;
	}
	
	public static DtoResult transformSindelToLoadedOwl()
	{
		OntModel basemodel = OKCoUploader.getBaseModel();
		String namespace = basemodel.getNsPrefixURI("");
		
		Sindel2OWL so = new Sindel2OWL(basemodel);
		so.run(sindelCode);
		
		/** ==================================================== 
		  * Set specific types for connects relations 
		  * ==================================================== */		
		List<String[]> list = ApplicationQueryUtil.getDomainAndRangeURI(basemodel,namespace+"has_forwarding");
		String specificRelation = null;		
		for(String[] st : list)
		{
			specificRelation = null;			
			List<String> st0Types = QueryUtil.getClassesURI(basemodel,st[0]);
			List<String> st1Types = QueryUtil.getClassesURI(basemodel,st[1]);			
			if(st0Types.contains(namespace+"Source_AP") && (st1Types.contains(namespace+"Sink_AP")))
			{
				specificRelation = "Forwarding_Unidirectional_Access_Transport_Entity";
			}
			else if(st0Types.contains(namespace+"Source_A-FEP") && (st1Types.contains(namespace+"Sink_A-FEP")))
			{
				specificRelation = "Forwarding_Path_NC";
			}
			else if(st0Types.contains(namespace+"Source_PM-FEP") && (st1Types.contains(namespace+"Sink_PM-FEP")))
			{
				specificRelation = "Forwarding_Path_NC";
			}			
			if(specificRelation != null)
			{
				ObjectProperty rel = basemodel.getObjectProperty(namespace+specificRelation);
				Statement stmt = basemodel.createStatement(basemodel.getIndividual(st[0]), rel, basemodel.getIndividual(st[1]));
				basemodel.add(stmt);
			}
		}
	
		return OKCoReasoner.runReasoner(OKCoUploader.reasonOnLoading);
	}
}
