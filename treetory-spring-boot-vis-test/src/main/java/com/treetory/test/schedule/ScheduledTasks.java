package com.treetory.test.schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.treetory.test.mvc.mapper.DashboardMapper;

@Component
public class ScheduledTasks {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	private DashboardMapper dMapper;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Scheduled(fixedRate = 1 * 1000)
	@Transactional(isolation=Isolation.DEFAULT, propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void sendCorelResultToSplunk() throws IOException, InterruptedException, SQLException, ParseException {
		
		List<Map<String, Object>> param = null;
		int cnt = 0;
		
		try {
			
			//String dateStr = sdf.format(new Date(System.currentTimeMillis()));
			
			param = dMapper.getCorelResultDuringRecent10Seconds(0);
			
			cnt = dMapper.insertMocaResultTest(param);
			
		} finally {
			if (param.size() != cnt) {
				LOG.debug("param size = {} ==> inserted count = {}", param.size(), cnt);
			}
		}
		
	}
	
}
