package br.ufes.inf.nemo.okco.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/helloworld")
public class OKCoResource {

	@GET
	@Path("/text")
	@Produces("text/plain")
	public String helloworld()
	{
		return "Hello motherfkcer";
	}
	
	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Track  helloworldJson()
	{
		Track track = new Track();
		track.setTitle("Enter Sandman");
		track.setSinger("Metallica");
 
		return track;
	}
}
