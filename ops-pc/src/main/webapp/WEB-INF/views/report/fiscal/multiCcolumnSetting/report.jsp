<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>多栏明细账</title>
<style type="text/css">
/* .btn-area{
	margin: 10px 0px;
}
.table-area{
	margin:20px 0px;
	height: 500px;
} */
</style>
</head>
<body>
<!-- <div class="btn-area"></div> -->
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>多栏明细账</h1>
                </div>             
             </div>
<form action="" class="form1" id="search-form">
	<p><font>开始期间：</font><input name="startFiscalPeriodId" id="startTime" class="textBox" type="text"/></p>
	<p><font>结束期间：</font><input name="endFiscalPeriodId" id="endTime" class="textBox" type="text"/></p>
	<br/>
	<p><font>栏目：</font><input name="settingId" id="column" value="" data-options="{required:false,novalidate:true}" class="textBox" type="text"/></p>
	<p><font>状态：</font><input type="checkbox" name="status" id="wsh" value="0"/><label for="wsh">未审核</label>
	    <input  name="status" type="checkbox" id="sh" value="1"/><label for="sh">已审核</label>
	    <input type="checkbox" name="status" id="gz" value="3"/><label for="gz">已过账</label>
	    <input type="checkbox" name="status" id="zf" value="2"/><label for="zf">已作废</label>
	</p>
	<br/>
	<p>
	    <font></font><a href="javascript:;" class="btn-blue4 btn-s" id="btn-search">查询</a>
        <a href="javascript:;" class="btn-blue4 btn-s" onclick="cleanInput()">清空</a>
        <!-- <a href="javascript:;" class="btn-blue4 btn-s">导出</a> -->
        <a href="javascript:;" class="btn-blue4 btn-s open-column-win" _flag=add>新增栏目</a>
	    <a href="javascript:;" class="btn-blue4 btn-s open-column-win"_flag=edit>编辑栏目</a>
	    <a href="javascript:;" class="btn-blue4 btn-s del-column">删除栏目</a>
	</p>
</form>

<div class="table-area" >
<iframe id="table-iframe" frameborder="0" width="100%" height="410" style="overflow: auto;" src="${ctx}/multiColumn/report/toList"></iframe>
</div>

<div id="column-win"></div>

</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script>
var comboOPT = {required:true,width:'160',height:'30',panelHeight:'auto',panelMaxHeight:300};
function $ex(newVal,oldVal){
	if(!newVal)newVal = {};
	if(!oldVal)oldVal = {};
	return $.extend({},oldVal,newVal);
}
function get$Opt(newVal){
	if(!newVal)newVal = {};
	return $ex(newVal,comboOPT);
}
function columnSelect(){
	var val = ($("#column").next())[0].comboObj.getSelectedValue();
	if(!val||val.length==0){
		/* $("#column").combobox({missingMessage:'请先选择栏目'}) */
		$(($("#column").next())[0].comboObj.getInput()).validatebox('enableValidation');
		return undefined;
	}
	return val;
}
function getSearchData(){
	var options = $("#search-form").serializeJson();
	var _status = "";
	if(options&&options.status){  
		for(var i in options.status){
			_status+=options.status[i]+",";
		}
	}
	//alert(_status);
	
	/*$('input[name="status"]:checked').each(function(){ 
		_status+=$(this).val(); 
	});*/
	if(_status.length!=0)_status = _status.substring(0, _status.length-1);
	else _status = "";
	options = $ex({status:_status},options); 
	return options;
}
$(function(){
	$("#column-win").fool('window',{width:620,height:500,closed:true});
	//删除栏目
	$("a.del-column").click(function(){
		var _fid = columnSelect();
		if(_fid){
			$.fool.confirm({
	    		  msg:'确定删除该记录？',
	    		  fn:function(r){
	    			  if(r){
	    				 $.post('${ctx}/multiColumnSetting/delete',{fid:_fid},function(data){
	    					 if(data.returnCode =='0'){
	    						 $.fool.alert({
		    						  msg:'删除成功！'
		    					  });
		    					  $("#column").next()[0].comboObj.setComboText("")
		    					  $("#column").next()[0].comboObj.clearAll(false);
		    					  $("#column").next()[0].comboObj.addOption(getComboData('${ctx}/multiColumnSetting/list'));
		    					  /* $("#column").combobox('reload'); */
	    					 }else{
	    						 $.fool.alert({
		    						  msg:'删除失败，请稍后再试！'
		    					  });
	    					 }
	    		    	  });
	    				  
	    			  }
	    		  }
	    	  });
		}		
		return false;
	});
	
	$("a.open-column-win").click(function(){
		var _t = $(this);
		var _flag = _t.attr('_flag');
		var _fid='';
		if(_flag=='add'){
			
		}else if(_flag=='edit'){
			val = columnSelect();
			if(columnSelect()){
				_fid = val;
			}else{
				return false;
			}
		}
		$("#column-win").window("open");
		$("#column-win").window("setTitle",_t.text());
		$("#column-win").window("refresh","${ctx}/multiCcolumn/edit?fid="+_fid+"&_flag="+_flag);
		return false;
	});
	
	
	$("#search-form").find("input[_class]").each(function(i,n){inputInit($(this));});
	
	/* $("#column").combobox(get$Opt({
		url:'${ctx}/multiColumnSetting/list',
		valueField:'fid',    
	    textField:'name',
	    required:true,
	    novalidate:true,
	    editable:false,
	})); */
	function BindColCombo() {
        $('#column').fool("dhxCombo",get$Opt({
            data:getComboData('${ctx}/multiColumnSetting/list'),
            required:true,
            novalidate:true,
            editable:false,
            focusShow:true,
            setTemplate:{
                input:"#name#",
                option:"#name#"
            },
            onLoadSuccess:function(){
                ($("#column").next())[0].comboObj.deleteOption("")
            },
        }));
    }
    BindColCombo();
	
	/* $("#startTime").combobox(get$Opt({
		url:'${ctx}/fiscalPeriod/getAll',
		valueField:'fid',    
	    textField:'period',
	    required:true,
	    novalidate:true,
	    editable:false,
	    onSelect:function(record){
	    	$("#startTime").attr("enddate",record.endDate);
	    }
	})); */
	var periodData=getComboData('${ctx}/fiscalPeriod/getAll?defaultSelect=2');
	var defultPeriod="";
	for(var i=0;i<periodData.length;i++){
		if(periodData[i].text&&periodData[i].text.isChecked==1){
			defultPeriod=periodData[i].value;
			break;
		}
	}
	$('#startTime').fool("dhxCombo",get$Opt({
		data:periodData,
	    required:true,
	    novalidate:true,
	    editable:false,
	    focusShow:true,
	    value:defultPeriod,
	    setTemplate:{
			input:"#period#",
			option:"#period#"
		},
		onSelectionChange:function(){
			$("#startTime").attr("enddate",($('#startTime').next())[0].comboObj.getSelectedText().endDate);
		},
	}));
	
	
	/* $("#endTime").combobox(get$Opt({
		url:'${ctx}/fiscalPeriod/getAll',
		valueField:'fid',    
	    textField:'period',
	    required:true,
	    novalidate:true,
	    editable:false,
	    validType:'compare["#startTime","#endTime"]',
	    onSelect:function(record){
	    	$("#endTime").attr("enddate",record.endDate);
	    }
	})); */
	$('#endTime').fool("dhxCombo",get$Opt({
		data:periodData,
	    required:true,
	    novalidate:true,
	    editable:false,
	    focusShow:true,
	    value:defultPeriod,
	    validType:'compare["#startTime","#endTime"]',
	    setTemplate:{
			input:"#period#",
			option:"#period#"
		},
		onSelectionChange:function(){
			$("#endTime").attr("enddate",($('#endTime').next())[0].comboObj.getSelectedText().endDate);
		},
	}));
	
	$("#btn-search").click(function(){
		if(!validTime("startTime","请选择开始时间") || !validTime("endTime","请选择结束期间"))return;
        var star=$("#startTime").attr("enddate");
        var end=$("#endTime").attr("enddate");
        var starStr=star.split("-").join("");
        var endStr=end.split("-").join("");
        if(parseInt(starStr)>parseInt(endStr)){
        	$.fool.alert({msg:'开始期间必须在结束期间之前'});
			return false;
        }
		var _column = columnSelect();
		if(!_column){
			$.fool.alert({msg:'请选择栏目'});
			return false;
		}
		$("#table-iframe").attr({src:"${ctx}/multiColumn/report/toList?settingId="+_column+"&cde="+getSearchData()});
		//alert("查询");
		return false;
	});
});

function validTime(id,msg){
	var _startTime = ($("#"+id).next())[0].comboObj.getSelectedValue();
	if(!_startTime){
		$(($("#"+id).next())[0].comboObj.getInput()).validatebox("enableValidation");
		$.fool.alert({'msg':msg});
		return false;
	}
	return true;
}

function cleanInput(){
	$("#search-form").find(":checkbox").prop("checked",false);
	($("#column").next())[0].comboObj.setComboText("");
}
</script>