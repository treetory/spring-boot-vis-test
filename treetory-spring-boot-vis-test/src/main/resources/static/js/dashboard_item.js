/**
 * 대시보드에 그려져야 할 대상 데이터를 조회하여 보관하는 객체
 */
let DashboardItems = (function() {
	
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
		
		let requestURL = "../dashboard/items";
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function(e) {
			if (xhr.readyState === XMLHttpRequest.DONE) {
		      if (xhr.status === 200) {
		    	  _this.items = JSON.parse(xhr.responseText);
		    	  _this.retrieve_count++;
		      } else {
		    	  console.log('There was a problem with the request.');
		      }
		    }
		};
		xhr.onprogress = function(e) {
			//console.log("onprogress : ");
			//console.log(e);
		};
		xhr.onabort = function(e) {
			console.log(e);
		};
		xhr.ontimeout = function(e) {
			console.log(e);
		};
		xhr.open('GET', requestURL, true);
		xhr.setRequestHeader('Content-Type', "application/json;charset=UTF-8");
		xhr.send();
		
		postMessage(_this);
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


self.onmessage = function (e) {
	/*
	console.log('수신 메시지 : ', e.data);
	
	setTimeout(function() {
		postMessage('전송 결과 메시지');
	}, 1000);
	*/
	// 대시보드 아이템 조회 담당 객체 생성
	let dashboard_items = new DashboardItems();
	dashboard_items.setInterval(dashboard_items, 3000);
	
	postMessage(dashboard_items);
}