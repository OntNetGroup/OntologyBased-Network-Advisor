package br.com.padtec.nopen.studio.model;


public class StudioFactory {

	public static void createTransportFunction(String id, String layer)
	{
//		FactoryUtil.createInstanceIndividual(
//			OKCoUploader.getBaseModel(), 
//			OKCoUploader.getNamespace()+id, 
//			OKCoUploader.getNamespace()+ConceptEnum.TRANSPORT_FUNCTION.toString()
//		);
//		FactoryUtil.createInstanceRelation(
//			OKCoUploader.getBaseModel(), 
//			OKCoUploader.getNamespace()+ConceptEnum.TRANSPORT_FUNCTION.toString(), 
//			OKCoUploader.getNamespace()+RelationEnum.APPLIES.toString(), 
//			OKCoUploader.getNamespace()+ConceptEnum.LAYER.toString()
//		);
		System.out.println("Transport Function \""+id+"\" created at layer \""+layer+"\"");
	}

	public static void deleteTransportFunction(String id) 
	{		
		System.out.println("Transport Function \""+id+"\" deleted");
	}

	public static void createPort(String portID, String transportFunctionID) 
	{
		System.out.println("Port \""+portID+"\" created at transport function \""+transportFunctionID+"\"");
	}

	public static void deletePort(String id) 
	{
		System.out.println("Port \""+id+"\" deleted");
	}

	public static void createLink(String linkID, String sourcePortID, String targetPortID) 
	{
		System.out.println("Link \""+linkID+"\" created between \""+sourcePortID+"\" and \""+targetPortID+"\"");
	}

	public static void deleteLink(String id) 
	{
		System.out.println("Link \""+id+"\" deleted");
	}
}
