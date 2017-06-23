<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="xsyj" scope="page"></c:set>
<c:set var="billCodeName" value="现金盘点单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
</head>
<body>
<div class="titleCustom">
   <div class="squareIcon">
      <span class='Icon'></span>
      <div class="trian"></div>
      <h1>现金盘点单</h1>
   </div>             
</div>
 <div class="btn" style="margin-bottom: 10px;">
    <a href="javascript:;" id="addlist" class="btn-ora-add">新增</a> 
 </div>
  <table id="Tablelist"></table>
  <div id="pager"></div>
  <div id="addBox"></div>
<script type="text/javascript">
$(function(){//信息列表
	$("#Tablelist").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: getRootPath()+'/cashCheck/list',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#Tablelist")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		autowidth:true,
		height:$(window).outerHeight()-200+"px",
		pager:"#pager",
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[
                    {name:'fid',label:'fid',align:'center',hidden:true},
                    {name:'subjectName',label:'科目',align:'center',width:"100px",formatter:function(value,options,row){     	  			
        	  			var e= '<a title="编辑" href="javascript:;" onclick="compile(this,\''+row.fid+'\',1)">'+value+'</a>'
        	  			return e ;
        	  		}},
                    {name:'date',label:'日期',align:'center',width:"100px"},
                    {name:'amount',label:'金额',align:'center',width:"100px"},
                    {name:'creatorName',label:'创建人',align:'center',width:"100px"},    
                    {name:'createTime',label:'创建时间',align:'center',width:"100px"}, 
                    {name:'resume',label:'备注',align:'center',width:"100px"},
                    {name:'action',label:'操作',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                		//var e= '<a class="btn-edit" title="编辑" href="javascript:;" onclick="compile(this,\''+rowObject.fid+'\',1)"></a>'; 
                		var r = "<a href='javascript:;' class='btn-del' onclick='deleteRow(\""+options.rowid+"\",\""+rowObject.fid+"\")' title='删除'></a>";		
                		return r;		
                    }},
	              ],
	    onLoadSuccess:function(){//列表权限控制
	    	warehouseAll();
	    },
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 
    
	
    
	//新增
	$('#addlist').click(function(){
		var url=getRootPath()+'/cashCheck/editCashCheck?flag=add';	
		warehouseWin("新增现金盘点单",url);
		if($('#addBox').is(":visible")==true){
			$('html').css('overflow','hidden');
		}
	});
});

//编辑
function compile(target,fid,mark){
	/* var _url=getRootPath()+'/cashCheck/checkDate?fid='+fid;
	$.post(_url,function(data){	
	  if(data.result=='0'){ */
		  var url=getRootPath()+'/cashCheck/editCashCheck?mark=2&fid='+fid+'&flag=edit';	
			warehouseWin("修改现金盘点单",url);
			if($('#addBox').is(":visible")==true){
				$('html').css('overflow','hidden');
			}   
			/* } else{
		  var url=getRootPath()+'/cashCheck/editCashCheck?mark=1&fid='+fid+'&flag=detail';	
			warehouseWin("查看现金盘点单",url);
			if($('#addBox').is(":visible")==true){
				$('html').css('overflow','hidden');
			}     
	  } 
		 
	});	*/	
}
  
function updateActions(index){
	$('#datalist').datagrid('updateRow',{
		index: index,
		row:{}
	});
}

//删除
function deleteRow(index,fid){
	var _url=getRootPath()+'/cashCheck/delete?fid='+fid;
	$.fool.confirm({title:'提示',msg:"确认删除记录？",fn:function(data){
		if(data){	
			$.post(_url,function(data){	
				dataDispose(data);
				if(data.returnCode == '0'){
				    $("#Tablelist").trigger("reloadGrid"); 
				    $.fool.alert({time:1000,msg:'删除成功'});
				}else if(data.returnCode = '1'){
					$.fool.alert({msg:data.message});
				}else{
					$.fool.alert({msg:'服务器繁忙，请稍后再试'});
				}
			},'json');
		}else{
			return false;
		}
	}});
}
</script>
</body>
</html>
