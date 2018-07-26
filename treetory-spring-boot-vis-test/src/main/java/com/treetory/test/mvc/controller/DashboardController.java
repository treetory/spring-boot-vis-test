package com.treetory.test.mvc.controller;

import java.util.HashMap;
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
			value = "/dashboard/items", 
			method = { RequestMethod.GET }, 
			produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
			consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getDashboardItems(
			HttpServletRequest req, HttpServletResponse res
			) throws Exception {
		
		Map<String, Object> items = new HashMap<String, Object>();
		
		try {
			
			long current = System.currentTimeMillis();
			
			items = dService.getDashboardItems(current);
			
			LOG.debug("{}", items.size());
			
		} catch (Exception e) {
			throw e;
		}
		
		return items;		
	}
	
}
