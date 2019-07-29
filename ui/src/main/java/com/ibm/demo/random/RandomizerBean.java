package com.ibm.demo.random;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

/**
 * Data provider bean scoped for each user session.
 */
@ApplicationScoped
@Default
public class RandomizerBean implements RandomizerService {
	private static Logger logger= Logger.getLogger(RandomizerBean.class.getName());
	public static final String SERVER_NAME_PROPERTY="RANDOMIZER_SERVER_NAME";
	public static final String SERVICE_PORT_PROPERTY="RANDOMIZER_SERVICE_PORT";
	private String serverName=null;
	private String serverPort=null;
	
	public RandomizerBean() {
		this.serverName = System.getenv(SERVER_NAME_PROPERTY);
		this.serverPort = System.getenv(SERVICE_PORT_PROPERTY);
		if (this.serverName == null || this.serverPort == null && logger.isLoggable(Level.SEVERE))
				logger.severe(String.format("initialization found server %s and port %s", this.serverName, this.serverPort));

		if (logger.isLoggable(Level.FINE)) {
			Map<String, String> env = System.getenv();
			StringBuilder b = new StringBuilder().append("[");
			for (Map.Entry<String, String> e : env.entrySet()) {
				b.append(e.getKey()).append("=").append(e.getValue()).append(" ");
			}
			b.append("]");
			logger.fine(b.toString());
		}
	}
	
	public int getRandomNumber() {
		String url = String.format("http://%s:%s/randomizer-service/random/", this.serverName, this.serverPort);
		HttpResponse<String> response = null;
		try {
			response = Unirest.get(url).asString();
		} catch (UnirestException e) {
			;
		}
		if (response != null && response.isSuccess()) {
			if (logger.isLoggable(Level.FINE))
				logger.fine(String.format("random number from server: %s", response.getBody()));
			return absInt(response.getBody());
		} else {
			if (logger.isLoggable(Level.SEVERE))
				logger.severe(String.format("randomizer-service url: %s status: %d - %s", url,
						response == null ? -1 : response.getStatus(), response==null? "null": response.getStatusText()));
			return -1;
		}
	}
    private static int absInt(String valueStr) {
    	int result= Integer.parseInt(valueStr);
    	return Math.abs(result);
    }
}
