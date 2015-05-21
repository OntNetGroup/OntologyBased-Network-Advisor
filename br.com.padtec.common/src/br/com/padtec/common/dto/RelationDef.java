package br.com.padtec.common.dto;

public class RelationDef {
	String objectProperty;
	String originalDomain;
	CardinalityDef cardOnDomain;
	String originalRange;
	CardinalityDef cardOnRange;
	String possibleDomain;
	String possibleRange;
	
	public String getObjectProperty() {
		return objectProperty;
	}
	public void setObjectProperty(String objectProperty) {
		this.objectProperty = objectProperty;
	}
	public String getOriginalDomain() {
		return originalDomain;
	}
	public void setOriginalDomain(String originalDomain) {
		this.originalDomain = originalDomain;
	}
	public CardinalityDef getCardOnDomain() {
		return cardOnDomain;
	}
	public void setCardOnDomain(CardinalityDef cardOnDomain) {
		this.cardOnDomain = cardOnDomain;
	}
	public String getOriginalRange() {
		return originalRange;
	}
	public void setOriginalRange(String originalRange) {
		this.originalRange = originalRange;
	}
	public CardinalityDef getCardOnRange() {
		return cardOnRange;
	}
	public void setCardOnRange(CardinalityDef cardOnRange) {
		this.cardOnRange = cardOnRange;
	}
	public String getPossibleDomain() {
		return possibleDomain;
	}
	public void setPossibleDomain(String possibleDomain) {
		this.possibleDomain = possibleDomain;
	}
	public String getPossibleRange() {
		return possibleRange;
	}
	public void setPossibleRange(String possibleRange) {
		this.possibleRange = possibleRange;
	}
	
	
}
