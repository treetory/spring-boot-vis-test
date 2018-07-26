package com.treetory.test.mvc.service;

import java.sql.SQLException;
import java.util.HashMap;
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
	public Map<String, Object> getDashboardItems(long current) throws SQLException {
		
		Map<String, Object> items = new HashMap<String, Object>();
		
		items.put("moca_result", dMapper.getCorelResultDuringRecent10Seconds(System.currentTimeMillis()));
		items.put("moca_result_count", dMapper.getRecentMocaResultCount());
		items.put("endpoint_list", dMapper.getEndpointAsset());
		items.put("virus_stat_name", dMapper.getVirusNormalizedLogStatisticsByName());
		items.put("virus_stat_dip", dMapper.getVirusNormalizedLogStatisticsByDip());
		items.put("nac_stat_event", dMapper.getNacNormalizedLogStatisticsByEvent());
		items.put("nac_stat_sip", dMapper.getNacNormalizedLogStatisticsBySip());
		items.put("moca_stat_rule", dMapper.getMocaResultStatisticsByRule());
		items.put("moca_stat_sip", dMapper.getMocaResultStatisticsBySip());
		items.put("moca_stat_dip", dMapper.getMocaResultStatisticsByDip());
		
		return items;
	}
	
	@Override
	public List<Map<String, Object>> getCorelResultDuringRecent10Seconds(long currentTime) throws SQLException {
		return dMapper.getCorelResultDuringRecent10Seconds(System.currentTimeMillis());
	}

	@Override
	public List<Map<String, Object>> getRecentMocaResultCount() throws SQLException {
		return dMapper.getRecentMocaResultCount();
	}

}
