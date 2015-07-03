package br.com.padtec.nopen.model;

public enum RelationEnum {

	links_input_FEP_PM_Input_Sk("links_input.FEP.PM_Input_Sk"),
	connects_FEP_FEP("connects.FEP.FEP"),
	INV_links_input_FEP_PM_Input_So("INV.links_input.FEP.PM_Input_So"),
	A_Matrix_MatrixOutput("A_Matrix_MatrixOutput"),
	INV_connects_FEP_FEP("INV.connects.FEP.FEP"),
	INV_is_client_Layer_Type_Layer_Type("INV.is_client"),
	links_output("links_output"),
	INV_A_Matrix_MatrixOutput("INV.A_Matrix_MatrixOutput"),
	INV_links_output_AP_Adaptation_Function_Output("INV.links_output.AP.Adaptation_Function_Output"),
	mc_FP_FEP("mc"),
	INV_links_output_FP_Matrix_Output("INV.links_output.FP.Matrix_Output"),
	A_PhysicalMedia_PMInputSo("A_PhysicalMedia_PMInputSo"),
	links_input_FEP_Matrix_Input("links_input.FEP.Matrix_Input"),
	supervises_Supervisor_Equipment("supervises"),
	A_Slot_Subslot("A_Slot_Subslot"),
	INV_binds_Trail_Termination_Function_Physical_Media("INV.binds.Trail_Termination_Function.Physical_Media"),
	A_TrailTerminationFunction_TrailTerminationFunctionOutput("A_TrailTerminationFunction_TrailTerminationFunctionOutput"),
	binds_Matrix_Adaptation_Function("binds.Matrix.Adaptation_Function"),
	componentOf("componentOf"),
	INV_binds("INV.binds"),
	connects("connects"),
	INV_A_TrailTerminationFunction_TrailTerminationFunctionOutput("INV.A_TrailTerminationFunction_TrailTerminationFunctionOutput"),
	INV_links_input_AP_Trail_Termination_Function_Input("INV.links_input.AP.Trail_Termination_Function_Input"),
	intermediates_up_Transport_Function_Card_Layer("intermediates_up"),
	connects_Reference_Point_Reference_Point("connects.Reference_Point.Reference_Point"),
	INV_is_interface_of_Output_Card_Transport_Function("INV.is_interface_of.Output_Card.Transport_Function"),
	A_Card_OutputCard("A_Card_OutputCard"),
	INV_componentOf("INV.componentOf"),
	INV_intermediates_up_Card_Layer_Transport_Function("INV.intermediates_up"),
	INV_has_path_FEP_AP("INV.has_path.FEP.AP"),
	connects_FP_FP("connects.FP.FP"),
	implements_Technology_Service("implements.Technology.Service"),
	INV_A_PhysicalMedia_PMInputSk("INV.A_PhysicalMedia_PMInputSk"),
	links_output_Reference_Point_Output("links_output.Reference_Point.Output"),
	links_input_FEP_Adaptation_Function_Input("links_input.FEP.Adaptation_Function_Input"),
	INV_implements_Technology_Service("INV.implements.Technology.Service"),
	A_Card_TFCardElement("A_Card_TFCardElement"),
	INV_has_connects_supported_Reference_Point_Reference_Point("INV.has_connects_supported"),
	INV_A_Subslot_Card("INV.A_Subslot_Card"),
	INV_links_output_Reference_Point_Output("INV.links_output.Reference_Point.Output"),
	INV_binds_Matrix_Adaptation_Function("INV.binds.Matrix.Adaptation_Function"),
	INV_intermediates_down_Card_Layer_Transport_Function("INV.intermediates_down"),
	INV_has_path("INV.has_path"),
	INV_A_Card_CardLayer("INV.A_Card_CardLayer"),
	A_Slot_Supervisor("A_Slot_Supervisor"),
	INV_connects_AP_AP("INV.connects.AP.AP"),
	links_output_FEP_Trail_Termination_Function_Output("links_output.FEP.Trail_Termination_Function_Output"),
	A_AdaptationFunction_AdaptationFunctionInput("A_AdaptationFunction_AdaptationFunctionInput"),
	has_connects_supported_Reference_Point_Reference_Point("has_connects_supported"),
	INV_has_path_FP_AP("INV.has_path.FP.AP"),
	INV_A_Card_OutputCard("INV.A_Card_OutputCard"),
	has_path_FEP_AP("has_path.FEP.AP"),
	INV_binds_Trail_Termination_Function_Matrix("INV.binds.Trail_Termination_Function.Matrix"),
	INV_links_output_FEP_Trail_Termination_Function_Output("INV.links_output.FEP.Trail_Termination_Function_Output"),
	links_input_Reference_Point_Input("links_input.Reference_Point.Input"),
	INV_mc_FEP_FP("INV.mc"),
	A_Card_CardLayer("A_Card_CardLayer"),
	binds_Adaptation_Function_Trail_Termination_Function("binds.Adaptation_Function.Trail_Termination_Function"),
	A_Subslot_Supervisor("A_Subslot_Supervisor"),
	INV_is_interface_of("INV.is_interface_of"),
	INV_binds_Trail_Termination_Function_Adaptation_Function("INV.binds.Trail_Termination_Function.Adaptation_Function"),
	INV_A_CardLayer_TrailTerminationFunction("INV.A_CardLayer_TrailTerminationFunction"),
	INV_A_Rack_Shelf("INV.A_Rack_Shelf"),
	binds_Trail_Termination_Function_Matrix("binds.Trail_Termination_Function.Matrix"),
	is_client_Layer_Type_Layer_Type("is_client"),
	connects_AP_AP("connects.AP.AP"),
	INV_binds_Output_Card_Output("INV.binds.Output_Card.Output"),
	INV_links_input_FEP_Matrix_Input("INV.links_input.FEP.Matrix_Input"),
	A_CardLayer_TrailTerminationFunction("A_CardLayer_TrailTerminationFunction"),
	supervises_card_Supervisor_Card("supervises_card"),
	has_path_AP_FEP("has_path.AP.FEP"),
	INV_A_Matrix_MatrixInput("INV.A_Matrix_MatrixInput"),
	INV_A_TransportFunction_Input("INV.A_TransportFunction_Input"),
	INV_A_PhysicalMedia_PMInputSo("INV.A_PhysicalMedia_PMInputSo"),
	INV_A_Slot_Supervisor("INV.A_Slot_Supervisor"),
	INV_binds_Adaptation_Function_Trail_Termination_Function("INV.binds.Adaptation_Function.Trail_Termination_Function"),
	INV_A_AdaptationFunction_AdaptationFunctionInput("INV.A_AdaptationFunction_AdaptationFunctionInput"),
	INV_is_physical_connected_Equipment_Equipment("INV.is_physical_connected"),
	INV_connects("INV.connects"),
	binds("binds"),
	INV_connects_FP_FP("INV.connects.FP.FP"),
	binds_Output_Card_Output("binds.Output_Card.Output"),
	INV_links_input_FEP_PM_Input_Sk("INV.links_input.FEP.PM_Input_Sk"),
	INV_A_Slot_Card("INV.A_Slot_Card"),
	INV_A_Shelf_Slot("INV.A_Shelf_Slot"),
	INV_has_path_Reference_Point_Reference_Point("INV.has_path.Reference_Point.Reference_Point"),
	INV_A_Card_InputCard("INV.A_Card_InputCard"),
	A_Technology_LayerType("A_Technology_LayerType"),
	is_physical_connected_Equipment_Equipment("is_physical_connected"),
	links_output_FP_Matrix_Output("links_output.FP.Matrix_Output"),
	has_path_FP_AP("has_path.FP.AP"),
	instantiates_Card_Layer_Layer_Type("instantiates"),
	INV_supervises_Equipment_Supervisor("INV.supervises"),
	INV_links_input("INV.links_input"),
	has_path("has_path"),
	binds_Input_Card_Input("binds.Input_Card.Input"),
	INV_A_TransportFunction_Output("INV.A_TransportFunction_Output"),
	A_Matrix_MatrixInput("A_Matrix_MatrixInput"),
	is_interface_of_Output_Card_Transport_Function("is_interface_of.Output_Card.Transport_Function"),
	links_input("links_input"),
	A_TrailTerminationFunction_TrailTerminationFunctionInput("A_TrailTerminationFunction_TrailTerminationFunctionInput"),
	links_input_FEP_PM_Input_So("links_input.FEP.PM_Input_So"),
	links_input_AP_Trail_Termination_Function_Input("links_input.AP.Trail_Termination_Function_Input"),
	implements_("implements"),
	A_TransportFunction_Output("A_TransportFunction_Output"),
	INV_connects_Reference_Point_Reference_Point("INV.connects.Reference_Point.Reference_Point"),
	links_input_FP_Adaptation_Function_Input("links_input.FP.Adaptation_Function_Input"),
	implements_Equipment_Technology("implements.Equipment.Technology"),
	INV_is_interface_of_Input_Card_Transport_Function("INV.is_interface_of.Input_Card.Transport_Function"),
	A_PhysicalMedia_PMInputSk("A_PhysicalMedia_PMInputSk"),
	INV_has_path_AP_FEP("INV.has_path.AP.FEP"),
	INV_A_AdaptationFunction_AdaptationFunctionOutput("INV.A_AdaptationFunction_AdaptationFunctionOutput"),
	is_interface_of("is_interface_of"),
	INV_A_Technology_LayerType("INV.A_Technology_LayerType"),
	links_output_AP_Adaptation_Function_Output("links_output.AP.Adaptation_Function_Output"),
	INV_A_Subslot_Supervisor("INV.A_Subslot_Supervisor"),
	is_interface_of_Input_Card_Transport_Function("is_interface_of.Input_Card.Transport_Function"),
	A_Rack_Shelf("A_Rack_Shelf"),
	A_Slot_Card("A_Slot_Card"),
	INV_implements("INV.implements"),
	INV_instantiates_Layer_Type_Card_Layer("INV.instantiates"),
	INV_implements_Equipment_Technology("INV.implements.Equipment.Technology"),
	INV_A_Card_TFCardElement("INV.A_Card_TFCardElement"),
	INV_A_TrailTerminationFunction_TrailTerminationFunctionInput("INV.A_TrailTerminationFunction_TrailTerminationFunctionInput"),
	has_path_Reference_Point_Reference_Point("has_path.Reference_Point.Reference_Point"),
	intermediates_down_Transport_Function_Card_Layer("intermediates_down"),
	INV_links_output("INV.links_output"),
	A_TransportFunction_Input("A_TransportFunction_Input"),
	INV_binds_Input_Card_Input("INV.binds.Input_Card.Input"),
	INV_links_input_FEP_Adaptation_Function_Input("INV.links_input.FEP.Adaptation_Function_Input"),
	INV_links_input_Reference_Point_Input("INV.links_input.Reference_Point.Input"),
	A_Shelf_Slot("A_Shelf_Slot"),
	binds_Trail_Termination_Function_Physical_Media("binds.Trail_Termination_Function.Physical_Media"),
	A_AdaptationFunction_AdaptationFunctionOutput("A_AdaptationFunction_AdaptationFunctionOutput"),
	binds_Trail_Termination_Function_Adaptation_Function("binds.Trail_Termination_Function.Adaptation_Function"),
	INV_links_input_FP_Adaptation_Function_Input("INV.links_input.FP.Adaptation_Function_Input"),
	A_Subslot_Card("A_Subslot_Card"),
	INV_supervises_card_Card_Supervisor("INV.supervises_card"),
	INV_A_Slot_Subslot("INV.A_Slot_Subslot"),
	A_Card_InputCard("A_Card_InputCard");

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