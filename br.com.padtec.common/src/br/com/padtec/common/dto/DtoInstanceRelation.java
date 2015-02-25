package br.com.padtec.common.dto;


public class DtoInstanceRelation{
	
	public String Source = new String();
	public String Property = new String();
	public String Target = new String();;
	public String Inverse = new String();
	
	public String getInverse() {
		return Inverse;
	}

	public DtoInstanceRelation() {
		super();
	}

	public void setInverse(String inverse) {
		Inverse = inverse;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DtoInstanceRelation)){
			return false;
		}
		
		DtoInstanceRelation objDto = (DtoInstanceRelation)obj;
		
		if(this.Source.equals(objDto.Source) && this.Property.equals(objDto.Property) && this.Target.equals(objDto.Target)){
			return true;
		}
		
		return false;
	}
	
	public DtoInstanceRelation(String Source, String Property, String Target) {
		this.Source = Source;
		this.Property = Property;
		this.Target = Target;
	}

}
