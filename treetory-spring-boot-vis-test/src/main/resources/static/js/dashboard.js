var timeline_container = /*$('#timeline_visualization')*/document.getElementById('visualization');

var MocaResult = (function() {
	
	function MocaResult() {
		this.data = new Array();
		this.timeline_items = new vis.DataSet(this.data);
		this.timeline_options = {};
		this.timeline = new vis.Timeline(timeline_container, this.timeline_items, this.timeline_options);
	}
	
	MocaResult.prototype.retrieve = function(_this) {
		$.ajax({
			url : "/result/list",
			async : false,
			method : "get",
			dataType : "json",
			contentType : "application/json;charset=UTF-8",
			success : function(result) {
				for (var i=0; i<result.length; i++) {
					_this.data.push(result[i]);
				}
				_this.setData(_this);
			},
			timeout : 100000,
			complete : function(jqXHR, textStatus) {
				console.log(jqXHR);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR);
			}
		});
	}
	
	MocaResult.prototype.setData = function(_this) {
		_this.timeline_items = new vis.DataSet(_this.getData());
		_this.timeline = new vis.Timeline(timeline_container, _this.timeline_items, _this.timeline_options);
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
		if (cnt == 5) {
			clearInterval(aaa);
		}
	}, 5000);
	console.log(aaa);
	//mr.data.push(mr.retrieve());
	
});