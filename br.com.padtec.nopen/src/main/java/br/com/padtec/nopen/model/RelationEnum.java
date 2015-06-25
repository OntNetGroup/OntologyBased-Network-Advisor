package br.com.padtec.nopen.model;

public enum RelationEnum {

	A_Card_CardLayer("A_Card_CardLayer"),
	INV_instantiates_Layer_Type_Card_Layer("INV.instantiates"),
	INV_intermediates_down_Card_Layer_Transport_Function("INV.intermediates_down"),
	INV_binds_Trail_Termination_Function_Physical_Media("INV.binds.Trail_Termination_Function.Physical_Media"),
	INV_supervises_card_Card_Supervisor("INV.supervises_card"),
	INV_A_Card_CardLayer("INV.A_Card_CardLayer"),
	links_output("links_output"),
	INV_links_input_FEP_PM_Input_So("INV.links_input.FEP.PM_Input_So"),
	connects("connects"),
	INV_A_TransportFunction_Input("INV.A_TransportFunction_Input"),
	INV_has_path_Reference_Point_Reference_Point("INV.has_path.Reference_Point.Reference_Point"),
	A_Card_TFCardElement("A_Card_TFCardElement"),
	INV_A_Slot_Subslot("INV.A_Slot_Subslot"),
	INV_A_Card_OutputCard("INV.A_Card_OutputCard"),
	A_TrailTerminationFunction_TrailTerminationFunctionOutput("A_TrailTerminationFunction_TrailTerminationFunctionOutput"),
	intermediates_down_Transport_Function_Card_Layer("intermediates_down"),
	A_TrailTerminationFunction_TrailTerminationFunctionInput("A_TrailTerminationFunction_TrailTerminationFunctionInput"),
	INV_links_output_FP_Matrix_Output("INV.links_output.FP.Matrix_Output"),
	INV_A_Technology_LayerType("INV.A_Technology_LayerType"),
	A_CardLayer_TrailTerminationFunction("A_CardLayer_TrailTerminationFunction"),
	supervises_card_Supervisor_Card("supervises_card"),
	INV_A_TransportFunction_Output("INV.A_TransportFunction_Output"),
	INV_A_Matrix_MatrixInput("INV.A_Matrix_MatrixInput"),
	A_Rack_Shelf("A_Rack_Shelf"),
	componentOf("componentOf"),
	INV_connects_FEP_FEP("INV.connects.FEP.FEP"),
	INV_connects_AP_AP("INV.connects.AP.AP"),
	INV_A_Card_TFCardElement("INV.A_Card_TFCardElement"),
	INV_links_input("INV.links_input"),
	INV_connects_Reference_Point_Reference_Point("INV.connects.Reference_Point.Reference_Point"),
	INV_binds_Matrix_Adaptation_Function("INV.binds.Matrix.Adaptation_Function"),
	links_input_FEP_Matrix_Input("links_input.FEP.Matrix_Input"),
	INV_links_output("INV.links_output"),
	binds_Trail_Termination_Function_Matrix("binds.Trail_Termination_Function.Matrix"),
	A_Subslot_Card("A_Subslot_Card"),
	is_client_Layer_Type_Layer_Type("is_client"),
	INV_links_input_FP_Adaptation_Function_Input("INV.links_input.FP.Adaptation_Function_Input"),
	INV_is_client_Layer_Type_Layer_Type("INV.is_client"),
	binds("binds"),
	binds_Trail_Termination_Function_Physical_Media("binds.Trail_Termination_Function.Physical_Media"),
	implements_Technology_Service("implements.Technology.Service"),
	INV_binds_Adaptation_Function_Trail_Termination_Function("INV.binds.Adaptation_Function.Trail_Termination_Function"),
	INV_A_Shelf_Slot("INV.A_Shelf_Slot"),
	INV_binds_Trail_Termination_Function_Matrix("INV.binds.Trail_Termination_Function.Matrix"),
	INV_binds_Trail_Termination_Function_Adaptation_Function("INV.binds.Trail_Termination_Function.Adaptation_Function"),
	A_PhysicalMedia_PMInputSk("A_PhysicalMedia_PMInputSk"),
	connects_AP_AP("connects.AP.AP"),
	is_interface_of("is_interface_of"),
	INV_is_interface_of_Input_Card_Transport_Function("INV.is_interface_of.Input_Card.Transport_Function"),
	INV_A_TrailTerminationFunction_TrailTerminationFunctionInput("INV.A_TrailTerminationFunction_TrailTerminationFunctionInput"),
	INV_intermediates_up_Card_Layer_Transport_Function("INV.intermediates_up"),
	INV_implements("INV.implements"),
	INV_connects("INV.connects"),
	INV_links_input_Reference_Point_Input("INV.links_input.Reference_Point.Input"),
	INV_binds_Input_Card_Input("INV.binds.Input_Card.Input"),
	connects_Reference_Point_Reference_Point("connects.Reference_Point.Reference_Point"),
	has_path_FEP_AP("has_path.FEP.AP"),
	INV_implements_Technology_Service("INV.implements.Technology.Service"),
	mc_FP_FEP("mc"),
	connects_FP_FP("connects.FP.FP"),
	INV_links_input_FEP_Adaptation_Function_Input("INV.links_input.FEP.Adaptation_Function_Input"),
	A_Card_InputCard("A_Card_InputCard"),
	A_Matrix_MatrixInput("A_Matrix_MatrixInput"),
	A_Technology_LayerType("A_Technology_LayerType"),
	INV_has_path("INV.has_path"),
	intermediates_up_Transport_Function_Card_Layer("intermediates_up"),
	instantiates_Card_Layer_Layer_Type("instantiates"),
	INV_links_input_FEP_Matrix_Input("INV.links_input.FEP.Matrix_Input"),
	INV_binds_Output_Card_Output("INV.binds.Output_Card.Output"),
	INV_A_CardLayer_TrailTerminationFunction("INV.A_CardLayer_TrailTerminationFunction"),
	INV_is_interface_of("INV.is_interface_of"),
	INV_A_AdaptationFunction_AdaptationFunctionOutput("INV.A_AdaptationFunction_AdaptationFunctionOutput"),
	INV_has_path_FP_AP("INV.has_path.FP.AP"),
	INV_A_Rack_Shelf("INV.A_Rack_Shelf"),
	links_input_FEP_PM_Input_Sk("links_input.FEP.PM_Input_Sk"),
	is_physical_connected_Equipment_Equipment("is_physical_connected"),
	implements_("implements"),
	connects_FEP_FEP("connects.FEP.FEP"),
	binds_Trail_Termination_Function_Adaptation_Function("binds.Trail_Termination_Function.Adaptation_Function"),
	INV_A_Matrix_MatrixOutput("INV.A_Matrix_MatrixOutput"),
	INV_has_path_AP_FEP("INV.has_path.AP.FEP"),
	INV_A_AdaptationFunction_AdaptationFunctionInput("INV.A_AdaptationFunction_AdaptationFunctionInput"),
	INV_binds("INV.binds"),
	INV_is_physical_connected_Equipment_Equipment("INV.is_physical_connected"),
	INV_connects_FP_FP("INV.connects.FP.FP"),
	INV_A_PhysicalMedia_PMInputSo("INV.A_PhysicalMedia_PMInputSo"),
	INV_mc_FEP_FP("INV.mc"),
	A_PhysicalMedia_PMInputSo("A_PhysicalMedia_PMInputSo"),
	A_Slot_Card("A_Slot_Card"),
	has_path_AP_FEP("has_path.AP.FEP"),
	INV_A_TrailTerminationFunction_TrailTerminationFunctionOutput("INV.A_TrailTerminationFunction_TrailTerminationFunctionOutput"),
	has_path("has_path"),
	INV_has_connects_supported_Reference_Point_Reference_Point("INV.has_connects_supported"),
	A_Slot_Subslot("A_Slot_Subslot"),
	INV_has_path_FEP_AP("INV.has_path.FEP.AP"),
	INV_links_output_Reference_Point_Output("INV.links_output.Reference_Point.Output"),
	has_connects_supported_Reference_Point_Reference_Point("has_connects_supported"),
	INV_A_Subslot_Card("INV.A_Subslot_Card"),
	A_AdaptationFunction_AdaptationFunctionOutput("A_AdaptationFunction_AdaptationFunctionOutput"),
	links_input_FP_Adaptation_Function_Input("links_input.FP.Adaptation_Function_Input"),
	INV_links_output_FEP_Trail_Termination_Function_Output("INV.links_output.FEP.Trail_Termination_Function_Output"),
	A_AdaptationFunction_AdaptationFunctionInput("A_AdaptationFunction_AdaptationFunctionInput"),
	links_input_Reference_Point_Input("links_input.Reference_Point.Input"),
	links_output_FP_Matrix_Output("links_output.FP.Matrix_Output"),
	INV_is_interface_of_Output_Card_Transport_Function("INV.is_interface_of.Output_Card.Transport_Function"),
	A_Shelf_Slot("A_Shelf_Slot"),
	supervises_Supervisor_Equipment("supervises"),
	INV_links_input_FEP_PM_Input_Sk("INV.links_input.FEP.PM_Input_Sk"),
	links_output_FEP_Trail_Termination_Function_Output("links_output.FEP.Trail_Termination_Function_Output"),
	links_input_AP_Trail_Termination_Function_Input("links_input.AP.Trail_Termination_Function_Input"),
	implements_Equipment_Technology("implements.Equipment.Technology"),
	has_path_FP_AP("has_path.FP.AP"),
	binds_Adaptation_Function_Trail_Termination_Function("binds.Adaptation_Function.Trail_Termination_Function"),
	INV_implements_Equipment_Technology("INV.implements.Equipment.Technology"),
	A_Card_OutputCard("A_Card_OutputCard"),
	INV_A_Slot_Card("INV.A_Slot_Card"),
	links_output_AP_Adaptation_Function_Output("links_output.AP.Adaptation_Function_Output"),
	binds_Input_Card_Input("binds.Input_Card.Input"),
	links_input_FEP_PM_Input_So("links_input.FEP.PM_Input_So"),
	is_interface_of_Input_Card_Transport_Function("is_interface_of.Input_Card.Transport_Function"),
	INV_links_output_AP_Adaptation_Function_Output("INV.links_output.AP.Adaptation_Function_Output"),
	A_Matrix_MatrixOutput("A_Matrix_MatrixOutput"),
	INV_A_PhysicalMedia_PMInputSk("INV.A_PhysicalMedia_PMInputSk"),
	has_path_Reference_Point_Reference_Point("has_path.Reference_Point.Reference_Point"),
	A_TransportFunction_Input("A_TransportFunction_Input"),
	INV_componentOf("INV.componentOf"),
	INV_links_input_AP_Trail_Termination_Function_Input("INV.links_input.AP.Trail_Termination_Function_Input"),
	links_output_Reference_Point_Output("links_output.Reference_Point.Output"),
	binds_Matrix_Adaptation_Function("binds.Matrix.Adaptation_Function"),
	links_input("links_input"),
	INV_supervises_Equipment_Supervisor("INV.supervises"),
	links_input_FEP_Adaptation_Function_Input("links_input.FEP.Adaptation_Function_Input"),
	is_interface_of_Output_Card_Transport_Function("is_interface_of.Output_Card.Transport_Function"),
	binds_Output_Card_Output("binds.Output_Card.Output"),
	A_TransportFunction_Output("A_TransportFunction_Output"),
	INV_A_Card_InputCard("INV.A_Card_InputCard");

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