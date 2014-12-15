package br.com.padtec.okco.service;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;

import com.google.gson.Gson;

import br.com.padtec.common.features.okco.OKCoFeatures;
import br.com.padtec.common.features.okco.OKCoResult;
import br.com.padtec.common.features.okco.OKCoResultFromFile;

@Path("/app")
public class OKCoResource {

	@GET
	@Path("/getSokcoObjectJSON")
	@Produces(MediaType.APPLICATION_JSON)
	public SokcoObject getSokcoObjectJSON() {
		
		SokcoObject obj = new SokcoObject();
		
		obj.setPathOwlFileString("C://Users//fabio_000//Desktop//OntologiasOWL//assassinato.owl");
		obj.setReasonerOption("PELLET");
		obj.setStrength("FULL");
		
		ArrayList<String> list = new ArrayList<>();
		list.add("i1");
		obj.setSetInstances(list);
 
		return obj; 
	}
 
	@POST
	@Path("/listFileIncompleteness")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listFileIncompleteness(SokcoObject obj) {
 
		OKCoFeatures o = new OKCoFeatures();
		OKCoResult dto = o.listFileIncompleteness(obj.getPathOwlFileString(), obj.getReasonerOption());
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(dto);
		
		return Response.status(201).entity(resultJson).build();
 
	}
	
	@POST
	@Path("/completePropertyIncompleteness")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completePropertyIncompleteness(SokcoObject obj) {
 
		OKCoFeatures o = new OKCoFeatures();
		OKCoResultFromFile dto = o.completeIncompleteness(obj.getPathOwlFileString(), obj.getReasonerOption(), obj.getStrength());
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(dto);
		
		return Response.status(201).entity(resultJson).build(); 
	}
	
	@POST
	@Path("/completePropertyIncompletenessSet")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completePropertyIncompletenessSet(SokcoObject obj) {
 
		OKCoFeatures o = new OKCoFeatures();
		OKCoResultFromFile dto = o.completeIncompleteness(obj.getSetInstances(), obj.getPathOwlFileString(), obj.getReasonerOption(), obj.getStrength());
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(dto);
		return Response.status(201).entity(resultJson).build();
		
 
	}
	
}
