package br.com.padtec.common.types;

public enum OntCardinalityEnum 
{	
	SOME, MIN, MAX, EXACTLY, COMPLETE;
	
	public String getOwlAxiom() {
		String ret = "";
		switch (this) {
			case SOME:
				ret = "someValuesFrom";
				break;
			case MIN:
				ret = "minQualifiedCardinality";
				break;
			case MAX:
				ret = "maxQualifiedCardinality";
				break;
			case EXACTLY:
				ret = "qualifiedCardinality";
				break;
			case COMPLETE:
				ret = "";
				break;
			default:
				ret = super.toString(); 
		}
		
		return ret;
	}
}
