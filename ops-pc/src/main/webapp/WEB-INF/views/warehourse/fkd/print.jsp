<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单据打印模板</title>
</head>
<body>
<div id="warp-div">

<table cellpadding="0" cellspacing="0" width="100%">
<thead>
<tr>
<td id="org-name-area" colspan="100" style="position: relative;">
<div id="theBillStat" style="font-size: 18px;position: absolute;right: 0px;">未审核</div>
<h1>${orgName}</h1>
<h2>
<c:choose>
<c:when test="${code eq 'skd'}">收款单</c:when>
<c:when test="${code eq 'fkd'}">付款单</c:when>
<c:when test="${code eq 'fyd'}">费用单</c:when>
</c:choose>
</h2>
</td></tr>
<tr><td colspan="100"><table id="text-area"></table></td></tr>
<tr id="table-th"></tr>
</thead>
<tbody id="table-tb"></tbody>
<tfoot id="table-tfoot">
<tr><td colspan="100">&nbsp;</td></tr>
<tr>
<td>制单人</td><td  id="zdr"></td>
<td>审核人</td><td id="shr"></td>
<td>签名</td><td id="qm"></td>
</tr>
<tr><td></td><td style="text-align: right;"><span>合计：</span></td><td id="totalAmount" style="text-align: left;"></td></tr>
<tr><td colspan="100">&nbsp;</td></tr>
<tr><td colspan="100" style="padding: 0px;margin: 0px;"><span tdata="PageNO">第#页</span>
&nbsp;&nbsp;/&nbsp;&nbsp;
<span tdata="PageCount">共#页</span></td></tr>
</tfoot>
</table>

<style>
#warp-div{
	position: relative;
}
#theBillStat1{
	position: absolute;
	top: 5px;
	right: 5px;
	font-size: 18px;
}
#warp-div table{
	width: 100%;
	font-size: 15px;
}
#warp-div td,#table-th th,#table-tb td,#table-tfoot td,#table-sum td{
	padding: 3px;
	text-align: center;
}
#table-th th{
	border: 1px solid #000000;
	border-right: none;
	font-size: 15px;
}
#table-th th.last-th,#table-tb td.last-th{
	border-right:1px solid #000000;
}
#table-tb td{
	border: 1px solid #000000;
	border-right: none;
	border-top: none;
	font-size: 15px;
}
#table-tb td div{
	height: 17px;
}
#org-name-area h1{
	text-align: center;
	font-size: 24px;
	padding: 0px;
	margin: 0px;
}
#org-name-area h2{
	text-align: center;
	font-size: 20px;
	font-weight: normal;
	padding: 0px;
	margin: 0px;
}
#text-area td{
	font-size: 15px;
}
#text-area td.title{
	width:100px; 
}
#text-area td.val{
	width:180px; 
}
#table-tfoot{
	font-size: 15px;
}
#table-sum{
	border-left: 1px solid #000000;
	border-right: 1px solid #000000;
}
#table-sum td{	
	border-bottom: 1px solid #000000;
	text-align: left;
}
</style>

<script type="text/javascript">
var vals = ${empty data?"''":data};
var totalAmount=0;
for(var i=0;i<vals.length;i++){
	if(vals[i].amount){
		totalAmount=totalAmount+parseFloat(vals[i].amount);
	}
}
$("#totalAmount").text(totalAmount.toFixed(2));
var nullRow = ${empty param.nullRow?0:param.nullRow};
if("undefined" == typeof texts||vals.length==0){
	$.fool.alert({time:1000,msg:'必要参数不存在'});
}else if(texts.length == 1){
	var _str = "",_align='left',_width = 100/3;
	//文本区域
	_str = "<tr><td style='text-align:"+_align+";width:"+_width+"%;' colspan='"+texts[0].colspan+"'>单据日期：<span class='val'>"+getDateStr(vals[0]["billDate"])+"</span></td>";
	
	/* for (var i = 1; i < texts.length; i++) {
		if((i!=0&&i%3==0)||texts[i].br)_str+="</tr><tr>";
		_align = texts[i].align == undefined ? 'left' : texts[i].align;
		_width = texts[i].colspan == undefined ? _width : _width*texts[i].colspan;
		_val = (texts[i].key.length==0) ? "&nbsp;&nbsp;" : vals[texts[i].key];
		_str+="<td style='text-align:"+_align+";width:"+_width+"%;' colspan='"+texts[i].colspan+"'>"+texts[i].title+"：<span class='val'>"+_val+"</span></td>";
	} */
	$("#text-area").html(_str+"</tr>");
	
	//表格区域
	_str = "";
	var _tlen = thead.length;
	_width=100/_tlen;
	var _headKey = {};
	//表头
	for(var i=0;i<thead.length;i++){
		_width = thead[i].width == undefined ? 'auto' : thead[i].width;
		if(i+1==_tlen){
			_str += "<th width='"+_width+"%' class='last-th'>"+thead[i].title+"</th>";
		}else
			_str += "<th width='"+_width+"%'>"+thead[i].title+"</th>";
		_headKey[thead[i].key] = (i+1); 
	}
	$("#table-th").html(_str);
	
	var _dt = vals;
	if(_dt){
		_str = "",_align="center";
		//_dt = (new Function("return "+_dt))();
		for(var i=0;i<_dt.length+nullRow;i++){
			_str += "<tr>";
			for(var j=0;j<thead.length;j++){
				_align = thead[j].textAlign == undefined ? "center" : thead[j].textAlign;
				if(j+1==_tlen)
					_str += "<td width='"+_width+"%' style='text-align:"+_align+";height:17px !important;' class='last-th'>"+tranKey(_dt[i],thead[j].key)+"</td>";
				else
					_str += "<td width='"+_width+"%' style='text-align:"+_align+";height:17px !important;'padding:>"+tranKey(_dt[i],thead[j].key)+"</td>";
			}
			_str += "</tr>";
		}
		
		/* for(var i=0;i<7;i++){
			_str+=_str;
		} */ 
		
		$("#table-tb").html(_str);
	}
	$("#zdr").prev().hide();
	$("#shr").prev().hide();
	$("#qm").prev().hide();
	$("#theBillStat").hide();
}else{
	var _str = "";
	//文本区域
	_str = "<tr>";
	for (var i = 0; i < texts.length; i++) {
		if((i!=0&&i%3==0)||texts[i].br)_str+="</tr><tr>";
		_str+="<td class='title'>"+texts[i].title+"</td><td class='val'>"+vals[0][texts[i].key]+"</td>";	
	}
	$("#text-area").html(_str+"</tr>");
	
	$("#zdr").html(vals[0].creatorName);
	$("#shr").html(vals[0].auditorName);
	$("#theBillStat").html(vals.recordStatus==0?'未审核':(vals.recordStatus==2?'已作废':''));
}

function tranKey(obj,key){
	var _keys = null,_vals='';
	if("undefined" != typeof obj&&"undefined" != typeof key){
		if(key.indexOf(",")!=-1)_keys = key.split(",");
		if(_keys!=null){
			for(var n in _keys)
				_vals += obj[_keys[n]];
		}else
			_vals = obj[key];
		return _vals;
	}
	return "<div></div>";
		
}
</script>
</div>
</body>
</html>