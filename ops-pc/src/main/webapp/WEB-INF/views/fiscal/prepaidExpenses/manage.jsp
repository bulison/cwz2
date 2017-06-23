<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>待摊费用</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
.myform p.hideOut{
    display: none;
}
/* .form1{
	padding:5px 0 0 0;
}
.form1 p{
	margin:5px 0px;
}
.form1 p font{
	width:115px;
}
.form1 .easyui-validatebox{
	width:158px;
	height:26px;
}
#btnBox{
  text-align: center;
} */
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>待摊费用</h1>
                </div>             
             </div>
  <div class="toolBox">
    <div class="toolBox-button" style="margin-right:5px;">
      <a href="javascript:;" id="add" class="btn-ora-add">新增</a>
    </div>
  </div>
  <table id="cardList"></table>
  <div id="pager"></div>
  <div id="addBox"></div>
</body>

<script type="text/javascript">
$("#cardList").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/prepaidExpenses/query',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#cardList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,
	height:"100%",
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
                {name : 'expensesCode',index : '费用编号',hidden:true},
                {name : 'expensesName',label : '费用名称',align:'center',width:"100px"}, 
                {name : 'expensesAmount',label : '待摊金额',align:'center',width:"100px"}, 
                {name : 'discountPeriod',label : '计提期数',align:'center',width:"100px"}, 
                {name : 'recordStatus',label : '状态',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	switch(cellvalue){
            		case 0: return '未审核';break;
            		case 1: return '审核';break;
            		}
                }}, 
                
                {name:'action',label:'操作',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	var e = '<a class="btn-edit" title="修改" href="javascript:;" onclick="editer(\''+rowObject.fid+'\')"></a> ';
                	var d = '<a class="btn-del" title="删除" href="javascript:;" onclick="deleterList(\''+rowObject.fid+'\')"></a>';
                	var c = '<a class="btn-approve" title="审核" href="javascript:;" onclick="checker(\''+rowObject.fid+'\')"></a>';
                	var uc = '<a class="btn-cancel" title="反审核" href="javascript:;" onclick="unchecker(\''+rowObject.fid+'\')"></a>';
                	switch(rowObject.recordStatus){
                	case 0: return e+c+d;break;
   			  	 	case 1: return e+uc;break;
                	}
                }}
              ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

$('#add').click(function(){
	$('#addBox').window({
		title:'新增待摊费用',
		top:10+$(window).scrollTop(),  
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/prepaidExpenses/edit',
		modal:true,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},onClose:function(){
			$("html").css("overflow","");
		}
	});
});

function editer(fid){
	$('#addBox').window({
		title:'修改待摊费用',
		top:10+$(window).scrollTop(), 
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/prepaidExpenses/edit?fid='+fid,
		modal:true,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},onClose:function(){
			$("html").css("overflow","");
		}
	});
}

function deleterList(id){
	var success=false;
	$.fool.confirm({
		title:'删除提示',
		msg:'确认删除此数据？',
		fn:function(data){
			if(data){
				$.ajax({
				    url: "${ctx}/prepaidExpenses/delete?fid="+id,
				    type: "POST",
				}).done(function(data) {
					if(data.returnCode==0){
						$.fool.alert({msg:'删除成功！',fn:function(){
							$('#cardList').trigger("reloadGrid"); 
						},time:2000});
						success=true;
					}else if(data.returnCode==1){
						$.fool.alert({msg:data.message});
					}else{
						$.fool.alert({msg:'系统繁忙，请稍后再试。'});
					}
				});
			}else{
				return false;
			}
		}
	});
	return success;
}
function checker(id){
	var success=false;
	$.ajax({
	    url: "${ctx}/prepaidExpenses/passAudit?fid="+id,
	    type: "POST",
	    async:false,
	    success:function(data){
	    	if(data.returnCode==0){
				$.fool.alert({msg:'审核完成！',fn:function(){
					$('#cardList').trigger("reloadGrid"); 
				},time:2000});
				success=true;
			}else if(data.returnCode==1){
				$.fool.alert({msg:data.message});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
	    }
	});
	return success;
}
function unchecker(id){
	var success=false;
	$.ajax({
	    url: "${ctx}/prepaidExpenses/cancleAudit?fid="+id,
	    type: "POST",
	    async:false,
	    success:function(data){
	    	if(data.returnCode==0){
				$.fool.alert({msg:'反审核完成！',fn:function(){
					$('#cardList').trigger("reloadGrid"); 
				},time:2000});
				success=true;
			}else if(data.returnCode==1){
				$.fool.alert({msg:data.message});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
	    }
	});
	return success;
}
</script>
</html>