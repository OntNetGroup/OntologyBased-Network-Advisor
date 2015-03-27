package br.com.padtec.nopen.core.types;

public enum RelationEnum {

	APPLIES("applies"),
	COMPONENTOF3("ComponentOf3");
	
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
	
	@Override
	public String toString() {
		return relation();
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
