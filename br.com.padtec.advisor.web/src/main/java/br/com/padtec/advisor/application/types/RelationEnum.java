package br.com.padtec.advisor.application.types;

public enum RelationEnum {

	MAPS_INPUT("maps_input"),
	MAPS_OUTPUT("maps_output"),
	BINDS("binds"),
	SITE_CONNECTS("site_connects"),
	HAS_EQUIPMENT("has_equipament"),
	INTERFACE_BINDS("interface_binds");
	
	private String relation;
	private String domain = new String();
	private String range = new String();
	
	RelationEnum(String relation)
	{
		this.relation = relation;
	}
	
	RelationEnum(String relation, String domain, String range)
	{
		this.relation = relation;
		this.domain = domain;
		this.range = range;
	}
	
	public String relation() { return relation; }  
	public String domain() { return domain; }
	public String range() { return range; }
	
	public static void main (String args[])
	{
		for(RelationEnum r: RelationEnum.values()){
			System.out.println(r.relation);
		}
	}
}
