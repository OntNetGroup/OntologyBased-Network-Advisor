package br.com.padtec.advisor.core.application;

import java.util.HashMap;

import br.com.padtec.advisor.core.types.ConceptEnum;
import br.com.padtec.advisor.core.types.RelationEnum;

public class PossibleBindsMap {
	
	public HashMap<HashMap<String, String>, HashMap<String,String>> map = new HashMap<HashMap<String,String>, HashMap<String,String>>();
	
	public HashMap<HashMap<String, String>, HashMap<String,String>> getMap() { return map; }
	
	public PossibleBindsMap()
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
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_A_FEP_TO.toString());		
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_A_FEP_FROM.toString());		
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.MATRIX_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_M_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_M_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_M_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_M_FEP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_M_FEP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.SUBNETWORK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_SN_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_SN_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_SN_FEP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_SN_FEP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.LAYER_PROCESSOR_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_LPF_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_LPF_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_LPF_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_LPF_FEP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_LPF_FEP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1.put("INPUT", ConceptEnum.TERMINATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.ADAPTATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_A_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_A_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_A_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_A_FEP_FROM.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_A_FEP_TO.toString());		
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.MATRIX_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_M_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_M_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_M_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_M_FEP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_M_FEP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.SUBNETWORK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_SN_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_SN_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_SN_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_SN_FEP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_SN_FEP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.LAYER_PROCESSOR_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_LPF_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_LPF_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_LPF_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_LPF_FEP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_LPF_FEP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.PHYSICAL_MEDIA_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_PM_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_PM_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_PM_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_PM_FEP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_PM_FEP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.TERMINATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.PHYSICAL_MEDIA_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_PM_FEP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_PM_FEP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_PM_FEP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_PM_FEP_FROM.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_PM_FEP_TO.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.TERMINATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.ADAPTATION_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_AP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_AP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_AP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_AP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_AP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.TERMINATION_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_AP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_AP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_AP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_AP_FROM.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_AP_TO.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.LAYER_PROCESSOR_SOURCE_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_LP_FP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_L_FP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_L_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_L_FP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_L_FP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.LAYER_PROCESSOR_SINK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SINK_LP_FP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_L_FP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_L_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_L_FP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_L_FP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_OUTPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		hashrp.put("RP", ConceptEnum.UNIDIRECTIONAL_A_FP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_A_FP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.A_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_A_FP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_A_FP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.SUBNETWORK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_SN_FP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_SN_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_SN_FP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_SN_FP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.SUBNETWORK_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_SN_FP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_SN_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_SN_FP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_SN_FP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SOURCE_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.MATRIX_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_SN_FP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SO_M_FP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SOURCE_M_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SO_M_FP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SO_M_FP_FROM.toString());	
		map.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", ConceptEnum.ADAPTATION_SINK_INPUT.toString());
		tf1.put("OUTPUT", ConceptEnum.MATRIX_OUTPUT.toString());
		hashrp.put("RP", ConceptEnum.SOURCE_M_FP.toString());
		hashrp.put("RP_RELATION", RelationEnum.IS_REPRESENTED_BY_SK_M_FP.toString());
		hashrp.put("RP_BINDING", ConceptEnum.SINK_M_FP_BINDING.toString());
		hashrp.put("RP_BINDING_REL_IN", RelationEnum.BINDS_SK_M_FP_TO.toString());
		hashrp.put("RP_BINDING_REL_OUT", RelationEnum.BINDS_SK_M_FP_FROM.toString());	
		map.put(tf1, hashrp);
	}
}
