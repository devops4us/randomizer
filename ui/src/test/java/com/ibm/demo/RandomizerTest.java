package com.ibm.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ibm.demo.random.RandomizerBean;

public class RandomizerTest {

	@Test
	public void get_randomnumber_without_server() {
		RandomizerBean rb= new RandomizerBean();
		int rn= rb.getRandomNumber();
		assertEquals(-1,rn);
	}
	
}
