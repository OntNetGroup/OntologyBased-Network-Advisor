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
	BINDS_SO_A_FEP_TO("binds_So_A-FEP_to"),
	BINDS_SO_A_FEP_FROM("binds_So_A-FEP_from"),
	IS_REPRESENTED_BY_SO_M_FEP("is_represented_by_So_M-FEP"),
	BINDS_SO_M_FEP_TO("binds_So_M-FEP_to"),		
	BINDS_SO_M_FEP_FROM("binds_So_M-FEP-from"),	
	IS_REPRESENTED_BY_SO_SN_FEP("is_represented_by_So_SN-FEP"),
	BINDS_SO_SN_FEP_TO("binds_So_SN-FEP_to"),
	BINDS_SO_SN_FEP_FROM("binds_So_SN-FEP_from"),
	IS_REPRESENTED_BY_SO_LPF_FEP("is_represented_by_So_LPF-FEP"),
	BINDS_SO_LPF_FEP_TO("binds_So_LPF-FEP_to"),
	BINDS_SO_LPF_FEP_FROM("binds_So_LPF-FEP-from"),
	IS_REPRESENTED_BY_SK_A_FEP("is_represented_by_Sk_A-FEP"),
	BINDS_SK_A_FEP_FROM("binds_Sk_A-FEP_from"),
	BINDS_SK_A_FEP_TO("binds_Sk_A-FEP_to"),
	IS_REPRESENTED_BY_SK_M_FEP("is_represented_by_Sk_M-FEP"),
	BINDS_SK_M_FEP_TO("binds_Sk_M-FEP_to"),
	BINDS_SK_M_FEP_FROM("binds_Sk_M-FEP_from"),
	IS_REPRESENTED_BY_SK_SN_FEP("is_represented_by_Sk_SN-FEP"),
	BINDS_SK_SN_FEP_TO("binds_Sk_SN-FEP_to"),
	BINDS_SK_SN_FEP_FROM("binds_Sk_SN-FEP_from"),
	IS_REPRESENTED_BY_SK_LPF_FEP("is_represented_by_Sk_LPF-FEP"),
	BINDS_SK_LPF_FEP_TO("binds_Sk_LPF-FEP_to"),
	BINDS_SK_LPF_FEP_FROM("binds_Sk_LPF-FEP_from"),
	IS_REPRESENTED_BY_SO_PM_FEP("is_represented_by_So_PM-FEP"),
	BINDS_SO_PM_FEP_TO("binds_So_PM-FEP_to"),
	BINDS_SO_PM_FEP_FROM("binds_So_PM-FEP_from"),
	IS_REPRESENTED_BY_SK_PM_FEP("is_represented_by_Sk_PM-FEP"),
	BINDS_SK_PM_FEP_FROM("binds_Sk_PM-FEP_from"),
	BINDS_SK_PM_FEP_TO("binds_Sk_PM-FEP_to"),
	IS_REPRESENTED_BY_SO_AP("is_represented_by_So_AP"),
	BINDS_SO_AP_TO("binds_So_AP_to"),
	BINDS_SO_AP_FROM("binds_So_AP_from"),
	IS_REPRESENTED_BY_SK_AP("is_represented_by_Sk_AP"),
	BINDS_SK_AP_FROM("binds_Sk_AP_from"),
	BINDS_SK_AP_TO("binds_Sk_AP_to"),
	IS_REPRESENTED_BY_SO_L_FP("is_represented_by_So_L-FP"),
	BINDS_SO_L_FP_TO("binds_So_L-FP_to"),
	BINDS_SO_L_FP_FROM("binds_So_L-FP_from"),
	IS_REPRESENTED_BY_SK_L_FP("is_represented_by_Sk_L-FP"),
	BINDS_SK_L_FP_TO("binds_Sk_L-FP_to"),
	BINDS_SK_L_FP_FROM("binds_Sk_L-FP_from"),
	IS_REPRESENTED_BY_A_FP("is_represented_by_A-FP"),
	BINDS_A_FP_TO("binds_A-FP_to"),
	BINDS_A_FP_FROM("binds_A-FP_from"),
	IS_REPRESENTED_BY_SO_SN_FP("is_represented_by_So_SN-FP"),
	BINDS_SO_SN_FP_TO("binds_So_SN-FP_to"),
	BINDS_SO_SN_FP_FROM("binds_So_SN-FP_from"),
	IS_REPRESENTED_BY_SK_SN_FP("is_represented_by_Sk_SN-FP"),
	BINDS_SK_SN_FP_TO("binds_Sk_SN-FP_to"),
	BINDS_SK_SN_FP_FROM("binds_Sk_SN-FP_from"),
	IS_REPRESENTED_BY_SO_M_FP("is_represented_by_So_M-FP"),
	BINDS_SO_M_FP_TO("binds_So_M-FP_to"),
	BINDS_SO_M_FP_FROM("binds_So_M-FP_from"),
	IS_REPRESENTED_BY_SK_M_FP("is_represented_by_Sk_M-FP"),
	BINDS_SK_M_FP_TO("binds_Sk_M-FP_to"),
	BINDS_SK_M_FP_FROM("binds_Sk_M-FP_from"),
		
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
