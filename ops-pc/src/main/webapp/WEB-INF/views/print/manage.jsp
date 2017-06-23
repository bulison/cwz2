<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<title>打印配置</title>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>打印配置</h1>
                </div>             
             </div>
<a href="javascript:;" id="addBtn" class="btn-ora-add" onclick="editer('add')" style="margin-bottom:10px;">新增</a> 
<div id="edBox"></div>
<div id="container">
  <table id="list">
  </table>
  <div id="pager"></div>
</div>
  <script type="text/javascript">
    $("#list").jqGrid({
    	datatype:function(postdata){
    		$.ajax({
    			url:"${ctx}/printTempController/list",
    			data:postdata,
    	        dataType:"json",
    	        complete: function(data,stat){
    	        	if(stat=="success") {
    	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
    	        		$("#list")[0].addJSONData(data.responseJSON);
    	        	}
    	        }
    		});
    	},
    	autowidth:true,//自动填满宽度
    	height:"100%",
    	mtype:"post",
    	pager:'#pager',
    	rowNum:10,
    	rowList:[ 10, 20, 30 ],
    	jsonReader:{
    		records:"total",
    		total: "totalpages",  
    	}, 
    	viewrecords:true,
    	forceFit:true,//调整列宽度，表格总宽度不会改变
    	height:$(window).outerHeight()-200+"px",
    	colModel:[
                  {name:'fid',hidden:true},
                  {name:'orgName',label:'机构名称',align:"center",sortable:false,width:100},
                  {name:'printTempUrl',label:'模板路径',align:"center",sortable:false,width:100},
                  {name:'typeName',label:'模板类型',align:"center",sortable:false,width:100},
                  {name:'pageRow',label:'行数',align:"center",sortable:false,width:100},
                  {name:'action',label:'操作',align:"center",sortable:false,width:100,formatter:function(value,options,row){
                	  var e= '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editer(\'edit\',\''+row.fid+'\')"></a>'; 
					  var d= '<a class="btn-del" title="删除" href="javascript:;" onclick="deleter(\''+row.fid+'\')"></a>';
					  var down= '<a class="btn-backup" title="下载模板" href="${ctx}/printTempController/download?id='+row.fid+'"></a>';
					  if(row.defaultFlag==1){
						  return e+" "+down;
					  }else{
						  return e+"  "+down+" "+d;
					  };
                  }}
                  ]
    }).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
    
    //编辑窗口
    $("#edBox").window({
    	  collapsible:false,
    	  minimizable:false,
    	  maximizable:false,
    	  closed:true,
    	  modal:true,
    	  top:"20%",
    	  left:"35%",
    	  width:400
    });
    
    function editer(flag,fid){
    	var title="";
    	var url="";
    	if(flag=="add"){
    		title="新增模板";
    		url="${ctx}/printTempController/add";
    	}else if(flag=="edit"){
    		title="修改模板";
    		url="${ctx}/printTempController/edit?fid="+fid;
    	}
    	$("#edBox").window("setTitle",title);
    	$("#edBox").window("open");
    	$("#edBox").window("refresh",url);
    };
    
    function deleter(fid){
    	$.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
    		if(r){
    			$.ajax({
    				type : 'post',
   					url : '${ctx}/printTempController/delete',
   					data : {id : fid},
   					dataType : 'json',
   					success : function(data) {
   						dataDispose(data);
   						if(data.result == '0'){
   							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){$('#list').trigger('reloadGrid');}});
   						}else{
   							$.fool.alert({msg:data.msg});
   						}
   		    		},
   		    		error:function(){
   		    			$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
   		    		}
    			});
    		}
    	}});
    };
  </script>
</body>
</html>
