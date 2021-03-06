package com.treetory.test.mvc.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DashboardService {

	public Map<String, Object> getDashboardItems(long current) throws SQLException;
	
	public Map<String, Object> getDashboardItemsFromJson(long current) throws SQLException, IOException;
	
	public List<Map<String, Object>> getCorelResultDuringRecent10Seconds(long currentTime) throws SQLException;
	
	public List<Map<String, Object>> getRecentMocaResultCount() throws SQLException;

}
