package com.ibm.demo;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class RandomizerIT {
	private static String randomizerUiHost;
	private static String randomizerUiPort;
	@BeforeClass
	public static void setUp() {		
		randomizerUiHost= System.getProperty("RANDOMIZER_UI_NAME");
		randomizerUiPort= System.getProperty("RANDOMIZER_UI_PORT");
		if(randomizerUiHost==null || randomizerUiPort ==null)
			throw new RuntimeException("randomizer ui host and/or port not set");
	}
	
	@Test
	public void call_randomnumber_from_ui() {
		String url= String.format("http://%s:%s/randomizer-ui/random", randomizerUiHost, randomizerUiPort);
		HttpResponse<String> response= Unirest.get(url).asString();
		try {
			Integer.parseInt(response.getBody());
		} catch (Exception e) {
			fail(String.format("called %s and found error parsing response %s as integer", url, response.getBody()));
		}
	}

}
