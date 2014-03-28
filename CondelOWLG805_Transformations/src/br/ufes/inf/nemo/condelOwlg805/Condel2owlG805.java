package br.ufes.inf.nemo.condelOwlg805;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;

public class Condel2owlG805 {
	
	public static String separator = "%-%-%";
	public final  String w3String = "http://www.w3.org/";
	public static String ns = "";
	
	public static OntModel transformToOWL(OntModel model, String codeWithSeparator)
	{		
		ns = model.getNsPrefixURI("");	
		boolean ComentBlockHappens = false;
		String comandlines[] = codeWithSeparator.split(separator);
		
		for (String instruction : comandlines) 
		{	
			instruction = instruction.replace(";", "");			
			
			//Comment Block
			if(instruction.contains("/*") || instruction.contains("*/"))
			{
				if(instruction.contains("/*") && instruction.contains("*/"))
				{
					//comments at the same line
					ComentBlockHappens = false;
					continue;
					
				} else {
					
					if(instruction.contains("/*"))
					{
						ComentBlockHappens = true;
						continue;
					}
					
					if(instruction.contains("*/"))
					{
						ComentBlockHappens = false;					
						continue;
					}
				}				
			}
						
			if(ComentBlockHappens == false){
				//System.out.println(instruction);
				model = transformComand(model, instruction);
				
			}
				
		}
		
		return model;
	}

	private static OntModel transformComand(OntModel model, String instruction) {
		
		if (isClassDeclaration(instruction)){			
			
			instruction = instruction.replace(" ", "");
			
			String clsName = instruction.split(":")[0].replace(" ", "");
			String[] instances = instruction.split(":")[1].split(",");			
			OntClass cls = model.getOntClass(ns + clsName);
			
			for (String ins : instances) {
				
				cls.createIndividual(ns + ins.replace(" ", ""));
			}			
			
		} else if (isRelationDeclaration(instruction)){
			
			String instanceSource = instruction.split(" ")[0].replace(" ", "");
			String relation = instruction.split(" ")[1].replace(" ", "");
			
			Individual indInstance = model.getIndividual(ns + instanceSource);
			Property relationProp = model.getProperty(ns + relation);
			
			if(instruction.split(" ")[2].contains(","))
			{
				String[] instancesTarget = instruction.split(" ")[2].split(",");
				
				for (String ins : instancesTarget) {
					
					Individual indTarget = model.getIndividual(ns + ins.replace(" ", ""));
					indInstance.addProperty(relationProp, indTarget); 		//Add relation
				}				
				
			} else {
				
				String sTarget = instruction.split(" ")[2].replace(" ", "");
				Individual indTarget = model.getIndividual(ns + sTarget);
				indInstance.addProperty(relationProp, indTarget); 			//Add relation	
			}			
			
		} else if (isAtributeDeclaration(instruction)){
			
			String instanceSource = "";
			String value = "";
			String relationName = "";
			String classValue = "http://www.w3.org/2001/XMLSchema#integer";
			
			if(instruction.contains(".type:")) 
			{
				instanceSource = instruction.split(".type:")[0].replace(" ", "");
				value = instruction.split(".type:")[1].replace(" ", "");
				relationName = "Trail_Termination_Function.type";				
			}
			if(instruction.contains(".location:")) 
			{
				instanceSource = instruction.split(".location:")[0].replace(" ", "");
				value = instruction.split(".location:")[1].replace(" ", "");
				relationName = "Geographical_Element_With_Alias.location";
			}
			if(instruction.contains(".lat.deg:")) 
			{
				instanceSource = instruction.split(".lat.deg:")[0].replace(" ", "");
				value = instruction.split(".lat.deg:")[1].replace(" ", "");
				relationName = "Defined_Geographical_Element.latitude.degree";
				
			}
			if(instruction.contains(".lat.min:")) 
			{
				instanceSource = instruction.split(".lat.min:")[0].replace(" ", "");
				value = instruction.split(".lat.min:")[1].replace(" ", "");
				relationName = "Defined_Geographical_Element.latitude.minute";
			}
			if(instruction.contains(".lat.sec:")) 
			{
				instanceSource = instruction.split(".lat.sec:")[0].replace(" ", "");
				value = instruction.split(".lat.sec:")[1].replace(" ", "");
				relationName = "Defined_Geographical_Element.latitude.second";
				
			}
			if(instruction.contains(".lon.deg:")) 
			{
				instanceSource = instruction.split(".lon.deg:")[0].replace(" ", "");
				value = instruction.split(".lon.deg:")[1].replace(" ", "");
				relationName = "Defined_Geographical_Element.longitude.degree";
			}
			if(instruction.contains(".lon.min:")) 
			{
				instanceSource = instruction.split(".lon.min:")[0].replace(" ", "");
				value = instruction.split(".lon.min:")[1].replace(" ", "");
				relationName = "Defined_Geographical_Element.longitude.minute";
			}
			if(instruction.contains(".lon.sec:")) 
			{
				instanceSource = instruction.split(".lon.sec:")[0].replace(" ", "");
				value = instruction.split(".lon.sec:")[1].replace(" ", "");
				relationName = "Defined_Geographical_Element.longitude.second";
			}
			
			//Get instance, class, property
			Individual indInstance = model.getIndividual(ns + instanceSource);
			Literal literal = model.createTypedLiteral(value, classValue);
			Property relation = model.getDatatypeProperty(ns + relationName);
			indInstance.addProperty(relation, literal);			
			
		} else if (isDisjointWithDeclaration(instruction)){			
			
			String instanceSource = instruction.split("disjointWith")[0].replace(" ", "");
			Individual iSource = model.getIndividual(ns + instanceSource);
			
			if(instruction.split("disjointWith")[1].contains(","))
			{
				String[] instancesTarget = instruction.split("disjointWith")[1].split(",");
				
				for (String ins : instancesTarget) {
					
					Individual iTarget = model.getIndividual(ns + ins.replace(" ", ""));					
					iSource.setDifferentFrom(iTarget);
					iTarget.setDifferentFrom(iSource);
					
					//Applying at all
					for (String ins2 : instancesTarget) {
						Individual iTarget2 = model.getIndividual(ns + ins2.replace(" ", ""));
						iTarget.setDifferentFrom(iTarget2);
						iTarget2.setDifferentFrom(iTarget);
					}	
				}	
				
			} else {
				
				String insTargetName = instruction.split("disjointWith")[1].replace(" ", "");
				Individual iTarget = model.getIndividual(ns + insTargetName);
				iSource.setDifferentFrom(iTarget);
			}

			
		} else if (isSameAsDeclaration(instruction)){
			
			String instanceSource = instruction.split("sameAs")[0].replace(" ", "");
			Individual iSource = model.getIndividual(ns + instanceSource);
			
			if(instruction.split("sameAs")[1].contains(","))
			{
				String[] instancesTarget = instruction.split("sameAs")[1].split(",");
				
				for (String ins : instancesTarget) {
					
					//Applying at the source
					Individual iTarget = model.getIndividual(ns + ins.replace(" ", ""));					
					iSource.setSameAs(iTarget);
					iTarget.setSameAs(iSource);
					
					//Applying at all
					for (String ins2 : instancesTarget) {
						Individual iTarget2 = model.getIndividual(ns + ins2.replace(" ", ""));
						iTarget.setSameAs(iTarget2);
						iTarget2.setSameAs(iTarget);
					}	
					
				}
				
			} else {
				
				String insTargetName = instruction.split("sameAs")[1].replace(" ", "");
				Individual iTarget = model.getIndividual(ns + insTargetName);
				iSource.setSameAs(iTarget);
			}
			
		} else {
			
			//No valid instructions
		}			
		
		return model;
	}

	private static boolean isClassDeclaration(String instruction)
	{
		if(instruction.contains("Active_Intermediate_Bidirectional_M-CP:") || instruction.contains("A_Sk_Output_-_Sk_M-CP_Binded:") || instruction.contains("Server_Physical_Media_Layer:") || instruction.contains("Sink_PM-TCP:") || instruction.contains("Defined_Geographical_Element:") || instruction.contains("Physical_Media_Layer_Network:") || instruction.contains("Sink_M-TCP:") || instruction.contains("Bidirectional_NC:") || instruction.contains("Sink_Active_RP_-_Path_LC_connected:") || instruction.contains("Source_MC:") || instruction.contains("A_So_-_Trail_Termination_Source_Connected:") || instruction.contains("Unidirectional_Trail:") || instruction.contains("Related_Layer_Network:") || instruction.contains("Trail_Termination_Source_-_M_Connected:") || instruction.contains("Trail_Termination_Sink_-_A_Sk_Connected:") || instruction.contains("Bidirectional_PM_NC:") || instruction.contains("Bidirectional_PM-TCP:") || instruction.contains("Trail_Termination_Source_-_TM_Connected:") || instruction.contains("Trail_Termination_Sink_Output:") || instruction.contains("Unidirectional_A-CP:") || instruction.contains("M-CP:") || instruction.contains("Transport_Entity:") || instruction.contains("Output:") || instruction.contains("Source_SN-CP_Binding:") || instruction.contains("Adaptation_Sink_-_A_So_Connected:") || instruction.contains("Information_Transfer:") || instruction.contains("Physical_Media:") || instruction.contains("Active_Path_Bidirectional_M-TCP:") || instruction.contains("Active_Sk_M-TCP_-_Sk_MC_connected:") || instruction.contains("PM_LC_Bi_Component:") || instruction.contains("NC_Supported_by_LC:") || instruction.contains("SN_-_Sk_S-TCP_Binded:") || instruction.contains("Bidirectional_Path_NC:") || instruction.contains("Sk_SNC_Information_Transfer:") || instruction.contains("Bi_Intermediate_MC:") || instruction.contains("Supporting_NC:") || instruction.contains("Physical_Media_Layer_TTF:") || instruction.contains("Adaptation_Sink_Output:") || instruction.contains("Physical_Media_Component:") || instruction.contains("Unidirectional_Port:") || instruction.contains("Source_PM-TCP:") || instruction.contains("Adaptation_Source_Output:") || instruction.contains("Trail_Termination_Source_Input:") || instruction.contains("A_Sk_-_Uni_Subnetwork_Connected:") || instruction.contains("Source_M-CP:") || instruction.contains("Sink_PM-TCP_Binding:") || instruction.contains("Unidirectional_Matrix:") || instruction.contains("Adaptation_Sink_Input:") || instruction.contains("Bi_Intermediate_SNC:") || instruction.contains("Uni_Subnetwork_-_A_Sk_Connected:") || instruction.contains("A_So_Input_-_So_A-TCP_Binded:") || instruction.contains("Path-Section_Layers_AF:") || instruction.contains("Source_SN-TCP_Bi_Component:") || instruction.contains("Trail_Termination_Sink_-_SN_Connected:") || instruction.contains("Sink_M-TCP_Binding:") || instruction.contains("Unidirectional_NC:") || instruction.contains("Binded_Input-Output:") || instruction.contains("Sink_SN-CP_Bi_Component:") || instruction.contains("Active_Path_Bidirectional_SN-TCP:") || instruction.contains("Source_AP_Bi_Component:") || instruction.contains("Active_Sk_M-CP_-_Int_MC_connected:") || instruction.contains("Bi_M_-_Bidirectional_Trail_Termination_Connected:") || instruction.contains("Source_SN-CP_Bi_Component:") || instruction.contains("Source_PM-TCP_Binding:") || instruction.contains("Reference_Point:") || instruction.contains("Active_Intermediate_Bidirectional_SN-CP:") || instruction.contains("Composed_PM_Layer:") || instruction.contains("Bidirectional_LC:") || instruction.contains("Bi_PM_LC:") || instruction.contains("Client_Section_Layer:") || instruction.contains("Active_Sk_PM-TCP_-_PM_NC_connected:") || instruction.contains("Active_So_Path-TCP_-_Path_NC_connected:") || instruction.contains("Subnetwork:") || instruction.contains("Source_Connected_Subnetwork:") || instruction.contains("AP_Information_Transfer:") || instruction.contains("Path_Layer_TTF:") || instruction.contains("Trail_Termination_Sink_-_TM_Connected:") || instruction.contains("Active_So_SN-TCP_-_End_SNC_connected:") || instruction.contains("Active_So_M-CP_-_Int_MC_connected:") || instruction.contains("Source_M-TCP:") || instruction.contains("M_-_Trail_Termination_Source_Connected:") || instruction.contains("Source_Path_TCP:") || instruction.contains("Client_Path_Layer:") || instruction.contains("Source_M-CP_Binding:") || instruction.contains("Single_Physical_Media:") || instruction.contains("Bidirectional_A-CP:") || instruction.contains("Sink_M-CP_Binding:") || instruction.contains("Trail_Termination_Sink_-_M_Connected:") || instruction.contains("Link_Connection:") || instruction.contains("Sink_AP:") || instruction.contains("Sink_SNC_Bi_Component:") || instruction.contains("PM_Input_-_So_PM-TCP_Binded:") || instruction.contains("Sink_Matrix:") || instruction.contains("Sink_SN-TCP_Bi_Component:") || instruction.contains("Intermediate_SNC_Bi_Component:") || instruction.contains("Bidirectional_SNC:") || instruction.contains("Transmission_Media_Layer_TTF:") || instruction.contains("Bidirectional_MC:") || instruction.contains("TT_So_Output_-_So_SN-TCP_Binded:") || instruction.contains("Server_Path_Layer:") || instruction.contains("M_-_Trail_Termination_Sink_Connected:") || instruction.contains("Aggregated_Physical_Media:") || instruction.contains("Active_Path_Bidirectional_M-CP:") || instruction.contains("Active_Bidirectional_PM-TCP_-_PM_LC_Connected:") || instruction.contains("Supporting_Trail:") || instruction.contains("Active_Bidirectional_AP:") || instruction.contains("PM_Output_-_Sk_PM-TCP_Binded:") || instruction.contains("Component_Matrix:") || instruction.contains("SN_-_Trail_Termination_Sink_Connected:") || instruction.contains("Bidirectional_Trail_Termination_-_Ag_TM_Connected:") || instruction.contains("SN_-_Trail_Termination_Source_Connected:") || instruction.contains("Active_Sk_Path-TCP_-_Path_NC_connected:") || instruction.contains("Active_Sk_SN-CP_-_Sk_SNC_connected:") || instruction.contains("Sink_A-TCP_Binding:") || instruction.contains("Bidirectional_A-TCP:") || instruction.contains("Trail_Termination_Sink:") || instruction.contains("Subnetwork_Connection:") || instruction.contains("PM_-_Trail_Termination_Source_Connected:") || instruction.contains("A-CP_Binding:") || instruction.contains("Active_So_M-TCP_-_So_MC_connected:") || instruction.contains("Sink_A-TCP_Bi_Component:") || instruction.contains("Active_Sk_SN-CP_-_Int_SNC_connected:") || instruction.contains("PM_LC:") || instruction.contains("Sink_M-TCP_Bi_Component:") || instruction.contains("NC_Supported_by_SNC:") || instruction.contains("Active_Sk_SN-TCP_-_End_SNC_connected:") || instruction.contains("Intermediate_Connected__Subnetwork:") || instruction.contains("TT_Sk_Output_-_Sk_AP_Binded:") || instruction.contains("Source_A-TCP:") || instruction.contains("Sink_AP_Binding:") || instruction.contains("Ag_PM_-_Bidirectional_Trail_Termination_Connected:") || instruction.contains("M_Input_-_Sk_M-CP_Binded:") || instruction.contains("Bidirectional_Connected_Subnetwork:") || instruction.contains("A-CP:") || instruction.contains("A_So_Input_-_A-CP_Binded:") || instruction.contains("End_Connected_Subnetwork:") || instruction.contains("Intermediate_MC:") || instruction.contains("Bi_TTF_-_Bidirectional_A_Connected:") || instruction.contains("Active_Bidirectional_PM-TCP_-_PM_NC_Connected:") || instruction.contains("Section_Layer_Network:") || instruction.contains("SN-CP:") || instruction.contains("PM_NC_Information_Transfer:") || instruction.contains("Bi_A_-_Bidirectional_Trail_Termination_Connected:") || instruction.contains("PM_-_Trail_Termination_Sink_Connected:") || instruction.contains("A_Sk_Output_-_Sk_SN-CP_Binded:") || instruction.contains("Trail_Termination_Source_Output:") || instruction.contains("Active_Sk_PM-TCP_-_PM_LC_connected:") || instruction.contains("Supporting_LC:") || instruction.contains("Active_So_M-CP_-_So_MC_connected:") || instruction.contains("Adaptation_Source_Input:") || instruction.contains("Active_End_Bidirectional_SN-TCP:") || instruction.contains("Bidirectional_Adaptation:") || instruction.contains("Layer_Network:") || instruction.contains("Matrix:") || instruction.contains("Active_So_PM-TCP_-_PM_LC_connected:") || instruction.contains("Sink_MC_Bi_Component:") || instruction.contains("Possible_Bi_LC_Connected_RP:") || instruction.contains("Adaptation_Source_-_TT_So_Connected:") || instruction.contains("Binded_Transport_Function:") || instruction.contains("SN_-_So_SN-CP_Binded:") || instruction.contains("Path-Path_Layers_AF:") || instruction.contains("So_MC_Information_Transfer:") || instruction.contains("Bidirectional_Path_TCP:") || instruction.contains("Physical_Media_Input:") || instruction.contains("Adaptation_Sink_Process:") || instruction.contains("Sk_MC_Information_Transfer:") || instruction.contains("Source_TCP:") || instruction.contains("Bidirectional_Adaptation_-_Bi_TT_Connected:") || instruction.contains("Uni_Subnetwork_-_A_So_Connected:") || instruction.contains("TT_So_-_Adaptation_Source_Connected:") || instruction.contains("Path_Layer_Network:") || instruction.contains("Geographical_Element:") || instruction.contains("Adaptation_Sink:") || instruction.contains("Active_Possible_Bi_LC_Connected_RP:") || instruction.contains("Path-Section_Client_Layer:") || instruction.contains("Sink_M-CP_Bi_Component:") || instruction.contains("Unidirectional_Sink-Source_Matrix:") || instruction.contains("Trail_Termination_Sink_Input:") || instruction.contains("Source_M-CP_Bi_Component:") || instruction.contains("Bi_Path_MC:") || instruction.contains("A_Sk_Output_-_A-CP_Binded:") || instruction.contains("Source_AP:") || instruction.contains("Trail_Termination_Sink_Process:") || instruction.contains("Source_SNC_Bi_Component:") || instruction.contains("Path_Connected_Reference_Point:") || instruction.contains("Source_SN-TCP:") || instruction.contains("Sink_PM-TCP_Bi_Component:") || instruction.contains("Adaptation_Sk_Bidirectional_Component:") || instruction.contains("Int_SNC_Information_Transfer:") || instruction.contains("Section_Layer_TTF:") || instruction.contains("Active_So_PM-TCP_-_PM_NC_connected:") || instruction.contains("End_SNC:") || instruction.contains("Section-Physical_Media_Layers_AF:") || instruction.contains("Path_LC:") || instruction.contains("Transport_Function:") || instruction.contains("Trail_Termination_Sk_Bidirectional_Component:") || instruction.contains("Sink_TCP:") || instruction.contains("A_So_-_Uni_Subnetwork_Connected:") || instruction.contains("Source_A-TCP_Binding:") || instruction.contains("Bi_SN_-_Bidirectional_Trail_Termination_Connected:") || instruction.contains("M_Output_-_So_M-CP_Binded:") || instruction.contains("Sink_A-TCP:") || instruction.contains("So_Matrix_Bi_Component:") || instruction.contains("TT_Sk_Input_-_Sk_SN-TCP_Binded:") || instruction.contains("TT_So_Output_-_So_A-TCP_Binded:") || instruction.contains("Source_SN-TCP_Binding:") || instruction.contains("Source_PM-TCP_Bi_Component:") || instruction.contains("Active_Sk_M-CP_-_Sk_MC_connected:") || instruction.contains("Sink_M-CP:") || instruction.contains("So_SNC_Information_Transfer:") || instruction.contains("Bi_Matrix_-_A_Bi_Connected:") || instruction.contains("SN_-_Sk_SN-CP_Binded:") || instruction.contains("A_So_Input_-_So_M-CP_Binded:") || instruction.contains("A_Sk_-_Trail_Termination_Sink_Connected:") || instruction.contains("LC_Composed_Layer_Network:") || instruction.contains("Sink_Path_TCP:") || instruction.contains("Supporting_SNC:") || instruction.contains("Sink_SNC:") || instruction.contains("Sink_AP_Bi_Component:") || instruction.contains("Unidirectional_Trail_Bidirectional_Component:") || instruction.contains("Path_Bidirectional_Matrix:") || instruction.contains("Matrix_Output:") || instruction.contains("Bi_Subnetwork_-_A_Bi_Connected:") || instruction.contains("Directly_Connected_Transport_Entity:") || instruction.contains("Architectural_Component:") || instruction.contains("A_So_-_Uni_Matrix_Connected:") || instruction.contains("Active_So_SN-TCP_-_So_SNC_connected:") || instruction.contains("Server_Section_Layer:") || instruction.contains("Intermediate_MC_Bi_Component:") || instruction.contains("Bidirectional_SN-CP:") || instruction.contains("Trail_Termination_Source_-_A_So_Connected:") || instruction.contains("Active_So_SN-CP_-_So_SNC_connected:") || instruction.contains("Int_MC_Information_Transfer:") || instruction.contains("TT_Sk_Input_-_Sk_A-TCP_Binded:") || instruction.contains("Source_M-TCP_Bi_Component:") || instruction.contains("Connected_Reference_Point:") || instruction.contains("Bi_A_-_Bi_Matrix_Connected:") || instruction.contains("Sink_SN-CP:") || instruction.contains("Path_LC_Information_Transfer:") || instruction.contains("Component_Subnetwork:") || instruction.contains("Network_Connection:") || instruction.contains("Trail_Termination_Source_-_SN_Connected:") || instruction.contains("Unidirectional_MC:") || instruction.contains("Active_Path_Bidirectional_SN-CP:") || instruction.contains("Path-Path_Client_Layer:") || instruction.contains("Bidirectional_Trail_Termination:") || instruction.contains("Matrix_Composed_Path_Layer:") || instruction.contains("SN_-_So_S-TCP_Binded:") || instruction.contains("Bi_A_-_Bi_Subnetwork_Connected:") || instruction.contains("Unidirectional_SNC:") || instruction.contains("Trail_Termination_Source_Process:") || instruction.contains("Geographical_Element_With_Alias:") || instruction.contains("Source_Matrix:") || instruction.contains("Matrix_Connection:") || instruction.contains("Uni_A-CP_Bi_Component:") || instruction.contains("M_Input_-_So_M-TCP_Binded:") || instruction.contains("Sink_Connected_Subnetwork:") || instruction.contains("Unidirectional_PM_NC_Bidirectional_Component:") || instruction.contains("Source_M-TCP_Binding:") || instruction.contains("End_SNC_Bi_Component:") || instruction.contains("Sink_SN-TCP_Binding:") || instruction.contains("Sink_SN-CP_Binding:") || instruction.contains("Adaptation_Sink_-_TT_Sk_Connected:") || instruction.contains("Uni_Sk-So_Matrix_Bi_Component:") || instruction.contains("Access_Point:") || instruction.contains("Sink_SN-TCP:") || instruction.contains("Binding:") || instruction.contains("Bidirectional_AP:") || instruction.contains("TT_Sk_Input_-_Sk_PM-TCP_Binded:") || instruction.contains("Source_Active_RP_-_Path_LC_connected:") || instruction.contains("TE_Connected_Reference_Point:") || instruction.contains("Termination_Connection_Point:") || instruction.contains("Transport_Processing_Function:") || instruction.contains("Bidirectional_Trail_Termination_-_Bi_A_Connected:") || instruction.contains("Bidirectional_TCP:") || instruction.contains("Trail:") || instruction.contains("Bidirectional_Adaptation_-_Bi_A_Connected:") || instruction.contains("Uni_Matrix_-_A_So_Connected:") || instruction.contains("Sink_MC:") || instruction.contains("End_Information_Transfer:") || instruction.contains("Bi_Path_SNC:") || instruction.contains("TT_So_Output_-_So_M-TCP_Binded:") || instruction.contains("A_Sk_-_Uni_Matrix_Connected:") || instruction.contains("Bi_End_SNC:") || instruction.contains("Active_So_SN-CP_-_Int_SNC_connected:") || instruction.contains("Connected_Subnetwork:") || instruction.contains("Connected_Unidirectional_Matrix:") || instruction.contains("Adaptation_Function:") || instruction.contains("Bi_Path_LC:") || instruction.contains("TT_So_Output_-_So_PM-TCP_Binded:") || instruction.contains("A_So_-_Adaptation_Sink_Connected:") || instruction.contains("Bidirectional_Trail_Termination_-_Bi_M_Connected:") || instruction.contains("Unidirectional_PM_NC:") || instruction.contains("Abstraction_Component:") || instruction.contains("Bidirectional_Trail:") || instruction.contains("Sk_Matrix_Bi_Component:") || instruction.contains("Bidirectional_Matrix:") || instruction.contains("Source_SN-CP:") || instruction.contains("Directly_Binded_Reference_Point:") || instruction.contains("NC_Supported_by_MC:") || instruction.contains("Bidirectional_M-TCP:") || instruction.contains("Bidirectional_M-CP:") || instruction.contains("Uni_Matrix_-_A_Sk_Connected:") || instruction.contains("Transmission_Media_Layer_Network:") || instruction.contains("Source_MC_Bi_Component:") || instruction.contains("Active_Source_AP:") || instruction.contains("A_So_Input_-_So_SN-CP_Binded:") || instruction.contains("TT_So_Input_-_So_AP_Binded:") || instruction.contains("Supporting_MC:") || instruction.contains("Unidirectional_Path_NC_Bidirectional_Component:") || instruction.contains("Intermediate_SNC:") || instruction.contains("Adaptation_Source_Process:") || instruction.contains("Trail_Termination_So_Bidirectional_Component:") || instruction.contains("Subnetwork_Composed_Path_Layer:") || instruction.contains("Source_AP_Binding:") || instruction.contains("Bidirectional_Trail_Termination_-_Bi_SN_Connected:") || instruction.contains("Bidirectional_Sink-Source_Matrix:") || instruction.contains("Path_LC_Bi_Component:") || instruction.contains("Input:") || instruction.contains("A_So_Output_-_So_AP_Binded:") || instruction.contains("Source_A-TCP_Bi_Component:") || instruction.contains("PM_LC_Information_Transfer:") || instruction.contains("TT_Sk_Input_-_Sk_M-TCP_Binded:") || instruction.contains("M_Output_-_Sk_M-TCP_Binded:") || instruction.contains("A_Sk_Input_-_Sk_AP_Binded:") || instruction.contains("Active_Sk_SN-TCP_-_Sk_SNC_connected:") || instruction.contains("Active_Bidirectional_Path_TCP_-_Path_NC_Connected:") || instruction.contains("Adaptation_Source:") || instruction.contains("TT_Sk_-_Adaptation_Sink_Connected:") || instruction.contains("Bidirectional_SN-TCP:") || instruction.contains("A_Sk_Output_-_Sk_A-TCP_Binded:") || instruction.contains("Trail_Termination_Function:") || instruction.contains("Matrix_Input:") || instruction.contains("Physical_Media_Layer_Component:") || instruction.contains("Physical_Media_Output:") || instruction.contains("Unidirectional_Path_NC:") || instruction.contains("Adaptation_So_Bidirectional_Component:") || instruction.contains("Source_SNC:") || instruction.contains("Unidirectional_LC:") || instruction.contains("Connection_Point:") || instruction.contains("Trail_Termination_Source:") || instruction.contains("Active_Sink_AP:") || instruction.contains("Possible_LC_Connected_RP:"))
		{
			return true;
		} else
		{
			return false;
		}		
	}
	
	private static boolean isRelationDeclaration(String instruction)
	{
		/* Object properties */
		
		if(instruction.contains(" INV.componentOf ") || instruction.contains(" INV.memberOf ") || instruction.contains(" componentOf ") || instruction.contains(" supports ") || instruction.contains(" INV.supports ") || instruction.contains(" memberOf ") || instruction.contains(" INV.componentOf.Trail_Termination_Sink.Trail_Termination_Sink_Output ") || instruction.contains(" INV.componentOf.Bidirectional_SN-CP.Sink_SN-CP_Bi_Component ") || instruction.contains(" componentOf.Unidirectional_Matrix.Matrix_Output ") || instruction.contains(" componentOf.Bi_Path_MC.Source_MC_Bi_Component ") || instruction.contains(" INV.supports.Supporting_LC.NC_Supported_by_LC ") || instruction.contains(" componentOf.Bidirectional_Path_NC.Unidirectional_Path_NC_Bidirectional_Component ") || instruction.contains(" INV.componentOf.Trail_Termination_Sink.Trail_Termination_Sink_Process ") || instruction.contains(" componentOf.Adaptation_Sink.Adaptation_Sink_Process ") || instruction.contains(" INV.componentOf.Bidirectional_Trail.Unidirectional_Trail_Bidirectional_Component ") || instruction.contains(" componentOf.Trail_Termination_Sink.Trail_Termination_Sink_Process ") || instruction.contains(" INV.componentOf.Path_Layer_Network.Path_Layer_TTF ") || instruction.contains(" INV.componentOf.Bidirectional_Adaptation.Adaptation_So_Bidirectional_Component ") || instruction.contains(" componentOf.Bi_End_SNC.End_SNC_Bi_Component ") || instruction.contains(" componentOf.Physical_Media_Layer_Network.Physical_Media_Layer_TTF ") || instruction.contains(" supports.Supporting_SNC.NC_Supported_by_SNC ") || instruction.contains(" supports.Supporting_MC.NC_Supported_by_MC ") || instruction.contains(" componentOf.Adaptation_Source.Adaptation_Source_Input ") || instruction.contains(" INV.componentOf.LC_Composed_Layer_Network.Link_Connection ") || instruction.contains(" componentOf.Trail_Termination_Source.Trail_Termination_Source_Process ") || instruction.contains(" componentOf.Bi_Path_SNC.Source_SNC_Bi_Component ") || instruction.contains(" INV.componentOf.Unidirectional_Matrix.Matrix_Output ") || instruction.contains(" componentOf.Bidirectional_SN-TCP.Source_SN-TCP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_SN-CP.Sink_SN-CP_Bi_Component ") || instruction.contains(" componentOf.Single_Physical_Media.Physical_Media_Input ") || instruction.contains(" componentOf.Path_Bidirectional_Matrix.So_Matrix_Bi_Component ") || instruction.contains(" INV.componentOf.Single_Physical_Media.Physical_Media_Input ") || instruction.contains(" INV.componentOf.Bi_Path_SNC.Sink_SNC_Bi_Component ") || instruction.contains(" INV.componentOf.Section_Layer_Network.Section_Layer_TTF ") || instruction.contains(" componentOf.Bi_Path_MC.Sink_MC_Bi_Component ") || instruction.contains(" INV.componentOf.Physical_Media_Layer_Network.Physical_Media_Layer_TTF ") || instruction.contains(" componentOf.Bi_Path_LC.Path_LC_Bi_Component ") || instruction.contains(" INV.componentOf.Trail_Termination_Source.Trail_Termination_Source_Output ") || instruction.contains(" supports.Link_Connection.Supporting_Trail ") || instruction.contains(" INV.componentOf.Bidirectional_A-TCP.Sink_A-TCP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_M-CP.Source_M-CP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_Adaptation.Adaptation_So_Bidirectional_Component ") || instruction.contains(" INV.componentOf.Bi_PM_LC.PM_LC_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_A-TCP.Source_A-TCP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_Trail_Termination.Trail_Termination_Sk_Bidirectional_Component ") || instruction.contains(" componentOf.Bidirectional_Trail.Unidirectional_Trail_Bidirectional_Component ") || instruction.contains(" componentOf.Bidirectional_A-CP.Uni_A-CP_Bi_Component ") || instruction.contains(" INV.componentOf.Adaptation_Source.Adaptation_Source_Output ") || instruction.contains(" INV.componentOf.Adaptation_Source.Adaptation_Source_Input ") || instruction.contains(" INV.componentOf.Bidirectional_Sink-Source_Matrix.Uni_Sk-So_Matrix_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_PM-TCP.Sink_PM-TCP_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_Trail_Termination.Trail_Termination_Sk_Bidirectional_Component ") || instruction.contains(" INV.componentOf.Unidirectional_Matrix.Matrix_Input ") || instruction.contains(" INV.componentOf.Matrix_Composed_Path_Layer.Component_Matrix ") || instruction.contains(" INV.componentOf.Adaptation_Sink.Adaptation_Sink_Input ") || instruction.contains(" INV.supports.Link_Connection.Supporting_Trail ") || instruction.contains(" componentOf.Bidirectional_PM-TCP.Sink_PM-TCP_Bi_Component ") || instruction.contains(" componentOf.Path_Layer_Network.Path_Layer_TTF ") || instruction.contains(" INV.componentOf.Bidirectional_Trail_Termination.Trail_Termination_So_Bidirectional_Component ") || instruction.contains(" componentOf.Bidirectional_AP.Source_AP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_Sink-Source_Matrix.Uni_Sk-So_Matrix_Bi_Component ") || instruction.contains(" INV.supports.Trail.Supporting_NC ") || instruction.contains(" INV.componentOf.Bidirectional_SN-TCP.Sink_SN-TCP_Bi_Component ") || instruction.contains(" componentOf.Adaptation_Source.Adaptation_Source_Output ") || instruction.contains(" componentOf.Bi_Path_SNC.Sink_SNC_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_M-TCP.Sink_M-TCP_Bi_Component ") || instruction.contains(" INV.componentOf.Bi_Path_MC.Source_MC_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_M-TCP.Sink_M-TCP_Bi_Component ") || instruction.contains(" INV.componentOf.Bi_Intermediate_MC.Intermediate_MC_Bi_Component ") || instruction.contains(" INV.componentOf.Trail_Termination_Source.Trail_Termination_Source_Input ") || instruction.contains(" INV.componentOf.Bidirectional_PM_NC.Unidirectional_PM_NC_Bidirectional_Component ") || instruction.contains(" INV.componentOf.Bidirectional_A-CP.Uni_A-CP_Bi_Component ") || instruction.contains(" INV.componentOf.Bi_Intermediate_SNC.Intermediate_SNC_Bi_Component ") || instruction.contains(" INV.componentOf.Adaptation_Source.Adaptation_Source_Process ") || instruction.contains(" componentOf.Bidirectional_A-TCP.Sink_A-TCP_Bi_Component ") || instruction.contains(" INV.componentOf.Bi_Path_MC.Sink_MC_Bi_Component ") || instruction.contains(" INV.componentOf.Single_Physical_Media.Physical_Media_Output ") || instruction.contains(" componentOf.Section_Layer_Network.Section_Layer_TTF ") || instruction.contains(" INV.componentOf.Bi_Path_SNC.Source_SNC_Bi_Component ") || instruction.contains(" componentOf.Trail_Termination_Sink.Trail_Termination_Sink_Input ") || instruction.contains(" INV.componentOf.Trail_Termination_Sink.Trail_Termination_Sink_Input ") || instruction.contains(" componentOf.Adaptation_Source.Adaptation_Source_Process ") || instruction.contains(" INV.componentOf.Bidirectional_M-CP.Sink_M-CP_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_M-CP.Source_M-CP_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_A-TCP.Source_A-TCP_Bi_Component ") || instruction.contains(" memberOf.Aggregated_Physical_Media.Physical_Media_Component ") || instruction.contains(" componentOf.Bidirectional_PM-TCP.Source_PM-TCP_Bi_Component ") || instruction.contains(" INV.componentOf.Path_Bidirectional_Matrix.So_Matrix_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_PM-TCP.Source_PM-TCP_Bi_Component ") || instruction.contains(" componentOf.Unidirectional_Matrix.Matrix_Input ") || instruction.contains(" INV.componentOf.Adaptation_Sink.Adaptation_Sink_Process ") || instruction.contains(" componentOf.Bidirectional_PM_NC.Unidirectional_PM_NC_Bidirectional_Component ") || instruction.contains(" INV.componentOf.Bidirectional_SN-CP.Source_SN-CP_Bi_Component ") || instruction.contains(" componentOf.Path_Bidirectional_Matrix.Sk_Matrix_Bi_Component ") || instruction.contains(" supports.Trail.Supporting_NC ") || instruction.contains(" INV.componentOf.Bidirectional_Adaptation.Adaptation_Sk_Bidirectional_Component ") || instruction.contains(" componentOf.Bidirectional_M-CP.Sink_M-CP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_Adaptation.Adaptation_Sk_Bidirectional_Component ") || instruction.contains(" INV.componentOf.Bidirectional_Path_NC.Unidirectional_Path_NC_Bidirectional_Component ") || instruction.contains(" INV.memberOf.Aggregated_Physical_Media.Physical_Media_Component ") || instruction.contains(" componentOf.Bi_Intermediate_SNC.Intermediate_SNC_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_SN-CP.Source_SN-CP_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_AP.Source_AP_Bi_Component ") || instruction.contains(" componentOf.Subnetwork_Composed_Path_Layer.Component_Subnetwork ") || instruction.contains(" componentOf.Composed_PM_Layer.Physical_Media_Layer_Component ") || instruction.contains(" componentOf.Single_Physical_Media.Physical_Media_Output ") || instruction.contains(" INV.componentOf.Bidirectional_SN-TCP.Source_SN-TCP_Bi_Component ") || instruction.contains(" INV.supports.Supporting_MC.NC_Supported_by_MC ") || instruction.contains(" componentOf.Adaptation_Sink.Adaptation_Sink_Input ") || instruction.contains(" INV.componentOf.Bi_End_SNC.End_SNC_Bi_Component ") || instruction.contains(" INV.componentOf.Subnetwork_Composed_Path_Layer.Component_Subnetwork ") || instruction.contains(" componentOf.Trail_Termination_Sink.Trail_Termination_Sink_Output ") || instruction.contains(" supports.Supporting_LC.NC_Supported_by_LC ") || instruction.contains(" INV.componentOf.Adaptation_Sink.Adaptation_Sink_Output ") || instruction.contains(" INV.supports.Supporting_SNC.NC_Supported_by_SNC ") || instruction.contains(" componentOf.Bidirectional_AP.Sink_AP_Bi_Component ") || instruction.contains(" componentOf.Bi_PM_LC.PM_LC_Bi_Component ") || instruction.contains(" componentOf.Trail_Termination_Source.Trail_Termination_Source_Output ") || instruction.contains(" componentOf.Bi_Intermediate_MC.Intermediate_MC_Bi_Component ") || instruction.contains(" componentOf.Trail_Termination_Source.Trail_Termination_Source_Input ") || instruction.contains(" componentOf.Adaptation_Sink.Adaptation_Sink_Output ") || instruction.contains(" INV.componentOf.Path_Bidirectional_Matrix.Sk_Matrix_Bi_Component ") || instruction.contains(" componentOf.LC_Composed_Layer_Network.Link_Connection ") || instruction.contains(" INV.componentOf.Composed_PM_Layer.Physical_Media_Layer_Component ") || instruction.contains(" INV.componentOf.Bidirectional_AP.Sink_AP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_M-TCP.Source_M-TCP_Bi_Component ") || instruction.contains(" INV.componentOf.Bidirectional_M-TCP.Source_M-TCP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_SN-TCP.Sink_SN-TCP_Bi_Component ") || instruction.contains(" componentOf.Bidirectional_Trail_Termination.Trail_Termination_So_Bidirectional_Component ") || instruction.contains(" componentOf.Matrix_Composed_Path_Layer.Component_Matrix ") || instruction.contains(" INV.componentOf.Bi_Path_LC.Path_LC_Bi_Component ") || instruction.contains(" INV.componentOf.Trail_Termination_Source.Trail_Termination_Source_Process ") || instruction.contains(" binds_Sk_PM-TCP_to ") || instruction.contains(" information_transfer_from_Intermediate_SNC ") || instruction.contains(" information_transfer_Bidirectional_Path_MC_TCP ") || instruction.contains(" information_transfer_Bidirectional_PM_TCP ") || instruction.contains(" INV.information_transfer_to_Sink_SNC ") || instruction.contains(" binding_is_represented_by ") || instruction.contains(" INV.binds_So_SN-CP ") || instruction.contains(" INV.TT-PM_Source_Connection ") || instruction.contains(" TT-A_Sink_Connection ") || instruction.contains(" INV.information_transfer_Source_SNC ") || instruction.contains(" INV.information_transfer_from_Intermediate_SNC ") || instruction.contains(" Defined_Geographical_Element.longitude.second ") || instruction.contains(" INV.binds_Sk_PM-TCP_from ") || instruction.contains(" INV.te_connected ") || instruction.contains(" INV.information_transfer_Unidirectional_Trail ") || instruction.contains(" INV.M-A_Source_Connection ") || instruction.contains(" information_transfer_from_PM_LC ") || instruction.contains(" INV.binds_A-CP_from ") || instruction.contains(" INV.information_transfer_from_Source_MC ") || instruction.contains(" is_binding ") || instruction.contains(" information_transfer_to_Path_LC ") || instruction.contains(" INV.information_transfer_Bidirectional_Path_LC ") || instruction.contains(" INV.information_transfer_Path_NC ") || instruction.contains(" binds_Sk_M-TCP_from ") || instruction.contains(" INV.binds_So_M-CP ") || instruction.contains(" information_transfer_from_Path_LC ") || instruction.contains(" INV.binds_Sk_AP_from ") || instruction.contains(" is_represented_by_Sink_MC ") || instruction.contains(" INV.TT-SN_Source_Connection ") || instruction.contains(" INV.TT-M_Source_Connection ") || instruction.contains(" information_transfer_PM_LC ") || instruction.contains(" Defined_Geographical_Element.longitude.minute ") || instruction.contains(" binds_Sk_AP ") || instruction.contains(" binds_A-CP_from ") || instruction.contains(" information_transfer_Unidirectional_Trail ") || instruction.contains(" INV.is_represented_by_Source_SNC ") || instruction.contains(" binds_So_SN-CP_to ") || instruction.contains(" TT-PM_Bidirectional_Connection ") || instruction.contains(" binds_Sk_M-CP_from ") || instruction.contains(" INV.M-A_Bidirectional_Connection ") || instruction.contains(" A-TT_Sink_Connection ") || instruction.contains(" INV.A-A_Bidirectional_Connection ") || instruction.contains(" INV.information_transfer_to_Intermediate_SNC ") || instruction.contains(" INV.binds_So_AP_to ") || instruction.contains(" INV.binds_Sk_SN-CP_from ") || instruction.contains(" INV.binds_Sk_TM-TCP ") || instruction.contains(" information_transfer_Path_LC ") || instruction.contains(" INV.information_transfer_to_Uni_PM_NC ") || instruction.contains(" INV.is_represented_by_Sk_SN-CP ") || instruction.contains(" INV.TT-PM_Bidirectional_Connection ") || instruction.contains(" information_transfer_Bidirectional_Path_SNC_TCP ") || instruction.contains(" path ") || instruction.contains(" INV.information_transfer_Bidirectional_PM_NC ") || instruction.contains(" is_represented_by_So_M-CP ") || instruction.contains(" information_transfer_from_Uni_Trail ") || instruction.contains(" binds_Sk_A-TCP_from ") || instruction.contains(" INV.binds_So_AP ") || instruction.contains(" INV.is_represented_by_Sk_PM-TCP ") || instruction.contains(" INV.binds_Sk_A-TCP_to ") || instruction.contains(" SN-A_Bidirectional_Connection ") || instruction.contains(" is_connecting ") || instruction.contains(" M-A_Bidirectional_Connection ") || instruction.contains(" INV.binds_So_SN-TCP ") || instruction.contains(" is_represented_by_End_SNC ") || instruction.contains(" INV.TT-SN_Bidirectional_Connection ") || instruction.contains(" TT-SN_Source_Connection ") || instruction.contains(" binds_A-CP_to ") || instruction.contains(" INV.information_transfer_from_Sink_SNC ") || instruction.contains(" INV.is_represented_by_Sk_AP ") || instruction.contains(" INV.binds_So_SN-TCP_from ") || instruction.contains(" information_transfer_from_Intermediate_MC ") || instruction.contains(" is_represented_by_PM_LC ") || instruction.contains(" binds_Sk_A-TCP ") || instruction.contains(" INV.binds_So_M-CP_to ") || instruction.contains(" INV.binds_Sk_M-CP ") || instruction.contains(" binds_So_M-TCP_from ") || instruction.contains(" INV.TT-SN_Sink_Connection ") || instruction.contains(" is_represented_by_Sk_PM-TCP ") || instruction.contains(" TT-M_Source_Connection ") || instruction.contains(" binds_Sk_A-TCP_to ") || instruction.contains(" INV.is_represented_by_Sk_M-CP ") || instruction.contains(" binds_A-CP ") || instruction.contains(" information_transfer_Path_NC ") || instruction.contains(" tf_connection ") || instruction.contains(" INV.M-A_Sink_Connection ") || instruction.contains(" INV.information_transfer_to_Uni_Path_NC ") || instruction.contains(" information_transfer_to_Uni_Path_NC ") || instruction.contains(" INV.is_represented_by_Sink_MC ") || instruction.contains(" INV.information_transfer_Source_MC ") || instruction.contains(" INV.is_represented_by_So_AP ") || instruction.contains(" INV.A-TT_Bidirectional_Connection ") || instruction.contains(" INV.binds_So_A-TCP_to ") || instruction.contains(" is_represented_by_So_PM-TCP ") || instruction.contains(" INV.is_represented_by_Source_MC ") || instruction.contains(" A-A_Bidirectional_Connection ") || instruction.contains(" INV.information_transfer_to_Uni_Trail ") || instruction.contains(" INV.SN-A_Bidirectional_Connection ") || instruction.contains(" binds_So_PM-TCP_to ") || instruction.contains(" INV.A-TT_Source_Connection ") || instruction.contains(" binds_So_SN-TCP_to ") || instruction.contains(" information_transfer_Bidirectional_Path_MC_CP ") || instruction.contains(" INV.is_represented_by_Intermediate_MC ") || instruction.contains(" binds_So_M-CP_to ") || instruction.contains(" Trail_Termination_Function.type ") || instruction.contains(" INV.binds_A-CP_to ") || instruction.contains(" is_represented_by_Sk_SN-CP ") || instruction.contains(" is_represented_by_So_M-TCP ") || instruction.contains(" INV.binds_So_PM-TCP ") || instruction.contains(" information_transfer_from_Uni_Path_NC ") || instruction.contains(" INV.information_transfer_from_Uni_Path_NC ") || instruction.contains(" binds_So_M-CP ") || instruction.contains(" binds_So_A-TCP_from ") || instruction.contains(" binds_So_M-TCP_to ") || instruction.contains(" information_transfer_to_PM_LC ") || instruction.contains(" INV.IT_is_represented_by ") || instruction.contains(" INV.binds_So_SN-CP_to ") || instruction.contains(" M-A_Source_Connection ") || instruction.contains(" binds_So_PM-TCP ") || instruction.contains(" information_transfer_from_Sink_SNC ") || instruction.contains(" information_transfer_Intermediate_MC ") || instruction.contains(" M-A_Sink_Connection ") || instruction.contains(" INV.binds_Sk_M-TCP_to ") || instruction.contains(" INV.information_transfer_PM_LC ") || instruction.contains(" TT-A_Bidirectional_Connection ") || instruction.contains(" INV.information_transfer_from_Path_LC ") || instruction.contains(" is_represented_by_Intermediate_MC ") || instruction.contains(" binds_Sk_TM-TCP ") || instruction.contains(" binds_Sk_SN-CP_from ") || instruction.contains(" INV.TT-PM_Sink_Connection ") || instruction.contains(" INV.is_represented_by_Sk_M-TCP ") || instruction.contains(" binds_Sk_SN-CP_to ") || instruction.contains(" INV.information_transfer_to_Path_LC ") || instruction.contains(" Geographical_Element_With_Alias.location ") || instruction.contains(" INV.binds_So_AP_from ") || instruction.contains(" information_transfer_to_Uni_Trail ") || instruction.contains(" binds_Sk_AP_from ") || instruction.contains(" is_represented_by_Sk_SN-TCP ") || instruction.contains(" binds_Sk_SN-TCP_from ") || instruction.contains(" is_represented_by_Uni_PM_NC ") || instruction.contains(" is_represented_by_Sk_M-CP ") || instruction.contains(" INV.binds_So_SN-CP_from ") || instruction.contains(" binds_So_M-TCP ") || instruction.contains(" binds_Sk_SN-CP ") || instruction.contains(" INV.information_transfer_Bidirectional_Path_SNC_CP ") || instruction.contains(" binds_So_A-TCP ") || instruction.contains(" INV.is_represented_by_Sink_SNC ") || instruction.contains(" is_represented_by_Source_SNC ") || instruction.contains(" information_transfer_PM_NC ") || instruction.contains(" information_transfer_Bidirectional_Int_SNC ") || instruction.contains(" is_represented_by_Intermediate_SNC ") || instruction.contains(" information_transfer_to_Intermediate_MC ") || instruction.contains(" INV.is_represented_by_End_SNC ") || instruction.contains(" INV.information_transfer_from_Sink_MC ") || instruction.contains(" TT-A_Source_Connection ") || instruction.contains(" binds_Sk_M-CP ") || instruction.contains(" is_represented_by_Uni_Path_NC ") || instruction.contains(" binds_Sk_M-CP_to ") || instruction.contains(" INV.binds_Sk_SN-CP ") || instruction.contains(" Path-Path_client-server_layer_relationship ") || instruction.contains(" INV.path ") || instruction.contains(" INV.information_transfer_to_Sink_MC ") || instruction.contains(" INV.information_transfer_Bidirectional_Path_NC ") || instruction.contains(" binds_So_AP ") || instruction.contains(" SN-A_Source_Connection ") || instruction.contains(" INV.information_transfer_Bidirectional_Int_MC ") || instruction.contains(" A-TT_Bidirectional_Connection ") || instruction.contains(" INV.binds_Sk_SN-TCP ") || instruction.contains(" information_transfer_Int_SNC ") || instruction.contains(" A-A_Connection ") || instruction.contains(" INV.information_transfer_Bidirectional_Path_MC_CP ") || instruction.contains(" information_transfer_from_Uni_PM_NC ") || instruction.contains(" INV.information_transfer_Sink_MC ") || instruction.contains(" INV.binds_So_PM-TCP_from ") || instruction.contains(" INV.SN-A_Sink_Connection ") || instruction.contains(" INV.information_transfer_Bidirectional_Path_SNC_TCP ") || instruction.contains(" INV.is_represented_by_Uni_PM_NC ") || instruction.contains(" information_transfer_to_Sink_MC ") || instruction.contains(" INV.information_transfer_Intermediate_MC ") || instruction.contains(" INV.TT-A_Bidirectional_Connection ") || instruction.contains(" INV.information_transfer_from_Uni_Trail ") || instruction.contains(" INV.information_transfer_Path_LC ") || instruction.contains(" INV.is_represented_by_So_PM-TCP ") || instruction.contains(" is_represented_by_Sk_M-TCP ") || instruction.contains(" INV.is_represented_by_Path_LC ") || instruction.contains(" is_represented_by_So_SN-TCP ") || instruction.contains(" INV.binds_Sk_AP ") || instruction.contains(" information_transfer_Bidirectional_Trail ") || instruction.contains(" INV.Path-Section_client-server_layer_relationship ") || instruction.contains(" INV.binds_Sk_M-TCP_from ") || instruction.contains(" INV.binds_Sk_PM-TCP_to ") || instruction.contains(" INV.is_binding ") || instruction.contains(" INV.information_transfer_Bidirectional_Path_MC_TCP ") || instruction.contains(" TT-M_Bidirectional_Connection ") || instruction.contains(" binds_So_SN-CP_from ") || instruction.contains(" Defined_Geographical_Element.longitude.degree ") || instruction.contains(" Section-PM_client-server_layer_relationship ") || instruction.contains(" INV.information_transfer_from_Intermediate_MC ") || instruction.contains(" INV.A-TT_Sink_Connection ") || instruction.contains(" INV.binds_Sk_SN-CP_to ") || instruction.contains(" INV.information_transfer_PM_NC ") || instruction.contains(" INV.is_represented_by_So_SN-CP ") || instruction.contains(" IT_is_represented_by ") || instruction.contains(" binds_Sk_PM-TCP_from ") || instruction.contains(" INV.binds_So_M-TCP_from ") || instruction.contains(" TT-PM_Source_Connection ") || instruction.contains(" INV.binds_Sk_M-CP_from ") || instruction.contains(" INV.Path-Path_client-server_layer_relationship ") || instruction.contains(" TT-SN_Sink_Connection ") || instruction.contains(" binds_Sk_SN-TCP_to ") || instruction.contains(" is_represented_by_So_SN-CP ") || instruction.contains(" INV.binds_So_A-TCP_from ") || instruction.contains(" INV.binds_So_M-TCP ") || instruction.contains(" INV.binds_So_A-TCP ") || instruction.contains(" te_connected ") || instruction.contains(" information_transfer_Bidirectional_End_SNC ") || instruction.contains(" INV.binds_Sk_SN-TCP_to ") || instruction.contains(" is_represented_by_Sk_AP ") || instruction.contains(" is_represented_by_Uni_Trail ") || instruction.contains(" INV.information_transfer_End_SNC ") || instruction.contains(" INV.binds_So_PM-TCP_to ") || instruction.contains(" client-server_layer_relationship ") || instruction.contains(" INV.information_transfer_from_Uni_PM_NC ") || instruction.contains(" information_transfer_Source_SNC ") || instruction.contains(" INV.is_represented_by_So_M-TCP ") || instruction.contains(" binds_So_A-TCP_to ") || instruction.contains(" INV.is_represented_by_Sk_SN-TCP ") || instruction.contains(" INV.information_transfer_to_End_SNC ") || instruction.contains(" Path-Section_client-server_layer_relationship ") || instruction.contains(" information_transfer_to_Source_MC ") || instruction.contains(" is_represented_by_Sk_A-TCP ") || instruction.contains(" INV.binds_A-CP ") || instruction.contains(" binds_So_SN-TCP ") || instruction.contains(" INV.binds_Sk_AP_to ") || instruction.contains(" information_transfer_from_End_SNC ") || instruction.contains(" INV.binds ") || instruction.contains(" TT-M_Sink_Connection ") || instruction.contains(" Defined_Geographical_Element.latitude.second ") || instruction.contains(" INV.binds_Sk_A-TCP ") || instruction.contains(" information_transfer_to_End_SNC ") || instruction.contains(" INV.is_represented_by_So_A-TCP ") || instruction.contains(" INV.is_represented_by_PM_LC ") || instruction.contains(" INV.is_represented_by_Uni_Path_NC ") || instruction.contains(" INV.binds_Sk_M-TCP ") || instruction.contains(" information_transfer_Bidirectional_PM_NC ") || instruction.contains(" INV.is_represented_by_Uni_Trail ") || instruction.contains(" binds_Sk_AP_to ") || instruction.contains(" binds ") || instruction.contains(" INV.A-A_Connection ") || instruction.contains(" INV.has_information_transfer ") || instruction.contains(" is_represented_by_Path_LC ") || instruction.contains(" binds_Sk_M-TCP_to ") || instruction.contains(" information_transfer_from_Source_MC ") || instruction.contains(" binds_Sk_SN-TCP ") || instruction.contains(" information_transfer_Sink_MC ") || instruction.contains(" binds_So_PM-TCP_from ") || instruction.contains(" INV.binds_Sk_M-CP_to ") || instruction.contains(" INV.information_transfer_Sink_SNC ") || instruction.contains(" SN-A_Sink_Connection ") || instruction.contains(" INV.SN-A_Source_Connection ") || instruction.contains(" binds_So_M-CP_from ") || instruction.contains(" INV.information_transfer_to_Source_SNC ") || instruction.contains(" INV.information_transfer_Bidirectional_End_SNC ") || instruction.contains(" is_represented_by_Source_MC ") || instruction.contains(" information_transfer_Sink_SNC ") || instruction.contains(" binds_Sk_M-TCP ") || instruction.contains(" INV.binds_So_SN-TCP_to ") || instruction.contains(" INV.TT-M_Sink_Connection ") || instruction.contains(" INV.information_transfer_from_Source_SNC ") || instruction.contains(" INV.information_transfer_Bidirectional_Trail ") || instruction.contains(" INV.binds_Sk_SN-TCP_from ") || instruction.contains(" INV.is_represented_by_So_M-CP ") || instruction.contains(" INV.Section-PM_client-server_layer_relationship ") || instruction.contains(" INV.information_transfer_Int_SNC ") || instruction.contains(" INV.is_represented_by_A-CP ") || instruction.contains(" INV.is_connecting ") || instruction.contains(" INV.binds_So_M-CP_from ") || instruction.contains(" INV.TT-M_Bidirectional_Connection ") || instruction.contains(" A-TT_Source_Connection ") || instruction.contains(" is_represented_by_Sink_SNC ") || instruction.contains(" INV.binding_is_represented_by ") || instruction.contains(" is_represented_by_A-CP ") || instruction.contains(" information_transfer_to_Sink_SNC ") || instruction.contains(" INV.binds_Sk_A-TCP_from ") || instruction.contains(" INV.information_transfer_from_End_SNC ") || instruction.contains(" INV.binds_So_M-TCP_to ") || instruction.contains(" TT-SN_Bidirectional_Connection ") || instruction.contains(" INV.information_transfer_to_Intermediate_MC ") || instruction.contains(" binds_So_AP_to ") || instruction.contains(" Defined_Geographical_Element.latitude.minute ") || instruction.contains(" information_transfer_to_Intermediate_SNC ") || instruction.contains(" INV.is_represented_by_So_SN-TCP ") || instruction.contains(" binds_So_SN-CP ") || instruction.contains(" is_represented_by_So_AP ") || instruction.contains(" binds_So_SN-TCP_from ") || instruction.contains(" INV.information_transfer_to_Source_MC ") || instruction.contains(" INV.information_transfer_to_PM_LC ") || instruction.contains(" INV.tf_connection ") || instruction.contains(" information_transfer_Bidirectional_Path_SNC_CP ") || instruction.contains(" INV.information_transfer_from_PM_LC ") || instruction.contains(" has_information_transfer ") || instruction.contains(" information_transfer_Source_MC ") || instruction.contains(" Defined_Geographical_Element.latitude.degree ") || instruction.contains(" information_transfer_End_SNC ") || instruction.contains(" INV.TT-A_Sink_Connection ") || instruction.contains(" INV.is_represented_by_Intermediate_SNC ") || instruction.contains(" information_transfer_to_Uni_PM_NC ") || instruction.contains(" INV.TT-A_Source_Connection ") || instruction.contains(" INV.information_transfer_Bidirectional_Int_SNC ") || instruction.contains(" information_transfer_from_Source_SNC ") || instruction.contains(" information_transfer_Bidirectional_Int_MC ") || instruction.contains(" information_transfer_to_Source_SNC ") || instruction.contains(" information_transfer_Bidirectional_Path_NC ") || instruction.contains(" information_transfer_Bidirectional_Path_LC ") || instruction.contains(" information_transfer_from_Sink_MC ") || instruction.contains(" binds_So_AP_from ") || instruction.contains(" INV.is_represented_by_Sk_A-TCP ") || instruction.contains(" INV.information_transfer_Bidirectional_PM_TCP ") || instruction.contains(" is_represented_by_So_A-TCP ") || instruction.contains(" TT-PM_Sink_Connection ") || instruction.contains(" INV.client-server_layer_relationship "))
		{
			return true;
		} else
		{
			return false;
		}		
	}
	
	private static boolean isAtributeDeclaration(String instruction)
	{
		/* Data properties*/
		if(instruction.contains(".type:") || instruction.contains(".location:") || instruction.contains(".lat.deg:") || instruction.contains(".lat.min:") || instruction.contains(".lat.sec:") || instruction.contains(".lon.deg:") || instruction.contains(".lon.min:") || instruction.contains(".lon.sec:") )
		{
			return true;
		} else
		{
			return false;
		}		
	}
	
	private static boolean isDisjointWithDeclaration(String instruction)
	{
		/* Data properties*/
		if(instruction.contains("disjointWith"))
		{
			return true;
		} else
		{
			return false;
		}		
	}
	
	private static boolean isSameAsDeclaration(String instruction)
	{
		/* Data properties*/
		if(instruction.contains("sameAs"))
		{
			return true;
		} else
		{
			return false;
		}		
	}	

}
