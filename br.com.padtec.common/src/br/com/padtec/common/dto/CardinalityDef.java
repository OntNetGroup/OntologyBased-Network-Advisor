package br.com.padtec.common.dto;

import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;

public class CardinalityDef {
	String domainClass;
	String objectProperty;
	String rangeType;
	int lowerBound = 0;
	int upperBound = -1;
	
	String owlUri = OWL2.getURI();
	String someValuesFrom = OWL2.someValuesFrom.toString().replace(owlUri, "");
	String minQualifiedCardinality = OWL2.minQualifiedCardinality.toString().replace(owlUri, "");
	String maxQualifiedCardinality = OWL2.maxQualifiedCardinality.toString().replace(owlUri, "");
	String qualifiedCardinality = OWL2.qualifiedCardinality.toString().replace(owlUri, "");
	
	public String getDomainClass() {
		return domainClass;
	}
	public void setDomainClass(String domainClass) {
		this.domainClass = domainClass;
	}
	public String getObjectProperty() {
		return objectProperty;
	}
	public void setObjectProperty(String objectProperty) {
		this.objectProperty = objectProperty;
	}
	public String getRangeType() {
		return rangeType;
	}
	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}
	public int getLowerBound() {
		return lowerBound;
	}
	public int getUpperBound() {
		return upperBound;
	}
	public void setBounds(String owlCardinality, int bound) {
		System.out.println(owlCardinality);
		if(owlCardinality.equals(OWL.getURI()+someValuesFrom)){
			this.lowerBound = 1;
			this.upperBound = -1;
		}else if(owlCardinality.equals(OWL.getURI()+minQualifiedCardinality)){
			this.lowerBound = bound;
		}else if(owlCardinality.equals(OWL.getURI()+maxQualifiedCardinality)){
			this.upperBound = bound;
		}else if(owlCardinality.equals(OWL.getURI()+qualifiedCardinality)){
			this.lowerBound = bound;
			this.upperBound = bound;
		}		
	}
	
	@Override
	public String toString() {
		String ret = ""
				+ "<" + domainClass.replace("http://nemo.inf.ufes.br/NewProject.owl#", "") + "> "
				+ "<" + objectProperty.replace("http://nemo.inf.ufes.br/NewProject.owl#", "")+ "> "
				+ "<" + rangeType.replace("http://nemo.inf.ufes.br/NewProject.owl#", "")+ "> "
				+ "\nlowerBound: " + lowerBound
				+ "\nupperBound: " + upperBound;
		
		return ret;
	}
}
