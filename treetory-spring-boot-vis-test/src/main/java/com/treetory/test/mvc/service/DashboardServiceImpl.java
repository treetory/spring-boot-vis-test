package com.treetory.test.mvc.service;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.treetory.test.mvc.mapper.DashboardMapper;

@Service
public class DashboardServiceImpl implements DashboardService {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardServiceImpl.class);
	
	@Autowired
	private DashboardMapper dMapper;

	@Autowired
	private Gson gson;
	
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

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getDashboardItemsFromJson(long current) throws SQLException, IOException {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Resource resource = null;
		FileReader fReader = null;
		JsonReader jReader = null;
		
		try {
			
			resource = new ClassPathResource("sample/sample.json");
			fReader = new FileReader(resource.getFile());
			jReader = new JsonReader(fReader);
			
			Map<String, Object> json = gson.fromJson(jReader, HashMap.class);
			
			List<Map<String, Object>> virus_stat_name = (List<Map<String, Object>>) json.get("virus_stat_name");
			List<Map<String, Object>> moca_stat_sip = (List<Map<String, Object>>) json.get("moca_stat_sip");
			List<Map<String, Object>> nac_stat_event = (List<Map<String, Object>>) json.get("nac_stat_event");
			List<Map<String, Object>> moca_stat_dip = (List<Map<String, Object>>) json.get("moca_stat_dip");
			List<Map<String, Object>> nac_stat_sip = (List<Map<String, Object>>) json.get("nac_stat_sip");
			List<Map<String, Object>> moca_stat_rule = (List<Map<String, Object>>) json.get("moca_stat_rule");
			List<Map<String, Object>> virus_stat_dip = (List<Map<String, Object>>) json.get("virus_stat_dip");
			List<Map<String, Object>> endpoint_list = (List<Map<String, Object>>) json.get("endpoint_list");
			List<Map<String, Object>> moca_result = (List<Map<String, Object>>) json.get("moca_result");
			List<Map<String, Object>> moca_result_count = (List<Map<String, Object>>) json.get("moca_result_count");
			
			virus_stat_name.stream().forEachOrdered(stat -> {
				stat.put("data", (int) (Math.random() * 10) + 1);
			});
			
			moca_stat_sip.stream().forEachOrdered(stat -> {
				stat.put("data", (int) (Math.random() * 10) + 1);
			});
			
			nac_stat_event.stream().forEachOrdered(stat -> {
				stat.put("data", (int) (Math.random() * 10) + 1);
			});
			
			moca_stat_dip.stream().forEachOrdered(stat -> {
				stat.put("data", (int) (Math.random() * 10) + 1);
			});
			
			nac_stat_sip.stream().forEachOrdered(stat -> {
				stat.put("data", (int) (Math.random() * 10) + 1);
			});
			
			moca_stat_rule.stream().forEachOrdered(stat -> {
				stat.put("data", (int) (Math.random() * 10) + 1);
			});
			
			virus_stat_dip.stream().forEachOrdered(stat -> {
				stat.put("data", (int) (Math.random() * 10) + 1);
			});
			
			endpoint_list.stream().forEachOrdered(endpoint -> {
				endpoint.put("checkTime", new Date(current));
			});
			
			for (int i=0; i<moca_result.size(); i++) {
				moca_result.get(i).put("start", new Date((current / 1000 * 1000)));
				moca_result.get(i).put("end", new Date((current / 1000 * 1000)));
				moca_result.get(i).put("id", (current / 1000) - (i));
			}
			
			for (int i=0; i<moca_result_count.size(); i++) {
				moca_result_count.get(i).put("alert_count", (int) (Math.random() * 20) + 1);
				moca_result_count.get(i).put("create_ts", new Date((current / 1000 * 1000) - (i * 1000)));
			}
			
			result.put("virus_stat_name", virus_stat_name);
			result.put("moca_stat_sip", moca_stat_sip);
			result.put("nac_stat_event", nac_stat_event);
			result.put("moca_stat_dip", moca_stat_dip);
			result.put("nac_stat_sip", nac_stat_sip);
			result.put("moca_stat_rule", moca_stat_rule);
			result.put("virus_stat_dip", virus_stat_dip);
			result.put("endpoint_list", endpoint_list);
			result.put("moca_result", moca_result);
			result.put("moca_result_count", moca_result_count);
			
		} catch (IOException e) {
			throw e;
		} finally {
			/*
			if (result.containsKey("moca_result")) {
				LOG.debug("{}", result.get("moca_result"));
			}
			*/
		}
		
		return result;
	}

}
