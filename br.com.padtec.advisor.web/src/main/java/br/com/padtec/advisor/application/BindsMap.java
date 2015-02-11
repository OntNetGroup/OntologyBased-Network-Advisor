package br.com.padtec.advisor.application;

import java.util.HashMap;

import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;

public class BindsMap {
	
	public HashMap<HashMap<String, String>, HashMap<String,String>> map = new HashMap<HashMap<String,String>, HashMap<String,String>>();
	
	public HashMap<HashMap<String, String>, HashMap<String,String>> getMap() { return map; }
	
	public BindsMap()
	{
		init();
	}
	
	private void init()
	{
		HashMap<String, String> tf1= new HashMap<String, String>();
		HashMap<String, String> hashrp= new HashMap<String, String>();
		
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());		
		hashrp.put("RP", ConceptEnum.SOURCE_A_FEP.toString());		
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_A_FEP.toString());		
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_A_FEP_BINDING.toString());		
		hashrp.put("RP_BINDING_REL_IN", "binds_So_A-FEP_to");		
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_A-FEP_from");		
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.MATRIX_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_M_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_M_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FEP-from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.SUBNETWORK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_SN_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FEP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.LAYER_PROCESSOR_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_LPF_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_LPF-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_LPF_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_LPF-FEP-from");	
		map.put(tf1, hashrp);

		tf1.put("INPUT", ConceptEnum.TERMINATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.ADAPTATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_A_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_A-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_A_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_A-FEP_from");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_A-FEP_to");		
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.MATRIX_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_M_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_M_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FEP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.SUBNETWORK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_SN_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_SN_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FEP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.LAYER_PROCESSOR_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_LPF_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_LPF-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_LPF_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_LPF-FEP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.PHYSICAL_MEDIA_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_PM_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_PM-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_PM_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_PM-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_PM-FEP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.TERMINATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.PHYSICAL_MEDIA_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_PM_FEP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_PM-FEP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_PM_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_PM-FEP_from");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_PM-FEP_to");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.TERMINATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.ADAPTATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_AP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_AP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_AP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_AP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_AP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_AP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_AP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_AP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_AP_from");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_AP_to");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.LAYER_PROCESSOR_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_LP_FP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_L-FP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_L_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_L-FP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.LAYER_PROCESSOR_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_LP_FP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_L-FP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_L_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_L-FP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_OUTPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		hashrp.put("RP", ConceptEnum.UNIDIRECTIONAL_A_FP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_A-FP");
		hashrp.put("RP_BINDING", ConceptEnum.A_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_A-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_A-FP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.SUBNETWORK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_SN_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.SUBNETWORK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_SN_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.MATRIX_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FP");
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_M_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FP_from");	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.MATRIX_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_M_FP.toString());
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FP");
		hashrp.put("RP_BINDING", ConceptEnum.SINK_M_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FP_from");	
		map.put(tf1, hashrp);
	}
}
