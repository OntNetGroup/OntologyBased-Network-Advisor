package br.com.padtec.nopen.model;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.ContainerStructure;
import br.com.padtec.nopen.service.NOpenComponents;
import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.Individual;

public class InstanceFabricator {
	
	//================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void createTTFAtLayer(OKCoUploader repository, String ttfId, String ttfName, String layerId, String layerName) throws Exception
	{	
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Function.toString()
		);
		i.setLabel(ttfName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+layerId, 
			repository.getNamespace()+RelationEnum.A_CardLayer_TrailTerminationFunction.toString(),
			repository.getNamespace()+ttfId
		);
		
		NOpenLog.appendLine(repository.getName()+": TTF "+ttfName+" created at Layer "+layerName);
	}
	
	public static void createTTF(OKCoUploader repository, String ttfId, String ttfName) throws Exception
	{	
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Function.toString()
		);
		i.setLabel(ttfName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": TTF "+ttfName+" created");
	}
	
	
	public static void createInputCard(OKCoUploader repository, String inCardId, String inCardName) throws Exception
	{	
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inCardId, 
			repository.getNamespace()+ConceptEnum.Input_Card.toString()
		);
		i.setLabel(inCardName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Input Card "+inCardName+" created");
	}
	
	public static void createLinkFromCardToInputCard(OKCoUploader repository, String cardId, String cardName, String inCardId, String inCardName) throws Exception
	{	
		FactoryUtil.createInstanceRelation(
				repository.getBaseModel(), 
				repository.getNamespace()+cardId, 
				repository.getNamespace()+RelationEnum.A_Card_InputCard.toString(),
				repository.getNamespace()+inCardId
			);
		
		NOpenLog.appendLine(repository.getName()+": Input Card "+inCardName+" linked to Card "+cardName);
	}
	
	public static void createOutputCard(OKCoUploader repository, String outCardId, String outCardName) throws Exception
	{	
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outCardId, 
			repository.getNamespace()+ConceptEnum.Output_Card.toString()
		);
		i.setLabel(outCardName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Output Card "+outCardName+" created");
	}
	
	public static void createOutput(OKCoUploader repository, String outId, String outName) throws Exception
	{	
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outId, 
			repository.getNamespace()+ConceptEnum.Output_Card.toString()
		);
		i.setLabel(outName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Output "+outName+" created");
	}
	
	public static void createInput(OKCoUploader repository, String inId, String inName) throws Exception
	{	
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inId, 
			repository.getNamespace()+ConceptEnum.Output_Card.toString()
		);
		i.setLabel(inName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Input "+inName+" created");
	}
	
	public static void createLinkFromCardToOutputCard(OKCoUploader repository, String cardId, String cardName, String outCardId, String outCardName) throws Exception
	{	
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.A_Card_OutputCard.toString(),
			repository.getNamespace()+outCardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Output Card "+outCardName+" linked to Card "+cardName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createAFAtCard(OKCoUploader repository, String afId, String afName, String cardId, String cardName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);
		i.setLabel(afName,"EN");
				
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(),
			repository.getNamespace()+afId
		);
		
		NOpenLog.appendLine(repository.getName()+": AF "+afName+" created at Card "+cardName);
	}
	
	public static void createAF(OKCoUploader repository, String afId, String afName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);
		i.setLabel(afName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": AF "+afName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createPhysicalMediaAtCard(OKCoUploader repository, String pmId, String pmName, String cardId, String cardName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+pmId, 
			repository.getNamespace()+ConceptEnum.Physical_Media.toString()
		);
		i.setLabel(pmName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(),
			repository.getNamespace()+pmId
		);
		
		NOpenLog.appendLine(repository.getName()+": Physical Media "+pmName+" created at Card "+cardName);
	}
	
	public static void createPhysicalMedia(OKCoUploader repository, String pmId, String pmName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+pmId, 
			repository.getNamespace()+ConceptEnum.Physical_Media.toString()
		);
		i.setLabel(pmName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Physical Media "+pmName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createMatrixAtCard(OKCoUploader repository, String mId, String mName, String cardId, String cardName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+mId, 
			repository.getNamespace()+ConceptEnum.Matrix.toString()
		);
		i.setLabel(mName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(),
			repository.getNamespace()+mId
		);
		
		NOpenLog.appendLine(repository.getName()+": Matrix "+mName+" created at Card "+cardName);
	}
	
	 
	public static void createMatrixInput(OKCoUploader repository, String mId, String mName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+mId, 
			repository.getNamespace()+ConceptEnum.Matrix_Input.toString()
		);		
		i.setLabel(mName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Matrix Input "+mName+" created");
	}
	
	public static void createMatrixOutput(OKCoUploader repository, String mId, String mName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+mId, 
			repository.getNamespace()+ConceptEnum.Matrix_Output.toString()
		);		
		i.setLabel(mName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Matrix Output "+mName+" created");
	}
	
	public static void createMatrix(OKCoUploader repository, String mId, String mName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+mId, 
			repository.getNamespace()+ConceptEnum.Matrix.toString()
		);
		i.setLabel(mName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Matrix "+mName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createLayerAtCard(OKCoUploader repository, String layerId, String layerName, String cardId, String cardName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+layerId, 
			repository.getNamespace()+ConceptEnum.Card_Layer.toString()
		);
		i.setLabel(layerName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.A_Card_CardLayer.toString(),
			repository.getNamespace()+layerId
		);
		
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" created at Card "+cardName);
	}
	
	public static void createLinkFromCardToCardLayer(OKCoUploader repository, String cardId, String cardName, String layerId, String layerName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.A_Card_CardLayer.toString(),
			repository.getNamespace()+layerId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" linked to Card Layer "+layerName);		
	}
	
	public static void createLinkFromCardLayerToTTF(OKCoUploader repository, String layerId, String layerName, String ttfId, String ttfName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+layerId, 
			repository.getNamespace()+RelationEnum.A_CardLayer_TrailTerminationFunction.toString(),
			repository.getNamespace()+ttfId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card Layer "+layerName+" linked to TTF "+ttfName);		
	}
	
	public static void createLinkFromCardToCardElement(OKCoUploader repository, String cardId, String cardName, String tfId, String tfName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(),
			repository.getNamespace()+tfId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" linked to Card TF"+tfName);		
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteTF(OKCoUploader repository, String tfId, String tfName, String tfType)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+tfId
		);
		
		NOpenLog.appendLine(repository.getName()+": TF "+tfType+"-"+tfName+" deleted");
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createAFOutput(OKCoUploader repository, String outputId, String outputName, String afId, String afName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outputId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Function_Output.toString()
		);
		i.setLabel(outputName,"EN");		
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+RelationEnum.A_AdaptationFunction_AdaptationFunctionOutput.toString(),
			repository.getNamespace()+outputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": AF Output "+outputName+" created at AF: "+afName);
	}
	
	public static void createAFInput(OKCoUploader repository, String inputId, String inputName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inputId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Function_Input.toString()
		);
		i.setLabel(inputName,"EN");	
		
		NOpenLog.appendLine(repository.getName()+": AF Input "+inputName+" created");
	}
	
	public static void createAFOutput(OKCoUploader repository, String outputId, String outputName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outputId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Function_Output.toString()
		);
		i.setLabel(outputName,"EN");	
		
		NOpenLog.appendLine(repository.getName()+": AF Output "+outputName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createAFInput(OKCoUploader repository, String inputId, String inputName, String afId, String afName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inputId, 
			repository.getNamespace()+ConceptEnum.Adaptation_Function_Input.toString()
		);
		i.setLabel(inputName,"EN");	
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+RelationEnum.A_AdaptationFunction_AdaptationFunctionInput,
			repository.getNamespace()+inputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": AF Input "+inputName+" created at AF: "+afName);
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createTTFOutput(OKCoUploader repository, String outputId, String outputName, String ttfId, String ttfName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outputId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Function_Output.toString()
		);
		i.setLabel(outputName,"EN");	
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.A_TrailTerminationFunction_TrailTerminationFunctionOutput.toString(),
			repository.getNamespace()+outputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": TTF Output "+outputName+" created at TTF: "+ttfName);
	}
	
	public static void createTTFInput(OKCoUploader repository, String inputId, String inputName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inputId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Function_Input.toString()
		);
		i.setLabel(inputName,"EN");	
		
		NOpenLog.appendLine(repository.getName()+": TTF Input "+inputName+" created");
	}
	
	public static void createTTFOutput(OKCoUploader repository, String outputId, String outputName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+outputId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Function_Output.toString()
		);
		i.setLabel(outputName,"EN");	
		
		NOpenLog.appendLine(repository.getName()+": TTF Output "+outputName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createTTFInput(OKCoUploader repository, String inputId, String inputName, String ttfId, String ttfName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+inputId, 
			repository.getNamespace()+ConceptEnum.Trail_Termination_Function_Input.toString()
		);
		i.setLabel(inputName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.A_TrailTerminationFunction_TrailTerminationFunctionInput.toString(),
			repository.getNamespace()+inputId
		);		
		
		NOpenLog.appendLine(repository.getName()+": TTF Input "+inputName+" created at TTF: "+ttfName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deletePort(OKCoUploader repository, String portId, String portName, String portType)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+portId
		);		
		
		NOpenLog.appendLine(repository.getName()+": Port "+portType+"-"+portName+" deleted");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void insertLayerLink(OKCoUploader repository, String layerId, String layerName, String cardId, String cardName) throws Exception 
	{
//		FactoryUtil.createInstanceRelation(
//			repository.getBaseModel(), 
//			repository.getNamespace()+cardId, 
//			repository.getNamespace()+RelationEnum.ComponentOf3_Card_Layer,
//			repository.getNamespace()+layerId
//		);
//		
//		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" inserted at Card: "+cardName);
	}
		
	/**
	 * @author John Guerson
	 */
	public static void removeLayerLink(OKCoUploader repository, String layerId, String layerName, String cardId, String cardName)
	{
//		FactoryUtil.deleteObjectProperty(
//			repository.getBaseModel(),
//			repository.getNamespace()+cardId, 
//			repository.getNamespace()+RelationEnum.ComponentOf3_Card_Layer,
//			repository.getNamespace()+layerId
//		);		
//		
//		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" removed from Card "+cardName);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void changeLayerOfTTF(OKCoUploader repository, String ttfId, String ttfName, String srcLayerId, String srcLayerName, String tgtLayerId, String tgtLayerName) throws Exception
	{	
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(),
			repository.getNamespace()+srcLayerId, 
			repository.getNamespace()+RelationEnum.A_CardLayer_TrailTerminationFunction.toString(),
			repository.getNamespace()+ttfId
		);
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+tgtLayerId, 
			repository.getNamespace()+RelationEnum.A_CardLayer_TrailTerminationFunction.toString(),
			repository.getNamespace()+ttfId
		);
		
		NOpenLog.appendLine(repository.getName()+": TTF's Layer Changed From "+srcLayerName+" to "+tgtLayerName);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void createLinkFromTTFToAF(OKCoUploader repository, String ttfId, String ttfName, String afId, String afName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+ttfId, 
			repository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Adaptation_Function.toString(),
			repository.getNamespace()+afId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link created from TTF "+ttfName+" to AF "+afName);
	}
	
	/**
	 * @author John Guerson
	 * @throws Exception 
	 */
	public static void createLinkFromAFToTTF(OKCoUploader repository, String afId, String afName, String ttfId, String ttfName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+afId, 
			repository.getNamespace()+RelationEnum.binds_Adaptation_Function_Trail_Termination_Function.toString(),
			repository.getNamespace()+ttfId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link created from AF "+afName+" to TTF "+ttfName);
	}	
	
	public static void deleteLinkFromTTFToAF(OKCoUploader repository, String srcTTFId, String srcTTFName, String tgtAFId, String tgtAFName)
	{
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(),
			repository.getNamespace()+srcTTFId, 
			repository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Adaptation_Function.toString(),
			repository.getNamespace()+tgtAFId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link Deleted from TTF "+srcTTFName+" to AF "+tgtAFName);
	}
	
	public static void deleteLinkFromAFToTTF(OKCoUploader repository, String srcAFId, String srcAFName, String tgtTTFId, String tgtTTFName)
	{
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(),
			repository.getNamespace()+srcAFId, 
			repository.getNamespace()+RelationEnum.binds_Adaptation_Function_Trail_Termination_Function.toString(),
			repository.getNamespace()+tgtTTFId
		);
		
		NOpenLog.appendLine(repository.getName()+": Link Deleted from AF "+srcAFName+" to TTF "+tgtTTFName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createRack(OKCoUploader repository, String rackId, String rackName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+rackId, 
			repository.getNamespace()+ConceptEnum.Rack.toString()
		);
		i.setLabel(rackName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Rack "+rackName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createRackForShelf(OKCoUploader repository, String rackId, String rackName, String shelfId, String shelfName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+rackId, 
			repository.getNamespace()+ConceptEnum.Rack.toString()
		);
		i.setLabel(rackName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+rackId, 
			repository.getNamespace()+RelationEnum.A_Rack_Shelf.toString(),
			repository.getNamespace()+shelfId
		);	
		
		NOpenLog.appendLine(repository.getName()+": Rack "+rackName+" created for Shelf "+shelfName);
	}	
	
	/**
	 * @author John Guerson
	 */
	public static void createShelfAtRack(OKCoUploader repository, String shelfId, String shelfName, String rackId, String rackName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId, 
			repository.getNamespace()+ConceptEnum.Shelf.toString()
		);
		i.setLabel(shelfName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+rackId, 
			repository.getNamespace()+RelationEnum.A_Rack_Shelf.toString(),
			repository.getNamespace()+shelfId
		);		
		
		NOpenLog.appendLine(repository.getName()+": Shelf "+shelfName+" created at Rack: "+rackName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createShelfForSlot(OKCoUploader repository, String shelfId, String shelfName, String slotId, String slotName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId, 
			repository.getNamespace()+ConceptEnum.Shelf.toString()
		);
		i.setLabel(shelfName,"EN");

		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId, 
			repository.getNamespace()+RelationEnum.A_Shelf_Slot.toString(),
			repository.getNamespace()+slotId
		);		
		
		NOpenLog.appendLine(repository.getName()+": Shelf "+shelfName+" created for Slot: "+slotId);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSlotAtShelf(OKCoUploader repository, String slotId, String slotName, String shelfId, String shelfName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);
		i.setLabel(slotName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId, 
			repository.getNamespace()+RelationEnum.A_Shelf_Slot.toString(),
			repository.getNamespace()+slotId
		);	
		
		NOpenLog.appendLine(repository.getName()+": Slot "+slotName+" created at Shelf: "+shelfName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSubSlotForCard(OKCoUploader repository, String subslotId, String subslotName, String cardId, String cardName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+subslotId, 
			repository.getNamespace()+ConceptEnum.Subslot.toString()
		);
		i.setLabel(subslotName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+subslotId, 
			repository.getNamespace()+RelationEnum.A_Subslot_Card.toString(),
			repository.getNamespace()+cardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Subslot "+subslotName+" created at Card: "+cardName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSlotForCard(OKCoUploader repository, String slotId, String slotName, String cardId, String cardName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);
		i.setLabel(slotName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+RelationEnum.A_Slot_Card.toString(),
			repository.getNamespace()+cardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Slot "+slotName+" created at Card: "+cardName);
	}

	/**
	 * @author John Guerson
	 */
	public static void createSubSlotAtSlot(OKCoUploader repository, String subslotId, String subslotName, String slotId, String slotName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+subslotId, 
			repository.getNamespace()+ConceptEnum.Subslot.toString()
		);
		i.setLabel(subslotName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+RelationEnum.A_Slot_Subslot.toString(),
			repository.getNamespace()+subslotId
		);	
		
		NOpenLog.appendLine(repository.getName()+": SubSlot "+subslotName+" created at Slot: "+slotName);
	}
	
	public static void createEquipment(OKCoUploader repository, String equipmentId, String equipmentName) throws Exception
	{			
		Individual ind = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			equipmentId, 
			repository.getNamespace()+ConceptEnum.Equipment.toString()
		);
		ind.setLabel(equipmentName, "EN");
		
		NOpenLog.appendLine(repository.getName()+": Equipment "+equipmentName+" created");
	}
	
	/**
	 * @author John Guerson and Jordana Salamon
	 */
	public static void createCard(OKCoUploader repository, String cardName) throws Exception
	{		
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardName,
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		i.setLabel(cardName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created");		
	}
	
	public static void createCard(OKCoUploader repository, String cardId, String cardName) throws Exception
	{		
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId,
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		i.setLabel(cardName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created");		
	}
		
	public static void createCardLayer(OKCoUploader repository, String cardLayerId, String cardLayerName) throws Exception
	{		
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardLayerId,
			repository.getNamespace()+ConceptEnum.Card_Layer.toString()
		);
		
		i.setLabel(cardLayerName,"EN");
		
		NOpenLog.appendLine(repository.getName()+": Card Layer "+cardLayerName+" created");		
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createCardAtSupervisor(OKCoUploader repository, String cardId, String cardName, String supervisorId, String supervisorName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			cardId, 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		i.setLabel(cardName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(),
			supervisorId,
			repository.getNamespace()+RelationEnum.A_Slot_Card.toString(), 
			cardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created at Supervisor "+supervisorName);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createCardAtSlot(OKCoUploader repository, String cardId, String cardName, String slotId, String slotName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		i.setLabel(cardName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId, 
			repository.getNamespace()+RelationEnum.A_Slot_Card.toString(),
			repository.getNamespace()+cardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created at Slot: "+slotName);
	}
	
	
	/**
	 * @author John Guerson
	 */
	public static void createCardAtSubSlot(OKCoUploader repository, String cardId, String cardName, String subslotId, String subslotName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId, 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		i.setLabel(cardName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+subslotId, 
			repository.getNamespace()+RelationEnum.A_Subslot_Card.toString(),
			repository.getNamespace()+cardId
		);
		
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" created at SubSlot: "+subslotName);
	}	
	
	/**
	 * @author John Guerson
	 */
	public static void deleteEquipment(OKCoUploader repository, String holderId, String holderName, String holderType) throws Exception
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+holderId			
		);		
		
		NOpenLog.appendLine(repository.getName()+": Equipment Holder "+holderType+"-"+holderName+" deleted");
	}
		
	/**
	 * @author John Guerson
	 */
	public static void deleteShelf(OKCoUploader repository, String shelfId, String shelfName)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+shelfId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Shelf "+shelfName+" deleted");	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteSlot(OKCoUploader repository, String slotId, String slotName)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+slotId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Slot "+slotName+" deleted");	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteCard(OKCoUploader repository, String cardId, String cardName)
	{
		FactoryUtil.deleteIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Card "+cardName+" deleted");	
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createSupervisor(OKCoUploader repository, String supervisorId, String supervisorName, String equipmentId, String equipmentName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+supervisorId,			 
			repository.getNamespace()+ConceptEnum.Supervisor.toString()			
		);
		i.setLabel(supervisorName,"EN");
		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+supervisorId,			 
			repository.getNamespace()+RelationEnum.supervises_Supervisor_Equipment.toString(),
			repository.getNamespace()+equipmentId
		);
		
		NOpenLog.appendLine(repository.getName()+": Supervisor "+supervisorName+" created for Equipment "+equipmentName);
	}
	
	public static void createLinkFromSupervisorToEquipment(OKCoUploader repository, String supervisorId, String supervisorName, String equipmentId, String equipmentName) throws Exception
	{
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+supervisorId,			 
			repository.getNamespace()+RelationEnum.supervises_Supervisor_Equipment.toString(),
			repository.getNamespace()+equipmentId
		);
		
		NOpenLog.appendLine(repository.getName()+": Supervisor "+supervisorName+" linked to Equipment "+equipmentName);
	}
	
	public static void createSupervisor(OKCoUploader repository, String supervisorId, String supervisorName) throws Exception
	{
		Individual i = FactoryUtil.createInstanceIndividual(
			repository.getBaseModel(), 
			repository.getNamespace()+supervisorId,			 
			repository.getNamespace()+ConceptEnum.Supervisor.toString()			
		);
		i.setLabel(supervisorName,"EN");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void superviseCard(OKCoUploader repository, String supervisorId, String supervisorName, String cardId, String cardName) throws Exception 
	{		
		FactoryUtil.createInstanceRelation(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId,			 
			repository.getNamespace()+RelationEnum.supervises_card_Supervisor_Card.toString(),
			repository.getNamespace()+supervisorId
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void unsuperviseCard(OKCoUploader repository, String supervisorId, String supervisorName, String cardId, String cardName) throws Exception 
	{		
		FactoryUtil.deleteObjectProperty(
			repository.getBaseModel(), 
			repository.getNamespace()+cardId,			 
			repository.getNamespace()+RelationEnum.supervises_card_Supervisor_Card.toString(),
			repository.getNamespace()+supervisorId
		);
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteSupervisor(OKCoUploader repository, String supervisorId, String supervisorName)
	{
		FactoryUtil.deleteIndividual(
			StudioComponents.studioRepository.getBaseModel(), 
			StudioComponents.studioRepository.getNamespace()+supervisorId			
		);
			
		NOpenLog.appendLine(repository.getName()+": Supervisor "+supervisorName+" deleted");		
	}
	
	//================================================================================
	
	/**
	 * @author John Guerson
	 */
	public static void createTechnology(OKCoUploader repository, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+techName;		
		String techURI = repository.getNamespace()+ConceptEnum.Technology.toString();
		Individual i = FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI,techURI);
		i.setLabel(techName, "EN");
		NOpenLog.appendLine(repository.getName()+": Technology "+techName+" created");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void deleteTechnology(OKCoUploader repository, String techName) 
	{		
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+techName);
		NOpenLog.appendLine(repository.getName()+": Technology "+techName+" deleted");
	}
	
	/**
	 * @author John Guerson
	 */
	public static void createLayer(OKCoUploader repository, String layerName, String techName) throws Exception
	{
		String indURI = repository.getNamespace()+layerName;		
		String layerURI = repository.getNamespace()+ConceptEnum.Layer_Type.toString();
		Individual i = FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indURI, layerURI);
		i.setLabel(layerName,"EN");
		
		String ind2URI = repository.getNamespace()+techName;		
		String techToLayerURI = repository.getNamespace()+RelationEnum.A_Technology_LayerType.toString();		
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),ind2URI, techToLayerURI, indURI);
		
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" created for Technology "+techName);
	}	

	public static void createIsClientOf(OKCoUploader repository, String clientLayerName, String serverLayerName) throws Exception
	{
		String clientURI = repository.getNamespace()+clientLayerName;
		String serverURI = repository.getNamespace()+serverLayerName;
		String isClientURI = repository.getNamespace()+RelationEnum.is_client_Layer_Type_Layer_Type.toString();	
		FactoryUtil.createInstanceRelation(repository.getBaseModel(),clientURI, isClientURI, serverURI);
		
		NOpenLog.appendLine(repository.getName()+": Layer "+clientLayerName+" is client of Layer "+serverLayerName);
	}
		
	/**
	 * @author John Guerson
	 */
	public static void deleteLayer(OKCoUploader repository, String layerName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+layerName);
		NOpenLog.appendLine(repository.getName()+": Layer "+layerName+" deleted");
	}
		
	/**
	 * @author John Guerson
	 */
	public static void createService(OKCoUploader repository, String serviceName, String layerName, String techName) throws Exception
	{
		//String indLayerURI = repository.getNamespace()+layerName;		
		String indServURI = repository.getNamespace()+serviceName;
		String serviceURI = repository.getNamespace()+ConceptEnum.Service.toString();
		Individual i = FactoryUtil.createInstanceIndividual(repository.getBaseModel(), indServURI, serviceURI);		
		i.setLabel(serviceName,"EN");
		
		//Exception thrown: relation is UNDECLARED. This is weird...
		//FactoryUtil.createInstanceRelation(repository.getBaseModel(),indLayerURI, RelationEnum.implements_Layer_Service.toString(), indServURI);		
		
		NOpenLog.appendLine(repository.getName()+": Service "+serviceName+" created for Layer "+layerName+" and Tech "+techName);
	}	
	
	/**
	 * @author John Guerson
	 */
	public static void deleteService(OKCoUploader repository, String serviceName) 
	{
		FactoryUtil.deleteIndividual(repository.getBaseModel(), repository.getNamespace()+serviceName);
		NOpenLog.appendLine(repository.getName()+": Service "+serviceName+" deleted");
	}
			
	/**
	 * @author John Guerson
	 */
	public static void createEquipmentHolder(String equipHolderId, OKCoUploader repository) throws Exception
	{
		String individualURI = repository.getNamespace()+equipHolderId;
		if(!QueryUtil.individualExists(repository.getBaseModel(), individualURI))
		{
			String classURI = repository.getNamespace()+ConceptEnum.Equipment_Holder.toString();
			FactoryUtil.createInstanceIndividual(repository.getBaseModel(), individualURI, classURI);			
			NOpenLog.appendLine(repository.getName()+": Equipment Holder "+equipHolderId+" created");
		}
	}
	
	/**
	 * @author Jordana Salamon
	 * @throws Exception 
	 */
	public static void createComponentOfRelation(DtoJointElement dtoContainer, DtoJointElement dtoContent) throws Exception{
		//create the property relation between source and target

		if(!ContainerStructure.isTargetOfComponentOfRelation(dtoContent.getType()) && dtoContainer.getId() == null){ 
			FactoryUtil.createInstanceIndividual(
					StudioComponents.studioRepository.getBaseModel(),
					StudioComponents.studioRepository.getNamespace() + dtoContent.getId(),
					StudioComponents.studioRepository.getNamespace() + dtoContent.getType(),
					true);
			NOpenLog.appendLine(StudioComponents.studioRepository.getName()+": " + dtoContent.getId() + " created. ");
		}
		else{	
			String sourceURI = StudioComponents.studioRepository.getNamespace() + dtoContainer.getId();
			String targetURI = StudioComponents.studioRepository.getNamespace() + dtoContent.getId();

			String nameSource = dtoContainer.getName();
			String typeSource = dtoContainer.getType();
			String nameTarget = dtoContent.getName();
			String typeTarget = dtoContent.getType();

				if(ContainerStructure.verifyContainerRelation(sourceURI, dtoContainer.getType(), targetURI, dtoContent.getType())){
					String typeTargetURI = StudioComponents.studioRepository.getNamespace() + dtoContent.getType();
					String propertyURI = NOpenComponents.nopenRepository.getNamespace() + RelationEnum.componentOf.toString();
					ArrayList<String> specificPropertyURIs = QueryUtil.getRelationsBetweenClasses(NOpenComponents.nopenRepository.getBaseModel(), NOpenComponents.nopenRepository.getNamespace() + typeSource, NOpenComponents.nopenRepository.getNamespace() + typeTarget, propertyURI);
					String property = null;
					if(specificPropertyURIs.isEmpty()){
						List<String> supertypes = QueryUtil.getSupertypesURIs(NOpenComponents.nopenRepository.getBaseModel(), NOpenComponents.nopenRepository.getNamespace() + typeTarget);
						for(String s : supertypes){ 
							if(ContainerStructure.isTargetOfComponentOfRelation(s.substring(s.indexOf("#") +1))){
								ArrayList<String> properties = QueryUtil.getRelationsBetweenClasses(NOpenComponents.nopenRepository.getBaseModel(), NOpenComponents.nopenRepository.getNamespace() + typeSource, s, propertyURI);
								property = properties.get(0);
							}
						}
					}
					else{
						property = specificPropertyURIs.get(0).substring(specificPropertyURIs.get(0).indexOf("#")+1);
					}
					
					String specificPropertyURI = StudioComponents.studioRepository.getNamespace() + property.substring(property.indexOf("#")+1);
	
					
					FactoryUtil.createInstanceIndividual(
							StudioComponents.studioRepository.getBaseModel(),
							targetURI,
							typeTargetURI,
							true);

					if(typeTargetURI.equalsIgnoreCase(StudioComponents.studioRepository.getNamespace() + ConceptEnum.Card_Layer.toString())){
						String layerPropertyURI = StudioComponents.studioRepository.getNamespace() + RelationEnum.instantiates_Card_Layer_Layer_Type.toString();
						String layerTypeURI = StudioComponents.studioRepository.getNamespace() + nameTarget;
						FactoryUtil.createInstanceRelation(
								StudioComponents.studioRepository.getBaseModel(), 
								targetURI,			 
								layerPropertyURI,
								layerTypeURI
							);
					}
				
					FactoryUtil.createInstanceRelation(
						StudioComponents.studioRepository.getBaseModel(), 
						sourceURI,			 
						specificPropertyURI,
						targetURI
					);
				
					NOpenLog.appendLine(StudioComponents.studioRepository.getName()+": " + nameSource + " linked with " + nameTarget);
			}
			else{
				NOpenLog.appendLine("Error: " + nameSource + " cannot be connected to " + nameTarget + " because there is no \"componentOf\" relation between " + typeSource + " and " + typeTarget);
				throw new Exception("Error: Unexpected relation between " + nameSource + " and " + nameTarget + " because there is no \"componentOf\" relation between " + typeSource + " and " + typeTarget);	
			}
		}
	}
	
}
