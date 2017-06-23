<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>填制凭证</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/billModel.css" />
<style>
.subj{float:left;border: 1px #878787 solid;height: 90%;border-radius: 3px;background: #999999;color: #fff;}
.subj img{vertical-align: middle;width: 15px;margin-top: 1px;}
.subj div{float:left;}
#btnBox{
  margin:0px auto;
  text-align: center;
}
#signBox p.hideOut,.list2 p.hideOut,.list2 p.hideOut{
  display: none;
}
#signBox p.hideOut{
	font-size:12px;
}
#detailBox{
  overflow: auto;
}
#searchform .textBox{
  width:160px;
  height: 30px;
  padding: 0px
}
.form1{padding:0 0}
.form1 #searchform p{margin:5px 0px}
.form #searchform p font,.form1 #searchform p font{width:80px;}
#detailList{
  width:100%;
}
#gridBox{
  /* text-align: center; */
  width:100%
}
#gridBox .datagrid{
  margin:10px auto;
}
#gridBox .datagrid .datagrid-body{
  max-height: 200px;
  overflow: auto;
}

#signBox{
  margin:0px auto;
  /* border:1px solid gray; */
  display: block;
  width:95%;
}
#signBox p{
  text-align:center;
  display: inline-block;
  width:15%
}
#signBox p font{
  width:60px;height:30px;line-height:30px;text-align:right;display:inline-block;margin:0px;padding:0px;
}
.hideOut{
  display: none;
}

.datagrid .datagrid-pager{
  margin:0px
}
#btns a{
	margin-right:5px;
}
.status{
	background: none !important;
}
/*未审核*/
.status0{
	background: url("/ops-pc/resources/images/billstatus-0.png") no-repeat 320px 0%;
}
/*已审核*/
.status1{
	background: url("/ops-pc/resources/images/billstatus-1.png") no-repeat 320px 0%;
}
/*已作废*/
.status2{
	background: url("/ops-pc/resources/images/billstatus-2.png") no-repeat 320px 0%;
}
/*已过账*/
.status3{
	background: url("/ops-pc/resources/images/billstatus-3.png") no-repeat 320px 0%;
}
</style>
<%
	String Fid = request.getParameter("fid");
%>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>填制凭证</h1>
                </div>             
             </div>
	<div id="btns" style="margin: 10px 0px;"> 
	<fool:tagOpt optCode="voucherAdd"><a href="javascript:;" id="add" class="btn-ora-add" >新增</a></fool:tagOpt>
	<fool:tagOpt optCode="voucherAdd"><a href="javascript:;" id="add2" class="btn-ora-add" style="display:none;">新增测试</a></fool:tagOpt>
	<fool:tagOpt optCode="voucherPass"><a href="javascript:;" id="passMore" class="btn-ora-batch" >批审</a></fool:tagOpt>
	<fool:tagOpt optCode="voucherPost"><a href="javascript:;" id="post" class="btn-ora-introduce" >过账</a></fool:tagOpt>
	<fool:tagOpt optCode="voucherRpost"><a href="javascript:;" id="reversePost" class="btn-ora-recovery" >反过账</a></fool:tagOpt>
	</div>
	<div class="form1" >
              <form id="searchform">
               <!--  <input id="creatorId-search" name="creatorId" class="textBox" type="hidden"/>
                <input id="auditorId-search" name="auditorId" class="textBox" type="hidden"/>
                <input id="postPeopleId-search" name="postPeopleId" class="textBox" type="hidden"/> -->
                <p><font>凭证号：</font><input id="voucherNumberStr-search" name="startVoucherNumber" class="textBox"/> 至 <input id="voucherNumberEnd-search" name="endVoucherNumber" class="textBox"/></p>
                <input id="subjectId-search" name="subjectId" class="textBox" type="hidden"/>
                <input id="voucherWordId-search" name="voucherWordId" class="textBox" type="hidden"/>
				<p><font>凭证字：</font><input id="voucherWordName-search" name="voucherWordName" class="textBox"/></p>
				<p><font>科目：</font><input id="subjectName-search" name="subjectName" class="textBox"/></p>
				<p><font>摘要：</font><input id="resume-search" name="resume" class="textBox"/></p>
				<br/>
				<!-- <p><font>制单人：</font><input id="creatorName-search" name="creatorName" class="textBox"/></p>
				<p><font>审核人：</font><input id="auditorName-search" name="auditorName" class="textBox"/></p>
				<p><font>过账人：</font><input id="postPeopleName-search" name="postPeopleName" class="textBox"/></p>
				<p><font>附单据：</font><input id="accessoryNumber-search" name="accessoryNumber" class="textBox"/></p><br/> -->
				<p><font>凭证日期：</font><input id="createTimeStr-search" name="startDay" class="textBox"/> 至 <input id="createTimeEnd-search" name="endDay" class="textBox" /></p>
				<p><font>状态：</font><input id="recordStatus-search" name="recordStatus" class="textBox"/></p>
				<p><fool:tagOpt optCode="voucherSearch"><font></font><a id="searchBtn" class="btn-blue btn-s" style="margin-right:5px;">筛选</a></fool:tagOpt> <a href="javascript:;" id="clear" class="btn-blue btn-s">清空</a></p>
              </form>
    </div>
    <table id="voucherList"></table>
    <div id="pager"></div>
	<div id="addBox"></div> 
	<div id="searchBox"></div>
	<div id="passBox"></div>
	<div id="postBox"></div>
	<div id="reversePostBox"></div>
	
<script type="text/javascript">
var times=1;
var recordStatus=[{value:"0",text:'草稿单'},{value:"1",text:'已审核'},{value:"2",text:'已作废'},{value:"3",text:'已过账'}];

var fid = '<%=Fid%>';
var rowNum = 10;
var pgChos = [];
var rowChos = [];
var texts = [
             {title:'',key:'voucherDate'},
             {title:'凭证字',key:'voucherWordName'},
             {title:'凭证号',key:'voucherWordNumber'},
             {title:'附件数',key:'accessoryNumber'},
             {title:'顺序号',key:'number'},
             {title:'状态',key:'recordStatus'}
             ];
var thead = [
             {title:'摘要',key:'resume',width:25},
             {title:'科目',key:'subjectName',width:25},
             {title:'借方',key:'debitAmount',width:15,textAlign:"right"},
             {title:'贷方',key:'creditAmount',width:15,textAlign:"right"},
             ];
var tfoot = [
				{dtype:5,key:'debitAmount',text:'合计:#',width:"70",align:"left"},
				{dtype:2,key:'debitAmount',text:'#',width:"15",align:"right"},
				{dtype:2,key:'creditAmount',text:'#',width:"15",align:"right"},
			];

//控件初始化
$("#voucherWordName-search").validatebox({});
var statusCombo=$('#recordStatus-search').fool("dhxCombo",{
	height:"32px",
	width:"160px",
	editable:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	focusShow:true,
	data:recordStatus,
});
$("#resume-search").validatebox({});
$("#resume-search").validatebox({});
$("#subjectName-search").validatebox({});
$("#voucherNumberStr-search").validatebox({});
$("#voucherNumberEnd-search").validatebox({});
$("#createTimeStr-search").datebox({
	width:160,
	height:32,
	editable:false,
	value:"${startVoucherDate}"
});
$("#createTimeEnd-search").datebox({
	width:160,
	height:32,
	editable:false,
	value:"${endVoucherDate}"
});
$("#debitAmountStr-search").validatebox({});
$("#debitAmountEnd-search").validatebox({});
$("#creidAmountStr-search").validatebox({});
$("#creidAmountEnd-search").validatebox({});

var voucherWordCombo=$('#voucherWordName-search').fool("dhxCombo",{
	height:"32px",
	width:"160px",
	editable:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
    toolsBar:{
    	name:"凭证字",
        refresh:true
    },
	focusShow:true,
	data:getComboData(getRootPath()+'/basedata/voucherWord',"tree"),
});
//科目选择框操作方法
$("#subjectName-search").click(function(){
	chooserWindow_search=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubjectSearch&onDblClick=selectSubjectDBCSearch&singleSelect=true'});
});
function selectSubjectSearch(rowData) {
	$("#subjectName-search").val(rowData[0].name);
	$("#subjectId-search").val(rowData[0].fid);
	chooserWindow_search.window('close');
}
function selectSubjectDBCSearch(rowData) {
	$("#subjectName-search").val(rowData.name);
	$("#subjectId-search").val(rowData.fid);
	chooserWindow_search.window('close');
}

//清空按钮店家事件
$("#clear").click(function(){
	$("#searchform").form("clear");
	($("#recordStatus-search").next())[0].comboObj.setComboValue("");
	($("#recordStatus-search").next())[0].comboObj.setComboText("");
	($("#voucherWordName-search").next())[0].comboObj.setComboValue("");
	($("#voucherWordName-search").next())[0].comboObj.setComboText("");
});

//筛选按钮点击事件
$('#searchBtn').click(function(e) {
	var voucherWordId=($("#voucherWordName-search").next())[0].comboObj.getSelectedValue();
	var recordStatus=($("#recordStatus-search").next())[0].comboObj.getSelectedValue();
	if(recordStatus==null){
		recordStatus="";
	}
	var subjectId=$("#subjectId-search").val();
	var startVoucherNumber=$("#voucherNumberStr-search").val();
	var endVoucherNumber=$("#voucherNumberEnd-search").val();
	var createTimeStr=$("#createTimeStr-search").datebox('getValue');
	var createTimeEnd=$("#createTimeEnd-search").datebox('getValue');
	var voucherResume=$("#resume-search").val();
	if($('#searchform').form('validate')){
        pgChos= [];
        rowChos= [];
		$('#voucherList').jqGrid("setGridParam",{url:"${ctx}/voucher/query",postData:{voucherResume:voucherResume,voucherWordId:voucherWordId,recordStatus:recordStatus,startVoucherNumber:startVoucherNumber,endVoucherNumber:endVoucherNumber,startDay:createTimeStr,endDay:createTimeEnd,subjectId:subjectId}}).trigger("reloadGrid");
	}else{
		return false;
	}
});

//新增按钮点击事件
$('#add').click(function(){
	$('#addBox').window({
		onClose:function(){
			if(typeof(mykd) != "undefined"){
				$(document).unbind('keydown',mykd);
			}
			times=1;
		}
	});
	$('#addBox').window('setTitle',"填制凭证");
	$('#addBox').window('open');
	$('#addBox').window('refresh',"${ctx}/voucher/add");
});
//新增按钮点击事件
$('#add2').click(function(){
	$('#addBox').window({
		onClose:function(){
			if(typeof(mykd) != "undefined"){
				$(document).unbind('keydown',mykd);
			}
			times=1;
		}
	});
	$('#addBox').window('setTitle',"填制凭证");
	$('#addBox').window('open');
	$('#addBox').window('refresh',"${ctx}/voucher/add2");
});

//批审按钮点击事件
$('#passMore').click(function(){
	$('#passBox').window('open');
});

//过账按钮点击事件
$('#post').click(function(){
	$('#postBox').window('open');
	$("#sdd").dialog('open');
	$("#edd").dialog('close');
	$('#postBox').window('close');
});

//反过账按钮点击事件
$('#reversePost').click(function(){
	/* $('#reversePostBox').window('open'); */
	//var rows=$(".ui-state-highlight");
	var rows = [];
	for(var i  in rowChos){
		rows = rows.concat(rowChos[i]);
	}
	if(rows.length>0){
		var ids="";
		for(var i=0;i<rows.length;i++){
			ids+=rows[i].fid+",";
		}
		$.fool.confirm({title:'确认',msg:'确定要对选中的凭证进行反过账吗？',fn:function(r){
			if(r){
				$.ajax({
					type : 'post',
					url : '${ctx}/voucher/reversePostAccount',
					data : {ids : ids},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == '0'){
							$('#voucherList').trigger("reloadGrid"); 
							$.fool.alert({time:1000,msg:data.data,fn:function(){ 
								$('#voucherList').resetSelection(); 
							}});
						}else{
							$.fool.alert({msg:data.message});
						}
					}, 
					error:function(){
						$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
					}
				});
			}
		}});
	}else{
		$.fool.alert({time:1000,msg:"请先勾选凭证。"});
	}
});

//编辑按钮点击事件 
function editById(fid,mark){
	$('#addBox').window({
		onClose:function(){
			if(typeof(mykd) != "undefined"){
				$(document).unbind('keydown',mykd);
			}
			times=1;
			if(_myid!='' || _myfid!=''){
				var tab = parent.$('#tabs').tabs('getTab','填制凭证');
				tab.find('iframe').attr('src','${ctx}/voucher/manage');
			}
			$('html').css('overflow-x','hidden');
			$('html').css('overflow-y','auto');
		}
	});
	if(mark==1){
		$('#addBox').window('setTitle',"查看凭证");
	}else{
		$('#addBox').window('setTitle',"修改凭证");
	}
	$('#addBox').window('open');
	$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
}

//审核按钮点击事件
function verifyById(fid){
	$.fool.confirm({title:'确认',msg:'确定要审核通过此数据吗？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/voucher/audit',
				data : {id : fid},
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
							$('#addBox').window("close");
						}});
					}else{
						$.fool.alert({msg:data.message,fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
						}});
					}
				},
				error:function(){
					$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
				}
			});
		}
	}});
}

//反审核按钮点击事件
function unVerifyById(fid){
	$.fool.confirm({title:'确认',msg:'确定要反审核此数据吗？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/voucher/reverseAudit',
				data : {id : fid},
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'反审核成功！',fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
							$('#addBox').window("close");
						}});
					}else{
						$.fool.alert({msg:data.message,fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
						}});
					}
				},
				error:function(){
					$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
				}
			});
		}
	}});
}

//作废按钮点击事件
function cancelById(fid){
	$.fool.confirm({title:'确认',msg:'确定要作废此数据吗？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/voucher/cancel',
				data : {id : fid},
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'作废成功！',fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
							$('#addBox').window("close");
						}});
					}else{
						$.fool.alert({msg:data.message,fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
						}});
					}
				},
				error:function(){
					$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
				}
			});
		}
	}});
}

//反作废按钮点击事件
function unCancelById(fid){
	$.fool.confirm({title:'确认',msg:'确定要反作废此数据吗？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/voucher/reverseCancel',
				data : {id : fid},
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'反作废成功！',fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
							$('#addBox').window("close");
						}});
					}else{
						$.fool.alert({msg:data.message,fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
						}});
					}
				},
				error:function(){
					$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
				}
			});
		}
	}});
}

//签字按钮点击事件
function signById(fid){
	$.fool.confirm({title:'确认',msg:'确定要签字吗？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/voucher/signature',
				data : {id : fid},
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'签字成功！',fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
							$('#addBox').window("close");
						}});
					}else{
						$.fool.alert({msg:data.message,fn:function(){
							$('#voucherList').trigger("reloadGrid"); 
						}});
					}
				},
				error:function(){
					$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
				}
			});
		}
	}});
}

//删除按钮点击事件
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除此数据吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/voucher/delete',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#voucherList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#voucherList').trigger("reloadGrid"); 
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//冲销按钮点击事件
function writeOffById(fid){
	$('#addBox').window('setTitle',"新增凭证");
	$('#addBox').window('open');
	$('#addBox').window('refresh',"${ctx}/voucher/writeOff?mark=1&fid="+fid);
}

//新增、修改、详情窗口初始化
$('#addBox').window({
	top:10,  
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	modal:true,
	width:$(window).width()-10,
	height:$(window).height()-60,
});

//批审窗口初始化
$('#passBox').window({
	title:"批量审核",
	href:"${ctx}/voucher/batchAuditUI",
	top:150,  
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	modal:true,
	width:$(window).width()-650,
});

//过账窗口初始化
$('#postBox').window({
	href:"${ctx}/voucher/post",
	top:150,  
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	modal:true,
	width:$(window).width()-650,
});

var postData={startDay:'${startVoucherDate}',endDay:'${endVoucherDate}'};
<% if(Fid != null){ %>
postData.fid=fid,
<% } %>
$("#voucherList").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/voucher/query',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#voucherList")[0].addJSONData(data.responseJSON);
	        		$(".cbox").attr("title","请勾选已过帐凭证进行反过账。") 
	        	}
	        }
		});
	},
	postData:postData,
	footerrow: false,
	autowidth:true,
	height:400,
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	multiselect:true,
	colModel:[ 
                {name : 'fid',label :"fid",hidden:true}, 
                {name:'voucherNumber',label:'凭证号',hidden:true},
    	  		{name:'voucherWord',label:'凭证字',hidden:true},
    	  		/* {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' fid='"+rowObject.fid+"' type='checkbox' title='勾选已过账的记录进行反过账' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }}, */
    	  		{name:'voucherWordNumber',label:'凭证字号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    	  			var result="";
    	  			if(rowObject.recordStatus==0){
    	  				<fool:tagOpt optCode="voucherAction1">result='<a href="javascript:;" onclick="editById(\''+rowObject.fid+'\')" >'+cellvalue+'</a>';</fool:tagOpt>
    				}else if(rowObject.recordStatus==1){
    					<fool:tagOpt optCode="voucherAction2">result='<a href="javascript:;" onclick="editById(\''+rowObject.fid+'\',1)" >'+cellvalue+'</a>';</fool:tagOpt>
    				}else if(rowObject.recordStatus==2){
    					<fool:tagOpt optCode="voucherAction2">result='<a href="javascript:;" onclick="editById(\''+rowObject.fid+'\',1)" >'+cellvalue+'</a>';</fool:tagOpt>
    				}else if(rowObject.recordStatus==3){
    					<fool:tagOpt optCode="voucherAction2">result='<a href="javascript:;" onclick="editById(\''+rowObject.fid+'\',1)" >'+cellvalue+'</a>';</fool:tagOpt>
    				}
    	  			if(result==""){
    	  				result=cellvalue;
    	  			}
    	  			return result;
    	  		}},
    	  		{name:'voucherResume',label:'摘要',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(rowObject.recordStatus==0){ 
    					return typeof(cellvalue) == "undefined"?'':'<span style="color:black;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==1){
    					return typeof(cellvalue) == "undefined"?'':'<span style="color:green;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==2){
    					return typeof(cellvalue) == "undefined"?'':'<span style="color:blue;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==3){
    					return typeof(cellvalue) == "undefined"?'':'<span style="color:orange;">'+cellvalue+'</span>';
    				}else{
    					return cellvalue;
    				}
    			}},
    			{name:'voucherDate',label:'凭证日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(rowObject.recordStatus==0){
    					return '<span style="color:black;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==1){
    					return '<span style="color:green;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==2){
    					return '<span style="color:blue;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==3){
    					return '<span style="color:orange;">'+cellvalue+'</span>';
    				}else{
    					return cellvalue;
    				}
    			}}, 
    			{name:'accessoryNumber',label:'附件数',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(cellvalue){
    					if(rowObject.recordStatus==0){
    						return '<span style="color:black;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==1){
    						return '<span style="color:green;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==2){
    						return '<span style="color:blue;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==3){
    						return '<span style="color:orange;">'+cellvalue+'</span>';
    					}else{
    						return cellvalue;
    					}
    				}else{
    					return "";
    				}
    			}},
    			{name:'number',label:'顺序号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(cellvalue){
    					if(rowObject.recordStatus==0){
    						return '<span style="color:black;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==1){
    						return '<span style="color:green;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==2){
    						return '<span style="color:blue;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==3){
    						return '<span style="color:orange;">'+cellvalue+'</span>';
    					}else{
    						return cellvalue;
    					}
    				}else{
    					return"";
    				}
    			}},
    			{name:'creatorName',label:'创建人',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(rowObject.recordStatus==0){
    					return '<span style="color:black;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==1){
    					return '<span style="color:green;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==2){
    					return '<span style="color:blue;">'+cellvalue+'</span>';
    				}else if(rowObject.recordStatus==3){
    					return '<span style="color:orange;">'+cellvalue+'</span>';
    				}else{
    					return cellvalue;
    				}
    			}},
    			{name:'auditorName',label:'审核人',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(cellvalue){
    					if(rowObject.recordStatus==0){
    						return '<span style="color:black;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==1){
    						return '<span style="color:green;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==2){
    						return '<span style="color:blue;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==3){
    						return '<span style="color:orange;">'+cellvalue+'</span>';
    					}else{
    						return cellvalue;
    					}
    				}else{
    					return "";
    				}
    			}},
    			{name:'supervisorName',label:'凭证主管',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(cellvalue){
    					if(rowObject.recordStatus==0){
    						return '<span style="color:black;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==1){
    						return '<span style="color:green;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==2){
    						return '<span style="color:blue;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==3){
    						return '<span style="color:orange;">'+cellvalue+'</span>';
    					}else{
    						return cellvalue;
    					}
    				}else{
    					return "";
    				} 
    			}},
    			{name:'totalAmount',label:'金额',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(cellvalue){
    					if(rowObject.recordStatus==0){
    						return '<span style="color:black;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==1){
    						return '<span style="color:green;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==2){
    						return '<span style="color:blue;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==3){
    						return '<span style="color:orange;">'+cellvalue+'</span>';
    					}else{
    						return cellvalue;
    					}
    				}else{
    					return "";
    				}
    			}},
    			{name:'postPeopleName',label:'记账人',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(cellvalue){
    					if(rowObject.recordStatus==0){
    						return '<span style="color:black;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==1){
    						return '<span style="color:green;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==2){
    						return '<span style="color:blue;">'+cellvalue+'</span>';
    					}else if(rowObject.recordStatus==3){
    						return '<span style="color:orange;">'+cellvalue+'</span>';
    					}else{
    						return cellvalue;
    					}
    				}else{
    					return "";
    				}
    			}},
    			{name:'recordStatus',label:'状态',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    				if(cellvalue==0){
    					return '<span style="color:black;">'+"草稿单"+'</span>';
    				}else if(cellvalue==1){
    					return '<span style="color:green;">'+"已审核"+'</span>';
    				}else if(cellvalue==2){
    					return '<span style="color:blue;">'+"已作废"+'</span>';
    				}else if(cellvalue==3){
    					return '<span style="color:orange;">'+"已过账"+'</span>';
    				}else{
    					return cellvalue;
    				}
    			}},
    			<fool:tagOpt optCode="voucherAction">
    	  		{name:'action',label:'操作',sortable:false,align:'center',width:"120px",formatter:function(cellvalue, options, rowObject){
    	  			var e='',detail='',d='',a='',unA='',c='',unC='',s='',p='',w='',ex='';
    	  			<fool:tagOpt optCode="voucherAction1">e='<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction2">detail='<a class="btn-detail" title="查看" href="javascript:;" onclick="editById(\''+rowObject.fid+'\',1)"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction3">d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction4">a='<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction5">unA='<a class="btn-back" title="反审核" href="javascript:;" onclick="unVerifyById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction6">c='<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction7">unC='<a class="btn-back" title="反作废" href="javascript:;" onclick="unCancelById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction8">s='<a class="btn-initialize" title="签字" href="javascript:;" onclick="signById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction9">p='<a class="btn-printer" title="打印" href="javascript:;" onclick="printById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			<fool:tagOpt optCode="voucherAction10">w='<a class="btn-copy" title="冲销" href="javascript:;" onclick="writeOffById(\''+rowObject.fid+'\')"></a> ';</fool:tagOpt>
    	  			/* <fool:tagOpt optCode="voucherAction11">ex='<a class="btn-out" title="导出" href="javascript:;" onclick="exportById(\''+row.fid+'\')"></a> ';</fool:tagOpt> */
    	  			if(!rowObject.postPeopleName){
    	  				if(rowObject.recordStatus==0){
    		  				return d+a+c+p+w/* +ex */;
    		  			}else if(rowObject.recordStatus==1){
    		  				return unA+p+w/* +ex */;
    		  			}else if(rowObject.recordStatus==2){
    		  				return unC+p+w/* +ex */;
    		  			}else if(rowObject.recordStatus==3){
    		  				return p+w+s/* +ex */;
    		  			}
    	  			}else{
    	  				return p+w+ex;
    	  			}
    	  		}}
    	  		</fool:tagOpt>
              ],
	gridComplete:function(){
		if($('#voucherList').jqGrid("getGridParam").postData.rows != rowNum){
			pgChos= [];
			rowChos= [];
			rowNum = $('#voucherList').jqGrid("getGridParam").postData.rows;
		}else{
			var num = $('#voucherList').jqGrid('getGridParam').page;
			if(pgChos[num]){
				var rowids = pgChos[num];
				for(var i = 0; i < rowids.length; i++){
					$('#voucherList').jqGrid('setSelection',rowids[i],true);
				}
			}
		}
	},
    onSelectAll:function(aRowids,status){
    	var indexs=aRowids.concat();
    	var rowids = [];
    	var rows = [];
    	for(var i=0;i<indexs.length;i++){
    		if($("#"+indexs[i]).find("[aria-describedby='voucherList_recordStatus']").attr("title")!="已过账"){
    			$("#voucherList").setSelection(indexs[i],false);
        	}else{
        		var row = $("#voucherList").jqGrid("getRowData",indexs[i]);
    			rowids.push(indexs[i]);
    			rows.push(row);
        	}
    	}
    	var num = $('#voucherList').jqGrid('getGridParam').page;
  			pgChos[num] = rowids;
  			/* for(var i=0;i<rowids.length;i++){
  				var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
  				rows.push(row);
  			} */
  			rowChos[num] = rows;
    },
    onSelectRow:function(aRowids,status){
    	if($("#"+aRowids).find("[aria-describedby='voucherList_recordStatus']").attr("title")!="已过账"){
    		$("#voucherList").setSelection(aRowids,false);
    	}else{
    		var rowids = $('#voucherList').jqGrid('getGridParam', 'selarrrow');
      		var num = $('#voucherList').jqGrid('getGridParam').page;
      		pgChos[num] = rowids;
      		var rows = [];
      		for(var i=0;i<rowids.length;i++){
      			var row = $('#voucherList').jqGrid("getRowData",rowids[i]);
      			rows.push(row);
      		}
      		rowChos[num] = rows;
    	}
    }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

//设定分页栏
/* setPager($('#voucherList')); */
function printById(fid){
	printVoucher(fid,0);
};
var _myid = "${param.id}";
var _myfid = "${param.fid}"
if(_myid!=''){
	editById("${param.id}");
}
//点击制作凭证的链接直接弹出编辑页面
if(_myfid!=''){
	editById("${param.fid}");
}

function getComboData(url,mark){
	var result="";
	$.ajax({
		url:url,
		data:{num:Math.random()},
		async:false,
		success:function(data){
			if(data){
				result=data;
			}
		}
	});
	if(mark!="tree"){
		return formatData(result,"fid");
	}else{
		return formatTree(result[0].children,"text","id");
	}
}

function checker(target,recordStatus){
	/* var status=$(target).prop('checked'); */
	if(recordStatus!=3){
		$(target).attr("checked",false);
	}
}
</script>
</body>
</html>
