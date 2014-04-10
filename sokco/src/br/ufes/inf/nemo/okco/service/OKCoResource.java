package br.ufes.inf.nemo.okco.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public class OKCoResource {

	@GET
	@Produces("text/plain")
	public String helloworld()
	{
		return "Hello motherfkcer";
	}
	
}
