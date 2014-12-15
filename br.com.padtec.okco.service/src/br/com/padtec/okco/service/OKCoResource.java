package br.com.padtec.okco.service;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.padtec.okco.core.feature.OKCoFeatures;
import br.com.padtec.okco.core.feature.OKCoResult;
import br.com.padtec.okco.core.feature.OKCoResultFromFile;

import com.google.gson.Gson;

@Path("/app")
public class OKCoResource {

	@GET
	@Path("/getSokcoObjectJSON")
	@Produces(MediaType.APPLICATION_JSON)
	public OKCoObject getSokcoObjectJSON() {
		
		OKCoObject obj = new OKCoObject();
		
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
	public Response listFileIncompleteness(OKCoObject obj) {
 
		OKCoFeatures o = new OKCoFeatures();
		OKCoResult dto = o.listFileIncompleteness(obj.getPathOwlFileString(), obj.getReasonerOption());
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(dto);
		
		return Response.status(201).entity(resultJson).build();
 
	}
	
	@POST
	@Path("/completePropertyIncompleteness")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completePropertyIncompleteness(OKCoObject obj) {
 
		OKCoFeatures o = new OKCoFeatures();
		OKCoResultFromFile dto = o.completeIncompleteness(obj.getPathOwlFileString(), obj.getReasonerOption(), obj.getStrength());
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(dto);
		
		return Response.status(201).entity(resultJson).build(); 
	}
	
	@POST
	@Path("/completePropertyIncompletenessSet")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completePropertyIncompletenessSet(OKCoObject obj) {
 
		OKCoFeatures o = new OKCoFeatures();
		OKCoResultFromFile dto = o.completeIncompleteness(obj.getSetInstances(), obj.getPathOwlFileString(), obj.getReasonerOption(), obj.getStrength());
		
		Gson gson = new Gson();
		String resultJson = gson.toJson(dto);
		return Response.status(201).entity(resultJson).build();
		
 
	}
	
}
