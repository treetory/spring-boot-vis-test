
var MocaResult = (function() {
	
	function MocaResult() {
		this.timeline_container = document.getElementById('visualization');
		this.data = new Array();
		this.timeline_items = new vis.DataSet(this.data);
		this.timeline_options = 
		{
			rtl : true,
			showCurrentTime: true
		};
		this.timeline = null;
	}
	
	MocaResult.prototype.retrieve = function(_this) {
		$.ajax({
			url : "/result/list",
			async : false,
			method : "get",
			dataType : "json",
			contentType : "application/json;charset=UTF-8",
			success : function(result) {
				if (result instanceof Array) {
					if (result.length > 0) {
						_this.setData(_this, result);
					}
				}
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
	
	MocaResult.prototype.setData = function(_this, result) {
		
		for (var i=0; i<result.length; i++) {
			_this.data.push(result[i]);
			_this.timeline_items.add(result[i]);
		}
		if (_this.timeline == null) {
			_this.timeline = new vis.Timeline(_this.timeline_container, _this.timeline_items, _this.timeline_options);
		} else {
			//console.log(_this.timeline);
			_this.timeline.fit();
			_this.timeline.moveTo(result[0].create_ts);
			//_this.timeline.focus(_this.data.length - 10);
		}
		
	}
	
	MocaResult.prototype.getData = function() {
		return this.data;
	}
	
	return MocaResult;
	
}()); 

$(document).ready(function() {
	
	//console.log(document.getElementById('visualization'));
	
	let mr = new MocaResult();
	//var aaa = mr.setInterval(mr, 5000);
	var cnt = 0;
	var aaa = setInterval(function() {
		cnt++;
		mr.retrieve(mr);
		if (cnt == 100) {
			clearInterval(aaa);
		}
	}, 2000);
	console.log(aaa);
	//mr.data.push(mr.retrieve());
	
});