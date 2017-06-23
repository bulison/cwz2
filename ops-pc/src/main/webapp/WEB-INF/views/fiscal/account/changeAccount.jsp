<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>改变账套</title>
</head>
<body>
    <table id="dataTable">
	</table>
	<div id="pager"></div>
	 <div class="form1" id="btnBox" style="text-align: center; ">
	 <p><input type="button" onclick="changeAccount();" class="btn-blue btn-s" value="确定" /></p>
     </div>
	
	<script type="text/javascript">
	$(function(){
		/* $('#dataTable').datagrid(); */
		/* setPager($('#dataTable')); */
		$("#dataTable").jqGrid({
			datatype:function(postdata){
				$.ajax({
					url: '${ctx}/fiscalAccount/list',
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#dataTable")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			},
			/* footerrow: true, */
			autowidth:true,
			height:380,
			pager:"#pager",
			rowNum:10,
			rowList:[10,20,30],
			viewrecords:true,
			jsonReader:{
				records:"total",
				total: "totalpages",  
			},  
			colModel:[ 
		                {name : 'fid',label : 'fid',hidden:true}, 
		                {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' fid='"+rowObject.fid+"' fname='"+rowObject.name+"' type='checkbox' onclick='checker(this)' />"; }},
		                {name:'name',label:'账套名',align:'center',width:"100px"},
		                {name:'defaultFlag',label:'是否默认登录',align:'center',width:"100px",formatter:status},
		                {name:'description',label:'描述',align:'center',width:"100px"},
		              ],
		    ondblClickRow:function(rowid,iRow,iCol,e){
		    	$("#dataTable").find("#"+rowid).find(".checker").prop("checked",true);
		    	changeAccount();
		    }
		}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 
	});
	
	function changeAccount(){
		var row=$(".checker[type=checkbox]:checked");
		var fid = row.attr("fid");
		var name=row.attr("fname");
		if(fid){
			$('#save').attr("disabled","disabled");
			$.post('${ctx}/fiscalAccount/changeAccount',{"accountId":fid},function(data){
				dataDispose(data);
		    	if(data.result=='0'){
		    		$('#save').removeAttr("disabled");
		    		$.fool.alert({time:2000,msg:"切换成功！",fn:function(){
		    			$("#index_FiscalAccountName").text(name);
		    			$("#fiscalAccountWindows").window("close");
		    			location.reload(); 
		    		}});
		    	}else{
		    		$.fool.alert({msg:data.msg});
		    		$('#save').removeAttr("disabled");
	    		}
		    	return true;
		    });
		}
	}
	function status(value){
		return value=="1"?"是":"否";
	}
	function checker(target){
		$(".checker").prop('checked',false);
		$(target).prop('checked',true);
	}
	</script>
</body>
</html>
