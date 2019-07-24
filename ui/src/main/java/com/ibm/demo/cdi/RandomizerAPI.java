package com.ibm.demo.cdi;

import java.io.IOException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/random")
public class RandomizerAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger= LoggerFactory.getLogger(RandomizerAPI.class);
	@Inject @Dependent RandomizerBean randomizerBean;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		if(logger.isInfoEnabled()) logger.info("/random invoked");
		try {
			response.getWriter().print(randomizerBean.getRandomNumber());
		} catch (IOException e) {
			logger.error("error with randomizerBean", e);
		}
	}
	public RandomizerAPI() {
		if(logger.isInfoEnabled())	logger.info("created");
	}
}
