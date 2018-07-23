var DashboardItems = (function() {
	
	/* 생성자 */
	function DashboardItems() {
		this.items = new Object();
		this.interval = null;
		this.retrieve_count = 0;
	}
	
	/* 메소드 1 : 조회 메소드 
	 * 		    -> ajax 요청의 결과값을 받아서 데이터를 추가한다.
	 */
	DashboardItems.prototype.retrieve = function(_this) {
		$.ajax({
			url : "/dashboard/items",
			async : false,
			method : "get",
			dataType : "json",
			contentType : "application/json;charset=UTF-8",
			success : function(result) {
				if (result != null || result != undefined) {
					_this.items = result;
					_this.retrieve_count++;
				}
				//console.log(_this.items);
			},
			timeout : 100000,
			complete : function(jqXHR, textStatus) {
				//console.log(jqXHR);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR);
			}
		});
	}
	
	/*
	 * 메소드 2 : 해당 key 로 차트를 그리기 위한 데이터 가져가기
	 */
	DashboardItems.prototype.getItem = function(_this, key) {
		return _this.items[key];
	}
	
	/*
	 * 메소드 3 : 설정한 시간 간격에 따라 조회 스케줄 등록
	 */
	DashboardItems.prototype.setInterval = function(_this, _interval) {
		if (_this.interval == null) {
			_this.interval = setInterval(function() {
				_this.retrieve(_this);
			}, _interval);
		} else {
			console.log("Already setted the interval.")
		}
	}
	
	/*
	 * 메소드 4 : 등록된 조회 스케줄 취소
	 */
	DashboardItems.prototype.clearInterval = function(_this) {
		if (_this.interval != null) {
			clearInterval(_this.interval);
			_this.retrieve_count = 0;
		}
		_this.interval = null;
	}
	
	return DashboardItems;
	
}());


/**
 * 타임라인 그래프를 moca_result 정보와 연동하여 테스트해 봄
 */
var MocaResultTimeline = (function() {
	
	/* 생성자 */
	function MocaResultTimeline() {
		this.dashboard_items = null;
		this.timeline_container = document.getElementById('visualization_timeline');
		this.data = new Array();
		this.timeline_items = new vis.DataSet(this.data);
		this.timeline_options = 
		{
			rtl : true,
			showCurrentTime: true
		};
		this.timeline = new vis.Timeline(this.timeline_container, this.timeline_items, this.timeline_options);
	}
	
	/* 메소드 1 : 조회 메소드 
	 * 		    -> ajax 요청의 결과값을 받아서 데이터를 추가한다.
	 */
	MocaResultTimeline.prototype.retrieve = function(_this) {
		_this.setData(_this, _this.dashboard_items.items.moca_result);
	}
	
	/*
	 * 메소드 2 : 요청 결과 데이터를 받아서 timeline 에 추가한다.
	 */
	MocaResultTimeline.prototype.setData = function(_this, result) {
		
		for (var i=0; i<result.length; i++) {
			_this.data.push(result[i]);
			_this.timeline_items.add(result[i]);
		}
		
		_this.setEffect(_this);
	}
	
	/*
	 * 메소드 3 : timeline 의 그래픽 이펙트를 처리한다.
	 */
	MocaResultTimeline.prototype.setEffect = function(_this) {
		
		let _data = _this.getData();
		
		_this.timeline.focus(_data[_data.length-1].id, {animation: {duration: 1000, easingFunction: 'easeInOutQuad'}});
		_this.timeline.fit();
		
	}
	
	/*
	 * 메소드 4 : timeline 에 저장된 데이터를 가져온다.
	 */
	MocaResultTimeline.prototype.getData = function() {
		return this.data;
	}
	
	/*
	 * 메소드 5 : 설정한 시간 간격에 따라 조회 스케줄 등록
	 */
	MocaResultTimeline.prototype.setInterval = function(_this, _interval, _dashboard_items) {
		if (_this.interval == null) {
			_this.interval = setInterval(function() {
				_this.retrieve(_this);
				_this.retrieve_count++;
			}, _interval);
			_this.dashboard_items = _dashboard_items;
		} else {
			console.log("Already setted the interval.");
		}
	}
	
	/*
	 * 메소드 6 : 등록된 조회 스케줄 취소
	 */
	MocaResultTimeline.prototype.clearInterval = function(_this) {
		if (_this.interval != null) {
			clearInterval(_this.interval);
			_this.retrieve_count = 0;
		}
		_this.interval = null;
	}
	
	return MocaResultTimeline;
	
}()); 

/**
 * 타임라인 그래프를 moca_result 정보와 연동하여 테스트해 봄
 * @returns
 */
var MocaResultStreamingDataGraph = (function() {
	
	/* 생성자 */
	function MocaResultStreamingDataGraph(mode) {
		this.interval = null;
		this.retrieve_count = 0;
		this.mode = mode;
		this.dashboard_items = null;
		this.container = document.getElementById('visualization_streaming');
		this.dataset = new vis.DataSet();
		this.options = 
		{
			start: vis.moment().add(-5, 'minutes'), // changed so its faster
		    end: vis.moment(),
		    dataAxis: {
		    	left: {
		    		range: {
		    			min: 0, max: 10
		    		}
		    	}
		    },
		    drawPoints: {
		    	style: 'circle' // square, circle
		    },
		    shaded: {
		    	orientation: 'bottom' // top, bottom
		    }
		};
		this.graph2d = new vis.Graph2d(this.container, this.dataset, this.options);
	}
	
	/*
	 * 메소드 1 : 시계열 추이를 그리기 위한 실시간 집계 데이터 조회
	 */
	MocaResultStreamingDataGraph.prototype.retrieve = function(_this) {
		_this.setData(_this, _this.dashboard_items.items.moca_result_count);
	}
	
	/*
	 * 메소드 2 : 요청 결과 데이터를 받아서 timeline 에 추가한다.
	 */
	MocaResultStreamingDataGraph.prototype.setData = function(_this, result) {
		//console.log(vis);
		_this.dataset.add(
		{
			x: vis.moment(result.create_ts),
			y: result.alert_count
		});
		// remove all data points which are no longer visible
	    var range = _this.graph2d.getWindow();
	    var interval = range.end - range.start;
		var oldIds = _this.dataset.getIds(
		{
			filter: function (item) {
				return item.x < range.start - interval;
		    }
		});
		_this.dataset.remove(oldIds);
		
		_this.setRender(_this, result);
	}
	
	/*
	 * 메소드 3 : 스트리밍 데이터 그래프의 렌더링을 처리한다.
	 */
	MocaResultStreamingDataGraph.prototype.setRender = function(_this, result) {
		
		let now = vis.moment(result.create_ts);
		let range = _this.graph2d.getWindow();
		let interval = range.end - range.start;
		
		switch (_this.mode) {
		  case 'discrete':
	    	  _this.graph2d.setWindow(now - interval, now, {animation: false});
	        break;

	      default: // 'static'
	        // move the window 90% to the left when now is larger than the end of the window
	        if (now > range.end) {
	        	_this.graph2d.setWindow(now - 0.1 * interval, now + 0.9 * interval);
	        }
	        break;
		}
	}
	
	/*
	 * 메소드 4 : 설정한 시간 간격에 따라 조회 스케줄 등록
	 */
	MocaResultStreamingDataGraph.prototype.setInterval = function(_this, _interval, _dashboard_items) {
		if (_this.interval == null) {
			_this.interval = setInterval(function() {
				_this.retrieve(_this);
				_this.retrieve_count++;
			}, _interval);
			_this.dashboard_items = _dashboard_items;
		} else {
			console.log("Already setted the interval.");
		}
		
	}
	
	/*
	 * 메소드 5 : 등록된 조회 스케줄 취소
	 */
	MocaResultStreamingDataGraph.prototype.clearInterval = function(_this) {
		if (_this.interval != null) {
			clearInterval(_this.interval);
			_this.retrieve_count = 0;
		}
		_this.interval = null;
	}
	
	return MocaResultStreamingDataGraph;
	
}());

$(document).ready(function() {
	
	// 대시보드 아이템 조회 담당 객체 생성
	let dashboard_items = new DashboardItems();
	// 타임라인 객체 생성
	let mr_timeline = new MocaResultTimeline();
	// 스트리밍 데이터 그래프 객체 생성
	let mr_streaming = new MocaResultStreamingDataGraph("discrete");
	
	// 시작 이벤트
	$('#start').click(function(e){
		dashboard_items.setInterval(dashboard_items, 3000);
		mr_timeline.setInterval(mr_timeline, 3000, dashboard_items);
		mr_streaming.setInterval(mr_streaming, 3000, dashboard_items);
		console.log("count : "+dashboard_items.retrieve_count+" / "+"set the interval id ["+dashboard_items.interval+"].");
	});
	
	// 정지 이벤트
	$('#stop').click(function(e){
		console.log("count : "+dashboard_items.retrieve_count+" / "+"clear the interval id ["+dashboard_items.interval+"].");
		dashboard_items.clearInterval(dashboard_items);
		mr_timeline.clearInterval(mr_timeline);
		mr_streaming.clearInterval(mr_streaming);
	});
	
});