package br.com.padtec.transformation.sindel.processor;

import br.com.padtec.trasnformation.sindel.Sindel2OWL;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;

public class AttributeProcessor {

	private static Individual x;
	private static OntProperty dp;
	private static Literal value;
	private static String stringURI = "http://www.w3.org/2001/XMLSchema#string";
	private static String intURI = "http://www.w3.org/2001/XMLSchema#integer";
	
	public static void processLocationTFString(OntModel model, String ClassNS, String IndNS, String locations){

		String[] lin = locations.split(";");
		for (String s : lin) {
			String[] vars = s.split(":");
			if(Sindel2OWL.hashIndividuals.get(vars[0]).equalsIgnoreCase("pm") || Sindel2OWL.hashIndividuals.get(vars[0]).equalsIgnoreCase("sn")){
				//Exception
				Sindel2OWL.error += "\nLocation attribution aborted: Subnetworks and Physical Medias do not have location;";
			}else{
				x  = model.getIndividual(IndNS+vars[0]);
				dp = model.getDatatypeProperty(ClassNS+"Geographical_Element_With_Alias.location");
				value = model.createTypedLiteral(vars[1],stringURI);
				x.addProperty(dp, value);
			}
		}
	}

	public static void processLocationTFGeolocalization(OntModel model, String ClassNS, String IndNS, String locations){
		String[] lin = locations.split(";");
		for (String s : lin) {
			String[] geos = s.split(":");
			if(Sindel2OWL.hashIndividuals.get(geos[0]).equalsIgnoreCase("pm") || Sindel2OWL.hashIndividuals.get(geos[0]).equalsIgnoreCase("sn")){
				//Exception
				Sindel2OWL.error +="\nLocation attribution aborted: Subnetworks and Physical Medias do not have location;";
			}else{
				x  = model.getIndividual(IndNS+geos[0]);
				String [] vars = geos[1].split("\\+");
				String [] params = vars[0].split("\\*");

				dp = model.getDatatypeProperty(ClassNS+"Defined_Geographical_Element.latitude.degree");
				value = model.createTypedLiteral(params[0],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(ClassNS+"Defined_Geographical_Element.latitude.minute");
				value = model.createTypedLiteral(params[1],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(ClassNS+"Defined_Geographical_Element.latitude.second");
				value = model.createTypedLiteral(params[2],intURI);
				x.addProperty(dp, value);

				params = vars[1].split("\\*");

				dp = model.getDatatypeProperty(ClassNS+"Defined_Geographical_Element.longitude.degree");
				value = model.createTypedLiteral(params[0],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(ClassNS+"Defined_Geographical_Element.longitude.minute");
				value = model.createTypedLiteral(params[1],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(ClassNS+"Defined_Geographical_Element.longitude.second");
				value = model.createTypedLiteral(params[2],intURI);
				x.addProperty(dp, value);
			}
		}
	}

	public static void processTypeTF(OntModel model, String ClassNS, String IndNS, String types){
		OntProperty y;
		String[] lin = types.split(";");
		for (String s : lin) {
			String[] vars = s.split(":");
			x = model.getIndividual(IndNS+vars[0]);
			y = model.getDatatypeProperty(ClassNS+"Termination_Function.type");
			value = model.createTypedLiteral(vars[1],stringURI);
			x.addProperty(y, value);		    
		}
	}
}