<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工资打印条打印模板</title>
</head>
<body>
<div id="warp-div">

<table cellpadding="0" cellspacing="0" width="100%" style="margin-top: 20px">
    <thead id="tittles">
        <tr style="height: 20px"></tr>
        <tr>
            <td id="org-name-area" style="position: relative;text-align: center;" width=100% colspan="100">
                <p style="font-size:12px;margin:5px 0 0 0">${org.orgName}</p>
                <h1>工资表汇总</h1>
            </td>
        </tr>
        <tr style="height: 20px"></tr>
        <tr id="table-th">
        </tr>
    </thead>
    <tbody id="table-tb"></tbody>
    <tfoot id="table-tfoot">
        <tr>
            <td colspan="4" style="padding: 0px;">
                <table id="table-sum" cellpadding="0" cellspacing="0" width="100%"></table>
            </td>
        </tr>
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
	font-size: 16px;
}
#warp-div td,#table-th th,#table-tb td,#table-tfoot td,#table-sum td{
	padding: 3px;
	text-align: center;
}
#table-th th{
	border: 1px solid #000000;
	border-right: none;
	font-size: 16px;
}
#table-th th.last-th,#table-tb td.last-th{
	border-right:1px solid #000000;
}
#table-tb td{
	border: 1px solid #000000;
	border-right: none;
	border-top: none;
	font-size: 16px;	
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
	font-size: 16px;
}
#text-area td.title{
	width:100px; 
}
#text-area td.val{
	width:180px; 
}
#table-tfoot{
	font-size: 16px;
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
if("undefined" == typeof texts||"undefined" == typeof thead||"undefined" == typeof vals){
	$.fool.alert({msg:'必要参数不存在'});
}else{
	var _str = "",_val="&nbsp;&nbsp;",_align='left';
	var title="",key="",value="";
	var tittlesArea=$("#tittles").find("p");
	//文本区域
	for(var j=0;j<tittlesArea.length;j++){
		for (var i = 0; i < texts.length; i++) {
			title=texts[i].title;
			key=texts[i].key;
			value=vals[texts[i].key];
			if($(tittlesArea[j]).attr("id")==key){
				if(title!=""){
					$(tittlesArea[j]).text(title+":"+value);
				}else{
					$(tittlesArea[j]).text(value);
				}
			}
		}
	}
	
	//表格区域
	_str = "";
	var _tlen = thead.length;
	var _width = 'auto';//100/_tlen;
	var _headKey = {};
	//表头
	for(var i=0;i<thead.length;i++){
		_width = thead[i].width == undefined ? 'auto' : thead[i].width;
		if(i+1==_tlen){
			_str += "<th width="+_width+"% class='last-th'>"+thead[i].title+"</th>";
		}else
			_str += "<th width="+_width+"% >"+thead[i].title+"</th>";
		_headKey[thead[i].key] = (i+1); 
	}
	$("#table-th").html(_str);
	
	//表体
	var _dt = vals;
	if(_dt){
		_str = "",_align="center";
		/* _dt = (new Function("return "+_dt))(); */
		for(var i=0;i<_dt.length;i++){
			_str += "<tr>";
			for(var j=0;j<thead.length;j++){
				_align = thead[j].textAlign == undefined ? _align : thead[j].textAlign;
				if(j+1==_tlen)
					_str += "<td width='"+_width+"%' style='text-align:"+_align+"' class='last-th'>"+tranKey(_dt[i],thead[j].key)+"</td>";
				else
					_str += "<td width='"+_width+"%' style='text-align:"+_align+"'>"+tranKey(_dt[i],thead[j].key)+"</td>";
			}
			_str += "</tr>";
			_align="center";
		}
		
		/* for(var i=0;i<7;i++){
			_str+=_str;
		} */ 
		
		$("#table-tb").html(_str);
		
		_str = "";
		if("undefined" != typeof tfoot){
			_str = "<tr>";
			var _ind;
			for(var i=0;i<tfoot.length;i++){
				_ind = _headKey[tfoot[i].key];
				if(i!=0&&i%3==0||tfoot[i].br)_str+="</tr><tr>";
				if(tfoot[i].preNull){
					_str += "<td>&nbsp;</td>";
					//continue;
				}
				if(tfoot[i].dtype==0){//当页统计
					_str += "<td tdata='SubCount' tindex='"+_ind+"' style='text-align:"+tfoot[i].align+"' width='"+tfoot[i].width+"%'>"+tfoot[i].text+"</td>";
				}else if(tfoot[i].dtype==1){//统计全部
					_str += "<td tdata='AllCount' tindex='"+_ind+"' style='text-align:"+tfoot[i].align+"' width='"+tfoot[i].width+"%'>"+tfoot[i].text+"</td>";
				}else if(tfoot[i].dtype==2){//当前页金额
					_str += "<td tdata='SubSum' tindex='"+_ind+"' format='0.00' style='text-align:"+tfoot[i].align+"' width='"+tfoot[i].width+"%'>"+tfoot[i].text+"</td>";
				}else if(tfoot[i].dtype==3){//当前页金额大写
					_str += "<td tdata='SubSum' tindex='"+_ind+"' format='UpperMoney' style='text-align:"+tfoot[i].align+"' width='"+tfoot[i].width+"%'>"+tfoot[i].text+"</td>";
				}else if(tfoot[i].dtype==4){//所有记录总金额
					_str += "<td tdata='AllSum' tindex='"+_ind+"' format='0.00' style='text-align:"+tfoot[i].align+"' width='"+tfoot[i].width+"%'>"+tfoot[i].text+"</td>";
				}else if(tfoot[i].dtype==5){//所有记录总金额大写
					_str += "<td tdata='AllSum' tindex='"+_ind+"' format='UpperMoney' style='text-align:"+tfoot[i].align+"' width='"+tfoot[i].width+"%'>"+tfoot[i].text+"</td>";
				}else{
					_str += "<td style='text-align:"+tfoot[i].align+"' width='"+tfoot[i].width+"%'>&nbsp;</td>";
				}
			}
		}
		$("#table-sum").html(_str+"</tr>");
	}
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
		if(_vals){
			return _vals;
		}else{
			return "";
		};
	}
	return "";
		
}
</script>
</div>
</body>
</html>