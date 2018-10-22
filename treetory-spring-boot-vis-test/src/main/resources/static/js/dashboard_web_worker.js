let worker = new Array();
let dashboard_items = null;
let isReady = false;

let mr_timeline = null;
let mr_streaming = null;
let mr_piechart1 = null;
let mr_piechart2 = null;
let mr_piechart3 = null;
let mr_piechart4 = null;
let mr_piechart5 = null;
let mr_piechart6 = null;
let mr_piechart7 = null;

let chart_index = 1;

/**
 * vis.js 의 타임라인 그래프에 데이터 동적 바인딩 시킴
 */
let MocaResultTimeline = (function() {
	
	/* 생성자 */
	function MocaResultTimeline(id, items_key) {
		this.dashboard_items = null;
		this.items_key = items_key;														// {group:"aaa", data:"bbb"} 형태로 key 를 받는다.
		this.timeline_container = document.getElementById(id);
		this.timeline_groups = new vis.DataSet();
		this.timeline_items = new vis.DataSet();
		this.timeline_options = 
		{
			//rtl : true,
			showCurrentTime: true,
			groupOrder: 'content',
			stack: false,
			type: 'point',	// 'box', 'point', 'range'
		    verticalScroll: true,
		    zoomable: false,
		    //zoomKey: 'ctrlKey',
		    minHeight: 200,
		    maxHeight: 353,
		    height: 353,
		    start: vis.moment().add(-60, 'seconds'),
		    end: vis.moment().add(60, 'seconds'),
		};
		this.timeline = new vis.Timeline(this.timeline_container, this.timeline_items, this.timeline_options);
	}
	
	/* 메소드 1 : 조회 메소드 
	 * 		    -> ajax 요청의 결과값을 받아서 데이터를 추가한다.
	 */
	MocaResultTimeline.prototype.retrieve = function(_this, _dashboard_items) {
		_this.dashboard_items = _dashboard_items;
		// 데이터 컨테이너에 데이터가 있을 때만, 처리한다.
		if (_this.dashboard_items.items != null) {
			// 각 데이터의 계열을 그루핑하기 위한 그룹데이터를 생성한다.
			_this.setGroups(_this, _this.dashboard_items.items[_this.items_key.group]);
			// 렌더링 하기 위한 대상 데이터를 추가한다.
			_this.setData(_this, _this.dashboard_items.items[_this.items_key.data]);
		}
	}
	
	MocaResultTimeline.prototype.setGroups = function(_this, groups) {
		// TO-DO 엔드포인트가 추가되었을 때, 그룹에 add 되도록 처리하는 코드 필요
		if (_this.timeline_groups.length == 0) {
			for (let i=0; i<groups.length; i++) {
				_this.timeline_groups.add(groups[i]);
			}
			_this.timeline.setGroups(_this.timeline_groups);
		} 
	}
	
	/*
	 * 메소드 2 : 요청 결과 데이터를 받아서 timeline 에 추가한다.
	 */
	MocaResultTimeline.prototype.setData = function(_this, result) {
		
		let keys = [];
		
		// add retrieved data to DataSet(timeline_items)
		for (let i=0; i<result.length; i++) {
			keys = Object.keys(_this.timeline_items._data);
			if (keys.indexOf(result[i].id) == -1) {
				_this.timeline_items.add(result[i]);
			}
		}
		
		_this.setEffect(_this);
		
		// remove all data points which are no longer visible
	    var range = _this.timeline.getWindow();
	    var interval = range.end - range.start;
		
	    var oldIds = _this.timeline_items.getIds(
		{
			filter: function (item) {
				// interval = 10 초 * 10 이므로 100초 간의 데이터만 타임라인안에 남겨놓는다.
				return (new Date(item.end + 10 * interval) < range.start);
		    }
		});
		_this.timeline_items.remove(oldIds);
		
	}
	
	/*
	 * 메소드 3 : timeline 의 그래픽 이펙트를 처리한다.
	 */
	MocaResultTimeline.prototype.setEffect = function(_this) {
		// 타임라인의 중앙을 지금 시간으로부터 4초전의 시간을 위치시킨다.(더 많은 데이터를 화면에 보여주기 위함)
		_this.timeline.moveTo(vis.moment().add(-4, 'seconds'));
	}
	
	/*
	 * 메소드 4 : timeline 에 저장된 데이터를 가져온다.
	 */
	MocaResultTimeline.prototype.getDataSet = function() {
		return this.timeline_items;
	}
	
	/*
	 * 메소드 5 : 설정한 시간 간격에 따라 조회 스케줄 등록
	
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
	 */
	/*
	 * 메소드 6 : 등록된 조회 스케줄 취소
	
	MocaResultTimeline.prototype.clearInterval = function(_this) {
		if (_this.interval != null) {
			clearInterval(_this.interval);
			_this.retrieve_count = 0;
		}
		_this.interval = null;
	}
	 */
	return MocaResultTimeline;
	
}()); 

/**
 * vis.js 의 2D graph 중 Streaming Data 스타일 그래프에 데이터를 동적 바인딩 시킴
 */
let MocaResultStreamingDataGraph = (function() {
	
	/* 생성자 */
	function MocaResultStreamingDataGraph(id, mode, items_key) {
		this.interval = null;
		this.retrieve_count = 0;
		this.mode = mode;
		this.dashboard_items = null;
		this.items_key = items_key;
		this.container = document.getElementById(id);
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
		    	size: 2,
		    	style: 'circle' // square, circle
		    },
		    shaded: {
		    	orientation: 'bottom' // top, bottom
		    },
		    zoomable: false,
		    graphHeight: 300
		};
		this.graph2d = new vis.Graph2d(this.container, this.dataset, this.options);
	}
	
	/*
	 * 메소드 1 : 시계열 추이를 그리기 위한 실시간 집계 데이터 조회
	 */
	MocaResultStreamingDataGraph.prototype.retrieve = function(_this, _dashboard_items) {
		_this.dashboard_items = _dashboard_items;
		// 데이터를 담고 있는 컨테이너(주기적 ajax 호출로 데이터를 보관함)에서 시계열 추이를 그리기 위한 데이터를 꺼냄
		if (_this.dashboard_items.items != null) {
			_this.setData(_this, _this.dashboard_items.items[_this.items_key]);
		}
	}
	
	/*
	 * 메소드 2 : 요청 결과 데이터를 받아서 timeline 에 추가한다.
	 */
	MocaResultStreamingDataGraph.prototype.setData = function(_this, result) {
		//console.log(result);
		// array 타입일 때, dataset 추가
		if (result instanceof Array) {
			for (let i=0; i<result.length; i++) {
				let target = 
				{
					x: vis.moment(result[i].create_ts),
					y: result[i].alert_count
				};
				_this.dataset.add(target);
			}
		}
		
		// remove all data points which are no longer visible : 10초를 넘어서는 영역의 것은 제거
	    let range = _this.graph2d.getWindow();
	    let interval = range.end - range.start;
		let oldIds = _this.dataset.getIds(
		{
			filter: function (item) {
				
				if (_this.options.dataAxis.left.range.max <= item.y) {
					_this.options.dataAxis.left.range.max = (item.y + 10);
					_this.graph2d.setOptions(_this.options);
				}
				
				return (new Date(item.x) < range.start);
		    }
		});
		_this.dataset.remove(oldIds);
		
		// 렌더링 처리
		_this.setEffect(_this, result);
		
	}
	
	/*
	 * 메소드 3 : 스트리밍 데이터 그래프의 렌더링 이펙트를 처리한다.
	 */
	MocaResultStreamingDataGraph.prototype.setEffect = function(_this, result) {
		
		// 현재시각, 현재 윈도우에서 표시된 범위, 그리고 간격을 구한다.
		let now = vis.moment(result.create_ts);
		let range = _this.graph2d.getWindow();
		let interval = range.end - range.start;
		
		// 각 모드별로 렌더링 이펙트를 달리한다.
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
	
	MocaResultStreamingDataGraph.prototype.setInterval = function(_this, _interval, _dashboard_items) {
		// 조회 스케줄이 등록된 것이 없을 때만 등록한다 -> 중복 등록으로 인하여 이벤트 꼬임 방지
		if (_this.interval == null) {
			// 인터벌 등록된 객체는 내부에 보관하여, 나중에 인터벌 취소를 할 때 사용한다.
			_this.interval = setInterval(function() {
				_this.retrieve(_this);
				_this.retrieve_count++;
			}, _interval);
			_this.dashboard_items = _dashboard_items;
		} else {
			console.log("Already setted the interval.");
		}
		
	}
	 */
	/*
	 * 메소드 5 : 등록된 조회 스케줄 취소
	
	MocaResultStreamingDataGraph.prototype.clearInterval = function(_this) {
		// 인터벌 등록된 것이 있을 때만 취소한다.
		if (_this.interval != null) {
			clearInterval(_this.interval);
			_this.retrieve_count = 0;
		}
		_this.interval = null;
	}
	 */
	return MocaResultStreamingDataGraph;
	
}());

/**
 * TOAST 의 PieChart 에 데이터를 동적 바인딩 시킴
 */
let MocaResultPieChart = (function() {
	
	/* 생성자 */
	function MocaResultPieChart(id, items_key, options) {
		this.interval = null;
		this.retrieve_count = 0;
		this.dashboard_items = null;
		this.items_key = items_key;
		this.container = document.getElementById(id);
		this.data = {
				categories: [],
				series: [
					{
			            name: 'Legend1',
			            data: 10
			        }
		        ]
		};
		
		this.options = options;
		this.pieChart = tui.chart.pieChart(this.container, this.data, this.options);
	}
	
	/*
	 * 메소드 1 : 실시간 집계 데이터 조회
	 */
	MocaResultPieChart.prototype.retrieve = function(_this, _dashboard_items) {
		
		_this.dashboard_items = _dashboard_items;
		
		if (_this.dashboard_items.items != null) {

			_this.data.series = _this.dashboard_items.items[_this.items_key];
			
			if (_this.pieChart != null) {
				_this.pieChar = null;
				while ( _this.container.hasChildNodes() ) { 
					_this.container.removeChild( _this.container.firstChild ); 
				}
			}
			
			_this.pieChart = tui.chart.pieChart(_this.container, _this.data, _this.options);
			
		}
		
	}
	
	/*
	 * 메소드 4 : 설정한 시간 간격에 따라 조회 스케줄 등록
	
	MocaResultPieChart.prototype.setInterval = function(_this, _interval, _dashboard_items) {
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
	 */
	/*
	 * 메소드 5 : 등록된 조회 스케줄 취소
	
	MocaResultPieChart.prototype.clearInterval = function(_this) {
		if (_this.interval != null) {
			clearInterval(_this.interval);
			_this.retrieve_count = 0;
		}
		_this.interval = null;
	}
	 */
	return MocaResultPieChart;
	
}());

function renderringChart() {
	
	if (dashboard_items.items != undefined) {
		
		if ((dashboard_items.items).hasOwnProperty("moca_result")) {
			mr_timeline.retrieve(mr_timeline, dashboard_items);
			mr_streaming.retrieve(mr_streaming, dashboard_items);
			
			switch (chart_index) {
			case 1 :
				mr_piechart1.retrieve(mr_piechart1, dashboard_items);
				break;
			case 2 :
				mr_piechart2.retrieve(mr_piechart2, dashboard_items);
				break;
			case 3 :
				mr_piechart3.retrieve(mr_piechart3, dashboard_items);
				break;
			case 4 :
				mr_piechart4.retrieve(mr_piechart4, dashboard_items);
				break;
			case 5 :
				mr_piechart5.retrieve(mr_piechart5, dashboard_items);
				break;
			case 6 :
				mr_piechart6.retrieve(mr_piechart6, dashboard_items);
				break;
			case 7 :
				mr_piechart7.retrieve(mr_piechart7, dashboard_items);
				break;
			}
			
			if (chart_index < 7) {
				chart_index++;
				
			} else if (chart_index == 7) {
				chart_index = 1;
			}
			
		}
		
	}
}
/**
 * web worker 사용시 주의할 점
 * 
 * 1. worker 생성 시에 태울 js 내에선 dom access 는 지원되지 않는다. 그래서 dom 에 access 하는 chart rendering 기능은 별도의 js 로 작성하여 worker 를 태우지 못했다.
 * 2. worker object 내부에서 this 를 사용할 수 없다. (this 또한 dom 인 듯 하다.) 해당 객체를 찾을 땐, 직접 access 해야 한다.
 * 3. jquery 를 worker 에서 실행할 js 에 import 시킬 방법이 없다. jquery 또한 dom access 를 하기 떄문에 어차피 사용 못한다. -> javascript 에서 지원되는 function 으로 대체한다. -> XMLHttpRequest 사용했다.
 * 
 */
$(document).ready(function() {
	
	// 시작 이벤트
	$('#start').click(function(e){
		
		if (worker.length > 0) {
			for (let i=0; i<worker.length; i++) {
				worker[i].terminate();
			}
			worker = new Array();
		}
		
		worker[0] = new Worker('./js/dashboard_item.js');
		worker[0].postMessage( { name : '테스트' } );
		worker[0].onmessage = function (e) {
			
			dashboard_items = e.data;
			
			if (!isReady) {
				
				mr_timeline = new MocaResultTimeline("visualization_timeline", {group:"endpoint_list", data:"moca_result"});
				// 스트리밍 데이터 그래프 객체 생성
				mr_streaming = new MocaResultStreamingDataGraph("visualization_streaming", "discrete", "moca_result_count");
				// 파이차트
				mr_piechart1 = new MocaResultPieChart("visualization_pie1", "virus_stat_name",
						{
							chart:
							{
								width: 400,
								height: 320,
								title: "바이러스별 감염통계(당월)"
							},
							series: 
							{
								showLegend: false, 
								labelAlign:'outer', 
								startAngle: -90, 
								endAngle: 90, 
								radiusRange: ['70%', '100%']
							}, 
							legend:
							{
								visible:true
							}
						});
				
				mr_piechart2 = new MocaResultPieChart("visualization_pie2", "virus_stat_dip",
						{
							chart:
							{
								width: 400,
								height: 320,
								title: "IP별 바이러스 감염횟수(당월)"
							},
							series: 
							{
								showLegend: false, 
								labelAlign:'outer', 
								startAngle: -90, 
								endAngle: 90, 
								radiusRange: ['70%', '100%']
							}, 
							legend:
							{
								visible:true
							}
						});
				
				mr_piechart3 = new MocaResultPieChart("visualization_pie3", "nac_stat_event",
						{
							chart:
							{
								width: 400,
								height: 320,
								title: "NAC 이벤트별 통계(당월)"
							},
							series: 
							{
								showLegend: false, 
								labelAlign:'outer', 
								//startAngle: -90, 
								//endAngle: 90, 
								radiusRange: ['70%', '100%']
							}, 
							legend:
							{
								visible:true
							}
						});
				
				mr_piechart4 = new MocaResultPieChart("visualization_pie4", "nac_stat_sip",
						{
							chart:
							{
								width: 400,
								height: 320,
								title: "NAC IP별 이벤트 발생횟수(당월)"
							},
							series: 
							{
								showLegend: false, 
								labelAlign:'outer', 
								//startAngle: -90, 
								//endAngle: 90, 
								radiusRange: ['70%', '100%']
							}, 
							legend:
							{
								visible:true
							}
						});
				
				mr_piechart5 = new MocaResultPieChart("visualization_pie5", "moca_stat_rule",
						{
							chart:
							{
								width: 400,
								height: 320,
								title: "상관규칙 별 발생횟수(당월)"
							},
							series: 
							{
								showLegend: false, 
								labelAlign:'outer', 
								//startAngle: -90, 
								//endAngle: 90, 
								radiusRange: ['70%', '100%']
							}, 
							legend:
							{
								visible:true
							}
						});
				
				mr_piechart6 = new MocaResultPieChart("visualization_pie6", "moca_stat_sip",
						{
							chart:
							{
								width: 400,
								height: 320,
								title: "SIP 별 상관성 이벤트 발생횟수(당월)"
							},
							series: 
							{
								showLegend: false, 
								labelAlign:'outer', 
								//startAngle: -90, 
								//endAngle: 90, 
								radiusRange: ['70%', '100%']
							}, 
							legend:
							{
								visible:true
							}
						});
				
				mr_piechart7 = new MocaResultPieChart("visualization_pie7", "moca_stat_dip",
						{
							chart:
							{
								width: 400,
								height: 320,
								title: "DIP 별 상관성 이벤트 발생횟수(당월)"
							},
							series: 
							{
								showLegend: false, 
								labelAlign:'outer', 
								//startAngle: -90, 
								//endAngle: 90, 
								radiusRange: ['70%', '100%']
							}, 
							legend:
							{
								visible:true
							}
						});
				isReady = true;
			}
			
			if (dashboard_items) {
				renderringChart();
			}
		}
	});
	
	// 정지 이벤트
	$('#stop').click(function(e){
		
		if (worker.length > 0) {
			for (let i=0; i<worker.length; i++) {
				worker[i].terminate();
			}
			worker = new Array();
		}
		console.log(worker.length);
	});
	
});