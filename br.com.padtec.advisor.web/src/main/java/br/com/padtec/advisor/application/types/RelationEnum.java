package br.com.padtec.advisor.application.types;


public enum RelationEnum {

	// ==============================
	// Relations from the Ontology
	// ==============================
	
	MAPS_INPUT("maps_input"),
	MAPS_OUTPUT("maps_output"),
	BINDS("binds"),
	SITE_CONNECTS("site_connects"),
	HAS_EQUIPMENT("has_equipament"),
	INTERFACE_BINDS("interface_binds"),
	COMPONENTOF("componentOf"),	
	BINDING_IS_REPRESENTED_BY("binding_is_represented_by"),
	IS_BINDING("is_binding"),
	HAS_FORWARDING("has_forwarding"),	
		
	// ==============================
	// Inverses from the Ontology
	// ==============================
	
	INV_COMPONENTOF("INV.componentOf"),
	INV_BINDING_IS_REPRESENTED_BY("INV.binding_is_represented_by"),
	INV_IS_BINDING("INV.is_binding"),
	INV_BINDS("INV.binds"),
	INV_MAPS_OUTPUT("INV.maps_output"),
	INV_MAPS_INPUT("INV.maps_input"),
	
	// ==============================
	// New relations to be generated...
	// ==============================
	
	FORWARDING_FROM_UNI_PM_NC("Forwarding_from_Uni_PM_NC"),
	FORWARDING_TO_UNI_PM_NC("Forwarding_to_Uni_PM_NC"),
	IS_REPRESENTED_BY_UNI_ACCESS_TRANSPORT_ENTITY("is_represented_by_Uni_Access_Transport_Entity"),
	IS_REPRESENTED_BY_UNI_PATH_NC("is_represented_by_Uni_Path_NC"),
	FORWARDING_FROM_UNI_PATH_NC("Forwarding_from_Uni_Path_NC"),
	FORWARDING_TO_UNI_PATH_NC("Forwarding_to_Uni_Path_NC"),
	FORWARDING_FROM_UNI_ACCESS_TRANSPORT_ENTITY("Forwarding_from_Uni_Access_Transport_Entity"),	
	FORWARDING_TO_UNI_ACCESS_TRANSPORT_ENTITY("Forwarding_to_Uni_Access_Transport_Entity"),
	IS_REPRESENTED_BY_SO_A_FEP("is_represented_by_So_A-FEP");
	
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
