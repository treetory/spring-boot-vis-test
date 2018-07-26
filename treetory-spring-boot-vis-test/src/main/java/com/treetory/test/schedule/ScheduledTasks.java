package com.treetory.test.schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	
	private static int i = 0;
	
	//@Scheduled(fixedRate = 1 * 1000)
	@Transactional(isolation=Isolation.DEFAULT, propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void sendCorelResultToSplunk() throws IOException, InterruptedException, SQLException, ParseException {
		
		List<Map<String, Object>> target = null;
		int cnt = 0;
		
		try {
			
			i++;
			
			target = dMapper.getCorelResultDuringRecent10Seconds(0);
			
			if (i==10) {
				i=0;
			}
			
			List<Map<String, Object>> param = new ArrayList<Map<String, Object>>();
			param.add(target.get(i));
			
			cnt = dMapper.insertMocaResultTest(param);
			
		} finally {
			if (cnt != 1) {
				LOG.debug("inserted count is {}.", cnt);
			}
		}
		
	}
	
}
