package com.treetory.test.mvc.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.treetory.test.common.annotation.BaseDs;

@BaseDs
public interface DashboardMapper {

	public List<Map<String, Object>> getCorelResultDuringRecent10Seconds(long currentTime) throws SQLException;
	
	public int insertMocaResultTest(List<Map<String, Object>> param) throws SQLException;
	
	public Map<String, Object> getRecentMocaResultCount() throws SQLException;
	
}
