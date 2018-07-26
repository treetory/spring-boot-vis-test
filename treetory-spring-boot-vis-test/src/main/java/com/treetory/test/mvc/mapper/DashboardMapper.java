package com.treetory.test.mvc.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.treetory.test.common.annotation.BaseDs;

@BaseDs
public interface DashboardMapper {

	public List<Map<String, Object>> getCorelResultDuringRecent10Seconds(long currentTime) throws SQLException;
	
	public int insertMocaResultTest(List<Map<String, Object>> param) throws SQLException;
	
	public List<Map<String, Object>> getRecentMocaResultCount() throws SQLException;
	
	public List<Map<String, Object>> getEndpointAsset() throws SQLException;
	
	public List<Map<String, Object>> getVirusNormalizedLogStatisticsByName() throws SQLException;
	
	public List<Map<String, Object>> getVirusNormalizedLogStatisticsByDip() throws SQLException;
	
	public List<Map<String, Object>> getNacNormalizedLogStatisticsByEvent() throws SQLException;
	
	public List<Map<String, Object>> getNacNormalizedLogStatisticsBySip() throws SQLException;
	
	public List<Map<String, Object>> getMocaResultStatisticsByRule() throws SQLException;
	
	public List<Map<String, Object>> getMocaResultStatisticsBySip() throws SQLException;
	
	public List<Map<String, Object>> getMocaResultStatisticsByDip() throws SQLException;
	
}
