package com.treetory.test.mvc.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DashboardService {

	public List<Map<String, Object>> getCorelResultDuringRecent10Seconds(long currentTime) throws SQLException;
	
}
