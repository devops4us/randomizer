package com.ibm.random;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/random")
public class RandomNumberService {
	Logger logger= Logger.getLogger(RandomNumberService.class.getName());
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public int random() {
		Random rand= new Random(new Date().getTime());
		int result= rand.nextInt();
		if(logger.isLoggable(Level.INFO))
				logger.info(String.format("generated random: %s",result));
		return result;
	}	
}
