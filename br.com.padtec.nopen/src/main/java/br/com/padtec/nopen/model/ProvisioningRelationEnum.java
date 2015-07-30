package br.com.padtec.nopen.model;

public enum ProvisioningRelationEnum {

	INV_links_output_Reference_Point_Output("INV.links_output.Reference_Point.Output"),
	INV_links_output_AP_Adaptation_Function_Output("INV.links_output.AP.Adaptation_Function_Output"),
	A_Matrix_MatrixOutput("A_Matrix_MatrixOutput"),
	INV_instantiates_Layer_Type_Card_Layer("INV.instantiates"),
	INV_links_input_FP_Adaptation_Function_Input("INV.links_input.FP.Adaptation_Function_Input"),
	connects_FP_FP("connects.FP.FP"),
	A_Card_CardLayer("A_Card_CardLayer"),
	INV_links_input_FEP_Adaptation_Function_Input("INV.links_input.FEP.Adaptation_Function_Input"),
	binds_Trail_Termination_Function_Physical_Media("binds.Trail_Termination_Function.Physical_Media"),
	componentOf("componentOf"),
	INV_A_Card_CardLayer("INV.A_Card_CardLayer"),
	INV_A_Card_TFCardElement("INV.A_Card_TFCardElement"),
	INV_links_output("INV.links_output"),
	A_TransportFunction_Input("A_TransportFunction_Input"),
	INV_links_output_FP_Matrix_Output("INV.links_output.FP.Matrix_Output"),
	A_CardLayer_TrailTerminationFunction("A_CardLayer_TrailTerminationFunction"),
	A_AdaptationFunction_AdaptationFunctionInput("A_AdaptationFunction_AdaptationFunctionInput"),
	links_input_Reference_Point_Input("links_input.Reference_Point.Input"),
	INV_A_TransportFunction_Input("INV.A_TransportFunction_Input"),
	INV_connects_FP_FP("INV.connects.FP.FP"),
	INV_has_path_FEP_AP("INV.has_path.FEP.AP"),
	INV_is_interface_of_Input_Card_Transport_Function("INV.is_interface_of.Input_Card.Transport_Function"),
	implements_("implements"),
	binds_Trail_Termination_Function_Matrix("binds.Trail_Termination_Function.Matrix"),
	A_AdaptationFunction_AdaptationFunctionOutput("A_AdaptationFunction_AdaptationFunctionOutput"),
	A_TrailTerminationFunction_TrailTerminationFunctionInput("A_TrailTerminationFunction_TrailTerminationFunctionInput"),
	INV_A_AdaptationFunction_AdaptationFunctionOutput("INV.A_AdaptationFunction_AdaptationFunctionOutput"),
	horizontal_links_to_Output_Card_Output_Card("horizontal_links_to"),
	A_Card_TFCardElement("A_Card_TFCardElement"),
	INV_implements_Equipment_Technology("INV.implements.Equipment.Technology"),
	INV_has_path_FP_AP("INV.has_path.FP.AP"),
	links_output("links_output"),
	INV_vertical_links_to_Input_Card_Output_Card("INV.vertical_links_to"),
	INV_binds_Matrix_Adaptation_Function("INV.binds.Matrix.Adaptation_Function"),
	INV_intermediates_down_Card_Layer_Transport_Function("INV.intermediates_down"),
	INV_A_Card_InputCard("INV.A_Card_InputCard"),
	INV_A_Technology_LayerType("INV.A_Technology_LayerType"),
	INV_A_PhysicalMedia_PMInputSo("INV.A_PhysicalMedia_PMInputSo"),
	INV_A_Card_OutputCard("INV.A_Card_OutputCard"),
	INV_A_AdaptationFunction_AdaptationFunctionInput("INV.A_AdaptationFunction_AdaptationFunctionInput"),
	INV_connects_Reference_Point_Reference_Point("INV.connects.Reference_Point.Reference_Point"),
	binds("binds"),
	has_path_FEP_AP("has_path.FEP.AP"),
	INV_is_interface_of("INV.is_interface_of"),
	INV_connects("INV.connects"),
	links_input_FEP_PM_Input_Sk("links_input.FEP.PM_Input_Sk"),
	INV_links_output_FEP_Trail_Termination_Function_Output("INV.links_output.FEP.Trail_Termination_Function_Output"),
	INV_links_input_Reference_Point_Input("INV.links_input.Reference_Point.Input"),
	is_interface_of("is_interface_of"),
	A_Card_OutputCard("A_Card_OutputCard"),
	INV_links_input("INV.links_input"),
	INV_A_TransportFunction_Output("INV.A_TransportFunction_Output"),
	A_PhysicalMedia_PMInputSk("A_PhysicalMedia_PMInputSk"),
	binds_Output_Card_Output("binds.Output_Card.Output"),
	intermediates_down_Transport_Function_Card_Layer("intermediates_down"),
	INV_links_input_AP_Trail_Termination_Function_Input("INV.links_input.AP.Trail_Termination_Function_Input"),
	INV_A_Matrix_MatrixOutput("INV.A_Matrix_MatrixOutput"),
	INV_A_Equipment_Card("INV.A_Equipment_Card"),
	A_Equipment_Card("A_Equipment_Card"),
	has_path_AP_FEP("has_path.AP.FEP"),
	A_Matrix_MatrixInput("A_Matrix_MatrixInput"),
	binds_Matrix_Adaptation_Function("binds.Matrix.Adaptation_Function"),
	INV_is_interface_of_Output_Card_Transport_Function("INV.is_interface_of.Output_Card.Transport_Function"),
	connects_AP_AP("connects.AP.AP"),
	is_physical_connected_Equipment_Equipment("is_physical_connected"),
	INV_has_connects_supported_Reference_Point_Reference_Point("INV.has_connects_supported"),
	INV_binds_Trail_Termination_Function_Matrix("INV.binds.Trail_Termination_Function.Matrix"),
	has_connects_supported_Reference_Point_Reference_Point("has_connects_supported"),
	A_Card_InputCard("A_Card_InputCard"),
	vertical_links_to_Output_Card_Input_Card("vertical_links_to"),
	links_input_FP_Adaptation_Function_Input("links_input.FP.Adaptation_Function_Input"),
	connects_Reference_Point_Reference_Point("connects.Reference_Point.Reference_Point"),
	INV_links_input_FEP_PM_Input_Sk("INV.links_input.FEP.PM_Input_Sk"),
	connects_FEP_FEP("connects.FEP.FEP"),
	binds_Input_Card_Input("binds.Input_Card.Input"),
	links_output_FEP_Trail_Termination_Function_Output("links_output.FEP.Trail_Termination_Function_Output"),
	implements_Equipment_Technology("implements.Equipment.Technology"),
	implements_Technology_Service("implements.Technology.Service"),
	INV_implements("INV.implements"),
	A_TrailTerminationFunction_TrailTerminationFunctionOutput("A_TrailTerminationFunction_TrailTerminationFunctionOutput"),
	links_input_AP_Trail_Termination_Function_Input("links_input.AP.Trail_Termination_Function_Input"),
	INV_implements_Technology_Service("INV.implements.Technology.Service"),
	INV_A_TrailTerminationFunction_TrailTerminationFunctionOutput("INV.A_TrailTerminationFunction_TrailTerminationFunctionOutput"),
	INV_binds_Trail_Termination_Function_Physical_Media("INV.binds.Trail_Termination_Function.Physical_Media"),
	instantiates_Card_Layer_Layer_Type("instantiates"),
	links_input_FEP_Matrix_Input("links_input.FEP.Matrix_Input"),
	is_interface_of_Input_Card_Transport_Function("is_interface_of.Input_Card.Transport_Function"),
	has_path("has_path"),
	links_input_FEP_PM_Input_So("links_input.FEP.PM_Input_So"),
	links_input("links_input"),
	is_interface_of_Output_Card_Transport_Function("is_interface_of.Output_Card.Transport_Function"),
	INV_is_physical_connected_Equipment_Equipment("INV.is_physical_connected"),
	INV_has_path_Reference_Point_Reference_Point("INV.has_path.Reference_Point.Reference_Point"),
	INV_componentOf("INV.componentOf"),
	INV_binds_Adaptation_Function_Trail_Termination_Function("INV.binds.Adaptation_Function.Trail_Termination_Function"),
	binds_Trail_Termination_Function_Adaptation_Function("binds.Trail_Termination_Function.Adaptation_Function"),
	INV_is_client_Layer_Type_Layer_Type("INV.is_client"),
	A_PhysicalMedia_PMInputSo("A_PhysicalMedia_PMInputSo"),
	INV_binds_Trail_Termination_Function_Adaptation_Function("INV.binds.Trail_Termination_Function.Adaptation_Function"),
	INV_links_input_FEP_Matrix_Input("INV.links_input.FEP.Matrix_Input"),
	connects("connects"),
	INV_has_path("INV.has_path"),
	INV_binds_Output_Card_Output("INV.binds.Output_Card.Output"),
	INV_horizontal_links_to_Output_Card_Output_Card("INV.horizontal_links_to"),
	binds_Adaptation_Function_Trail_Termination_Function("binds.Adaptation_Function.Trail_Termination_Function"),
	INV_A_Matrix_MatrixInput("INV.A_Matrix_MatrixInput"),
	INV_A_CardLayer_TrailTerminationFunction("INV.A_CardLayer_TrailTerminationFunction"),
	INV_mc_FEP_FP("INV.mc"),
	links_output_AP_Adaptation_Function_Output("links_output.AP.Adaptation_Function_Output"),
	has_path_FP_AP("has_path.FP.AP"),
	is_client_Layer_Type_Layer_Type("is_client"),
	has_path_Reference_Point_Reference_Point("has_path.Reference_Point.Reference_Point"),
	A_Technology_LayerType("A_Technology_LayerType"),
	A_TransportFunction_Output("A_TransportFunction_Output"),
	INV_A_TrailTerminationFunction_TrailTerminationFunctionInput("INV.A_TrailTerminationFunction_TrailTerminationFunctionInput"),
	INV_connects_FEP_FEP("INV.connects.FEP.FEP"),
	links_output_FP_Matrix_Output("links_output.FP.Matrix_Output"),
	links_output_Reference_Point_Output("links_output.Reference_Point.Output"),
	INV_has_path_AP_FEP("INV.has_path.AP.FEP"),
	links_input_FEP_Adaptation_Function_Input("links_input.FEP.Adaptation_Function_Input"),
	intermediates_up_Transport_Function_Card_Layer("intermediates_up"),
	INV_binds("INV.binds"),
	INV_binds_Input_Card_Input("INV.binds.Input_Card.Input"),
	INV_connects_AP_AP("INV.connects.AP.AP"),
	INV_links_input_FEP_PM_Input_So("INV.links_input.FEP.PM_Input_So"),
	mc_FP_FEP("mc"),
	INV_A_PhysicalMedia_PMInputSk("INV.A_PhysicalMedia_PMInputSk"),
	INV_intermediates_up_Card_Layer_Transport_Function("INV.intermediates_up");

	private String relation;
	private String domain = new String();
	private String range = new String();

	ProvisioningRelationEnum(String relation)
	{
		this.relation = relation;
	}

	ProvisioningRelationEnum(String relation, String domain, String range)
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
		for(ProvisioningRelationEnum r: ProvisioningRelationEnum.values()){
			System.out.println(r.relation);
		}
	}
}