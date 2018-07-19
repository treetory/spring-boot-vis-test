package com.treetory.test.mvc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.treetory.test.mvc.service.DashboardService;

@RestController
public class DashboardController {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private DashboardService dService;
	
	@RequestMapping(
			value = "/result/list", 
			method = { RequestMethod.GET }, 
			produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
			consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
	public List<Map<String, Object>> getCorelResult(
			HttpServletRequest req, HttpServletResponse res
			) throws Exception {
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		try {
			
			long current = System.currentTimeMillis();
			
			LOG.debug("{}", current);
			
			result = dService.getCorelResultDuringRecent10Seconds(current);
			
			LOG.debug("{}", result);
			
		} catch (Exception e) {
			throw e;
		}
		
		return result;		
	}
	
}
