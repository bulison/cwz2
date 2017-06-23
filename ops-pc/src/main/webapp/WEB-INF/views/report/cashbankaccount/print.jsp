<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div id="header">
<div style="display:block; width:100%;text-align:center;hyphenate:auto;font-family:Calibri;font-size:20pt;">
${orgName}
</div>
<div style="height: 2pt;"></div>
<div id="theBillName" style="width: 100%;text-align:center;hyphenate:auto;font-family:Calibri;font-size:14pt;">

</div>
</div>

<div id="print-area1">
<div id="area1-div"></div>
<div id="area1-table">
<table style="width: 100%;margin-top: 10px;" border="1" cellpadding="0" cellspacing="0">
<thead><tr id="tb-th-tr"></tr></thead>
<tbody id="tb-tb"></tbody>
</table>
</div>
<style>
#area1-table table td{
	text-align: center;
}
#tb-th-tr td{
	font-weight: bold;
}
.t1,.v1{
	margin: 5px 8px;
	display: inline-block;
	font-weight: bold;
}
.v1{
	font-weight:normal;
	margin-left:0px;
	margin-right:10px;
}
</style>
<script type="text/javascript">
	var vals = ${empty data?"''":data};
	try{
		if(!texts||!thead||!vals){
			$.fool.alert({msg:'必要参数不存在'});
		}
	}catch(e){
		$.fool.alert({msg:'必要参数不存在'});
		(!texts||!thead||!vals);
	}
	var titles = texts;
	var tables = thead;
	//var vals = vlues;
	var _str = "";
	//文本区域
	for (var i = 0; i < titles.length; i++) {
		if((i!=0&&i%3==0)||titles[i].br)_str+="<Br/>";
		_str +="<span class='t1'>"+titles[i].title+":</span><span class='v1'>"+vals[titles[i].key]+"</span>";	
	}
	//document.getElementById("area1-div").innerHTML=_str;
	$("#area1-div").html(_str);

	//表格区域
	_str = "";
	//表头
	for(var i=0;i<tables.length;i++){
		_str += "<td>"+tables[i].title+"</td>";
	}
	//document.getElementById("tb-th-tr").innerHTML=_str;
	$("#tb-th-tr").html(_str);
	//表体
	var _dt = vals.details;
	if(_dt){
		_str = "";
		_dt = (new Function("return "+_dt))();
		for(var i=0;i<_dt.length;i++){
			_str += "<tr>";
			for(var j=0;j<tables.length;j++){
				_str += "<td>"+_dt[i][tables[j].key]+"</td>";
			}
			_str += "</tr>";
		}
		
		//document.getElementById("tb-tb").innerHTML=_str;
		/*for(var i=0;i<6;i++){
			_str+=_str;
		}*/
		
		$("#tb-tb").html(_str);
	}

</script>
</div>
</body>
</html>
