package br.com.padtec.common.dto;

public class DtoInstanceRelation{
	
	public String Property;
	public String Target;

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DtoInstanceRelation)){
			return false;
		}
		
		DtoInstanceRelation objDto = (DtoInstanceRelation)obj;
		
		if(this.Property.equals(objDto.Property) && this.Target.equals(objDto.Target)){
			return true;
		}
		
		return false;
	}
}
