package com.treetory.test.schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.treetory.test.mvc.mapper.DashboardMapper;

@Component
public class ScheduledTasks {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	private DashboardMapper dMapper;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Scheduled(fixedRate = 10 * 1000)
	public void sendCorelResultToSplunk() throws IOException, InterruptedException, SQLException, ParseException {
		
		try {
			
			//String dateStr = sdf.format(new Date(System.currentTimeMillis()));
			
			List<Map<String, Object>> param = dMapper.getCorelResultDuringRecent10Seconds(0);
			
			dMapper.insertMocaResultTest(param);
			
		} finally {
			
		}
		
	}
	
}
