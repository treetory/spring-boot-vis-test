package com.treetory.test.mvc.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treetory.test.mvc.mapper.DashboardMapper;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private DashboardMapper dMapper;
	
	@Override
	public List<Map<String, Object>> getCorelResultDuringRecent10Seconds(long currentTime) throws SQLException {
		return dMapper.getCorelResultDuringRecent10Seconds(System.currentTimeMillis());
	}

	@Override
	public Map<String, Object> getRecentMocaResultCount() throws SQLException {
		return dMapper.getRecentMocaResultCount();
	}

}
